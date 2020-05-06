package com.dmg.fish.business.handler.operator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dmg.fish.business.logic.FishMgr;
import com.dmg.common.pb.java.Fish;
import com.dmg.fish.core.msg.ReqPbMessageHandler;
import com.google.protobuf.Message;

/**
 * 请求锁定
 */
@Component("201117")
public final class ReqLockHandler implements ReqPbMessageHandler {

    @Autowired
    private FishMgr fishMgr;

    @Override
    public void action(Object player, Message message) {
        Fish.ReqLock msg = null;
        if (message instanceof Fish.ReqLock) {
            msg = (Fish.ReqLock) message;
        } else {
            return;
        }
        this.fishMgr.lock((Long) player, msg.getFishId());
    }

}
