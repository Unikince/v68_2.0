package com.dmg.clubserver.process.clubmanage;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.clubserver.dao.RClubPlayerDao;
import com.dmg.clubserver.dao.bean.RClubPlayerBean;
import com.dmg.clubserver.model.vo.FreezeAndUnFreezeVO;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.result.MessageResult;
import com.dmg.clubserver.result.ResultEnum;
import com.dmg.clubserver.tcp.manager.LocationManager;
import com.dmg.clubserver.tcp.server.MyWebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.dmg.clubserver.config.MessageConfig.*;

/**
 * @Description 冻结 解冻成员
 * @Author mice
 * @Date 2019/5/28 16:56
 * @Version V1.0
 **/
@Service
public class FreezeAndUnFreezeProcess implements AbstractMessageHandler {
    @Autowired
    private RClubPlayerDao clubPlayerDao;
    private static LocationManager locationManager;
    @Autowired
    public void setLocationManager(LocationManager locationManager) {
        FreezeAndUnFreezeProcess.locationManager = locationManager;
    }

    @Override
    public String getMessageId() {
        return FREEZE_AND_UNFREEZE;
    }

    @Override
    @Transactional
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        FreezeAndUnFreezeVO vo = params.toJavaObject(FreezeAndUnFreezeVO.class);
        RClubPlayerBean clubPlayerBean = clubPlayerDao.selectOne(new LambdaQueryWrapper<RClubPlayerBean>()
                .eq(RClubPlayerBean::getClubId,vo.getClubId())
                .eq(RClubPlayerBean::getRoleId,vo.getRoleId()));
        if (clubPlayerBean == null){
            result.setRes(ResultEnum.PARAM_ERROR.getCode());
            return;
        }
        RClubPlayerBean manager = clubPlayerDao.selectOne(new LambdaQueryWrapper<RClubPlayerBean>()
                .eq(RClubPlayerBean::getClubId,vo.getClubId())
                .eq(RClubPlayerBean::getRoleId,vo.getOperatorId()));

        RClubPlayerBean beFreezor = clubPlayerDao.selectOne(new LambdaQueryWrapper<RClubPlayerBean>()
                .eq(RClubPlayerBean::getClubId,vo.getClubId())
                .eq(RClubPlayerBean::getRoleId,vo.getRoleId()));
        if (manager.getPosition()==3 || beFreezor.getPosition()<=2){
            result.setRes(ResultEnum.NO_AUTH.getCode());
            return;
        }
        clubPlayerBean.setStatus(vo.getFreezeStatus());
        clubPlayerDao.updateById(clubPlayerBean);
        MyWebSocket myWebSocket = locationManager.getWebSocket(vo.getRoleId());
        JSONObject s = new JSONObject();
        s.put("clubId",vo.getClubId());
        s.put("status",vo.getFreezeStatus());
        if (myWebSocket!=null){
            MessageResult messageResult = new MessageResult(1,s,FREEZE_AND_UNFREEZE_NTC);
            myWebSocket.sendMessage(JSONObject.toJSONString(messageResult));
        }
    }
}