/**
 * 
 */
package com.zyhy.lhj_server.prcess.swk;

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
import com.zyhy.lhj_server.game.swk.SwkBetEnum;
import com.zyhy.lhj_server.game.swk.SwkIconEnum;
import com.zyhy.lhj_server.game.swk.SwkScatterInfo;
import com.zyhy.lhj_server.game.swk.SwkWindowEnum;
import com.zyhy.lhj_server.prcess.result.swk.SwkGamePanlResult;
import com.zyhy.lhj_server.service.swk.SwkGameService;

/**
 * 打开游戏页面
 */
@Order
@Component
public class SwkOpenGamePanlProcess extends AbstractHttpMsgProcess{
	@Autowired
	private BgManagementServiceImp bgManagementServiceImp;
	public Map<Double, Integer> betenum = new HashMap<>();
	@Autowired
	private SwkGameService gameService;
	@Override
	public int getMessageId() {
		return MessageConstants.SWK_OPEN_GAME_PANL;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		SwkGamePanlResult result = new SwkGamePanlResult();
		
		// 获取后台管理赌注配置
		List<Double> betList = bgManagementServiceImp.queryBetList(MessageIdEnum.SWK.getRedisName());
		for (int i = 0; i < betList.size(); i++) {
			for (SwkBetEnum e : SwkBetEnum.values()) {
				if (e.getId() == i) {
					e.setBetcoin(betList.get(i));
					betenum.put(e.getBetcoin(), e.getId());
				}
			}
		}
		
		result.setBetlimitinfos(betinfos(ArrayUtils.subarray(SwkBetEnum.values(), 0,betList.size())));
		result.setWindowinfos(windowinfos(SwkWindowEnum.values()));
		result.setZooIconInfos(iconinfos(SwkIconEnum.values()));
		String roleid = body.get("roleid");
		SwkScatterInfo scatter = gameService.getData(roleid, uuid);
		if(scatter != null){
			double lastbet = scatter.getBetInfo().getGold();
			if (betenum.containsKey(lastbet)) {
				scatter.setBetLine( betenum.get(lastbet));
			} else {
				betenum.put(lastbet, betenum.size());
				scatter.setBetLine( betenum.size());
			}
			result.setScatter(scatter);
		}
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
