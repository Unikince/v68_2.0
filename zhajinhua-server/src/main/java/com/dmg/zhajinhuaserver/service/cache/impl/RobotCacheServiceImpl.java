package com.dmg.zhajinhuaserver.service.cache.impl;


import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.common.core.config.RedisRegionConfig;
import com.dmg.common.core.util.RedisUtil;
import com.dmg.common.core.web.BaseResultEnum;
import com.dmg.common.core.web.Result;
import com.dmg.zhajinhuaserver.common.constant.RegionConstant;
import com.dmg.zhajinhuaserver.dao.RobotBean;
import com.dmg.zhajinhuaserver.dao.RobotDao;
import com.dmg.zhajinhuaserver.model.bean.Robot;
import com.dmg.zhajinhuaserver.model.dto.GameExchangeRateDTO;
import com.dmg.zhajinhuaserver.service.cache.RobotCacheService;
import com.dmg.zhajinhuaserver.service.config.GameExchangeRateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    @Cacheable(cacheNames = RegionConstant.ROBOT, key = "#userId")
    public Robot getRobot(Long userId, int grade) {
        RobotBean robotBean = robotDao.selectOne(new LambdaQueryWrapper<RobotBean>().eq(RobotBean::getUserId, userId));
        if (robotBean == null) {
            return null;
        }
        Robot robot = new Robot();
        BeanUtils.copyProperties(robotBean, robot);
        robot.setRoleId(robotBean.getUserId());
        robot.setHeadImgUrl(robotBean.getHeadImg());
        robot.setNickname(robotBean.getNickname());

        robot.setActive(true);
        if (grade == 1) {
            robot.setGold(RandomUtil.randomInt(20000, 100000) * getExchangeRate().doubleValue());
        } else if (grade == 2) {
            robot.setGold(RandomUtil.randomInt(500000, 2000000) * getExchangeRate().doubleValue());
        } else if (grade == 3) {
            robot.setGold(RandomUtil.randomInt(10000000, 50000000) * getExchangeRate().doubleValue());
        } else if (grade == 4) {
            robot.setGold(RandomUtil.randomInt(10000000, 100000000) * getExchangeRate().doubleValue());
        }

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