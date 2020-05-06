/**
 * 
 */
package com.zyhy.lhj_server.prcess.tgpd;

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
import com.zyhy.lhj_server.constants.Constants;
import com.zyhy.lhj_server.constants.MessageConstants;
import com.zyhy.lhj_server.game.tgpd.TgpdBetEnum;
import com.zyhy.lhj_server.game.tgpd.TgpdIconEnum;
import com.zyhy.lhj_server.game.tgpd.TgpdReplenish;
import com.zyhy.lhj_server.game.tgpd.TgpdWindowEnum;
import com.zyhy.lhj_server.prcess.result.tgpd.TgpdGameBetResult;
import com.zyhy.lhj_server.prcess.result.tgpd.TgpdGamePanlResult;
import com.zyhy.lhj_server.service.tgpd.TgpdGameService;

/**
 * @author linanjun
 * 打开游戏页面
 */
@Order
@Component
public class TgpdOpenGamePanlProcess extends AbstractHttpMsgProcess{
	@Autowired
	private BgManagementServiceImp bgManagementServiceImp;
	@Autowired
	private TgpdGameService gameService;
	public Map<Double, Integer> betenum = new HashMap<>();
	@Override
	public int getMessageId() {
		return MessageConstants.TGPD_OPEN_GAME_PANL;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		TgpdGamePanlResult result = new TgpdGamePanlResult();
		
		// 获取后台管理赌注配置
		List<Double> betList = bgManagementServiceImp.queryBetList(MessageIdEnum.TGPD.getRedisName());
		for (int i = 0; i < betList.size(); i++) {
			for (TgpdBetEnum e : TgpdBetEnum.values()) {
				if (e.getId() == i) {
					e.setBetcoin(betList.get(i));
					betenum.put(e.getBetcoin(), e.getId());
				}
			}
		}
				
		// 返回信息
		result.setBetlimitinfos(betinfos(ArrayUtils.subarray(TgpdBetEnum.values(), 0,betList.size())));
		result.setWindowinfos(windowinfos(TgpdWindowEnum.values()));
		result.setZooIconInfos(iconinfos(TgpdIconEnum.values()));
		String roleid = body.get("roleid");
		
		TgpdReplenish rep = gameService.getReplenish(roleid, uuid);
		
		if(rep != null ){
			//清除掉落数据
			gameService.delFreeInfoCache(roleid, Constants.REDIS_LHJ_TGPD_REPLENISH, uuid);
		}
		
		// 判断是否正常退出
		if(gameService.checkHeartInfo(roleid, uuid)){
			TgpdGameBetResult oldResult = gameService.getGameInfo(roleid, uuid);
			if (oldResult != null) {
				if (oldResult.getScatter() != null) {
					double lastbet = oldResult.getScatter().getBetInfo().getGold();
					if (betenum.containsKey(lastbet)) {
						oldResult.getScatter().setBetLine( betenum.get(lastbet));
					} else {
						betenum.put(lastbet, betenum.size());
						oldResult.getScatter().setBetLine( betenum.size());
					}
				}
			}
			result.setResult(oldResult);
		} else {
			gameService.delFreeInfoCache(roleid, Constants.REDIS_LHJ_TGPD_FREE, uuid);
			gameService.delFreeInfoCache(roleid, Constants.REDIS_LHJ_TGPD_GAMEINFO, uuid);
		}
		
		//判断是否免费游戏中
		//TgpdScatterInfo scatter = (TgpdScatterInfo) gameService.getInfo(
				//new TgpdScatterInfo(), roleid, Constants.REDIS_LHJ_TGPD_FREE);
		//if(scatter != null && scatter.getNum() > 0){
			//result.setScatter(scatter);
		//}
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
