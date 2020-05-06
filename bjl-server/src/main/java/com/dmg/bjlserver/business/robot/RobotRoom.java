package com.dmg.bjlserver.business.robot;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dmg.bjlserver.business.config.wrapper.BjlTableWrapper;
import com.dmg.bjlserver.business.logic.BjlMgr;
import com.dmg.bjlserver.business.platform.model.Player;

import cn.hutool.core.util.RandomUtil;

/**
 * 机器人房间类
 *
 * @author wh
 */
class RobotRoom {
    /**
     * 日志记录对象
     */
    private static final Logger LOG = LoggerFactory.getLogger(RobotRoom.class);
    /**
     * 房间id
     */
    private int roomId;
    /**
     * 当前房间机器人map
     */
    public ConcurrentHashMap<Long, Robot> robots = new ConcurrentHashMap<>();

    /**
     * 获取当前房间有效的机器人数量
     *
     * @return
     */
    private int getRobotsValid() {
        int result = 0;
        for (Robot robot : this.robots.values()) {
            RobotState state = robot.getState();
            if (state != RobotState.LOCK && state != RobotState.WAIT_EXIT && state != RobotState.DO_EXIT) {
                result++;
            }
        }
        return result;
    }

    /**
     * 向data server请求机器人
     */
    private void acquireRobotPlayers() {
        BjlTableWrapper config = BjlMgr.getInstance().getRoomConfig(this.roomId);
        // 房间没开启，不用请求机器人
        if (!config.isOpen()) {
            this.allExitRoom();
            return;
        }

        int result = 0;
        long goldLower = config.getRobotEnterGoldLower();
        long goldUpper = config.getRobotEnterGoldUpper();

        // 期望获取的机器人数量
        int robotsValid = this.getRobotsValid();
        int expectRobots = config.getRobotScheduleNum();
        if (robotsValid + expectRobots > config.getRobotMaxCount()) {
            expectRobots = config.getRobotMaxCount() - robotsValid;
        }

        if (expectRobots > 0) {
            Map<Long, Player> players = BjlMgr.getInstance().playerService.acquireRobotPlayers(expectRobots, goldLower, goldUpper);
            for (Player player : players.values()) {
                Robot robot = new Robot(player, config.getId(), this);
                this.robots.put(player.getId(), robot);
            }
            result = players.size();
        }
        Map<RobotState, Integer> map = new HashMap<>();
        for (Robot robot : this.robots.values()) {
            RobotState state = robot.getState();
            if (map.get(state) == null) {
                map.put(state, 0);
            }
            map.put(state, map.get(state) + 1);

        }

        if (this.robots.size() > (config.getRobotMaxCount() / 4 * 3)) {
            if (map.get(RobotState.GAME) < config.getRobotMaxCount() / 2) {
                this.releaseAllRobots();
            }
        }
        LOG.info("[机器人] 房间[{}]期望获取数量={} 配置基础数量={} 有效数量={} 实际获取数据={} 最终有效数量={},机器人数量[{}],机器人状态{}", config.getId(), expectRobots, config.getRobotBaseCount(), robotsValid, result, this.getRobotsValid(), this.robots.size(), map);

    }

    /**
     * 所有的机器人退出房间
     */
    private void allExitRoom() {
        for (Robot robot : this.robots.values()) {
            if (robot.getState() != RobotState.LOCK) {
                robot.exitRoom();
            }
        }
    }

    /**
     * 打印当前房间机器人状态
     */
    private void printRobotStatus() {
        ConcurrentHashMap<RobotState, Integer> status = new ConcurrentHashMap<>();
        for (Robot robot : this.robots.values()) {
            status.merge(robot.getState(), 1, Integer::sum);
        }

        LOG.info("[机器人] roomid={} total={} status={}", this.roomId, this.robots.size(), status);
    }

    RobotRoom(int roomId) {
        this.roomId = roomId;
    }

    Robot getRobot(long playerId) {
        return this.robots.get(playerId);
    }

    int getRoomId() {
        return this.roomId;
    }

    /**
     * 运行请求获取机器人方法
     */
    void run() {
        this.acquireRobotPlayers();
    }

    /**
     * 机器人驱动频率(多少秒驱动一次)
     */
    void robotScheduleRate() {
        this.printRobotStatus();

        this.acquireRobotPlayers();

        // 机器人调度
        int scheduleNum = BjlMgr.getInstance().getRoomConfig(this.roomId).getRobotScheduleNum();
        if (scheduleNum > 1) {
            scheduleNum = RandomUtil.randomInt(1, scheduleNum + 1);
        }
        for (Robot robot : this.robots.values()) {
            // 退出
            if (robot.getState() == RobotState.WAIT_EXIT) {
                // 只是标记出来要退出，还未实质退出，不用从map删除
                robot.exitRoom();
                continue;
            }
            // 每次调度的人数，不能一次就全部加进去
            if (robot.getState() == RobotState.NONE) {
                robot.enterRoom();
            }
        }
    }

    /**
     * 释放已锁定的机器人
     */
    void releaseLockRobots() {
        for (Iterator<Map.Entry<Long, Robot>> iterator = this.robots.entrySet().iterator(); iterator.hasNext();) {
            Robot robot = iterator.next().getValue();
            if (robot.getState() == RobotState.LOCK) {
                robot.player.setPlay(false);
                iterator.remove();
            }
        }
    }

    /**
     * 释放阻塞的机器人（等待进入房间、等待退出房间状态卡死）
     */
    void releaseBlockRobots() {
        for (Iterator<Map.Entry<Long, Robot>> iterator = this.robots.entrySet().iterator(); iterator.hasNext();) {
            Robot robot = iterator.next().getValue();
            if (robot.isBlock()) {
                robot.player.setPlay(false);
                iterator.remove();
            }
        }
    }

    /**
     * 释放所有机器人
     */
    void releaseAllRobots() {
        for (Iterator<Map.Entry<Long, Robot>> iterator = this.robots.entrySet().iterator(); iterator.hasNext();) {
            Robot robot = iterator.next().getValue();
            if (robot.getState() != RobotState.GAME) {
                robot.exitRoom();
                robot.player.setPlay(false);
                iterator.remove();
            }
        }
    }
}
