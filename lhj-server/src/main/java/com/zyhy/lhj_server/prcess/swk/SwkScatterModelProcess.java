/**
 * 
 */
package com.zyhy.lhj_server.prcess.swk;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.zyhy.common_lhj.Player;
import com.zyhy.common_lhj.process.AbstractHttpMsgProcess;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.constants.MessageConstants;
import com.zyhy.lhj_server.game.swk.SwkScatterInfo;
import com.zyhy.lhj_server.prcess.result.swk.SwkScatterModelResult;
import com.zyhy.lhj_server.service.swk.SwkGameService;
import com.zyhy.lhj_server.userservice.UserService;

@Order
@Component
public class SwkScatterModelProcess extends AbstractHttpMsgProcess{
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SwkGameService gameService;
	
	
	@Override
	public int getMessageId() {
		return MessageConstants.SWK_REPLENISH_GAME_MODL;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		String roleid = body.get("roleid");
		int model = Integer.parseInt(body.get("model"));//免费游戏模式0未选择 1 AMBER模式 2 TROY模式 3 MICHAEL模式 4 SARAH模式
		
		SwkScatterModelResult result = new SwkScatterModelResult();	
		if(model <= 0&& model > 3){
			result.setRet(2);
			result.setMsg("模式错误");
			return result;
		}
		// 用户信息
		Player userinfo = userService.getUserInfo(roleid, uuid);
		if (userinfo == null) {
			result.setRet(2);
			result.setMsg("登录错误");
			return result;
		}
		
		//判断是否免费游戏中
		SwkScatterInfo sinfo = gameService.getData(roleid, uuid);
		if(sinfo != null && sinfo.getModel() != 0){//正在进行免费游戏
			result.setRet(2);
			result.setMsg("免费游戏已选择模式选择");
			return result;
		}
		
		sinfo.setModel(model);
		switch (model) {
			case 1:
				sinfo.setNum(8);
				break;
			case 2:
				sinfo.setNum(10);
				break;
			case 3:
				sinfo.setNum(20);
				break;
			default:
				result.setRet(2);
				result.setMsg("模式错误");
				return result;
		}
		
		gameService.save(sinfo, roleid, userinfo, uuid);
		
		result.setScatter(sinfo);
		return result;
	}

}
