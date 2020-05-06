package com.dmg.fish.business.robot.handler.n;

import org.springframework.stereotype.Component;

import com.dmg.fish.core.msg.ReqPbMessageHandler;
import com.google.protobuf.Message;

/**
 * 锁定返回
 */
@Component("201217")
public final class ResLockHandler implements ReqPbMessageHandler {

    @Override
    public void action(Object player, Message message) {

    }
}
