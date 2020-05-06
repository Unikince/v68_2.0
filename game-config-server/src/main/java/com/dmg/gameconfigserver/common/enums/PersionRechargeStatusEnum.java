package com.dmg.gameconfigserver.common.enums;

/**
 * @Author liubo
 * @Description //TODO 人工充值订单状态
 * @Date 16:11 2020/1/20
 */
public enum PersionRechargeStatusEnum {

    WAIT_FOR_EXAMINE(1, "等待审核"),
    ADOPT_EXAMINE(2, "审核通过"),
    REFUSE_EXAMINE(3, "审核拒绝");
    private Integer code;
    private String msg;

    PersionRechargeStatusEnum(Integer code, String msg) {
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

    public static String getMsg(int code) {
        for (PersionRechargeStatusEnum e : values()) {
            if (e.getCode() == code) {
                return e.getMsg();
            }
        }
        return null;
    }
}
