package com.dmg.fish.business.robot.handler;

import org.springframework.stereotype.Component;

import com.dmg.common.pb.java.Fish;
import com.dmg.fish.business.robot.FishRobot;
import com.dmg.fish.core.msg.ReqPbMessageHandler;
import com.google.protobuf.Message;

/**
 * 炮台改变返回
 */
@Component("201211")
public final class ResBatteryChangeHandler implements ReqPbMessageHandler {

    @Override
    public void action(Object player, Message message) {
        Fish.ResBatteryChange msg = null;
        if (message instanceof Fish.ResBatteryChange) {
            msg = (Fish.ResBatteryChange) message;
        } else {
            return;
        }
        FishRobot robot = (FishRobot) player;
        robot.batteryScore = (int) (msg.getScore() * 100);
    }
}
