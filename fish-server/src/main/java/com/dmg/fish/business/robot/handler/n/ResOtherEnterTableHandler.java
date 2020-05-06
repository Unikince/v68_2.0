package com.dmg.fish.business.robot.handler.n;

import org.springframework.stereotype.Component;

import com.dmg.fish.core.msg.ReqPbMessageHandler;
import com.google.protobuf.Message;

/**
 * 其他人进入桌子返回
 */
@Component("201207")
public final class ResOtherEnterTableHandler implements ReqPbMessageHandler {

    @Override
    public void action(Object player, Message message) {

    }
}
