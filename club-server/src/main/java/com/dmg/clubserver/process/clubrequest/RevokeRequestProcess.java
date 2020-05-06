package com.dmg.clubserver.process.clubrequest;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.clubserver.config.RedPointType;
import com.dmg.clubserver.dao.ClubJoinRequestDao;
import com.dmg.clubserver.dao.RClubPlayerDao;
import com.dmg.clubserver.dao.bean.ClubJoinRequestBean;
import com.dmg.clubserver.dao.bean.RClubPlayerBean;
import com.dmg.clubserver.model.vo.RevokeRequestVO;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.result.MessageResult;
import com.dmg.clubserver.result.ResultEnum;
import com.dmg.clubserver.tcp.manager.LocationManager;
import com.dmg.clubserver.tcp.server.MyWebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static com.dmg.clubserver.config.MessageConfig.READ_POINT_NTC;
import static com.dmg.clubserver.config.MessageConfig.REVOKE_REQUEST;

/**
 * @Description 撤回俱乐部申请
 * @Author mice
 * @Date 2019/5/28 14:43
 * @Version V1.0
 **/
@Service
public class RevokeRequestProcess implements AbstractMessageHandler {
    @Autowired
    private ClubJoinRequestDao clubJoinRequestDao;
    @Autowired
    private RClubPlayerDao clubPlayerDao;
    private static LocationManager locationManager;
    @Autowired
    public void setLocationManager(LocationManager locationManager) {
        RevokeRequestProcess.locationManager = locationManager;
    }

    @Override
    public String getMessageId() {
        return REVOKE_REQUEST;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        RevokeRequestVO vo = params.toJavaObject(RevokeRequestVO.class);
        ClubJoinRequestBean clubJoinRequestBean = clubJoinRequestDao.selectOne(new LambdaQueryWrapper<ClubJoinRequestBean>()
                .eq(ClubJoinRequestBean::getClubId,vo.getClubId())
                .eq(ClubJoinRequestBean::getRequestorId,vo.getRoleId()));
        if (clubJoinRequestBean == null){
            result.setRes(ResultEnum.REQUEST_BE_DEAL.getCode());
            return;
        }
        if (clubJoinRequestBean.getReviewerId() != null){
            result.setRes(ResultEnum.REQUEST_BE_DEAL.getCode());
            return;
        }
        clubJoinRequestDao.deleteById(clubJoinRequestBean.getId());
        List<RClubPlayerBean> clubPlayerBeanList = clubPlayerDao.selectList(new LambdaQueryWrapper<RClubPlayerBean>()
                .eq(RClubPlayerBean::getClubId,vo.getClubId())
                .in(RClubPlayerBean::getPosition, Arrays.asList(1,2)));
        MessageResult messageResult = new MessageResult(0, RedPointType.REVIEW.getKey(),READ_POINT_NTC);
        clubPlayerBeanList.forEach(rClubPlayerBean -> {
            MyWebSocket myWebSocket = locationManager.getWebSocket(rClubPlayerBean.getRoleId());
            if (myWebSocket!=null){
                myWebSocket.sendMessage(JSONObject.toJSONString(messageResult));
            }
        });
    }
}