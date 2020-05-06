package com.dmg.fish.business.robot.handler.n;

import org.springframework.stereotype.Component;

import com.dmg.fish.core.msg.ReqPbMessageHandler;
import com.google.protobuf.Message;

/**
 * 超时未操作退出游戏
 */
@Component("201237")
public final class ResTimeoutExitHandler implements ReqPbMessageHandler {

    @Override
    public void action(Object player, Message message) {

    }
}
