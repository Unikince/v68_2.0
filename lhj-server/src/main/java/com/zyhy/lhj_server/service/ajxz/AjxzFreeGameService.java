/**
 * 
 */
package com.zyhy.lhj_server.service.ajxz;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zyhy.common_lhj.BetInfo;
import com.zyhy.common_lhj.Icon;
import com.zyhy.common_lhj.WinLineInfo;
import com.zyhy.common_lhj.Window;
import com.zyhy.common_lhj.WindowInfo;
import com.zyhy.common_lhj.logic.BaseLogic;
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.lhj_server.game.ajxz.AjxzFreeInfo;
import com.zyhy.lhj_server.game.ajxz.AjxzIconEnum;
import com.zyhy.lhj_server.game.ajxz.AjxzRollerWeightEnum;
import com.zyhy.lhj_server.game.ajxz.poi.impl.AjxzTemplateService;
import com.zyhy.lhj_server.game.ajxz.poi.template.AjxzOdds;
import com.zyhy.lhj_server.prcess.result.ajxz.AjxzGameBetResult;


@Service
public class AjxzFreeGameService extends BaseLogic {

	@Autowired
	private AjxzGameService gameService;
	@Autowired
	private AjxzTemplateService templateService;
	
	/**
	 * @param freeInfo 
	 * @param iconId 
	 * @param rep 
	 * @param jetton
	 *            档位
	 * @param betnum
	 *            下注的线路数
	 * @return
	 */
	public AjxzGameBetResult doGameProcess(String roleid, BetInfo betinfo, AjxzFreeInfo freeInfo, int iconId) {
		AjxzGameBetResult res = getResult(betinfo,roleid , freeInfo,iconId);
		return res;
	}
	
	private AjxzGameBetResult getResult(BetInfo betinfo, String roleid, AjxzFreeInfo freeInfo, int iconId) {
		AjxzGameBetResult res = new AjxzGameBetResult();
		int type = freeInfo.getType();
		// 获取图标
		List<AjxzOdds> baseinfos = templateService.getList(AjxzOdds.class);
		//System.out.println("加载进来的图标有:" + baseinfos);
		// 获取指定赔率图标
		Iterator<AjxzOdds> iterator = baseinfos.iterator();
		while (iterator.hasNext()) {
			AjxzOdds next = iterator.next();
			if (next.getType() != iconId) {
				iterator.remove();
			}
		}
		//System.out.println("加载进来的图标有:" + baseinfos);
		// 窗口信息
		List<Window> ws = new ArrayList<Window>();
		for (int i = 1; i <= 5; i++) {
			ws.addAll(AjxzRollerWeightEnum.windowInfo(i,baseinfos));
		}
		if (type == 5 && gameService.reswindow.size() > 0) {
			ws = gameService.reswindow;
		}
		// 是否需要重取图标
		if (gameService.checkReplenish(ws)) {
			ws = refetchIcon(ws , type,baseinfos);
			if (type == 5 && freeInfo.getFreeNum_5() != 1) {
				gameService.reswindow = ws;
			} else {
				gameService.reswindow = new ArrayList<>();
			}
		}
		// 获取线路信息
		List<WinLineInfo> wins = gameService.getWininfo(ws, betinfo);
		res.setWindowinfos(ws);
		res.setWinrouteinfos(wins);
		
		// 是否有免费游戏
		boolean isFree = gameService.CkickScatter(ws);
		res.setFree(isFree);
		
		// 计算奖励
		double totalReward = 0;
		if (wins != null && wins.size() > 0) {
			// 总的奖励数
			for (WinLineInfo w : wins) {
				double reward = NumberTool.multiply(w.getReward(), betinfo.getGold()).doubleValue();
				w.setReward(reward);
				totalReward += reward;
			}
		}
		res.setRewardcoin(totalReward);
		return res;
	}
	
	/**
	 * 重新取图标
	 * @param ws
	 * @param type 
	 */
	@SuppressWarnings("unchecked")
	private List<Window> refetchIcon(List<Window> ws, int type, List<AjxzOdds> baseinfos) {
		List<List<WindowInfo>> format = gameService.formatDouble(ws);
		for (int i = 0; i < format.size(); i++) {
			boolean isWild = false;
			List<WindowInfo> list = format.get(i);
			for (WindowInfo wi : list) {
				if (wi.getIcon() == AjxzIconEnum.WILD) {
					isWild = true;
				}
			}
			
			if (!isWild && type == 5) {
				format.set(i, (List<WindowInfo>)AjxzRollerWeightEnum.windowInfo(list.get(0).getId(),baseinfos));
			}
			
			if (isWild) {
				if (type == 5 ) {
					for (WindowInfo wi : list) {
						int id = wi.getId();
						if (wi.getIcon() == AjxzIconEnum.WILD) {
							switch (wi.getIndex()) {
							case 1:
								Icon icon12 = format.get(id-1).get(1).getIcon();
								Icon icon13 = format.get(id-1).get(2).getIcon();
								if (icon12 != AjxzIconEnum.WILD) {
									format.get(id-1).set(1, AjxzRollerWeightEnum.windowInfo(id , 2, baseinfos));
								}
								if (icon13 != AjxzIconEnum.WILD) {
									format.get(id-1).set(2, AjxzRollerWeightEnum.windowInfo(id , 3, baseinfos));
								}
								break;
							case 2:
								Icon icon21 = format.get(id-1).get(0).getIcon();
								Icon icon23 = format.get(id-1).get(2).getIcon();
								if (icon21 != AjxzIconEnum.WILD) {
									format.get(id-1).set(0, AjxzRollerWeightEnum.windowInfo(id , 1, baseinfos));
								}
								if (icon23 != AjxzIconEnum.WILD) {
									format.get(id-1).set(2, AjxzRollerWeightEnum.windowInfo(id , 3, baseinfos));
								}
								break;
							case 3:
								Icon icon31 = format.get(id-1).get(0).getIcon();
								Icon icon32 = format.get(id-1).get(1).getIcon();
								if (icon31 != AjxzIconEnum.WILD) {
									format.get(id-1).set(0, AjxzRollerWeightEnum.windowInfo(id , 1, baseinfos));
								}
								if (icon32 != AjxzIconEnum.WILD) {
									format.get(id-1).set(1, AjxzRollerWeightEnum.windowInfo(id , 2, baseinfos));
								}
								break;
							default:
								break;
							}
						}
					}
				}
				
				if (type == 10) {
					for (WindowInfo wi : list) {
						wi.setIcon(AjxzIconEnum.WILD);
					}
				}
				
			if (type == 15) {
				for (WindowInfo wi : list) {
					int id = wi.getId();
					if (wi.getIcon() == AjxzIconEnum.WILD) {
						switch (wi.getIndex()) {
						case 1:
							Icon icon12 = format.get(id-1).get(1).getIcon();
							Icon icon13 = format.get(id-1).get(2).getIcon();
							if (icon12 != AjxzIconEnum.WILD) {
								format.get(id-1).set(1, AjxzRollerWeightEnum.windowInfo(id , 2, baseinfos));
							}
							if (icon13 != AjxzIconEnum.WILD) {
								format.get(id-1).set(2, AjxzRollerWeightEnum.windowInfo(id , 3, baseinfos));
							}
							break;
						case 2:
							format.get(id-1).get(0).setIcon(AjxzIconEnum.WILD);
							Icon icon23 = format.get(id-1).get(2).getIcon();
							if (icon23 != AjxzIconEnum.WILD) {
								format.get(id-1).set(2, AjxzRollerWeightEnum.windowInfo(id , 3, baseinfos));
							}
							break;
						case 3:
							format.get(id-1).get(0).setIcon(AjxzIconEnum.WILD);
							format.get(id-1).get(1).setIcon(AjxzIconEnum.WILD);
							break;
						default:
							break;
						}
					}
				}
			}
			}
			
			
			
			
		/*	if (!isWild) {
				format.set(i, (List<WindowInfo>)AjxzRollerWeightEnum.windowInfo(list.get(0).getId()));
			} else {
				if (type == 5 ) {
					for (WindowInfo wi : list) {
						int id = wi.getId();
						if (wi.getIcon() == AjxzIconEnum.WILD) {
							switch (wi.getIndex()) {
							case 1:
								Icon icon12 = format.get(id-1).get(1).getIcon();
								Icon icon13 = format.get(id-1).get(2).getIcon();
								if (icon12 != AjxzIconEnum.WILD) {
									format.get(id-1).set(1, AjxzRollerWeightEnum.windowInfo(id , 2));
								}
								if (icon13 != AjxzIconEnum.WILD) {
									format.get(id-1).set(2, AjxzRollerWeightEnum.windowInfo(id , 3));
								}
								break;
							case 2:
								Icon icon21 = format.get(id-1).get(0).getIcon();
								Icon icon23 = format.get(id-1).get(2).getIcon();
								if (icon21 != AjxzIconEnum.WILD) {
									format.get(id-1).set(0, AjxzRollerWeightEnum.windowInfo(id , 1));
								}
								if (icon23 != AjxzIconEnum.WILD) {
									format.get(id-1).set(2, AjxzRollerWeightEnum.windowInfo(id , 3));
								}
								break;
							case 3:
								Icon icon31 = format.get(id-1).get(0).getIcon();
								Icon icon32 = format.get(id-1).get(1).getIcon();
								if (icon31 != AjxzIconEnum.WILD) {
									format.get(id-1).set(0, AjxzRollerWeightEnum.windowInfo(id , 1));
								}
								if (icon32 != AjxzIconEnum.WILD) {
									format.get(id-1).set(1, AjxzRollerWeightEnum.windowInfo(id , 2));
								}
								break;
							default:
								break;
							}
						}
					}
				}
				
			
			}*/
		}
		List<Window> formatSingle = gameService.formatSingle(format);
		return formatSingle;
	}
}
