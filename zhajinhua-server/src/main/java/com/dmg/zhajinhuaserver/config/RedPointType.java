package com.dmg.zhajinhuaserver.config;

import java.util.Arrays;

public enum RedPointType {
    REVIEW(1,"审批通知"),
    JOIN(2,"加入通知");

    private int key;
    private String value;
    RedPointType(int key, String value) {
        this.key = key;
        this.value = value;
    }
    public static String getValueByKey(int key){
        return Arrays.stream(RedPointType.values()).filter(g -> g.getKey()==key).findFirst().get().getValue();
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
