package com.dmg.lobbyserver.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.dmg.common.core.util.MD5Util;
import com.dmg.lobbyserver.config.RedisKey;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.exception.BusinessException;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.Result;
import com.dmg.lobbyserver.service.SendEmailService;
import com.dmg.lobbyserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import static com.dmg.lobbyserver.common.constants.CommonConstants.SECRETKEY;
import static com.dmg.lobbyserver.result.ResultEnum.ACCOUNT_NOT_EXIST;
import static com.dmg.lobbyserver.result.ResultEnum.VALIDATE_CODE_SEND_ERROR;

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
	@Autowired
	private SendEmailService sendEmailService;
	@Autowired
	private UserService userService;
	private final String smsUrl = "http://gmapi.bigfun.io/api/auth/sendsms";

	@RequestMapping("getValidateCode")
	public MessageResult getValidateCode(@RequestParam("phoneNumber") String phoneNumber,@RequestParam("userId") String userId) {
		/*String validateCode = RandomUtil.randomNumbers(4);
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
			builder.append("会员您好，您的验证码是"+validateCode+"请尽快输入!");
			builder.append("&command=");
			builder.append("submit");

			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("47.112.127.70", 8001));
			URL url = new URL(builder.toString());

			HttpURLConnection uc = (HttpURLConnection)url.openConnection(proxy);
			uc.setRequestMethod("GET");
			uc.connect();

			String line = null;
			StringBuffer tmp = new StringBuffer();
			BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			while ((line = in.readLine()) != null) {
				tmp.append(line);
			}
			log.info("验证码发送成功:"+builder.toString());
			log.info("验证码发送成功,验证码为:"+validateCode);*/
		Map<String,Object> paramMap = new TreeMap<>();
		paramMap.put("mobile",phoneNumber);
		//paramMap.put("channelCode",CHANNELCODE);
		String sign = this.sign(paramMap);
		paramMap.put("codeType",3+"");
		paramMap.put("sign",sign);
		paramMap.put("userId",userId);
		String result = HttpUtil.post(smsUrl,paramMap);
		log.info("验证码发送成功:"+result);
		String code = JSONObject.parseObject(result).getString("code");
		if (!code.equals("0")){
			return MessageResult.error(VALIDATE_CODE_SEND_ERROR.getCode()+"");
		}
		String validateCode = JSONObject.parseObject(result).getString("data");
		// 发送成功 保存验证码
		log.info("验证码发送成功,验证码为:"+validateCode);
		stringRedisTemplate.opsForValue().set(RedisKey.VALIDATE_CODE+phoneNumber,validateCode+"_"+System.currentTimeMillis(),60*6, TimeUnit.SECONDS);
		return new MessageResult("");
	}
	private String sign(Map<String,Object> paramMap){
		StringBuilder sb = new StringBuilder();
		for (String key : paramMap.keySet()){
			String value = (String) paramMap.get(key);
			sb.append(key + "=" + value + "&");
		}
		sb.append("key=");
		sb.append(SECRETKEY);
		log.info("签名字符串==>{}",sb.toString());
		String oursign = MD5Util.stringToMD5(sb.toString()).toLowerCase();
		log.info("签名==>{}",oursign);
		return oursign;
	}

	@RequestMapping("getImageValidateCode")
	public Result getImageValidateCode(@RequestParam("userId")Long userId) {
		String code = RandomUtil.randomString(4);
		log.info("验证码图片code为:"+code);
		stringRedisTemplate.opsForValue().set(RedisKey.VALIDATE_CODE+userId,code+"_"+System.currentTimeMillis(),60*6, TimeUnit.SECONDS);
		BufferedImage image = (BufferedImage) CaptchaUtil.createLineCaptcha(100,50,4,100).createImage(code);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, "JPEG", out);
		} catch (
				IOException e) {
			e.printStackTrace();
		}
		String imageStr = Base64.encode(out.toByteArray());
		return Result.success(imageStr);
	}


	@RequestMapping("getEmailValidateCode")
	public Result getEmailValidateCode(@RequestParam("userId")Long userId, @RequestParam("email")String email) {
		UserBean userBean = userService.getUserById(userId);
		if (userBean == null) {
			throw new BusinessException(ACCOUNT_NOT_EXIST.getCode(),ACCOUNT_NOT_EXIST.getMsg());
		}
		String validateCode = RandomUtil.randomNumbers(4);
		StringBuilder contentStringBuilder = new StringBuilder();
		contentStringBuilder.append(userBean.getUserName());
		contentStringBuilder.append("会员您好：\n" );
		contentStringBuilder.append("     欢迎进入我们的游戏，您的验证码是");
		contentStringBuilder.append(validateCode);
		contentStringBuilder.append("，如果您本人没有请求此验证码，请立即前往更改密码。 如果您需要支持，请联系客服。祝您游戏愉快！");
		sendEmailService.send(email,"验证码",contentStringBuilder.toString());
		log.info("验证码发送成功:"+contentStringBuilder.toString());
		log.info("验证码发送成功,验证码为:"+validateCode);
		// 发送成功 保存验证码
		stringRedisTemplate.opsForValue().set(RedisKey.VALIDATE_CODE+email,validateCode+"_"+System.currentTimeMillis(),60*6, TimeUnit.SECONDS);

		return Result.success("");
	}
}
