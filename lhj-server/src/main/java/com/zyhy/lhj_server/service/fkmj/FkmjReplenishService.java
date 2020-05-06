package com.zyhy.lhj_server.service.fkmj;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zyhy.common_lhj.BetInfo;
import com.zyhy.common_lhj.WinLineInfo;
import com.zyhy.common_lhj.Window;
import com.zyhy.common_lhj.logic.BaseLogic;
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.lhj_server.game.fkmj.FkmjBonusInfo;
import com.zyhy.lhj_server.game.fkmj.FkmjIconEnum;
import com.zyhy.lhj_server.game.fkmj.FkmjRollerWeightEnum;
import com.zyhy.lhj_server.game.fkmj.FkmjWindowInfo;
import com.zyhy.lhj_server.game.fkmj.poi.impl.FkmjTemplateService;
import com.zyhy.lhj_server.game.fkmj.poi.template.FkmjOdds;
import com.zyhy.lhj_server.prcess.result.fkmj.FkmjGameBetResult;

/**
 * @author DPC
 * @version 创建时间：2019年2月27日 上午10:58:02
 */
@Service
public class FkmjReplenishService extends BaseLogic {

	@Autowired
	private FkmjGameService gameService;
	@Autowired
	private FkmjTemplateService templateService;
	/**
	 * 
	 * @param isfree 
	 * @param bi 
	 * @param iconId 
	 * @param jetton
	 *            档位
	 * @param betnum
	 *            下注的线路数
	 * @return
	 */
	public FkmjGameBetResult doGameProcess(String roleid, BetInfo betinfo, boolean isfree, FkmjBonusInfo bi, int iconId) {
		FkmjGameBetResult res = getResult(betinfo, roleid , isfree , bi,iconId);
		return res;
	}
	
	private FkmjGameBetResult getResult(BetInfo betinfo, String roleid, boolean isfree, FkmjBonusInfo bi, int iconId) {
		FkmjGameBetResult res = new FkmjGameBetResult();
		// 获取图标
		List<FkmjOdds> baseinfos = templateService.getList(FkmjOdds.class);
		//System.out.println("加载进来的图标有:" + baseinfos);
		
		// 获取指定赔率图标
		Iterator<FkmjOdds> iterator = baseinfos.iterator();
		while (iterator.hasNext()) {
			FkmjOdds next = iterator.next();
			if (next.getType() != iconId) {
				iterator.remove();
			}
		}
		//System.out.println("加载进来的图标有:" + baseinfos);
		
		// 窗口信息
		List<Window> list = gameService.wInfo;
		// 获胜窗口
		List<WinLineInfo> winInfo = gameService.winInfo;
		
		// 替换获胜图标
		List<Window> replaceWinIcon = replaceWinIcon(list, winInfo);
		// 图标掉落
		List<Window> iconDrop = iconDrop(replaceWinIcon,baseinfos);
		res.setWindowinfos(iconDrop);
		
		List<WinLineInfo> wininfo2 = gameService.getWininfo(iconDrop, betinfo);
		
		//检测scatter奖励
		WinLineInfo ckickSatter = gameService.CkickSatter(iconDrop);
		if (ckickSatter != null) {
			wininfo2.add(ckickSatter);
		}
		// 设置bonus图标线路用于破碎
		List<Window> ckickBonus = gameService.CkickBonus(iconDrop);
		if (ckickBonus != null) {
			WinLineInfo scatterWin = new WinLineInfo();
			scatterWin.setIcon(FkmjIconEnum.BONUS);
			scatterWin.setNum(3);
			scatterWin.setId("bonus");
			scatterWin.setWindows(ckickBonus);
			wininfo2.add(scatterWin);
		}
		res.setWinrouteinfos(wininfo2);
		
		// 计算奖励
		double totalReward = 0;
		if (wininfo2 != null && wininfo2.size() > 0) {
			// 总的奖励数
			for (WinLineInfo w : wininfo2) {
				double reward = 0;
				if (w.getIcon() == FkmjIconEnum.SCATTER) {
					reward = NumberTool.multiply(betinfo.total(), w.getReward()).doubleValue();
				} else {
					reward = NumberTool.multiply(betinfo.getGold(), w.getReward()).doubleValue();
				}
				w.setReward(reward);
				totalReward += reward;
			}
			// 免费游戏根据连续中奖次数来获得总奖励
			if (isfree && bi != null) {
				if (bi.getCount() > 5) {
					res.setFreeRewardcoin(NumberTool.multiply(totalReward,
							5).doubleValue());
				} 
				switch (bi.getCount()) {
				case 1:
					res.setFreeRewardcoin(NumberTool.multiply(totalReward,
							1).doubleValue());
					break;
				case 2:
					res.setFreeRewardcoin(NumberTool.multiply(totalReward,
							2).doubleValue());
					break;
				case 3:
					res.setFreeRewardcoin(NumberTool.multiply(totalReward,
							3).doubleValue());
					break;
				case 4:
					res.setFreeRewardcoin(NumberTool.multiply(totalReward,
							4).doubleValue());
					break;
				case 5:
					res.setFreeRewardcoin(NumberTool.multiply(totalReward,
							5).doubleValue());
					break;
				}
			} else {
				// 正常游戏奖励
				res.setRewardcoin(totalReward);
			}
		}
		
		// 是否掉落
		if(totalReward > 0 || ckickBonus != null){
			res.setDrop(true);
			gameService.wInfo = iconDrop;
			gameService.winInfo = wininfo2;
		}
		return res;
	}
	
	/**
	 * 图标掉落
	 * @param baseinfos 
	 * @param maxReward 
	 * @param rollerList 
	 * @param i 
	 * @return 
	 */
	private List<Window> iconDrop(List<Window> ws, List<FkmjOdds> baseinfos) {
		// 转换格式
		List<List<FkmjWindowInfo>> allList = new ArrayList<>();
		for (int i = 1; i <= 5; i++) {
			List<FkmjWindowInfo> list = new ArrayList<>();
			for (Window w : ws) {
				FkmjWindowInfo wi1 = (FkmjWindowInfo) w;
				if (wi1.getId() == i) {
					list.add(wi1);
				}
			}
			allList.add(list);
		}
		// 掉落
		for (int i = 1; i <= 5; i++) {
			for (Window w : ws) {
				FkmjWindowInfo wi = (FkmjWindowInfo) w;
				if (wi.getId() == i && wi.isWinIcon()) {
					int id = wi.getId();
					int index = wi.getIndex();
					switch (index) {
					case 1:
						FkmjWindowInfo w12 = (FkmjWindowInfo) allList.get(id-1).get(1);
						FkmjWindowInfo w13 = (FkmjWindowInfo) allList.get(id-1).get(2);
						allList.get(id-1).set(0, FkmjRollerWeightEnum.windowInfo(id, 1,allList.get(id-1),baseinfos));
						if (w12.isWinIcon() && w13.isWinIcon()) {
							allList.get(id-1).set(1, FkmjRollerWeightEnum.windowInfo(id, 2,allList.get(id-1),baseinfos));
							allList.get(id-1).set(2, FkmjRollerWeightEnum.windowInfo(id, 3,allList.get(id-1),baseinfos));
							break;
						} 
						if (w12.isWinIcon()) {
							allList.get(id-1).set(1, FkmjRollerWeightEnum.windowInfo(id, 2,allList.get(id-1),baseinfos));
							break;
						}
						if (w13.isWinIcon()) {
							allList.get(id-1).set(2, allList.get(id-1).get(1));
							w13 = (FkmjWindowInfo) allList.get(id-1).get(2);
							w13.setIndex(3);
							allList.get(id-1).set(1, FkmjRollerWeightEnum.windowInfo(id, 2,allList.get(id-1),baseinfos));
							break;
						}
						break;
					case 2:
						FkmjWindowInfo w21 = (FkmjWindowInfo) allList.get(id-1).get(0);
						FkmjWindowInfo w23 = (FkmjWindowInfo) allList.get(id-1).get(2);
						allList.get(id-1).set(1, FkmjRollerWeightEnum.windowInfo(id, 2,allList.get(id-1),baseinfos));
						if (!w21.isWinIcon() && !w23.isWinIcon()) {
							allList.get(id-1).set(1, allList.get(id-1).get(0));
							FkmjWindowInfo w22 = (FkmjWindowInfo) allList.get(id-1).get(1);
							w22.setIndex(2);
							allList.get(id-1).set(0, FkmjRollerWeightEnum.windowInfo(id, 1,allList.get(id-1),baseinfos));
							break;
						}
						if (w21.isWinIcon() && w23.isWinIcon()) {
							allList.get(id-1).set(0, FkmjRollerWeightEnum.windowInfo(id, 1,allList.get(id-1),baseinfos));
							allList.get(id-1).set(2, FkmjRollerWeightEnum.windowInfo(id, 3,allList.get(id-1),baseinfos));
							break;
						}
						if (w21.isWinIcon()) {
							allList.get(id-1).set(0, FkmjRollerWeightEnum.windowInfo(id, 1,allList.get(id-1),baseinfos));
							break;
						}
						if (w23.isWinIcon()) {
							allList.get(id-1).set(2, allList.get(id-1).get(0));
							FkmjWindowInfo w22 = (FkmjWindowInfo) allList.get(id-1).get(2);
							w22.setIndex(3);
							allList.get(id-1).set(0, FkmjRollerWeightEnum.windowInfo(id, 1,allList.get(id-1),baseinfos));
							break;
						}
						break;
					case 3:
						FkmjWindowInfo w31= (FkmjWindowInfo) allList.get(id-1).get(0);
						FkmjWindowInfo w32 = (FkmjWindowInfo) allList.get(id-1).get(1);
						allList.get(id-1).set(2, FkmjRollerWeightEnum.windowInfo(id, 3,allList.get(id-1),baseinfos));
						if (!w31.isWinIcon() && !w32.isWinIcon()) {
							allList.get(id-1).set(2, allList.get(id-1).get(1));
							FkmjWindowInfo w33 = (FkmjWindowInfo) allList.get(id-1).get(2);
							w33.setIndex(3);
							allList.get(id-1).set(1, allList.get(id-1).get(0));
							w32 = (FkmjWindowInfo) allList.get(id-1).get(1);
							w32.setIndex(2);
							allList.get(id-1).set(0, FkmjRollerWeightEnum.windowInfo(id, 1,allList.get(id-1),baseinfos));
							break;
						}
						if (w31.isWinIcon() && w32.isWinIcon()) {
							allList.get(id-1).set(0, FkmjRollerWeightEnum.windowInfo(id, 1,allList.get(id-1),baseinfos));
							allList.get(id-1).set(1, FkmjRollerWeightEnum.windowInfo(id, 2,allList.get(id-1),baseinfos));
							break;
						}
						if (w31.isWinIcon()) {
							allList.get(id-1).set(2, allList.get(id-1).get(1));
							FkmjWindowInfo w33 = (FkmjWindowInfo) allList.get(id-1).get(2);
							w33.setIndex(3);
							allList.get(id-1).set(0, FkmjRollerWeightEnum.windowInfo(id, 1,allList.get(id-1),baseinfos));
							allList.get(id-1).set(1, FkmjRollerWeightEnum.windowInfo(id, 2,allList.get(id-1),baseinfos));
							break;
						}
						if (w32.isWinIcon()) {
							allList.get(id-1).set(2, allList.get(id-1).get(0));
							FkmjWindowInfo w33 = (FkmjWindowInfo) allList.get(id-1).get(2);
							w33.setIndex(3);
							allList.get(id-1).set(0, FkmjRollerWeightEnum.windowInfo(id, 1,allList.get(id-1),baseinfos));
							allList.get(id-1).set(1, FkmjRollerWeightEnum.windowInfo(id, 2,allList.get(id-1),baseinfos));
							break;
						}
						break;
					default:
						break;
					}
					break;
				}
			}
		}
		// 格式转换
		List<Window> list = new ArrayList<>();
		for (List<FkmjWindowInfo> wilist : allList) {
			for (FkmjWindowInfo wi : wilist) {
				Window w = wi;
				list.add(w);
			}
		}
		return list;
	}
	
	/**
	 * 替换中奖图标
	 * @return 
	 */
	public List<Window> replaceWinIcon(List<Window> ws , List<WinLineInfo> wins){
		if (wins.size() > 0) {
			for (WinLineInfo wl : wins) {
				for (int i = 0; i < wl.getNum(); i++) {
					FkmjWindowInfo wi = (FkmjWindowInfo) wl.getWindows().get(i);
					wi.setWinIcon(true);
				}
			}
		}
		List<FkmjWindowInfo> allWi = new ArrayList<>();
		for (WinLineInfo wl : wins) {
			List<Window> windows = wl.getWindows();
			for (int i = 0; i < wl.getNum(); i++) {
				allWi.add((FkmjWindowInfo) windows.get(i));
			}
		}
		for (Window w : ws) {
			FkmjWindowInfo wi1 = (FkmjWindowInfo) w;
			for (FkmjWindowInfo ew : allWi) {
				FkmjWindowInfo wi2 = (FkmjWindowInfo) ew;
				if (wi1.getId() == wi2.getId() 
						&& wi1.getIndex() == wi2.getIndex() 
						&& wi1.getIcon() == wi2.getIcon()) {
					wi1 = wi2;
				}
			}
		}
		return ws;
	}
	
	/**
	 * 转化类型
	 */
	@SuppressWarnings("unchecked")
	public List<?>  converType(List<?> ws) {
		if (ws.size() > 5) {
			List<List<FkmjWindowInfo>> converList = new ArrayList<>(5);
			for (int i = 0; i < 5; i++) {
				List<FkmjWindowInfo> list = new ArrayList<>(3);
				for (Object w : ws) {
					FkmjWindowInfo wi = (FkmjWindowInfo) w;
					if (wi.getId() == i + 1) {
						wi.setId(i);
						list.add(wi);
					}
				}
				converList.add(list);	
			}
		return converList;
		} else {
			List<FkmjWindowInfo> converList = new ArrayList<>();
			for (Object wiList : ws) {
				List<FkmjWindowInfo> wiList1 = (List<FkmjWindowInfo>) wiList;
				for (FkmjWindowInfo win : wiList1) {
					win.setId(win.getId() + 1);
					converList.add(win);
				}
			}
			return converList;
		}
	}
}
