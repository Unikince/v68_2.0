package com.dmg.bjlserver.business.model;

/**
 * 常量
 */
public interface Constant {
    /** 一局游戏时间(秒) */
    int GAME_TIME = 20;
    /** 下注时间(秒) */
    int BET_TIME = 12;
    /** 休息时间(秒) */
    int REST_TIME = 2;
    /** 洗牌时间(秒) */
    int SHUFFLE_TIME = 5;

    /** 玩家排名人数 */
    int PLAYER_RANK_NUM = 6;
    /** 无座列表头像显示数量 */
    int OTHER_SEATS_SHOW_LIMIT = 500;

    /** 输赢历史记录大小 */
    int HISTORY_SIZE = 500;
    /** 发了多少张就洗牌 */
    int SHUFFLE_LIMIT = 80;
    /** 发几套 */
    int PACK_SIZE = 8;
    /** 座位数量 */
    int RANK_SEAT_COUNT = 6;
}
