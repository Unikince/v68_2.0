package com.dmg.fish.business.robot.handler;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dmg.fish.business.config.dic.FishRoomDic;
import com.dmg.fish.business.config.wrapper.FishRoomWrapper;
import com.dmg.common.pb.java.Fish;
import com.dmg.fish.business.platform.model.Player;
import com.dmg.fish.business.robot.FishRobot;
import com.dmg.fish.core.msg.ReqPbMessageHandler;
import com.google.protobuf.Message;

import cn.hutool.core.util.RandomUtil;

/**
 * 游戏场景数据返回
 */
@Component("201215")
public final class ResRestoreHandler implements ReqPbMessageHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ResRestoreHandler.class);

    @Autowired
    private FishRoomDic fishRoomDic;

    @Override
    public void action(Object player, Message message) {
        Fish.ResRestore msg = null;
        if (message instanceof Fish.ResRestore) {
            msg = (Fish.ResRestore) message;
        } else {
            return;
        }

        FishRobot robot = (FishRobot) player;
        for (Fish.SeatInfo s : msg.getSeatsList()) {
            if (s.getPlayerId() == robot.getId()) {
                robot.batteryScore = (int) (s.getBatteryScore() * 100);
                robot.data.setGold((long) (s.getGold() * 100));
                robot.order = s.getOrder();
                break;
            }
        }

        robot.fishs.clear();
        robot.sceneTime = System.currentTimeMillis();
        for (Fish.FishInfo fish : msg.getFishsList()) {
            robot.fishs.put(fish.getFishId(), fish);
        }

        Player data = robot.data;

        // schedule发炮
        robot.scheduleFire();

        FishRoomWrapper fishRoomBean = this.fishRoomDic.get(robot.roomId);
        int gameSeconds = RandomUtil.randomInt(fishRoomBean.getRobotTimeLower(), fishRoomBean.getRobotTimeUpper() + 1);
        // schedule退出游戏
        robot.schedule(() -> {
            ResRestoreHandler.LOG.info("[捕鱼]玩家[{}][{}]游戏时间[{}]秒结束", data.getId(), data.getNickname(), gameSeconds);
            robot.logout();
        }, gameSeconds, TimeUnit.SECONDS);

        ResRestoreHandler.LOG.info("[捕鱼]玩家[{}][{}]恢复场景成功", data.getId(), data.getNickname());
    }
}
