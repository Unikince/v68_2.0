package com.dmg.bcbm.logic.work.mq;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.stereotype.Component;

import com.dmg.bcbm.core.manager.RoomManager;
import com.dmg.common.starter.rocketmq.annotation.RocketMQMessageListener;
import com.dmg.common.starter.rocketmq.core.listener.RocketMQListener;

import lombok.extern.slf4j.Slf4j;

/**
 * @description: 服务器维护消息
 * @return
 * @author mice
 * @date 2019/12/25
*/
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "MAINTAIN_CONSUMER", topic = "MAINTAIN", tags = "MAINTAIN", messageModel = MessageModel.BROADCASTING)
public class MaintainConsumer implements RocketMQListener {

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
        log.warn("=====>收到停服消息");
        if (RoomManager.intance().getGameId().equals(data)){
            RoomManager.intance().setShutdownServer(true);
        }
	}

}
