/**
 * 
 */
package com.zyhy.lhj_server.prcess.lll;

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
import com.zyhy.lhj_server.game.lll.LllBetEnum;
import com.zyhy.lhj_server.game.lll.LllBetLimitInfo;
import com.zyhy.lhj_server.game.lll.LllIconEnum;
import com.zyhy.lhj_server.game.lll.LllWindowType;
import com.zyhy.lhj_server.prcess.result.lll.LllGamePanlResult;

/**
 * @author linanjun
 * 打开游戏页面
 */
@Order
@Component
public class LllOpenGamePanlProcess extends AbstractHttpMsgProcess{
	@Autowired
	private BgManagementServiceImp bgManagementServiceImp;
	public Map<Double, Integer> betenum = new HashMap<>();
	@Override
	public int getMessageId() {
		return MessageConstants.LLL_OPEN_GAME_PANL;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		LllGamePanlResult result = new LllGamePanlResult();
		
		// 获取后台管理赌注配置
		List<Double> betList = bgManagementServiceImp.queryBetList(MessageIdEnum.LLL.getRedisName());
		for (int i = 0; i < betList.size(); i++) {
			for (LllBetEnum e : LllBetEnum.values()) {
				if (e.getId() == i) {
					e.setBetcoin(betList.get(i));
					betenum.put(e.getBetcoin(), e.getId());
				}
			}
		}
		
		result.setBetlimitinfos(betinfos(ArrayUtils.subarray(LllBetEnum.values(), 0,betList.size())));
		result.setWindowinfos(LllWindowType.infos());
		result.setZooLllIconInfos(LllIconEnum.infos());
		return result;
	}
	private List<LllBetLimitInfo> betinfos(Bet[] bs) {
		List<LllBetLimitInfo> is = new ArrayList<LllBetLimitInfo>();
		for(Bet b : bs) {
			is.add(new LllBetLimitInfo(b));
		}
		return is;
	}
}
