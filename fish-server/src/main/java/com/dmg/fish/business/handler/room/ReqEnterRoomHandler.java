package com.dmg.fish.business.handler.room;

import com.dmg.fish.business.logic.FishMsgMgr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dmg.fish.business.logic.FishMgr;
import com.dmg.common.pb.java.Fish;
import com.dmg.fish.core.msg.ReqPbMessageHandler;
import com.google.protobuf.Message;

/**
 * 请求进入房间
 */
@Component("201103")
public final class ReqEnterRoomHandler implements ReqPbMessageHandler {

    @Autowired
    private FishMgr fishMgr;

    @Autowired
    private FishMsgMgr fishMsgMgr;

    @Override
    public void action(Object player, Message message) {
        if(fishMgr.getStopService()) {
            this.fishMsgMgr.sendStopServerExitMsg((Long) player);
            return;
        }
        Fish.ReqEnterRoom msg = null;
        if (message instanceof Fish.ReqEnterRoom) {
            msg = (Fish.ReqEnterRoom) message;
        } else {
            return;
        }
        this.fishMgr.enterRoom((Long) player, msg.getRoomId(), false);
    }

}
