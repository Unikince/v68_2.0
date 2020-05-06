package com.dmg.fish.business.robot.handler.n;

import org.springframework.stereotype.Component;

import com.dmg.fish.core.msg.ReqPbMessageHandler;
import com.google.protobuf.Message;

/**
 * 开炮返回
 */
@Component("201209")
public final class ResFireHandler implements ReqPbMessageHandler {

    @Override
    public void action(Object player, Message message) {

    }
}
