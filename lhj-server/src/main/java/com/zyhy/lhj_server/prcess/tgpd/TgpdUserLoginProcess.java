/**
 * 
 */
package com.zyhy.lhj_server.prcess.tgpd;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.zyhy.common_lhj.Player;
import com.zyhy.common_lhj.process.AbstractHttpMsgProcess;
import com.zyhy.common_server.model.UserLhjLog;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.bgmanagement.config.MessageIdEnum;
import com.zyhy.lhj_server.bgmanagement.entity.SoltGameInfo;
import com.zyhy.lhj_server.bgmanagement.service.imp.BgManagementServiceImp;
import com.zyhy.lhj_server.constants.Constants;
import com.zyhy.lhj_server.constants.MessageConstants;
import com.zyhy.lhj_server.prcess.result.tgpd.TgpdLoginResult;
import com.zyhy.lhj_server.userservice.UserService;

/**
 * @author linanjun
 * 用户登录
 */
@Order
@Component
public class TgpdUserLoginProcess extends AbstractHttpMsgProcess{

	@Autowired
	private BgManagementServiceImp bgManagementServiceImp;
	@Autowired
	private UserService userService;
	//@Autowired
	//private RocketmqTemplate rocketmqTemplate;
	
	@Override
	public int getMessageId() {
		return MessageConstants.TGPD_USER_APPLY_LOGIN;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		TgpdLoginResult result = new TgpdLoginResult();
		//获取用户信息
		String roleid = body.get("roleid");
		Player user = userService.getUserInfo(roleid, uuid);
		if(user == null){
			result.setRet(2);
			result.setMsg("登录失败");
			return result;
		}
		
		
		SoltGameInfo gameInfo = bgManagementServiceImp.queryGameInfo(MessageIdEnum.TGPD.getRedisName());
		if (user.getGold() < gameInfo.getInAmount()) {
			result.setUuid(String.valueOf(gameInfo.getInAmount()));
			result.setGold(user.getGold());
			return result;
		}
		
		result.setGold(user.getGold());
		
//		CommonLog.getInstance(restTemplate).createUserLhjLog(LogType.LOGIN_CZDBZ, CzdbzConstants.SERVER_NAME, user.getRoleId(), user.getNickname(), result.getGold());
		UserLhjLog log = UserLhjLog.build(Constants.TGPD_SERVER_NAME, "login", roleid, user.getNickname(), user.getGold());
		//rocketmqTemplate.pushLog(Tags.LHJ_USER, JSONObject.toJSONString(log));
		return result;
	}
}
