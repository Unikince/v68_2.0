package com.dmg.fish.business.robot.handler.n;

import org.springframework.stereotype.Component;

import com.dmg.fish.core.msg.ReqPbMessageHandler;
import com.google.protobuf.Message;

/**
 * 代发碰撞玩家列表更新
 */
@Component("201233")
public final class ResInsteadPlayersHandler implements ReqPbMessageHandler {

    @Override
    public void action(Object player, Message message) {

    }
}
