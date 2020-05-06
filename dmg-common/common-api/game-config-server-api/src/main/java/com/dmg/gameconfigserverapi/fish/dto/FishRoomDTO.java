package com.dmg.gameconfigserverapi.fish.dto;

import java.io.Serializable;

/**
 * 捕鱼房间配置
 */
public class FishRoomDTO implements Serializable {
    /** 串行版本标识 */
    private static final long serialVersionUID = 1L;
    /** 房间id */
    private Integer id;
    /** 房间名称 */
    private String name;
    /** 是否开放 */
    private boolean open;
    /** 鱼的场景 */
    private String scences;
    /** 人数上限 */
    private Integer playerUpLimit;
    /** 桌子数 */
    private Integer tableNum;
    /** 每桌椅子数 */
    private Integer chair;
    /** 入场金币下限 */
    private Long goldLimitLower;
    /** 入场金币上限 */
    private Long goldLimitUpper;
    /** 玩家多长时间(秒)不发炮T出房间 */
    private Integer kickTime;
    /** 炮台分数 */
    private String batteryScores;
    /** 大额赔付值 */
    private Long bigPayoutValue;
    /** 大额赔付返奖率 */
    private Long bigPayoutRadio;
    /** 机器人数量 */
    private Integer robotNum;
    /** 机器人数量 */
    private Integer robotTableNum;
    /** 机器人每次驱动数量 */
    private Integer robotScheduleNum;
    /** 机器人驱动频率(多少秒驱动一次) */
    private Integer robotScheduleRate;
    /** 机器人游戏时间下限(秒) */
    private Integer robotTimeLower;
    /** 机器人游戏时间上限(秒) */
    private Integer robotTimeUpper;
    /** 机器人进入游戏分数下限 */
    private Long robotEnterPointLower;
    /** 机器人进入游戏分数上限 */
    private Long robotEnterPointUpper;
    /** 机器人多少毫秒射击一次 */
    private Integer robotFireRate;
    /** 每次发炮时换炮概率(万分比) */
    private Integer robotChangeBatteryRate;
    /** 每次发炮时换角度概率(万分比) */
    private Integer robotChangeAngleRate;
    /** 子弹最大发射 */
    private Integer bulletsNumber;

    /**
     * 获取：房间id
     *
     * @return 房间id
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * 设置：房间id
     *
     * @param id 房间id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取：房间名称
     *
     * @return 房间名称
     */
    public String getName() {
        return this.name;
    }

    /**
     * 设置：房间名称
     *
     * @param name 房间名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取：是否开放
     *
     * @return 是否开放
     */
    public boolean isOpen() {
        return this.open;
    }

    /**
     * 设置：是否开放
     *
     * @param open 是否开放
     */
    public void setOpen(boolean open) {
        this.open = open;
    }

    /**
     * 获取：鱼的场景
     *
     * @return 鱼的场景
     */
    public String getScences() {
        return this.scences;
    }

    /**
     * 设置：鱼的场景
     *
     * @param scences 鱼的场景
     */
    public void setScences(String scences) {
        this.scences = scences;
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
     * 获取：每桌椅子数
     *
     * @return 每桌椅子数
     */
    public Integer getChair() {
        return this.chair;
    }

    /**
     * 设置：每桌椅子数
     *
     * @param chair 每桌椅子数
     */
    public void setChair(Integer chair) {
        this.chair = chair;
    }

    /**
     * 获取：入场金币下限
     *
     * @return 入场金币下限
     */
    public Long getGoldLimitLower() {
        return this.goldLimitLower;
    }

    /**
     * 设置：入场金币下限
     *
     * @param goldLimitLower 入场金币下限
     */
    public void setGoldLimitLower(Long goldLimitLower) {
        this.goldLimitLower = goldLimitLower;
    }

    /**
     * 获取：入场金币上限
     *
     * @return 入场金币上限
     */
    public Long getGoldLimitUpper() {
        return this.goldLimitUpper;
    }

    /**
     * 设置：入场金币上限
     *
     * @param goldLimitUpper 入场金币上限
     */
    public void setGoldLimitUpper(Long goldLimitUpper) {
        this.goldLimitUpper = goldLimitUpper;
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
     * 获取：炮台分数
     *
     * @return 炮台分数
     */
    public String getBatteryScores() {
        return this.batteryScores;
    }

    /**
     * 设置：炮台分数
     *
     * @param batteryScores 炮台分数
     */
    public void setBatteryScores(String batteryScores) {
        this.batteryScores = batteryScores;
    }

    /**
     * 获取：大额赔付值
     *
     * @return 大额赔付值
     */
    public Long getBigPayoutValue() {
        return this.bigPayoutValue;
    }

    /**
     * 设置：大额赔付值
     *
     * @param bigPayoutValue 大额赔付值
     */
    public void setBigPayoutValue(Long bigPayoutValue) {
        this.bigPayoutValue = bigPayoutValue;
    }

    /**
     * 获取：大额赔付返奖率
     *
     * @return 大额赔付返奖率
     */
    public Long getBigPayoutRadio() {
        return this.bigPayoutRadio;
    }

    /**
     * 设置：大额赔付返奖率
     *
     * @param bigPayoutRadio 大额赔付返奖率
     */
    public void setBigPayoutRadio(Long bigPayoutRadio) {
        this.bigPayoutRadio = bigPayoutRadio;
    }

    /**
     * 获取：机器人数量
     *
     * @return 机器人数量
     */
    public Integer getRobotNum() {
        return this.robotNum;
    }

    /**
     * 设置：机器人数量
     *
     * @param robotNum 机器人数量
     */
    public void setRobotNum(Integer robotNum) {
        this.robotNum = robotNum;
    }

    /**
     * 获取：机器人数量
     *
     * @return 机器人数量
     */
    public Integer getRobotTableNum() {
        return this.robotTableNum;
    }

    /**
     * 设置：机器人数量
     *
     * @param robotTableNum 机器人数量
     */
    public void setRobotTableNum(Integer robotTableNum) {
        this.robotTableNum = robotTableNum;
    }

    /**
     * 获取：机器人每次驱动数量
     *
     * @return 机器人每次驱动数量
     */
    public Integer getRobotScheduleNum() {
        return this.robotScheduleNum;
    }

    /**
     * 设置：机器人每次驱动数量
     *
     * @param robotScheduleNum 机器人每次驱动数量
     */
    public void setRobotScheduleNum(Integer robotScheduleNum) {
        this.robotScheduleNum = robotScheduleNum;
    }

    /**
     * 获取：机器人驱动频率(多少秒驱动一次)
     *
     * @return 机器人驱动频率(多少秒驱动一次)
     */
    public Integer getRobotScheduleRate() {
        return this.robotScheduleRate;
    }

    /**
     * 设置：机器人驱动频率(多少秒驱动一次)
     *
     * @param robotScheduleRate 机器人驱动频率(多少秒驱动一次)
     */
    public void setRobotScheduleRate(Integer robotScheduleRate) {
        this.robotScheduleRate = robotScheduleRate;
    }

    /**
     * 获取：机器人游戏时间下限(秒)
     *
     * @return 机器人游戏时间下限(秒)
     */
    public Integer getRobotTimeLower() {
        return this.robotTimeLower;
    }

    /**
     * 设置：机器人游戏时间下限(秒)
     *
     * @param robotTimeLower 机器人游戏时间下限(秒)
     */
    public void setRobotTimeLower(Integer robotTimeLower) {
        this.robotTimeLower = robotTimeLower;
    }

    /**
     * 获取：机器人游戏时间上限(秒)
     *
     * @return 机器人游戏时间上限(秒)
     */
    public Integer getRobotTimeUpper() {
        return this.robotTimeUpper;
    }

    /**
     * 设置：机器人游戏时间上限(秒)
     *
     * @param robotTimeUpper 机器人游戏时间上限(秒)
     */
    public void setRobotTimeUpper(Integer robotTimeUpper) {
        this.robotTimeUpper = robotTimeUpper;
    }

    /**
     * 获取：机器人进入游戏分数下限
     *
     * @return 机器人进入游戏分数下限
     */
    public Long getRobotEnterPointLower() {
        return this.robotEnterPointLower;
    }

    /**
     * 设置：机器人进入游戏分数下限
     *
     * @param robotEnterPointLower 机器人进入游戏分数下限
     */
    public void setRobotEnterPointLower(Long robotEnterPointLower) {
        this.robotEnterPointLower = robotEnterPointLower;
    }

    /**
     * 获取：机器人进入游戏分数上限
     *
     * @return 机器人进入游戏分数上限
     */
    public Long getRobotEnterPointUpper() {
        return this.robotEnterPointUpper;
    }

    /**
     * 设置：机器人进入游戏分数上限
     *
     * @param robotEnterPointUpper 机器人进入游戏分数上限
     */
    public void setRobotEnterPointUpper(Long robotEnterPointUpper) {
        this.robotEnterPointUpper = robotEnterPointUpper;
    }

    /**
     * 获取：机器人多少毫秒射击一次
     *
     * @return 机器人多少毫秒射击一次
     */
    public Integer getRobotFireRate() {
        return this.robotFireRate;
    }

    /**
     * 设置：机器人多少毫秒射击一次
     *
     * @param robotFireRate 机器人多少毫秒射击一次
     */
    public void setRobotFireRate(Integer robotFireRate) {
        this.robotFireRate = robotFireRate;
    }

    /**
     * 获取：每次发炮时换炮概率(万分比)
     *
     * @return 每次发炮时换炮概率(万分比)
     */
    public Integer getRobotChangeBatteryRate() {
        return this.robotChangeBatteryRate;
    }

    /**
     * 设置：每次发炮时换炮概率(万分比)
     *
     * @param robotChangeBatteryRate 每次发炮时换炮概率(万分比)
     */
    public void setRobotChangeBatteryRate(Integer robotChangeBatteryRate) {
        this.robotChangeBatteryRate = robotChangeBatteryRate;
    }

    /**
     * 获取：每次发炮时换角度概率(万分比)
     *
     * @return 每次发炮时换角度概率(万分比)
     */
    public Integer getRobotChangeAngleRate() {
        return this.robotChangeAngleRate;
    }

    /**
     * 设置：每次发炮时换角度概率(万分比)
     *
     * @param robotChangeAngleRate 每次发炮时换角度概率(万分比)
     */
    public void setRobotChangeAngleRate(Integer robotChangeAngleRate) {
        this.robotChangeAngleRate = robotChangeAngleRate;
    }

    /**
     * 获取：子弹最大发射
     *
     * @return 子弹最大发射
     */
    public Integer getBulletsNumber() {
        return this.bulletsNumber;
    }

    /**
     * 设置：子弹最大发射
     *
     * @param bulletsNumber 子弹最大发射
     */
    public void setBulletsNumber(Integer bulletsNumber) {
        this.bulletsNumber = bulletsNumber;
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
