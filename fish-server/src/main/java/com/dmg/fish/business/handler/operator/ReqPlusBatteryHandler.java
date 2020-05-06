package com.dmg.fish.business.handler.operator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dmg.fish.business.logic.FishMgr;
import com.dmg.fish.core.msg.ReqPbMessageHandler;
import com.google.protobuf.Message;

/**
 * 请求加炮
 */
@Component("201109")
public final class ReqPlusBatteryHandler implements ReqPbMessageHandler {

    @Autowired
    private FishMgr fishMgr;

    @Override
    public void action(Object player, Message message) {
//		ReqPlusBatteryMsg msg = (ReqPlusBatteryMsg) message;
        this.fishMgr.plusBatteryScore((Long) player);
    }

}
