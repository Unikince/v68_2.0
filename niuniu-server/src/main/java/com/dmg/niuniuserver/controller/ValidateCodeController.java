package com.dmg.niuniuserver.controller;

import cn.hutool.core.util.RandomUtil;
import com.dmg.niuniuserver.result.MessageResult;
import com.dmg.niuniuserver.result.ResultEnum;
import com.zyhy.common_server.util.HttpURLUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 获取验证码
 * @return
 * @author mice
 * @date 2019/4/9
*/
@Slf4j
@RestController
public class ValidateCodeController{
	@Autowired
	StringRedisTemplate stringRedisTemplate;

	@GetMapping("getValidateCode")
	public MessageResult getValidateCode(String phoneNumber) {
		String validateCode = RandomUtil.randomNumbers(4);
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("http://148.66.63.107:8001/api?");
			builder.append("username=");
			builder.append("Monaco-8direct");
			builder.append("&password=");
			builder.append("mfmhmwim");
			builder.append("&ani=");
			builder.append("852678967893");
			builder.append("&dnis=");
			builder.append("86"+phoneNumber);
			builder.append("&message=");
			builder.append("【xbettest】会员您好，您的验证码是"+validateCode+"请尽快输入!");
			builder.append("&command=");
			builder.append("submit");

			log.debug(HttpURLUtils.doGet(builder.toString()));
			log.debug("验证码发送成功:"+builder.toString());
			log.debug("验证码发送成功,验证码为:"+validateCode);
			// 发送成功 保存验证码
			stringRedisTemplate.opsForValue().set("validate_code:"+phoneNumber,validateCode+"_"+System.currentTimeMillis());
		} catch (Exception e) {
			e.printStackTrace();
			return MessageResult.error(ResultEnum.SYSTEM_EXCEPTION.getCode()+"");
		}
		return new MessageResult("");
	}
}
