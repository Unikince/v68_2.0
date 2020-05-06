package com.dmg.bairenzhajinhuaserver.model.dto;

import com.dmg.bairenzhajinhuaserver.model.Seat;
import com.dmg.bairenzhajinhuaserver.model.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/31 16:32
 * @Version V1.0
 **/
@Data
public class RoomInfoDTO {
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
    private Map<String,Seat> infieldSeatMap = new TreeMap<>();
    /**
     *  5个牌桌位 key:牌位 value:牌桌
     */
    private Map<String, Table> tableMap = new HashMap<>();
    /**
     *  走势图记录
     */
    private Map<String, LinkedList<Map<String,Integer>>> reportFormMap = new HashMap<>();

    private BigDecimal taiHong;//房间台红值
    
    private BigDecimal restCanBetChip;//房间剩余可投注金额
    
    private boolean bankerChanged;//是否换庄
    
    private long nextcountdownTime; // 下轮开始倒计时

    private Integer roomLevel;//房间等级
    
    /**
     * 赌神第一次下注位置
     */
    private String godOfGamblersFirstBetTable =null;
    
}