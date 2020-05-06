package com.dmg.bairenlonghu.service.cache.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.bairenlonghu.common.model.BaseRobot;
import com.dmg.bairenlonghu.dao.RobotBean;
import com.dmg.bairenlonghu.dao.RobotDao;
import com.dmg.bairenlonghu.service.cache.RobotCacheService;
import com.dmg.bairenlonghu.sysconfig.RegionConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

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

    @Cacheable(cacheNames = RegionConfig.ROBOT_REDIS_KEY, key = "#userId")
    public BaseRobot getRobot(int userId) {
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
        return robot;
    }

    @CachePut(cacheNames = RegionConfig.ROBOT_REDIS_KEY, key = "#robot.userId")
    public BaseRobot update(BaseRobot robot){

        return robot;
    }
}