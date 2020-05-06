package com.dmg.bairenniuniuserver.service.logic.impl;

import com.dmg.bairenniuniuserver.common.model.Poker;
import com.dmg.bairenniuniuserver.model.constants.Combination;
import com.dmg.bairenniuniuserver.service.logic.PokerService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/30 16:37
 * @Version V1.0
 **/
@Service
public class PokerServiceImpl implements PokerService {

    @Override
    public int getPokerType(List<Poker> pokerList) {
        // 首先获取特殊牌型
        Combination combination = this.getSpecialCardType(pokerList);
        if (combination != null) {
            return combination.getValue();
        }
        List<Poker> copyPokerList = new ArrayList<>();
        for (Poker poker : pokerList) {
            Poker copyPoker = new Poker();
            copyPoker.setType(poker.getType());
            copyPoker.setValue(poker.getValue());
            copyPokerList.add(copyPoker);
        }
        return this.getOrdinaryCattle(copyPokerList);
    }


    private Combination getSpecialCardType(List<Poker> pokerList) {
        if (getWuXiaoCattle(pokerList) != Combination.UNDEFINE) {
            return Combination.WU_XIAO_CATTLE;
        }
        if (getBombCattle(pokerList) != Combination.UNDEFINE) {
            return Combination.BOMB_CATTLE;
        }
        if (getWuHuaCattle(pokerList) != Combination.UNDEFINE) {
            return Combination.WU_HUA_CATTLE;
        }
        return null;
    }

    /**
     * @description: 获取五小牛 规则:五张牌全小于5 且相加之和小于等于10
     * @param pokerList
     * @return com.dmg.bairenniuniuserver.model.constants.Combination
     * @author mice
     * @date 2019/7/30
    */
    private Combination getWuXiaoCattle(List<Poker> pokerList) {
        if (pokerList.get(4).getValue()>=5){
            return Combination.UNDEFINE;
        }
        int totalValue = pokerList.stream().map(Poker::getValue).reduce(0,Integer::sum);
        if (totalValue<=10){
            return Combination.WU_XIAO_CATTLE;
        }
        return Combination.UNDEFINE;
    }

   /**
    * @description: 获取炸弹牛 规则:五张牌中 有4张牌值相同的
    * @param pokerList
    * @return com.dmg.bairenniuniuserver.model.constants.Combination
    * @author mice
    * @date 2019/7/30
   */
    private Combination getBombCattle(List<Poker> pokerList) {
        //获取相同牌值数量的牌
        Map<Integer,List<Poker>> listMap = pokerList.stream().collect(Collectors.groupingBy(Poker::getValue));
        for (List<Poker> pokers : listMap.values()){
            if (pokers.size()==4){
                return Combination.BOMB_CATTLE;
            }
        }
        return Combination.UNDEFINE;
    }

    /**
     * @description: 五花牛 规则:5张牌均为花牌 J Q K
     * @param pokerList
     * @return com.dmg.bairenniuniuserver.model.constants.Combination
     * @author mice
     * @date 2019/7/30
    */
    private Combination getWuHuaCattle(List<Poker> pokerList) {
        int count = 0;
        for (Poker poker : pokerList) {
            if (poker.getValue() > 10) {
                count++;
            }
        }
        if (count == 5) {
            return Combination.WU_HUA_CATTLE;
        }
        return Combination.UNDEFINE;
    }

    /**
     * 计算普通牛牛牌型
     *
     * @return int
     * @author CharlesLee
     * @date 2018/4/13 0013 09:56
     */
    private int getOrdinaryCattle(List<Poker> pokerList) {
        List<Poker> copyPokers = new ArrayList<>();
        copyPokers.addAll(pokerList);
        int pokeListSize = pokerList.size();
        for (int i = 0; i < pokeListSize - 2; i++) {
            for (int j = i + 1; j < pokeListSize - 1; j++) {
                for (int k = j + 1; k < pokeListSize; k++) {
                    Poker pokerI = pokerList.get(i);
                    Poker pokerJ = pokerList.get(j);
                    Poker pokerK = pokerList.get(k);
                    // 如果没有特殊牌型,将大于10的牌转换为10进行计算
                    int valueI = pokerI.getValue() > 10 ? 10 : pokerI.getValue();
                    int valueJ = pokerJ.getValue() > 10 ? 10 : pokerJ.getValue();
                    int valueK = pokerK.getValue() > 10 ? 10 : pokerK.getValue();
                    // 该牌型有牛
                    if ((valueI + valueJ + valueK) % 10 == 0) {
                        copyPokers.remove(pokerI);
                        copyPokers.remove(pokerK);
                        copyPokers.remove(pokerJ);
                        int count = 0;
                        for (Poker poker : copyPokers) {
                            count += poker.getValue() > 10 ? 10 : poker.getValue();
                        }
                        count = count % 10;
                        if (count==0){
                            return Combination.CATTLE_CATTLE.getValue();
                        }
                        return count;
                    }
                }
            }
        }
        return Combination.HIGH_CARD.getValue();
    }
}