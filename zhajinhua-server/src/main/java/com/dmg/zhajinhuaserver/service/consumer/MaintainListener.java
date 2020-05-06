package com.dmg.zhajinhuaserver.service.consumer;

import com.dmg.common.starter.rocketmq.annotation.RocketMQMessageListener;
import com.dmg.common.starter.rocketmq.core.listener.RocketMQListener;
import com.dmg.zhajinhuaserver.common.constant.Constant;
import com.dmg.zhajinhuaserver.config.Config;
import com.dmg.zhajinhuaserver.config.MessageConfig;
import com.dmg.zhajinhuaserver.manager.RoomManager;
import com.dmg.zhajinhuaserver.model.bean.Robot;
import com.dmg.zhajinhuaserver.result.MessageResult;
import com.dmg.zhajinhuaserver.service.PushService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:20 2019/12/27
 */
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "MAINTAIN_CONSUMER", topic = "MAINTAIN", tags = "MAINTAIN", messageModel = MessageModel.BROADCASTING)
public class MaintainListener implements RocketMQListener {

    @Autowired
    private PushService pushService;

    @Override
    public void onMessage(MessageExt msg) {
        String data = null;
        try {
            data = new String(msg.getBody(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("消息解析异常{}", e);
            return;
        }
        if (StringUtils.isBlank(data)) {
            log.info("接收到的消息为空，不做任何处理");
            return;
        }
        log.warn("==>收到停服消息");
        if (String.valueOf(Constant.GAME_ID).equals(data)) {
            RoomManager.instance().setShutdownServer(true);
            MessageResult messageResult = new MessageResult(MessageConfig.SHUTDOWN_SERVER_NTC);
            this.pushService.broadcast(messageResult);
        }
    }
}
