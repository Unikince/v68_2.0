package com.dmg.lobbyserver.result;

/**
 * Author: ChenHao
 * Date: 2018/5/22 17:21
 */
public enum ResultEnum {
    UNKONW_ERROR(-1, "未知错误"),
    SUCCESS(1, "成功"),
    LESS_PARAMS(1001, "传入参数不全"),
    PARAM_ERROR(1002, "参数错误"),
    CODE_EXIST(1003, "编码已存在"),
    SYSTEM_EXCEPTION(1004, "系统异常"),
    SQL_EXCEPTION(1005, "数据库异常"),
    ACCOUNT_NOT_EXIST(1006, "账号不存在"),
    ACCOUNT_ERROR(1007, "账号或密码错误"),
    VALIDATE_CODE_ERROR(1008, "验证码错误"),
    VALIDATE_CODE_TIME_OUT(1009, "验证码已过期"),
    SQL_UPDATE_FAIL(1010, "数据更新失败"),
    PASSWORD_ERROR(1011, "密码错误"),
    ACCOUNT_VALIDATE_ERROR(1012, "验证失败"),
    OPERRATE_FAIL(1013, "操作失败"),
    BUSINESS_EXCEPTION(2000, "业务异常"),
    WECHAT_CODE_HAS_USE_OR_ERROR(2001, "code码错误或者该code码已经使用过了"),
    OPENID_INVALID(2002, "openId无效"),
    SENSITIVE_CHARACTER(2003, "敏感字符"),
    ACCOUNT_HAS_EXIST(2004, "账号已存在"),
    PHONE_HAS_REGISTE(2005, "该号码已被注册"),
    PHONE_NO_REGISTE(2006, "该手机号未注册"),
    DEVICE_CODE_DIFF(2007, "手机号与上次登录机型不符"),
    NO_BIND_PHONE(2008, "该账号未绑定手机"),
    BIND_PHONE_DIFF_USERNAME(2009, "账号与绑定手机号不一致"),
    LOGIN_LOBBY_VALIDATE_FAIL(2010, "登录大厅验证失败"),
    CANT_RECIEVE_AGAIN(2011, "不可重复领取"),
    PROMOTION_CODE_HAS_EXPIRE(2012, "优惠码已过期"),
    RECIEVE_PROMOTION_CODE_CONDITION_ERROR(2013, "未达到领取条件"),
    STRONG_BOX_INSUFFICIENT(2014, "保险箱余额不足"),
    ACCOUNT_INSUFFICIENT(2015, "账户余额不足"),
    STRONG_BOX_PASSWORD_ERROR(2016, "保险箱密码错误"),
    DATA_CONVERSION(2017, "数据结构异常"),
    SIGN_REPEAT(2018, "今天已经签到了"),
    EXTRACT_OUT(2019, "今天已经抽过了"),
    EMAIL_ISSAME(2020, "旧邮箱输入错误"),
    SYSTEM_DATA_ISNULL(2021, "无数据"),
    PASSWORD_ISSAME(2022, "旧密码错误"),
    PHONE_ISSAME(2023, "手机号不一致"),
    CONVERTI_EXCEPTION(2024, "输入的积分大于账户积分"),
    TASK_ISPULL(2025, "今日兑换进度已满"),
    EMAIL_ISEXIST(2026, "邮箱已存在"),
    RECHARGE_NUMBER_PULL(2027, "充值金额必须大于20小于20000"),
    PROMOTION_CODE_BO_EXIT(2028, "优惠码不存在"),
    PROMOTION_RECEIVE_MESSAGE_ISNULL(2029, "尊敬的会员,您未绑定完善个人资料,申请优惠请您先完善资料!"),
    SHARE_LIMIT_DAY(2030, "今日已分享,请明天再来"),
    JIFEN_ISNULL(2031, "请输入正确的积分"),
    USER_JIFEN_ISNULL(2032, "当前无可兑换积分"),
    LIMIT_JIFEN(2033, "当前积分超过今日兑换量"),
    PHONE_HAS_BE_USE(2034, "手机号已被使用"),
    RETURN_VISIT_ERROR(2035, "频繁请求回访"),
    PHONE_ISSAME_ASNEWPHONE(2036, "新手机号与原手机号一致"),
    EMAIL_ISSAME_ASNEWEMAIL(2037, "新邮箱与原邮箱一致"),
    OLDE_PHONE_IS_NOT_SAME(2038, "旧手机号与原手机号不一致"),
    PASSWORD_IS_NOT_SAME(2039, "新密码与旧密码一致"),
    ACCOUNT_DISABLE(2040, "账号已被封禁"),
    EMAIL_BO_EXIT(2041, "该邮件不存在"),
    EMAIL_FINISH(2042, "该订单已完成"),
    TASK_RECEIVE(5001, "任务未完成"),
    TASK_NOT_REWARD(5002, "该礼包已领取"),
    CREATE_WITHDRAW_ORDER_FAIL(5003, "创建提现订单失败"),
    WITHDRAW_ORDER_REQUEST_ERROR(5004, "订单创建频繁,请稍后再试"),
    WITHDRAW_UP_TODAY_LIMIT(5005, "超过每日提现上限"),
    WITHDRAW_DOWN_LIMIT(5006, "少于最低单笔提现金额"),
    WITHDRAW_UP_LIMIT(5007, "高于最高单笔提现金额"),
    MAX_WITHDRAWAL_TIME(5008, "提现次数达到上限"),
    WITHDRAWAL_NO_OPEN(5009, "提现暂时关闭"),
    WITHDRAW_TURNOVER_LIMIT(5010, "未达到指定流水金额"),
    WITHDRAW_DATE_LIMIT(5011, "未到提现时间"),
    VALIDATE_CODE_SEND_ERROR(5012, "验证码发送失败"),
    SYS_ERROR(5013, "服务异常"),
    DEC_ERROR(5014, "解密数据失败"),
    VND_ERROE(5015, "vnd参数错误"),
    GOLD_NOT_ENOUGH(5016, "额度不足"),
    APPID_ERROR(5017, "APP_ID验证失败"),
    USER_NO_EXIT(5018, "账号验证失败"),
    SIGN_ERROR(5019, "签名验证失败"),
    ;
    private Integer code;
    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
