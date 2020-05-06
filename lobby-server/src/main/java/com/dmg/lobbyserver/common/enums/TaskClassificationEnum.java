package com.dmg.lobbyserver.common.enums;

/**
 * @Author liubo
 * @Description //TODO 任务分类
 * @Date 18:09 2019/11/26
 */
public enum TaskClassificationEnum {

    CODE_EVERYDAY(1, "每日任务"),
    CODE_GROP_UP(2, "成长任务");

    private Integer code;

    private String msg;

    TaskClassificationEnum(Integer code, String msg) {
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
