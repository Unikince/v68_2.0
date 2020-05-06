package com.dmg.fish.business.robot.handler;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.dmg.common.pb.java.Fish;
import com.dmg.fish.business.robot.FishRobot;
import com.dmg.fish.core.msg.ReqPbMessageHandler;
import com.google.protobuf.Message;

/**
 * 场景数据返回
 */
@Component("201229")
public final class ResScenceHandler implements ReqPbMessageHandler {

    @Override
    public void action(Object player, Message message) {
        Fish.ResScence msg = null;
        if (message instanceof Fish.ResScence) {
            msg = (Fish.ResScence) message;
        } else {
            return;
        }

        FishRobot robot = (FishRobot) player;
        robot.pauseFire = true;
        robot.sceneTime = System.currentTimeMillis();
        robot.fishs.clear();
        for (Fish.FishInfo fish : msg.getFishsList()) {
            robot.fishs.put(fish.getFishId(), fish);
        }
        robot.schedule(() -> robot.pauseFire = false, 4, TimeUnit.SECONDS);
    }
}
