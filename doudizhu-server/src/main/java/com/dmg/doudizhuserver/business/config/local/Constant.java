package com.dmg.doudizhuserver.business.config.local;

/** 常量 */
public interface Constant {
    /** 玩家动作动作延时(秒,有网络延时等原因，要比客户端的定时长一点) */
    int PLAYER_ACTION_DELAY = 1;
    /** 发牌动画(秒) */
    int DEAL_CARDS_TIME = 5;
    /** 机器人进入时间(秒) */
    int ROBOT_ENTER_TIME = 5;
    /** 叫抢地主时间(秒) */
    int CALL_TIME = 15;
    /** 出牌时间(秒) */
    int PLAY_TIME = 15;
    /** 托管出牌时间(秒) */
    int AUTO_PLAY_TIME = 1;
    /** 农民的牌数量 */
    int FARMER_CARDS = 17;

    /** 座位数量 */
    int SEAT_NUM = 3;
    /** 底牌数量 */
    int HIDDEN_CARDS = 3;
}
