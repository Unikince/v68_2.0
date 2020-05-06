package com.zyhy.lhj_server.service.lqjx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zyhy.common_lhj.BetInfo;
import com.zyhy.common_lhj.WindowInfo;
import com.zyhy.common_lhj.logic.BaseLogic;
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.lhj_server.constants.Constants;
import com.zyhy.lhj_server.game.RewardInfo;
import com.zyhy.lhj_server.game.lqjx.LqjxIconEnum;
import com.zyhy.lhj_server.game.lqjx.LqjxReplenish;
import com.zyhy.lhj_server.game.lqjx.LqjxRollerWeightEnum;
import com.zyhy.lhj_server.game.lqjx.LqjxWinIndex;
import com.zyhy.lhj_server.game.lqjx.poi.impl.LqjxTemplateService;
import com.zyhy.lhj_server.game.lqjx.poi.template.LqjxOdds;
import com.zyhy.lhj_server.prcess.result.lqjx.LqjxGameBetResult;

/**
 * @author DPC
 * @version 创建时间：2019年2月27日 上午10:58:02
 */
@Service
public class LqjxReplenishService extends BaseLogic {
	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private LqjxGameService gameService;
	@Autowired
	private LqjxTemplateService templateService;
	/**
	 * 
	 * @param betInfo
	 * @param freeGame 
	 * @param iconId 
	 * @param jetton
	 *            档位
	 * @param betnum
	 *            下注的线路数
	 * @return
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public LqjxGameBetResult doGameProcess(String roleid, LqjxReplenish rep, BetInfo betInfo, boolean freeGame, int iconId) throws ClassNotFoundException, IOException {
		LqjxGameBetResult res = null;
		res = new LqjxGameBetResult();
		// 获取图标
		List<LqjxOdds> baseinfos = templateService.getList(LqjxOdds.class);
		//System.out.println("加载进来的图标有:" + baseinfos);
		
		// 获取指定赔率图标
		Iterator<LqjxOdds> iterator = baseinfos.iterator();
		while (iterator.hasNext()) {
			LqjxOdds next = iterator.next();
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
							list.get(i).add(j, new WindowInfo(i, j , LqjxIconEnum.WILD));
						} else {
							list.get(i).add(j, LqjxRollerWeightEnum.windowInfo(i, j,list.get(i),baseinfos));
						}
					} else {
						list.get(i).add(j, LqjxRollerWeightEnum.windowInfo(i, j,list.get(i),baseinfos));
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
							list.get(i).add(j, new WindowInfo(i, j , LqjxIconEnum.WILD));
						} else {
							list.get(i).add(j, LqjxRollerWeightEnum.windowInfo(i, j,list.get(i),baseinfos));
						}
					} else {
						list.get(i).add(j, LqjxRollerWeightEnum.windowInfo(i, j,list.get(i),baseinfos));
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
		double totalBet = betInfo.getTotalBet();
		LqjxWinIndex wins = getWininfo(list, rep,res);
		int scatter = 0;
		for (int i = 0; i < 5; i++) {
			if (gameService.chickFree(list.get(i))) {
				scatter++;
			}
		}

		if (scatter > 2) {
			double scatterReward = NumberTool.multiply(LqjxIconEnum.SCATTER.getTrs()[scatter - 1], totalBet).doubleValue();
			wins.setReward(NumberTool.add(wins.getReward(),scatterReward ).doubleValue());
			res.setScattericon(scatterReward);
			res.setScattericonnum(scatter);
		}

		if (wins.getRep() != null && wins.getRep().getNumber() > 1 && freeGame) {
			res.setRewardcoin(NumberTool.multiply(wins.getReward(), wins.getRep().getNumber()).doubleValue());
			res.setMul(1);
			res.setRewardmul(wins.getRep().getNumber());
		} else {
			res.setRewardcoin(wins.getReward());
		}

		res.setWin(wins.getWindows());

		// 判断是否进行免费旋转
		//if (!rep.isFree() && wins.getRewardType() == 2 && wins.getNum() != 0) {
			//res.setScatter(true);
			//res.setScatterNum(wins.getNum());
		//}


		res.setReplenish(wins.isLqjxReplenish());
		res.setRep(wins.getRep());
		return res;
	}

	/**
	 * 得到获胜线路信息
	 * @param res 
	 */
	private LqjxWinIndex getWininfo(List<List<WindowInfo>> ws, LqjxReplenish rep, LqjxGameBetResult res) {
		LqjxWinIndex win = new LqjxWinIndex();
		// 奖励免费次数
		int number = 0;
		int winNum = 0;
		int[] scatterNum = { 0, 0, 15, 20, 20 };
		
		// 检查是否有免费游戏
		for (int i = 0; i < 5; i++) {
			if (gameService.chickFree(ws.get(i)) ) {
				winNum++;
			}
		}
		
		if (winNum > 2) {
			number = scatterNum[winNum - 1];
		}

		// 总奖励
		double totalReward = 0;
		List<RewardInfo> winInfo = gameService.winNum(ws);
		Iterator<RewardInfo> iterator = winInfo.iterator();
		while (iterator.hasNext()) {
			RewardInfo next = iterator.next();
			if (next.getWinNum() > 0) {
				totalReward += NumberTool
						.multiply(NumberTool.multiply(rep.getBetInfo().getTotalBet(), next.getWinIcon().get(0).getIcon().getTrs()[next.getWinNum() - 1]),
								next.getNum()).doubleValue();
			}else {
				iterator.remove();
			}
		}
		res.setRewardInfo(winInfo);
		win.setReward(totalReward);
		win.setNum(number);

		// 进行消除
		List<List<WindowInfo>> winList = new ArrayList<List<WindowInfo>>(5);

			if (totalReward > 0 || number > 0) {
				winList = gameService.winIcon(winInfo,ws);
			}

		// 是否会补充数据
		if (totalReward > 0) {
			win.setLqjxReplenish(true);
			rep.setNumber(rep.getNumber() + 1);
			gameService.wininfo = ws;
		} else {
			rep.setNumber(0);
			win.setLqjxReplenish(false);
		}
		win.setRep(rep);
		win.setWindows(winList);
		return win;
	}

	/**
	 * 保存补充数据
	 * 
	 * @param LqjxReplenish
	 * @param roleid
	 */
	public void save(LqjxReplenish re, String roleid, String uuid) {
		
		/*redisTemplate.opsForValue().set(Constants.LQJX_REDIS_REPLENISH + roleid, 
				JSONObject.toJSONString(re));*/
		
		redisTemplate.opsForValue().set(Constants.LQJX_REDIS_REPLENISH + roleid + "|" + uuid, 
				JSONObject.toJSONString(re),Constants.RECORD_TIME_5MIN, TimeUnit.SECONDS);
	}

	/**
	 * 删除补充数据
	 * 
	 * @param LqjxReplenish
	 * @param roleid
	 */
	public void delete(String roleid, String uuid) {
		redisTemplate.delete(Constants.LQJX_REDIS_REPLENISH + roleid + "|" + uuid);
	}

	/**
	 * 获取补充数据
	 * 
	 * @param roleid
	 * @return LqjxReplenish
	 * @throws Exception
	 */
	public LqjxReplenish getLqjxReplenishData(String roleid, String uuid) throws Exception {
		String str = redisTemplate.opsForValue().get(Constants.LQJX_REDIS_REPLENISH + roleid + "|" + uuid);
		if (str != null) {
			LqjxReplenish re = JSONObject.parseObject(str, LqjxReplenish.class);
			return re;
		}
		return null;
	}
	
	
}
