package com.dmg.bairenzhajinhuaserver.tcp.server;

/**
 * @Description 消息号配置
 * @Author mice
 * @Date 2019/7/29 13:59
 * @Version V1.0
 **/
public class MessageIdConfig {
    // 停服通知
    public static final String SHUTDOWN_SERVER_NTC = "6666";
    // 同步玩家金币
    public static final String SYNC_GOLD = "7777";
    // 连接成功
    public static final String CONNECT_SUCCESS = "8888";
    // 心跳
    public static final String HEART_BIT = "9999";
    // 登录
    public static final String LOGIN = "1000";
    //加入房间
    public static final String JOIN_ROOM = "2000";
    //下注通知
    public static final String BET_NTC = "2001";
    // 更新房间
    public static final String UPDATE_ROOM_NTC = "2002";
    //下注
    public static final String BET = "2003";
    //下注结果通知
    public static final String BET_RESULT_NTC = "2004";
    //结算结果通知
    public static final String SETTLE_RESULT_NTC = "2005";
    //申请上庄
    public static final String APPLY_TO_ZHUANG = "2006";
    // 获取申请上庄人员信息
    public static final String GET_APPLY_TO_ZHUANG_INFO = "2007";
    // 退出房间
    public static final String QUIT_ROOM = "2008";
    // 获取场外玩家列表
    public static final String GET_OUTFILED_PLAYER_LIST = "2009";
    //发牌通知
    public static final String DEAL_POKER_NTC = "2010";
    // 踢出房间
    public static final String KICK_QUIT_ROOM = "2011";
    // 顶号
    public static final String NEW_PLAYER_LOGIN = "2012";
    // 停止下注
    public static final String STOP_BET_NTC = "2013";
    //檢查指定用戶 是否 可以登入游戏
    public static final String CHECK_SLOT_LOGIN = "5087";
}