package com.dmg.fish.business.robot.handler.n;

import org.springframework.stereotype.Component;

import com.dmg.fish.core.msg.ReqPbMessageHandler;
import com.google.protobuf.Message;

/**
 * 鱼的倍数更新
 */
@Component("201231")
public final class ResFishMultipleUpdateHandler implements ReqPbMessageHandler {

    @Override
    public void action(Object player, Message message) {

    }
}
