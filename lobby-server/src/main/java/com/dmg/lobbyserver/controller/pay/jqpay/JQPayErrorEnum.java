package com.dmg.lobbyserver.controller.pay.jqpay;

/**
 * @Description
 * @Author mice
 * @Date 2019/11/27 9:56
 * @Version V1.0
 **/
public enum JQPayErrorEnum {
    SIGN_ERROR(1000, "签名验证失败"),
    APPID_ERROR(1001, "APP_ID验证失败"),
    USER_NO_EXIT(1002, "用户验证失败"),
    ITEM_NO_EXIT(1003, "商品不存在"),
    PRICE_NO_MATCH(1004, "价格不匹配"),
    SYS_SEND_GOLD_ERROR(1005, "系统发送金币失败"),
    OUR_ORDER_CREATE_ERROR(1006, "我方订单未创建"),

    SUCCESS(1, "成功");
    private Integer code;
    private String msg;

    JQPayErrorEnum(Integer code, String msg) {
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