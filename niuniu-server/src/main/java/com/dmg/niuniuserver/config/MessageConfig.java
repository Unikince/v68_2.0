package com.dmg.niuniuserver.config;

/**
 * @author mice
 * @description: 消息号配置
 * @return
 * @date 2019/7/1
 */
public class MessageConfig {

    // 连接成功
    public static final String CONNECT_SUCCESS = "8888";

    public static final String HEART_BIT = "9999";
    // 停服通知
    public static final String SHUTDOWN_SERVER_NTC = "6666";
    // 同步玩家金币
    public static final String SYNC_GOLD = "7777";
    // 登录
    public static final String LOGIN = "1000";
    // 同步玩家金币
    public static final String SYNC_PLAYER_GOLD = "1001";

    public static final String CHOOSE_ROOM = "2000";
    // 加入房间
    public static final String JOIN_ROOM = "2001";
    // 坐下
    public static final String SITDOWN = "2002";
    // 准备
    public static final String READY = "2003";
    // 玩家下注,抢庄等通用操作
    public static final String DO_ACTION = "2004";
    // 强制离开房间
    public static final String FORCE_LEAVE_ROOM = "2005";
    // 快速换房
    public static final String QUICK_CHANGE_ROOM = "2006";
    // 请求离开
    public static final String PLAYER_LEAVE_ROOM = "2007";
    // 解散房间
    public static final String DISSOLVE_ROOM = "2008";
    // 玩家互动
    public static final String CHAT = "2009";

    // ==========================NTC=================

    // 玩家是否在线
    public static final String PLAYER_IS_ONLINE = "3001";
    /**
     * 椅子消息
     */
    public static final String SEAT_MESSAGE = "3002";
    /**
     * 请求坐下主动推送给其他玩家
     */
    public static final String SITDOWNNTC = "3003";
    /**
     * 通知客户端可以准备了
     */
    public static final String NTC_SEND_READ_INFO = "3004";
    /**
     * 主动推送准备消息
     */
    public static final String PLAYERREADYNTC = "3005";
    /**
     * 主动推送游戏开始消息
     */
    public static final String GAME_START = "3006";
    /**
     * 主动推送发牌消息
     */
    public static final String GAME_SEND_CARDS = "3007";
    /**
     * 主动轮次行动消息
     */
    public static final String TURN_ACTION = "3008";
    /**
     * 玩家操作信息结果数据通知
     */
    public static final String DO_ACTION_RESULT_NTC = "3009";
    /**
     * 主动推送离开消息
     */
    public static final String PLAYER_LEAVE_ROOM_NTC = "3010";
    /**
     * 推送房间解散成功失败
     */
    public static final String SUCCESS_DISOLUT_ROOM_NTC = "3011";
    /**
     * 总结算数据
     */
    public static final String RESULT_ALL_INFO = "3012";
    /**
     * 主动推送当局结算
     */
    public static final String GAME_RESULT_MSG = "3013";
    // 132014 战绩信息
    public static final String GAME_RECORD_MSG = "3014";
    /**
     * 解散房间成功
     */
    public static final String SUCCESS_DISBAND_ROOM_NTC = "3015";
    /**
     * 解散房间消息应答回复
     */
    public static final String ANSWER_DISBAND_ROOM = "3016";
    /**
     * 解散房间消息应答回复
     */
    public static final String ANSWER_DISBAND_ROOM_NTC = "3017";
    /**
     * 有玩家发起解散消息
     */
    public static final String DISBAND_ROOM_NTC = "3018";
    /**
     * 主动广播玩家互动
     */
    public static final String GAME_CHAT_MSGNTC = "3019";
    /**
     * 抢庄信息
     */
    public static final String QIANGZHUANG_INFO = "3020";

    // 用户游戏战绩
    public static final String USER_RECORD = "5086";

}