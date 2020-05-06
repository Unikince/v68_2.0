package com.dmg.bairenzhajinhuaserver.model;

import com.dmg.bairenzhajinhuaserver.common.model.Poker;
import com.dmg.bairenzhajinhuaserver.model.constants.Combination;
import lombok.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 牌桌
 * @Author mice
 * @Date 2019/7/31 10:02
 * @Version V1.0
 **/
@Data
public class Table {
    /**
     *  牌位 1号位为庄家位
     */
    private String tableIndex;
    /**
     *  手牌
     */
    private List<Poker> pokerList = new ArrayList<>();
    /**
     *  牌型 参看枚举:Combination
     */
    private int cardType;
    /**
     *  下注列表
     */
    private List<BigDecimal> betChipList = new ArrayList<>();
    /**
     *  总下注
     */
    private BigDecimal betChipTotal = new BigDecimal(0);
    /**
     *  机器人总下注
     */
    private BigDecimal robotBetChipTotal = new BigDecimal(0);
    /**
     *  玩家总下注
     */
    private BigDecimal playerBetChipTotal = new BigDecimal(0);
    
    public void clear() {
    	betChipList.clear();
    	pokerList.clear();
    	cardType = 0;
    	betChipTotal = new BigDecimal(0);
    	robotBetChipTotal = new BigDecimal(0);
    	playerBetChipTotal = new BigDecimal(0);
    }
    
    /**
     * 获取牌型
     * @return
     */
    public int getPokerType() {
        if (pokerList.size() != 3) {
           return Combination.NONE.getValue();
        }
        upperArray();
        cardType = Poker.getCartType(pokerList);
        return cardType;
    }


    /**
     * 将出的牌从小到大排序
     *
     * @return
     */
    public void upperArray() {
        int n = pokerList.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1; j++) {
                if (pokerList.get(j).getValue() > pokerList.get(j + 1).getValue()) {
                    int value = pokerList.get(j).getValue();
                    int type = pokerList.get(j).getType();
                    pokerList.get(j).setValue(pokerList.get(j + 1).getValue());
                    pokerList.get(j).setType(pokerList.get(j + 1).getType());
                    pokerList.get(j + 1).setValue(value);
                    pokerList.get(j + 1).setType(type);
                }
            }
        }
    }
}