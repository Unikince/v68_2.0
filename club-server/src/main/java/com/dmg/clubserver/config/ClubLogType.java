package com.dmg.clubserver.config;

/**
 * @description: 俱乐部日志类型
 * @param
 * @return
 * @author mice
 * @date 2019/6/3
*/
public enum ClubLogType {
    CREATE_TABLE(1,"创建牌桌"),
    DELETE_TABLE(2,"删除牌桌"),
    FORCE_LEAVE(3,"请离"),
    AGREE_JOIN(4,"同意加入"),
    REFUSE_JOIN(5,"拒绝加入"),
    QUIT_CLUB(6,"退出俱乐部"),
    JOIN_CLUB(7,"加入俱乐部");


    private int key;
    private String value;
    ClubLogType(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
