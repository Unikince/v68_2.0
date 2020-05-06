/**
 * 
 */
package com.zyhy.lhj_server.prcess.bxlm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.zyhy.lhj_server.game.bxlm.BxlmBetEnum;
import com.zyhy.lhj_server.game.bxlm.BxlmIconEnum;
import com.zyhy.lhj_server.game.bxlm.BxlmScatterInfo;
import com.zyhy.lhj_server.game.bxlm.BxlmWindowEnum;
import com.zyhy.lhj_server.prcess.result.bxlm.BxlmGamePanlResult;
import com.zyhy.lhj_server.service.bxlm.BxlmGameService;
import com.zyhy.lhj_server.service.bxlm.BxlmReplenishService;

/**
 * @author linanjun
 * 打开游戏页面
 */
@Order
@Component
public class BxlmOpenGamePanlProcess extends AbstractHttpMsgProcess{
	@Autowired
	private BgManagementServiceImp bgManagementServiceImp;
	public Map<Double, Integer> betenum = new HashMap<>();
	@Autowired
	private BxlmGameService gameService;
	@Autowired
	private BxlmReplenishService replenishService;
	private static final Logger LOG = LoggerFactory.getLogger(BxlmOpenGamePanlProcess.class);
	@Override
	public int getMessageId() {
		return MessageConstants.BXLM_OPEN_GAME_PANL;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		BxlmGamePanlResult result = new BxlmGamePanlResult();
		
		// 获取后台管理赌注配置
		List<Double> betList = bgManagementServiceImp.queryBetList(MessageIdEnum.BXQY.getRedisName());
		LOG.info("betList =====>" + betList);
		for (int i = 0; i < betList.size(); i++) {
			for (BxlmBetEnum e : BxlmBetEnum.values()) {
				if (e.getId() == i) {
					e.setBetcoin(betList.get(i));
					betenum.put(e.getBetcoin(), e.getId());
				}
			}
		}
		
		
		result.setBetlimitinfos(betinfos(ArrayUtils.subarray(BxlmBetEnum.values(), 0,betList.size())));
		result.setWindowinfos(windowinfos(BxlmWindowEnum.values()));
		result.setZooIconInfos(iconinfos(BxlmIconEnum.values()));
		String roleid = body.get("roleid");
		//判断是否免费游戏中
		BxlmScatterInfo data = gameService.getData(roleid, uuid);
		if(data != null ){
			double lastbet = data.getBetInfo().getGold();
			if (betenum.containsKey(lastbet)) {
				data.setBetLine( betenum.get(lastbet));
			} else {
				betenum.put(lastbet, betenum.size());
				data.setBetLine( betenum.size());
			}
			result.setScatter(data);
		}
		//清除掉落数据
		replenishService.delete(roleid, uuid);
		return result;
	}
	
	private List<BetLimitInfo> betinfos(Bet[] bs) {
		List<BetLimitInfo> is = new ArrayList<BetLimitInfo>();
		for(Bet b : bs) {
			is.add(new BetLimitInfo(b));
		}
		return is;
	}
	
	private List<List<Window>> windowinfos(Window[] ws){
		List<List<Window>> ls = new ArrayList<List<Window>>();
		int i,j;
		for(i = 0;i < 5; i++){
			ls.add(new ArrayList<Window>());
			for(j = 0;j < 3;j++){
				ls.get(i).add(new WindowInfo(ws[i*j]));
			}
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
