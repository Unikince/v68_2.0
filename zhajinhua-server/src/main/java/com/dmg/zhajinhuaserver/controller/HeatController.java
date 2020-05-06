/**
 * 
 */
package com.dmg.zhajinhuaserver.controller;

import com.dmg.zhajinhuaserver.result.MessageResult;
import com.zyhy.common_server.util.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nanjun.li
 * 心跳控制器
 */
@RestController
public class HeatController {
	
	@Value("${server.port}")
	private int port;
	
	@PostMapping("/heartBeat")
	public String heartBeat(String msg){
		MessageResult result = new MessageResult(1, msg,"");
		return JsonUtils.object2json(result);
	}
}
