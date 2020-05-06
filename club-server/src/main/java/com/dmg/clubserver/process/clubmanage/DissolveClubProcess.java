package com.dmg.clubserver.process.clubmanage;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.clubserver.dao.*;
import com.dmg.clubserver.dao.bean.*;
import com.dmg.clubserver.model.table.Table;
import com.dmg.clubserver.model.table.TableManager;
import com.dmg.clubserver.model.vo.DissolveClubVO;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.result.MessageResult;
import com.dmg.clubserver.result.ResultEnum;
import com.dmg.clubserver.tcp.manager.LocationManager;
import com.dmg.clubserver.tcp.server.MyWebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static com.dmg.clubserver.config.MessageConfig.*;

/**
 * @Description 解散俱乐部
 * @Author mice
 * @Date 2019/6/3 16:08
 * @Version V1.0
 **/
@Service
public class DissolveClubProcess implements AbstractMessageHandler {
    @Autowired
    private ClubDao clubDao;
    @Autowired
    private RClubPlayerDao clubPlayerDao;
    @Autowired
    private ClubLogDao clubLogDao;
    @Autowired
    private ClubJoinRequestDao clubJoinRequestDao;
    @Autowired
    private ClubInvitationDao clubInvitationDao;
    private static LocationManager locationManager;
    @Autowired
    public void setLocationManager(LocationManager locationManager) {
        DissolveClubProcess.locationManager = locationManager;
    }
    @Override
    public String getMessageId() {
        return DISSOLVE_CLUB;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        DissolveClubVO vo = params.toJavaObject(DissolveClubVO.class);
        ClubBean clubBean = clubDao.selectOne(new LambdaQueryWrapper<ClubBean>().eq(ClubBean::getClubId,vo.getClubId()).eq(ClubBean::getCreatorId,vo.getRoleId()));
        if (clubBean == null){
            result.setRes(ResultEnum.PARAM_ERROR.getCode());
            return;
        }
        Map<Integer,Table> tableMap = TableManager.instance().getTables(vo.getClubId());
        if (CollectionUtil.isNotEmpty(tableMap)){
            for (Table table : tableMap.values()){
                if (CollectionUtil.isNotEmpty(table.getSeatMap())){
                    result.setRes(ResultEnum.CLUB_TABLE_NOT_EMPTY.getCode());
                    return;
                }


            }

        }
        TableManager.instance().removeAllTable(vo.getClubId());
        // 通知玩家俱乐部解散
        List<RClubPlayerBean> rClubPlayerBeanList = clubPlayerDao.selectList(new LambdaQueryWrapper<RClubPlayerBean>().eq(RClubPlayerBean::getClubId,vo.getClubId()));

        // 清空俱乐部信息
        clubDao.deleteById(clubBean.getId());
        clubPlayerDao.delete(new LambdaQueryWrapper<RClubPlayerBean>().eq(RClubPlayerBean::getClubId,vo.getClubId()));
        clubLogDao.delete(new LambdaQueryWrapper<ClubLogBean>().eq(ClubLogBean::getClubId,vo.getClubId()));
        clubJoinRequestDao.delete(new LambdaQueryWrapper<ClubJoinRequestBean>().eq(ClubJoinRequestBean::getClubId,vo.getClubId()));
        clubInvitationDao.delete(new LambdaQueryWrapper<ClubInvitationBean>().eq(ClubInvitationBean::getClubId,vo.getClubId()));

        rClubPlayerBeanList.forEach(rClubPlayerBean -> {
            if (rClubPlayerBean.getPosition() == 1)return;
            MyWebSocket myWebSocket = locationManager.getWebSocket(rClubPlayerBean.getRoleId());
            if (myWebSocket!=null){
                MessageResult messageResult = new MessageResult(1,"",DISSOLVE_CLUB_NTC);
                myWebSocket.sendMessage(JSONObject.toJSONString(messageResult));
            }
        });
    }
}