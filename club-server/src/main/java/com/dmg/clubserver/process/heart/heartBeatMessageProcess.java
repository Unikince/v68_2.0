package com.dmg.clubserver.process.heart;

import com.alibaba.fastjson.JSONObject;
import com.dmg.clubserver.config.MessageConfig;
import com.dmg.clubserver.config.WebSocketClientConfig;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.result.MessageResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;


@Service
@Order
public class heartBeatMessageProcess implements AbstractMessageHandler {

	@Override
	public String getMessageId() {
		return MessageConfig.Heart_Beat;
	}

	@Override
	public void messageHandler(String userid, JSONObject params,MessageResult result) {
		MessageResult res = new MessageResult(1, "", getMessageId());
		System.out.print("测试=============================");
	}

}
