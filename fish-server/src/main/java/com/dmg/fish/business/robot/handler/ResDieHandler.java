package com.dmg.fish.business.robot.handler;

import org.springframework.stereotype.Component;

import com.dmg.common.pb.java.Fish;
import com.dmg.fish.business.robot.FishRobot;
import com.dmg.fish.core.msg.ReqPbMessageHandler;
import com.google.protobuf.Message;

/**
 * 子弹打死鱼返回
 */
@Component("201213")
public final class ResDieHandler implements ReqPbMessageHandler {

    @Override
    public void action(Object player, Message message) {
        Fish.ResDie msg = null;
        if (message instanceof Fish.ResDie) {
            msg = (Fish.ResDie) message;
        } else {
            return;
        }

        FishRobot robot = (FishRobot) player;

        for (Fish.FishDieInfo fish : msg.getFishsList()) {
            robot.fishs.remove(fish.getFishId());
        }
    }
}
