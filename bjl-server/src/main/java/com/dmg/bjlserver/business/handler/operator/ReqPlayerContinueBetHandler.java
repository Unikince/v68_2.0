package com.dmg.bjlserver.business.handler.operator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dmg.bjlserver.business.logic.BjlMgr;
import com.dmg.bjlserver.core.msg.ReqPbMessageHandler;
import com.dmg.common.pb.java.Bjl;
import com.google.protobuf.Message;

/**
 * 玩家续压
 */
@Component("" + Bjl.BjlMessageId.ReqPlayerContinueBet_ID_VALUE)
public final class ReqPlayerContinueBetHandler implements ReqPbMessageHandler {
    @Autowired
    private BjlMgr bjlMgr;

    @Override
    public void action(Object player, Message message) {
        Bjl.ReqPlayerContinueBet msg = (Bjl.ReqPlayerContinueBet) message;
        this.bjlMgr.continueBet((Long) player, msg.getBetsList());
    }

}
