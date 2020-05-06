package com.dmg.lobbyserver.model.constants;

public enum RecievePromotionCodeConditionType {
    NEW_DEPOSIT(1,"有新存款"),

            ;

    private int key;
    private String value;
    RecievePromotionCodeConditionType(int key, String value) {
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
