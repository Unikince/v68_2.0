package com.dmg.bjlserver.business.model.game;

import java.util.LinkedList;
import java.util.List;

/**
 * 房间战绩 最新50条
 */
public class TableRecord {
    /** 上局赢家 */
    public List<SeatPlayer> seatWinPlayer = new LinkedList<>();
}
