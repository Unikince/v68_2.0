package com.dmg.server.common.enums;

/**
 * @description:
 * @return
 * @author mice
 * @date 2019/12/31
*/
public enum PlatformRechargeStatusEnum {

    WAITE_PAY(1, "等待支付"),
    PAY_FAIL(5, "支付失败"),
    NO_ARRIVE(10, "未到账"),
    FINISH(15, "已完成"),
    REPLENISH_SUCCESS(20, "补单成功");

    private Integer code;

    private String msg;

    PlatformRechargeStatusEnum(Integer code, String msg) {
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
