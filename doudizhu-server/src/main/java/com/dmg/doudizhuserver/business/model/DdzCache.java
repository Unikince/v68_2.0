package com.dmg.doudizhuserver.business.model;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** 缓存 */
public interface DdzCache {
    /** 当前未开始房间 */
    Map<Integer, List<Room>> curNotStartRooms = new ConcurrentHashMap<>();
    /** 玩家座位 */
    ConcurrentHashMap<Long, Seat> playerSeats = new ConcurrentHashMap<>();
}
