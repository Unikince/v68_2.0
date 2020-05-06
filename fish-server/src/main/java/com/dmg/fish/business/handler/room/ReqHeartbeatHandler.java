package com.dmg.fish.business.handler.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dmg.fish.business.logic.FishMsgMgr;
import com.dmg.fish.core.msg.ReqPbMessageHandler;
import com.google.protobuf.Message;

/**
 * 心跳
 */
@Component("201199")
public final class ReqHeartbeatHandler implements ReqPbMessageHandler {

    @Autowired
    private FishMsgMgr fishMsgMgr;

    @Override
    public void action(Object player, Message message) {
        this.fishMsgMgr.sendHeartbeat((Long) player);
    }

}
