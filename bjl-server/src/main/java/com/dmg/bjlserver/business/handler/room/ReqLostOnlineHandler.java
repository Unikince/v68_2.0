package com.dmg.bjlserver.business.handler.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dmg.bjlserver.business.logic.BjlMgr;
import com.dmg.bjlserver.core.msg.ReqPbMessageHandler;
import com.google.protobuf.Message;

/**
 * 掉线
 */
@Component("998877")
public final class ReqLostOnlineHandler implements ReqPbMessageHandler {
    @Autowired
    private BjlMgr bjlMgr;

    @Override
    public void action(Object player, Message message) {
        this.bjlMgr.lostOnline((Long) player);
    }

}
