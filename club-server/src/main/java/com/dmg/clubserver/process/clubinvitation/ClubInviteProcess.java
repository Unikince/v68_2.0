package com.dmg.clubserver.process.clubinvitation;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.clubserver.config.RedPointType;
import com.dmg.clubserver.dao.ClubDao;
import com.dmg.clubserver.dao.ClubInvitationDao;
import com.dmg.clubserver.dao.RClubPlayerDao;
import com.dmg.clubserver.dao.bean.ClubBean;
import com.dmg.clubserver.dao.bean.ClubInvitationBean;
import com.dmg.clubserver.dao.bean.RClubPlayerBean;
import com.dmg.clubserver.model.vo.ClubInviteVO;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.process.clubrequest.RequestJoinClubProcess;
import com.dmg.clubserver.result.MessageResult;
import com.dmg.clubserver.result.ResultEnum;
import com.dmg.clubserver.tcp.manager.LocationManager;
import com.dmg.clubserver.tcp.server.MyWebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.dmg.clubserver.config.MessageConfig.CLUB_INVITATION;
import static com.dmg.clubserver.config.MessageConfig.READ_POINT_NTC;

/**
 * @Description 发起俱乐部邀请
 * @Author mice
 * @Date 2019/5/30 19:18
 * @Version V1.0
 **/
@Service
public class ClubInviteProcess implements AbstractMessageHandler {
    @Autowired
    private ClubDao clubDao;
    @Autowired
    private ClubInvitationDao clubInvitationDao;
    @Autowired
    private RClubPlayerDao clubPlayerDao;
    private static LocationManager locationManager;
    @Autowired
    public void setLocationManager(LocationManager locationManager) {
        ClubInviteProcess.locationManager = locationManager;
    }

    @Override
    public String getMessageId() {
        return CLUB_INVITATION;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        ClubInviteVO vo = params.toJavaObject(ClubInviteVO.class);

        // 权限检查
        RClubPlayerBean manager = clubPlayerDao.selectOne(new LambdaQueryWrapper<RClubPlayerBean>()
                .eq(RClubPlayerBean::getClubId, vo.getClubId())
                .eq(RClubPlayerBean::getRoleId, vo.getInvitorId()));
        if (manager.getPosition() == 3) {
            result.setRes(ResultEnum.NO_AUTH.getCode());
            return;
        }

        clubDao.selectOne(new LambdaQueryWrapper<ClubBean>().eq(ClubBean::getClubId,vo.getClubId()));
        ClubInvitationBean clubInvitationBean = clubInvitationDao.selectOne(new LambdaQueryWrapper<ClubInvitationBean>()
                .eq(ClubInvitationBean::getClubId,vo.getClubId())
                .eq(ClubInvitationBean::getBeInvitorId,vo.getBeInvitorId()));
        if (clubInvitationBean!=null){
            clubInvitationBean.setInviteDate(new Date());
            clubInvitationBean.setInvitorId(vo.getInvitorId());
            clubInvitationDao.updateById(clubInvitationBean);
            return;
        }
        clubInvitationBean = new ClubInvitationBean();
        clubInvitationBean.setClubId(vo.getClubId());
        clubInvitationBean.setInvitorId(vo.getInvitorId());
        clubInvitationBean.setBeInvitorId(vo.getBeInvitorId());
        clubInvitationBean.setInviteDate(new Date());
        clubInvitationDao.insert(clubInvitationBean);
        MessageResult messageResult = new MessageResult(1, RedPointType.JOIN.getKey(),READ_POINT_NTC);
        MyWebSocket myWebSocket = locationManager.getWebSocket(vo.getBeInvitorId());
        if (myWebSocket!=null){
            myWebSocket.sendMessage(JSONObject.toJSONString(messageResult));
        }
    }
}