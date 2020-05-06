/**
 * 
 */
package com.zyhy.lhj_server.prcess.nnyy;

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
import com.zyhy.lhj_server.game.nnyy.NnyyBetEnum;
import com.zyhy.lhj_server.game.nnyy.NnyyIconEnum;
import com.zyhy.lhj_server.game.nnyy.NnyyWindowEnum;
import com.zyhy.lhj_server.prcess.result.nnyy.NnyyGamePanlResult;
import com.zyhy.lhj_server.service.nnyy.NnyyDragonGameService;

/**
 * @author linanjun
 * 打开游戏页面
 */
@Order
@Component
public class NnyyOpenGamePanlProcess extends AbstractHttpMsgProcess{
	@Autowired
	private BgManagementServiceImp bgManagementServiceImp;
	public Map<Double, Integer> betenum = new HashMap<>();
	@Autowired
	private NnyyDragonGameService dragonGameService;
	@Override
	public int getMessageId() {
		return MessageConstants.NNYY_OPEN_GAME_PANL;
	}
	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		String roleid = body.get("roleid");
		NnyyGamePanlResult result = new NnyyGamePanlResult();
		
		// 获取后台管理赌注配置
		List<Double> betList = bgManagementServiceImp.queryBetList(MessageIdEnum.NNYC.getRedisName());
		for (int i = 0; i < betList.size(); i++) {
			for (NnyyBetEnum e : NnyyBetEnum.values()) {
				if (e.getId() == i) {
					e.setBetcoin(betList.get(i));
					betenum.put(e.getBetcoin(), e.getId());
				}
			}
		}
		
		result.setBetlimitinfos(betinfos(ArrayUtils.subarray(NnyyBetEnum.values(), 0,betList.size())));
		result.setWindowinfos(windowinfos(NnyyWindowEnum.values()));
		result.setZooIconInfos(iconinfos(NnyyIconEnum.values()));

		if(dragonGameService.check(roleid, uuid)){
			result.setDragonGameData(dragonGameService.getDragonGameData(roleid, uuid));
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
