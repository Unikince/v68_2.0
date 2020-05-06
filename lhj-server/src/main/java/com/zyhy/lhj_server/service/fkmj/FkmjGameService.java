/**
 * 
 */
package com.zyhy.lhj_server.service.fkmj;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zyhy.common_lhj.BetInfo;
import com.zyhy.common_lhj.Line;
import com.zyhy.common_lhj.Player;
import com.zyhy.common_lhj.WinLineInfo;
import com.zyhy.common_lhj.Window;
import com.zyhy.common_lhj.logic.BaseLogic;
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.lhj_server.constants.Constants;
import com.zyhy.lhj_server.game.alsj.poi.template.AlsjOdds;
import com.zyhy.lhj_server.game.fkmj.FkmjBonusInfo;
import com.zyhy.lhj_server.game.fkmj.FkmjIconEnum;
import com.zyhy.lhj_server.game.fkmj.FkmjReplenish;
import com.zyhy.lhj_server.game.fkmj.FkmjRollerWeightEnum;
import com.zyhy.lhj_server.game.fkmj.FkmjWinLineEnum;
import com.zyhy.lhj_server.game.fkmj.poi.impl.FkmjTemplateService;
import com.zyhy.lhj_server.game.fkmj.poi.template.FkmjOdds;
import com.zyhy.lhj_server.prcess.result.fkmj.FkmjGameBetResult;


@Service
public class FkmjGameService extends BaseLogic {

	@Autowired
	private StringRedisTemplate redisTemplate;
	@Autowired
	private FkmjTemplateService templateService;
	// 窗口信息
	public List<Window> wInfo = new ArrayList<>();
	// 获胜窗口信息
	public List<WinLineInfo>  winInfo = new ArrayList<>();
	/**
	 * 
	 * @param iconId 
	 * @param rep 
	 * @param jetton
	 *            档位
	 * @param betnum
	 *            下注的线路数
	 * @return
	 */
	public FkmjGameBetResult doGameProcess(String roleid, BetInfo betinfo,
			boolean isfree, FkmjBonusInfo bi, int iconId) {
		FkmjGameBetResult res = getResult(betinfo, isfree, bi , roleid,iconId);
		return res;
	}
	
	private FkmjGameBetResult getResult(BetInfo betinfo, boolean isfree,
			FkmjBonusInfo bi, String roleid, int iconId) {
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
		FkmjGameBetResult res = new FkmjGameBetResult();
		List<Window> ws = new ArrayList<Window>();
		for (int i = 1; i <= 5; i++) {
			ws.addAll(FkmjRollerWeightEnum.windowInfo(i,baseinfos));
		}
		res.setWindowinfos(ws);
		
		// 获胜线路信息
		List<WinLineInfo> wins = getWininfo(ws, betinfo);
		
		//检测scatter奖励
		WinLineInfo ckickSatter = CkickSatter(ws);
		if (ckickSatter != null) {
			wins.add(ckickSatter);
		}
		
		//检测是否中了免费游戏
		boolean isBonus = false;
		List<Window> ckickBonus = CkickBonus(ws);
		if (!isfree && ckickBonus != null) {
			isBonus = true;
			res.setBonus(isBonus);
		}
		
		// 设置bonus图标线路用于破碎
		if (ckickBonus != null) {
			WinLineInfo scatterWin = new WinLineInfo();
			scatterWin.setIcon(FkmjIconEnum.BONUS);
			scatterWin.setId("bonus");
			scatterWin.setNum(3);
			scatterWin.setWindows(ckickBonus);
			wins.add(scatterWin);
		}
		res.setWinrouteinfos(wins);
		// 计算奖励
		double totalReward = 0;
		if (wins != null && wins.size() > 0) {
			// 总的奖励数
			for (WinLineInfo w : wins) {
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
		
		// 保存补充数据
		if(totalReward > 0 || ckickBonus != null){
			wInfo = ws;
			winInfo = wins;
			FkmjReplenish rep = new FkmjReplenish();
			rep.setBetInfo(betinfo);
			rep.setReplenish(true);
			res.setDrop(true);
			res.setRep(rep);
		}
		return res;
	}

	/**
	 * 得到获胜线路信息
	 * 
	 * @param ws
	 * @param betnum
	 *            线路数
	 * @return
	 */
	public List<WinLineInfo> getWininfo(List<Window> ws, BetInfo betinfo) {
		List<WinLineInfo> res = new ArrayList<WinLineInfo>();
		// 获取所有线路
		Line[] ts = ArrayUtils.subarray(FkmjWinLineEnum.values(), 0,
				betinfo.getNum());
		for (Line line : ts) {
			List<Window> lws = getLineWindows(ws, line);
			
			// 正面
			WinLineInfo wl = getWinLineInfo(lws);
			if (wl != null && !wl.getIcon().isScatter()) {
				wl.setOrder(1);
				wl.setWindows(lws);
				wl.setId(String.valueOf(line.getId()));
				//reward(wl, betinfo);
				res.add(wl);
			}
		}
		return res;
	}
	
	/**
	 * 保存免费游戏次数
	 * 
	 * @param number
	 * @param roleid
	 */
	public void saveFree(FkmjBonusInfo bi, String roleid, Player userinfo, String uuid) {
		/*redisTemplate.opsForValue().set(Constants.FKMJ_REDIS_LHJ_FKMJ_BONUS + roleid,
				JSONObject.toJSONString(bi));*/
		
		if (Constants.PLATFORM_EBET.equals(uuid)) {
			if (userinfo.getTourist() == -1) {
				redisTemplate.opsForValue().set(Constants.FKMJ_REDIS_LHJ_FKMJ_BONUS + roleid + "|" + uuid,
						JSONObject.toJSONString(bi),Constants.RECORD_TIME_1HOUR, TimeUnit.SECONDS);
			} else {
				redisTemplate.opsForValue().set(Constants.FKMJ_REDIS_LHJ_FKMJ_BONUS + roleid + "|" + uuid,
						JSONObject.toJSONString(bi),Constants.RECORD_TIME_30DAY, TimeUnit.SECONDS);
			}
		} else if (Constants.PLATFORM_V68.equals(uuid)) {
			if (userinfo.getTourist() == 1) {
				redisTemplate.opsForValue().set(Constants.FKMJ_REDIS_LHJ_FKMJ_BONUS + roleid + "|" + uuid,
						JSONObject.toJSONString(bi),Constants.RECORD_TIME_1HOUR, TimeUnit.SECONDS);
			} else {
				redisTemplate.opsForValue().set(Constants.FKMJ_REDIS_LHJ_FKMJ_BONUS + roleid + "|" + uuid,
						JSONObject.toJSONString(bi),Constants.RECORD_TIME_30DAY, TimeUnit.SECONDS);
			}
		}
	}
	
	/**
	 * 获取免费游戏次数
	 * @param roleid
	 * @return
	 */
	public FkmjBonusInfo getFree(String roleid, String uuid) {
		String str = redisTemplate.opsForValue().get(
				Constants.FKMJ_REDIS_LHJ_FKMJ_BONUS + roleid + "|" + uuid);
		
		if (str != null && !str.equals("null")) {
			FkmjBonusInfo bi = JSONObject.parseObject(str, FkmjBonusInfo.class);
			return bi;
		}
		return null;
	}
	
	/**
	 * 删除免费数据
	 */
	public void delFree(String roleid, String uuid){
		redisTemplate.delete(Constants.FKMJ_REDIS_LHJ_FKMJ_BONUS + roleid + "|" + uuid);
	}
	
	/**
	 * 保存补充数据
	 * 
	 * @param FkmjReplenish
	 * @param roleid
	 */
	public void saveReplenish(FkmjReplenish re, String roleid, String uuid) {
		redisTemplate.opsForValue().set(Constants.FKMJ_REDIS_LHJ_FKMJ_REPLENISH + roleid + "|" + uuid,
				JSONObject.toJSONString(re),Constants.RECORD_TIME_5MIN, TimeUnit.SECONDS);
	}
	
	/**
	 * 删除补充数据
	 * 
	 * @param FkmjReplenish
	 * @param roleid
	 */
	public void deleteReplenish(String roleid, String uuid) {
		redisTemplate.delete(Constants.FKMJ_REDIS_LHJ_FKMJ_REPLENISH + roleid + "|" + uuid);
	}

	/**
	 * 获取补充数据
	 * 
	 * @param roleid
	 * @return Replenish
	 * @throws Exception
	 */
	public FkmjReplenish getReplenishData(String roleid, String uuid) {
		String str = redisTemplate.opsForValue().get(
				Constants.FKMJ_REDIS_LHJ_FKMJ_REPLENISH + roleid + "|" + uuid);
		if (str != null) {
			FkmjReplenish re = JSONObject.parseObject(str, FkmjReplenish.class);
			return re;
		}
		return null;
	}
	
	/**
	 * 判断是否有免费游戏
	 * @param ws
	 * @return
	 */
	public List<Window> CkickBonus(List<Window> ws){
		List<Window> scatter = new ArrayList<>();
		Set<Integer> set = new HashSet<Integer>();
			for(Window w:ws){
				if((w.getId() == 1 || w.getId() == 3 || w.getId() == 5) && w.getIcon() == FkmjIconEnum.BONUS){
					scatter.add(w);
					set.add(w.getId());
				}
			}
		if(set.size() > 2){
			return scatter;
		}
		return null;
	}
	
	/**
	 * 判断是否有免费游戏
	 * @param ws
	 * @return
	 */
	public WinLineInfo CkickSatter(List<Window> ws){
		List<Window> scatter = new ArrayList<>();
			for(Window w:ws){
				if(w.getIcon().isScatter()){
					if (scatter.size() > 0) {
						if (w.getId() == scatter.get(scatter.size() - 1).getId()) {
							continue;
						}
					}
					scatter.add(w);
				}
			}
		if(scatter.size() > 1){
			WinLineInfo winfo = new WinLineInfo();
			winfo.setReward(FkmjIconEnum.SCATTER.getTrs()[scatter.size() - 1]);
			winfo.setNum(scatter.size());
			winfo.setIcon(scatter.get(0).getIcon());
			winfo.setOrder(1);
			winfo.setWindows(scatter);
			winfo.setId("scatter");
			return winfo;
		}
		return null;
	}
	
}
