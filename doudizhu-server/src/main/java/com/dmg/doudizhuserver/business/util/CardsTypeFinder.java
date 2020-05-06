package com.dmg.doudizhuserver.business.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableSet;
import java.util.TreeMap;

import com.dmg.doudizhuserver.business.model.Card;
import com.dmg.doudizhuserver.business.model.CardsType;
import com.dmg.doudizhuserver.business.model.HandCards;
import com.dmg.doudizhuserver.business.model.PlayCards;
import com.google.common.collect.TreeMultimap;

/**
 * 牌查找工具类
 */
public class CardsTypeFinder {

    private CardsTypeFinder() {
    }

    /**
     * 查找手上最小的牌 规则：最小的牌是单牌出单牌,对牌出对牌(不包括王炸),三牌出三牌,没有继续找,都没有,从炸弹找,最后出王炸
     *
     * @param fromCards
     * @return
     */
    public static List<Card> findMinCards(Collection<Card> fromCards) {
        if ((fromCards == null) || (fromCards.size() == 0)) {
            throw new IllegalArgumentException("fromCards不能为null且数量大于0");
        }

        // 有大王
        boolean hasDaWang = false;
        // 有小王
        boolean hasXiaoWang = false;
        // 查询的牌的数字map
        TreeMultimap<Integer, Card> fromCardNums = TreeMultimap.create();
        // 四个一样的牌(炸弹)
        TreeMultimap<Integer, Card> zhaDans = TreeMultimap.create();

        for (Card fromCard : fromCards) {
            if (fromCard == Card.DW) {
                hasDaWang = true;
            } else if (fromCard == Card.XW) {
                hasXiaoWang = true;
            }
            fromCardNums.put(fromCard.num, fromCard);
        }

        for (Integer cardNum : fromCardNums.keys().elementSet()) {
            NavigableSet<Card> cards = fromCardNums.get(cardNum);

            if (cards.size() == 1) {
                Card card = cards.first();
                if (((card == Card.DW) && hasXiaoWang) || ((card == Card.XW) && hasDaWang)) {// 有大小王时不单出
                    continue;
                } else {
                    return new ArrayList<>(cards);
                }
            } else if (cards.size() == 2) {
                return new ArrayList<>(cards);
            } else if (cards.size() == 3) {
                if (fromCards.size() == 3) {// 最后剩三张,可以全部出
                    return new ArrayList<>(cards);
                } else {
                    return Arrays.asList(cards.first(), cards.last());
                }
            } else if (cards.size() == 4) {
                zhaDans.putAll(cardNum, cards);
            }
        }

        // 从炸弹中找
        if (zhaDans.size() > 0) {
            // 最小的炸弹
            return new ArrayList<>(zhaDans.get(zhaDans.keySet().first()));
        }

        // 有王炸
        if (hasXiaoWang && hasDaWang) {
            return Arrays.asList(Card.DW, Card.XW);
        }

        return null;
    }

    /**
     * 从fromCards找出比comparedCards更大的牌
     *
     * @param handCards 必须将牌按照从小到大排列
     * @param playCards 找不到更大的牌
     * @return
     */
    public static PlayCards findBiggerCards(HandCards handCards, PlayCards playCards) {
        if (handCards.cards.size() == 0) {
            return null;
        }
        // 有大王
        boolean hasDaWang = false;
        // 有小王
        boolean hasXiaoWang = false;

        // 查询的牌的数字map
        TreeMultimap<Integer, Card> fromCardNums = TreeMultimap.create();
        for (Card fromCard : handCards.cards) {
            if (fromCard == Card.DW) {
                hasDaWang = true;
            } else if (fromCard == Card.XW) {
                hasXiaoWang = true;
            }
            fromCardNums.put(fromCard.num, fromCard);
        }

        // 找出更大的牌,comparedCardsType:1.王炸,2.炸弹,在炸弹中找,3.其他牌型
        List<Card> biggerCards = null;

        if (playCards.type == CardsType.WANG_ZHA) {
            return null;
        } else if (playCards.type == CardsType.ZHA_DAN) {
            biggerCards = CardsTypeFinder.findBiggerZhaDan(playCards.sortedCards.get(0), fromCardNums);
        } else {// 剩下的牌型(SHUN_ZI,DAN,DUI_ZI,SAN_BU_DAI,SAN_DAI_DAN,SAN_DAI_DUI,SHUANG_SHUN,SAN_SHUN,FEI_JI_DAI_DAN,FEI_JI_DAI_DUI,SI_DAI_DAN,SI_DAI_DUI)
            // 单牌
            TreeMap<Integer, Card> danPais = new TreeMap<>();
            // 对牌
            TreeMultimap<Integer, Card> duiPais = TreeMultimap.create();
            // 三个一样的牌
            TreeMultimap<Integer, Card> sanPais = TreeMultimap.create();
            // 四个一样的牌(炸弹)
            TreeMultimap<Integer, Card> zhaDans = TreeMultimap.create();

            for (Integer cardNum : fromCardNums.keys().elementSet()) {
                NavigableSet<Card> cards = fromCardNums.get(cardNum);
                if (cards.size() == 1) {
                    danPais.put(cardNum, cards.first());
                    continue;
                }
                if ((cards.size() == 2) && (cardNum != Card.DW.num)) {
                    duiPais.putAll(cardNum, cards);
                    continue;
                }
                if (cards.size() == 3) {
                    sanPais.putAll(cardNum, cards);
                    continue;
                }
                if (cards.size() == 4) {
                    zhaDans.putAll(cardNum, cards);
                    continue;
                }
            }

            switch (playCards.type) {
                case SHUN:
                    biggerCards = CardsTypeFinder.findBiggerShunZi(playCards.sortedCards.get(0), playCards.sortedCards.size(), fromCardNums);
                    break;
                case DAN:
                    biggerCards = CardsTypeFinder.findBiggerDanPai(playCards.sortedCards.get(0), danPais, duiPais, sanPais);
                    break;
                case DUI:
                    biggerCards = CardsTypeFinder.findBiggerDuizi(playCards.sortedCards.get(0), duiPais, sanPais);
                    break;
                case SAN_ZHANG:
                    biggerCards = CardsTypeFinder.findBiggerSanZhang(playCards.sortedCards.get(0), sanPais);
                    break;
                case SAN_DAI_DAN:
                    List<Card> biggerSanDaiDanCards = CardsTypeFinder.findBiggerSanZhang(playCards.sortedCards.get(2), sanPais);
                    if (biggerSanDaiDanCards != null) {
                        sanPais.removeAll(biggerSanDaiDanCards.get(0).num);
                        List<Card> minDans = CardsTypeFinder.findMinDanPai(1, danPais, duiPais, sanPais);
                        if (minDans != null) {
                            biggerSanDaiDanCards.addAll(minDans);
                            biggerCards = biggerSanDaiDanCards;
                        }
                    }
                    break;
                case SAN_DAI_DUI:
                    List<Card> biggerSanDaiDuiCards = CardsTypeFinder.findBiggerSanZhang(playCards.sortedCards.get(2), sanPais);
                    if (biggerSanDaiDuiCards != null) {
                        sanPais.removeAll(biggerSanDaiDuiCards.get(0).num);
                        List<Card> minDuis = CardsTypeFinder.findMinDuiPai(1, duiPais, sanPais);
                        if (minDuis != null) {
                            biggerSanDaiDuiCards.addAll(minDuis);
                            biggerCards = biggerSanDaiDuiCards;
                        }
                    }
                    break;
                case LIAN_DUI:
                    biggerCards = CardsTypeFinder.findBiggerShuangShun(playCards.sortedCards.get(0), playCards.sortedCards.size() / 2, duiPais, sanPais);
                    break;
                case FEI_JI:
                    biggerCards = CardsTypeFinder.findBiggerSanShun(playCards.sortedCards.get(0), playCards.sortedCards.size() / 3, sanPais);
                    break;
                case FEI_JI_DAI_DAN:
                    List<Card> biggerFeiJiDaiDanCards = CardsTypeFinder.findBiggerSanShun(playCards.sortedCards.get(playCards.sortedCards.size() / 4), playCards.sortedCards.size() / 4, sanPais);
                    if (biggerFeiJiDaiDanCards != null) {
                        // 移除已经找到的三牌
                        for (Card biggerFeiJiDaiDanCard : biggerFeiJiDaiDanCards) {
                            sanPais.removeAll(biggerFeiJiDaiDanCard.num);
                        }

                        List<Card> minDanPais = CardsTypeFinder.findMinDanPai(playCards.sortedCards.size() / 4, danPais, duiPais, sanPais);

                        if (minDanPais != null) {
                            biggerFeiJiDaiDanCards.addAll(minDanPais);
                            biggerCards = biggerFeiJiDaiDanCards;
                        }
                    }
                    break;
                case FEI_JI_DAI_DUI:
                    List<Card> biggerFeiJiDaiDuiCards = CardsTypeFinder.findBiggerSanShun(playCards.sortedCards.get((playCards.sortedCards.size() / 5) * 2), playCards.sortedCards.size() / 5, sanPais);
                    if (biggerFeiJiDaiDuiCards != null) {
                        // 移除已经找到的三牌
                        for (Card biggerFeiJiDaiDuiCard : biggerFeiJiDaiDuiCards) {
                            sanPais.removeAll(biggerFeiJiDaiDuiCard.num);
                        }

                        List<Card> minDuiPais = CardsTypeFinder.findMinDuiPai(playCards.sortedCards.size() / 5, duiPais, sanPais);

                        if (minDuiPais != null) {
                            biggerFeiJiDaiDuiCards.addAll(minDuiPais);
                            biggerCards = biggerFeiJiDaiDuiCards;
                        }
                    }
                    break;
                case SI_DAI_DAN:
                    List<Card> biggerSiDaiDanCards = CardsTypeFinder.findBiggerZhaDan(playCards.sortedCards.get(2), zhaDans);
                    if (biggerSiDaiDanCards != null) {
                        zhaDans.removeAll(biggerSiDaiDanCards.get(0).num);
                        List<Card> minDanPais = CardsTypeFinder.findMinDanPai(2, danPais, duiPais, sanPais);

                        if (minDanPais != null) {
                            biggerSiDaiDanCards.addAll(minDanPais);
                            biggerCards = biggerSiDaiDanCards;
                        }
                    }
                    break;
                case SI_DAI_DUI:
                    Card comparedSiDaiDuicard;
                    if ((playCards.sortedCards.get(0).num == playCards.sortedCards.get(1).num) && (playCards.sortedCards.get(1).num == playCards.sortedCards.get(2).num)) {
                        comparedSiDaiDuicard = playCards.sortedCards.get(0);
                    } else {
                        comparedSiDaiDuicard = playCards.sortedCards.get(4);
                    }

                    List<Card> biggerSiDaiDuiCards = CardsTypeFinder.findBiggerZhaDan(comparedSiDaiDuicard, zhaDans);
                    if (biggerSiDaiDuiCards != null) {
                        zhaDans.removeAll(biggerSiDaiDuiCards.get(0).num);
                        List<Card> minDanDuis = CardsTypeFinder.findMinDuiPai(2, duiPais, sanPais);

                        if (minDanDuis != null) {
                            biggerSiDaiDuiCards.addAll(minDanDuis);
                            biggerCards = biggerSiDaiDuiCards;
                        }
                    }
                    break;
                default:
                    break;
            }

            // 查找是否有炸弹
            if ((biggerCards == null) && (zhaDans.size() > 0)) {
                biggerCards = new ArrayList<>(zhaDans.asMap().firstEntry().getValue());
            }
        }

        // 最后查找是否有王炸
        if ((biggerCards == null) && hasDaWang && hasXiaoWang) {
            biggerCards = Arrays.asList(Card.DW, Card.XW);
        }

        return biggerCards == null ? null : new PlayCards(biggerCards);
    }

    /**
     * 查找比comparedCard更大的单牌,查找顺序：单牌集合,对牌集合,三张牌的集合,必须找出足够num的牌,找不到则为空
     *
     * @param comparedCard 被比较的牌
     * @param danPais 单牌集合
     * @param duiPais 对牌集合
     * @param sanPais 三张牌的集合
     * @return
     */
    private static List<Card> findBiggerDanPai(Card comparedCard, TreeMap<Integer, Card> danPais, TreeMultimap<Integer, Card> duiPais, TreeMultimap<Integer, Card> sanPais) {
        // 从单牌中查找更大的单牌
        for (Entry<Integer, Card> danPaiEtr : danPais.entrySet()) {
            if (danPaiEtr.getKey() > comparedCard.num) {
                return Arrays.asList(danPaiEtr.getValue());
            }
        }

        // 从对牌中查找更大的单牌
        for (Entry<Integer, Card> duiPaiEtr : duiPais.entries()) {
            if (duiPaiEtr.getKey() > comparedCard.num) {
                return Arrays.asList(duiPaiEtr.getValue());
            }
        }

        // 从三张牌的集合中查找更大的单牌
        for (Entry<Integer, Card> sanPaiEtr : sanPais.entries()) {
            if (sanPaiEtr.getKey() > comparedCard.num) {
                return Arrays.asList(sanPaiEtr.getValue());
            }
        }

        return null;
    }

    /**
     * 查找比comparedCard更大的对牌,查找顺序：对牌集合,三张牌的集合
     *
     * @param comparedCard
     * @param duiPais 对牌集合
     * @param sanPais 三张牌的集合
     * @return
     */
    private static List<Card> findBiggerDuizi(Card comparedCard, TreeMultimap<Integer, Card> duiPais, TreeMultimap<Integer, Card> sanPais) {
        // 从对牌中查找更大的对子
        for (Integer cardNum : duiPais.keys().elementSet()) {
            if (cardNum > comparedCard.num) {
                return new ArrayList<>(duiPais.get(cardNum));
            }
        }

        // 从三张牌的集合中查找更大的对子
        for (Integer cardNum : sanPais.keys().elementSet()) {
            if (cardNum > comparedCard.num) {
                NavigableSet<Card> sanPai = sanPais.get(cardNum);
                Card last = sanPai.last();

                return Arrays.asList(sanPai.lower(last), last);
            }
        }

        return null;
    }

    /**
     * 查找比comparedCard更大的三张一样的牌,查找顺序：三张牌的集合
     *
     * @param comparedCard
     * @param sanPais 三张牌的集合
     * @return
     */
    private static List<Card> findBiggerSanZhang(Card comparedCard, TreeMultimap<Integer, Card> sanPais) {
        // 从三张牌的集合中查找更大的三张
        for (Integer cardNum : sanPais.keys().elementSet()) {
            if (cardNum > comparedCard.num) {

                return new ArrayList<>(sanPais.get(cardNum));
            }
        }

        return null;
    }

    /**
     * 查找最小的单牌
     *
     * @param num 最小的单牌数量
     * @param danPais
     * @param duiPais
     * @param sanPais
     * @return
     */
    private static List<Card> findMinDanPai(int num, TreeMap<Integer, Card> danPais, TreeMultimap<Integer, Card> duiPais, TreeMultimap<Integer, Card> sanPais) {
        List<Card> minDanPais = new ArrayList<>();
        for (Entry<Integer, Card> entry : danPais.entrySet()) {
            if (num > 0) {
                Card card = entry.getValue();
                minDanPais.add(card);
                num--;
            } else {
                return minDanPais;
            }
        }

        for (Entry<Integer, Card> cardEtr : duiPais.entries()) {
            if (num > 0) {
                minDanPais.add(cardEtr.getValue());
                num--;
            } else {
                return minDanPais;
            }
        }

        for (Entry<Integer, Card> cardEtr : sanPais.entries()) {
            if (num > 0) {
                minDanPais.add(cardEtr.getValue());
                num--;
            } else {
                return minDanPais;
            }
        }

        return num == minDanPais.size() ? minDanPais : null;
    }

    /**
     * 查找最小的对牌
     *
     * @param num 对牌的对数
     * @param duiPais
     * @param sanPais
     * @return
     */
    private static List<Card> findMinDuiPai(int num, TreeMultimap<Integer, Card> duiPais, TreeMultimap<Integer, Card> sanPais) {
        List<Card> minDuiPais = new ArrayList<>();
        // 对牌查找
        for (Integer cardNum : duiPais.keys().elementSet()) {
            if (num > 0) {
                minDuiPais.addAll(duiPais.get(cardNum));
                num--;
            } else {
                return minDuiPais;
            }
        }

        // 从三牌中查找
        for (Integer cardNum : sanPais.keys().elementSet()) {
            if (num > 0) {
                NavigableSet<Card> fistSanPai = sanPais.get(cardNum);
                minDuiPais.add(fistSanPai.first());
                minDuiPais.add(fistSanPai.last());
                num--;
            } else {
                return minDuiPais;
            }
        }

        if (num == 0) {
            return minDuiPais;
        } else {
            return null;
        }

    }

    /**
     * 查找比comparedCard更大的双顺
     *
     * @param comparedCard
     * @param num 双顺的对子数量
     * @param duiPais
     * @param sanPais
     * @return
     */
    private static List<Card> findBiggerShuangShun(Card comparedCard, int num, TreeMultimap<Integer, Card> duiPais, TreeMultimap<Integer, Card> sanPais) {

        for (int startNum = comparedCard.num; startNum <= (Card.FANG_KUAI_A.num - num); startNum++) {
            // 更大的对牌集合
            List<Card> biggerCards = new ArrayList<>();
            // 更大的对牌数
            int biggerCardsCount = num * 2;

            for (int i = 0; i < num; i++) {
                int biggerCardNum = startNum + i + 1;

                /* 先从对牌中找,再从三牌中找 */
                NavigableSet<Card> biggerNumCards = duiPais.get(biggerCardNum);
                if (biggerNumCards.size() == 0) {
                    biggerNumCards = sanPais.get(biggerCardNum);
                }

                if ((biggerCardNum < Card.FANG_KUAI_ER.num) && (biggerNumCards.size() > 0) && (biggerCardsCount-- > 0)) {
                    biggerCards.add(biggerNumCards.first());
                    biggerCards.add(biggerNumCards.last());
                } else {
                    break;
                }
            }

            if (biggerCards.size() == (num * 2)) {
                return biggerCards;
            }
        }

        return null;
    }

    /**
     * 查找比comparedCards更大的三顺
     *
     * @param minCard 三顺中最小的牌的数字
     * @param num 有几个三顺
     * @param sanPais
     * @return
     */
    private static List<Card> findBiggerSanShun(Card minCard, int num, TreeMultimap<Integer, Card> sanPais) {

        for (int startNum = minCard.num; startNum <= (Card.FANG_KUAI_A.num - num); startNum++) {
            // 更大的三顺牌
            List<Card> biggerCards = new ArrayList<>();
            // 更大的三顺牌数量
            int biggerCardsCount = num * 3;

            for (int i = 0; i < num; i++) {
                int biggerCardNum = startNum + i + 1;
                NavigableSet<Card> biggerNumCards = sanPais.get(biggerCardNum);

                if ((biggerCardNum < Card.FANG_KUAI_ER.num) && (biggerNumCards.size() > 0) && (biggerCardsCount-- > 0)) {
                    biggerCards.addAll(biggerNumCards);
                } else {
                    break;
                }
            }

            if (biggerCards.size() == (num * 3)) {
                return biggerCards;
            }
        }

        return null;
    }

    /**
     * 从fromCardNums找出比comparedCard大的顺子
     *
     * @param comparedCard
     * @param num 顺子数量
     * @param fromCardNums
     * @return
     */
    private static List<Card> findBiggerShunZi(Card comparedCard, int num, TreeMultimap<Integer, Card> fromCardNums) {

        for (int startNum = comparedCard.num; startNum <= (Card.FANG_KUAI_A.num - num); startNum++) {
            // 更大的顺子牌
            List<Card> biggerCards = new ArrayList<>();
            // 更大的顺子牌数量
            int biggerCardsCount = num;

            for (int i = 0; i < num; i++) {
                int biggerCardNum = startNum + i + 1;
                NavigableSet<Card> biggerNumCards = fromCardNums.get(biggerCardNum);

                if ((biggerCardNum < Card.FANG_KUAI_ER.num) && (biggerNumCards.size() > 0) && (biggerCardsCount-- > 0)) {
                    biggerCards.add(biggerNumCards.first());
                } else {
                    break;
                }
            }

            if (biggerCards.size() == num) {
                return biggerCards;
            }
        }

        return null;
    }

    /**
     * 从fromCardNums找出比comparedCard更大的炸弹
     *
     * @param comparedCard
     * @param fromCardNums
     * @return
     */
    private static List<Card> findBiggerZhaDan(Card comparedCard, TreeMultimap<Integer, Card> fromCardNums) {
        for (Integer fromCardNum : fromCardNums.keys()) {
            NavigableSet<Card> biggerCards = fromCardNums.get(fromCardNum);
            if ((biggerCards.size() == 4) && (fromCardNum > comparedCard.num)) {
                return new ArrayList<>(biggerCards);
            }
        }

        return null;
    }
}
