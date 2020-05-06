package com.dmg.niuniuserver.model.constants;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Date 2018年3月13日
 * @Desc 定义
 */
public class GameConfig {

    /**
     * 游戏类型
     */
    public static final int NIUNIU = 1;

    public static double RUN_AWAY_BET = 10.0D;
    /**
     * 大王牌值
     */
    public static final int KING = 56;
    /**
     * 小王牌值
     */
    public static final int WANG = 55;

    /**
     * 准备倒计时长
     */
    public static final int READY_LENGTH_TIME = 7000;

    /**
     * 抢庄倒计时长
     */
    public static final int QIANG_ZHUANG_LENGTH_TIME = 5000;

    /**
     * 定庄倒计时长
     */
    public static final int DING_ZHUANG_LENGTH_TIME = 4000;

    /**
     * 下注倒计时长
     */
    public static final int XIA_ZHU_LENGTH_TIME = 5000;

    /**
     * 开牌任务倒计时长
     */
    public static final int KAI_PAI_LENGTH_TIME = 3000;
    /**
     * 下局开始时长
     */
    public static final int XIA_JU_LENGTH_TIME = 3000;

    /**
     * 解散房间时长
     */
    public static final int DISBAND_ROOM_TIME = 30000;

    /**
     * 游戏房间类型,自由场
     */
    public static final int FREE_FIELD = 1;
    /**
     * 房间最大轮数
     */
    public static final int ROOM_MAX_TURNS = 15;

    /**
     * 默认byte最大值
     */
    public static final int DEFINENUM = 255;
}
