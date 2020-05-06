package com.dmg.bairenzhajinhuaserver.sysconfig;

import com.dmg.bairenzhajinhuaserver.Bootstrap;
import com.dmg.common.core.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * MQ消息处理器
 * @author zhh
 */
@Component
@Slf4j
public class MessageReceiver {
    /**
     * 更新游戏配置
     **/
    public void updateBairenGameConfig(String message) {
        Bootstrap bootstrap = SpringUtil.getBean(Bootstrap.class);
        bootstrap.initsystemWinConfig();
        bootstrap.initRoomconfig();
        log.warn("==> 收到游戏配置更新消息,更新游戏配置!!!");
    }


}