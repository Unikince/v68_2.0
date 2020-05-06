package com.dmg.fish.business.robot;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmg.fish.core.msg.MessageMgr;
import com.dmg.fish.core.msg.ReqPbMessageHandler;
import com.dmg.fish.core.work.WorkerGroup;

import lombok.extern.slf4j.Slf4j;

/**
 * 机器人发送和接受数据
 */
@Slf4j
@Service
public class RobotNetUtil {
    /** 消息处理管理器 */
    private static MessageMgr msgMgr;
    private static WorkerGroup worker;
    private static FishRobotScheduler fishRobotScheduler;

    @Autowired
    public void setMessageMgr(MessageMgr msgMgr) {
        RobotNetUtil.msgMgr = msgMgr;
    }

    @Autowired
    public void setWorker(WorkerGroup worker) {
        RobotNetUtil.worker = worker;
    }

    @Autowired
    public void setFishRobotScheduler(FishRobotScheduler fishRobotScheduler) {
        RobotNetUtil.fishRobotScheduler = fishRobotScheduler;
    }

    public static void send(long playerId, int msgId, com.google.protobuf.Message msg) {
        log.debug("机器人发送[{}]的消息", playerId);
        ReqPbMessageHandler handler = msgMgr.getHandler("" + msgId);
        if (handler == null) {
            log.warn("未知消息ID[{}]", msgId);
        } else {
            worker.schedule(() -> {
                handler.action(playerId, msg);
            }, 0, TimeUnit.SECONDS);
        }
    }

    public void recv(long playerId, int msgId, com.google.protobuf.Message msg) {
        log.debug("机器人收到[{}]的消息", playerId);
        ReqPbMessageHandler handler = msgMgr.getHandler("" + msgId);
        if (handler == null) {
            log.warn("未知消息ID[{}]", msgId);
        } else {
            FishRobot robot = fishRobotScheduler.getUseRobot(playerId);
            if (robot != null) {
                worker.schedule(() -> {
                    handler.action(robot, msg);
                }, 0, TimeUnit.SECONDS);
            }
        }
    }
}
