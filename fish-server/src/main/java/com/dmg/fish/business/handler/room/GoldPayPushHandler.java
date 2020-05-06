package com.dmg.fish.business.handler.room;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.dmg.data.client.net.DataPushHandler;
import com.dmg.data.common.constant.MsgConst;
import com.dmg.data.common.dto.GoldPayPushDto;
import com.dmg.fish.business.logic.FishMgr;

/**
 * 金币充值
 */
@Component(MsgConst.goldPay)
public class GoldPayPushHandler implements DataPushHandler {
    @Autowired
    private FishMgr fishMgr;

    @Override
    public void action(String msg) {
        GoldPayPushDto pushDto = JSON.parseObject(msg, GoldPayPushDto.class);
        this.fishMgr.goldPay(pushDto.getUserId(), pushDto.getPayGold().multiply(new BigDecimal(100)).longValue());
    }
}
