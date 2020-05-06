package com.dmg.bairenlonghu.service.logic;

import com.dmg.bairenlonghu.common.model.Poker;

import java.util.List;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/30 16:37
 * @Version V1.0
 **/
public interface PokerService {

    /**
     * @description: 获取牌型
     * @param pokerList
     * @return int
     * @author mice
     * @date 2019/7/30
    */
    int getPokerType(List<Poker> pokerList);
}