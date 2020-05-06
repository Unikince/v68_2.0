package com.zyhy.lhj_server.service.bxlm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zyhy.common_lhj.WindowInfo;
import com.zyhy.common_lhj.logic.BaseLogic;
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.lhj_server.constants.Constants;
import com.zyhy.lhj_server.game.RewardInfo;
import com.zyhy.lhj_server.game.bxlm.BxlmIconEnum;
import com.zyhy.lhj_server.game.bxlm.BxlmReplenish;
import com.zyhy.lhj_server.game.bxlm.BxlmRollerWeightEnum;
import com.zyhy.lhj_server.game.bxlm.BxlmWinIndex;
import com.zyhy.lhj_server.game.bxlm.poi.impl.BxlmTemplateService;
import com.zyhy.lhj_server.game.bxlm.poi.template.BxlmOdds;
import com.zyhy.lhj_server.prcess.result.bxlm.BxlmGameBetResult;

/**
 * @author DPC
 * @version 创建时间：2019年2月27日 上午10:58:02
 */
@Service
public class BxlmReplenishService extends BaseLogic {
	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private BxlmGameService gameService;
	@Autowired
	private BxlmTemplateService templateService;

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
	public BxlmGameBetResult doGameProcess(String roleid, BxlmReplenish rep, int iconId) throws ClassNotFoundException, IOException {
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
		List<List<WindowInfo>> list = gameService.wininfo;
		if (list.size() == 0) {
			int i, j;
			for (i = 0; i <= 4; i++) {
				list.add(new ArrayList<WindowInfo>(3));
				for (j = 0; j <= 2; j++) {
					if (rep.getWild() != null && rep.getWild().size() > 0) {
						if (i == rep.getWild().get(rep.getWild().size() - 1)) {
							rep.getWild().remove(rep.getWild().size() - 1);
							list.get(i).add(j,
									new WindowInfo(i, j , BxlmIconEnum.WILD));
						} else {
							list.get(i).add(j,
									BxlmRollerWeightEnum.windowInfo(i, j,list.get(i),baseinfos));
						}
					} else {
						list.get(i).add(j, BxlmRollerWeightEnum.windowInfo(i, j,list.get(i),baseinfos));
					}
				}
			}
		} else {
			int i, j;
			for (j = 2; j >= 0; j--) {
				for (i = 4; i >= 0; i--) {
					if (list.get(i).get(j).getNum() == 99) {
						list.get(i).remove(j);
					}
				}
			}
			for (i = 0; i <= 4; i++) {
				for (j = list.get(i).size(); j <= 2; j++) {
					if (rep.getWild() != null && rep.getWild().size() > 0) {
						if (rep.getWild().contains(i)) {
							list.get(i).add(j,
									new WindowInfo(i, j , BxlmIconEnum.WILD));
						} else {
							list.get(i).add(j,
									BxlmRollerWeightEnum.windowInfo(i, j,list.get(i),baseinfos));
						}
					} else {
						list.get(i).add(j, BxlmRollerWeightEnum.windowInfo(i, j,list.get(i),baseinfos));
					}
				}
			}
		}
		
		// 整理图标索引
		for (List<WindowInfo> list2 : list) {
			for (int i = 0; i < 3; i++) {
				list2.get(i).setIndex(i);
			}
		}
		List<List<WindowInfo>> deepCopy = gameService.deepCopy(list);
		res.setWindowinfos(deepCopy);
		// 获奖位置
		BxlmWinIndex wins = getWininfo(list, rep,res, iconId);
		
		if (wins.getRep() != null && wins.getRep().getNumber() > 1) {
			// 连续掉落奖励
			res.setRewardcoin(NumberTool.multiply(wins.getReward(),wins.getRep().getNumber()).doubleValue());
			// 更新奖励信息中的奖励
			for (RewardInfo rewardInfo : res.getRewardInfo()) {
				rewardInfo.setReward(NumberTool.multiply(rewardInfo.getReward(),wins.getRep().getNumber()).doubleValue());
			}
		} else {
			res.setRewardcoin(wins.getReward());
		}

		res.setWin(wins.getWindows());

		// 判断是否进行免费旋转
		if (!rep.isFree() && wins.getRewardType() == 2 && wins.getNum() != 0) {
			res.setScatter(true);
			res.setScatterNum(wins.getNum());
		}
		
		res.setReplenish(wins.isBxlmReplenish());
		res.setRep(wins.getRep());
		return res;
	}

	/**
	 * 得到获胜线路信息
	 * @param res 
	 */
	private BxlmWinIndex getWininfo(List<List<WindowInfo>> ws, BxlmReplenish rep, BxlmGameBetResult res,int iconId) {
		BxlmWinIndex win = new BxlmWinIndex();
		// 总奖励
		//double all = 0;

		// 奖励免费次数
		int number = 0;
		// int winNum = 0;
		// int[] scatterNum = { 0, 0, 15, 20, 20 };
		// 中奖的图标
		//List<WindowInfo> winlist = new ArrayList<WindowInfo>();
		// // 检查是否有免费游戏
		// for (int i = 0; i < 5; i++) {
		// if (gameService.chickFree(ws.get(i)) && !gameService.isScatter) {
		// winNum++;
		// }
		// }
		// if (winNum > 2) {
		// gameService.isScatter = true;
		// all = +BxlmIconEnum.SCATTER.getTrs()[winNum - 1];
		// number = scatterNum[winNum - 1];
		// }

		// 仅仅只有scatter中奖
		boolean onlyScatter = true;

		for (int j = 0; j < 3; j++) {
			// 检查前三列是否有中奖的
			if (gameService.chickList(ws.get(0), ws.get(0).get(j))
					&& gameService.chickList(ws.get(1), ws.get(0).get(j))
					&& gameService.chickList(ws.get(2), ws.get(0).get(j))) {
				//int num = 3;
				// 检查第四列是否有中奖的
				if (gameService.chickList(ws.get(3), ws.get(0).get(j))) {
					//num++;
					// 检查第五列是否有中奖的
					if (gameService.chickList(ws.get(4), ws.get(0).get(j))) {
						//num++;
					}
				}
				// 奖励金币
				//double d = 0;

				if (ws.get(0).get(j).getIcon() != BxlmIconEnum.SCATTER) {
					onlyScatter = false;
					//d = ws.get(0).get(j).getIcon().getTrs()[num - 1];
				}
				//ws.get(0).get(j).setNum(num);
				//winlist.add(ws.get(0).get(j));
				//all += d;
			}
		}
		
		// 总奖励
		double totalReward = 0;
		List<RewardInfo> winInfo = gameService.winNum(ws);
		List<WindowInfo> scatter = new ArrayList<>();
		// 检查scatter奖励
		for (int i = 0; i < 5; i++) {
			if (gameService.chickScatter(ws.get(i)) != null) {
				scatter.add(gameService.chickScatter(ws.get(i)));
			}
		}
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
			if (next.getWinNum() > 0) {
				boolean iswild= false;
				boolean isScatter= false;
				for (WindowInfo wi : next.getWinIcon()) {
					if (wi.getIcon().isWild()) {
						iswild = true;
					}
					if (wi.getIcon().isScatter()) {
						isScatter = true;
						break;
					}
				}
				if (iswild) {
					double reward = NumberTool.multiply(
							NumberTool.multiply(rep.getBetInfo().total() * 2, next.getIcon().getTrs()[next.getWinNum() - 1]),
							next.getNum()).doubleValue();
					totalReward += reward;
					next.setReward(reward);
				} else {
					if (isScatter) { // scatter图标奖励(总赌注*赔率倍数*中奖线数)
						double reward = NumberTool.multiply(NumberTool.multiply(rep.getBetInfo().getTotalBet(), next.getIcon().getTrs()[next.getWinNum() - 1]),next.getNum()).doubleValue();
						totalReward += reward;
						next.setReward(reward);
					} else { // 普通图标奖励(线注*硬币数*赔率倍数*中奖线数)
						double reward = NumberTool.multiply(NumberTool.multiply(rep.getBetInfo().total(), next.getIcon().getTrs()[next.getWinNum() - 1]),next.getNum()).doubleValue();
						totalReward += reward;
						next.setReward(reward);
					}
				}
			}else {
				iterator.remove();
			}
		}
		res.setRewardInfo(winInfo);
		
//		win.setReward(NumberTool.multiply(
//				all,
//				rep.getBetInfo().getGold()).doubleValue());
		win.setReward(totalReward);
		win.setRewardType(2);
		win.setNum(number);

		// 进行消除
		List<List<WindowInfo>> winList = new ArrayList<List<WindowInfo>>(5);

		if (!onlyScatter) {
			if (totalReward > 0) {
				winList = gameService.winIcon(winInfo,ws);
				
				/*for (WindowInfo info : winlist) {
					int i, j;
					for (i = 0; i < info.getNum(); i++) {
						winList.add(new ArrayList<WindowInfo>(3) {
							private static final long serialVersionUID = 1L;
							{
								add(null);
								add(null);
								add(null);
							}
						});
						for (j = 0; j < 3; j++) {
							if ((ws.get(i).get(j).getIcon() == info.getIcon() || ws
									.get(i).get(j).getIcon() == BxlmIconEnum.WILD)
									&& ws.get(i).get(j).getIcon() != BxlmIconEnum.SCATTER) {
								winList.get(i).set(j, ws.get(i).get(j));
								ws.get(i).get(j).setIndex(99);
							}
						}
					}
				}*/
			}
		}

		// 是否会补充数据
		if (totalReward > 0 && !onlyScatter) {
			win.setBxlmReplenish(true);
			rep.setNumber(rep.getNumber() + 1);
			gameService.wininfo = ws;
			gameService.lastRoundIconId = iconId;
			// rep.setWininfo(ws);
		} else {
			rep.setBxlmReplenish(false);
			rep.setNumber(0);
			win.setBxlmReplenish(false);
		}

		win.setRep(rep);
		win.setWindows(winList);	

		return win;
	}

	/**
	 * 掉落数据缓存
	 */
	private Map<String, BxlmReplenish> ReplenishData = new ConcurrentHashMap<>();
	
	
	/**
	 * 保存补充数据
	 * 
	 * @param BxlmReplenish
	 * @param roleid
	 */
	public void save(BxlmReplenish re, String roleid, String uuid) {
		
		ReplenishData.put(roleid, re);
		/*try {
			redisTemplate.opsForValue().set(Constants.BXLMREDIS_REPLENISH + roleid + "|" + uuid,
					JSONObject.toJSONString(re),Constants.RECORD_TIME_5MIN, TimeUnit.SECONDS);
		} catch (Exception e) {
			 e.printStackTrace();
		}*/
		
	}

	/**
	 * 删除补充数据
	 * 
	 * @param BxlmReplenish
	 * @param roleid
	 */
	public void delete(String roleid, String uuid) {
		ReplenishData.remove(roleid);
		/*try {
			redisTemplate.delete(Constants.BXLMREDIS_REPLENISH + roleid + "|" + uuid);
		} catch (Exception e) {
			 e.printStackTrace();
		}*/
		
	}

	/**
	 * 获取补充数据
	 * 
	 * @param roleid
	 * @return BxlmReplenish
	 * @throws Exception
	 */
	public BxlmReplenish getBxlmReplenishData(String roleid, String uuid) {
		BxlmReplenish bxlmReplenish = ReplenishData.get(roleid);
		if (bxlmReplenish != null) {
			return bxlmReplenish;
		}
		
	/*	try {
			String str = redisTemplate.opsForValue().get(
					Constants.BXLMREDIS_REPLENISH + roleid + "|" + uuid);
			if (str != null) {
				BxlmReplenish re = JSONObject.parseObject(str, BxlmReplenish.class);
				return re;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		return null;
	}
}
