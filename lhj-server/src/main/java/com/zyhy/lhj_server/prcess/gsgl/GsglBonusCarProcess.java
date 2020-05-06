/**
 * 
 */
package com.zyhy.lhj_server.prcess.gsgl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.zyhy.common_lhj.Player;
import com.zyhy.common_lhj.process.AbstractHttpMsgProcess;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.constants.MessageConstants;
import com.zyhy.lhj_server.game.gsgl.GsglBonusInfo;
import com.zyhy.lhj_server.prcess.result.gsgl.GsglBonusCarResult;
import com.zyhy.lhj_server.service.gsgl.GsglBonusService;
import com.zyhy.lhj_server.userservice.UserService;

/**
 * @author ASUS
 *
 */
@Order
@Component
public class GsglBonusCarProcess extends AbstractHttpMsgProcess{

	@Autowired
	private GsglBonusService bonusService;
	@Autowired
	private UserService userService;
	@Override
	public int getMessageId() {
		return MessageConstants.GSGL_BONUS_CAR;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		String roleid = body.get("roleid");
		int car = Integer.parseInt(body.get("car"));
		// 用户信息
		Player userinfo = userService.getUserInfo(roleid, uuid);
		GsglBonusCarResult result = new GsglBonusCarResult();
		if(car < 1 || car > 3){
			result.setRet(2);
			result.setMsg("参数错误");
			return result;
		}
		
		GsglBonusInfo bi = bonusService.getData(roleid, uuid);
		result = bonusService.doGameCar(roleid, car ,bi);
		bonusService.save(result.getBonusInfo(), roleid, userinfo, uuid);
		return result;
	}

}
