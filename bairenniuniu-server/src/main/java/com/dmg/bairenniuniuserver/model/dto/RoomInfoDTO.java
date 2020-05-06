package com.dmg.bairenniuniuserver.model.dto;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.dmg.bairenniuniuserver.model.Seat;
import com.dmg.bairenniuniuserver.model.Table;

import lombok.Data;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/31 16:32
 * @Version V1.0
 **/
@Data
public class RoomInfoDTO {
    /**
     * 游戏当前局数
     */
    private int curRound;
    /**
     * 当前房间等级(1,2,3...)
     */
    private int level;
    /**
     * 房间id
     */
    private int roomId;
    /**
     * 房间状态
     */
    private int roomStatus;
    /**
     * 动作倒计时
     */
    private long countdownTime;
    /**
     * 庄家
     */
    private Seat banker;
    /**
     * 是否为系统庄家
     */
    private boolean systemBanker;
    /**
     * 自己的座位
     */
    private Seat selfSeat;
    /**
     * 内场玩家
     */
    private Map<String, Seat> infieldSeatMap = new TreeMap<>();
    /**
     * 5个牌桌位 key:牌位 value:牌桌
     */
    private Map<String, Table> tableMap = new HashMap<>();
    /**
     * 有牛统计 key:牌位 value:是否有牛
     */
    private Map<String, LinkedList<Boolean>> reportFormMap = new HashMap<>();

    /**
     * 房间结算信息
     */
    private SettleResultDTO settleResultDTO=new SettleResultDTO();
    
    /**
     * 赌神第一次下注位置
     */
    private String godOfGamblersFirstBetTable =null;
}