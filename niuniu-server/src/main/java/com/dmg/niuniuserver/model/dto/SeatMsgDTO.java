package com.dmg.niuniuserver.model.dto;

import com.dmg.niuniuserver.model.bean.Poker;
import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/3 15:00
 * @Version V1.0
 **/
@Data
public class SeatMsgDTO {
    /**
     * 座位状态
    */
    private Integer seatStatus;
    /**
     * 手牌
     */
    private List<Poker> handCards;
    /**
     * 玩家是否在线
     */
    private boolean playerOnline;
    /**
     * 玩家id
     */
    private Long userId;
    /**
     * 钱
     */
    private double gold;
    /**
     * 手牌数
     */
    private Integer cardsNum;
    /**
     * 座位id
     */
    private Integer seatId;
    /**
     * 是否托管
     */
    private boolean trusteeship;
    /**
     * 是否看牌
     */
    private boolean seeCard;
    /**
     * 自动跟注
     */
    private boolean autoBet;
    /**
     * 是否准备
     */
    private boolean ready;
    /**
     * 总下注数
     */
    private double totalBet;
    /**
     * 准备倒计时
     */
    private long readyToTime;
}