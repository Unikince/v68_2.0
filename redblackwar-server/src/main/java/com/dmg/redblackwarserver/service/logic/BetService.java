package com.dmg.redblackwarserver.service.logic;

import java.math.BigDecimal;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/30 14:27
 * @Version V1.0
 **/
public interface BetService {

    /**
     * @description: 玩家下注
     * @param userId
     * @param copyBet
     * @param betTableIndex
     * @param betChip
     * @return void
     * @author mice
     * @date 2019/7/30
    */
    void playerBet(int userId, boolean copyBet, String betTableIndex, BigDecimal betChip);

    /**
     * @description: 机器人随机下注
     * @param roomId
     * @return void
     * @author mice
     * @date 2019/7/30
     */
    void robotRandomBet(int roomId);

    /**
     * @description: 机器人下注
     * @param userId
     * @param betTableIndex
     * @param betChip
     * @return void
     * @author mice
     * @date 2019/7/30
     */
    void robotBet(int userId, String betTableIndex, BigDecimal betChip);
}