package com.dmg.bjlserver.business.service;

import com.dmg.bjlserver.business.model.game.Card;

public class CardUtil {

    /**
     * 是否停止发牌
     *
     * @param first
     * @param second
     * @return
     */
    public static boolean isDone(Card first, Card second) {
        int total = (first.no + second.no) % 10;
        return total >= 8;
    }

    /**
     * 要不要补牌
     *
     * @param first
     * @param second
     * @return
     */
    public static boolean needAddCard(Card first, Card second) {
        int total = (first.no + second.no) % 10;
        if (total >= 0 && total <= 5) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 牌值
     *
     * @param first
     * @param second
     * @return
     */
    public static int total(Card first, Card second) {
        int total = first.no + second.no;
        return total % 10;
    }

    /**
     * 牌值
     *
     * @param first
     * @param second
     * @param third
     * @return
     */
    public static int total(Card first, Card second, Card third) {

        if (third == null) {
            return CardUtil.total(first, second);
        }

        int total = first.no + second.no + third.no;
        return total % 10;
    }

    /**
     * 庄家可否补牌
     *
     * @param first
     * @param second
     * @return
     */
    public static boolean canBankerAddCard(Card first, Card second, Card third) {
        int t = CardUtil.total(first, second);

        switch (t) {
            case 0:
            case 1:
            case 2:
                return true;
            case 3:
                return third == null || (third.no != 8);
            case 4:
                return third == null || (third.no != 0) && (third.no != 1) && (third.no != 8) && (third.no != 9);
            case 5:
                return third == null || (third.no != 0) && (third.no != 1) && (third.no != 8) && (third.no != 9) && (third.no != 2)
                        && (third.no != 3);
            case 6:
                return third == null || ((third.no == 6) || (third.no == 7));
            default:
                return false;
        }

    }

    /**
     * 是不是对子
     *
     * @param first
     * @param second
     * @return
     */
    public static boolean isPair(Card first, Card second) {
        return first.num == second.num;
    }

}
