package com.dmg.game.task.job;

import com.dmg.game.task.service.UserEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:38 2020/3/19
 */
@EnableScheduling
@Component
@Slf4j
public class EmailExpireJob {

    @Autowired
    private UserEmailService userEmailService;

    @Scheduled(cron = "0 0 0 1/1 * ?")
    public void execute() {
        log.info("==>定时器清理过期邮件数据");
        userEmailService.clearExpireEmail();
    }
}
