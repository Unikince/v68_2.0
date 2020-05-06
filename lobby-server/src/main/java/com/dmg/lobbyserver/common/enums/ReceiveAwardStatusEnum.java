package com.dmg.lobbyserver.common.enums;

/**
 * @Author liubo
 * @Description //TODO 任务领取status
 * @Date 12:55 2019/11/28
 */
public enum ReceiveAwardStatusEnum {

    CODE_SURE_RECEIVE(0, "可领取"),
    CODE_NOT_COMPLETE(1, "未完成"),
    CODE_HAVE_RECEIVED(2, "已领取");

    private Integer code;

    private String msg;

    ReceiveAwardStatusEnum(Integer code, String msg) {
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
