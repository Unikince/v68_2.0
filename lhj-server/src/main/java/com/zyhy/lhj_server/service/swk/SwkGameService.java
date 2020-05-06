/**
 * 
 */
package com.zyhy.lhj_server.service.swk;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zyhy.common_lhj.BetInfo;
import com.zyhy.common_lhj.Icon;
import com.zyhy.common_lhj.Line;
import com.zyhy.common_lhj.Player;
import com.zyhy.common_lhj.WinLineInfo;
import com.zyhy.common_lhj.Window;
import com.zyhy.common_lhj.logic.BaseLogic;
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.lhj_server.constants.Constants;
import com.zyhy.lhj_server.game.swk.SwkIconEnum;
import com.zyhy.lhj_server.game.swk.SwkRollerWeightEnum;
import com.zyhy.lhj_server.game.swk.SwkScatterInfo;
import com.zyhy.lhj_server.game.swk.SwkWinLineEnum;
import com.zyhy.lhj_server.game.swk.poi.impl.SwkTemplateService;
import com.zyhy.lhj_server.game.swk.poi.template.SwkOdds;
import com.zyhy.lhj_server.prcess.result.swk.SwkGameBetResult;

/**
 * @author linanjun
 *
 */
@Service
public class SwkGameService extends BaseLogic {

	@Autowired
	private StringRedisTemplate redisTemplate;
	@Autowired
	private SwkTemplateService templateService;

	/**
	 * 
	 * @param iconId 
	 * @param jetton
	 *            档位
	 * @param betnum
	 *            下注的线路数
	 * @return
	 */
	public SwkGameBetResult doGameProcess(String roleid, BetInfo betinfo,
			boolean isfree, SwkScatterInfo bi, int iconId) {
		SwkGameBetResult res = getResult(betinfo, isfree, bi,iconId);
		return res;
	}

	private SwkGameBetResult getResult(BetInfo betinfo, boolean isfree,
			SwkScatterInfo bi, int iconId) {
		// 获取图标
		List<SwkOdds> baseinfos = templateService.getList(SwkOdds.class);
		//System.out.println("加载进来的图标有:" + baseinfos);
		
		// 获取指定赔率图标
		Iterator<SwkOdds> iterator = baseinfos.iterator();
		while (iterator.hasNext()) {
			SwkOdds next = iterator.next();
			if (next.getType() != iconId) {
				iterator.remove();
			}
		}
		//System.out.println("加载进来的图标有:" + baseinfos);
		
		// 窗口信息
		SwkGameBetResult res = new SwkGameBetResult();
		List<Window> ws = new ArrayList<Window>();
		for (int i = 1; i <= 5; i++) {
			ws.addAll(SwkRollerWeightEnum.windowInfo(i,baseinfos));
		}
		res.setWindowinfos(ws);
		// 获胜线路信息
		List<WinLineInfo> wins = getWininfo(ws, betinfo);
		//检测是否中了免费游戏
		WinLineInfo ckickSatter = CkickSatter(ws);
		int num = ckickSatter.getNum();
		boolean isScatter = false;
		if (num > 2) {
			isScatter = true;
		}
		//scatter奖励
		double scatterReward = 0;
		if(num > 1){
			res.setScatterCount(num);
			scatterReward = NumberTool.multiply(SwkIconEnum.SCATTER.getTrs()[CkickSatter(ws).getNum() - 1],betinfo.getTotalBet()).doubleValue();
			res.setScatterReward(scatterReward);
			ckickSatter.setReward(scatterReward);
			wins.add(ckickSatter);
		}
		
		//计算总奖励
		if(wins != null && wins.size() > 0){
			//总的奖励数
			double reward = 0;
			for(WinLineInfo w : wins){
				reward += w.getReward() ;
			}
			// 免费游戏根据模式来获得总奖励
			if (isfree && bi != null) {
				switch (bi.getModel()) {
				case 1:
					res.setRewardcoin(NumberTool.multiply(reward,
							5).doubleValue());
					break;
				case 2:
					res.setRewardcoin(NumberTool.multiply(reward,
							4).doubleValue());
					break;
				case 3:
					res.setRewardcoin(NumberTool.multiply(reward,
							2).doubleValue());
					break;
				}
			} else {
				res.setRewardcoin(reward);
			}
		}
		res.setScatter(isScatter);
		res.setWinrouteinfos(wins);
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
	private List<WinLineInfo> getWininfo(List<Window> ws, BetInfo betinfo) {
		List<WinLineInfo> res = new ArrayList<WinLineInfo>();
		// 获取所有线路
		Line[] ts = ArrayUtils.subarray(SwkWinLineEnum.values(), 0,
				betinfo.getNum());
		for (Line line : ts) {
			List<Window> lws = getLineWindows(ws, line);
			// 正面
			WinLineInfo wl = getWinLineInfo(lws);
			if (wl != null && !wl.getIcon().isScatter()) {
				wl.setOrder(1);
				wl.setWindows(new ArrayList<Window>(lws.subList(0, wl.getNum())));
				wl.setId(String.valueOf(line.getId()));
				reward(wl, betinfo);
				res.add(wl);
			}
		}
		return res;
	}
	
	public void reward(WinLineInfo wl, BetInfo betinfo) {
		if(wl != null){
			rewardWail(wl);
			wl.setReward(NumberTool.multiply(wl.getReward(), betinfo.getGold()).doubleValue());
		}
	}
	
	/**
	 * 计算wail奖励
	 */
	public void rewardWail(WinLineInfo wl) {
		if(wl.validWailNum() > 0){
			wl.setReward(NumberTool.multiply(wl.getReward(), 2).doubleValue());
		}
	}
	
	/**
	 * 获胜线路信息
	 */
	public WinLineInfo getWinLineInfo(List<Window> lineWindows) {
		int wail = 0;
		int num = 0;
		Icon lineIcon = null;
		int size = lineWindows.size();
		for(int i=0;i<size;i++){
			Icon icon = lineWindows.get(i).getIcon();
			if (icon == SwkIconEnum.SCATTER) {
				return null;
			}
			if(i > 0 && i != wail && !icon.isWild() && lineIcon != icon){
				break;
			}
			
			if(icon.isWild()){
				wail++;
			}else{
				lineIcon = icon;
			}
			num++;
		}
		
		if(num > 0){
			if(lineIcon == null){
				lineIcon = lineWindows.get(0).getIcon();
			}
			
			double v = lineIcon.getTrs()[num-1];
			if(v > 0){
				WinLineInfo winfo = new WinLineInfo();
				winfo.setReward(v);
				winfo.setNum(num);
				winfo.setIcon(lineIcon);
				return winfo;
			}
		}
		return null;
	}
	
	/**
	 * 判断wild数量
	 * @return 
	 */
	public int cheakWild(List<Window> lws) {
			Icon icon2 = lws.get(0).getIcon();
			int count = 0;
			for (int i = 1; i < lws.size(); i++) {
				Icon icon = lws.get(i).getIcon();
				if ( icon2 != SwkIconEnum.WILD && icon != icon2) {
					break;
				}
				count ++;
			}
			
		return count;
	}

	/**
	 * 保存免费游戏次数
	 * 
	 * @param number
	 * @param roleid
	 */
	public void save(SwkScatterInfo bi, String roleid, Player userinfo, String uuid) {
		
		/*redisTemplate.opsForValue().set(Constants.SWK_REDIS_LHJ_SWK_SCATTER + roleid,
				JSONObject.toJSONString(bi));*/
		
		if (Constants.PLATFORM_EBET.equals(uuid)) {
			if (userinfo.getTourist() == -1) {
				redisTemplate.opsForValue().set(Constants.SWK_REDIS_LHJ_SWK_SCATTER + roleid + "|" + uuid,
						JSONObject.toJSONString(bi),Constants.RECORD_TIME_1HOUR, TimeUnit.SECONDS);
			}else {
				redisTemplate.opsForValue().set(Constants.SWK_REDIS_LHJ_SWK_SCATTER + roleid + "|" + uuid,
						JSONObject.toJSONString(bi),Constants.RECORD_TIME_30DAY, TimeUnit.SECONDS);
			}
		} else if (Constants.PLATFORM_V68.equals(uuid)) {
			if (userinfo.getTourist() == 1) {
				redisTemplate.opsForValue().set(Constants.SWK_REDIS_LHJ_SWK_SCATTER + roleid + "|" + uuid,
						JSONObject.toJSONString(bi),Constants.RECORD_TIME_1HOUR, TimeUnit.SECONDS);
			}else {
				redisTemplate.opsForValue().set(Constants.SWK_REDIS_LHJ_SWK_SCATTER + roleid + "|" + uuid,
						JSONObject.toJSONString(bi),Constants.RECORD_TIME_30DAY, TimeUnit.SECONDS);
			}
		}
		
		
	}
	
	/**
	 * 获取免费游戏信息
	 * @param roleid
	 * @return
	 */
	public SwkScatterInfo getData(String roleid, String uuid) {
		String str = redisTemplate.opsForValue().get(
				Constants.SWK_REDIS_LHJ_SWK_SCATTER + roleid + "|" + uuid);
		if (str != null) {
			SwkScatterInfo bi = JSONObject.parseObject(str, SwkScatterInfo.class);
			return bi;
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
		redisTemplate.delete(Constants.SWK_REDIS_LHJ_SWK_SCATTER + roleid + "|" + uuid);
	}
	
	/**
	 * 判断SCATTER数量
	 * @param ws
	 * @return
	 */
	public WinLineInfo CkickSatter(List<Window> ws){
		List<Window> list = new ArrayList<>();
			for(Window w:ws){
				if(w.getIcon() == SwkIconEnum.SCATTER){
					list.add(w);
				}
			}
			WinLineInfo winfo = new WinLineInfo();
			if (list.size() > 1) {
				winfo.setId("Scatter");
				winfo.setNum(list.size());
				winfo.setIcon(SwkIconEnum.SCATTER);
				winfo.setWindows(list);
			}
		return winfo;
	}
}
