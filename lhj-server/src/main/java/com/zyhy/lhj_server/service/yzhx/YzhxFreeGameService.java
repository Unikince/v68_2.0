/**
 * 
 */
package com.zyhy.lhj_server.service.yzhx;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zyhy.common_lhj.BetInfo;
import com.zyhy.common_lhj.WinLineInfo;
import com.zyhy.common_lhj.Window;
import com.zyhy.common_lhj.logic.BaseLogic;
import com.zyhy.lhj_server.game.yzhx.YzhxRollerWeightEnum;
import com.zyhy.lhj_server.game.yzhx.poi.impl.YzxhTemplateService;
import com.zyhy.lhj_server.game.yzhx.poi.template.YzhxOdds;
import com.zyhy.lhj_server.prcess.result.yzhx.YzhxGameBetResult;


@Service
public class YzhxFreeGameService extends BaseLogic {

	@Autowired
	private YzhxGameService gameService;
	@Autowired
	private YzxhTemplateService templateService;
	/**
	 * @param iconId 
	 * @param rep 
	 * @param jetton
	 *            档位
	 * @param betnum
	 *            下注的线路数
	 * @return
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public YzhxGameBetResult doGameProcess(String roleid, BetInfo betInfo, int iconId) throws ClassNotFoundException, IOException {
		YzhxGameBetResult res = getResult(betInfo,roleid,iconId);
		return res;
	}
	
	private YzhxGameBetResult getResult(BetInfo betinfo, String roleid, int iconId) throws ClassNotFoundException, IOException {
		// 获取图标
		List<YzhxOdds> baseinfos = templateService.getList(YzhxOdds.class);
		//System.out.println("加载进来的图标有:" + baseinfos);
		
		// 获取指定赔率图标
		Iterator<YzhxOdds> iterator = baseinfos.iterator();
		while (iterator.hasNext()) {
			YzhxOdds next = iterator.next();
			if (next.getType() != iconId) {
				iterator.remove();
			}
		}
		//System.out.println("加载进来的图标有:" + baseinfos);
		
		// 窗口信息
		YzhxGameBetResult res = new YzhxGameBetResult();
		List<Window> ws = new ArrayList<Window>();
		for (int i = 1; i <= 5; i++) {
			ws.addAll(YzhxRollerWeightEnum.windowInfo(i,baseinfos));
		}
		// 检验wild出现的位置以及类型
		List<Window> ws1 = gameService.checkWild(ws,baseinfos);
		List<Integer> wildType = gameService.wildType(ws1);
		res.setWindowinfos(ws1);
		res.setWildType(wildType);
		
		// 重取图标
		List<Window> deepCopy = gameService.deepCopy(ws1);
		List<Window> refetchIcon = gameService.refetchIcon(deepCopy);
		// 获胜线路信息
		List<WinLineInfo> wins = gameService.getWininfo(refetchIcon, betinfo);
		res.setWinrouteinfos(wins);
		//检测是否中了免费游戏
		int ckickScatter = gameService.CkickScatter(ws);
		int[] scatterNum = { 0, 0, 15, 25, 50 };
		if (ckickScatter > 2) {
			res.setFreeNum(scatterNum[ckickScatter - 1]);
		}
		
		// 计算奖励
		BigDecimal reward = new BigDecimal(0);
		if (wins != null && wins.size() > 0) {
			// 总的奖励数
			for (WinLineInfo w : wins) {
				reward = reward.add(new BigDecimal(w.getReward() + ""));
			}
		}
		res.setRewardcoin(reward.doubleValue());
		return res;
	}
}
