package com.dmg.bjlserver.business.model.room;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 下注区域
 */
public class Area {
    /** 区域id */
    public final int id;
    /** 倍数 */
    public final int multiple;
    /** 下注合计 */
    public final AtomicLong betCount;
    /** 区域下注信息集合:玩家id->区域下注信息 */
    public final Map<Long, AreaBetInfo> areaBetInfoMap;

    /**
     * 构造方法
     *
     * @param id 区域id
     * @param multiple 倍数
     */
    public Area(int id, int multiple) {
        this.id = id;
        this.multiple = multiple;
        this.betCount = new AtomicLong();
        this.areaBetInfoMap = new ConcurrentHashMap<>();
    }
}
