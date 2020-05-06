package com.zyhy.lhj_server.game.yzhx.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zyhy.common_lhj.WinLineInfo;
import com.zyhy.common_lhj.process.MainProcess;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.common_server.result.MessageClient;
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.lhj_server.prcess.result.yzhx.YzhxGameBetResult;
@Service
public class YzhxTest {
	private  String roleid = "802";
	private  String uuid = "1";
	@Autowired
	private MainProcess mainProcess;
	// 正常游戏奖励
	private  double totalReward = 0;
	// 免费游戏奖励
	private  double freeGameReward = 0;
	// 总中奖次数
	private  int totalCount = 0;
	// 下注次数
	private  int betCount = 100000;
	// 测试轮数
	private   int round = 1;
	// 实际游戏次数
	private  int gameCount = 0;
	// 下注线数
	private  int line = 40;
	// 下注线注
	private  double lineBet = 2.5;
	// 正常游戏奖励信息(K：图标名称 V：K：中奖个数 V：中奖次数)
	private  Map<String, Map<Integer, Integer>> map = new HashMap<>();
	// 免费游戏奖励信息
	private  Map<Integer, Map<Integer, Double>> freeMap = new HashMap<>();
	
	public  void initTest() throws IOException, InterruptedException {
		testOpenGame();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd _HH_mm");
		Date date = new Date(System.currentTimeMillis());
		String format = sdf.format(date);
		String path = "C:\\Users\\Administrator\\Desktop\\shuzhi\\yzhx\\1";
		String name = "yzhx-" + format +".txt";
		File file = new File(path, name);
		System.out.println(file.createNewFile());
		for (int i = 1; i <= round; i++) {
			String testLogin1 = testLogin();
			YzhxResult result = testAuto();
			String testLogin2 = testLogin();
			String filename = i + ".==========>\r\n";
			String content1 = "统计结果: " + result.toString() + "\r\n";
			String content2 = "正常游戏统计结果: " + map.toString()+ "\r\n";
			String content3 = "免费游戏统计结果: " + freeMap.toString()+ "\r\n";
			String content4 = "游戏开始金币: " + testLogin1.toString()+ "\r\n";
			String content5 = "游戏结束金币: " + testLogin2.toString()+ "\r\n";
			String output = filename + content1 + content2 + content3 + content4 + content5;
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
		// 免费游戏奖励
		freeGameReward = 0;
		// 总中奖次数
		totalCount = 0;
		// 实际游戏次数
		gameCount = 0;
		// 正常游戏奖励信息(K：图标名称 V：K：中奖个数 V：中奖次数)
		map.clear();
		// 免费游戏奖励信息
		freeMap.clear();
		String result =  "[清除数据成功!当前数据 : ]"
				+ "totalReward = " + totalReward
				+ ", freeGameReward = " + freeGameReward
				+ ", totalCount = " + totalCount
				+ ", gameCount = " + gameCount
				+ ", map = " + map.size()
				+ ", freeMap = " + map.size()
				+"\r\n"
				;
		return result;
	}
	
	
	/**
	 * 测试老虎机登陆
	 * @return 
	 */
	public String testLogin(){
		int messageid = 2700;
		
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
		int messageid = 2701;
		
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
	public  YzhxGameBetResult testStartGame(int line,double lineBet){
		int messageid = 2702;
		
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
		YzhxGameBetResult result = (YzhxGameBetResult) mainProcess.mainProcessMsg(mc);
		return result;
	}
	
	/**
	 * 测试老虎机免费游戏
	 * @return 
	 */
	public  YzhxGameBetResult testFreeGame(int line,double lineBet){
		int messageid = 2703;
		
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
		YzhxGameBetResult result = (YzhxGameBetResult) mainProcess.mainProcessMsg(mc);
		return result;
	}
	
	/**
	 * 自动测试
	 * @return 
	 * @throws InterruptedException 
	 */
	public  YzhxResult testAuto() throws InterruptedException{
		// 统计结果
		YzhxResult yzhxResult = new YzhxResult();
		// 开始测试
		for (int i = 0; i < betCount; i++) {
			gameCount ++;
			System.out.println("目前自动运行次数: " + gameCount);
			YzhxGameBetResult result = testStartGame(line,lineBet);
			dataRecord(result);
			//System.out.println("正常游戏result: " + result);
			int freeNum = result.getFreeNum();
			if (freeNum > 0) {
				// scatter个数
				int scatterCount = 0;
				// 本次免费游戏奖励
				double reward = 0;
				int[] scatterNum = { 0, 0, 15, 25, 50 };
				for (int j = 0; j < scatterNum.length; j++) {
					if (freeNum == scatterNum[j] ) {
						scatterCount = j+1;
					}
				}
				while (freeNum != 0) {
					YzhxGameBetResult freeResult = testFreeGame(line,lineBet);
					double rewardcoin = freeResult.getRewardcoin();
					reward += rewardcoin;
					freeNum = freeResult.getFreeNum();
					//System.out.println("免费游戏result: " + freeResult);
				}
				if (freeMap.containsKey(scatterCount)) {
					Map<Integer, Double> map2 = freeMap.get(scatterCount);
					Set<Integer> keySet = map2.keySet();
					Iterator<Integer> iterator = keySet.iterator();
					Integer num = iterator.next();
					double gold =  map2.get(num);
					map2.clear();
					map2.put(num + 1, gold + reward);
				} else {
					Map<Integer, Double> map2 = new HashMap<>();
					map2.put(1, reward);
					freeMap.put(scatterCount, map2);
				}
				freeGameReward += reward;
			}
		}
		yzhxResult.setBetCount(betCount);
		yzhxResult.setTotalBet(lineBet*line*betCount);
		yzhxResult.setTotalCount(totalCount);
		yzhxResult.setTotalreward(totalReward);
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
	 * 正常游戏数据记录
	 * @param yzhxResult 
	 */
	public  void dataRecord(YzhxGameBetResult result){
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
	
}
