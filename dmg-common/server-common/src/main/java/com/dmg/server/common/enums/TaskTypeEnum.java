package com.dmg.server.common.enums;

/**
 * @Author liubo
 * @Description //TODO 任务类型
 * @Date 18:09 2019/11/26
 */
public enum TaskTypeEnum {

    CODE_GAME(1, "游戏"),
    CODE_SHARE(2, "分享"),
    CODE_LOGIN(3, "登陆"),
    CODE_PURCHASE_GOODS(4, "购买商品"),
    CODE_GOLD(5, "金币突破"),
    CODE_ACTIVITYLEVEL(6, "活跃度"),
    CODE_GAME_WIN(7, "游戏胜利"),
    CODE_BINDING_PHONE(8, "绑定手机号"),
    CODE_GAME_WIN_GOLD(9, "游戏赢金币");

    private Integer code;

    private String msg;

    TaskTypeEnum(Integer code, String msg) {
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
