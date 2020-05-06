package com.dmg.lobbyserver.model.constants;

public enum CommonItemType {
    GOLD(1,"金币"),
    INTEGRAL(2,"积分"),
    MAKE_PERSISTENT_EFFORTS(3,"再接再厉"),
    PROMO_CODE(4,"优惠码"),
            ;

    private int key;
    private String value;
    CommonItemType(int key, String value) {
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
