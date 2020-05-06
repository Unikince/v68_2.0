package com.dmg.redblackwarserver.service.cache.impl;


import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.redblackwarserver.common.model.BaseRobot;
import com.dmg.redblackwarserver.dao.RobotBean;
import com.dmg.redblackwarserver.dao.RobotDao;
import com.dmg.redblackwarserver.service.cache.RobotCacheService;
import com.dmg.redblackwarserver.sysconfig.RegionConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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

    @Cacheable(cacheNames = RegionConfig.ROBOT, key = "#userId")
    public BaseRobot getRobot(int userId) {
//    	for(int i = 1000;i < 1200;i++) {
//    		RobotBean robot = new RobotBean();
//         robot.setUserId(i);
//         robot.setNickname("robot" + i);
//         robot.setGold(RandomUtil.randomInt(50000,100000));
//         robotDao.insert(robot);
//    	}
        RobotBean robotBean = robotDao.selectOne(new LambdaQueryWrapper<RobotBean>().eq(RobotBean::getUserId,userId));
        if (robotBean == null){
            return null;
        }
        BaseRobot robot = new BaseRobot();
        BeanUtils.copyProperties(robotBean,robot);
        robot.setUserId(robotBean.getUserId());
        robot.setHeadIcon(robotBean.getHeadImg());
        robot.setNickname(robotBean.getNickname());
        robot.setOnline(true);
        robot.setGold(new BigDecimal(RandomUtil.randomInt(500,1000)));
        return robot;
    }

    @CachePut(cacheNames = RegionConfig.ROBOT, key = "#robot.userId")
    public BaseRobot update(BaseRobot robot){

        return robot;
    }
}