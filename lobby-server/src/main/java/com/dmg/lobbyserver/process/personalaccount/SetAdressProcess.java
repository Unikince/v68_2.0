package com.dmg.lobbyserver.process.personalaccount;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.dao.ReceiveAddressDao;
import com.dmg.lobbyserver.dao.bean.ReceiveAddressBean;
import com.dmg.lobbyserver.model.vo.AdressVo;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.dmg.lobbyserver.config.MessageConfig.SET_ADDRESS;

/**
 * @Description 设置收货地址
 * @Author jock
 * @Date 2019/6/18 0018
 * @Version V1.0
 **/
@Service
@Slf4j
public class SetAdressProcess implements AbstractMessageHandler {
    @Override
    public String getMessageId() {
        return SET_ADDRESS;
    }

    @Autowired
    ReceiveAddressDao receiveAddressDao;

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        AdressVo adressVo = params.toJavaObject(AdressVo.class);
        ReceiveAddressBean receiveAddressBean = receiveAddressDao.selectOne(new LambdaQueryWrapper<ReceiveAddressBean>().eq(ReceiveAddressBean::getUserId,userid));
        if (receiveAddressBean == null){
            receiveAddressBean = new ReceiveAddressBean();
            receiveAddressBean.setAddress(adressVo.getAddress());
            receiveAddressBean.setArea(adressVo.getArea());
            receiveAddressBean.setPhone(adressVo.getPhone());
            receiveAddressBean.setReceiverName(adressVo.getReceiverName());
            receiveAddressBean.setUserId(Long.parseLong(userid));
            receiveAddressDao.insert(receiveAddressBean);
        }else {
            receiveAddressBean.setAddress(adressVo.getAddress());
            receiveAddressBean.setArea(adressVo.getArea());
            receiveAddressBean.setPhone(adressVo.getPhone());
            receiveAddressBean.setReceiverName(adressVo.getReceiverName());
            receiveAddressBean.setUserId(Long.parseLong(userid));
            receiveAddressDao.updateById(receiveAddressBean);
        }
    }
}
