/**
 * 
 */
package com.zyhy.lhj_server.service.ajxz;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zyhy.common_lhj.BetInfo;
import com.zyhy.common_lhj.Line;
import com.zyhy.common_lhj.Player;
import com.zyhy.common_lhj.WinLineInfo;
import com.zyhy.common_lhj.Window;
import com.zyhy.common_lhj.WindowInfo;
import com.zyhy.common_lhj.logic.BaseLogic;
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.common_server.util.StringUtils;
import com.zyhy.lhj_server.constants.Constants;
import com.zyhy.lhj_server.game.ajxz.AjxzFreeInfo;
import com.zyhy.lhj_server.game.ajxz.AjxzIconEnum;
import com.zyhy.lhj_server.game.ajxz.AjxzReplenish;
import com.zyhy.lhj_server.game.ajxz.AjxzRollerWeightEnum;
import com.zyhy.lhj_server.game.ajxz.AjxzWinLineEnum;
import com.zyhy.lhj_server.game.ajxz.poi.impl.AjxzTemplateService;
import com.zyhy.lhj_server.game.ajxz.poi.template.AjxzOdds;
import com.zyhy.lhj_server.game.bxlm.BxlmScatterInfo;
import com.zyhy.lhj_server.prcess.result.ajxz.AjxzGameBetResult;


@Service
public class AjxzGameService extends BaseLogic {
	@Autowired
	private AjxzTemplateService templateService;
	@Autowired
	private StringRedisTemplate redisTemplate;
	// 模式5窗口信息
	public List<Window> reswindow = new ArrayList<>();
	/**
	 * @param iconId 
	 * @param rep 
	 * @param jetton
	 *            档位
	 * @param betnum
	 *            下注的线路数
	 * @return
	 */
	public AjxzGameBetResult doGameProcess(String roleid, BetInfo betinfo, int iconId) {
		AjxzGameBetResult res = getResult(betinfo,roleid,iconId);
		return res;
	}
	
	private AjxzGameBetResult getResult(BetInfo betinfo, String roleid, int iconId) {
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
		AjxzGameBetResult res = new AjxzGameBetResult();
		List<Window> ws = new ArrayList<Window>();
		for (int i = 1; i <= 5; i++) {
			ws.addAll(AjxzRollerWeightEnum.windowInfo(i,baseinfos));
		}
		res.setWindowinfos(ws);
		
		// 获胜线路信息
		List<WinLineInfo> wins = getWininfo(ws, betinfo);
		res.setWinrouteinfos(wins);
		
		//检测是否中了免费游戏
		boolean isFree = CkickScatter(ws);
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
		Line[] ts = ArrayUtils.subarray(AjxzWinLineEnum.values(), 0,
				betinfo.getNum());
		for (Line line : ts) {
			List<Window> lws = getLineWindows(ws, line);
			
			// 正面
			WinLineInfo wl = getWinLineInfo(lws);
			if (wl != null) {
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
	 * 判断是否有免费游戏
	 * @param ws
	 * @return
	 */
	protected boolean CkickScatter(List<Window> ws){
		Set<Integer> set = new HashSet<Integer>();
			for(Window w:ws){
				if((w.getId() == 2 || w.getId() == 3 || w.getId() == 4) && w.getIcon() == AjxzIconEnum.SCATTER){
					set.add(w.getId());
				}
			}
		if(set.size() > 2){
			return true;
		}
		return false;
	}
	
	private Map<String, AjxzFreeInfo> freeGameInfo = new ConcurrentHashMap<>();
	private Map<String, AjxzReplenish> replenishGameInfo = new ConcurrentHashMap<>();
	
	public void saveFreeInfoCache(String roleid, AjxzFreeInfo free, Player userinfo, String uuid){
		freeGameInfo.put(roleid + "|" + uuid, free);
		saveFreeInfo(roleid,free,userinfo,uuid);
	}
	
	public void saveReplenishInfoCache(String roleid, AjxzReplenish re, String uuid){
		replenishGameInfo.put(roleid + "|" + uuid, re);
		saveReplenish(roleid,re,uuid);
	}
	
	public void delFreeInfoCache(String roleid, String uuid){
		freeGameInfo.remove(roleid + "|" + uuid);
		delFreeInfo(roleid,uuid);
	}
	
	public void delReplenishInfoCache(String roleid, String uuid){
		replenishGameInfo.remove(roleid + "|" + uuid);
		delReplenish(roleid,uuid);
	}
	
	/**
	 * 保存免费信息
	 * @param free 
	 */
	@Async
	public void saveFreeInfo(String roleid, AjxzFreeInfo free, Player userinfo, String uuid){
		/*redisTemplate.opsForValue().set(Constants.AJXZ_REDIS_LHJ_AJXZ_FREE + roleid,
				JSONObject.toJSONString(free));*/
		
		if (Constants.PLATFORM_EBET.equals(uuid)) {
			if (userinfo.getTourist() == -1) {
				redisTemplate.opsForValue().set(Constants.AJXZ_REDIS_LHJ_AJXZ_FREE + roleid + "|" + uuid,
						JSONObject.toJSONString(free),Constants.RECORD_TIME_1HOUR,TimeUnit.SECONDS);
			} else {
				redisTemplate.opsForValue().set(Constants.AJXZ_REDIS_LHJ_AJXZ_FREE + roleid + "|" + uuid,
						JSONObject.toJSONString(free),Constants.RECORD_TIME_30DAY,TimeUnit.SECONDS);
			}
		} else if (Constants.PLATFORM_V68.equals(uuid)) {
			if (userinfo.getTourist() == 1) {
				redisTemplate.opsForValue().set(Constants.AJXZ_REDIS_LHJ_AJXZ_FREE + roleid + "|" + uuid,
						JSONObject.toJSONString(free),Constants.RECORD_TIME_1HOUR,TimeUnit.SECONDS);
			} else {
				redisTemplate.opsForValue().set(Constants.AJXZ_REDIS_LHJ_AJXZ_FREE + roleid + "|" + uuid,
						JSONObject.toJSONString(free),Constants.RECORD_TIME_30DAY,TimeUnit.SECONDS);
			}
		}
	}
	
	/**
	 * 获取免费信息
	 */
	public AjxzFreeInfo getFreeInfo(String roleid, String uuid) {
		if (freeGameInfo.containsKey(roleid + "|" + uuid)) {
			return freeGameInfo.get(roleid + "|" + uuid);
		}
		String str = redisTemplate.opsForValue().get(
				Constants.AJXZ_REDIS_LHJ_AJXZ_FREE + roleid + "|" + uuid);
		if (!StringUtils.isEmpty(str) && !str.equals("null")) {
			AjxzFreeInfo free = JSONObject.parseObject(str, AjxzFreeInfo.class);
			return free;
		}
		return null;
	}
	
	/**
	 * 删除免费信息
	 */
	@Async
	public void delFreeInfo(String roleid, String uuid){
			redisTemplate.delete(Constants.AJXZ_REDIS_LHJ_AJXZ_FREE + roleid + "|" + uuid);
	}
	
	/**
	 * 判断是否需要补充数据
	 * @param wins 
	 * @return 
	 */
	public boolean checkReplenish(List<Window> ws) {
		for (Window wl : ws) {
			if (wl.getIcon() == AjxzIconEnum.WILD) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 保存补充数据
	 */
	@Async
	public void saveReplenish(String roleid, AjxzReplenish re, String uuid){
		/*redisTemplate.opsForValue().set(Constants.AJXZ_REDIS_REPLENISH + roleid,
				JSONObject.toJSONString(re));*/
		
		redisTemplate.opsForValue().set(Constants.AJXZ_REDIS_REPLENISH + roleid + "|" + uuid,
				JSONObject.toJSONString(re),Constants.RECORD_TIME_5MIN, TimeUnit.SECONDS);
	}
	
	/**
	 * 获取补充数据
	 */
	public AjxzReplenish getReplenish(String roleid, String uuid) {
		
		if (replenishGameInfo.containsKey(roleid + "|" + uuid)) {
			return replenishGameInfo.get(roleid + "|" + uuid);
		}
		String str = redisTemplate.opsForValue().get(
				Constants.AJXZ_REDIS_REPLENISH + roleid + "|" + uuid);
		if (!StringUtils.isEmpty(str) && !str.equals("null")) {
			AjxzReplenish re = JSONObject.parseObject(str, AjxzReplenish.class);
			return re;
		}
		return null;
	}
	
	/**
	 * 删除补充数据
	 */
	@Async
	public void delReplenish(String roleid, String uuid){
		redisTemplate.delete(Constants.AJXZ_REDIS_REPLENISH + roleid + "|" + uuid);
	}
	/**
	 * 转换为双list格式
	 */
	public List<List<WindowInfo>> formatDouble(List<Window> ws){
		List<List<WindowInfo>> allList = new ArrayList<>();
		for (int i = 1; i <= 5; i++) {
			List<WindowInfo> list = new ArrayList<>();
			for (Window w : ws) {
				WindowInfo wi1 = (WindowInfo) w;
				if (wi1.getId() == i) {
					list.add(wi1);
				}
			}
			allList.add(list);
		}
		return allList;
	}
	
	/**
	 * 转换为单list格式
	 */
	public List<Window> formatSingle(List<List<WindowInfo>> wi){
		List<Window> formatSingle = new ArrayList<>();
		for (List<WindowInfo> list : wi) {
			for (WindowInfo windowInfo : list) {
				formatSingle.add(windowInfo);
			}
		}
		return formatSingle; 
	}
}
