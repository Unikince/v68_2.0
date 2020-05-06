package com.dmg.dataserver.common.net.web;

public enum BaseResultEnum {
    PARAM_ERROR(1000, "参数错误"),

    SUCCESS(1, "成功");
    private Integer code;
    private String msg;

    BaseResultEnum(Integer code, String msg) {
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
