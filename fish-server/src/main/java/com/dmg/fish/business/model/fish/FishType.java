package com.dmg.fish.business.model.fish;

/**
 * 鱼类型
 */
public enum FishType {
    /** 倍数鱼 */
    MULTIPLE(1, "倍数鱼"),
    /** 同类炸弹鱼 */
    KINDS_BOMB(3, "同类炸弹鱼"),
    ;

    public final int code;
    public final String desc;

    private FishType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static FishType get(int code) {
        for (FishType obj : FishType.values()) {
            if (obj.code == code) {
                return obj;
            }
        }
        return null;
    }
}
