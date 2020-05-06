package com.dmg.bjlserver.business.model.game;

/**
 * 1-13：方块，14-26：梅花，27-39：红桃，40-52：黑桃
 */
public enum Card {
    /* 方块 A - K,1-13 */
    FANG_KUAI_A(1, 1, 1, CardSuit.FANG_KUAI),
    FANG_KUAI_ER(2, 2, 2, CardSuit.FANG_KUAI),
    FANG_KUAI_SAN(3, 3, 3, CardSuit.FANG_KUAI),
    FANG_KUAI_SI(4, 4, 4, CardSuit.FANG_KUAI),
    FANG_KUAI_WU(5, 5, 5, CardSuit.FANG_KUAI),
    FANG_KUAI_LIU(6, 6, 6, CardSuit.FANG_KUAI),
    FANG_KUAI_QI(7, 7, 7, CardSuit.FANG_KUAI),
    FANG_KUAI_BA(8, 8, 8, CardSuit.FANG_KUAI),
    FANG_KUAI_JIU(9, 9, 9, CardSuit.FANG_KUAI),
    FANG_KUAI_SHI(10, 10, 0, CardSuit.FANG_KUAI),
    FANG_KUAI_J(11, 11, 0, CardSuit.FANG_KUAI),
    FANG_KUAI_Q(12, 12, 0, CardSuit.FANG_KUAI),
    FANG_KUAI_K(13, 13, 0, CardSuit.FANG_KUAI),

    /* 梅花 A - K,14-26 */
    MEI_HUA_A(14, 1, 1, CardSuit.MEI_HUA),
    MEI_HUA_ER(15, 2, 2, CardSuit.MEI_HUA),
    MEI_HUA_SAN(16, 3, 3, CardSuit.MEI_HUA),
    MEI_HUA_SI(17, 4, 4, CardSuit.MEI_HUA),
    MEI_HUA_WU(18, 5, 5, CardSuit.MEI_HUA),
    MEI_HUA_LIU(19, 6, 6, CardSuit.MEI_HUA),
    MEI_HUA_QI(20, 7, 7, CardSuit.MEI_HUA),
    MEI_HUA_BA(21, 8, 8, CardSuit.MEI_HUA),
    MEI_HUA_JIU(22, 9, 9, CardSuit.MEI_HUA),
    MEI_HUA_SHI(23, 10, 0, CardSuit.MEI_HUA),
    MEI_HUA_J(24, 11, 0, CardSuit.MEI_HUA),
    MEI_HUA_Q(25, 12, 0, CardSuit.MEI_HUA),
    MEI_HUA_K(26, 13, 0, CardSuit.MEI_HUA),

    /* 红桃 A - K，27-39 */
    HONG_TAO_A(27, 1, 1, CardSuit.HONG_TAO),
    HONG_TAO_ER(28, 2, 2, CardSuit.HONG_TAO),
    HONG_TAO_SAN(29, 3, 3, CardSuit.HONG_TAO),
    HONG_TAO_SI(30, 4, 4, CardSuit.HONG_TAO),
    HONG_TAO_WU(31, 5, 5, CardSuit.HONG_TAO),
    HONG_TAO_LIU(32, 6, 6, CardSuit.HONG_TAO),
    HONG_TAO_QI(33, 7, 7, CardSuit.HONG_TAO),
    HONG_TAO_BA(34, 8, 8, CardSuit.HONG_TAO),
    HONG_TAO_JIU(35, 9, 9, CardSuit.HONG_TAO),
    HONG_TAO_SHI(36, 10, 0, CardSuit.HONG_TAO),
    HONG_TAO_J(37, 11, 0, CardSuit.HONG_TAO),
    HONG_TAO_Q(38, 12, 0, CardSuit.HONG_TAO),
    HONG_TAO_K(39, 13, 0, CardSuit.HONG_TAO),

    /* 黑桃 A - K,40-52 */
    HEI_TAO_A(40, 1, 1, CardSuit.HEI_TAO),
    HEI_TAO_ER(41, 2, 2, CardSuit.HEI_TAO),
    HEI_TAO_SAN(42, 3, 3, CardSuit.HEI_TAO),
    HEI_TAO_SI(43, 4, 4, CardSuit.HEI_TAO),
    HEI_TAO_WU(44, 5, 5, CardSuit.HEI_TAO),
    HEI_TAO_LIU(45, 6, 6, CardSuit.HEI_TAO),
    HEI_TAO_QI(46, 7, 7, CardSuit.HEI_TAO),
    HEI_TAO_BA(47, 8, 8, CardSuit.HEI_TAO),
    HEI_TAO_JIU(48, 9, 9, CardSuit.HEI_TAO),
    HEI_TAO_SHI(49, 10, 0, CardSuit.HEI_TAO),
    HEI_TAO_J(50, 11, 0, CardSuit.HEI_TAO),
    HEI_TAO_Q(51, 12, 0, CardSuit.HEI_TAO),
    HEI_TAO_K(52, 13, 0, CardSuit.HEI_TAO),

    ;

    /** 牌id */
    public final int id;
    /** 牌的数字(1-13) */
    public final int num;
    /** 编号(1-9) */
    public final int no;
    /** 牌的花色 */
    public final CardSuit suit;

    /**
     * 构造方法
     *
     * @param id 牌id
     * @param num 牌的数字(1-13)
     * @param no 编号(1-9)
     * @param suit 牌的花色
     */
    private Card(int id, int num, int no, CardSuit suit) {
        this.id = id;
        this.num = num;
        this.no = no;
        this.suit = suit;
    }

    /**
     * 根据牌的id获取牌
     */
    public static Card getCard(int id) {
        for (Card obj : Card.values()) {
            if (obj.id == id) {
                return obj;
            }
        }
        return null;
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
            if (this.num > 10 || this.num == 1) {
                if (this.num == 11) {
                    builder.append("J");
                } else if (this.num == 12) {
                    builder.append("Q");
                } else if (this.num == 13) {
                    builder.append("K");
                } else if (this.num == 1) {
                    builder.append("A");
                }
            } else {
                builder.append(this.num);
            }
        }
        return builder.toString();
    }

}
