package com.dmg.fish.business.handler.operator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dmg.fish.business.logic.FishMgr;
import com.dmg.fish.core.msg.ReqPbMessageHandler;
import com.google.protobuf.Message;

/**
 * 请求减炮
 */
@Component("201111")
public final class ReqMinusBatteryHandler implements ReqPbMessageHandler {

    @Autowired
    private FishMgr fishMgr;

    @Override
    public void action(Object player, Message message) {
        this.fishMgr.minusBatteryScore((Long) player);
    }

}
