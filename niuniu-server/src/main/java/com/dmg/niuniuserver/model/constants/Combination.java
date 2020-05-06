package com.dmg.niuniuserver.model.constants;

/**
 * 牌型枚举
 */
public enum Combination {
    UNDEFINE(0),
    HIGH_CARD(1),         //散牌;
    FALSE_CATTLE_ONE(2),    //假牛1;
    CATTLE_ONE(3),         //牛一;
    FALSE_CATTLE_TWO(4),    //假牛2;
    CATTLE_TWO(5),          //牛二;
    FALSE_CATTLE_THREE(6),    //假牛3;
    CATTLE_THREE(7),          //牛三;
    FALSE_CATTLE_FOUR(8),    //假牛4;
    CATTLE_FOUR(9),          //牛四;
    FALSE_CATTLE_FIVE(10),    //假牛5;
    CATTLE_FIVE(11),          //牛五;
    FALSE_CATTLE_SIX(12),    //假牛6;
    CATTLE_SIX(13),          //牛六;
    FALSE_CATTLE_SEVEN(14),    //假牛7;
    CATTLE_SEVEN(15),          //牛七;
    FALSE_CATTLE_EIGHT(16),    //假牛8;
    CATTLE_EIGHT(17),          //牛八;
    FALSE_CATTLE_NINE(18),    //假牛9;
    CATTLE_NINE(19),         //牛九;
    FALSE_CATTLE_CATTLE(20),    //假牛牛;
    CATTLE_CATTLE(21),        //牛牛
    FALSE_SUEN_ZI_CATTLE(22),        //假顺子牛
    SUEN_ZI_CATTLE(23),        //顺子牛
    FALSE_TONG_HUA_CATTLE(24),        //假同花牛;
    TONG_HUA_CATTLE(25),        //同花牛;
    FALSE_WU_HUA_CATTLE(26),        //假五花牛
    WU_HUA_CATTLE(27),        //五花牛
    //FALSE_HU_LU_CATTLE(28),        //假葫芦牛;
    //HU_LU_CATTLE(29),        //葫芦牛;
    FALSE_BOMB_CATTLE(28),        //假炸弹牛;
    BOMB_CATTLE(29),        //炸弹牛;
    FALSE_WU_XIAO_CATTLE(30),        //假五小牛;
    WU_XIAO_CATTLE(31);       //五小牛;
    private int intValue;

    Combination(int value) {
        this.intValue = value;
    }

    public int getValue() {
        return intValue;
    }
}