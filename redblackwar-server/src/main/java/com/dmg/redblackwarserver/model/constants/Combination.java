package com.dmg.redblackwarserver.model.constants;

/**
 * @Description  特殊牌型
 * @Author jock
 * @Date 9:56
 * @Version V1.0
 **/
public enum  Combination {
    /** 无 */
    NONE(-1),
    /**特殊牌型(2,3.5) */
    SPECIAL(0),
    /** 单张 */
    HIGHCARD(1),
    /** 对子 */
    PAIR(2),
    /** 顺子 */
    PROGRESSION(3),
    /** 金花 */
    FLUSH(4),
    /** 顺金 */
    STRAIGHTFLUSH(5),
    /** 豹子 */
    LEOPARD(6);
    private int intValue;
    Combination(int value) {
        this.intValue = value;
    }

    public int getValue() {
        return intValue;
    }

    public static Combination forValue(int value) {
    	return values()[value + 1];
    }
}
