package com.dmg.lobbyserver.config;

/**
 * 消息号配置
 *
 * @author linanjun
 */
public class MessageConfig {
    // 同步玩家金币
    public static final String SYNC_USER_GOLD = "0001";
    // 同步玩家房间
    public static final String SYNC_USER_ROOM = "0002";

    // 心跳
    public static final String Heart_Beat = "9999";
    // 获取用户信息
    public static final String GET_USER_INFO = "1000";
    // 登录大厅
    public static final String LOGIN_LOBBY = "1001";
    // 大厅活动开放信息
    public static final String LOBBY_ACTIVITY_INFO = "1002";
    // 大厅红点信息
    public static final String LOBBY_RED_POINT_INFO = "1003";
    // 邮件列表
    public static final String EMAIL_LIST = "1004";
    // 邮件已读
    public static final String HAS_RED_EMAIL = "1005";
    // 删除单个邮件
    public static final String DELETE_TEXT_EMAIL = "1006";
    // 删除所有已读邮件
    public static final String DELETE_TEXT_ALL_EMAIL = "1007";
    // 删除已过期的优惠码邮件
    public static final String DELETE_EXPIRE_PROMOTIONCODE_EMAIL = "1008";
    // 删除所有已过期的优惠码邮件
    public static final String DELETE_ALL_EXPIRE_PROMOTIONCODE_EMAIL = "1009";
    // 领取优惠码
    public static final String RECIEVE_PROMOTIONCODE = "1010";
    // 从保险箱中提钱到余额
    public static final String TAKE_OUT_MONEY_TO_ACCOUNT = "1011";
    // 从余额存钱到保险箱
    public static final String DEPOSIT_MONEY_TO_STRONG_BOX = "1012";
    // 设置保险箱密码
    public static final String SET_STRONG_BOX_PASSWORD = "1013";
    // 修改保险箱密码
    public static final String CHANGE_STRONG_BOX_PASSWORD = "1014";
    // 客服信息
    public static final String CUSTOMER_SERVICE_INFO = "1015";
    // 设置脸部 手势 指纹信息
    public static final String SYS_SETTING = "1016";
    // 更新用户短信设置
    public static final String UPDATE_USER_SMS_CONFIG = "1017";
    // 获取用户短信设置
    public static final String GET_USER_SMS_CONFIG = "1018";
    // 验证用户密码
    public static final String VALIDATE_PASSWORD = "1019";
    // 保存回访信息
    public static final String RETURN_VISIT = "1020";
    // 更新红点通知
    public static final String RED_POINT_NTC = "1021";
    // 领取邮件附件
    public static final String RECIEVE_ITEM = "1022";

    // 转盘抽奖
    public static final String TURN_TABLE_EXTRACT = "2001";
    // 转盘奖品列表
    public static final String TURN_TABLE_LIST = "2002";
    // 新手大礼包奖品列表
    public static final String GIFT_LIST = "2003";
    // 新手大礼包领取
    public static final String GIFT_GET = "2004";
    // 分享礼包领取
    public static final String SHARE_GET = "2005";
    // 分享礼包物品列表
    public static final String SHARE_LIST = "2006";
    // 绑定礼包领取
    public static final String BIND_GET = "2007";
    // 绑定礼包物品列表
    public static final String BIND_LIST = "2008";
    // 签到
    public static final String SIGN_GET = "2009";
    // 签到情况
    public static final String SIGN_LIST = "2010";
    // 金币排行榜
    public static final String GOLDLEADERBOARD = "2011";
    //=====================================================
    // 获取商城列表
    public static final String SYS_MALL_CONFIG_LIST = "3000";

    //=====================================================
    // 红点通知
    public static final String READ_POINT_NTC = "4001";
    // 转盘红点通知
    public static final String TURN_TABLE_NTC = "4002";
    // 消息红点通知
    public static final String MESSAGE_NTC = "4003";
    // 任务红点通知
    public static final String TASK_NTC = "4004";
    // 分享红点
    public static final String SHARE_NTC = "4005";
    // 跑马灯通知
    public static final String MARQUEE_NTC = "4006";



    //=====================================================
    // 更新用户信息
    public static final String UPDATE_USER_INFO_NTC = "3005";
    // 礼包信息
    public static final String GIFT_DATA = "3006";
    //个人账户显示
    public static final String USER_INFO="5001";
    //设置真实姓名
    public static final String USER_NAME="5002";
    //绑定手机
    public static final String BINDING_PHONE="5003";
    //修改密码
    public static final String REVICE_PASSWORD="5004";
    //设置密码
    public static final String SET_PASSWORD="5050";
    //设置生日
    public static final String SET_BIRTH="5005";
    //设置收货地址
    public static final String SET_ADDRESS="5006";
    //设置游戏名称
    public static final String SET_USERNAME="5007";
    //修改手机号
    public static final String UPDATE_PHONE="5008";
    //游戏记录显示
    public  static  final String GAME_RECORD="5009";
    //优惠记录
    public  static  final String DISCOUNTS_RECORD="5010";
    //充值记录
    public  static  final String VOUCHER_RECORD="5011";
    //提款纪录
    public  static  final String DRAWINGS_RECORD="5012";
    //绑定邮箱
    public  static  final String SET_EMAIL="5013";
    //银行卡信息
    public  static  final String GET_BANKDETAILS="5014";
    //绑定银行卡验证码验证开户信息
    public  static  final String SETBANK_TOCODE="5015";
    //绑定银行卡信息
    public  static  final String SETBANK_MESSAGE="5016";
    //洗码比例,金额显示
    public static final String CODE_WASH_INFO="5017";
    //洗码
    public static final String CODE_WASH="5018";
    //vip等级显示
    public static final String VIP__PRIVILEGE="5020";
    //修改邮箱
    public static final String UPDATE_EMAIL="5025";
    // 类型显示
    public static final String TYPE_SHOW = "5026";
    // 日常任务
    public static final String DAILY_TASK = "5070";
    // 领取任务
    public static final String GET_TASK = "5071";
    // 一键领取任务
    public static final String GETALL_TASK = "5072";
    // 存款任务
    public static final String DEPOSIT_TASK = "5073";
    //积分显示
    public static final String CONVERSION_TASK = "5074";
    //确认兑换积分
    public static final String CONFIRM_CONVERSION_TASK = "5075";
    //兑换记录
    public static final String CONVERSION_RECORD = "5076";
    //删除银行信息
    public static final String DELETE_BANK = "5077";
    //验证密码
    public static final String CHECK_PASSWORD = "5078";
    //充值金额显示
    public static final String RECHARGE_MONNEY_SHOW = "5079";
    //优惠代码显示
    public static final String PROMOTION_CODE_SHOW = "5080";
    //优惠活动显示
    public static final String PROMOTION_ACTIVITY_SHOW = "5081";
    //立刻充值
    public static final String PROMOTION_QUCKLIY = "5082";
    //优惠活动显示
    public static final String PROMOTION_ALL_SHOW = "5083";
    //优惠活动立即领取
    public static final String PROMOTION_RECEIVE = "5084";
    //用户分享日志
    public static final String SHARE_LOG = "5085";
    //用户游戏战绩
    public static final String USER_RECORD = "5086";
    //用户修改头像
    public static final String UPDATE_USER_ICON = "5087";
    //vnnnpay回兑
    public static final String USER_VNNN_PAY_EXCHANGE = "5088";
    //vnnnpay回兑限制
    public static final String USER_VNNN_EXCHANG_LIMIT = "5089";





}