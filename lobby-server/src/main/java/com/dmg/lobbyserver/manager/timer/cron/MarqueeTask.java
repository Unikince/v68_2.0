package com.dmg.lobbyserver.manager.timer.cron;

import com.alibaba.fastjson.JSONObject;
import com.dmg.gameconfigserverapi.dto.SysMarqueeConfigDTO;
import com.dmg.gameconfigserverapi.feign.MarqueeConfigService;
import com.dmg.lobbyserver.config.MessageConfig;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.tcp.manager.LocationManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.alibaba.fastjson.serializer.SerializerFeature.*;

/**
 * @author mice
 * @description: 跑马灯
 * @date 2019/12/25
 */
@EnableScheduling
@Component
@Slf4j
public class MarqueeTask {
    @Autowired
    private MarqueeConfigService marqueeConfigService;
    @Autowired
    private LocationManager locationManager;

    /**
     * @param
     * @return void
     * @description: 普通公告
     * @author mice
     * @date 2019/12/25
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void doMarquee() {
        log.info("==>定时器刷新普通公告");
        List<SysMarqueeConfigDTO> sysMarqueeConfigDTOList = marqueeConfigService.getResourceServerList(1);
        if (CollectionUtils.isEmpty(sysMarqueeConfigDTOList)) {
            return;
        }
        locationManager.getWebSocketAll().forEach(myWebSocket -> {
            if (myWebSocket != null) {
                synchronized (myWebSocket.getSession()) {
                    sysMarqueeConfigDTOList.forEach(sysMarqueeConfigDTO -> {
                        MessageResult messageResult = new MessageResult(1, sysMarqueeConfigDTO, MessageConfig.MARQUEE_NTC);
                        myWebSocket.sendMessage(JSONObject.toJSONString(messageResult, WriteNullStringAsEmpty, WriteNullListAsEmpty, DisableCircularReferenceDetect));
                    });
                }
            }
        });
    }

    /**
     * @param
     * @return void
     * @description: 维护公告
     * @author mice
     * @date 2019/12/25
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void doMaintainMarquee() {
        log.info("==>定时器刷新维护公告");
        List<SysMarqueeConfigDTO> sysMarqueeConfigDTOList = marqueeConfigService.getResourceServerList(2);
        if (CollectionUtils.isEmpty(sysMarqueeConfigDTOList)) {
            return;
        }
        locationManager.getWebSocketAll().forEach(myWebSocket -> {
            if (myWebSocket != null) {
                synchronized (myWebSocket.getSession()) {
                    sysMarqueeConfigDTOList.forEach(sysMarqueeConfigDTO -> {
                        MessageResult messageResult = new MessageResult(1, sysMarqueeConfigDTO, MessageConfig.MARQUEE_NTC);
                        myWebSocket.sendMessage(JSONObject.toJSONString(messageResult, WriteNullStringAsEmpty, WriteNullListAsEmpty, DisableCircularReferenceDetect));
                    });
                }
            }
        });
    }
}