package com.dmg.zhajinhuaserver.model.dto;

import com.dmg.zhajinhuaserver.config.Config;
import com.dmg.zhajinhuaserver.model.bean.Poker;
import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @Author liubo
 * @Description //TODO 金花牌
 * @Date 10:11 2019/9/29
 */
@Data
public class BrandDTO implements Comparable<BrandDTO>, Serializable {

    //poker牌
    private List<Poker> hand;

    //牌型
    private int combination;

    @Override
    public int compareTo(BrandDTO o) {
        int thisCombination = this.combination;
        int otherCombination = o.combination;
        List<Poker> otherHand = o.hand;
        List<Poker> thisHand = this.hand;
        if (thisCombination < otherCombination) {
            return -1;
        }
        if (thisCombination > otherCombination) {
            return 1;
        }
        if (thisCombination == otherCombination) {
            Collections.sort(otherHand);
            Collections.sort(thisHand);
            for (int i = 0; i < thisHand.size() - 1; i++) {
                int thisValue = thisHand.get(i).getValue() == 1 ? 14 : thisHand.get(i).getValue();
                int otherValue = otherHand.get(i).getValue() == 1 ? 14 : otherHand.get(i).getValue();
                if (thisValue > otherValue) {
                    return 1;
                }
                if (thisValue < otherValue) {
                    return -1;
                }
            }
        }
        return 0;
    }
}
