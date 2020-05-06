package com.dmg.bjlserver.business.handler.room;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.dmg.bjlserver.business.logic.BjlMgr;
import com.dmg.data.client.net.DataPushHandler;
import com.dmg.data.common.constant.MsgConst;
import com.dmg.data.common.dto.GoldPayPushDto;

/**
 * 金币充值
 */
@Component(MsgConst.goldPay)
public class GoldPayPushHandler implements DataPushHandler {
    @Autowired
    private BjlMgr bjlMgr;

    @Override
    public void action(String msg) {
        GoldPayPushDto pushDto = JSON.parseObject(msg, GoldPayPushDto.class);
        this.bjlMgr.goldPay(pushDto.getUserId(), pushDto.getPayGold().multiply(new BigDecimal(100)).longValue());
    }
}
