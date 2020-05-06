package com.dmg.gameconfigserver.model.vo.fish;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.dmg.gameconfigserver.model.vo.group.SaveValid;
import com.dmg.gameconfigserver.model.vo.group.UpdateValid;

/**
 * 修改捕鱼房间配置
 */
public class FishRoomConfigUpdateReq implements Serializable {
    /** 串行版本标识 */
    private static final long serialVersionUID = 1L;
    /** 场次ID */
    @NotNull(message = "场次ID不能为空", groups = UpdateValid.class)
    private Integer id;

    /** 场次名称 */
    @NotNull(message = "场次名称不能为空", groups = SaveValid.class)
    private String name;

    /** 场次状态 */
    @NotNull(message = "场次状态不能为空", groups = SaveValid.class)
    private boolean open;

    /** 人数上限 */
    @NotNull(message = "人数上限不能为空", groups = SaveValid.class)
    @Min(value = 500, message = "人数上限不能小于500", groups = SaveValid.class)
    private Integer playerUpLimit;

    /** 桌子数 */
    @NotNull(message = "桌子数不能为空", groups = SaveValid.class)
    @Min(value = 100, message = "桌子数不能小于100", groups = SaveValid.class)
    private Integer tableNum;

    /** 准入 */
    @NotNull(message = "准入不能为空", groups = SaveValid.class)
    private Long goldLimitLower;

    /** 大额赔付金额 */
    @NotNull(message = "大额赔付金额不能为空", groups = SaveValid.class)
    private Long bigPayoutValue;

    /** 大额赔付赔率 */
    @NotNull(message = "大额赔付赔率不能为空", groups = SaveValid.class)
    private Long bigPayoutRadio;

    /** 炮台分数 */
    @NotNull(message = "炮台分数不能为空", groups = SaveValid.class)
    private List<Integer> batteryScores;

    /** 玩家多长时间(秒)不发炮T出房间 */
    @NotNull(message = "玩家多长时间(秒)不发炮T出房间不能为空", groups = SaveValid.class)
    @Min(value = 60, message = "玩家多长时间(秒)不发炮T出房间不能小于100", groups = SaveValid.class)
    private Integer kickTime;

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
     * 获取：场次状态
     *
     * @return 场次状态
     */
    public boolean isOpen() {
        return this.open;
    }

    /**
     * 设置：场次状态
     *
     * @param open 场次状态
     */
    public void setOpen(boolean open) {
        this.open = open;
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
     * 获取：桌子数
     *
     * @return 桌子数
     */
    public Integer getTableNum() {
        return this.tableNum;
    }

    /**
     * 设置：桌子数
     *
     * @param tableNum 桌子数
     */
    public void setTableNum(Integer tableNum) {
        this.tableNum = tableNum;
    }

    /**
     * 获取：准入
     *
     * @return 准入
     */
    public Long getGoldLimitLower() {
        return this.goldLimitLower;
    }

    /**
     * 设置：准入
     *
     * @param goldLimitLower 准入
     */
    public void setGoldLimitLower(Long goldLimitLower) {
        this.goldLimitLower = goldLimitLower;
    }

    /**
     * 获取：大额赔付金额
     *
     * @return 大额赔付金额
     */
    public Long getBigPayoutValue() {
        return this.bigPayoutValue;
    }

    /**
     * 设置：大额赔付金额
     *
     * @param bigPayoutValue 大额赔付金额
     */
    public void setBigPayoutValue(Long bigPayoutValue) {
        this.bigPayoutValue = bigPayoutValue;
    }

    /**
     * 获取：大额赔付赔率
     *
     * @return 大额赔付赔率
     */
    public Long getBigPayoutRadio() {
        return this.bigPayoutRadio;
    }

    /**
     * 设置：大额赔付赔率
     *
     * @param bigPayoutRadio 大额赔付赔率
     */
    public void setBigPayoutRadio(Long bigPayoutRadio) {
        this.bigPayoutRadio = bigPayoutRadio;
    }

    /**
     * 获取：炮台分数
     *
     * @return 炮台分数
     */
    public List<Integer> getBatteryScores() {
        return this.batteryScores;
    }

    /**
     * 设置：炮台分数
     *
     * @param batteryScores 炮台分数
     */
    public void setBatteryScores(List<Integer> batteryScores) {
        this.batteryScores = batteryScores;
    }

    /**
     * 获取：玩家多长时间(秒)不发炮T出房间
     *
     * @return 玩家多长时间(秒)不发炮T出房间
     */
    public Integer getKickTime() {
        return this.kickTime;
    }

    /**
     * 设置：玩家多长时间(秒)不发炮T出房间
     *
     * @param kickTime 玩家多长时间(秒)不发炮T出房间
     */
    public void setKickTime(Integer kickTime) {
        this.kickTime = kickTime;
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
