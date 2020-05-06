package com.dmg.fish.business.handler.room;

import com.dmg.fish.business.logic.FishMgr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dmg.fish.business.logic.FishMsgMgr;
import com.dmg.fish.core.msg.ReqPbMessageHandler;
import com.google.protobuf.Message;

/**
 * 请求房间数据
 */
@Component("201101")
public final class ReqRoomsHandler implements ReqPbMessageHandler {

    @Autowired
    private FishMsgMgr fishMsgMgr;

    @Autowired
    private FishMgr fishMgr;

    @Override
    public void action(Object player, Message message) {
        if(fishMgr.getStopService()) {
            this.fishMsgMgr.sendStopServerExitMsg((Long) player);
            return;
        }
        this.fishMsgMgr.sendRoomsMsg((Long) player);
    }

}
