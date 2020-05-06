package com.dmg.server.common.enums;

/**
 * @Author liubo
 * @Description //TODO 任务领取状态
 * @Date 11:51 2019/11/26
 */
public enum ReceiveAwardEnum {

    CODE_0(0, "未领取"),
    CODE_1(1, "已领取");

    private Integer code;

    private String msg;

    ReceiveAwardEnum(Integer code, String msg) {
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
