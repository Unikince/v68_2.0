package com.dmg.clubserver.process.clubrequest;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.clubserver.config.RedPointType;
import com.dmg.clubserver.dao.ClubDao;
import com.dmg.clubserver.dao.ClubJoinRequestDao;
import com.dmg.clubserver.dao.ClubKickOutLogDao;
import com.dmg.clubserver.dao.RClubPlayerDao;
import com.dmg.clubserver.dao.bean.ClubBean;
import com.dmg.clubserver.dao.bean.ClubJoinRequestBean;
import com.dmg.clubserver.dao.bean.ClubKickOutLogBean;
import com.dmg.clubserver.dao.bean.RClubPlayerBean;
import com.dmg.clubserver.model.vo.RequestJoinClubVO;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.process.clubmanage.KickOutProcess;
import com.dmg.clubserver.result.MessageResult;
import com.dmg.clubserver.result.ResultEnum;
import com.dmg.clubserver.tcp.manager.LocationManager;
import com.dmg.clubserver.tcp.server.MyWebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.dmg.clubserver.config.MessageConfig.*;

/**
 * @Description
 * @Author mice
 * @Date 2019/5/28 10:26
 * @Version V1.0
 **/
@Service
public class RequestJoinClubProcess implements AbstractMessageHandler {
    @Autowired
    private ClubJoinRequestDao clubJoinRequestDao;
    @Autowired
    private RClubPlayerDao clubPlayerDao;
    @Autowired
    private ClubDao clubDao;
    @Autowired
    private ClubKickOutLogDao clubKickOutLogDao;
    private static LocationManager locationManager;
    @Autowired
    public void setLocationManager(LocationManager locationManager) {
        RequestJoinClubProcess.locationManager = locationManager;
    }
    @Override
    public String getMessageId() {
        return REQUEST_JOIN_CLUB;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        RequestJoinClubVO vo = params.toJavaObject(RequestJoinClubVO.class);
        ClubBean clubBean = clubDao.selectOne(new LambdaQueryWrapper<ClubBean>().eq(ClubBean::getClubId,vo.getClubId()));
        if (clubBean == null){
            result.setRes(ResultEnum.PARAM_ERROR.getCode());
            return;
        }
        // 检查踢出上限
        int kickoutCount = clubKickOutLogDao.selectCount(new LambdaQueryWrapper<ClubKickOutLogBean>()
                .eq(ClubKickOutLogBean::getClubId,vo.getClubId())
                .eq(ClubKickOutLogBean::getRoleId,vo.getRoleId()));
        if (kickoutCount>= 3){
            result.setRes(ResultEnum.KICK_OUT_LIMIT.getCode());
            return;
        }
        if (clubBean.getCurrentMemberNum()>=clubBean.getMemberNumLimit()){
            result.setRes(ResultEnum.CLUB_MEMBER_LIMIT.getCode());
            return;
        }
        int hasJoinClubCount = clubPlayerDao.selectCount(new LambdaQueryWrapper<RClubPlayerBean>().eq(RClubPlayerBean::getRoleId,vo.getRoleId()));
        if (hasJoinClubCount>=10){
            result.setRes(ResultEnum.JOINED_CLUB_LIMIT.getCode());
            return;
        }
        ClubJoinRequestBean clubJoinRequestBean = clubJoinRequestDao.selectOne(new LambdaQueryWrapper<ClubJoinRequestBean>()
                .eq(ClubJoinRequestBean::getClubId,vo.getClubId())
                .eq(ClubJoinRequestBean::getRequestorId,vo.getRoleId()));
        if (clubJoinRequestBean != null){
            if (clubJoinRequestBean.getReviewerId() != null){
                result.setRes(ResultEnum.CLUB_REQUEST_BE_REFUSE.getCode());
                return;
            }
            result.setRes(ResultEnum.REPEAT_REQUEST.getCode());
            return;
        }
        clubJoinRequestBean = new ClubJoinRequestBean();
        clubJoinRequestBean.setClubId(vo.getClubId());
        clubJoinRequestBean.setRequestorId(vo.getRoleId());
        clubJoinRequestBean.setRequestDate(new Date());
        clubJoinRequestDao.insert(clubJoinRequestBean);

        List<RClubPlayerBean> clubPlayerBeanList = clubPlayerDao.selectList(new LambdaQueryWrapper<RClubPlayerBean>()
                .eq(RClubPlayerBean::getClubId,vo.getClubId())
                .in(RClubPlayerBean::getPosition, Arrays.asList(1,2)));
        MessageResult messageResult = new MessageResult(1, RedPointType.REVIEW.getKey(),READ_POINT_NTC);
        clubPlayerBeanList.forEach(rClubPlayerBean -> {
            MyWebSocket myWebSocket = locationManager.getWebSocket(rClubPlayerBean.getRoleId());
            if (myWebSocket!=null){
                myWebSocket.sendMessage(JSONObject.toJSONString(messageResult));
            }
        });

    }
}