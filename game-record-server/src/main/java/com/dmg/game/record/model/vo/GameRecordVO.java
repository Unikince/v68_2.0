package com.dmg.game.record.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:52 2019/11/29
 */
@Data
public class GameRecordVO {

    private Long id;
    /**
     * 游戏牌局时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gameDate;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 游戏id
     */
    private Integer gameId;
    /**
     * 下注金币
     */
    private BigDecimal betsGold;
    /**
     * 输赢金币
     */
    private BigDecimal winLosGold;
    /**
     * 游戏前金币
     */
    private BigDecimal beforeGameGold;
    /**
     * 游戏后金币
     */
    private BigDecimal afterGameGold;
}
