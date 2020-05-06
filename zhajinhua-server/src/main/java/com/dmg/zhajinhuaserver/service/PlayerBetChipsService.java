package com.dmg.zhajinhuaserver.service;

import com.dmg.zhajinhuaserver.model.bean.Player;

/**
 * @Description
 * @Author jock
 * @Date 2019/7/10 0010
 * @Version V1.0
 **/
public interface PlayerBetChipsService {
    /**
     * 操作下注
     * @param player 玩家信息
     * @param beSeatId 比牌座位
     * @param oper 下注动作PokerOperState
     * @param wager 加注倍数
     */
    void handleBet(Player player, int beSeatId, int oper, long wager);

}
