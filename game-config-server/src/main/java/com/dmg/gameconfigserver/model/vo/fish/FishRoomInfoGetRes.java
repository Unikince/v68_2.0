package com.dmg.gameconfigserver.model.vo.fish;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 获取捕鱼房间信息
 */
public class FishRoomInfoGetRes implements Serializable {
    /** 串行版本标识 */
    private static final long serialVersionUID = 1L;
    /** 场次ID */
    private Integer id;
    /** 场次名称 */
    private String name;
    /** 准入 */
    private BigDecimal goldLimitLower;
    /** 当前状态 */
    private boolean open;
    /** 当前玩家数量 */
    private Integer curPersionNum;
    /** 当前机器人数量 */
    private Integer curRobotNum;
    /** 总下注 */
    private Long sumBet;
    /** 总赔付 */
    private Long sumPay;
    /** 当前赔率 */
    private BigDecimal returnRate;
    /** 当前库存 */
    private Long sumWin;
    /** 控制库存当前值 */
    private Long ctrlStockNum;
    /** 控制库存最大值 */
    private Long ctrlStockMax;
    /** 人数上限 */
    private Integer playerUpLimit;

    /**
     * 获取：场次ID
     *
     * @return 场次ID
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * 设置：场次ID
     *
     * @param id 场次ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取：场次名称
     *
     * @return 场次名称
     */
    public String getName() {
        return this.name;
    }

    /**
     * 设置：场次名称
     *
     * @param name 场次名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取：准入
     *
     * @return 准入
     */
    public BigDecimal getGoldLimitLower() {
        return this.goldLimitLower;
    }

    /**
     * 设置：准入
     *
     * @param goldLimitLower 准入
     */
    public void setGoldLimitLower(BigDecimal goldLimitLower) {
        this.goldLimitLower = goldLimitLower;
    }

    /**
     * 获取：当前状态
     *
     * @return 当前状态
     */
    public boolean isOpen() {
        return this.open;
    }

    /**
     * 设置：当前状态
     *
     * @param open 当前状态
     */
    public void setOpen(boolean open) {
        this.open = open;
    }

    /**
     * 获取：当前玩家数量
     *
     * @return 当前玩家数量
     */
    public Integer getCurPersionNum() {
        return this.curPersionNum;
    }

    /**
     * 设置：当前玩家数量
     *
     * @param curPersionNum 当前玩家数量
     */
    public void setCurPersionNum(Integer curPersionNum) {
        this.curPersionNum = curPersionNum;
    }

    /**
     * 获取：当前机器人数量
     *
     * @return 当前机器人数量
     */
    public Integer getCurRobotNum() {
        return this.curRobotNum;
    }

    /**
     * 设置：当前机器人数量
     *
     * @param curRobotNum 当前机器人数量
     */
    public void setCurRobotNum(Integer curRobotNum) {
        this.curRobotNum = curRobotNum;
    }

    /**
     * 获取：总下注
     *
     * @return 总下注
     */
    public Long getSumBet() {
        return this.sumBet;
    }

    /**
     * 设置：总下注
     *
     * @param sumBet 总下注
     */
    public void setSumBet(Long sumBet) {
        this.sumBet = sumBet;
    }

    /**
     * 获取：总赔付
     *
     * @return 总赔付
     */
    public Long getSumPay() {
        return this.sumPay;
    }

    /**
     * 设置：总赔付
     *
     * @param sumPay 总赔付
     */
    public void setSumPay(Long sumPay) {
        this.sumPay = sumPay;
    }

    /**
     * 获取：当前赔率
     *
     * @return 当前赔率
     */
    public BigDecimal getReturnRate() {
        return this.returnRate;
    }

    /**
     * 设置：当前赔率
     *
     * @param returnRate 当前赔率
     */
    public void setReturnRate(BigDecimal returnRate) {
        this.returnRate = returnRate;
    }

    /**
     * 获取：当前库存
     *
     * @return 当前库存
     */
    public Long getSumWin() {
        return this.sumWin;
    }

    /**
     * 设置：当前库存
     *
     * @param sumWin 当前库存
     */
    public void setSumWin(Long sumWin) {
        this.sumWin = sumWin;
    }

    /**
     * 获取：控制库存当前值
     *
     * @return 控制库存当前值
     */
    public Long getCtrlStockNum() {
        return this.ctrlStockNum;
    }

    /**
     * 设置：控制库存当前值
     *
     * @param ctrlStockNum 控制库存当前值
     */
    public void setCtrlStockNum(Long ctrlStockNum) {
        this.ctrlStockNum = ctrlStockNum;
    }

    /**
     * 获取：控制库存最大值
     *
     * @return 控制库存最大值
     */
    public Long getCtrlStockMax() {
        return this.ctrlStockMax;
    }

    /**
     * 设置：控制库存最大值
     *
     * @param ctrlStockMax 控制库存最大值
     */
    public void setCtrlStockMax(Long ctrlStockMax) {
        this.ctrlStockMax = ctrlStockMax;
    }

    /**
     * 获取：人数上限
     *
     * @return 人数上限
     */
    public Integer getPlayerUpLimit() {
        return this.playerUpLimit;
    }

    /**
     * 设置：人数上限
     *
     * @param playerUpLimit 人数上限
     */
    public void setPlayerUpLimit(Integer playerUpLimit) {
        this.playerUpLimit = playerUpLimit;
    }

    /**
     * 获取：串行版本标识
     *
     * @return 串行版本标识
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
