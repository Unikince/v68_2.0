package com.dmg.bjlserver.business.handler.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dmg.bjlserver.business.logic.BjlMsgMgr;
import com.dmg.bjlserver.core.msg.ReqPbMessageHandler;
import com.dmg.common.pb.java.Bjl;
import com.google.protobuf.Message;

/**
 * 请求无座玩家
 */
@Component("" + Bjl.BjlMessageId.ReqOtherPlayers_ID_VALUE)
public final class ReqOtherPlayersHandler implements ReqPbMessageHandler {
    @Autowired
    private BjlMsgMgr bjlMsgMgr;

    @Override
    public void action(Object player, Message message) {
        this.bjlMsgMgr.sendOtherPlayersMsg((long) player);
    }

}
