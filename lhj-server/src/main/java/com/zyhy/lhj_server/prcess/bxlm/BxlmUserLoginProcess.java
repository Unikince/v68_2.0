/**
 * 
 */
package com.zyhy.lhj_server.prcess.bxlm;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.zyhy.common_lhj.Player;
import com.zyhy.common_lhj.process.AbstractHttpMsgProcess;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.bgmanagement.config.MessageIdEnum;
import com.zyhy.lhj_server.bgmanagement.entity.SoltGameInfo;
import com.zyhy.lhj_server.bgmanagement.manager.CacheManager;
import com.zyhy.lhj_server.bgmanagement.service.imp.BgManagementServiceImp;
import com.zyhy.lhj_server.constants.MessageConstants;
import com.zyhy.lhj_server.prcess.result.LoginResult;
import com.zyhy.lhj_server.userservice.UserService;

/**
 * @author linanjun
 * 用户登录
 */
@Order
@Component
public class BxlmUserLoginProcess extends AbstractHttpMsgProcess{

	@Autowired
	private BgManagementServiceImp bgManagementServiceImp;
	@Autowired
	private UserService userService;
	private static final Logger LOG = LoggerFactory.getLogger(BxlmUserLoginProcess.class);
	@Override
	public int getMessageId() {
		return MessageConstants.BXLM_USER_APPLY_LOGIN;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		LoginResult result = new LoginResult();
		
		//获取用户信息
		String roleid = body.get("roleid");
		Player user = userService.getUserInfo(roleid, uuid);
		if(user == null){
			result.setRet(2);
			result.setMsg("登录失败");
			return result;
		}
		
		// 验证准入金额
		SoltGameInfo gameInfo = bgManagementServiceImp.queryGameInfo(MessageIdEnum.BXQY.getRedisName());
		if (user.getGold() < gameInfo.getInAmount()) {
			result.setRet(4);
			result.setMsg("金币达不到入场要求");
			result.setInAmount(gameInfo.getInAmount());
			result.setCurrentGold(user.getGold());
			return result;
		}
		
		// 获取当前汇率
		double exchangeRate = CacheManager.instance().getExchangeRate(); // 当前汇率
		LOG.info("当前游戏汇率 =====> " + exchangeRate);
		if (exchangeRate <= 0d) {
			result.setRet(2);
			result.setMsg("exchangeRate error!");
			return result;
		}
		
		result.setGold(user.getGold());
		result.setExchangeRate(exchangeRate);
		return result;
	}
}
