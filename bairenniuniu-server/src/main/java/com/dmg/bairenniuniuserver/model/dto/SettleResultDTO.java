package com.dmg.bairenniuniuserver.model.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dmg.bairenniuniuserver.model.Table;

import lombok.Data;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/31 14:20
 * @Version V1.0
 **/
@Data
public class SettleResultDTO {
    /**
     * 内场人员的结算信息
     */
    private Map<String, SettleInfo> infieldSettleInfoMap = new HashMap<>();
    /**
     * 外场赢钱数
     */
    private BigDecimal outfieldWinGold = new BigDecimal(0);
    /**
     * 自己的结算信息
     */
    private SettleInfo selfSettleInfo;
    /**
     * 庄家的结算信息
     */
    private SettleInfo bankerSettleInfo;
    /**
     * 5个牌桌位 key:牌位 value:牌桌
     */
    private Map<String, Table> tableMap = new HashMap<>();

    /**
     * 输赢金币排名
     */
    private List<SettleInfo> winGoldRank = new ArrayList<>();

    @Data
    public static class SettleInfo {

        private int seatIndex;
        /**
         * 玩家当前金币
         */
        private BigDecimal gold;
        /**
         * 当局赢分
         */
        private BigDecimal curWinGold = new BigDecimal(0);
        /**
         * 赢钱统计
         * key:牌桌位置 value:赢钱数
         */
        private Map<String, BigDecimal> winGoldMap = new HashMap<>();
        
        /**
         * 玩家id
        */
        private int userId;
        /**
         * 玩家昵称
         */
        private String nickname;
        /**
         * 头像
         */
        private String headIcon;

    }
}