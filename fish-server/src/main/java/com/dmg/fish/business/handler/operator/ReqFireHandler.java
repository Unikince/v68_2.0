package com.dmg.fish.business.handler.operator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dmg.fish.business.logic.FishMgr;
import com.dmg.common.pb.java.Fish;
import com.dmg.fish.core.msg.ReqPbMessageHandler;
import com.google.protobuf.Message;

/**
 * 请求开炮
 */
@Component("201105")
public final class ReqFireHandler implements ReqPbMessageHandler {

    @Autowired
    private FishMgr fishMgr;

    @Override
    public void action(Object player, Message message) {
        Fish.ReqFire msg = null;
        if (message instanceof Fish.ReqFire) {
            msg = (Fish.ReqFire) message;
        } else {
            return;
        }
        this.fishMgr.fire((Long) player, msg.getAngle(), msg.getBulletId(), msg.getFishId());
    }

}
