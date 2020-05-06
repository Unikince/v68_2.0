package com.dmg.fish.business.robot.handler;

import org.springframework.stereotype.Component;

import com.dmg.common.pb.java.Fish;
import com.dmg.fish.business.robot.FishRobot;
import com.dmg.fish.core.msg.ReqPbMessageHandler;
import com.google.protobuf.Message;

/**
 * 金币变化消息
 */
@Component("201222")
public final class ResGoldChangeHandler implements ReqPbMessageHandler {

    @Override
    public void action(Object player, Message message) {
        Fish.ResGoldChange msg = null;
        if (message instanceof Fish.ResGoldChange) {
            msg = (Fish.ResGoldChange) message;
        } else {
            return;
        }

        FishRobot robot = (FishRobot) player;
        if (msg.getPlayerId() != robot.data.getId()) {
            return;
        }

        robot.data.setGold((long) (msg.getGold() * 100));
    }
}
