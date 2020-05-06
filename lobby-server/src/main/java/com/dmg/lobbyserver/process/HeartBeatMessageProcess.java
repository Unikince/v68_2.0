package com.dmg.lobbyserver.process;

import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.config.MessageConfig;
import com.dmg.lobbyserver.result.MessageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;


@Service
@Order
@Slf4j
public class HeartBeatMessageProcess implements AbstractMessageHandler {

	@Override
	public String getMessageId() {
		return MessageConfig.Heart_Beat;
	}

	@Override
	public void messageHandler(String userid, JSONObject params, MessageResult result) {
	}

}
