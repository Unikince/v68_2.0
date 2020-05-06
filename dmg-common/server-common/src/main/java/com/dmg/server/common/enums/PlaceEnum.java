package com.dmg.server.common.enums;

/**
 * @Author liubo
 * @Description //TODO 位置
 * @Date 17:18 2020/1/14
 */
public enum PlaceEnum {

    CODE_ALL(0, "所有界面"),
    CODE_LOBBY(100, "大厅");

    private Integer code;

    private String msg;

    PlaceEnum(Integer code, String msg) {
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
