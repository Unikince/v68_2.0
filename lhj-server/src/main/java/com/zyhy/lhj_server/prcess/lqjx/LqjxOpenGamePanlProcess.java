/**
 * 
 */
package com.zyhy.lhj_server.prcess.lqjx;

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
import com.zyhy.lhj_server.game.lqjx.LqjxBetEnum;
import com.zyhy.lhj_server.game.lqjx.LqjxIconEnum;
import com.zyhy.lhj_server.game.lqjx.LqjxScatterInfo;
import com.zyhy.lhj_server.game.lqjx.LqjxWindowEnum;
import com.zyhy.lhj_server.prcess.result.lqjx.LqjxGamePanlResult;
import com.zyhy.lhj_server.service.lqjx.LqjxGameService;
import com.zyhy.lhj_server.service.lqjx.LqjxReplenishService;

/**
 * @author linanjun
 * 打开游戏页面
 */
@Order
@Component
public class LqjxOpenGamePanlProcess extends AbstractHttpMsgProcess{
	@Autowired
	private BgManagementServiceImp bgManagementServiceImp;
	public Map<Double, Integer> betenum = new HashMap<>();
	@Autowired
	private LqjxGameService gameService;
	@Autowired
	private LqjxReplenishService replenishService;
	@Override
	public int getMessageId() {
		return MessageConstants.LQJX_OPEN_GAME_PANL;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		LqjxGamePanlResult result = new LqjxGamePanlResult();
		
		// 获取后台管理赌注配置
		List<Double> betList = bgManagementServiceImp.queryBetList(MessageIdEnum.LQJX.getRedisName());
		for (int i = 0; i < betList.size(); i++) {
			for (LqjxBetEnum e : LqjxBetEnum.values()) {
				if (e.getId() == i) {
					e.setBetcoin(betList.get(i));
					betenum.put(e.getBetcoin(), e.getId());
				}
			}
		}
		
		result.setBetlimitinfos(betinfos(ArrayUtils.subarray(LqjxBetEnum.values(), 0,betList.size())));
		result.setWindowinfos(windowinfos(LqjxWindowEnum.values()));
		result.setZooIconInfos(iconinfos(LqjxIconEnum.values()));
		String roleid = body.get("roleid");
		
		//判断是否免费游戏中
		LqjxScatterInfo scatter = gameService.getData(roleid, uuid);
		
		//清除掉落数据
		replenishService.delete(roleid, uuid);
		
		if(scatter != null && scatter.getNum() > 0){
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
