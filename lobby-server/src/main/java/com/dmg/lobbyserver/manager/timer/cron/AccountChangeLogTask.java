package com.dmg.lobbyserver.manager.timer.cron;

import com.dmg.common.core.util.RedisUtil;
import com.dmg.lobbyserver.config.RedisKey;
import com.dmg.lobbyserver.dao.bean.AccountChangeLogBean;
import com.dmg.lobbyserver.service.AccountChangeLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author liubo
 * @Description //TODO 账变数据
 * @Date 20:06 2019/12/30
 **/
@EnableScheduling
@Component
@Slf4j
public class AccountChangeLogTask {

    @Autowired
    private AccountChangeLogService accountChangeLogService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * @Author liubo
     * @Description //TODO 同步账变数据
     * @Date 20:07 2019/12/30
     **/
    @Scheduled(cron = "0 */5 * * * ?")
    public void doMarquee() {
        log.info("==>定时器同步账变数据");
        List<AccountChangeLogBean> accountChangeLogBeanList = new ArrayList<>();
        List<Object> data = redisUtil.lGetAndTrim(RedisKey.ACCOUNT_CHANGE_LOG, 0, -1);
        data.forEach(object -> accountChangeLogBeanList.add((AccountChangeLogBean) object));
        if(accountChangeLogBeanList != null && accountChangeLogBeanList.size() > 0) {
            accountChangeLogService.insertList(accountChangeLogBeanList);
        }
    }

}