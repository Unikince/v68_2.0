package com.dmg.server.common.enums;

/**
 * @description: 提现订单状态
 * @author mice
 * @date 2019/12/30
*/
public enum WithdrawOrderStatusEnum {

    WAITE_REVIEW(1, "等待审核"),
    REFUSE_REVIEW(5, "审核拒绝"),
    NO_ARRIVE(10, "未到账"),
    FINISH(15, "已完成"),
    REPLENISH_SUCCESS(20, "补单成功"),
    PAYING(25, "支付中"),
    REFUSE_WITHDRAW(30, "拒绝提现");

    private Integer code;

    private String msg;

    WithdrawOrderStatusEnum(Integer code, String msg) {
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
