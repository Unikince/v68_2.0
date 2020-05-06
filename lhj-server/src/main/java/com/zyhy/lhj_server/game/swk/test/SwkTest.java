package com.zyhy.lhj_server.game.swk.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zyhy.common_lhj.WinLineInfo;
import com.zyhy.common_lhj.Window;
import com.zyhy.common_lhj.process.MainProcess;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.common_server.result.MessageClient;
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.common_server.util.RandomUtil;
import com.zyhy.lhj_server.game.swk.SwkScatterInfo;
import com.zyhy.lhj_server.prcess.result.swk.SwkGameBetResult;
import com.zyhy.lhj_server.prcess.result.swk.SwkScatterModelResult;
@Service
public class SwkTest {
	private  String roleid = "802";
	private  String uuid = "1";
	@Autowired
	private MainProcess mainProcess;
	// 下注线数
	private   int line = 15;
	// 下注线注
	private   double lineBet = 5;
	// 下注次数
	private   int betCount = 100000;
	// 统计轮数	
	private  int round = 1;
	// 正常游戏次数
	private   int gameCount = 0;
	// 正常游戏中奖次数
	private   int totalCount = 0;
	// 正常游戏奖励
	private   double totalReward = 0;
	// wild游戏奖励
	private   double wildReward = 0;
	// 免费游戏触发次数
	private   int freeCount = 0;
	// 免费游戏总次数
	private   int totoalFreeCount = 0;
	// 免费游戏奖励
	private   double freeGameReward = 0;
	// 正常游戏奖励信息(K：图标名称 V：K：中奖个数 V：中奖次数)
	private   Map<String, Map<Integer, Integer>> countmap = new HashMap<>();
	private   Map<String, Map<Integer, Double>> map = new HashMap<>();
	// wild游戏奖励信息
	private   Map<String, Map<Integer, Integer>> countwildMap = new HashMap<>();
	private   Map<String, Map<Integer, Double>> wildMap = new HashMap<>();
	// 免费游戏奖励信息
	private   Map<Integer, Map<Integer, Double>> freeMap = new HashMap<>();
	
	public  void initTest() throws IOException, InterruptedException {
		testOpenGame();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd _HH_mm");
		Date date = new Date(System.currentTimeMillis());
		String format = sdf.format(date);
		String path = "C:\\Users\\Administrator\\Desktop\\shuzhi\\swk\\2";
		String name = "swk-" + format +".txt";
		File file = new File(path, name);
		System.out.println(file.createNewFile());
		for (int i = 1; i <= round; i++) {
			String testLogin1 = testLogin();
			SwkResult result = testAuto();
			String testLogin2 = testLogin();
			String filename = i + ".==========>\r\n";
			String content1 = "统计结果: " + result.toString() + "\r\n";
			String content2 = "不包含WILD统计结果: " + map.toString()+ "\r\n";
			String content3 = "不包含WILD次数统计: " + countmap.toString()+ "\r\n";
			String content4 = "包含WILD统计结果: " + wildMap.toString()+ "\r\n";
			String content5 = "包含WILD次数统计: " + countwildMap.toString()+ "\r\n";
			String content6 = "免费游戏统计结果: " + freeMap.toString()+ "\r\n";
			String content7 = "游戏开始金币: " + testLogin1.toString()+ "\r\n";
			String content8 = "游戏结束金币: " + testLogin2.toString()+ "\r\n";
			String output = filename + content1 + content2 + content3 + content4 + content5 + content6 + content7 + content8;
			FileWriter fw = new FileWriter(file,true);    
			fw.write(output,0,output.length());   
			String clean = clean();
			fw.write(clean,0,clean.length());   
			fw.flush();
		}
	}
	public  String clean(){
		// 正常游戏次数
		gameCount = 0;
		// 正常游戏中奖次数
		totalCount = 0;
		// 正常游戏奖励
		totalReward = 0;
		// wild游戏奖励
		wildReward = 0;
		// 免费游戏触发次数
		freeCount = 0;
		// 免费游戏总次数
		totoalFreeCount = 0;
		// 免费游戏奖励
		freeGameReward = 0;
		// 正常游戏奖励信息(K：图标名称 V：K：中奖个数 V：中奖次数)
		countmap.clear();
		map.clear();
		// wild游戏奖励信息
		countwildMap.clear();
		wildMap.clear();
		// 免费游戏奖励信息
		freeMap.clear();
		String result =  "[清除数据成功!当前数据 : ]"
				+ "gameCount = " + gameCount
				+ ", totalCount = " + totalCount
				+ ", totalReward = " + totalReward
				+ ", wildReward = " + wildReward
				+ ", freeCount = " + freeCount
				+ ", totoalFreeCount = " + totoalFreeCount
				+ ", freeGameReward = " + freeGameReward
				+ ", countmap = " + countmap.size()
				+ ", map = " + map.size()
				+ ", countwildMap = " + countwildMap.size()
				+ ", wildMap = " + wildMap.size()
				+ ", freeMap = " + freeMap.size()
				+"\r\n"
				;
		return result;
	}
	
	/**
	 * 自动测试
	 * @return 
	 * @throws InterruptedException 
	 */
	public  SwkResult testAuto() throws InterruptedException{
		// 统计结果
		SwkResult yzhxResult = new SwkResult();
		// 开始测试
		for (int i = 0; i < betCount; i++) {
			//Thread.sleep(20L);
			gameCount ++;
			System.out.println("目前自动运行次数: " + gameCount);
			SwkGameBetResult result = testStartGame(line,lineBet);
			//System.out.println("正常游戏result: " + result);
			Boolean scatter = result.isScatter();
			if (scatter) {
				freeCount ++;
				SwkScatterModelResult freeGame = freeGame();
				//System.out.println("选择模式result: " + freeGame);
				SwkScatterInfo jsonObject = freeGame.getScatter();
				int model = jsonObject.getModel();
				double totalReward = 0;
				while (true) {
					totoalFreeCount ++;
					SwkGameBetResult freeResult = testStartGame(line,lineBet);
					//System.out.println("免费游戏result: " + freeResult);
					int scatterNum = freeResult.getScatterNum();
					Double reward = freeResult.getRewardcoin();
					totalReward += reward;
					if (scatterNum == 0) {
						break;
					}
				}
				freeGameReward += totalReward;
				if (freeMap.containsKey(model)) {
					Map<Integer, Double> map2 = freeMap.get(model);
					for (Integer count : map2.keySet()) {
						Double double1 = map2.get(count);
						double1 += totalReward;
						int count1 = count +1;
						map2.clear();
						map2.put(count1, double1);
					}
				} else {
					Map<Integer, Double> map2 = new HashMap<>();
					map2.put(1, totalReward);
					freeMap.put(model, map2);
				}
			}
			dataRecord(result);
		}
		yzhxResult.setBetCount(betCount);
		yzhxResult.setTotalBet(lineBet*line*betCount);
		yzhxResult.setTotalCount(totalCount);
		yzhxResult.setTotalreward(totalReward);
		yzhxResult.setFreeCount(freeCount);
		yzhxResult.setWildReward(wildReward);
		yzhxResult.setTotoalFreeCount(totoalFreeCount);
		yzhxResult.setFreeGameReward(freeGameReward);
		yzhxResult.setGameCount(gameCount);
		yzhxResult.setTotalIncomelv((totalReward + freeGameReward + wildReward)/(lineBet*line*betCount));
		yzhxResult.setTotalRewardlv(NumberTool.divide(totalCount, betCount).doubleValue());
		System.out.println("统计结果: " + yzhxResult);
		System.out.println("不包含WILD统计结果: " + map);
		System.out.println("包含WILD统计结果: " + wildMap);
		System.out.println("免费游戏统计结果: " + freeMap);
		return yzhxResult;
	}
	
	/**
	 * 测试老虎机登陆
	 */
	public  String testLogin(){
		int messageid = 2000;
		
		MessageClient mc = new MessageClient();
		mc.setMessageid(messageid);
		mc.setUuid(uuid);
		mc.setSid("");
		mc.setType(0);
		Map<String, String> map = new HashMap<>();
		map.put("messageid", String.valueOf(messageid));
		map.put("roleid", roleid);
		map.put("uuid", uuid);
		mc.setParams(map);
		HttpMessageResult result = mainProcess.mainProcessMsg(mc);
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 测试老虎机界面返回
	 */
	public  void testOpenGame(){
		int messageid = 2001;
		
		MessageClient mc = new MessageClient();
		mc.setMessageid(messageid);
		mc.setUuid(uuid);
		mc.setSid("");
		mc.setType(0);
		Map<String, String> map = new HashMap<>();
		map.put("messageid", String.valueOf(messageid));
		map.put("roleid", roleid);
		map.put("uuid", uuid);
		mc.setParams(map);
		mainProcess.mainProcessMsg(mc);
	}
	
	
	/**
	 * 测试老虎机正常游戏
	 * @return 
	 */
	public  SwkGameBetResult testStartGame(int line,double lineBet){
		int messageid = 2002;
		
		MessageClient mc = new MessageClient();
		mc.setMessageid(messageid);
		mc.setUuid(uuid);
		mc.setSid("");
		mc.setType(0);
		Map<String, String> map = new HashMap<>();
		map.put("messageid", String.valueOf(messageid));
		map.put("roleid", roleid);
		map.put("num", String.valueOf(line));
		map.put("jetton", String.valueOf(lineBet));
		map.put("uuid", uuid);
		mc.setParams(map);
		SwkGameBetResult result = (SwkGameBetResult) mainProcess.mainProcessMsg(mc);
		return result;
	}
	
	/**
	 * 免费游戏
	 * @return 
	 */
	public  SwkScatterModelResult freeGame(){
		int messageid = 2003;
		
		MessageClient mc = new MessageClient();
		mc.setMessageid(messageid);
		mc.setUuid(uuid);
		mc.setSid("");
		mc.setType(0);
		Map<String, String> map = new HashMap<>();
		map.put("messageid", String.valueOf(messageid));
		map.put("roleid", roleid);
		map.put("model", String.valueOf(RandomUtil.getRandom(1, 3)));
		map.put("uuid", uuid);
		mc.setParams(map);
		SwkScatterModelResult result = (SwkScatterModelResult) mainProcess.mainProcessMsg(mc);
		return result;
	}
	
	/**
	 * 正常游戏数据记录
	 * @param yzhxResult 
	 */
	public  void dataRecord(SwkGameBetResult result){
		double rewardcoin = result.getRewardcoin();
		if (rewardcoin > 0) {
			boolean iswild = false;
			totalCount += 1;
			List<WinLineInfo> winrouteinfos = result.getWinrouteinfos();
			for (WinLineInfo wl : winrouteinfos) {
				//System.out.println("wl" + wl);
				String icon = wl.getIcon().getName();
				int num = wl.getNum();
				//System.out.println("icon" + icon);
				//System.out.println("num" + num);
				Double reward = wl.getReward();
				List<Window> windows = wl.getWindows();
				//System.out.println("windows" + windows);
				int wild = 0;
				for (Window wl2 : windows) {
					String icon2 = wl2.getIcon().getName();
					//System.out.println("icon2" + icon2);
					if (icon2.equalsIgnoreCase("WILD")) {
						wild ++;
					}
					//System.out.println("wild" + wild);
				}
				if (wild > 0) {
					iswild = true;
					if (wildMap.containsKey(icon)) {
						Map<Integer, Double> map2 = wildMap.get(icon);
						if (map2.containsKey(num)) {
							map2.put(num, map2.get(num) + reward);
						} else {
							map2.put(num, reward);
						}
					} else{
						Map<Integer, Double> count = new HashMap<>();
						count.put(num, reward);
						wildMap.put(icon, count);
					}
					
					if (countwildMap.containsKey(icon)) {
						Map<Integer, Integer> map2 = countwildMap.get(icon);
						if (map2.containsKey(num)) {
							map2.put(num, map2.get(num) + 1);
						} else {
							map2.put(num, 1);
						}
					} else{
						Map<Integer, Integer> count = new HashMap<>();
						count.put(num, 1);
						countwildMap.put(icon, count);
					}
					wildReward += reward;
				} else {
					iswild = false;
					if (map.containsKey(icon)) {
						Map<Integer, Double> map2 = map.get(icon);
						if (map2.containsKey(num)) {
							map2.put(num, map2.get(num) + reward);
						} else {
							map2.put(num, reward);
						}
					} else{
						Map<Integer, Double> count = new HashMap<>();
						count.put(num, reward);
						map.put(icon, count);
					}
					
					if (countmap.containsKey(icon)) {
						Map<Integer, Integer> map2 = countmap.get(icon);
						if (map2.containsKey(num)) {
							map2.put(num, map2.get(num) + 1);
						} else {
							map2.put(num, 1);
						}
					} else{
						Map<Integer, Integer> count = new HashMap<>();
						count.put(num, 1);
						countmap.put(icon, count);
					}
					totalReward += reward;
				}
			}
		}
	}
}
