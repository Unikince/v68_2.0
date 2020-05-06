package com.dmg.bjlserver.business.handler.operator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dmg.bjlserver.business.logic.BjlMgr;
import com.dmg.bjlserver.core.msg.ReqPbMessageHandler;
import com.dmg.common.pb.java.Bjl;
import com.google.protobuf.Message;

/**
 * 请求下注
 */
@Component("" + Bjl.BjlMessageId.ReqBet_ID_VALUE)
public final class ReqBetHandler implements ReqPbMessageHandler {
    @Autowired
    private BjlMgr bjlMgr;

    @Override
    public void action(Object player, Message message) {
        Bjl.ReqBet msg = (Bjl.ReqBet) message;
        this.bjlMgr.bet((Long) player, msg.getBet().getArea(), msg.getBet().getGold());
    }

}
