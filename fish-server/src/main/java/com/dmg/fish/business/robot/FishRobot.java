package com.dmg.fish.business.robot;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dmg.common.core.util.SpringUtil;
import com.dmg.fish.business.config.dic.FishRoomDic;
import com.dmg.fish.business.config.dic.FishRouteDic;
import com.dmg.fish.business.config.wrapper.FishRoomWrapper;
import com.dmg.common.pb.java.Fish;
import com.dmg.fish.business.platform.model.Player;
import com.dmg.fish.business.platform.model.Robot;

import cn.hutool.core.util.RandomUtil;

/**
 * 捕鱼机器人
 */
public class FishRobot {
    private static final Logger LOG = LoggerFactory.getLogger(FishRobot.class);
    private static ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(5 + 1, (r) -> new Thread(r));
    /** 玩家数据 */
    public Player data;
    /** 是否使用，空为未使用，true为使用中，false为等待清理 */
    public Boolean use;
    /** 分配的房间 */
    public int roomId;
    /** 座位顺序 */
    public int order;
    /** 炮台分数信息 */
    public int batteryScore;
    /** 是否暂停发炮(模拟切换场景的时候不能发炮) */
    public boolean pauseFire;
    /** 游戏开始时间 */
    public long sceneTime;
    /** 鱼 */
    public Map<Integer, Fish.FishInfo> fishs = new HashMap<>();
    /** 开炮角度 */
    private int angle;
    /** 机器人多少毫秒射击一次 */
    private int fireRate;
    /** 每次发炮时换炮概率(万分比) */
    private int changeBatteryRate;
    /** 每次发炮时换角度概率(万分比) */
    private int robotChangeAngleRate;
    /** 子弹id生成seed */
    private AtomicInteger bulletIdSeed = new AtomicInteger(0);
    /** 屏幕上的鱼的数量(有一定延时) */
    private int screenFishNum;
    private Future<?> fireFuture;
    private Future<?> calcScreenFishsFuture;

    public long getId() {
        return this.data.getId();
    }

    public FishRobot(Robot r) {
        this.data = r;
    }

    /**
     * 清理对象
     */
    public void clear() {
        this.use = null;
        this.roomId = 0;
        this.order = 0;
        this.batteryScore = 0;
        this.pauseFire = false;
        this.sceneTime = 0;
        this.fishs.clear();
        this.angle = 0;
        this.fireRate = 0;
        this.changeBatteryRate = 0;
        this.robotChangeAngleRate = 0;
        this.bulletIdSeed = new AtomicInteger(0);
        this.screenFishNum = 0;
        this.fireFuture = null;
        this.calcScreenFishsFuture = null;
    }

    public void schedule() {
        this.use = true;
        FishRoomWrapper fishRoomBean = SpringUtil.getBean(FishRoomDic.class).get(this.roomId);
        this.fireRate = fishRoomBean.getRobotFireRate();
        this.changeBatteryRate = fishRoomBean.getRobotChangeBatteryRate();
        this.robotChangeAngleRate = fishRoomBean.getRobotChangeAngleRate();
        // 请求进入房间
        RobotNetUtil.send(this.getId(), 201998, Fish.ReqEnterRoom.newBuilder().setRoomId(this.roomId).build());
        FishRobot.LOG.info("[捕鱼]机器人[{}]请求进入房间", this.getId());
    }

    /**
     * schedule开炮
     */
    public void scheduleFire() {
        this.fireFuture = this.scheduleWithFixedDelay(this::doFire, 2000, this.fireRate, TimeUnit.MILLISECONDS);
        this.calcScreenFishsFuture = this.scheduleWithFixedDelay(this::calcScreenFishs, 2, 3, TimeUnit.SECONDS);
    }

    /**
     * 发炮
     */
    private void doFire() {
        if (this.use == false) {
            return;
        }
        if (this.data.getGold() < this.batteryScore) {
            FishRobot.LOG.info("[捕鱼]机器人[{}][{}]鱼币不足[{}]炮退出游戏", this.data.getId(), this.data.getNickname(), this.batteryScore);
            RobotNetUtil.send(this.getId(), Fish.FishMessageId.ReqExitRoom_ID_VALUE, Fish.ReqExitRoom.newBuilder().build());
            this.logout();
            return;
        }

        if (this.screenFishNum < 5) {
            return;
        }
        if ((this.screenFishNum < 30) && RandomUtil.randomInt(10) < 5) {
            return;
        }

        if ((this.screenFishNum < 60) && RandomUtil.randomInt(10) < 4) {
            return;
        }

        if (RandomUtil.randomInt(10000) < 9000) {// 百分之90概率发炮
            Fish.ReqFire.Builder fireMsg = Fish.ReqFire.newBuilder();
            fireMsg.setBulletId(this.bulletIdSeed.incrementAndGet());
            // 是否切换角度(20分之1的概率)
            if (RandomUtil.randomInt(10000) < this.robotChangeAngleRate) {
                this.angle = RandomUtil.randomInt(-75, 75 + 1);
                fireMsg.setAngle(this.angle);
            } else {
                fireMsg.setAngle(this.angle);
            }

            RobotNetUtil.send(this.getId(), Fish.FishMessageId.ReqFire_ID_VALUE, fireMsg.build());
        }
        if (RandomUtil.randomInt(10000) < this.changeBatteryRate) {// 加减炮?
            if (RandomUtil.randomInt(2) < 1) {
                RobotNetUtil.send(this.getId(), Fish.FishMessageId.ReqPlusBattery_ID_VALUE, Fish.ReqPlusBattery.newBuilder().build());
            } else {
                RobotNetUtil.send(this.getId(), Fish.FishMessageId.ReqMinusBattery_ID_VALUE, Fish.ReqMinusBattery.newBuilder().build());
            }
        }
    }

    /**
     * 计算屏幕鱼的数量
     */
    private void calcScreenFishs() {
        long now = System.currentTimeMillis();
        FishRouteDic fishRouteDic = SpringUtil.getBean(FishRouteDic.class);
        int screenFishNum = 0;
        for (Fish.FishInfo fish : this.fishs.values()) {
            long fishTime = ((now - this.sceneTime) + fish.getTime()) / 1000;
            if ((fishTime > 0) && ((fishTime * fish.getSpeed()) < fishRouteDic.get(fish.getRoute()).getLength())) {
                screenFishNum++;
            }
        }

        this.screenFishNum = screenFishNum;
    }

    /**
     * 停止开炮
     */
    public void stopFire() {
        if (this.fireFuture != null) {
            this.fireFuture.cancel(true);
        }
    }

    public void logout() {
        this.use = false;
        this.stopFire();
        this.fishs.clear();
        if (this.calcScreenFishsFuture != null) {
            this.calcScreenFishsFuture.cancel(true);
        }
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable cmd, long initDelay, long delay, TimeUnit unit) {
        return FishRobot.scheduledExecutor.scheduleWithFixedDelay(cmd, initDelay, delay, unit);
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable cmd, long initDelay, long period, TimeUnit unit) {
        return FishRobot.scheduledExecutor.scheduleAtFixedRate(cmd, initDelay, period, unit);
    }

    public ScheduledFuture<?> schedule(Runnable cmd, long delay, TimeUnit unit) {
        return FishRobot.scheduledExecutor.schedule(cmd, delay, unit);
    }

}
