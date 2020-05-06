package com.dmg.fish.business.robot.handler.n;

import org.springframework.stereotype.Component;

import com.dmg.fish.core.msg.ReqPbMessageHandler;
import com.google.protobuf.Message;

/**
 * 房间数据返回
 */
@Component("201201")
public final class ResRoomsHandler implements ReqPbMessageHandler {

    @Override
    public void action(Object player, Message message) {

    }
}
