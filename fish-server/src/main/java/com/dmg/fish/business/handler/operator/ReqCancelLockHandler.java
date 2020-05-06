package com.dmg.fish.business.handler.operator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dmg.fish.business.logic.FishMgr;
import com.dmg.fish.core.msg.ReqPbMessageHandler;
import com.google.protobuf.Message;

/**
 * 取消锁定
 */
@Component("201119")
public final class ReqCancelLockHandler implements ReqPbMessageHandler {

    @Autowired
    private FishMgr fishMgr;

    @Override
    public void action(Object player, Message message) {
//		ReqCancelLockMsg msg = (ReqCancelLockMsg) message;
        this.fishMgr.cancelLock((Long) player);
    }

}
