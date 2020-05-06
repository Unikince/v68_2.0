package com.dmg.doudizhuserver.business.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * 斗地主手牌
 */
public class HandCards {
    // 牌
    public final Set<Card> cards = Collections.synchronizedSet(EnumSet.noneOf(Card.class));

    public HandCards(Collection<Card> cards) {
        this.cards.addAll(cards);
    }

    /**
     * 牌数字
     */
    public List<Integer> cardNums() {
        List<Integer> cardNums = new ArrayList<>();
        for (Card c : this.cards) {
            cardNums.add(c.num);
        }
        Collections.sort(cardNums);
        return cardNums;
    }

    public List<Card> getSortCards() {
        List<Card> result = new ArrayList<>();
        for (Card card : this.cards) {
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

    public List<String> printCardStr() {
        List<String> result = new ArrayList<>();
        for (Card card : this.getSortCards()) {
            result.add(card.toString());
        }
        return result;
    }

}
