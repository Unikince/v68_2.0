package com.dmg.server.common.enums;

/**
 * @Author liubo
 * @Description //TODO 账变类型
 * @Date 17:18 2020/1/14
 */
public enum AccountChangeTypeEnum {

    CODE_TASK(301, "任务"),
    CODE_CHANNEL_RECHARGE(101, "渠道充值"),
    CODE_ARTIFICIAL_RECHARGE(102, "人工充值"),
    CODE_DRAWING(201, "提款"),
    CODE_EMAIL(401, "邮件"),
    TRANSFER_ACCOUNT_DEDUCT(501, "代理转账扣除"),
    TRANSFER_ACCOUNT_RECEVICE(502, "玩家收到转账"),
    TRANSFER_ACCOUNT_ROLLBACK(503, "代理转账撤回"),
    AGENT_SETTLE_BROKERAGE(601, "佣金结算"),
    ;

    private Integer code;

    private String msg;

    AccountChangeTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
