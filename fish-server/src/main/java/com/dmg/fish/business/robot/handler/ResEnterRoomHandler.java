package com.dmg.fish.business.robot.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.dmg.common.pb.java.Fish;
import com.dmg.fish.business.robot.FishRobot;
import com.dmg.fish.business.robot.RobotNetUtil;
import com.dmg.fish.core.msg.ReqPbMessageHandler;
import com.google.protobuf.Message;

/**
 * 进入房间返回
 */
@Component("201203")
public final class ResEnterRoomHandler implements ReqPbMessageHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ResEnterRoomHandler.class);

    @Override
    public void action(Object player, Message message) {
        Fish.ResEnterRoom msg = null;
        if (message instanceof Fish.ResEnterRoom) {
            msg = (Fish.ResEnterRoom) message;
        } else {
            return;
        }
        FishRobot robot = (FishRobot) player;
        RobotNetUtil.send(robot.getId(), Fish.FishMessageId.ReqRestore_ID_VALUE, Fish.ReqRestore.newBuilder().build());
        ResEnterRoomHandler.LOG.info("[捕鱼]玩家[{}]进入捕鱼房间[{}]成功", robot.getId(), msg.getRoomId());
    }
}
