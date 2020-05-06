package com.dmg.gameconfigserver.common.enums;


/**
 * @description: 游戏状态
 * @author mice
 * @date 2020/1/7
*/
public enum GameStatusEnum {
    MAINTAIN(0, "维护"),
    NORMAL(1, "正常"),
    FLUENT(2, "流畅"),
    CROWD(3, "拥挤"),
    HOT(4, "火爆");
    private Integer code;
    private String msg;

    GameStatusEnum(Integer code, String msg) {
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

    public static String getMsg(int code){
        for (GameStatusEnum e : values()){
            if (e.getCode()==code){
                return e.getMsg();
            }
        }
        return null;
    }
}
