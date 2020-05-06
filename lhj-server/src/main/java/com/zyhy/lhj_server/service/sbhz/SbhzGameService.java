/**
 * 
 */
package com.zyhy.lhj_server.service.sbhz;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
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
import com.zyhy.common_lhj.WinLineInfo;
import com.zyhy.common_lhj.Window;
import com.zyhy.common_lhj.WindowInfo;
import com.zyhy.common_lhj.logic.BaseLogic;
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.common_server.util.RandomUtil;
import com.zyhy.lhj_server.constants.Constants;
import com.zyhy.lhj_server.game.sbhz.SbhzIconEnum;
import com.zyhy.lhj_server.game.sbhz.SbhzRollerWeightEnum;
import com.zyhy.lhj_server.game.sbhz.SbhzWildMonKey;
import com.zyhy.lhj_server.game.sbhz.SbhzWinLineEnum;
import com.zyhy.lhj_server.game.sbhz.poi.impl.SbhzTemplateService;
import com.zyhy.lhj_server.game.sbhz.poi.template.SbhzOdds;
import com.zyhy.lhj_server.prcess.result.sbhz.SbhzGameBetResult;

/**
 * @author linanjun
 *
 */
@Service
public class SbhzGameService extends BaseLogic {
	@Autowired
	private StringRedisTemplate redisTemplate;
	@Autowired
	private SbhzTemplateService templateService;
	// 窗口信息
	private List<Window> windows = new ArrayList<Window>();
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
	public SbhzGameBetResult doGameProcess(String roleid, BetInfo betinfo,SbhzWildMonKey monkey, int iconId) throws ClassNotFoundException, IOException {
//		// 奖池信息
//		JackPool p = jackPoolManager.getPool();
//		boolean k = false;
		// 获取图标
		List<SbhzOdds> baseinfos = templateService.getList(SbhzOdds.class);
		//System.out.println("加载进来的图标有:" + baseinfos);
		
		// 获取指定赔率图标
		Iterator<SbhzOdds> iterator = baseinfos.iterator();
		while (iterator.hasNext()) {
			SbhzOdds next = iterator.next();
			if (next.getType() != iconId) {
				iterator.remove();
			}
		}
		//System.out.println("加载进来的图标有:" + baseinfos);
		
		SbhzGameBetResult res = new SbhzGameBetResult();
//		do {
//			res = new SbhzGameBetResult();
			// 窗口信息
			List<Window> ws = new ArrayList<Window>();
			if(monkey != null){
				ws.addAll(windows);
				for (int i = 1; i <= 3; i++) {
					if(useArraysBinarySearch(monkey.getWild(),i-1)){
						continue;
					}
					int num = (i-1)*3;
					for(int j = 0;j<= 2;j++){
						ws.remove(num);
					}
					int random = RandomUtil.getRandom(0, 30000);
					for(Window w : SbhzRollerWeightEnum.mokeyWindowInfo(i, random,baseinfos)){
						ws.add(num, w);
					}
//					ws.addAll(SbhzRollerWeightEnum.windowInfo(i, random));
				}
			}else{
				for (int i = 1; i <= 3; i++) {
					ws.addAll(SbhzRollerWeightEnum.windowInfo(i,baseinfos));
				}
			}
			List<Integer> wild = new ArrayList<Integer>();
			if(monkey != null && monkey.isMonKey()){
				wild = chickTurned(ws,monkey.getWild());
			}else{
				wild = chickTurned(ws,wild);
			}
			
			// ......
			List<WindowInfo> ws1 = new ArrayList<>();
			for (Window w : ws) {
				ws1.add((WindowInfo) w);
			}
			List<WindowInfo> deepCopy = deepCopy(ws1);
			List<Window> ws2 = new ArrayList<>();
			for (Window w : deepCopy) {
				ws2.add(w);
			}
			res.setWindowinfos(ws2);
			// 是否是百搭猴子大奖
			if (chickeMonkey(ws)) {

				res.setMonkey1(true);
				// 总的奖励数
				res.setRewardcoin(NumberTool
						.multiply(
								NumberTool.multiply(betinfo.getGold(),
										betinfo.getNum()), 1000).doubleValue());
			} else {
				// 获胜线路信息
				List<WinLineInfo> wins = getWininfo(ws, betinfo,monkey);
				// 计算总奖励
				if (wins != null && wins.size() > 0) {
					// 总的奖励数
					BigDecimal reward = new BigDecimal(0);
					for (WinLineInfo w : wins) {
//						if (kill(w)) {
//							return doGameProcess(roleid, betinfo,monkey);
//						}
						reward = reward.add(new BigDecimal(w.getReward() + ""));
					}
					res.setRewardcoin(reward.doubleValue());
				}
				
				res.setWinrouteinfos(wins);
			}
//			if(monkey!=null){
//				if (res.getRewardcoin() > 0
//						&& p.getStatus() == JackPool.STATUS_KILL && !monkey.isMonKey()) {
//					k = true;
//				} else if (res.getRewardcoin() < 0
//						&& p.getStatus() == JackPool.STATUS_WIN && !monkey.isMonKey()) {
//					k = true;
//				}
//			}else{
//				if (res.getRewardcoin() > 0
//						&& p.getStatus() == JackPool.STATUS_KILL) {
//					k = true;
//				} else if (res.getRewardcoin() < 0
//						&& p.getStatus() == JackPool.STATUS_WIN) {
//					k = true;
//				}
//			}
			
//			if(!(p.getStatus() != JackPool.STATUS_NORMAL && k)){
				if(monkey != null && monkey.isMonKey() && res.getRewardcoin() == 0){
					windows = ws;
					monkey.setWild(wild);
					monkey.setMonKey(true);
					monkey.setBetInfo(betinfo);
					res.setTurned(true);
					res.setWilds(wild);
					res.setMonkey(monkey);
				}else if((monkey == null || !monkey.isMonKey()) && wild.size() > 0 && res.getRewardcoin() == 0){
					windows = ws;
					SbhzWildMonKey mk = new SbhzWildMonKey();
					mk.setMonKey(true);
					mk.setWild(wild);
					mk.setBetInfo(betinfo);
					res.setTurned(true);
					res.setWilds(wild);
					res.setMonkey(mk);
				}else if(res.getRewardcoin() > 0 && monkey != null){
					res.setTurned(false);
					res.setWilds(wild);
				}
//			}

//		} while (p.getStatus() != JackPool.STATUS_NORMAL && k);
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
	private List<WinLineInfo> getWininfo(List<Window> ws, BetInfo betinfo,SbhzWildMonKey monkey) {
		List<WinLineInfo> res = new ArrayList<WinLineInfo>();
		// 获取所有线路
		Line[] ts = ArrayUtils.subarray(SbhzWinLineEnum.values(), 0,
				betinfo.getNum());
		for (Line line : ts) {
			List<Window> lws = getLineWindows(ws, line);
			// 正面
			WinLineInfo wl = getWinLineInfo(lws,monkey);
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
	

	/**
	 * 获取线路奖励信息
	 */
	public WinLineInfo getWinLineInfo(List<Window> lineWindows,SbhzWildMonKey monkey) {
		int wail = 0;
		int num = 0;
		//是否scatter触发免费游戏
		Icon lineIcon = null;
		int size = lineWindows.size();
		for(int i=0;i<size;i++){
			Icon icon = lineWindows.get(i).getIcon();
			if(icon == SbhzIconEnum.Monkey && monkey!=null && monkey.isMonKey()){
				wail++;
				num++;
			}else{
				if(i > 0 && i != wail && !icon.isWild() && lineIcon != icon){
					break;
				}
				if(icon.isScatter()){
					lineIcon = icon;
				}else if(icon.isWild()){
					wail++;
				}
				else if(icon.isBar()){
					lineIcon = icon;
				}else{
					lineIcon = icon;
				}
				num++;
			}
		}
		if(num > 2){
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
	 * 检测是否中百搭猴子大奖
	 * 
	 * @param ws
	 * @return
	 */
	private boolean chickeMonkey(List<Window> ws) {
		int count = 0;
		for (Window w : ws) {
			if (w.getIcon() == SbhzIconEnum.Monkey) {
				count++;
			}
		}
		if (count >= 9) {
			return true;
		}
		return false;
	}

	/**
	 * 检测是否需要重转
	 * 
	 * @param ws
	 * @return
	 */
	private List<Integer> chickTurned(List<Window> ws,List<Integer> wild){
		if(wild.size() == 0){
			for(int i = 0;i<=2;i++){
				int num = i*3;
				int count = 0;
				int v = num + 2;
				int index = -1;
				for(int j = num ; j<= v; j++){
					if(ws.get(j).getIcon() == SbhzIconEnum.Monkey){
						count++;
						index = ws.get(j).getId();
					}
					if(count >= 3 && index > 0){
						wild.add(index-1);
					}
				}
			}
		}else{
			for(int i = 0;i<=2;i++){
				if(useArraysBinarySearch(wild,i)){
					continue;
				}
				int num = i*3;
				int count = 0;
				int v = num + 2;
				int index = -1;
				for(int j = num; j<= v; j++){
					if(ws.get(j).getIcon() == SbhzIconEnum.Monkey){
						count++;
					}
					if(count >= 3 && index > 0){
						wild.add(index-1);
					}
				}
			}
		}
		return wild;
	}
	/**
	 * 保存重转数据
	 * 
	 * @param isMonkey
	 * @param roleid
	 */
	public void save(SbhzWildMonKey monkey, String roleid, String uuid) {
		/*redisTemplate.opsForValue().set(Constants.SBHZ_REDIS_WILD + roleid,
				JSONObject.toJSONString(monkey));*/
		
		redisTemplate.opsForValue().set(Constants.SBHZ_REDIS_WILD + roleid + "|" + uuid,
				JSONObject.toJSONString(monkey),Constants.RECORD_TIME_5MIN, TimeUnit.SECONDS);
	}
	
	/**
	 * 获取重转数据
	 * @param roleid
	 * @param uuid
	 * @return
	 */
	public SbhzWildMonKey getData(String roleid, String uuid) {
		String str = redisTemplate.opsForValue().get(
				Constants.SBHZ_REDIS_WILD + roleid + "|" + uuid);
		if (str != null) {
			SbhzWildMonKey mk = JSONObject.parseObject(str, SbhzWildMonKey.class);
			return mk;
		}
		return null;
	}
	
	/**
	 * 删除重转数据
	 * 
	 * @param roleid
	 */
	public void delete(String roleid, String uuid) {
		redisTemplate.delete(Constants.SBHZ_REDIS_WILD + roleid + "|" + uuid);
	}
	
	// 查找数组中是否含有此列
	public static boolean useArraysBinarySearch(List<Integer> arr, int targetValue) {
		for(int i : arr){
			if(i == targetValue){
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
	
}
