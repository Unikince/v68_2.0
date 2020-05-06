package com.dmg.niuniuserver.model.dto;

import com.dmg.niuniuserver.model.bean.Poker;
import com.dmg.niuniuserver.model.constants.Combination;
import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @Author liubo
 * @Description //TODO 牛牛牌
 * @Date 10:11 2019/9/29
 */
@Data
public class BrandDTO implements Comparable<BrandDTO>, Serializable {

    //poker牌
    private List<Poker> hand;

    //牌型
    private Combination combination;

    @Override
    public int compareTo(BrandDTO o) {
        Combination thisCombination = this.combination;
        Combination otherCombination = o.combination;
        List<Poker> otherHand = o.hand;
        List<Poker> thisHand = this.hand;
        if (thisCombination.getValue() < otherCombination.getValue()) {
            return -1;
        }
        if (thisCombination.getValue() > otherCombination.getValue()) {
            return 1;
        }
        if (thisCombination.getValue() == otherCombination.getValue()) {
            Collections.sort(otherHand);
            Collections.sort(thisHand);
            for (int i = 0; i < thisHand.size() - 1; i++) {
                if(thisHand.get(i).getValue() > otherHand.get(i).getValue()){
                    return 1;
                }
                if(thisHand.get(i).getValue() < otherHand.get(i).getValue()){
                    return -1;
                }
            }
        }
        return 0;
    }
}
