package com.dmg.bjlserver.business.model.room;

import java.util.ArrayList;
import java.util.List;

/**
 * 区域下注信息
 */
public class AreaBetInfo {
    /** 下注座位 */
    public final Seat seat;
    /** 下注列表 */
    public final List<Long> bets;

    /**
     * 构造方法
     *
     * @param seat 下注座位
     */
    public AreaBetInfo(Seat seat) {
        this.seat = seat;
        this.bets = new ArrayList<>();
    }
}
