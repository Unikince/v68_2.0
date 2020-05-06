package com.dmg.doudizhuserver.business.util;

import java.util.List;
import java.util.TreeSet;

import com.dmg.doudizhuserver.business.model.Card;
import com.dmg.doudizhuserver.business.model.CardsType;
import com.google.common.collect.Multiset.Entry;
import com.google.common.collect.TreeMultiset;

/**
 * 牌型获取工具类
 */
public class CardsTypeGetter {

    /**
     * 获取牌型
     */
    public static CardsType getCardsType(List<Card> cards) {
        // 出牌类型
        CardsType cardsType = null;

        if (CardsTypeGetter.isDan(cards)) { // 单牌
            cardsType = CardsType.DAN;
        } else if (CardsTypeGetter.isWangZha(cards)) { // 火箭：即双王（双花牌），什么牌型都可打，是最大的牌
            cardsType = CardsType.WANG_ZHA;
        } else if (CardsTypeGetter.isDui(cards)) { // 对子：两个
            cardsType = CardsType.DUI;
        } else if (CardsTypeGetter.isSanZhang(cards)) { // 三张牌：三张牌点相同的牌。
            cardsType = CardsType.SAN_ZHANG;
        } else if (CardsTypeGetter.isZhaDan(cards)) { // 四张牌点相同的牌（如四个8）。除火箭和比自己大的炸弹外，什么牌型都可打
            cardsType = CardsType.ZHA_DAN;
        } else if (CardsTypeGetter.isSanDaiDan(cards)) { // 三带单：三张牌 ＋ 一张单牌。例如： 888+9
            cardsType = CardsType.SAN_DAI_DAN;
        } else if (CardsTypeGetter.isSanDaiDui(cards)) { // 三带对：三张牌 ＋ 一对牌。例如： 888+99
            cardsType = CardsType.SAN_DAI_DUI;
        } else if (CardsTypeGetter.isShun(cards)) { // 单顺：五张或更多连续的单牌。例如：3+4+5+6+7+8。不包括2和大、小王。
            cardsType = CardsType.SHUN;
        } else if (CardsTypeGetter.isShuangShun(cards)) { // 双顺：三个或更多连续的对牌。例如：33+44+55。不包括2和大、小王。
            cardsType = CardsType.LIAN_DUI;
        } else if (CardsTypeGetter.isFeiJi(cards)) { // 三顺：二个或更多连续的三张牌。例如：333444、444555666777。不包括2和大、小王。
            cardsType = CardsType.FEI_JI;
        } else if (CardsTypeGetter.isFeiJiDaiDan(cards)) { // 飞机带单翅膀：三顺＋同数量的单套牌。例如：333444+69 ，333444+66也是合法的
            cardsType = CardsType.FEI_JI_DAI_DAN;
        } else if (CardsTypeGetter.isFeiJiDaiDui(cards)) { // 飞机带双翅膀：三顺＋同数量的对套牌。例如：333444555+667799
            cardsType = CardsType.FEI_JI_DAI_DUI;
        } else if (CardsTypeGetter.isSiDaiDan(cards)) { // 四张牌＋任意两套张数相同的单牌。例如：5555＋3＋8
            cardsType = CardsType.SI_DAI_DAN;
        } else if (CardsTypeGetter.isSiDaiDui(cards)) { // 四张牌＋任意两套张数相同的对牌。例如：4444＋55＋77
            cardsType = CardsType.SI_DAI_DUI;
        }

        return cardsType;
    }

    /**
     * 是否是单牌
     */
    public static boolean isDan(List<Card> cards) {
        return cards.size() == 1;
    }

    /**
     * 是否是王炸,王炸不是对子
     */
    public static boolean isWangZha(List<Card> cards) {
        return (cards.size() == 2) && ((cards.get(0).id + cards.get(1).id) == (Card.XW.id + Card.DW.id));
    }

    /**
     * 是否是对子
     */
    public static boolean isDui(List<Card> cards) {
        return (cards.size() == 2) && (cards.get(0).num == cards.get(1).num);
    }

    /**
     * 是否是炸弹
     */
    public static boolean isZhaDan(List<Card> cards) {
        return (cards.size() == 4) && (cards.get(0).num == cards.get(1).num) && (cards.get(1).num == cards.get(2).num) && (cards.get(2).num == cards.get(3).num);
    }

    /**
     * 是否是三张
     */
    public static boolean isSanZhang(List<Card> cards) {
        return (cards.size() == 3) && (cards.get(0).num == cards.get(1).num) && (cards.get(1).num == cards.get(2).num);
    }

    /**
     * 是否是三带单
     */
    public static boolean isSanDaiDan(List<Card> cards) {
        if (cards.size() == 4) {
            // 牌的数字集合
            TreeMultiset<Integer> cardNums = TreeMultiset.create();
            for (Card card : cards) {
                cardNums.add(card.num);
            }

            int firstCardCount = cardNums.firstEntry().getCount();
            return (cardNums.elementSet().size() == 2) && ((firstCardCount == 1) || (firstCardCount == 3));
        }

        return false;
    }

    /**
     * 是否三带对
     */
    public static boolean isSanDaiDui(List<Card> cards) {
        if (cards.size() == 5) {
            // 牌的数字集合
            TreeMultiset<Integer> cardNums = TreeMultiset.create();
            for (Card card : cards) {
                cardNums.add(card.num);
            }

            if (cardNums.elementSet().size() == 2) {
                int firstCardCount = cardNums.firstEntry().getCount();
                return (firstCardCount == 2) || (firstCardCount == 3);
            }
        }

        return false;
    }

    /**
     * 是否顺子
     */
    public static boolean isShun(List<Card> cards) {
        int size = cards.size();
        if ((size >= 5) && (size <= 12)) {
            TreeSet<Integer> cardNums = new TreeSet<>();
            for (Card card : cards) {
                if (card.num > Card.A_NUM) {
                    return false;
                }
                cardNums.add(card.num);
            }

            return (((cardNums.last() - cardNums.first()) + 1) == cards.size()) && (cardNums.size() == cards.size());
        }

        return false;
    }

    /**
     * 是否连对
     */
    public static boolean isShuangShun(List<Card> cards) {
        int size = cards.size();
        if (((size % 2) == 0) && (size >= 6) && (size <= 20)) {
            TreeMultiset<Integer> cardNums = TreeMultiset.create();
            for (Card card : cards) {
                if (card.num > Card.A_NUM) {
                    return false;
                }
                cardNums.add(card.num);
            }

            int firstCardNum = cardNums.firstEntry().getElement();
            for (int i = 0; i < (size / 2); i++) {
                if (cardNums.count(firstCardNum + i) != 2) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    /**
     * 是否是三顺
     */
    public static boolean isFeiJi(List<Card> cards) {
        int size = cards.size();
        // 几个连号
        int n = size / 3;
        if (((size % 3) == 0) && (n > 1) && (n < 7)) {
            TreeMultiset<Integer> cardNums = TreeMultiset.create();
            for (Card card : cards) {
                if (card.num > Card.A_NUM) {
                    return false;
                }
                cardNums.add(card.num);
            }

            Integer firstCardNum = cardNums.firstEntry().getElement();
            for (int i = 0; i < n; i++) {
                if (cardNums.count(firstCardNum + i) != 3) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    /**
     * 飞机带单翅膀：三顺＋同数量的单套牌。例如：333444+69 ，333444+66也是合法的，333444+36？？ 先判断开始的牌是否连续三顺
     * 不是的话再判断结尾的牌是否连续三顺
     */
    public static boolean isFeiJiDaiDan(List<Card> cards) {
        // 飞机数量(三顺的数量)
        int feiJiNum = cards.size() / 4;
        // 飞机带单翅膀牌数量至少是4的2倍
        if (((cards.size() % 4) == 0) && (feiJiNum > 1) && (feiJiNum < 6)) {
            TreeMultiset<Integer> cardNums = TreeMultiset.create();
            for (Card card : cards) {
                cardNums.add(card.num);
            }

            for (Integer cardNum : cardNums.elementSet()) {
                if (cardNums.count(cardNum) >= 3) {
                    boolean isFeiJiDaiDan = true;
                    for (int i = 1; i < feiJiNum; i++) {
                        if ((cardNums.count(cardNum + i) < 3) || ((cardNum + i) > Card.A_NUM)) {
                            isFeiJiDaiDan = false;
                            break;
                        }
                    }

                    if (isFeiJiDaiDan) {
                        return true;
                    } else {
                        continue;
                    }
                }
            }
        }

        return false;
    }

    /**
     * 飞机带双翅膀：三顺＋同数量的对套牌。例如：333444555+667799，333444555 + 666677？？？不合法
     */
    public static boolean isFeiJiDaiDui(List<Card> cards) {
        // 飞机数量(三顺的数量)
        int feiJiNum = cards.size() / 5;

        // 飞机带双翅膀牌数量至少是5的2倍
        if (((cards.size() % 5) == 0) && (feiJiNum > 1) && (feiJiNum < 5)) {
            TreeMultiset<Integer> cardNums = TreeMultiset.create();
            for (Card card : cards) {
                cardNums.add(card.num);
            }

            // 对子数量
            int duiNum = 0;
            // 是否是飞机
            boolean isFeiJi = false;

            for (Integer cardNum : cardNums.elementSet()) {
                if (cardNums.count(cardNum) == 3) {
                    if (isFeiJi) {
                        continue;
                    }

                    isFeiJi = true;
                    for (int i = 1; i < feiJiNum; i++) {
                        if ((cardNums.count(cardNum + i) < 3) || ((cardNum + i) > Card.A_NUM)) {
                            isFeiJi = false;
                            break;
                        }
                    }
                } else if ((cardNums.count(cardNum) % 2) == 0) {
                    duiNum += cardNums.count(cardNum) / 2;
                } else {
                    return false;
                }
            }

            return isFeiJi && (duiNum == feiJiNum);
        }

        return false;
    }

    /**
     * 四带二张单牌：张牌＋任意两套张数相同的单牌。例如：5555＋3＋8,不允许4带2个王
     */
    public static boolean isSiDaiDan(List<Card> cards) {
        if (cards.size() == 6) {
            TreeMultiset<Integer> cardNums = TreeMultiset.create();
            boolean hasXw = false;
            boolean hasDw = false;
            for (Card card : cards) {
                if (card == Card.DW) {
                    hasDw = true;
                } else if (card == Card.XW) {
                    hasXw = true;
                }
                cardNums.add(card.num);
            }

            if (hasDw && hasXw) {
                return false;
            }

            for (Entry<Integer> cardNumEtr : cardNums.entrySet()) {
                if (cardNumEtr.getCount() == 4) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 四带二对：四张牌＋任意两套张数相同的对牌。例如：4444＋55＋77,4444 + 5555或者4444+8888不合法
     */
    public static boolean isSiDaiDui(List<Card> playCards) {
        if (playCards.size() == 8) {
            TreeMultiset<Integer> cardNums = TreeMultiset.create();
            for (Card card : playCards) {
                cardNums.add(card.num);
            }

            // 排序去重以后第一张数字出现的重复次数
            int firstCardNumCount = cardNums.firstEntry().getCount();
            // 排序去重以后最后一张数字出现的重复次数
            int lastCardNumcount = cardNums.lastEntry().getCount();

            if (cardNums.entrySet().size() == 3) {
                if ((firstCardNumCount == 4) && (lastCardNumcount == 2)) {
                    return true;
                } else if ((firstCardNumCount == 2) && (lastCardNumcount == 2)) {
                    return true;
                } else if ((firstCardNumCount == 2) && (lastCardNumcount == 4)) {
                    return true;
                }
            }
        }

        return false;
    }

}
