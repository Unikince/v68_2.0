package com.dmg.bjlserver.business.handler.room;

import com.dmg.bjlserver.business.logic.BjlMgr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dmg.bjlserver.business.logic.BjlMsgMgr;
import com.dmg.common.pb.java.Bjl;
import com.dmg.bjlserver.core.msg.ReqPbMessageHandler;
import com.google.protobuf.Message;

/**
 * 请求房间数据
 */
@Component("" + Bjl.BjlMessageId.ReqRooms_ID_VALUE)
public final class ReqRoomsHandler implements ReqPbMessageHandler {

    @Autowired
    private BjlMsgMgr bjlMsgMgr;

    @Autowired
    private BjlMgr bjlMgr;

    @Override
    public void action(Object player, Message message) {
        if (bjlMgr.getStopService()) {
            this.bjlMsgMgr.sendExitRoomMsg((Long) player, Bjl.BjlCode.STOP_SERVER);
            return;
        }
        this.bjlMsgMgr.sendRoomsMsg((Long) player);
    }

}
