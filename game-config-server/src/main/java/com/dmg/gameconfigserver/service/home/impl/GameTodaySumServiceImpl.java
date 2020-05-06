package com.dmg.gameconfigserver.service.home.impl;

import com.dmg.gameconfigserver.model.vo.home.GameTodaySunVO;
import com.dmg.gameconfigserver.service.financing.PlatformRechargeLogService;
import com.dmg.gameconfigserver.service.financing.WithdrawOrderService;
import com.dmg.gameconfigserver.service.home.GameTodaySumService;
import com.dmg.gameconfigserver.service.record.GameRecordService;
import com.dmg.gameconfigserver.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 14:46 2020/3/17
 */
@Service
public class GameTodaySumServiceImpl implements GameTodaySumService {

    @Autowired
    private UserService userService;

    @Autowired
    private PlatformRechargeLogService platformRechargeLogService;

    @Autowired
    private WithdrawOrderService withdrawOrderService;

    @Autowired
    private GameRecordService gameRecordService;

    @Override
    public GameTodaySunVO getGameTodaySun() {
        return GameTodaySunVO.builder()
                .activeNum(userService.getTodayLoginCount())
                .registerNum(userService.getTodayRegisterCount())
                .firstRechargeNum(platformRechargeLogService.getFirstRechargeCount())
                .sumFirstRecharge(platformRechargeLogService.getSumFirstRechargeAmount().setScale(2, BigDecimal.ROUND_DOWN))
                .sumRecharge(platformRechargeLogService.getSumRechargeAmount().setScale(2, BigDecimal.ROUND_DOWN))
                .sumDrawing(withdrawOrderService.countTodayWithdraw().setScale(2, BigDecimal.ROUND_DOWN))
                .profit(gameRecordService.getTodayProfit().negate().setScale(2, BigDecimal.ROUND_DOWN))
                .service(gameRecordService.getTodayServiceCharge().setScale(2, BigDecimal.ROUND_DOWN)).build();
    }
}
