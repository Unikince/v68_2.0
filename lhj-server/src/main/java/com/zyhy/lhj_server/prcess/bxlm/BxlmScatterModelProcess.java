/**
 * 
 */
package com.zyhy.lhj_server.prcess.bxlm;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.zyhy.common_lhj.Player;
import com.zyhy.common_lhj.process.AbstractHttpMsgProcess;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.constants.MessageConstants;
import com.zyhy.lhj_server.game.bxlm.BxlmScatterInfo;
import com.zyhy.lhj_server.prcess.result.bxlm.BxlmScatterModelResult;
import com.zyhy.lhj_server.service.bxlm.BxlmGameService;
import com.zyhy.lhj_server.userservice.UserService;

/**
 * @author ASUS
 *
 */
@Order
@Component
public class BxlmScatterModelProcess extends AbstractHttpMsgProcess{
	@Autowired
	private UserService userService;
	@Autowired
	private BxlmGameService gameService;
	
	@Override
	public int getMessageId() {
		return MessageConstants.BXLM_REPLENISH_GAME_MODL;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		String roleid = body.get("roleid");
		int model = Integer.parseInt(body.get("model"));//免费游戏模式0未选择 1 AMBER模式 2 TROY模式 3 MICHAEL模式 4 SARAH模式
		
		BxlmScatterModelResult result = new BxlmScatterModelResult();	
		if(model <= 0&& model > 4){
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
		BxlmScatterInfo sinfo = gameService.getData(roleid, uuid);
		if(sinfo != null && sinfo.getModel() != 0){//正在进行免费游戏
			result.setRet(2);
			result.setMsg("免费游戏已选择模式选择");
			return result;
		}
		
		sinfo.setModel(model);
		switch (model) {
			case 1:
				sinfo.setNum(10);
				break;
			case 2:
				sinfo.setNum(15);
				break;
			case 3:
				sinfo.setNum(20);
				break;
			case 4:
				sinfo.setNum(25);
				break;
			default:
				result.setRet(2);
				result.setMsg("模式错误");
				return result;
		}
		
		gameService.save(sinfo, roleid, userinfo, uuid);
		
		result.setScatter(sinfo);
		result.setScatterNum(sinfo.getNum());
		
		return result;
	}

}
