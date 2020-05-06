package com.dmg.doudizhuserver.business.util;

import java.util.List;

import com.dmg.doudizhuserver.business.model.Card;
import com.dmg.doudizhuserver.business.model.CardsType;
import com.dmg.doudizhuserver.business.model.PlayCards;

/**
 * CardsType比较工具类
 */
public class CardsTypeComparator {

    private CardsTypeComparator() {
    }

    /**
     * 比较我的牌和另一个人牌的大小,如果不是王炸或者炸弹那么myCards数量必须和上一家preCards数量一致
     *
     * @param myPlayCards
     * @param otherPlayCards
     * @return -2:牌数量或牌型不匹配,-1：我小，0：相等，1：我大
     */
    public static int compare(PlayCards myPlayCards, PlayCards otherPlayCards) {
        List<Card> myCards = myPlayCards.sortedCards;
        CardsType myCardsType = myPlayCards.type;
        List<Card> otherCards = otherPlayCards.sortedCards;
        CardsType otherCardsType = otherPlayCards.type;

        /* 集中判断特殊情况 我是王炸 上家是王炸 上家不是炸弹，我是炸弹 剩下的牌型和数量必须一致 */
        // 我是王炸
        if (myCardsType == CardsType.WANG_ZHA) {
            return 1;
        }

        // 上家是王炸
        if (otherCardsType == CardsType.WANG_ZHA) {
            return -1;
        }

        // 上家不是炸弹，我是炸弹
        if ((otherCardsType != CardsType.ZHA_DAN) && (myCardsType == CardsType.ZHA_DAN)) {
            return 1;
        }

        // 牌型和数量是否匹配
        if ((myCards.size() != otherCards.size()) || (myCardsType != otherCardsType)) {
            return -2;
        }

        /* 判断同牌型的大小 */
        // 上家出DAN,DUI_ZI,SAN_BU_DAI,SHUN_ZI,SHUANG_SHUN,SAN_SHUN,ZHA_DAN，只需要比较第1张牌
        if ((otherCardsType == myCardsType) && ((otherCardsType == CardsType.DAN) || (otherCardsType == CardsType.DUI) || (otherCardsType == CardsType.SAN_ZHANG) || (otherCardsType == CardsType.SHUN) || (otherCardsType == CardsType.LIAN_DUI) || (otherCardsType == CardsType.FEI_JI) || (otherCardsType == CardsType.ZHA_DAN))) {
            int myCardNum = myCards.get(0).num;
            int otherCardNum = otherCards.get(0).num;

            if (myCardNum > otherCardNum) {
                return 1;
            } else if (myCardNum == otherCardNum) {
                return 0;
            } else {
                return -1;
            }
        }

        // 上家出SAN_DAI_DAN,SAN_DAI_DUI,SI_DAI_DAN，只需要比较第3张牌
        if ((otherCardsType == CardsType.SAN_DAI_DAN) || (otherCardsType == CardsType.SAN_DAI_DUI) || (otherCardsType == CardsType.SI_DAI_DAN)) {
            int myCardNum = myCards.get(2).num;
            int otherCardNum = otherCards.get(2).num;

            return myCardNum > otherCardNum ? 1 : -1;
        }

        // 上家出SI_DAI_DUI,前面三张如果相等则取第一张来比较，否则取第5张
        if (otherCardsType == CardsType.SI_DAI_DUI) {
            int myCardNum;
            int otherCardNum;

            if ((myCards.get(0).num == myCards.get(1).num) && (otherCards.get(1).num == otherCards.get(2).num)) {
                myCardNum = myCards.get(0).num;
            } else {
                myCardNum = myCards.get(4).num;
            }

            if ((otherCards.get(0).num == otherCards.get(1).num) && (otherCards.get(1).num == otherCards.get(2).num)) {
                otherCardNum = otherCards.get(0).num;
            } else {
                otherCardNum = otherCards.get(4).num;
            }

            return myCardNum > otherCardNum ? 1 : -1;
        }

        /* 上家出FEI_JI_DAI_DAN 8张时比较第3张 12张时比较第4张 16张时比较第5张 20张时比较第6张 */
        if (otherCardsType == CardsType.FEI_JI_DAI_DAN) {
            int myCardNum = myCards.get(myCards.size() / 4).num;
            int otherCardNum = otherCards.get(otherCards.size() / 4).num;

            return myCardNum > otherCardNum ? 1 : -1;
        }

        /* 上家出FEI_JI_DAI_DUI 10张时比较第5张 15张时比较第7张 20张时比较第9张 */
        if (otherCardsType == CardsType.FEI_JI_DAI_DUI) {
            int myCardNum = myCards.get((myCards.size() / 5) * 2).num;
            int otherCardNum = otherCards.get((otherCards.size() / 5) * 2).num;

            return myCardNum > otherCardNum ? 1 : -1;
        }

        return 0;
    }
}
