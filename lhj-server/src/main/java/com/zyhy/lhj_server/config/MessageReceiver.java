package com.zyhy.lhj_server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zyhy.lhj_server.bgmanagement.service.imp.BgManagementServiceImp;

import lombok.extern.slf4j.Slf4j;
 
/**
 * MQ消息处理器
 * @author zhh
 */
@Component
@Slf4j
public class MessageReceiver {
	@Autowired
    private BgManagementServiceImp bgManagementServiceImp;
    /**
     * 更新游戏配置
     **/
    public void updateLhjGameConfig(String message) {
        log.warn("=====> 收到游戏配置更新消息,更新游戏配置!!!");
        bgManagementServiceImp.updateAllGameConfig();
    }


}