package com.dmg.server.common.enums;

/**
 * @Author liubo
 * @Description //TODO 场次位置
 * @Date 17:18 2020/1/14
 */
public enum FilePlaceEnum {

    CODE_FILE_LOBBY(0, "选场大厅");

    private Integer code;

    private String msg;

    FilePlaceEnum(Integer code, String msg) {
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
