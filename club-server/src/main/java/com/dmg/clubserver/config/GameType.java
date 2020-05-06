package com.dmg.clubserver.config;

import java.util.Arrays;

public enum GameType {
    XUEZHAN_3(1,"血战麻将 (三人场)"),
    XUEZHAN_4(2,"血战麻将 (四人场)"),
    TUIDAOHU(3,"推倒胡"),
    XUELIUCHENGHE(4,"血流成河");

    private int key;
    private String value;
    GameType(int key, String value) {
        this.key = key;
        this.value = value;
    }
    public static String getValueByKey(int key){
        return Arrays.stream(GameType.values()).filter(g -> g.getKey()==key).findFirst().get().getValue();
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
