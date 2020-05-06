package com.dmg.lobbyserver.model.constants;

public enum GoldChangeType {
    BET(1,"下注"),
    PAY_OUT(2,"派彩"),
            ;

    private int key;
    private String value;
    GoldChangeType(int key, String value) {
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
