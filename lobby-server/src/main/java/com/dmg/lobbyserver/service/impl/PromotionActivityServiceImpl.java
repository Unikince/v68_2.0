package com.dmg.lobbyserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.dao.SysPromotionCodeDao;
import com.dmg.lobbyserver.dao.TaskConfigDao;
import com.dmg.lobbyserver.dao.bean.SysPromotionCodeBean;
import com.dmg.lobbyserver.dao.bean.TaskConfigBean;
import com.dmg.lobbyserver.service.PromotionActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description  用户优惠活动(充值入口)
 * @Author jock
 * @Date 13:53
 * @Version V1.0
 **/
@Service
public class PromotionActivityServiceImpl  implements PromotionActivityService {
    @Autowired
    TaskConfigDao taskConfigDao ;
    @Autowired
    SysPromotionCodeDao sysPromotionCodeDao ;
    @Override
    public List<TaskConfigBean> getUserActivity(Long userId) {
        List<TaskConfigBean> taskConfigBeans = taskConfigDao.selectList(new LambdaQueryWrapper<TaskConfigBean>().
                eq(TaskConfigBean::getTaskType, 5));
        List<TaskConfigBean> collect1 = taskConfigBeans.stream().filter(x -> x.getTaskEndTime().getTime() >= new Date().getTime())
                .collect(Collectors.toList());
        return  collect1;
    }

    @Override
    public List<SysPromotionCodeBean> getUserActivityCode(Long userId) {
        List<SysPromotionCodeBean> sysPromotionCodeBeans = sysPromotionCodeDao.selectList(new LambdaQueryWrapper<SysPromotionCodeBean>().
                eq(SysPromotionCodeBean::getUserId, userId).eq(SysPromotionCodeBean::getHasReceive,1).eq(SysPromotionCodeBean::getPromotionType,1));
        //过滤过期时间
        List<SysPromotionCodeBean> collect = sysPromotionCodeBeans.stream().filter(x -> x.getExpireDate().getTime() >=
                new Date().getTime()).collect(Collectors.toList());
       return  collect;
    }
}

