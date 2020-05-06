/**
 * 
 */
package com.dmg.clubserver.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zyhy.common_server.util.JsonUtils;
import com.dmg.clubserver.result.MessageResult;

/**
 * @author nanjun.li
 * 心跳控制器
 */
@RestController
public class   HeatController {
	
	@Value("${server.port}")
	private int port;
	
	@PostMapping("/heartBeat")
	public String heartBeat(String msg){
		MessageResult result = new MessageResult(1, msg,"");
		return JsonUtils.object2json(result);
	}
}
