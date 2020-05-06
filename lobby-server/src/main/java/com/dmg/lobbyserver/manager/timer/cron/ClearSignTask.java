package com.dmg.lobbyserver.manager.timer.cron;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.dao.SignLogDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/21 14:06
 * @Version V1.0
 **/
@EnableScheduling
@Component
@Slf4j
public class ClearSignTask {
    @Autowired
    private SignLogDao signLogDao;

    /**
     * @description: 每周清除签到日志
     * @param
     * @return void
     * @author mice
     * @date 2019/6/21
     */
    @Scheduled(cron = "1 0 0 ? * MON")
    public void clearSignLog(){
        log.info("定时任务==>每周清除签到日志");
        signLogDao.delete(new LambdaQueryWrapper<>());
    }
}