package com.dmg.bairenlonghu.model.constants;

/**
 * 牌型枚举
 */
public enum Combination {
    UNDEFINE(-1),
    HIGH_CARD(0),         //散牌;
    CATTLE_ONE(1),         //一;
    CATTLE_TWO(2),          //二;
    CATTLE_THREE(3),          //三;
    CATTLE_FOUR(4),          //四;
    CATTLE_FIVE(5),          //五;
    CATTLE_SIX(6),          //六;
    CATTLE_SEVEN(7),          //七;
    CATTLE_EIGHT(8),          //八;
    CATTLE_NINE(9);       //九;
    private int intValue;

    Combination(int value) {
        this.intValue = value;
    }

    public int getValue() {
        return intValue;
    }
}