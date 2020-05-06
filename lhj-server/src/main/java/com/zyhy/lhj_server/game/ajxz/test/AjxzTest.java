package com.zyhy.lhj_server.game.ajxz.test;

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
import com.zyhy.common_lhj.process.MainProcess;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.common_server.result.MessageClient;
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.common_server.util.RandomUtil;
import com.zyhy.lhj_server.prcess.result.ajxz.AjxzGameBetResult;
@Service
public class AjxzTest {
	private  String roleid = "802";
	private  String uuid = "1";
	@Autowired
	private MainProcess mainProcess;
	// 下注次数
	private   int betCount = 100000;
	// 测试轮数
	private  int round = 1;
	// 下注线数
	private   int line = 50;
	// 下注线注
	private   double lineBet = 1;
	// 正常游戏次数
	private   int gameCount = 0;
	// 正常游戏中奖次数
	private   int totalCount = 0;
	// 正常游戏奖励
	private   double totalReward = 0;
	// 免费游戏触发次数
	private   int freeCount = 0;
	// 免费游戏总次数
	private   int totoalFreeCount = 0;
	// 免费游戏奖励
	private   double freeGameReward = 0;
	// 正常游戏奖励信息(K：图标名称 V：K：中奖个数 V：中奖次数)
	private   Map<String, Map<Integer, Integer>> map = new HashMap<>();
	// 免费游戏奖励信息
	private   Map<String, Map<Integer, Integer>> freeMap = new HashMap<>();
	// 免费模式选择统计
	private   Map<Integer, Integer> freemodelMap = new HashMap<>();
	// 每个模式游戏次数
	private   Map<Integer, Integer> freecountMap = new HashMap<>();
	
	public  void initTest() throws IOException, InterruptedException {
		testOpenGame();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd _HH_mm");
		Date date = new Date(System.currentTimeMillis());
		String format = sdf.format(date);
		String path = "C:\\Users\\Administrator\\Desktop\\shuzhi\\ajxz\\1";
		String name = "ajxz-"+ format +".txt";
		File file = new File(path, name);
		System.out.println(file.createNewFile());
		for (int i = 1; i <= round; i++) {
			String testLogin1 = testLogin();
			AjxzResult result = testAuto();
			String testLogin2 = testLogin();
			String filename = i + ".==========>\r\n";
			String content1 = "统计结果: " + result.toString() + "\r\n";
			String content2 = "正常游戏统计结果: " + map.toString()+ "\r\n";
			String content3 = "免费游戏统计结果: " + freeMap.toString()+ "\r\n";
			String content4 = "免费模式统计结果: " + freemodelMap.toString()+ "\r\n";
			String content5 = "免费模式游戏次数统计结果: " + freecountMap.toString()+ "\r\n";
			String content6 = "游戏开始金币: " + testLogin1.toString()+ "\r\n";
			String content7 = "游戏结束金币: " + testLogin2.toString()+ "\r\n";
			String output = filename + content1 + content2 + content3 + content4 + content5 + content6 + content7;
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
		// 免费游戏触发次数
		freeCount = 0;
		// 免费游戏总次数
		totoalFreeCount = 0;
		// 免费游戏奖励
		freeGameReward = 0;
		// 正常游戏奖励信息(K：图标名称 V：K：中奖个数 V：中奖次数)
		map.clear();
		// 免费游戏奖励信息
		freeMap.clear();
		freemodelMap.clear();
		freecountMap.clear();
		String result =  "[清除数据成功!当前数据 : ]"
				+ "gameCount = " + gameCount
				+ ", totalCount = " + totalCount
				+ ", totalReward = " + totalReward
				+ ", freeCount = " + freeCount
				+ ", totoalFreeCount = " + totoalFreeCount
				+ ", freeGameReward = " + freeGameReward
				+ ", map = " + map.size()
				+ ", freeMap = " + freeMap.size()
				+ ", freemodelMap = " + freemodelMap.size()
				+ ", freecountMap = " + freecountMap.size()
				+"\r\n"
				;
		return result;
	}
	
	/**
	 * 自动测试
	 * @return 
	 * @throws InterruptedException 
	 */
	public  AjxzResult testAuto() throws InterruptedException{
		// 统计结果
		AjxzResult yzhxResult = new AjxzResult();
		// 开始测试
		for (int i = 0; i < betCount; i++) {
			//Thread.sleep(20L);
			gameCount ++;
			System.out.println("目前自动运行次数: " + gameCount);
			AjxzGameBetResult result = testStartGame(line,lineBet);
			dataRecord(result);
			//System.out.println("正常游戏result: " + result);
			boolean free = result.isFree();
			if (free) {
				freeCount ++;
				Integer[] type = {5,10,15};
				int random = RandomUtil.getRandom(type);
				//int random = 15;
				if (freemodelMap.containsKey(random)) {
					freemodelMap.put(random, freemodelMap.get(random) + 1);
				} else {
					freemodelMap.put(random, 1);
				}
				while (true) {
					totoalFreeCount ++;
					if (freecountMap.containsKey(random)) {
						freecountMap.put(random, freecountMap.get(random) + 1);
					} else {
						freecountMap.put(random, 1);
					}
					AjxzGameBetResult freeResult = testFreeGame1(random);
					int freeNum = freeResult.getFreeNum();
					double rewardcoin = freeResult.getRewardcoin();
					freeGameReward += rewardcoin;
					freedataRecord(freeResult);
					//System.out.println("免费游戏result: " + freeResult);
					if (freeNum <= 0) {
						break;
					}
				}
				
			}
		}
		yzhxResult.setBetCount(betCount);
		yzhxResult.setTotalBet(lineBet*line*betCount);
		yzhxResult.setTotalCount(totalCount);
		yzhxResult.setTotalreward(totalReward);
		yzhxResult.setFreeCount(freeCount);
		yzhxResult.setTotoalFreeCount(totoalFreeCount);
		yzhxResult.setFreeGameReward(freeGameReward);
		yzhxResult.setGameCount(gameCount);
		yzhxResult.setTotalIncomelv((totalReward + freeGameReward)/(lineBet*line*betCount));
		yzhxResult.setTotalRewardlv(NumberTool.divide(totalCount, betCount).doubleValue());
		System.out.println("统计结果: " + yzhxResult);
		System.out.println("正常游戏统计结果: " + map);
		System.out.println("免费游戏统计结果: " + freeMap);
		return yzhxResult;
	}
	
	/**
	 * 测试老虎机登陆
	 * @return 
	 */
	public  String testLogin(){
		int messageid = 2600;
		
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
		int messageid = 2601;
		
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
	public  AjxzGameBetResult testStartGame(int line,double lineBet){
		int messageid = 2602;
		
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
		AjxzGameBetResult result = (AjxzGameBetResult) mainProcess.mainProcessMsg(mc);
		return result;
	}
	/**
	 * 免费游戏
	 * @return 
	 */
	public  AjxzGameBetResult testFreeGame1(int type){
		int messageid = 2603;
		
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
		map.put("type", String.valueOf(type));
		map.put("uuid", uuid);
		mc.setParams(map);
		AjxzGameBetResult result = (AjxzGameBetResult) mainProcess.mainProcessMsg(mc);
		return result;
	}
	
	/**
	 * 正常游戏数据记录
	 * @param yzhxResult 
	 */
	public  void dataRecord(AjxzGameBetResult result){
		double rewardcoin = result.getRewardcoin();
		if (rewardcoin > 0) {
			totalCount += 1;
			totalReward += rewardcoin;
			List<WinLineInfo> winrouteinfos = result.getWinrouteinfos();
			for (WinLineInfo wl : winrouteinfos) {
				//System.out.println("wl" + wl);
				String icon = wl.getIcon().getName();
				int num = wl.getNum();
				//System.out.println("icon" + icon);
				//System.out.println("num" + num);
				if (map.containsKey(icon)) {
					Map<Integer, Integer> map2 = map.get(icon);
					if (map2.containsKey(num)) {
						map2.put(num, map2.get(num) + 1);
					} else {
						map2.put(num, 1);
					}
				} else{
					Map<Integer, Integer> count = new HashMap<>();
					count.put(num, 1);
					map.put(icon, count);
				}
			}
		}
	}
	
	/**
	 * 免费游戏数据记录
	 * @param yzhxResult 
	 */
	public  void freedataRecord(AjxzGameBetResult freeResult){
		double rewardcoin = freeResult.getRewardcoin();
		if (rewardcoin > 0) {
			//totalCount += 1;
			//totalReward += rewardcoin;
			List<WinLineInfo> winrouteinfos = freeResult.getWinrouteinfos();
			for (WinLineInfo wl : winrouteinfos) {
				//System.out.println("wl" + wl);
				String icon = wl.getIcon().getName();
				int num = wl.getNum();
				//System.out.println("icon" + icon);
				//System.out.println("num" + num);
				if (freeMap.containsKey(icon)) {
					Map<Integer, Integer> map2 = freeMap.get(icon);
					if (map2.containsKey(num)) {
						map2.put(num, map2.get(num) + 1);
					} else {
						map2.put(num, 1);
					}
				} else{
					Map<Integer, Integer> count = new HashMap<>();
					count.put(num, 1);
					freeMap.put(icon, count);
				}
			}
		}
	}
}
