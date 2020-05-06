package com.dmg.lobbyserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.common.core.util.RedisUtil;
import com.dmg.lobbyserver.config.RedisKey;
import com.dmg.lobbyserver.dao.UserDao;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.model.dto.LeaderboardDTO;
import com.dmg.lobbyserver.service.UserService;
import com.dmg.lobbyserver.service.UserTaskProgressService;
import com.dmg.server.common.enums.TaskTypeEnum;
import com.dmg.server.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.dmg.common.core.config.RedisRegionConfig.USER;


/**
 * @Description
 * @Author mice
 * @Date 2019/6/24 15:44
 * @Version V1.0
 **/
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserTaskProgressService userTaskProgressService;

    @Autowired
    private RedisUtil redisUtil;

    @Cacheable(cacheNames = USER, key = "#userId", unless="#result == null")
    public UserBean getUserById(Long userId) {
        return userDao.selectById(userId);
    }

    @Override
    public UserBean getUserByUserCode(Long userCode) {
        return userDao.selectOne(new LambdaQueryWrapper<UserBean>().eq(UserBean::getUserCode, userCode));
    }

    @CachePut(cacheNames = USER, key = "#userBean.id",unless="#result == null")
    public UserBean insert(UserBean userBean) {
        userDao.insert(userBean);
        return userBean;
    }

    @CachePut(cacheNames = USER, key = "#userBean.id")
    public UserBean updateUserById(UserBean userBean) {
        userDao.updateById(userBean);
        return userBean;
    }

    @Override
    @CachePut(cacheNames = USER, key = "#userId",unless="#result == null")
    public UserBean changeAccountBalance(Long userId, BigDecimal changeAccount) {
        Integer row = userDao.updateAccountBalance(userId, changeAccount);
        if (row < 1) {
            return null;
        }
        UserBean userBean = userDao.selectById(userId);
        try {
            userTaskProgressService.userTaskChange(userId, TaskTypeEnum.CODE_GOLD.getCode(), null, userBean.getAccountBalance().intValue());
        } catch (Exception e) {
            log.error("任务进度变更出现异常：{}", e);
        }
        return userBean;
    }

    @Override
    @CachePut(cacheNames = USER, key = "#userId",unless="#result == null")
    public UserBean changeIntegral(Long userId, Long integral) {
        Integer row = userDao.updateIntegral(userId, integral);
        if (row < 1) {
            return null;
        }
        UserBean userBean = userDao.selectById(userId);
        return userBean;
    }

    @Override
    public void changeActivityLevel(Long userId, Double activityLevel) {
        redisUtil.incr(RedisKey.USER_ACTIVITY_LEVEL.concat(":").concat(DateUtils.getDate("yyyyMMdd"))
                .concat(":").concat(String.valueOf(userId)), activityLevel);
        redisUtil.expire(RedisKey.USER_ACTIVITY_LEVEL.concat(":").concat(DateUtils.getDate("yyyyMMdd"))
                .concat(":").concat(String.valueOf(userId)), 1, TimeUnit.DAYS);
        //活跃度任务
        try {
            userTaskProgressService.userTaskChange(userId, TaskTypeEnum.CODE_ACTIVITYLEVEL.getCode(), activityLevel.intValue(), null);
        } catch (Exception e) {
            log.error("任务进度变更出现异常：{}", e);
        }
    }

    @Override
    public UserBean getUserByDeviceCode(String deviceCode) {
        return userDao.selectOne(new LambdaQueryWrapper<UserBean>().eq(UserBean::getDeviceCode,deviceCode));
    }

    @Override
    public List<LeaderboardDTO> getLeaderboard(int limit) {
        return userDao.selectLeaderboard(50);
    }

}