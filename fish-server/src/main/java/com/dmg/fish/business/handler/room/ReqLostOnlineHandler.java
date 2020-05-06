package com.dmg.fish.business.handler.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dmg.fish.business.logic.FishMgr;
import com.dmg.fish.core.msg.ReqPbMessageHandler;
import com.google.protobuf.Message;

/**
 * 掉线
 */
@Component("998877")
public final class ReqLostOnlineHandler implements ReqPbMessageHandler {
    @Autowired
    private FishMgr fishMgr;

    @Override
    public void action(Object player, Message message) {
        this.fishMgr.lostOnline((Long) player);
    }

}
