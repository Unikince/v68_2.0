package com.dmg.fish.business.model.room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.dmg.gameconfigserverapi.fish.dto.FishCtrlReturnRateDTO;

import cn.hutool.core.util.RandomUtil;

/**
 * 捕鱼房间
 */
public class Room {
    /** 房间id */
    public final int id;
    /** 房间名称 */
    public final String name;
    /** 桌子 */
    public final List<Table> tables;

    /** 房间控制模型 */
    public FishCtrlReturnRateDTO ctrlModel;

    /** 变化库存值 */
    private StockValue changeStock;
    /** 读取缓存库存值 */
    private StockValue readStock;

    public Room(int id, String name, int talbleNum, int chairNum, List<Integer> scences) {
        this.id = id;
        this.name = name;
        this.tables = this.createTables(talbleNum, chairNum, scences);
        this.changeStock = new StockValue();
        this.readStock = new StockValue();
    }

    private List<Table> createTables(int tableNum, int chairNum, List<Integer> scences) {
        List<Table> tables = new ArrayList<>();
        for (int tableId = 1; tableId <= tableNum; tableId++) {
            tables.add(new Table(tableId, scences, chairNum, this));
        }

        return Collections.unmodifiableList(tables);
    }

    /**
     * 随机给真人找空位置,优先查找有人的桌子
     *
     * @param excludeTbl 要排除的桌子
     */
    public Seat findEmptySeatForPlayer(Table excludeTbl) {
        Seat emptySeat = null;
        int tblSize = this.tables.size();
        int tblFrom = RandomUtil.randomInt(tblSize);

        // 随机桌子找
        for (int i = tblFrom; i < (tblFrom + tblSize); i++) {
            Table table = this.tables.get(i % tblSize);
            boolean hasPlayer = false;
            Seat tblEmptySeat = null;

            if (table == excludeTbl) {
                continue;
            }

            int seatSize = table.seats.size();
            int seatFrom = RandomUtil.randomInt(table.seats.size());
            // 随机位置找
            for (int j = seatFrom; j < (seatFrom + seatSize); j++) {
                Seat seat = table.seats.get(j % seatSize);
                if (seat.playerId == 0) {
                    emptySeat = seat;
                    tblEmptySeat = seat;
                } else {
                    hasPlayer = true;
                }
            }

            if (hasPlayer && (tblEmptySeat != null)) {
                return tblEmptySeat;
            }
        }

        return emptySeat;
    }

    /**
     * 随机给真人找空位置,优先查找有人的桌子
     *
     * @param excludeTbl 要排除的桌子
     * @param robotUpper 桌子机器人上限
     */
    public Seat findEmptySeatForRobot(Table excludeTbl, int robotUpper) {
        Seat emptySeat = null;
        int tblSize = this.tables.size();
        int tblFrom = RandomUtil.randomInt(tblSize);

        // 随机桌子找
        for (int i = tblFrom; i < (tblFrom + tblSize); i++) {
            Table table = this.tables.get(i % tblSize);
            Seat tblEmptySeat = null;
            int robotNum = 0;

            if (table == excludeTbl) {
                continue;
            }

            int seatSize = table.seats.size();
            int seatFrom = RandomUtil.randomInt(table.seats.size());
            // 随机位置找
            for (int j = seatFrom; j < (seatFrom + seatSize); j++) {
                Seat seat = table.seats.get(j % seatSize);
                if (seat.playerId == 0) {
                    emptySeat = seat;
                    tblEmptySeat = seat;
                } else if (seat.robot) {
                    robotNum++;
                }
            }

            if ((tblEmptySeat != null) && (robotNum > 0) && (robotNum < robotUpper)) {
                return tblEmptySeat;
            }
        }

        return emptySeat;
    }

    /**
     * 下注值增加
     */
    public synchronized void bet(long value) {
        this.changeStock.bet += value;
    }

    /**
     * 赔付值增加
     */
    public synchronized void payout(long value) {
        this.changeStock.payout += value;
    }

    /**
     * 库存改变值，用于改变redis,每次调用将会清楚改变值
     */
    public synchronized StockValue getChangeStock() {
        StockValue result = new StockValue();
        result.bet = this.changeStock.bet;
        result.payout = this.changeStock.payout;
        this.changeStock.bet = 0;
        this.changeStock.payout = 0;
        return result;
    }

    /**
     * 从redis读取的库存值缓存到内存
     */
    public synchronized void setReadStock(long bet, long payout) {
        this.readStock.bet = bet;
        this.readStock.payout = payout;
    }

    /**
     * 库存值，用于程序计算
     */
    public synchronized StockValue getReadStock() {
        StockValue result = new StockValue();
        result.bet = this.readStock.bet;
        result.payout = this.readStock.payout;
        return result;
    }
}
