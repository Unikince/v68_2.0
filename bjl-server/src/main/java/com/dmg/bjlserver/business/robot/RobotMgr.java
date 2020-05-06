package com.dmg.bjlserver.business.robot;

import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dmg.bjlserver.business.config.wrapper.BjlTableWrapper;
import com.dmg.bjlserver.business.logic.BjlMgr;
import com.dmg.bjlserver.business.platform.model.Player;
import com.dmg.bjlserver.core.msg.ReqPbMessageHandler;
import com.dmg.bjlserver.core.work.WorkerGroup;
import com.dmg.common.core.util.SpringUtil;
import com.dmg.common.pb.java.Bjl;

import lombok.extern.log4j.Log4j2;

/**
 * 百家乐机器人管理类
 *
 * @author wh
 */
@Log4j2
public class RobotMgr {
    /**
     * 日志记录对象
     */
    private static final Logger LOG = LoggerFactory.getLogger(RobotMgr.class);
    /**
     * 机器人管理类单例对象
     */
    private static final RobotMgr INSTANCE = new RobotMgr();
    /**
     * 百家乐房间列表
     */
    private ConcurrentHashMap<Integer, RobotRoom> rooms = new ConcurrentHashMap<>();
    /** 所有机器人 */
    private static Map<Long, Player> allRobots = new ConcurrentHashMap<>();
    /**
     * 机器人工作线程
     */
    public WorkerGroup robotWorker;
    /**
     * 初始化标志
     */
    private boolean isInit = false;

    /**
     * 下次重置机器人时间
     */
    private long nextResetRobotTime = 0;

    /**
     * 重置机器人标志
     */
    private boolean resetRobotFlag = false;

    /**
     * 无参构造函数
     */
    private RobotMgr() {
    }

    /**
     * 获取机器人管理类单例
     *
     * @return
     */
    public static RobotMgr getInstance() {
        return INSTANCE;
    }

    /**
     * 发布消息，游戏管理类处理逻辑
     *
     * @param player
     * @param msgId
     * @param message
     */
    public void pushMessage(Object player, int msgId, com.google.protobuf.Message message) {
        if (!this.isInit) {
            return;
        }
        this.robotWorker.execute(() -> {
            ReqPbMessageHandler handler = (ReqPbMessageHandler) SpringUtil.getBean("" + msgId);
            handler.action(player, message);
        });
    }

    public Map<Long, Player> getAllRobots() {
        return allRobots;
    }

    /**
     * 接收消息，机器人管理类处理逻辑
     *
     * @param playerId
     * @param msgId
     * @param msg
     */
    public void putMessage(long playerId, int msgId, com.google.protobuf.Message msg) {
        if (!this.isInit) {
            return;
        }
        this.robotWorker.execute(() -> {
            for (RobotRoom room : this.rooms.values()) {
                Robot robot = room.getRobot(playerId);
                if (robot == null) {
                    continue;
                }
                switch (msgId) {
                    case Bjl.BjlMessageId.ResEnterRoom_ID_VALUE:
                        // 进入房间
                        robot.onResEnterRoom((Bjl.ResEnterRoom) msg);
                        break;
                    case Bjl.BjlMessageId.ResExitRoom_ID_VALUE:
                        // 退出房间
                        robot.onResExitRoom((Bjl.ResExitRoom) msg);
                        break;
                    case Bjl.BjlMessageId.ResStage_ID_VALUE:
                        // 开始下注
                        robot.onResStartBet((Bjl.ResStage) msg);
                        break;
                    case Bjl.BjlMessageId.ResPlayerBet_ID_VALUE:
                        // 下注成功
                        robot.onResDoBetAlone((Bjl.ResPlayerBet) msg);
                        break;
                    case Bjl.BjlMessageId.ResBalance_ID_VALUE:
                        // 结算
                        robot.onResEventGameEnd((Bjl.ResBalance) msg);
                        break;
                    case Bjl.BjlMessageId.ResOtherPlayerNum_ID_VALUE:
                        // 其他（无座）玩家人数
                        break;
                    case Bjl.BjlMessageId.ResSyncSeat_ID_VALUE:
                        // 金币同步
                        break;
                    case Bjl.BjlMessageId.ResOtherPlayersBet_ID_VALUE:
                        // 其他（无座）玩家下注结果
                        break;
                    case Bjl.BjlMessageId.ResPlayerRatio_ID_VALUE:
                        // 当前机器人/（机器人+真人）
                        break;
                    case Bjl.BjlMessageId.ResGoldChange_ID_VALUE:
                        // 金币变化
                        break;
                    case Bjl.BjlMessageId.ResRankPlayers_ID_VALUE:
                        // 排名玩家
                        break;
                    case Bjl.BjlMessageId.ResShuffleInfo_ID_VALUE:
                        // 洗牌
                        break;
                    default:
                        LOG.info("百家乐机器人管理类在处理消息时，无法找到合适的方法处理该消息，msgId={}", msgId);
                        break;
                }
            }
        });
    }

    /**
     * 添加房间信息
     *
     * @param roomId
     */
    public void addRoom(int roomId) {
//        BjlTableWrapper config = BjlMgr.getInstance().getRoomConfig(roomId);
//        if (!config.isOpen()) {
//            LOG.warn("房间信息未开启 roomId={}", roomId);
//            return;
//        }

        if (this.rooms.containsKey(roomId)) {
            LOG.error("重复添加房间信息 roomId={}", roomId);
            return;
        }
        RobotRoom room = new RobotRoom(roomId);
        this.rooms.put(roomId, room);
    }

    /**
     * 执行方法
     */
    public void run() {
        this.isInit = true;

        if (this.robotWorker != null) {
            this.robotWorker.shutdown();
        }

        this.robotWorker = new WorkerGroup("robotWorker");
        this.robotWorker.execute(() -> {
            for (RobotRoom room : this.rooms.values()) {
                room.run();
            }
        });

        for (RobotRoom room : this.rooms.values()) {
            BjlTableWrapper config = BjlMgr.getInstance().getRoomConfig(room.getRoomId());
            int rate = config.getRobotScheduleTime();
            this.robotWorker.scheduleWithFixedDelay(() -> {
                if (this.resetRobotFlag) {
                    return;
                }
                try {
                    room.robotScheduleRate();
                } catch (Exception e) {
                    log.error("机器人加载错误", e);
                }
            }, 20L, rate, TimeUnit.SECONDS);
        }

        this.robotWorker.scheduleWithFixedDelay(() -> {
            for (RobotRoom room : this.rooms.values()) {
                if (this.resetRobotFlag) {
                    return;
                }
                try {
                    room.releaseLockRobots();
                    // 释放因进入、退出房间失败阻塞的机器人
                    room.releaseBlockRobots();
                } catch (Exception e) {
                    log.error("机器人释放错误", e);
                }

            }
        }, 60L, 120, TimeUnit.SECONDS);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        cal.set(Calendar.HOUR_OF_DAY, 5);
        this.nextResetRobotTime = cal.getTimeInMillis();
        this.resetAllRobot();
    }

    /**
     * 重置所有机器人
     */
    private void resetAllRobot() {
        this.robotWorker.scheduleWithFixedDelay(() -> {
            if (System.currentTimeMillis() < this.nextResetRobotTime) {
                return;
            }
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 1);
            cal.set(Calendar.HOUR_OF_DAY, 5);
            this.nextResetRobotTime = cal.getTimeInMillis();

            this.resetRobotFlag = true;
            try {
                for (RobotRoom room : this.rooms.values()) {
                    room.releaseAllRobots();
                }
                for (Player p : allRobots.values()) {
                    p.setPlay(false);
                }
                for (RobotRoom room : this.rooms.values()) {
                    room.robotScheduleRate();
                }
            } catch (Exception e) {
                log.error("机器人重置错误", e);
            }
            this.resetRobotFlag = false;
        }, 1, 1, TimeUnit.HOURS);
    }

    /**
     * 停止运行
     */
    public void shutdown() {
        this.isInit = false;
        if (this.robotWorker != null) {
            this.robotWorker.shutdown();
        }
        LOG.info("百家乐机器人正常关闭");
    }
}
