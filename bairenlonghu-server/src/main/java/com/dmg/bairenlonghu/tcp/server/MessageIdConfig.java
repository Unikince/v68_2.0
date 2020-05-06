package com.dmg.bairenlonghu.tcp.server;

/**
 * @Description 消息号配置
 * @Author mice
 * @Date 2019/7/29 13:59
 * @Version V1.0
 **/
public class MessageIdConfig {

    // 连接成功
    public static final String CONNECT_SUCCESS = "8888";
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
    //续压结果通知
    public static final String COPY_BET_RESULT_NTC = "2010";
    // 停服通知
    public static final String SHUTDOWN_SERVER_NTC = "2011";
}