package com.dmg.clubserver.model.table;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description
 * @Author mice
 * @Date 2019/5/30 15:55
 * @Version V1.0
 **/
@Data
public class Table implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 牌桌编号
    */
    private Integer tableNum;
    /**
     * 房间id
     */
    private Integer roomId;
    /**
     * 游戏类型
     */
    private Integer gameType;
    /**
     * 牌桌状态 0:等待中 1:游戏中
     */
    private Integer tableStatus=0;
    /**
     * 当前人数
     */
    private Integer currentPlayerNum;
    /**
     * 人数上限
     */
    private Integer playerNumLimit;
    /**
     * 当前局数
     */
    private Integer currentRound;
    /**
     * 总局数
     */
    private Integer totalRound;
    /**
     * 消耗房卡
     */
    private Integer costRoomCard;
    /**
     * 创建者id
     */
    private Integer creatorId;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 牌桌规则
     *
     */
    TableRule tableRule;
    /**
     * 座位信息<座位号,数据>
     */
    private Map<Integer, Seat> seatMap = new ConcurrentHashMap<>();


}