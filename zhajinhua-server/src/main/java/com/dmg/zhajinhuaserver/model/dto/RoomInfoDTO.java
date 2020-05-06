package com.dmg.zhajinhuaserver.model.dto;

import com.dmg.zhajinhuaserver.model.bean.Poker;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/3 19:31
 * @Version V1.0
 **/
@Data
public class RoomInfoDTO {
    private TableInfo tableInfo;
    private Map<String,SeatInfo> seatInfoMap;
    private Map<String,PlayerInfo> playerInfoMap;
    private Map<String,Object> actionInfo;


    @Data
    public static class TableInfo{
        /**
         * 当前局数
         */
        private int curRound;
        /**
         * 最大局数
         */
        private int maxRound;
        /**
         * 封顶下注
         */
        private int topBet;
        /**
         * 房间id
         */
        private int roomId;
        /**
         * 房间状态
         */
        private int roomStatus;
        /**
         *房间类型
         */
        private int roomType;
        /**
         *  创建者id
         */
        private long creatorId;
        /**
         * 创建时间
         */
        private long createDate;
        /**
         * 房间等级
         */
        private int roomLevel;
        /**
         * 最低限制
         */
        private int lowerLimit;
        /**
         * 最高限制
         */
        private int upperLimit;
        /**
         * 底分
         */
        private int baseScore;
        /**
         * 当前玩家人数
         */
        private int curPlayerNumber;
        /**
         * 总人数
         */
        private int totalPlayer;
        /**
         * 当前动作类型
         */
        private int actionType;
    }

    @Data
    public static class SeatInfo{
        /**
         * 座位状态
         */
        private int seatStatus;
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
        private long userId;
        /**
         * 玩家name
         */
        private String nickname;
        /**
         * 钱
         */
        private double gold;
        /**
         * 手牌数
         */
        private int cardsNum;
        /**
         * 手牌类型 参看枚举:Combination
         */
        private int handCardType;
        /**
         * 座位id
         */
        private int seatId;
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
        /**
         * 是否玩过
         */
        private boolean hasPlayed;
        /**
         * 动作类型
         */
        private int actionType;
        /**
         * 抢庄倍数
         */
        private Integer qiangZhuangMultiple;
        /**
         * 玩家椅子下注筹码
         */
        private long betChips;
        /**
         * 是否庄家
         */
        private boolean bankerFlag;
    }

    @Data
    public static class PlayerInfo{
        private long userId;
        private String nickname;
        private String headImage;
        private int sex;
        private double gold;
    }
}