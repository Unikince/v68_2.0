package com.dmg.doudizhuserver.business.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dmg.doudizhuserver.business.util.CardsTypeGetter;

/**
 * 斗地主出的牌
 */
public class PlayCards {
    // 牌(已经排好序)
    public final List<Card> cards;
    // 牌(已经排好序)
    public final List<Card> sortedCards;
    // 类型
    public final CardsType type;
    // 关键牌
    public final Card flagCard;

    public PlayCards(List<Card> cards) {
        this(cards, CardsTypeGetter.getCardsType(cards));
    }

    public PlayCards(List<Card> cards, CardsType type) {
        this.cards = cards;
        this.type = type;
        List<Card> sortedCards = null;

        Map<Integer, List<Card>> map = new HashMap<>();
        for (Card card : cards) {
            List<Card> t = map.get(card.num);
            if (t == null) {
                t = new ArrayList<>();
                map.put(card.num, t);
            }
            t.add(card);
        }
        if (type == null) {
            type = CardsType.PASS;
        }
        switch (type) {
            case SAN_DAI_DAN: {
            }
            case SAN_DAI_DUI: {

            }
            case FEI_JI_DAI_DAN: {
            }
            case FEI_JI_DAI_DUI: {
                List<Card> m = new ArrayList<>();
                List<Card> n = new ArrayList<>();
                for (List<Card> c : map.values()) {
                    if (c.size() == 3) {
                        m.addAll(c);
                    } else {
                        n.addAll(c);
                    }
                }

                m = this.getSortCards(m);
                n = this.getSortCards(n);
                sortedCards = new ArrayList<>();
                sortedCards.addAll(m);
                sortedCards.addAll(n);
                break;
            }
            case SI_DAI_DAN: {
            }
            case SI_DAI_DUI: {
                List<Card> m = new ArrayList<>();
                List<Card> n = new ArrayList<>();
                for (List<Card> c : map.values()) {
                    if (c.size() == 4) {
                        m.addAll(c);
                    } else {
                        n.addAll(c);
                    }
                }
                m = this.getSortCards(m);
                n = this.getSortCards(n);
                sortedCards = new ArrayList<>();
                sortedCards.addAll(m);
                sortedCards.addAll(n);
                break;
            }
            default: {
                sortedCards = this.getSortCards(cards);
                break;
            }
        }
        this.sortedCards = sortedCards;
        this.flagCard = sortedCards.get(0);
    }

    private List<Card> getSortCards(List<Card> cards) {
        List<Card> result = new ArrayList<>();
        for (Card card : cards) {
            result.add(card);
        }
        result.sort(new Comparator<Card>() {
            @Override
            public int compare(Card c1, Card c2) {
                if (c1.num != c2.num) {
                    return c1.num - c2.num;
                } else {
                    if (c1.suit == null) {
                        return 1;
                    } else if (c2.suit == null) {
                        return -1;
                    } else {
                        return c1.suit.compareTo(c2.suit);
                    }

                }
            }
        });
        Collections.reverse(result);
        return result;
    }

    @Override
    public String toString() {
        return this.sortedCards.toString();
    }

}
