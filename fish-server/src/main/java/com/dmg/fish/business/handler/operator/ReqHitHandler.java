package com.dmg.fish.business.handler.operator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dmg.fish.business.logic.FishMgr;
import com.dmg.common.pb.java.Fish;
import com.dmg.fish.core.msg.ReqPbMessageHandler;
import com.google.protobuf.Message;

/**
 * 请求子弹打中鱼
 */
@Component("201107")
public final class ReqHitHandler implements ReqPbMessageHandler {

    @Autowired
    private FishMgr fishMgr;

    @Override
    public void action(Object player, Message message) {
        Fish.ReqHit msg = null;
        if (message instanceof Fish.ReqHit) {
            msg = (Fish.ReqHit) message;
        } else {
            return;
        }
        this.fishMgr.hit(msg.getPlayerId(), msg.getBulletId(), msg.getFishId());
    }

}
