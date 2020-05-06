/**
 * 
 */
package com.zyhy.lhj_server.prcess.fkmj;

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
import com.zyhy.lhj_server.game.fkmj.FkmjBetEnum;
import com.zyhy.lhj_server.game.fkmj.FkmjBonusInfo;
import com.zyhy.lhj_server.game.fkmj.FkmjIconEnum;
import com.zyhy.lhj_server.game.fkmj.FkmjWindowEnum;
import com.zyhy.lhj_server.prcess.result.fkmj.FkmjGamePanlResult;
import com.zyhy.lhj_server.service.fkmj.FkmjGameService;

/**
 * 打开游戏页面
 */
@Order
@Component
public class FkmjOpenGamePanlProcess extends AbstractHttpMsgProcess{
	@Autowired
	private BgManagementServiceImp bgManagementServiceImp;
	public Map<Double, Integer> betenum = new HashMap<>();
	@Autowired
	private FkmjGameService gameService;
	@Override
	public int getMessageId() {
		return MessageConstants.FKMJ_OPEN_GAME_PANL;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		FkmjGamePanlResult result = new FkmjGamePanlResult();
		
		// 获取后台管理赌注配置
		List<Double> betList = bgManagementServiceImp.queryBetList(MessageIdEnum.FKMJ.getRedisName());
		for (int i = 0; i < betList.size(); i++) {
			for (FkmjBetEnum e : FkmjBetEnum.values()) {
				if (e.getId() == i) {
					e.setBetcoin(betList.get(i));
					betenum.put(e.getBetcoin(), e.getId());
				}
			}
		}
		
		result.setBetlimitinfos(betinfos(ArrayUtils.subarray(FkmjBetEnum.values(), 0,betList.size())));
		result.setWindowinfos(windowinfos(FkmjWindowEnum.values()));
		result.setZooIconInfos(iconinfos(FkmjIconEnum.values()));
		String roleid = body.get("roleid");
		FkmjBonusInfo bonus = gameService.getFree(roleid, uuid);
		if(bonus != null && bonus.getNum() > 0){
			double lastbet = bonus.getBetInfo().getGold();
			if (betenum.containsKey(lastbet)) {
				bonus.setBetLine( betenum.get(lastbet));
			} else {
				betenum.put(lastbet, betenum.size());
				bonus.setBetLine( betenum.size());
			}
			result.setBonus(bonus);
		}
		gameService.deleteReplenish(roleid, uuid);
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
