package com.dmg.fish.business.handler.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dmg.fish.business.logic.FishMgr;
import com.dmg.common.pb.java.Fish;
import com.dmg.fish.core.msg.ReqPbMessageHandler;
import com.google.protobuf.Message;

/**
 * 检查是否可以进入房间
 */
@Component("201102")
public final class ReqCheckRoomHandler implements ReqPbMessageHandler {
    @Autowired
    private FishMgr fishMgr;

    @Override
    public void action(Object player, Message message) {
        Fish.ReqCheckRoom msg = null;
        if (message instanceof Fish.ReqCheckRoom) {
            msg = (Fish.ReqCheckRoom) message;
        } else {
            return;
        }
        this.fishMgr.checkRoom((Long) player, msg.getRoomId());
    }

}
