package com.dmg.niuniuserver.service.cache.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.common.core.config.RedisRegionConfig;
import com.dmg.common.core.util.RedisUtil;
import com.dmg.common.core.web.BaseResultEnum;
import com.dmg.common.core.web.Result;
import com.dmg.niuniuserver.constant.RegionConstant;
import com.dmg.niuniuserver.dao.RobotBean;
import com.dmg.niuniuserver.dao.RobotDao;
import com.dmg.niuniuserver.model.bean.Robot;
import com.dmg.niuniuserver.model.dto.GameExchangeRateDTO;
import com.dmg.niuniuserver.model.dto.NiuniuRobotActionDTO;
import com.dmg.niuniuserver.service.cache.RobotCacheService;
import com.dmg.niuniuserver.service.config.GameExchangeRateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/1 18:47
 * @Version V1.0
 **/
@Service
@Slf4j
public class RobotCacheServiceImpl implements RobotCacheService {

    @Autowired
    private RobotDao robotDao;

    @Value("${exchange-rate-code}")
    private String exchangeRateCode;

    @Autowired
    private GameExchangeRateService gameExchangeRateService;

    @Autowired
    private RedisUtil redisUtil;

    @Cacheable(cacheNames = RegionConstant.ROBOT, key = "#userId", unless = "#result == null")
    public Robot getRobot(Long userId) {
        RobotBean robotBean = robotDao.selectOne(new LambdaQueryWrapper<RobotBean>().eq(RobotBean::getUserId, userId));
        if (robotBean == null) {
            return null;
        }
        Robot robot = new Robot();
        BeanUtils.copyProperties(robotBean, robot);
        robot.setUserCode(robotBean.getUserId());
        robot.setOnline(true);
        robot.updateRobotAndPrivateField(robot.getGold() + RandomUtil.randomInt(2000, 5000) * getExchangeRate().doubleValue());
        return robot;
    }

    @CachePut(cacheNames = RegionConstant.ROBOT, key = "#robot.userId")
    public Robot update(Robot robot) {

        return robot;
    }

    private BigDecimal getExchangeRate() {
        Object object = redisUtil.get(RedisRegionConfig.GAME_EXCHANGE_RATE);
        if (object != null) {
            return new BigDecimal(String.valueOf(object));
        }
        try {
            Result<GameExchangeRateDTO> result = gameExchangeRateService.getInfoByWater(exchangeRateCode);
            if (BaseResultEnum.SUCCESS.getCode().toString().equals(result.getCode())) {
                if (result.getData() != null) {
                    redisUtil.set(RedisRegionConfig.GAME_EXCHANGE_RATE, String.valueOf(result.getData().getExchangeRate()), 180, TimeUnit.DAYS);
                }
                return result.getData().getExchangeRate();
            }
        } catch (Exception e) {
            log.error("调用game-config服务查询汇率赔偿出现异常:{}", e);
        }
        return BigDecimal.valueOf(1);
    }
}