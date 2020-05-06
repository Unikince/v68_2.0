package com.dmg.fish.business.model.room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import com.dmg.fish.business.model.FishScence;

/**
 * 捕鱼桌子
 */
public class Table {
    /** 所属房间 */
    public final Room room;
    /** 桌子id */
    public final int id;
    /** 座位(数目固定) */
    public final List<Seat> seats;
    /** 桌子中的场景 */
    public final List<FishScence> scences;
    /** 当前场景Index */
    public int scenceIndex = 0;
    /** 代替机器人检测碰撞的leader */
    public Seat leader;
    /** 切换场景Future */
    public ScheduledFuture<?> switchScenceFuture = null;
    /** 子弹统计 */
    public int bulletCount = 0;

    public Table(int id, List<Integer> scenceIds, int chairNum, Room room) {
        this.room = room;
        this.id = id;
        this.seats = this.createSeats(chairNum);
        this.scences = this.createScences(scenceIds);
    }

    /** 创建座位 */
    private List<Seat> createSeats(int chairNum) {
        List<Seat> seats = new ArrayList<>(chairNum);
        for (int i = 0; i < chairNum; i++) {
            seats.add(new Seat(i, this));
        }
        return Collections.unmodifiableList(seats);
    }

    /** 创建场景 */
    private List<FishScence> createScences(List<Integer> scenceIds) {
        List<FishScence> scences = new ArrayList<>();
        for (Integer scenceId : scenceIds) {
            scences.add(new FishScence(scenceId, this));
        }
        return Collections.unmodifiableList(scences);
    }

    /** 根据顺序获取seat */
    public Seat getSeat(int order) {
        return this.seats.get(order);
    }

    /** 有玩家座位的数量 */
    public int playersNum() {
        int num = 0;
        for (Seat s : this.seats) {
            if (s.playerId > 0) {
                num++;
            }
        }
        return num;
    }

    /** 真实玩家的数量 */
    public int realPlayersNum() {
        int num = 0;
        for (Seat s : this.seats) {
            if ((s.playerId > 0) && !s.robot) {
                num++;
            }
        }
        return num;
    }

    /** 当前场景 */
    public FishScence scence() {
        return this.scences.get(this.scenceIndex % this.scences.size());
    }

    /** 上一个场景 */
    public FishScence preScence() {
        return this.scences.get((this.scenceIndex - 1) % this.scences.size());
    }

    /** 下一个场景 */
    public FishScence nextScence() {
        return this.scences.get((this.scenceIndex + 1) % this.scences.size());
    }

    /** 重置数据 */
    public void reset() {
        this.scenceIndex = 0;
        this.leader = null;
        if (this.switchScenceFuture != null) {
            this.switchScenceFuture.cancel(false);
        }
    }

    /** 子弹数增加 */
    public void incrementBullet() {
        this.bulletCount += 1;
    }

    /** 子弹数减少 */
    public void decrementBullet() {
        this.bulletCount -= 1;
    }
}
