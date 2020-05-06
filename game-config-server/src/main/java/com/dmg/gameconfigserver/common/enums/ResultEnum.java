package com.dmg.gameconfigserver.common.enums;


/**
 * @Author: ChenHao
 * @Date: 2018/5/22 17:21
 */
public enum ResultEnum {
    PARAM_ERROR(1000, "参数错误"),
    HAS_EXIT(1001, "配置已存在"),
    CONFIG_ERROR(1002, "配置错误"),
    LOGIN_ERROR(1003, "账户或密码错误"),
    NOT_LOGIN(1004, "NOT LOGIN"),
    USER_EXIST(1005, "账户已存在"),
    USER_NO_EXIST(1006, "账户不存在"),
    NOT_AUTH(1007, "NOT AUTH"),
    OTHER_DEALING(1008, "其他人正在处理"),
    ORDER_STATUS_ERROR(1009, "订单状态错误"),
    HAS_NOT_EXIT(1010, "配置不存在"),
    ROLE_NOT_EXIT(1011, "角色不存在"),
    ROLE_EXIT(1012, "角色已存在"),
    USER_STATUS_ERROR(1013, "该账号已被禁用"),
    JQ_WITHDRAW_ORDER_ERROR(1014, "提现订单审核失败"),
    PERSION_RECHARGE_STATUS_ERROR(1015, "审核状态异常"),
    EMAIL_NOT_EXIT(1016, "邮件不存在"),
    EMAIL_SEND(1017, "邮件已发送，不能进行编辑|删除操作"),
    PLAYER_HAS_BE_DISTRIBUTE(1018, "该玩家已拥有靓号,不能再分配"),
    GOOD_NUM_HAS_BE_DISTRIBUTE(1019, "该靓号已被分配"),
    PLAYER_NO_GOOD_NUM(1020, "该玩家无靓号"),
    EMAIL_RECEIVE(1021, "邮件已领取"),
    SUCCESS(1, "成功");
    private Integer code;
    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getCodeStr() {
        return String.valueOf(code);
    }

    public String getMsg() {
        return msg;
    }
}
