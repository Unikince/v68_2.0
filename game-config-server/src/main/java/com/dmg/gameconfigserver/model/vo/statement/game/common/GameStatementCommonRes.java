package com.dmg.gameconfigserver.model.vo.statement.game.common;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 游戏报表_公用_返回
 */
@Data
public class GameStatementCommonRes {
    /** 游戏id */
    private int gameId;
    /** 游戏名称 */
    private String gameName;
    /** 总盈利 */
    private BigDecimal sumWin;
    /** 总下注 */
    private BigDecimal sumBet;
    /** 总赔付 */
    private BigDecimal sumPay;
    /** 游戏次数 */
    private int gameTimes;
    /** 中奖次数 */
    private int winTimes;
    /** 中奖率 */
    private BigDecimal winRatio;
    /** 返奖率 */
    private BigDecimal returnRate;
    /** 服务费 */
    private BigDecimal charge;
    /** 平均下注 */
    private BigDecimal aveBet;
    /** 平均赔付 */
    private BigDecimal avePay;
    /** 最大下注 */
    private BigDecimal maxBet;
    /** 最大赔付 */
    private BigDecimal maxPay;

    /**
     * 设置：总盈利
     *
     * @param sumWin 总盈利
     */
    public void setSumWin(BigDecimal sumWin) {
        this.sumWin = sumWin.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 设置：总下注
     *
     * @param sumBet 总下注
     */
    public void setSumBet(BigDecimal sumBet) {
        this.sumBet = sumBet.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 设置：总赔付
     *
     * @param sumPay 总赔付
     */
    public void setSumPay(BigDecimal sumPay) {
        this.sumPay = sumPay.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 设置：中奖率
     *
     * @param winRatio 中奖率
     */
    public void setWinRatio(BigDecimal winRatio) {
        this.winRatio = winRatio.setScale(4, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 设置：返奖率
     *
     * @param returnRate 返奖率
     */
    public void setReturnRate(BigDecimal returnRate) {
        this.returnRate = returnRate.setScale(4, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 设置：服务费
     *
     * @param charge 服务费
     */
    public void setCharge(BigDecimal charge) {
        this.charge = charge.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 设置：平均下注
     *
     * @param aveBet 平均下注
     */
    public void setAveBet(BigDecimal aveBet) {
        this.aveBet = aveBet.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 设置：平均赔付
     *
     * @param avePay 平均赔付
     */
    public void setAvePay(BigDecimal avePay) {
        this.avePay = avePay.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 设置：最大下注
     *
     * @param maxBet 最大下注
     */
    public void setMaxBet(BigDecimal maxBet) {
        this.maxBet = maxBet.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 设置：最大赔付
     *
     * @param maxPay 最大赔付
     */
    public void setMaxPay(BigDecimal maxPay) {
        this.maxPay = maxPay.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

}