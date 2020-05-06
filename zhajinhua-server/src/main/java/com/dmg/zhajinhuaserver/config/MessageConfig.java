package com.dmg.zhajinhuaserver.config;

/**
 * @author jock
 * @description: 消息号配置
 * @return
 * @date 2019/7/1
 */
public class MessageConfig {

    // 停服通知
    public static final String SHUTDOWN_SERVER_NTC = "6666";
    // 连接成功
    public static final String CONNECT_SUCCESS = "8888";

    public static final String HEART_BIT = "9999";

    public static final String LOGIN = "1000";


    public static final String CHOOSE_ROOM = "2000";
    /**
     * 加入房间
     */
    public static final String JION_ROOM = "2001";

    /**
     * 坐下
     */
    public static final String SITDOWN = "2002";

    /**
     * 玩家准备
     */
    public static final String PLAYERREADY = "2003";

    /**
     * 房主主动开始游戏
     */
    public static final String REQUEST_START_GAME = "2004";
    /**
     * 玩家操作
     */
    public static final String PLAYER_PLAY = "2005";
    /**
     * 退出房间
     */
    public static final String PLAYER_LEAVEROOM = "2006";
    /**
     * 玩家互动
     */
    public static final String GAME_CHAT_MSG = "2007";
    /**
     * 玩家互动通知
     */
    public static final String GAME_CHAT_MSGNTC = "2008";

    public static final String DISOLUT_ROOM = "2009";
    /**
     * 成功解散房间
     */
    public static final String SUCCESS_DISOLUT_ROOM_NTC = "2010";
    /**
     * 玩家请求创建房间
     */
    public static final String CREATE_PRIVATE_ROOM = "2011";

    /**
     * 强制离开
     */
    public static final String PLAYER_FORCE_LEAVEROOM = "2012";

    /**
     *
     */
    public static final String QUERY_ROUND = "2013";
    /**
     * 快速换房
     */
    public static final String QUICK_CHANGE_ROOM = "2014";
    /**
     * 回答解散房间
     */
    public static final String ANSWER_DISOLUT_ROOM = "";
    /**
     * 回答解散房间通知
     */
    public static final String ANSWER_DISOLUT_ROOM_NTC = "";

    /**
     * 解散房间
     */
    public static final String DISBAND_ROOM = "";
    /**
     * 围观列表
     */
    public static final String QUERY_WATCHLIST = "";

    /**
     * 玩家在线
     */
    public static final String PLAYER_ISONLINE = "3000";

    /**
     * 坐下通知
     */
    public static final String SITDOWNNTC = "3001";
    /**
     * 有玩家准备通知
     */
    public static final String SEND_READY_NTC = "3002";
    /**
     *
     * 通知准备通知
     */
    public static final String PLAYER_READY_NTC = "3003";
    /**
     * 动作通知
     */
    public static final String TURN_ACTION = "3004";
    /**
     * 开始游戏通知
     */
    public static final String GAME_START_NTC = "3005";
    /**
     * 发牌通知
     */
    public static final String GAME_SEND_CARDS = "3006";
    /**
     * 玩家操作结果
     */
    public static final String PLAYER_PLAY_RESULT = "3007";
    /**
     * 退出房间通知
     */
    public static final String PLAYER_LEAVEROOMNTC = "3008";
    /**
     * 战绩通知
     */
    public static final String GAME_RESULT_MSG = "3009";

    /**
     * 总结算通知
     */
    public static final String RESULT_ALL_INFO = "3010";
    /**
     * 看牌通知
     */
    public static final String SEE_CARD_ACTION = "3011";
    /**
     * 添加删除机器人
     */
    public static final String ROOM_ADD_REMOVE_ROBOT = "";

    //用户游戏战绩
    public static final String USER_RECORD = "5086";

    //檢查指定用戶 是否 可以登入游戏
    public static final String CHECK_SLOT_LOGIN = "5087";


}