package com.dmg.bjlserver.business.model.game;

/**
 * 游戏阶段枚举
 */
public enum GameStage {
    /** 下注状态 */
    BET(1, "下注"),
    /** 洗牌状态 */
    SHUFFLE(2, "洗牌"),
    /** 游戏状态 */
    GAMING(3, "游戏"),
    /** 休息状态 */
    REST(5, "休息"),;

    /** 阶段编码 */
    public final int code;
    /** 阶段描述 */
    public final String desc;

    /**
     * 构造方法
     * 
     * @param code 阶段编码
     * @param desc 阶段描述
     */
    private GameStage(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
