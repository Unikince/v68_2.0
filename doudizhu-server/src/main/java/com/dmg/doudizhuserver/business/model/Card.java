package com.dmg.doudizhuserver.business.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 1-13：方块，14-26：梅花，27-39：红桃，40-52：黑桃，53：小王，54：大王
 */
public enum Card {

    /* 方块 A - K,1-13 */
    FANG_KUAI_A(1, 14, CardSuit.FANG_KUAI),
    FANG_KUAI_ER(2, 15, CardSuit.FANG_KUAI),
    FANG_KUAI_SAN(3, 3, CardSuit.FANG_KUAI),
    FANG_KUAI_SI(4, 4, CardSuit.FANG_KUAI),
    FANG_KUAI_WU(5, 5, CardSuit.FANG_KUAI),
    FANG_KUAI_LIU(6, 6, CardSuit.FANG_KUAI),
    FANG_KUAI_QI(7, 7, CardSuit.FANG_KUAI),
    FANG_KUAI_BA(8, 8, CardSuit.FANG_KUAI),
    FANG_KUAI_JIU(9, 9, CardSuit.FANG_KUAI),
    FANG_KUAI_SHI(10, 10, CardSuit.FANG_KUAI),
    FANG_KUAI_J(11, 11, CardSuit.FANG_KUAI),
    FANG_KUAI_Q(12, 12, CardSuit.FANG_KUAI),
    FANG_KUAI_K(13, 13, CardSuit.FANG_KUAI),

    /* 梅花 A - K,14-26 */
    MEI_HUA_A(14, 14, CardSuit.MEI_HUA),
    MEI_HUA_ER(15, 15, CardSuit.MEI_HUA),
    MEI_HUA_SAN(16, 3, CardSuit.MEI_HUA),
    MEI_HUA_SI(17, 4, CardSuit.MEI_HUA),
    MEI_HUA_WU(18, 5, CardSuit.MEI_HUA),
    MEI_HUA_LIU(19, 6, CardSuit.MEI_HUA),
    MEI_HUA_QI(20, 7, CardSuit.MEI_HUA),
    MEI_HUA_BA(21, 8, CardSuit.MEI_HUA),
    MEI_HUA_JIU(22, 9, CardSuit.MEI_HUA),
    MEI_HUA_SHI(23, 10, CardSuit.MEI_HUA),
    MEI_HUA_J(24, 11, CardSuit.MEI_HUA),
    MEI_HUA_Q(25, 12, CardSuit.MEI_HUA),
    MEI_HUA_K(26, 13, CardSuit.MEI_HUA),

    /* 红桃 A - K，27-39 */
    HONG_TAO_A(27, 14, CardSuit.HONG_TAO),
    HONG_TAO_ER(28, 15, CardSuit.HONG_TAO),
    HONG_TAO_SAN(29, 3, CardSuit.HONG_TAO),
    HONG_TAO_SI(30, 4, CardSuit.HONG_TAO),
    HONG_TAO_WU(31, 5, CardSuit.HONG_TAO),
    HONG_TAO_LIU(32, 6, CardSuit.HONG_TAO),
    HONG_TAO_QI(33, 7, CardSuit.HONG_TAO),
    HONG_TAO_BA(34, 8, CardSuit.HONG_TAO),
    HONG_TAO_JIU(35, 9, CardSuit.HONG_TAO),
    HONG_TAO_SHI(36, 10, CardSuit.HONG_TAO),
    HONG_TAO_J(37, 11, CardSuit.HONG_TAO),
    HONG_TAO_Q(38, 12, CardSuit.HONG_TAO),
    HONG_TAO_K(39, 13, CardSuit.HONG_TAO),

    /* 黑桃 A - K,40-52 */
    HEI_TAO_A(40, 14, CardSuit.HEI_TAO),
    HEI_TAO_ER(41, 15, CardSuit.HEI_TAO),
    HEI_TAO_SAN(42, 3, CardSuit.HEI_TAO),
    HEI_TAO_SI(43, 4, CardSuit.HEI_TAO),
    HEI_TAO_WU(44, 5, CardSuit.HEI_TAO),
    HEI_TAO_LIU(45, 6, CardSuit.HEI_TAO),
    HEI_TAO_QI(46, 7, CardSuit.HEI_TAO),
    HEI_TAO_BA(47, 8, CardSuit.HEI_TAO),
    HEI_TAO_JIU(48, 9, CardSuit.HEI_TAO),
    HEI_TAO_SHI(49, 10, CardSuit.HEI_TAO),
    HEI_TAO_J(50, 11, CardSuit.HEI_TAO),
    HEI_TAO_Q(51, 12, CardSuit.HEI_TAO),
    HEI_TAO_K(52, 13, CardSuit.HEI_TAO),

    /* 小王,大王 53,54 */
    XW(53, 16, null), DW(54, 17, null);

    public static final int A_NUM = 14;
    public static final int ER_NUM = 15;
    public static final int SAN_NUM = 3;
    public static final int J_NUM = 11;
    public static final int XW_NUM = 16;
    public static final int DW_NUM = 17;

    public final int id;
    // 3-k(3-13),A:14,2:15,小王:16,大王:17
    public final int num;
    // 牌的花色
    public final CardSuit suit;

    private Card(int id, int num, CardSuit suit) {
        this.id = id;
        this.num = num;
        this.suit = suit;
    }

    /**
     * 根据牌的id获取牌
     *
     * @param id
     * @return
     */
    public static Card getCard(int id) {
        for (Card obj : Card.values()) {
            if (obj.id == id) {
                return obj;
            }
        }
        return null;
    }

    /** 返回外部牌型 */
    public static List<Integer> getOutCard(List<Card> cards) {
        List<Integer> result = new ArrayList<>();
        if (cards == null) {
            return result;
        }
        for (Card obj : cards) {
            result.add(obj.id);
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.suit == null) {
            if (this.num == 16) {
                builder.append('小');
            } else {
                builder.append('大');
            }
        } else {
            if (this.suit == CardSuit.HEI_TAO) {
                builder.append('♠');
            } else if (this.suit == CardSuit.HONG_TAO) {
                builder.append('♥');
            } else if (this.suit == CardSuit.MEI_HUA) {
                builder.append('♣');
            } else {
                builder.append('♦');
            }
            if (this.num > 10) {
                if (this.num == 11) {
                    builder.append("J");
                } else if (this.num == 12) {
                    builder.append("Q");
                } else if (this.num == 13) {
                    builder.append("K");
                } else if (this.num == 14) {
                    builder.append("A");
                } else if (this.num == 15) {
                    builder.append("2");
                }
            } else {
                builder.append(this.num);
            }
        }
        return builder.toString();
    }
}
