package com.dmg.bairenlonghu.service.logic;

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
     * @param betTableIndex
     * @param betChip
     * @return void
     * @author mice
     * @date 2019/7/30
    */
    void playerBet(int userId,String betTableIndex, BigDecimal betChip);
    /**
     * @description: 续压
     * @param userId
     * @return void
     * @author mice
     * @date 2019/9/5
    */
    void copyBet(int userId);

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