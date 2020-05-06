package com.dmg.clubserver.config;

/**
 * 消息号配置
 *
 * @author linanjun
 */
public class MessageConfig {
    // 测试登录
    public static final String USER_APPLY_LOGIN = "1000";
    // 心跳
    public static final String Heart_Beat = "9999";
    // 俱乐部登录
    public static final String CLUB_LOGIN = "1001";
    //创建俱乐部
    public static final String CREATE_CLUB = "1002";
    //更新俱乐部公告俱乐部
    public static final String UPDATE_CLUB_REMARK = "1003";
    //俱乐部成员列表
    public static final String CLUB_PLAYER_LIST = "1004";
    // 推荐俱乐部
    public static final String RECOMMEND_CLUB = "1005";
    //申请加入俱乐部
    public static final String REQUEST_JOIN_CLUB = "1006";
    // 撤回俱乐部申请
    public static final String REVOKE_REQUEST = "1007";
    // 获取俱乐部大厅审核列表
    public static final String CLUB_LOBBY_REVIEW_LIST = "1008";
    // 获取俱乐部内审核列表
    public static final String CLUB_REVIEW_LIST = "1009";
    // 冻结/解冻成员
    public static final String FREEZE_AND_UNFREEZE = "1010";
    // 踢出成员
    public static final String KICK_OUT = "1011";
    // 设置职位
    public static final String SET_POSITION = "1012";
    // 审核俱乐部申请(大厅)
    public static final String REVIEW_CLUB_LOBBY_REQUES = "1013";
    // 审核俱乐部申请
    public static final String REVIEW_CLUB_REQUES = "1014";
    // 俱乐部冻结列表
    public static final String CLUB_FREEZE_LIST = "1015";
    // 俱乐部战绩
    public static final String CLUB_GAME_RECORD = "1016";
    // 俱乐部战绩详情
    public static final String CLUB_GAME_RECORD_DETAIL = "1017";
    // 排行榜
    public static final String  LEADER_BOARD = "1018";
    // 发起俱乐部邀请
    public static final String CLUB_INVITATION = "1019";
    // 俱乐部邀请列表
    public static final String CLUB_INVITATION_LIST = "1020";
    // 审核俱乐部邀请
    public static final String CLUB_REVIEW_INVITATION = "1021";
    // 俱乐部内部登录
    public static final String CLUB_INNER_LOGIN = "1022";
    // 俱乐部记录列表
    public static final String CLUB_LOG_LIST = "1023";
    // 退出俱乐部
    public static final String QUIT_CLUB = "1024";
    // 解散俱乐部
    public static final String DISSOLVE_CLUB = "1025";
    // 俱乐部福利
    public static final String CLUB_WELFARE = "1026";
    // 俱乐部房卡消耗查询
    public static final String CLUB_ROOM_CARD_SEARCH = "1027";
    // 游戏配置
    public static final String GAME_CONFIG = "1028";
    // 举报俱乐部
    public static final String REPORT_CLUB= "1029";

    //================================通知=========================

    // 审核结果通知
    public static final String REVIEW_CLUB_LOBBY_REQUES_NTC = "2001";
    // 被踢出
    public static final String KICK_OUT_NTC = "2002";
    // 解冻 冻结通知
    public static final String FREEZE_AND_UNFREEZE_NTC = "2003";
    // 职位变化通知
    public static final String SET_POSITION_NTC = "2004";
    // 退出牌桌通知
    public static final String QUIT_TABLE_NTC = "2005";
    // 玩家准备通知
    public static final String PLAYER_READY_NTC = "2006";
    // 游戏开始通知
    public static final String GAME_START_NTC = "2007";
    // 更新牌桌信息
    public static final String UPDATE_TABLE_INFO_NTC = "2008";
    // 删除/解散牌桌通知
    public static final String DEAL_TABLE_NTC = "2009";
    // 俱乐部邀请 审核结果通知
    public static final String INVITATION_RESULT_NTC = "2010";
    // 更新俱乐部房卡
    public static final String UPDATE_CLUB_ROOM_CARD_NTC = "2011";
    // 加入牌桌通知
    public static final String JOIN_TABLE_NTC = "2012";
    // 红点通知
    public static final String READ_POINT_NTC = "2013";
    // 解散俱乐部
    public static final String DISSOLVE_CLUB_NTC = "2014";

    //================================大厅返回消息=========================


    
    // 创建牌桌返回
    public static final String ROOM_List = "createPrivateRoom";
    
    
    
  //================================发送到大厅消息=========================
    // 加入牌桌
    public static final String JOIN_TABLE = "4001";
    // 创建牌桌
    public static final String CREATE_TABLE = "4002";
    // 牌桌列表
    public static final String CLUB_TABLE_LIST = "4003";
    // 牌桌规则变更
    public static final String CLUB_TABLE_CHANGE = "4004";
    // 删除牌桌
    public static final String DELETE_CLUB_TABLE = "4005";
    // 创建牌桌成功
    public static final String CREATE_TABLE_SUCCESS = "4006";

    //================================麻将返回的消息=========================

    // 删除牌桌
    public static final String DEAL_TABLE = "5001";
    // 退出牌桌
    public static final String QUIT_TABLE = "5002";
    // 加入牌桌
    public static final String JOIN_TABLE_SUCCESS = "5003";
    // 玩家准备
    public static final String PLAYER_READY = "5004";
    // 游戏开始
    public static final String GAME_START = "5005";
    // 下一局数据更新
    public static final String NEXT_ROUND = "5006";
    //===========================v68消息通知==============================
    //个人账户显示
    public static final String User_info="50001";
}