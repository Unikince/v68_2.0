package com.dmg.fish.business.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.dmg.fish.business.model.fish.Fish;
import com.dmg.fish.business.model.room.Table;

/**
 * 鱼场景
 */
public class FishScence {
    /** 场景id */
    public final int id;
    /** 桌子 */
    public final Table table;
    /** 鱼 */
    public final Map<Integer, Fish> fishs = new ConcurrentHashMap<>();
    /** 本场景鱼的id生成种子 */
    public int fishIdSeed = 0;

    public FishScence(int id, Table table) {
        this.id = id;
        this.table = table;
    }

    /**
     * 重置场景
     */
    public void reset() {
        this.fishs.clear();
        this.fishIdSeed = this.id * 1000000;
    }
}
