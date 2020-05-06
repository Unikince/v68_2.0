package com.zyhy.lhj_server.service.tgpd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zyhy.common_lhj.WinLineInfo;
import com.zyhy.common_lhj.Window;
import com.zyhy.common_lhj.logic.BaseLogic;
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.common_server.util.RandomUtil;
import com.zyhy.lhj_server.bgmanagement.service.imp.BgManagementServiceImp;
import com.zyhy.lhj_server.game.tgpd.TgpdDorpInfo;
import com.zyhy.lhj_server.game.tgpd.TgpdIconEnum;
import com.zyhy.lhj_server.game.tgpd.TgpdReplenish;
import com.zyhy.lhj_server.game.tgpd.WindowInfo;
import com.zyhy.lhj_server.prcess.result.tgpd.TgpdGameBetResult;

/**
 * @author DPC
 * @version 创建时间：2019年2月27日 上午10:58:02
 */
@Service
public class TgpdReplenishService extends BaseLogic {
	@Autowired
	private BgManagementServiceImp bgManagementServiceImp;
	@Autowired
	private TgpdGameService gameService;
	/**
	 * 
	 * @param freeGame 
	 * @param jetton
	 *            档位
	 * @param betnum
	 *            下注的线路数
	 * @return
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public TgpdGameBetResult doGameProcess(String roleid, TgpdReplenish rep, boolean isfree) throws ClassNotFoundException, IOException {
		TgpdGameBetResult res = new TgpdGameBetResult();
		// 窗口信息
		List<List<WindowInfo>> list = gameService.windowInfo;
		List<List<WindowInfo>> dropList = gameService.dropWindowInfo;
		List<List<WindowInfo>> winInfos = gameService.winInfos;
		int num = list.size();
		//int type = num == 4 ? 1 : num == 5 ? 2 : num == 6 ? 3 : 1;
		
		// 掉落
		for (List<WindowInfo> list2 : winInfos) {
			for (WindowInfo wi : list2) {
				if (wi != null) {
					list.get(wi.getId()).set(wi.getIndex(), new WindowInfo(wi.getId(), 99));
				}
			}
		}
		for (List<WindowInfo> list2 : list) {
			int id = list2.get(0).getId();
			Iterator<WindowInfo> iterator = list2.iterator();
			while (iterator.hasNext()) {
				WindowInfo wi = iterator.next();
				if (wi.getIndex() == 99) {
					iterator.remove();
				}
			}
			while (list2.size() < num) {
				list2.add(0, dropList.get(id).remove(0));
			}
		}
		
		// 整理图标索引
		for (List<WindowInfo> list2 : list) {
			for (int i = 0; i < num; i++) {
				list2.get(i).setIndex(i);
			}
		}
		
		// 检查是否中奖池游戏
		Map<String, Double> checkPoolGame = bgManagementServiceImp.checkPoolGame(18);
		// 添加奖池游戏中奖图标
		if (checkPoolGame.size() > 0) {
			for (int i = 0; i < checkPoolGame.size(); i++) {
				List<WindowInfo> list2 = list.get(RandomUtil.getRandom(0, list.size() -1));
				WindowInfo windowInfo2 = list2.get(RandomUtil.getRandom(0, list2.size()-1));
				windowInfo2.setIcon(TgpdIconEnum.POOL);
			}
		}
		
		List<List<WindowInfo>> deepCopy = gameService.deepCopy(list);
		res.setWindowinfos(deepCopy);
		res.setDropWindowInfos(dropList);
		
		// 检查免费游戏
		boolean checkFree = false;
		List<WindowInfo> checkFreeList = gameService.checkFree(deepCopy);
		if (!isfree) {
			if (checkFreeList.size() > 0) {
				checkFree = true;
			}
		}
		
		// 检查奖池游戏
		boolean checkPool = false;
		List<WindowInfo> checkPoolList = gameService.checkPool(deepCopy);
		if (checkPoolList.size() > 0) {
			checkPool = true;
			res.setPoolCount(checkPoolList.size());
		}
		
		// 检查升级道具
		List<WindowInfo> checkPropsList = gameService.checkProps(deepCopy);
		// 获取到获胜信息
		List<WinLineInfo> winInfo = gameService.getWinInfo(list,num,checkPropsList);
		// 计算奖励
		double reward = 0;
		if (winInfo.size() > 0) {
			for (WinLineInfo wl : winInfo) {
				double singleReward = NumberTool.multiply(
						NumberTool.multiply(NumberTool.multiply(rep.getBetInfo().getGold(), 0.1), rep.getBetInfo().getNum()), wl.getReward()).doubleValue();
				wl.setReward(singleReward);
				reward += singleReward;
			}
		}
		
		// 返回获奖图标
		List<WindowInfo> winIcon = new ArrayList<>();
		for (WinLineInfo wl : winInfo) {
			List<WindowInfo> temp = new ArrayList<>();
			if (wl.getWindows() != null) {
				for (Window window : wl.getWindows()) {
					WindowInfo wi = (WindowInfo) window;
					temp.add(wi);
				}
			}
			winIcon.addAll(temp);
		}
		if (checkFreeList.size() > 0) {
			winIcon.addAll(checkFreeList);
		}
		if (checkPoolList.size() > 0) {
			winIcon.addAll(checkPoolList);
		}
		if (checkPropsList.size() > 0) {
			winIcon.addAll(checkPropsList);
		}
		List<List<WindowInfo>> winList = gameService.winIcon(winIcon , num);
		// 是否掉落
		boolean checkReplenish = false;
		if (winIcon.size() > 0) {
			checkReplenish = true;
			gameService.windowInfo = deepCopy;
			gameService.dropWindowInfo = dropList;
			gameService.winInfos = winList;
		}
		res.setWinLine(winInfo);
		res.setWinrouteinfos(winList);
		res.setFree(checkFree);
		res.setPoolReward(checkPool);
		res.setPropsNum(checkPropsList.size());
		res.setReplenish(checkReplenish);
		res.setRewardcoin(reward);
		return res;
	}

}
