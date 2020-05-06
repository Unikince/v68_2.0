package com.dmg.bairenniuniuserver.model.constants;

/**
 * 牌型枚举
 */
public enum Combination {
    UNDEFINE(-1),
    HIGH_CARD(0),         //散牌;
    CATTLE_ONE(1),         //牛一;
    CATTLE_TWO(2),          //牛二;
    CATTLE_THREE(3),          //牛三;
    CATTLE_FOUR(4),          //牛四;
    CATTLE_FIVE(5),          //牛五;
    CATTLE_SIX(6),          //牛六;
    CATTLE_SEVEN(7),          //牛七;
    CATTLE_EIGHT(8),          //牛八;
    CATTLE_NINE(9),         //牛九;
    CATTLE_CATTLE(10),        //牛牛
    BOMB_CATTLE(11),        //炸弹牛;
    WU_HUA_CATTLE(12),        //五花牛
    WU_XIAO_CATTLE(13);       //五小牛;
    private int intValue;

    Combination(int value) {
        this.intValue = value;
    }

    public int getValue() {
        return intValue;
    }
}