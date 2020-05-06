package com.dmg.fish.business.strategy;

/**
 * 刷鱼策略
 */
public enum StrategyType {
    /** 点 */
    POINT(1, "点"),
    /** 矩形范围 */
    RECTANGLE(2, "矩形范围"),
    /** 圆 */
    CIRCLE(3, "圆"),
    /** 随机策略 */
    RANDOM(4, "随机策略"),

    ;
    public final int val;
    public final String desc;

    private StrategyType(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    public static StrategyType get(int type) {
        for (StrategyType t : StrategyType.values()) {
            if (t.val == type) {
                return t;
            }
        }
        return null;
    }
}
