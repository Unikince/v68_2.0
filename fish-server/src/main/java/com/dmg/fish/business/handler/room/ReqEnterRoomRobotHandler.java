package com.dmg.fish.business.handler.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dmg.fish.business.logic.FishMgr;
import com.dmg.common.pb.java.Fish;
import com.dmg.fish.core.msg.ReqPbMessageHandler;
import com.google.protobuf.Message;

/**
 * 请求机器人进入房间
 */
@Component("201998")
public final class ReqEnterRoomRobotHandler implements ReqPbMessageHandler {

    @Autowired
    private FishMgr fishMgr;

    @Override
    public void action(Object player, Message message) {
        Fish.ReqEnterRoom msg = null;
        if (message instanceof Fish.ReqEnterRoom) {
            msg = (Fish.ReqEnterRoom) message;
        } else {
            return;
        }
        this.fishMgr.enterRoom((Long) player, msg.getRoomId(), true);
    }

}
