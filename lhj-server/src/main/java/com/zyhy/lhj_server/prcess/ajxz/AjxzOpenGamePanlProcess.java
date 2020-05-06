/**
 * 
 */
package com.zyhy.lhj_server.prcess.ajxz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.zyhy.common_lhj.Bet;
import com.zyhy.common_lhj.BetLimitInfo;
import com.zyhy.common_lhj.Icon;
import com.zyhy.common_lhj.IconInfo;
import com.zyhy.common_lhj.Window;
import com.zyhy.common_lhj.WindowInfo;
import com.zyhy.common_lhj.process.AbstractHttpMsgProcess;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.bgmanagement.config.MessageIdEnum;
import com.zyhy.lhj_server.bgmanagement.service.imp.BgManagementServiceImp;
import com.zyhy.lhj_server.constants.MessageConstants;
import com.zyhy.lhj_server.game.ajxz.AjxzBetEnum;
import com.zyhy.lhj_server.game.ajxz.AjxzFreeInfo;
import com.zyhy.lhj_server.game.ajxz.AjxzIconEnum;
import com.zyhy.lhj_server.game.ajxz.AjxzWindowEnum;
import com.zyhy.lhj_server.prcess.result.ajxz.AjxzGamePanlResult;
import com.zyhy.lhj_server.service.ajxz.AjxzGameService;

/**
 * 打开游戏页面
 */
@Order
@Component
public class AjxzOpenGamePanlProcess extends AbstractHttpMsgProcess{
	@Autowired
	private AjxzGameService gameService;
	@Autowired
	private BgManagementServiceImp bgManagementServiceImp;
	public Map<Double, Integer> betenum = new HashMap<>();
	@Override
	public int getMessageId() {
		return MessageConstants.AJXZ_OPEN_GAME_PANL;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		AjxzGamePanlResult result = new AjxzGamePanlResult();
		
		// 获取后台管理赌注配置
		List<Double> betList = bgManagementServiceImp.queryBetList(MessageIdEnum.AJXZ.getRedisName());
		for (int i = 0; i < betList.size(); i++) {
			for (AjxzBetEnum e : AjxzBetEnum.values()) {
				if (e.getId() == i) {
					e.setBetcoin(betList.get(i));
					betenum.put(e.getBetcoin(), e.getId());
				}
			}
		}
		
		// 返回信息
		result.setBetlimitinfos(betinfos(ArrayUtils.subarray(AjxzBetEnum.values(), 0,betList.size())));
		result.setWindowinfos(windowinfos(AjxzWindowEnum.values()));
		result.setZooIconInfos(iconinfos(AjxzIconEnum.values()));
		String roleid = body.get("roleid");
		// 保存免费信息
		AjxzFreeInfo freeInfo = gameService.getFreeInfo(roleid, uuid);
		if(freeInfo != null){
			double lastbet = freeInfo.getBetInfo().getGold();
			if (betenum.containsKey(lastbet)) {
				freeInfo.setBetLine( betenum.get(lastbet));
			} else {
				betenum.put(lastbet, betenum.size());
				freeInfo.setBetLine( betenum.size());
			}
			result.setBonus(freeInfo);
		}
		// 删除补充数据
		gameService.delReplenishInfoCache(roleid, uuid);
		return result;
	}
	
	
	
	private List<BetLimitInfo> betinfos(Bet[] bs) {
		List<BetLimitInfo> is = new ArrayList<BetLimitInfo>();
		for(Bet b : bs) {
			is.add(new BetLimitInfo(b));
		}
		return is;
	}
	
	private List<WindowInfo> windowinfos(Window[] ws){
		List<WindowInfo> ls = new ArrayList<WindowInfo>();
		for(Window t : ws) {
			ls.add(new WindowInfo(t));
		}
		return ls;
	}
	
	private List<IconInfo> iconinfos(Icon[] is) {
		List<IconInfo> ls = new ArrayList<IconInfo>();
		for(Icon i : is) {
			ls.add(new IconInfo(i));
		}
		return ls;
	}

}
