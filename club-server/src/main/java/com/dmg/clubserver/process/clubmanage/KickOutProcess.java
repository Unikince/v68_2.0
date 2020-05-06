package com.dmg.clubserver.process.clubmanage;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.clubserver.config.ClubLogType;
import com.dmg.clubserver.dao.ClubDao;
import com.dmg.clubserver.dao.ClubKickOutLogDao;
import com.dmg.clubserver.dao.RClubPlayerDao;
import com.dmg.clubserver.dao.bean.ClubBean;
import com.dmg.clubserver.dao.bean.ClubKickOutLogBean;
import com.dmg.clubserver.dao.bean.RClubPlayerBean;
import com.dmg.clubserver.model.vo.KickOutVO;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.process.clubrequest.ReviewClubLobbyRequestProcess;
import com.dmg.clubserver.result.MessageResult;
import com.dmg.clubserver.result.ResultEnum;
import com.dmg.clubserver.service.ClubLogService;
import com.dmg.clubserver.tcp.manager.LocationManager;
import com.dmg.clubserver.tcp.server.MyWebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static com.dmg.clubserver.config.MessageConfig.*;

/**
 * @Description 踢出成员
 * @Author mice
 * @Date 2019/5/28 17:09
 * @Version V1.0
 **/
@Service
public class KickOutProcess implements AbstractMessageHandler {
    @Autowired
    private RClubPlayerDao clubPlayerDao;
    @Autowired
    private ClubKickOutLogDao clubKickOutLogDao;
    @Autowired
    private ClubDao clubDao;
    @Autowired
    private ClubLogService clubLogService;
    private static LocationManager locationManager;
    @Autowired
    public void setLocationManager(LocationManager locationManager) {
        KickOutProcess.locationManager = locationManager;
    }

    @Override
    public String getMessageId() {
        return KICK_OUT;
    }

    @Override
    @Transactional
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        KickOutVO vo = params.toJavaObject(KickOutVO.class);

        RClubPlayerBean clubPlayerBean = clubPlayerDao.selectOne(new LambdaQueryWrapper<RClubPlayerBean>()
                .eq(RClubPlayerBean::getClubId,vo.getClubId())
                .eq(RClubPlayerBean::getRoleId,vo.getKickOutRoleId()));
        if (clubPlayerBean == null){
            result.setRes(ResultEnum.PARAM_ERROR.getCode());
            return;
        }
        RClubPlayerBean manager = clubPlayerDao.selectOne(new LambdaQueryWrapper<RClubPlayerBean>()
                .eq(RClubPlayerBean::getClubId,vo.getClubId())
                .eq(RClubPlayerBean::getRoleId,vo.getOperatorId()));

        RClubPlayerBean beKickOut = clubPlayerDao.selectOne(new LambdaQueryWrapper<RClubPlayerBean>()
                .eq(RClubPlayerBean::getClubId,vo.getClubId())
                .eq(RClubPlayerBean::getRoleId,vo.getKickOutRoleId()));
        if (manager.getPosition()==3 || beKickOut.getPosition()<=manager.getPosition()){
            result.setRes(ResultEnum.NO_AUTH.getCode());
            return;
        }
        ClubBean clubBean = clubDao.selectOne(new LambdaQueryWrapper<ClubBean>().eq(ClubBean::getClubId,vo.getClubId()));
        clubPlayerDao.deleteById(clubPlayerBean.getId());
        clubBean.setCurrentMemberNum(clubBean.getCurrentMemberNum()-1);
        clubDao.updateById(clubBean);
        clubLogService.saveLog(vo.getOperatorId(),vo.getKickOutRoleId(),
                ClubLogType.FORCE_LEAVE.getKey(),vo.getClubId(),null);
        // 保存踢出日志
        ClubKickOutLogBean clubKickOutLogBean = new ClubKickOutLogBean();
        clubKickOutLogBean.setClubId(vo.getClubId());
        clubKickOutLogBean.setRoleId(vo.getKickOutRoleId());
        clubKickOutLogBean.setOperatorId(vo.getOperatorId());
        clubKickOutLogBean.setOperateDate(new Date());
        clubKickOutLogDao.insert(clubKickOutLogBean);
        MyWebSocket myWebSocket = locationManager.getWebSocket(vo.getKickOutRoleId());
        if (myWebSocket!=null){
            MessageResult messageResult = new MessageResult(1,vo.getClubId(),KICK_OUT_NTC);
            myWebSocket.sendMessage(JSONObject.toJSONString(messageResult));
        }
    }
}