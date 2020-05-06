package com.dmg.lobbyserver.config;

/**
 * @Description
 * @Author mice
 * @Date 2019/5/27 18:47
 * @Version V1.0
 **/
public class RedisKey {
    /**
     * 验证码
    */
    public static final String VALIDATE_CODE = "v0:";
    /**
     * userCode
     */
    public static final String USER_CODE = "user_code";

    /**
     * VNNN请求支付key
     */
    public static final String VNNN_PAY_REQUEST_ID = "VNNN_PAY_REQ_ID";

    /**
     * JQ请求支付key
     */
    public static final String JQ_PAY_REQUEST_ID = "JQ_PAY_REQ_ID";

    /**
     * JQ请求支付key
     */
    public static final String JQ_THIRD_PAY_REQUEST_ID = "JQ_THIRD_PAY_REQ_ID";

    /**
     * JQ代付回调key
     */
    public static final String JQ_DAIFU_REQUEST_ID = "JQ_DAIFU_REQ_ID";
    /**
     * 提现订单请求
     */
    public static final String WITHDEAW_ORDER_REQUEST = "WITHDEAW_ORDER_REQUEST";

    /**
     * 系统配置
     */
    public static final String SYS_CONFIG = "sysconfig";

    /**
     * 用户房间
     */
    public static final String USER_GAME_ROOM = "userGameRoom";
    /**
     * 用户活跃度
     */
    public static final String USER_ACTIVITY_LEVEL = "user_activity_level";

    /**
     * 用户当日登陆标记
     */
    public static final String USER_LGOGIN = "lobby_user_login";

    /**
     * 用户账变
     */
    public static final String ACCOUNT_CHANGE_LOG = "account_change_log";


}