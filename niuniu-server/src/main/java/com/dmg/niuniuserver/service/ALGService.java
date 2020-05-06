package com.dmg.niuniuserver.service;


import com.dmg.niuniuserver.model.bean.GameRoom;
import com.dmg.niuniuserver.model.bean.Poker;
import com.dmg.niuniuserver.model.bean.Seat;
import com.dmg.niuniuserver.model.constants.Combination;

import java.util.LinkedList;
import java.util.List;

/**
 * @author hexf
 * @Date 2018年3月13日
 * @Desc 双Q算法
 */
public interface ALGService {

    /**
     * 生成整副牌
     *
     * @return
     */
    LinkedList<Poker> createDeck(GameRoom room);


    /**
     * 生成测试整副牌
     *
     * @return
     */
    LinkedList<Poker> createTestDeck();


    /**
     * 计算牌型
     *
     * @param list
     * @return
     */
    Combination evalPokerType(List<Poker> list, Seat seat, GameRoom room);

    int getPokerType(List<Poker> pokerList);
    /**
     * @Author liubo
     * @Description //TODO 设置牌型
     * @Date 13:44 2019/9/29
     **/
    void setOrdinaryCattleByNoWang(List<Poker> pokerList, Seat seat);

}
