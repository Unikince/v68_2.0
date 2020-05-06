package com.zyhy.lhj_server.game.sbhz.test;

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
import com.zyhy.lhj_server.prcess.result.sbhz.SbhzGameBetResult;
@Service
public class SbhzTest {
	private  String roleid = "802";
	private  String uuid = "1";
	@Autowired
	private MainProcess mainProcess;
	// 正常游戏奖励
	private   double totalReward = 0;
	// wild_1奖励
	private   double wild_1Reward = 0;
	// wild_2奖励
	private   double wild_2Reward = 0;
	// wild_3奖励
	private   double wild_3Reward = 0;
	// wild_1次数
	private   int wild_1 = 0;
	// wild_2次数
	private   int wild_2 = 0;
	// wild_3次数
	private   int wild_3 = 0;
	// 免费wild_3次数
	private   int freewild_3 = 0;
	// 免费wild_3奖励
	private   double freewild_3Reward = 0;
	// 正常游戏中奖次数
	private   int totalCount = 0;
	// 下注次数
	private   int betCount = 100000;
	// 测试轮数
	private   int round = 1;
	// 实际游戏次数
	private   int gameCount = 0;
	// 下注线数
	private   int line = 5;
	// 下注线注
	private   double lineBet = 25;
	// 正常游戏奖励信息(K：图标名称 V：K：中奖个数 V：中奖次数)
	private   Map<String, Map<Integer, Integer>> map = new HashMap<>();
	// 猴子重转奖励信息
	private   Map<String, Map<Integer, Integer>> freeMap = new HashMap<>();
	
	public  void initTest() throws IOException, InterruptedException {
		testOpenGame();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd _HH_mm");
		Date date = new Date(System.currentTimeMillis());
		String format = sdf.format(date);
		String path = "C:\\Users\\Administrator\\Desktop\\shuzhi\\sbhz\\1";
		String name = "sbhz-" + format +".txt";
		File file = new File(path, name);
		System.out.println(file.createNewFile());
		for (int i = 1; i <= round; i++) {
			String testLogin1 = testLogin();
			SbhzResult result = testAuto();
			String testLogin2 = testLogin();
			String filename = i + ".==========>\r\n";
			String content1 = "统计结果: " + result.toString() + "\r\n";
			String content2 = "正常游戏统计结果: " + map.toString()+ "\r\n";
			String content3 = "重转游戏统计结果:: " + freeMap.toString()+ "\r\n";
			String content4 = "游戏开始金币: " + testLogin1.toString()+ "\r\n";
			String content5 = "游戏结束金币: " + testLogin2.toString()+ "\r\n";
			String output = filename + content1 + content2 + content3 + content4 + content5 ;
			FileWriter fw = new FileWriter(file,true);    
			fw.write(output,0,output.length());   
			String clean = clean();
			fw.write(clean,0,clean.length());   
			fw.flush();
		}
	}
	public  String clean(){
		// 正常游戏奖励
		totalReward = 0;
		// wild_1奖励
		wild_1Reward = 0;
		// wild_2奖励
		wild_2Reward = 0;
		wild_3Reward = 0;
		// wild_1次数
		wild_1 = 0;
		// wild_2次数
		wild_2 = 0;
		// wild_3次数
		wild_3 = 0;
		// 重转中wild_3次数
		freewild_3 = 0;
		freewild_3Reward = 0;
		// 正常游戏中奖次数
		totalCount = 0;
		// 实际游戏次数
		gameCount = 0;
		// 正常游戏奖励信息(K：图标名称 V：K：中奖个数 V：中奖次数)
		map.clear();
		// 猴子重转奖励信息
		freeMap.clear();
		String result =  "[清除数据成功!当前数据 : ]"
				+ "totalReward = " + totalReward
				+ ", wild_1Reward = " + wild_1Reward
				+ ", wild_2Reward = " + wild_2Reward
				+ ", wild_3Reward = " + wild_3Reward
				+ ", wild_1 = " + wild_1
				+ ", wild_2 = " + wild_2
				+ ", wild_3 = " + wild_3
				+ ", freewild_3 = " + freewild_3
				+ ", freewild_3Reward = " + freewild_3Reward
				+ ", totalCount = " + totalCount
				+ ", gameCount = " + gameCount
				+ ", map = " + map.size()
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
	public  SbhzResult testAuto() throws InterruptedException{
		// 统计结果
		SbhzResult yzhxResult = new SbhzResult();
		// 开始测试
		for (int i = 0; i < betCount; i++) {
			gameCount ++;
			System.out.println("目前自动运行次数: " + gameCount);
			SbhzGameBetResult result = testStartGame(line,lineBet);
			dataRecord(result);
			//System.out.println("正常游戏result: " + result);
			Boolean turned = result.isTurned();
			if (turned) {
				while (true) {
					SbhzGameBetResult freeResult = testFreeGame();
					//System.out.println("猴子重转result: " + freeResult);
					if (freeResult.getRewardcoin() > 0) {
						freeDataRecord(freeResult);
						break;
					}
				}
			}
		}
		yzhxResult.setBetCount(betCount);
		yzhxResult.setTotalBet(lineBet*line*betCount);
		yzhxResult.setTotalCount(totalCount);
		yzhxResult.setTotalreward(totalReward);
		yzhxResult.setGameCount(gameCount);
		yzhxResult.setWild_1(wild_1);
		yzhxResult.setWild_2(wild_2);
		yzhxResult.setWild_3(wild_3);
		yzhxResult.setFreewild_3(freewild_3);
		yzhxResult.setFreewild_3Reward(freewild_3Reward);
		yzhxResult.setWild_1Reward(wild_1Reward);
		yzhxResult.setWild_2Reward(wild_2Reward);
		yzhxResult.setWild_3Reward(wild_3Reward);
		yzhxResult.setTotalIncomelv((totalReward + wild_1Reward + wild_2Reward + wild_3Reward + freewild_3Reward)/(lineBet*line*betCount));
		yzhxResult.setTotalRewardlv(NumberTool.divide(totalCount, betCount).doubleValue());
		System.out.println("统计结果: " + yzhxResult);
		System.out.println("正常游戏统计结果: " + map);
		System.out.println("重转游戏统计结果: " + freeMap);
		return yzhxResult;
	}
	
	/**
	 * 测试老虎机登陆
	 * @return 
	 */
	public  String testLogin(){
		int messageid = 1800;
		
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
		int messageid = 1801;
		
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
	public  SbhzGameBetResult testStartGame(int line,double lineBet){
		int messageid = 1802;
		
		MessageClient mc = new MessageClient();
		mc.setMessageid(messageid);
		mc.setUuid(uuid);
		mc.setSid("");
		mc.setType(0);
		Map<String, String> map = new HashMap<>();
		map.put("messageid", String.valueOf(messageid));
		map.put("roleid", roleid);
		map.put("jetton", String.valueOf(lineBet));
		map.put("uuid", uuid);
		mc.setParams(map);
		SbhzGameBetResult result = (SbhzGameBetResult) mainProcess.mainProcessMsg(mc);
		return result;
	}
	
	/**
	 * 猴子重转
	 * @return 
	 */
	public  SbhzGameBetResult testFreeGame(){
		int messageid = 1803;
		
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
		SbhzGameBetResult result = (SbhzGameBetResult) mainProcess.mainProcessMsg(mc);
		return result;
	}
	
	
	
	/**
	 * 正常游戏数据记录
	 * @param yzhxResult 
	 */
	public  void dataRecord(SbhzGameBetResult result){
		
		boolean monkey1 = result.isMonkey1();
		if (monkey1) {
			wild_3 ++;
			wild_3Reward += result.getRewardcoin();
			return;
		}
		
		double rewardcoin = result.getRewardcoin();
		if (rewardcoin > 0) {
			totalCount += 1;
			totalReward += rewardcoin;
			List<WinLineInfo> winrouteinfos = result.getWinrouteinfos();
			if (winrouteinfos != null) {
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
	}
	
	/**
	 * 免费游戏数据记录
	 * @param yzhxResult 
	 */
	public  void freeDataRecord(SbhzGameBetResult freeResult){
		boolean monkey1 = freeResult.isMonkey1();
		if (monkey1) {
			freewild_3 ++;
			freewild_3Reward += freeResult.getRewardcoin();
			return;
		}
		
		double rewardcoin = freeResult.getRewardcoin();
		List<Integer> wilds = freeResult.getWilds();
		
		if (wilds.size() == 1) {
			wild_1 ++;
			wild_1Reward += rewardcoin;
		}
		if (wilds.size() == 2) {
			wild_2 ++;
			wild_2Reward += rewardcoin;
		}
		
		if (rewardcoin > 0) {
			List<WinLineInfo> winrouteinfos = freeResult.getWinrouteinfos();
			if (winrouteinfos != null) {
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
	
}
