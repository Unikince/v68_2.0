package com.dmg.fish.business.robot.handler.n;

import org.springframework.stereotype.Component;

import com.dmg.fish.core.msg.ReqPbMessageHandler;
import com.google.protobuf.Message;

/**
 * 退出房间返回
 */
@Component("201205")
public final class ResExitRoomHandler implements ReqPbMessageHandler {

    @Override
    public void action(Object player, Message message) {

    }
}
