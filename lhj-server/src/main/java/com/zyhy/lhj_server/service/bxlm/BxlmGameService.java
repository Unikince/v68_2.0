/**
 * 
 */
package com.zyhy.lhj_server.service.bxlm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zyhy.common_lhj.BetInfo;
import com.zyhy.common_lhj.Icon;
import com.zyhy.common_lhj.Player;
import com.zyhy.common_lhj.WindowInfo;
import com.zyhy.common_lhj.logic.BaseLogic;
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.common_server.util.RandomUtil;
import com.zyhy.lhj_server.constants.Constants;
import com.zyhy.lhj_server.game.RewardInfo;
import com.zyhy.lhj_server.game.ShowRecordResult;
import com.zyhy.lhj_server.game.bxlm.BxlmIconEnum;
import com.zyhy.lhj_server.game.bxlm.BxlmReplenish;
import com.zyhy.lhj_server.game.bxlm.BxlmRollerWeightEnum;
import com.zyhy.lhj_server.game.bxlm.BxlmScatterInfo;
import com.zyhy.lhj_server.game.bxlm.BxlmWildEnum;
import com.zyhy.lhj_server.game.bxlm.BxlmWildLvEnum;
import com.zyhy.lhj_server.game.bxlm.BxlmWinIndex;
import com.zyhy.lhj_server.game.bxlm.poi.impl.BxlmTemplateService;
import com.zyhy.lhj_server.game.bxlm.poi.template.BxlmOdds;
import com.zyhy.lhj_server.prcess.bxlm.BxlmStartGameInfoProcess;
import com.zyhy.lhj_server.prcess.result.bxlm.BxlmGameBetResult;

/**
 * @author linanjun
 *
 */
@Service
public class BxlmGameService extends BaseLogic {

	@Autowired
	private StringRedisTemplate redisTemplate;
	@Autowired
	private BxlmTemplateService templateService;
	@Autowired
	private BxlmStartGameInfoProcess bxlmStartGameInfoProcess;

	// 是否已经计算免费游戏
	public boolean isScatter = false;
	// 上一局使用图标序列id
	public int lastRoundIconId = 0; 
	// 获奖窗口信息
	public List<List<WindowInfo>> wininfo = new ArrayList<List<WindowInfo>>();

	/**
	 * 
	 * @param iconId 
	 * @param jetton
	 *            档位
	 * @param betnum
	 *            下注的线路数
	 * @return
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public BxlmGameBetResult doGameProcess(String roleid, BetInfo betinfo, boolean isfree, BxlmScatterInfo bi, int iconId) throws ClassNotFoundException, IOException {
		BxlmGameBetResult res = new BxlmGameBetResult();
		// 获取图标
		List<BxlmOdds> baseinfos = templateService.getList(BxlmOdds.class);
		//System.out.println("加载进来的图标有:" + baseinfos);
		
		// 获取指定赔率图标
		Iterator<BxlmOdds> iterator = baseinfos.iterator();
		while (iterator.hasNext()) {
			BxlmOdds next = iterator.next();
			if (next.getType() != iconId) {
				iterator.remove();
			}
		}
		//System.out.println("加载进来的图标有:" + baseinfos);
		
		// 窗口信息
		List<List<WindowInfo>> list = new ArrayList<List<WindowInfo>>(5);

		List<Integer> wilds = new ArrayList<Integer>();
		// 是否是狂野的欲望
		boolean AllWild = false;
		boolean modelWild = false;
		isScatter = false;
		if (!isfree) {
			// 全是wild的概率
			int random = RandomUtil.getRandom(1, 100000);
			BxlmWildLvEnum e = BxlmWildLvEnum.getLvById(iconId);
			if (e == null) {
				e = BxlmWildLvEnum.D;
			}
			int count = 0;
			if (random > 100000 - e.getLv1()) {
				AllWild = true;
				count = 1;
				if (random > 100000 - e.getLv2()) {
					count = 2;
				}
				if (random > 100000 - e.getLv3()) {
					count = 3;
				}
				if (random > 100000 - e.getLv4()) {
					count = 4;
				}
				if (random > 100000 - e.getLv5()) {
					count = 5;
				}
			}
			
			if (count == 1) {
				int[] index = BxlmWildEnum.getById(RandomUtil.getRandom(1,5)).getIndex();
				for (int i : index) {
					wilds.add(i);
				}
			} else if (count == 2) {
				int[] index = BxlmWildEnum.getById(RandomUtil.getRandom(6,15)).getIndex();
				for (int i : index) {
					wilds.add(i);
				}
			} else if (count == 3) {
				int[] index = BxlmWildEnum.getById(RandomUtil.getRandom(16,24)).getIndex();
				for (int i : index) {
					wilds.add(i);
				}
			}else if (count == 4) {
				int[] index = BxlmWildEnum.getById(RandomUtil.getRandom(25,27)).getIndex();
				for (int i : index) {
					wilds.add(i);
				}
			} else if (count == 5) {
				modelWild = true;
				int[] index = BxlmWildEnum.getById(RandomUtil.getRandom(28,28)).getIndex();
				for (int i : index) {
					wilds.add(i);
				}
			}
			
			if (AllWild) {
				int i, j;
				for (i = 0; i <= 4; i++) {
					list.add(i, new ArrayList<WindowInfo>(3));
					for (j = 0; j <= 2; j++) {
						if (chickWild(wilds, i)) {
							// 直接赋值wild 
							if (!res.getGod().contains(i)) {
								res.getGod().add(i);
							}
							list.get(i).add(j, new WindowInfo(i, j , BxlmIconEnum.WILD));
						} else {
							list.get(i).add(j, BxlmRollerWeightEnum.windowInfo(i, j,list.get(i), baseinfos));
						}
					}
				}
			} else {
				int i, j;
				for (i = 0; i <= 4; i++) {
					list.add(i, new ArrayList<WindowInfo>(3));
					for (j = 0; j <= 2; j++) {
						list.get(i).add(j, BxlmRollerWeightEnum.windowInfo(i, j,list.get(i), baseinfos));
					}
				}
			}
		} else {
			// 免费游戏走正常流程
			if (bi != null && bi.getModel() == 4) {
				int i, j;
				if (modelWild) {
					for (i = 0; i <= 4; i++) {
						list.add(i, new ArrayList<WindowInfo>(3));
						for (j = 0; j <= 2; j++) {
							list.get(i).add(j, new WindowInfo(i, j , BxlmIconEnum.WILD));
						}
					}
				} else {
					for (i = 0; i <= 4; i++) {
						list.add(new ArrayList<WindowInfo>(3));
						for (j = 0; j <= 2; j++) {
							list.get(i).add(j, BxlmRollerWeightEnum.windowInfo(i, j,list.get(i), baseinfos));
						}
					}
				}
				res.setAllWild(modelWild);
			} else {
				int i, j;
				for (i = 0; i <= 4; i++) {
					list.add(new ArrayList<WindowInfo>(3));
					for (j = 0; j <= 2; j++) {
						list.get(i).add(j, BxlmRollerWeightEnum.windowInfo(i, j,list.get(i), baseinfos));
					}
				}
			}
		}
		List<List<WindowInfo>> deepCopy = deepCopy(list);
		res.setWindowinfos(deepCopy);

		// 获奖位置
		BxlmWinIndex wins = getWininfo(list, betinfo, isfree, bi, res);
		res.setRewardcoin(wins.getReward());
		res.setWin(wins.getWindows());

		// 判断是否进行免费旋转 狂野欲望不触发旋转
		if (!isfree && wins.getRewardType() == 2 && wins.getNum() != 0 && !AllWild) {
			res.setScatter(true);
		}

		if (AllWild) {
			wins.setAllWild(true);
			res.setIsWild(true);
			for (int i : wilds) {
				wins.getWild().add(i);
				wins.getRep().getWild().add(i);
			}
		}
		if (wins.getBat() > 1) {
			res.setBat(wins.getBat());
		}

		if (wins.getRep() != null && wins.isBxlmReplenish() && isfree) {
			wins.getRep().setFree(isfree);
			res.setRep(wins.getRep());
			res.setReplenish(wins.isBxlmReplenish());
		}
		return res;
	}

	/**
	 * 得到获胜线路信息
	 * 
	 * @param ws
	 * @param res 
	 * @param betnum
	 *            线路数
	 * @return
	 */
	private BxlmWinIndex getWininfo(List<List<WindowInfo>> ws, BetInfo betinfo, boolean isfree, BxlmScatterInfo bi, BxlmGameBetResult res) {
		BxlmWinIndex win = new BxlmWinIndex();
		// 总奖励
		// double all = 0;

		// 奖励免费次数
		int number = 0;
		int winNum = 0;
		int[] scatterNum = { 0, 1, 2, 3, 4 };
		// 中奖的图标
		//List<WindowInfo> winlist = new ArrayList<WindowInfo>();
		
		// 检查scatter奖励
		List<WindowInfo> scatter = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			if (chickScatter(ws.get(i)) != null) {
				scatter.add(chickScatter(ws.get(i)));
			}
		}
		// 检查是否有免费游戏
		for (int i = 0; i < 5; i++) {
			if (chickFree(ws.get(i))) {
				winNum++;
			}
		}
		
		if (winNum > 2 || (bi != null && bi.getModel() == 4 && winNum > 1) || (winNum > 2 && bi.getModel() == 1)) {
			isScatter = true;
			// all = +BxlmIconEnum.SCATTER.getTrs()[winNum - 1];
			number = scatterNum[winNum - 1];
		}

		// 仅仅只有scatter中奖
		boolean onlyScatter = true;
		//int num = 0;
		for (int j = 0; j < 3; j++) {
			// 检查前三列是否有中奖的
			if (this.chickList(ws.get(0), ws.get(0).get(j)) && this.chickList(ws.get(1), ws.get(0).get(j))
					&& this.chickList(ws.get(2), ws.get(0).get(j))) {
				//num = 3;
				// 检查第四列是否有中奖的
				if (this.chickList(ws.get(3), ws.get(0).get(j))) {
					//num++;
					// 检查第五列是否有中奖的
					if (this.chickList(ws.get(4), ws.get(0).get(j))) {
						//num++;
					}
				}
				// 奖励金币
				// double d = 0;

				if (ws.get(0).get(j).getIcon() != BxlmIconEnum.SCATTER) {
					onlyScatter = false;
					// d = ws.get(0).get(j).getIcon().getTrs()[num - 1];
				}
				//ws.get(0).get(j).setNum(num);
				//winlist.add(ws.get(0).get(j));
				// all += d;
			}
		}
		
		// 总奖励
		double totalReward = 0;
		List<RewardInfo> winInfo = winNum(ws);
		// 添加scatter中奖
		if (scatter.size() > 1) {
			RewardInfo r = new RewardInfo();
			r.setIcon(scatter.get(0).getIcon());
			r.setNum(1);
			r.setWinNum(scatter.size());
			r.setWinIcon(scatter);
			winInfo.add(r);
		}
		Iterator<RewardInfo> iterator = winInfo.iterator();
		while (iterator.hasNext()) {
			RewardInfo next = iterator.next();
			System.out.println("结算RewardInfo =====>" + next);
			if (next.getWinNum() > 0) {
				boolean iswild= false;
				boolean isScatter= false;
				for (WindowInfo wi : next.getWinIcon()) {
					if (wi.getIcon().isWild()) {
						iswild = true;
						break;
					}
					if (wi.getIcon().isScatter()) {
						isScatter = true;
						break;
					}
				}
				if (iswild) { // wild图标奖励(线注*硬币数*赔率倍数*中奖线数*2)
					double reward = NumberTool.multiply(
							NumberTool.multiply(betinfo.total() * 2, next.getIcon().getTrs()[next.getWinNum() - 1]),next.getNum()).doubleValue();
					System.out.println("wild结算加倍 =====> 线注x2 : "+ betinfo.total() * 2 +" , 赔率倍数 : "+ next.getIcon().getTrs()[next.getWinNum() - 1] 
									+ " , 硬币数: "+ next.getNum() +" !");
					totalReward += reward;
					next.setReward(reward);
				} else {
					if (isScatter) { // scatter图标奖励(总赌注*赔率倍数*中奖线数)
						double reward = NumberTool.multiply(NumberTool.multiply(betinfo.getTotalBet(), next.getIcon().getTrs()[next.getWinNum() - 1]),next.getNum()).doubleValue();
						totalReward += reward;
						next.setReward(reward);
					} else { // 普通图标奖励(线注*硬币数*赔率倍数*中奖线数)
						double reward = NumberTool.multiply(NumberTool.multiply(betinfo.total(), next.getIcon().getTrs()[next.getWinNum() - 1]),next.getNum()).doubleValue();
						totalReward += reward;
						next.setReward(reward);
					}
				}
			}else {
				iterator.remove();
			}
		}

		// 免费游戏根据模式来获得总奖励
		if (isfree && bi != null) {
			switch (bi.getModel()) {
			case 1:
				// 免费模式1,奖励*5倍
				win.setReward(NumberTool.multiply(totalReward, 5).doubleValue());
				// 免费次数叠加
				if (winNum > 2) {
					bi.setNum(bi.getNum() + 10);
				}
				// 更新获奖信息中的奖励
				for (RewardInfo rewardInfo : winInfo) {
					rewardInfo.setReward(NumberTool.multiply(rewardInfo.getReward(), 5).doubleValue());
				}
				break;
			case 2:
				// 免费模式2,随机奖励倍数
				int bat = RandomUtil.getRandom(1, 6);
				win.setReward(NumberTool.multiply(totalReward, bat).doubleValue());
				if (bat > 1) {
					win.setBat(bat);
				}
				// 更新获奖信息中的奖励
				for (RewardInfo rewardInfo : winInfo) {
					rewardInfo.setReward(NumberTool.multiply(rewardInfo.getReward(), bat).doubleValue());
				}
				break;
			case 3:
				win.setReward(totalReward);
				break;
			case 4:
				win.setReward(totalReward);
				if (winNum > 2) {
					bi.setNum(bi.getNum() + number + 25);
				} else if (number > 0) {
					bi.setNum(bi.getNum() + number);
				}
				break;
			default:
				break;
			}
		} else {
			win.setReward(totalReward);
		}
		// 保存奖励信息
		res.setRewardInfo(winInfo);
		win.setRewardType(2);
		win.setNum(number);
		// 进行消除
		List<List<WindowInfo>> winList = new ArrayList<List<WindowInfo>>(5);

		if (totalReward > 0) {
			winList = winIcon(winInfo ,ws);
	/*		int i, j;
			for (i = 0; i < 5; i++) {
				winList.add(new ArrayList<WindowInfo>(3) {
					private static final long serialVersionUID = 1L;
					{
						add(null);
						add(null);
						add(null);
					}
				});
				if (i <= max) {
					for (j = 0; j < 3; j++) {
						if (i == 0) {
							if (chickList(winlist, ws.get(i).get(j))
									|| ws.get(i).get(j).getIcon() == BxlmIconEnum.WILD) {
								winList.get(i).set(j, ws.get(i).get(j));
								ws.get(i).get(j).setIndex(99);
							}
						} else {
							if ((chickList(winlist, ws.get(i).get(j)) && chickList(ws.get(i - 1), ws.get(i).get(j)))
									|| ws.get(i).get(j).getIcon() == BxlmIconEnum.WILD) {
								winList.get(i).set(j, ws.get(i).get(j));
								ws.get(i).get(j).setIndex(99);
							}
						}
					}
				}
			}*/
		}

		// 是否会补充数据
		BxlmReplenish rep = new BxlmReplenish(betinfo);
		if (totalReward > 0 && !onlyScatter && isfree && bi.getModel() == 3) {
			rep.setBxlmReplenish(true);
			rep.setNumber(0);
			rep.setFree(isfree);
			win.setBxlmReplenish(true);
			wininfo = ws;
			lastRoundIconId = bxlmStartGameInfoProcess.getLastRoundIconId();
		} else {
			rep.setBxlmReplenish(false);
			rep.setNumber(0);
			// isBxlmReplenish = false;
			win.setBxlmReplenish(false);
		}
		win.setRep(rep);
		win.setWindows(winList);
		return win;
	}

	/**
	 * 获胜线路数量
	 * 
	 * @return
	 *//*
	public List<RewardInfo> winNum(List<List<WindowInfo>> ws) {
		List<RewardInfo> ri = new ArrayList<>();
		for (int i = 0; i <= 2; i++) {
			List<List<WindowInfo>> wiLlist = new ArrayList<>();
			RewardInfo ari = new RewardInfo();
			List<WindowInfo> icon = new ArrayList<>();
			for (int j = 1; j <= 4; j++) {
				List<WindowInfo> list = checkIcon(ws.get(0).get(i), ws.get(j),ws,wiLlist);
				wiLlist.add(j - 1 , list);
			}
			
			
			
			if (!wiLlist.isEmpty()) {
				System.out.println("wilistsize =====>" + wiLlist.size());
				int total = 0;
				if (wiLlist.get(0).size() != 0 && wiLlist.get(1).size() != 0) {
					total = wiLlist.get(0).size() * wiLlist.get(1).size();
					icon.add(ws.get(0).get(i));
					icon.addAll(wiLlist.get(0));
					icon.addAll(wiLlist.get(1));
					ari.setWinNum(3);
					if (wiLlist.get(2).size() != 0) {
						total = total * wiLlist.get(2).size();
						icon.addAll(wiLlist.get(2));
						ari.setWinNum(4);
						if (wiLlist.get(3).size() != 0) {
							total = total * wiLlist.get(3).size();
							icon.addAll(wiLlist.get(3));
							ari.setWinNum(5);
						}
					}
					ari.setNum(total);
					chickWild3(icon);
					ari.setWinIcon(icon);
					for (WindowInfo wl : icon) {
						Icon icon2 = wl.getIcon();
						if (icon2 != BxlmIconEnum.WILD) {
							ari.setIcon(icon2);
							break;
						} 
						ari.setIcon(icon.get(0).getIcon());
					}
					ri.add(ari);
				}
			}
		}
		return ri;
	}*/
	
	/**
	 * 获胜线路数量
	 * 
	 * @return
	 */
	public List<RewardInfo> winNum(List<List<WindowInfo>> ws) {
		List<RewardInfo> ri = new ArrayList<>();
		for (int i = 0; i <= 2; i++) {
			List<List<WindowInfo>> wiLlist = new ArrayList<>();
			for (int j = 1; j <= 4; j++) {
				List<WindowInfo> list = checkIcon(ws.get(0).get(i), ws.get(j),ws,wiLlist);
				wiLlist.add(j - 1 , list);
			}
			//if (ws.get(0).get(i).getIcon().isWild()) {
				List<List<List<WindowInfo>>> checkOther = checkOther(wiLlist);
				List<RewardInfo> rewardInfoList = addRewardInfo(ws.get(0).get(i),checkOther);
				ri.addAll(rewardInfoList);
				//continue;
			//}
			
/*			if (!wiLlist.isEmpty()) {
				int total = 0;
				if (wiLlist.get(0).size() != 0 && wiLlist.get(1).size() != 0) {
					total = wiLlist.get(0).size() * wiLlist.get(1).size();
					icon.add(ws.get(0).get(i));
					icon.addAll(wiLlist.get(0));
					icon.addAll(wiLlist.get(1));
					ari.setWinNum(3);
					if (wiLlist.get(2).size() != 0) {
						total = total * wiLlist.get(2).size();
						icon.addAll(wiLlist.get(2));
						ari.setWinNum(4);
						if (wiLlist.get(3).size() != 0) {
							total = total * wiLlist.get(3).size();
							icon.addAll(wiLlist.get(3));
							ari.setWinNum(5);
						}
					}
					ari.setNum(total);
					chickWild3(icon);
					ari.setWinIcon(icon);
					for (WindowInfo wl : icon) {
						Icon icon2 = wl.getIcon();
						if (icon2 != BxlmIconEnum.WILD) {
							ari.setIcon(icon2);
							break;
						} 
						ari.setIcon(icon.get(0).getIcon());
					}
					ri.add(ari);
				}
			}*/
		}
		return ri;
	}
	
	/**
	 * 添加奖励信息
	 */
	private List<RewardInfo> addRewardInfo(WindowInfo wi,List<List<List<WindowInfo>>> checkOther){
		List<RewardInfo> ri = new ArrayList<>();
		if (checkOther.size() > 0) {
			for (List<List<WindowInfo>> wiLlist : checkOther) {
				if (!wiLlist.isEmpty()) {
					int total = 0;
					RewardInfo ari = new RewardInfo();
					List<WindowInfo> icon = new ArrayList<>();
					if (wiLlist.get(0).size() != 0 && wiLlist.get(1).size() != 0) {
						total = wiLlist.get(0).size() * wiLlist.get(1).size();
						icon.add(wi);
						icon.addAll(wiLlist.get(0));
						icon.addAll(wiLlist.get(1));
						ari.setWinNum(3);
						if (wiLlist.get(2).size() != 0) {
							total = total * wiLlist.get(2).size();
							icon.addAll(wiLlist.get(2));
							ari.setWinNum(4);
							if (wiLlist.get(3).size() != 0) {
								total = total * wiLlist.get(3).size();
								icon.addAll(wiLlist.get(3));
								ari.setWinNum(5);
							}
						}
						ari.setNum(total);
						chickWild3(icon);
						ari.setWinIcon(icon);
						for (WindowInfo wl : icon) {
							Icon icon2 = wl.getIcon();
							if (icon2 != BxlmIconEnum.WILD) {
								ari.setIcon(icon2);
								break;
							} 
							ari.setIcon(icon.get(0).getIcon());
						}
						ri.add(ari);
					}
				}
			}
		}
		return ri;
	}

	/**
	 * 检查图标是否相同
	 * @param wiLlist 
	 * 
	 * @param awi
	 * @param b
	 * @return
	 */
	
	public List<WindowInfo> checkIcon(WindowInfo w, List<WindowInfo> wiList,List<List<WindowInfo>> ws, List<List<WindowInfo>> winList) {
		Set<WindowInfo> list = new HashSet<>();
		// 被对比的图标
		Icon icon = w.getIcon();
		// 检查前一列是否有wild图标
		int id = wiList.get(0).getId();
		
		if (icon.isWild()){
			if (id == 1) {  // 如果是第二列,就与第三列比较
				for (int i = 0; i < 3; i++) {
					icon = ws.get(2).get(i).getIcon();
					for (WindowInfo wi1 : wiList) {
						 if ((wi1.getIcon() == icon || wi1.getIcon().isWild() || checkWildIcon(ws.get(2))) && !icon.isScatter()) { // 有与第3列相同或者是wild的或者第三列有wild就算中奖
								list.add(wi1);
						}
					}
				}
			} else { // 其他列取出前一列中奖的图标进行比较
				List<WindowInfo> list2 = null;
				for (List<WindowInfo> WindowInfoList : winList) {
					if (WindowInfoList.size() > 0) {
						if (WindowInfoList.get(0).getId() == id - 1) {
							list2 = WindowInfoList;
							break;
						}
					}
				}
				
				if (list2 != null) {
					for (WindowInfo windowInfo : list2) {
						icon = windowInfo.getIcon();
						for (WindowInfo wi1 : wiList) {
							 if ((wi1.getIcon() == icon || wi1.getIcon().isWild() || icon.isWild()) && !icon.isScatter()) {
									list.add(wi1);
							}
						}
					}
				}
			}
			List<WindowInfo> temp = new ArrayList<>();
			for (WindowInfo windowInfo : list) {
				temp.add(windowInfo);
			}
			return temp;
		}
		
		// 不是wild就正常比较
		for (WindowInfo wi : wiList) {
			if ((wi.getIcon() == icon || wi.getIcon().isWild()) && !icon.isScatter()) {
				list.add(wi);
			}
		}
		List<WindowInfo> temp = new ArrayList<>();
		for (WindowInfo windowInfo : list) {
			temp.add(windowInfo);
		}
		return temp;
	}
	
	/**
	 * 检查一列中是否有wild图标
	 * @return
	 */
	private boolean checkWildIcon(List<WindowInfo> list){
		for (WindowInfo wi : list) {
			if (wi.getIcon().isWild()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 将图标单独分组
	 * @param windowInfo 
	 * @param list
	 * @return
	 */
	private List<List<List<WindowInfo>>> checkOther(List<List<WindowInfo>> wiLists){
		List<List<List<WindowInfo>>> winList = new ArrayList<>();
		//System.out.println("传入windowInfoList =====> " + wiLists);
		//System.out.println("传入windowInfoList.size =====> " + wiLists.size());
		// 找出中奖的图标
		Set<String> iconList = new HashSet<>();
		for (List<WindowInfo> list : wiLists) {
			for (WindowInfo wi : list) {
				if (!wi.getIcon().isWild() && !wi.getIcon().isScatter() && !wi.getIcon().IsBonus()) {
					iconList.add(wi.getIcon().getName());
				}
			}
		}
		
		//System.out.println("一共有哪些图标 ======>" + iconList);
		
		if (iconList.size() > 0) {
			for (String icon : iconList) {
				List<List<WindowInfo>> wiLists1 = new ArrayList<>();
				for (List<WindowInfo> list : wiLists) {
					List<WindowInfo> wiList = new ArrayList<>();
					for (WindowInfo wi : list) {
						if ((wi.getIcon().getName().equals(icon) || wi.getIcon().isWild())) {
							wiList.add(wi);
						}
					}
					wiLists1.add(wiList);
				}
				winList.add(wiLists1);
			}
		}
		System.out.println("传出winList =====>" + winList);
		//System.out.println("传出winList.size =====>" + winList.size());
		return winList;
	}
	
	/**
	 * 返回中奖图标
	 * @param awi
	 * @param b
	 * @return
	 */
	public List<List<WindowInfo>> winIcon(List<RewardInfo> winInfo , List<List<WindowInfo>> list) {
		List<List<WindowInfo>> winList = new ArrayList<List<WindowInfo>>(5);
		for (int i = 0; i <= 4; i++) {
			winList.add(new ArrayList<WindowInfo>(3) {
				private static final long serialVersionUID = 1L;
				{
					add(null);
					add(null);
					add(null);
				}
			});
		}
		//addFree(list,winList);
		for (RewardInfo re : winInfo) {
			if(re.getWinIcon() != null){
				for (WindowInfo wi : re.getWinIcon()) {
					wi.setNum(99);
					winList.get(wi.getId()).set(wi.getIndex(), wi);
				}
			}
		}
		
		
		return winList;
	}
	
	/**
	 * 添加scatter图标
	 * @param list
	 * @param list2 
	 * @return
	 */
	public List<List<WindowInfo>> addFree(List<List<WindowInfo>> list, List<List<WindowInfo>> list2) {
		List<WindowInfo> list1 = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			if (chickScatter(list.get(i)) == null) {
				break;
			}
			list1.add(chickScatter(list.get(i)));
		}
		
		if (list.size() != 0 && list1.size() > 2) {
			for (WindowInfo wi : list1) {
				list2.get(wi.getId()).set(wi.getIndex(), wi);
			}
		}
		return list2;
	}
	
	/**
	 * 添加wild图标
	 * @param list
	 * @param list2 
	 * @return
	 */
	public List<List<WindowInfo>> addWild(List<WindowInfo> list, List<List<WindowInfo>> list2) {
		List<WindowInfo> list1 = new ArrayList<>();
		for (WindowInfo w : list) {
			if (w.getIcon() == BxlmIconEnum.WILD) {
				list1.add(w);
			}
		}
		if (list.size() != 0 && list1.size() > 2) {
			for (WindowInfo wi : list1) {
				list2.get(wi.getId()).set(wi.getIndex(), wi);
			}
		}
		return list2;
	}

	public void chickWild3(List<WindowInfo> w) {
		List<WindowInfo> ws = new ArrayList<>();
		for (WindowInfo windowInfo : w) {
			if (ws.size() > 0) {
				WindowInfo windowInfo2 = ws.get(ws.size() -1);
				if (windowInfo.getId() == windowInfo2.getId()) {
					continue;
				}
			}
			ws.add(windowInfo);
		}
		
		int wild = 0;
		for (WindowInfo wi : ws) {
			if (!wi.getIcon().isWild()) {
				break;
			}
			wild ++;
		}
		
		if (wild == 3 && ws.size() > 3) {
			//System.out.println("传进来的ws=========================" + w);
			double wildlv = BxlmIconEnum.WILD.getTrs()[2];
			double iconlv = 0;
			if (ws.size() == 4) {
				iconlv = ws.get(ws.size()-1).getIcon().getTrs()[3];
				if (wildlv > iconlv) {
					ws.remove(3);
					w.clear();
					w.addAll(ws);
					//System.out.println("传出去的ws=========================" + w);
				}
			}
			if (ws.size() == 5) {
				iconlv = ws.get(ws.size() -1).getIcon().getTrs()[4];
				if (wildlv > iconlv) {
					ws.remove(3);
					ws.remove(4);
					w.clear();
					w.addAll(ws);
					//System.out.println("传出去的ws=========================" + w);
				}
			}
		} 
		if (wild == 4 && ws.size() > 4) {
			//System.out.println("传进来的ws=========================" + w);
			double wildlv =BxlmIconEnum.WILD.getTrs()[3];
			double iconlv = ws.get(4).getIcon().getTrs()[3];
			if (wildlv > iconlv) {
				ws.remove(4);
				w.clear();
				w.addAll(ws);
				//System.out.println("传出去的ws=========================" + w);
			}
		}
	}
	
	/**
	 * 免费游戏数据缓存
	 */
	//private Map<String, BxlmScatterInfo> freeData = new ConcurrentHashMap<>();
	
	/**
	 * 保存免费游戏次数
	 * 
	 * @param number
	 * @param roleid
	 */
	public void save(BxlmScatterInfo bi, String roleid, Player userinfo, String uuid) {
		//freeData.put(roleid, bi);
		try {
			if (Constants.PLATFORM_EBET.equals(uuid)) {
				if (userinfo.getTourist() == -1) {
					redisTemplate.opsForValue().set(Constants.BXQY_REDIS_SCATTER + roleid + "|" + uuid,
							JSONObject.toJSONString(bi),Constants.RECORD_TIME_1HOUR, TimeUnit.SECONDS);
				}else {
					redisTemplate.opsForValue().set(Constants.BXQY_REDIS_SCATTER + roleid + "|" + uuid,
							JSONObject.toJSONString(bi),Constants.RECORD_TIME_30DAY, TimeUnit.SECONDS);
				}
			} else if (Constants.PLATFORM_V68.equals(uuid)) {
				if (userinfo.getTourist() == 1) {
					redisTemplate.opsForValue().set(Constants.BXQY_REDIS_SCATTER + roleid + "|" + uuid,
							JSONObject.toJSONString(bi),Constants.RECORD_TIME_1HOUR, TimeUnit.SECONDS);
				}else {
					redisTemplate.opsForValue().set(Constants.BXQY_REDIS_SCATTER + roleid + "|" + uuid,
							JSONObject.toJSONString(bi),Constants.RECORD_TIME_30DAY, TimeUnit.SECONDS);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取免费游戏信息
	 * @param roleid
	 * @param uuid
	 * @return
	 */
	public BxlmScatterInfo getData(String roleid, String uuid) {
		/*BxlmScatterInfo bxlmScatterInfo = freeData.remove(roleid);
		if (bxlmScatterInfo != null) {
			return bxlmScatterInfo;
		}*/
		
		try {
			String str = redisTemplate.opsForValue().get(Constants.BXQY_REDIS_SCATTER + roleid + "|" + uuid);
			if (str != null) {
				BxlmScatterInfo bi = JSONObject.parseObject(str, BxlmScatterInfo.class);
				return bi;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 删除免费游戏次数
	 * 
	 * @param number
	 * @param roleid
	 */
	public void deleteInfo(String roleid, String uuid) {
		//freeData.remove(roleid);
		try {
			redisTemplate.delete(Constants.BXQY_REDIS_SCATTER + roleid + "|" + uuid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean chickList(List<WindowInfo> list, WindowInfo info) {
		for (WindowInfo w : list) {
			if (w.getIcon() == info.getIcon()
					|| (w.getIcon() == BxlmIconEnum.WILD && info.getIcon() != BxlmIconEnum.SCATTER)) {
				return true;
			}
		}

		return false;
	}

	public WindowInfo chickScatter(List<WindowInfo> list) {
		for (WindowInfo w : list) {
			if (w.getIcon() == BxlmIconEnum.SCATTER) {
				w.setNum(99);
				return w;
			}
		}
		return null;
	}
	
	/**
	 * 检查免费游戏
	 * @param list
	 * @return
	 */
	public boolean chickFree(List<WindowInfo> list) {
		for (WindowInfo w : list) {
			if (w.getIcon() == BxlmIconEnum.SCATTER) {
				return true;
			}
		}
		return false;
	}


	public boolean chickWild(List<Integer> list, int i) {
		for (int w : list) {
			if (w == i) {
				return true;
			}
		}
		return false;
	}

	public boolean chickWild(List<WindowInfo> list) {
		for (WindowInfo w : list) {
			if (w.getIcon() == BxlmIconEnum.WILD) {
				return true;
			}
		}

		return false;
	}
	
	/**
	 * 深度复制list
	 * 
	 * @param src
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	protected <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(byteOut);
		out.writeObject(src);

		ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
		ObjectInputStream in = new ObjectInputStream(byteIn);
		@SuppressWarnings("unchecked")
		List<T> dest = (List<T>) in.readObject();
		return dest;
	}
	
	/**
	 * 用于后台显示的胜利信息
	 */
	public void winInfosForBgView(BxlmGameBetResult result,ShowRecordResult srr){
		List<String> windows = new ArrayList<>(); // 窗口信息
		Map<String, Double> winLine = new HashMap<>(); // 获胜线路信息
		List<List<WindowInfo>> windowinfos = result.getWindowinfos();
		for (int i = 0; i < 3; i++) {
			for (List<WindowInfo> list : windowinfos) {
				windows.add(list.get(i).getIcon().getName());
			}
		}
		
		if (result.getRewardcoin() > 0) {
			winLine.put(srr.getRecordType(), result.getRewardcoin());
		}
		srr.setWindows(windows);
		srr.setWinLine(winLine);
		srr.setViewType(15);
	}
	
}
