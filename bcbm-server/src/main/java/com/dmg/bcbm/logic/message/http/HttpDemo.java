package com.dmg.bcbm.logic.message.http;

import java.util.Map;

import com.dmg.bcbm.core.annotation.Http;
import com.dmg.bcbm.core.server.http.handler.HttpReader;
import com.dmg.bcbm.core.server.http.handler.HttpSender;



@Http("/version")
public class HttpDemo extends HttpReader {

	@Override
	public void readMessage(Map<String, String> paramMap, HttpSender httpSender) {
		String aaa = "httpDemo!";
		System.out.println(aaa);
		httpSender.send(aaa);
	}

}
