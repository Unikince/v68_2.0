/**
 * 
 */
package com.dmg.clubserver.controller;

import com.dmg.clubserver.result.MessageResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zyhy.common_server.util.JsonUtils;

/**
 * @author nanjun.li
 * 登录控制器
 */
@RestController
public class LoginController {
	
	@Value("${server.port}")
	private int port;
	
	@PostMapping("/testLogin")
	public String testLogin(String msg){
		MessageResult result = new MessageResult(1, msg,"");
		return JsonUtils.object2json(result);
//		return "端口:" + port + ",返回数据:" + login;
	}
}
