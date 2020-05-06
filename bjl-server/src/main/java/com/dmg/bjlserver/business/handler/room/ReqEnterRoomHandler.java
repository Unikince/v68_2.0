package com.dmg.bjlserver.business.handler.room;

import com.dmg.bjlserver.business.logic.BjlMsgMgr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dmg.bjlserver.business.logic.BjlMgr;
import com.dmg.bjlserver.core.msg.ReqPbMessageHandler;
import com.dmg.common.pb.java.Bjl;
import com.google.protobuf.Message;

/**
 * 请求进入房间
 */
@Component("" + Bjl.BjlMessageId.ReqEnterRoom_ID_VALUE)
public final class ReqEnterRoomHandler implements ReqPbMessageHandler {

    @Autowired
    private BjlMgr bjlMgr;

    @Autowired
    private BjlMsgMgr msgMgr;

    @Override
    public void action(Object player, Message message) {
        if(bjlMgr.getStopService()) {
            this.msgMgr.sendExitRoomMsg((Long) player, Bjl.BjlCode.STOP_SERVER);
            return;
        }
        Bjl.ReqEnterRoom msg = (Bjl.ReqEnterRoom) message;
        this.bjlMgr.enterRoom(player, msg.getRoomId());
    }

}
