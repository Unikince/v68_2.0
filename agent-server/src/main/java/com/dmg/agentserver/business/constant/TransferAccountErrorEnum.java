package com.dmg.agentserver.business.constant;

import com.dmg.agentserver.core.exception.IErrorEnum;

public enum TransferAccountErrorEnum implements IErrorEnum {
    /** 禁止转账 */
    FORBID(101, "禁止转账"),
    /** 玩家不存在 */
    PLAYER_NOT_EMPTY(102, "玩家不存在"),
    /** 禁止转账时间 */
    FORBID_TIME(102, "禁止转账时间"),
    /** 不是下级关系 */
    NOT_CHILD(103, "不是下级关系"),
    /** 总充值不满足 */
    TOTAL_RECHARGE_NOT_ENOUGH(104, "总充值不满足"),
    /** 总流水不满足 */
    TOTAL_WATER_NOT_ENOUGH(105, "总流水不满足"),
    /** 单笔金额不满足 */
    ONCE_AMOUNT_ENOUGH(106, "单笔金额不满足"),
    /** 今日转账次数超出 */
    TIMES_TODAY_OVER(107, "今日转账次数超出"),
    /** 今日转账金额超出 */
    AMOUT_TODAY_OVER(108, "今日转账金额超出"),
    /** 订单已完成 */
    ORDER_FINISH(111, "订单已完成"),

    ;

    /** 错误码 */
    private int code;
    /** 错误描述 */
    private String desc;

    /**
     * 构建方法
     *
     * @param code 错误码
     * @param desc 错误描述
     */
    private TransferAccountErrorEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 获取：错误码
     *
     * @return 错误码
     */
    @Override
    public int getCode() {
        return this.code;
    }

    /**
     * 获取：错误描述
     *
     * @return 错误描述
     */
    @Override
    public String getDesc() {
        return this.desc;
    }
}
