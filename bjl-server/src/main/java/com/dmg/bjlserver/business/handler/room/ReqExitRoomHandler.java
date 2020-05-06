package com.dmg.bjlserver.business.handler.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dmg.bjlserver.business.logic.BjlMgr;
import com.dmg.bjlserver.core.msg.ReqPbMessageHandler;
import com.dmg.common.pb.java.Bjl;
import com.google.protobuf.Message;

/**
 * 请求退出房间
 */
@Component("" + Bjl.BjlMessageId.ReqExitRoom_ID_VALUE)
public final class ReqExitRoomHandler implements ReqPbMessageHandler {

    @Autowired
    private BjlMgr bjlMgr;

    @Override
    public void action(Object player, Message message) {
        this.bjlMgr.exitRoom((Long) player);
    }

}
