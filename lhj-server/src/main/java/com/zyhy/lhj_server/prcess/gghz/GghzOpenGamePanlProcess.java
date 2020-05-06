/**
 * 
 */
package com.zyhy.lhj_server.prcess.gghz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.zyhy.common_lhj.Bet;
import com.zyhy.common_lhj.process.AbstractHttpMsgProcess;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.bgmanagement.config.MessageIdEnum;
import com.zyhy.lhj_server.bgmanagement.service.imp.BgManagementServiceImp;
import com.zyhy.lhj_server.constants.MessageConstants;
import com.zyhy.lhj_server.game.gghz.GghzBetEnum;
import com.zyhy.lhj_server.game.gghz.GghzBetLimitInfo;
import com.zyhy.lhj_server.game.gghz.GghzIconEnum;
import com.zyhy.lhj_server.game.gghz.GghzWindowType;
import com.zyhy.lhj_server.prcess.result.gghz.GghzGamePanlResult;

/**
 * @author linanjun
 * 打开游戏页面
 */
@Order
@Component
public class GghzOpenGamePanlProcess extends AbstractHttpMsgProcess{
	@Autowired
	private BgManagementServiceImp bgManagementServiceImp;
	public Map<Double, Integer> betenum = new HashMap<>();
	@Override
	public int getMessageId() {
		return MessageConstants.GGHZ_OPEN_GAME_PANL;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		GghzGamePanlResult result = new GghzGamePanlResult();
		
		// 获取后台管理赌注配置
		List<Double> betList = bgManagementServiceImp.queryBetList(MessageIdEnum.HYHZ.getRedisName());
		for (int i = 0; i < betList.size(); i++) {
			for (GghzBetEnum e : GghzBetEnum.values()) {
				if (e.getId() == i) {
					e.setBetcoin(betList.get(i));
					betenum.put(e.getBetcoin(), e.getId());
				}
			}
		}
		
		result.setBetlimitinfos(betinfos(ArrayUtils.subarray(GghzBetEnum.values(), 0,betList.size())));
		result.setWindowinfos(GghzWindowType.infos());
		result.setZooIconInfos(GghzIconEnum.infos());
		return result;
	}
	
	private List<GghzBetLimitInfo> betinfos(Bet[] bs) {
		List<GghzBetLimitInfo> is = new ArrayList<GghzBetLimitInfo>();
		for(Bet b : bs) {
			is.add(new GghzBetLimitInfo(b));
		}
		return is;
	}
}
