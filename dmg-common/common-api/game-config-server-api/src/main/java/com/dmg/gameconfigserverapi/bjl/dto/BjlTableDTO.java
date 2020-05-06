package com.dmg.gameconfigserverapi.bjl.dto;

/**
 * 百家乐场次配置
 */
public class BjlTableDTO {
    /** 场次ID */
    private int id;
    /** 场次名称 */
    private String name;
    /** 是否开启 */
    private boolean open;
    /** 人数上限 */
    private int playerNum;
    /** 观战踢出局数 */
    private int watchKickRound;
    /** 筹码(json) */
    private String chipJson;
    /** 玩家最大下注 */
    private long playerMaxBet;
    /** 桌子最大下注 */
    private long tableMaxBet;
    /** 服务费 */
    private int pumpRate;
    /** 机器人基础数量 */
    private int robotBaseCount;
    /** 机器人最大数量 */
    private int robotMaxCount;
    /** 机器人调度增加数量 */
    private int robotScheduleNum;
    /** 机器人调度时间(秒) */
    private int robotScheduleTime;
    /** 机器人下注筹码权重(josn) */
    private String robotBetChipWeightJson;
    /** 机器人单局下注次数(json) */
    private String robotBetCountJson;
    /** 机器人下注区域(json) */
    private String robotBetAreaWeightJson;
    /** 机器人进场金币下限 */
    private long robotEnterGoldLower;
    /** 机器人进场金币上限 */
    private long robotEnterGoldUpper;
    /** 机器人退出局数 */
    private int robotOutRound;

    /**
     * 获取：场次ID
     *
     * @return 场次ID
     */
    public int getId() {
        return this.id;
    }

    /**
     * 设置：场次ID
     *
     * @param id 场次ID
     */
    public void setId(int id) {
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
     * 获取：是否开启
     *
     * @return 是否开启
     */
    public boolean isOpen() {
        return this.open;
    }

    /**
     * 设置：是否开启
     *
     * @param open 是否开启
     */
    public void setOpen(boolean open) {
        this.open = open;
    }

    /**
     * 获取：人数上限
     *
     * @return 人数上限
     */
    public int getPlayerNum() {
        return this.playerNum;
    }

    /**
     * 设置：人数上限
     *
     * @param playerNum 人数上限
     */
    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    /**
     * 获取：观战踢出局数
     *
     * @return 观战踢出局数
     */
    public int getWatchKickRound() {
        return this.watchKickRound;
    }

    /**
     * 设置：观战踢出局数
     *
     * @param watchKickRound 观战踢出局数
     */
    public void setWatchKickRound(int watchKickRound) {
        this.watchKickRound = watchKickRound;
    }

    /**
     * 获取：筹码(json)
     *
     * @return 筹码(json)
     */
    public String getChipJson() {
        return this.chipJson;
    }

    /**
     * 设置：筹码(json)
     *
     * @param chipJson 筹码(json)
     */
    public void setChipJson(String chipJson) {
        this.chipJson = chipJson;
    }

    /**
     * 获取：玩家最大下注
     *
     * @return 玩家最大下注
     */
    public long getPlayerMaxBet() {
        return this.playerMaxBet;
    }

    /**
     * 设置：玩家最大下注
     *
     * @param playerMaxBet 玩家最大下注
     */
    public void setPlayerMaxBet(long playerMaxBet) {
        this.playerMaxBet = playerMaxBet;
    }

    /**
     * 获取：桌子最大下注
     *
     * @return 桌子最大下注
     */
    public long getTableMaxBet() {
        return this.tableMaxBet;
    }

    /**
     * 设置：桌子最大下注
     *
     * @param tableMaxBet 桌子最大下注
     */
    public void setTableMaxBet(long tableMaxBet) {
        this.tableMaxBet = tableMaxBet;
    }

    /**
     * 获取：服务费
     *
     * @return 服务费
     */
    public int getPumpRate() {
        return this.pumpRate;
    }

    /**
     * 设置：服务费
     *
     * @param pumpRate 服务费
     */
    public void setPumpRate(int pumpRate) {
        this.pumpRate = pumpRate;
    }

    /**
     * 获取：机器人基础数量
     *
     * @return 机器人基础数量
     */
    public int getRobotBaseCount() {
        return this.robotBaseCount;
    }

    /**
     * 设置：机器人基础数量
     *
     * @param robotBaseCount 机器人基础数量
     */
    public void setRobotBaseCount(int robotBaseCount) {
        this.robotBaseCount = robotBaseCount;
    }

    /**
     * 获取：机器人最大数量
     *
     * @return 机器人最大数量
     */
    public int getRobotMaxCount() {
        return this.robotMaxCount;
    }

    /**
     * 设置：机器人最大数量
     *
     * @param robotMaxCount 机器人最大数量
     */
    public void setRobotMaxCount(int robotMaxCount) {
        this.robotMaxCount = robotMaxCount;
    }

    /**
     * 获取：机器人调度增加数量
     *
     * @return 机器人调度增加数量
     */
    public int getRobotScheduleNum() {
        return this.robotScheduleNum;
    }

    /**
     * 设置：机器人调度增加数量
     *
     * @param robotScheduleNum 机器人调度增加数量
     */
    public void setRobotScheduleNum(int robotScheduleNum) {
        this.robotScheduleNum = robotScheduleNum;
    }

    /**
     * 获取：机器人调度时间(秒)
     *
     * @return 机器人调度时间(秒)
     */
    public int getRobotScheduleTime() {
        return this.robotScheduleTime;
    }

    /**
     * 设置：机器人调度时间(秒)
     *
     * @param robotScheduleTime 机器人调度时间(秒)
     */
    public void setRobotScheduleTime(int robotScheduleTime) {
        this.robotScheduleTime = robotScheduleTime;
    }

    /**
     * 获取：机器人下注筹码权重(josn)
     *
     * @return 机器人下注筹码权重(josn)
     */
    public String getRobotBetChipWeightJson() {
        return this.robotBetChipWeightJson;
    }

    /**
     * 设置：机器人下注筹码权重(josn)
     *
     * @param robotBetChipWeightJson 机器人下注筹码权重(josn)
     */
    public void setRobotBetChipWeightJson(String robotBetChipWeightJson) {
        this.robotBetChipWeightJson = robotBetChipWeightJson;
    }

    /**
     * 获取：机器人单局下注次数(json)
     *
     * @return 机器人单局下注次数(json)
     */
    public String getRobotBetCountJson() {
        return this.robotBetCountJson;
    }

    /**
     * 设置：机器人单局下注次数(json)
     *
     * @param robotBetCountJson 机器人单局下注次数(json)
     */
    public void setRobotBetCountJson(String robotBetCountJson) {
        this.robotBetCountJson = robotBetCountJson;
    }

    /**
     * 获取：机器人下注区域(json)
     * 
     * @return 机器人下注区域(json)
     */
    public String getRobotBetAreaWeightJson() {
        return this.robotBetAreaWeightJson;
    }

    /**
     * 设置：机器人下注区域(json)
     * 
     * @param robotBetAreaWeightJson 机器人下注区域(json)
     */
    public void setRobotBetAreaWeightJson(String robotBetAreaWeightJson) {
        this.robotBetAreaWeightJson = robotBetAreaWeightJson;
    }

    /**
     * 获取：机器人进场金币下限
     *
     * @return 机器人进场金币下限
     */
    public long getRobotEnterGoldLower() {
        return this.robotEnterGoldLower;
    }

    /**
     * 设置：机器人进场金币下限
     *
     * @param robotEnterGoldLower 机器人进场金币下限
     */
    public void setRobotEnterGoldLower(long robotEnterGoldLower) {
        this.robotEnterGoldLower = robotEnterGoldLower;
    }

    /**
     * 获取：机器人进场金币上限
     *
     * @return 机器人进场金币上限
     */
    public long getRobotEnterGoldUpper() {
        return this.robotEnterGoldUpper;
    }

    /**
     * 设置：机器人进场金币上限
     *
     * @param robotEnterGoldUpper 机器人进场金币上限
     */
    public void setRobotEnterGoldUpper(long robotEnterGoldUpper) {
        this.robotEnterGoldUpper = robotEnterGoldUpper;
    }

    /**
     * 获取：机器人退出局数
     *
     * @return 机器人退出局数
     */
    public int getRobotOutRound() {
        return this.robotOutRound;
    }

    /**
     * 设置：机器人退出局数
     *
     * @param robotOutRound 机器人退出局数
     */
    public void setRobotOutRound(int robotOutRound) {
        this.robotOutRound = robotOutRound;
    }

}
