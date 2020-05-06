package com.dmg.clubserver.process.clubmanage;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.clubserver.dao.RClubPlayerDao;
import com.dmg.clubserver.dao.bean.RClubPlayerBean;
import com.dmg.clubserver.model.vo.SetPositionVO;
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
 * @Description 设置职位
 * @Author mice
 * @Date 2019/5/28 17:46
 * @Version V1.0
 **/
@Service
public class SetPositionProcess implements AbstractMessageHandler {
    @Autowired
    private RClubPlayerDao clubPlayerDao;
    private static LocationManager locationManager;
    @Autowired
    public void setLocationManager(LocationManager locationManager) {
        SetPositionProcess.locationManager = locationManager;
    }

    @Override
    public String getMessageId() {
        return SET_POSITION;
    }

    @Override
    @Transactional
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        SetPositionVO vo = params.toJavaObject(SetPositionVO.class);
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

        if (manager.getPosition()!=1){
            result.setRes(ResultEnum.NO_AUTH.getCode());
            return;
        }
        if (vo.getPosition()==2){
            int managerCount = clubPlayerDao.selectCount(new LambdaQueryWrapper<RClubPlayerBean>()
                    .eq(RClubPlayerBean::getClubId,vo.getClubId())
                    .eq(RClubPlayerBean::getPosition,vo.getPosition()));
            if (managerCount>=2){
                result.setRes(ResultEnum.MANAGER_LIMIT.getCode());
                return;
            }
        }
        clubPlayerBean.setPosition(vo.getPosition());
        clubPlayerDao.updateById(clubPlayerBean);
        MyWebSocket myWebSocket = locationManager.getWebSocket(vo.getRoleId());
        if (myWebSocket!=null){
            MessageResult messageResult = new MessageResult(1,vo.getPosition(),SET_POSITION_NTC);
            myWebSocket.sendMessage(JSONObject.toJSONString(messageResult));
        }
    }
}