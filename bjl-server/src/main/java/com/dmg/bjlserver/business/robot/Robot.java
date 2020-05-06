package com.dmg.bjlserver.business.robot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dmg.bjlserver.business.config.wrapper.BjlTableWrapper;
import com.dmg.bjlserver.business.logic.BjlMgr;
import com.dmg.bjlserver.business.model.Cache;
import com.dmg.bjlserver.business.model.room.Seat;
import com.dmg.bjlserver.business.platform.model.Player;
import com.dmg.common.pb.java.Bjl;

import cn.hutool.core.util.RandomUtil;

/**
 * 机器人基础类
 *
 * @author wh
 */
public class Robot {
    /**
     * 日志记录对象
     */
    private static final Logger LOG = LoggerFactory.getLogger(Robot.class);
    /**
     * 机器人对应的玩家对象
     */
    public final Player player;
    /**
     * 所在房间的id
     */
    final int roomId;
    /**
     * 所属机器人房间实例
     */
    private final RobotRoom room;
    /**
     * 当前状态
     */
    private RobotState state = RobotState.NONE;
    /**
     * 已进行游戏回合数
     */
    private int rounds = 0;

    /**
     * 机器人状态最后一次更新的时间戳，单位：ms
     */
    private long lastTimeStamp = System.currentTimeMillis();

    /**
     * 默认阻塞超时时间（等待进入房间和等待退出房间）,单位：ms
     */
    private static final long defaultBlockTime = 3 * 60 * 1000;

    /**
     * 获取当前机器人的状态
     *
     * @return
     */
    RobotState getState() {
        return this.state;
    }

    /**
     * 获取机器人的id
     *
     * @return
     */
    Long getId() {
        return this.player.getId();
    }

    /**
     * 设置状态
     *
     * @param state
     */
    private void setState(RobotState state) {
        this.state = state;
    }

    /**
     * 检查该机器人是否已退出房间
     */
    private void checkPlayerLogOut() {
        boolean logout = this.getState() == RobotState.WAIT_EXIT || this.getState() == RobotState.DO_EXIT || this.getState() == RobotState.LOCK;
        if (logout) {
            return;
        }

        // 检查，满足条件的就可以锁定离场了
        BjlTableWrapper roomBean = BjlMgr.getInstance().getRoomConfig(this.room.getRoomId());

        // check局数
        int deadRounds = roomBean.getRobotOutRound();
        if (this.rounds > RandomUtil.randomInt(deadRounds / 2, deadRounds + 1)) {
            this.setState(RobotState.WAIT_EXIT);
            return;
        }

        long gold = this.player.getGold();
        // 下注门槛
        if (gold <= roomBean.getChipList().get(0)) {
            this.setState(RobotState.WAIT_EXIT);
            return;
        }

        // 超过最大入场金币上限或低于最小入场金币下限需要退出房间
        if ((gold < roomBean.getRobotEnterGoldLower()) || (gold > roomBean.getRobotEnterGoldUpper())) {
            this.setState(RobotState.WAIT_EXIT);
        }
    }

    /**
     * 下注
     *
     * @param betTime
     */
    private void bet(long betTime) {
        if (this.state != RobotState.GAME) {
            return;
        }

        this.rounds++;
        BjlTableWrapper roomBean = BjlMgr.getInstance().getRoomConfig(this.room.getRoomId());
        // 如果金币低于门槛， 禁止下注
        if (this.player.getGold() <= roomBean.getChipList().get(0)) {
            return;
        }

        int betTimes = roomBean.getRandomRobotBetCount();

        List<Long> timeList = new ArrayList<>();
        for (int i = 0; i < betTime; i++) {
            timeList.add((long) i);
        }

        for (int i = 0; i < betTimes; i++) {
            long delay = RandomUtil.randomEle(timeList);
            RobotMgr.getInstance().robotWorker.schedule(() -> {
                long chip = roomBean.getRandomRobotBetChipValue();// 下注筹码数额
                int area = roomBean.getRandomRobotBetArea();// 下注区域
                if (this.player.getGold() >= chip) {
                    Bjl.ReqBet.Builder req = Bjl.ReqBet.newBuilder();
                    Bjl.AreaBetInfo.Builder betInfo = Bjl.AreaBetInfo.newBuilder();
                    betInfo.setArea(area);
                    betInfo.setGold(chip);
                    req.setBet(betInfo);
                    RobotMgr.getInstance().pushMessage(this.getId(), Bjl.BjlMessageId.ReqBet_ID_VALUE, req.build());
                }
            }, delay, TimeUnit.SECONDS);
        }
    }

    /**
     * 构造函数
     *
     * @param player
     * @param roomId
     * @param room
     */
    Robot(Player player, int roomId, RobotRoom room) {
        this.player = player;
        this.roomId = roomId;
        this.room = room;
    }

    /**
     * 申请进入游戏房间
     */
    void enterRoom() {
        Bjl.ReqEnterRoom.Builder req = Bjl.ReqEnterRoom.newBuilder();
        req.setRoomId(this.roomId);
        RobotMgr.getInstance().pushMessage(this.player, Bjl.BjlMessageId.ReqEnterRoom_ID_VALUE, req.build());
        this.setState(RobotState.WAIT_ENTER);
    }

    /**
     * 申请退出游戏房间
     */
    void exitRoom() {
        Seat seat = Cache.getSeat(this.player.getId());
        if (seat == null) {
            this.setState(RobotState.LOCK);
        } else {
            Bjl.ReqExitRoom.Builder req = Bjl.ReqExitRoom.newBuilder();
            RobotMgr.getInstance().pushMessage(this.getId(), Bjl.BjlMessageId.ReqExitRoom_ID_VALUE, req.build());
            this.setState(RobotState.DO_EXIT);
            this.player.setPlay(false);
            LOG.info("[机器人] playerId={} 申请退出房间", this.getId());
        }
    }

    void onResEnterRoom(Bjl.ResEnterRoom msg) {
        if (msg.getCode() == Bjl.BjlCode.SUCCESS) {
            this.setState(RobotState.GAME);
        } else {
            // 不能成功进入房间，标记出来，等一段时间回收回去
            this.setState(RobotState.LOCK);
        }
    }

    void onResExitRoom(Bjl.ResExitRoom msg) {
        try {
            if (msg.getCode() == Bjl.BjlCode.SUCCESS) {
                this.setState(RobotState.LOCK);
            } else {
                this.setState(RobotState.WAIT_EXIT);
            }
        } catch (Exception e) {
            this.setState(RobotState.LOCK);
            LOG.error("百家乐响应退出时出错，错误堆栈：", e);
        }
    }

    void onResStartBet(Bjl.ResStage msg) {
        LOG.info("机器人[{}]接收到下注开始消息，准备下注", this.player.getId());
        // 进入下注阶段
        if (msg.getStage() == 1) {
            this.checkPlayerLogOut();
            this.bet(msg.getTime());
        }
    }

    /**
     * 下注成功返回（自己与场次6人返回消息）
     *
     * @param msg
     */
    void onResDoBetAlone(Bjl.ResPlayerBet msg) {
        if (msg.getCode().getNumber() == 0 && msg.getPlayerId() == this.getId()) {
            this.lastTimeStamp = System.currentTimeMillis();
            this.player.setGold(this.player.getGold() - msg.getBet().getGold());
        }
    }

    /**
     * 处理结算
     *
     * @param msg
     */
    void onResEventGameEnd(Bjl.ResBalance msg) {
        this.player.setGold(this.player.getGold() + msg.getPlayerGold());
    }

    /**
     * 判断是否阻塞，不带参数（默认阻塞超时时间 1800s）
     *
     * @return
     */
    boolean isBlock() {
        boolean flag = false;
        if (this.state == RobotState.WAIT_ENTER) {
            flag = (System.currentTimeMillis() - this.lastTimeStamp - 5000) >= 0;
        } else {
            flag = (System.currentTimeMillis() - this.lastTimeStamp - defaultBlockTime) >= 0;
        }
        return flag;
    }
}
