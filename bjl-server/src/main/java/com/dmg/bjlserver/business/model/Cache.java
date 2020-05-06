package com.dmg.bjlserver.business.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.dmg.bjlserver.business.model.room.Seat;
import com.dmg.bjlserver.business.model.room.Table;

/**
 * 缓存
 */
public class Cache {
    /** 区域倍数 */
    private final static List<Integer> areaMultiples = new ArrayList<>();

    /** 游戏场次:场次id->场次对象 */
    private final static ConcurrentHashMap<Integer, Table> tables = new ConcurrentHashMap<>();
    /** 玩家座位:玩家id->座位 */
    private final static ConcurrentHashMap<Long, Seat> playerSeats = new ConcurrentHashMap<>();

    static {
        initAreaMultiples();
    }

    /** 初始化区域倍数 */
    private static void initAreaMultiples() {
        areaMultiples.add(8);
        areaMultiples.add(1);
        areaMultiples.add(1);
        areaMultiples.add(11);
        areaMultiples.add(11);
    }

    /**
     * 获取场次对象
     *
     * @param id 场次id
     * @return 场次
     */
    public static Table getTable(int id) {
        return tables.get(id);
    }

    /**
     * 获取所有场次对象
     *
     * @return 场次对象集合
     */
    public static Collection<Table> getAllTable() {
        return tables.values();
    }

    /**
     * 添加场次对象到缓存
     *
     * @param table 场次对象
     */
    public static void addTable(Table table) {
        if (table.id == 0) {
            return;
        }
        tables.put(table.id, table);
    }

    /**
     * 从缓存中移除场次对象
     *
     * @param id 场次id
     * @return 被移除的场次对象
     */
    public static Table removeTable(int id) {
        return tables.remove(id);
    }

    /**
     * 获取玩家座位
     *
     * @param id 玩家id
     * @return 玩家座位
     */
    public static Seat getSeat(long id) {
        return playerSeats.get(id);
    }

    /**
     * 获取所有座位
     *
     * @return 座位集合
     */
    public static Collection<Seat> getAllSeat() {
        return playerSeats.values();
    }

    /**
     * 添加座位到缓存
     *
     * @param seat 座位
     */
    public static void addSeat(Seat seat) {
        if (seat.playerId == 0) {
            return;
        }
        playerSeats.put(seat.playerId, seat);
    }

    /**
     * 从缓存中移除座位
     *
     * @param id 玩家id
     * @return 被移除的座位
     */
    public static Seat removeSeat(long id) {
        return playerSeats.remove(id);
    }

    /* ================================getter================================ */

    /**
     * 获取：区域倍数
     *
     * @return 区域倍数
     */
    public static List<Integer> getAreaMultiples() {
        return areaMultiples;
    }

    /**
     * 获取：游戏场次:场次id->场次
     *
     * @return 游戏场次:场次id->场次
     */
    public static ConcurrentHashMap<Integer, Table> getTables() {
        return tables;
    }

    /**
     * 获取：玩家座位:玩家id->座位
     *
     * @return 玩家座位:玩家id->座位
     */
    public static ConcurrentHashMap<Long, Seat> getPlayerseats() {
        return playerSeats;
    }

}
