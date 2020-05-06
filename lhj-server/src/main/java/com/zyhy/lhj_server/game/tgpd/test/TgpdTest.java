package com.zyhy.lhj_server.game.tgpd.test;

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
import com.zyhy.lhj_server.prcess.result.tgpd.TgpdGameBetResult;
@Service
public class TgpdTest {
	@Autowired
	private MainProcess mainProcess;
	private  String roleid = "802";
	private  String uuid = "1";
	// 下注线数
	private   int line = 5;
	// 下注线注
	private   double lineBet = 2;
	// 下注次数
	private   int betCount = 1000000;
	private   int roundCount = 0;
	// 测试轮数
	private   int round = 1;
	// 正常游戏次数
	private   int gameCount = 0;
	// 正常游戏下注次数
	private   int gameCount1 = 0;
	private   int gameCount2 = 0;
	private   int gameCount3 = 0;
	// 正常游戏中奖次数
	private   int totalCount1 = 0;
	private   int totalCount2 = 0;
	private   int totalCount3= 0;
	// 正常游戏奖励
	private  double totalReward1 = 0;
	private  double totalReward2 = 0;
	private  double totalReward3 = 0;
	// 免费游戏触发次数
	private   int freeCount1 = 0;
	private   int freeCount2 = 0;
	private   int freeCount3 = 0;
	// 免费游戏中奖次数
	private   int totoalFreeCount1 = 0;
	private   int totoalFreeCount2 = 0;
	private   int totoalFreeCount3 = 0;
	// 免费游戏奖励
	private   double freeGameReward1 = 0;
	private   double freeGameReward2 = 0;
	private   double freeGameReward3 = 0;
	
	// 正常游戏奖励信息(K：图标名称 V：K：中奖个数 V：中奖次数)
	private   Map<String, Map<Integer, Integer>> map1 = new HashMap<>();
	private   Map<String, Map<Integer, Integer>> map2 = new HashMap<>();
	private   Map<String, Map<Integer, Integer>> map3 = new HashMap<>();
	// 免费游戏奖励信息
	private   Map<String, Map<Integer, Integer>> freeMap1 = new HashMap<>();
	private   Map<String, Map<Integer, Integer>> freeMap2 = new HashMap<>();
	private   Map<String, Map<Integer, Integer>> freeMap3 = new HashMap<>();
	private  int type = 0;
	
	public  void initTest() throws IOException, InterruptedException {
		testOpenGame();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd _HH_mm");
		Date date = new Date(System.currentTimeMillis());
		String format = sdf.format(date);
		String path = "C:\\Users\\Administrator\\Desktop\\shuzhi\\tgpd\\1";
		String name = "tgpd-"+ format +".txt";
		File file = new File(path, name);
		System.out.println(file.createNewFile());
		for (int i = 1; i <= round; i++) {
			String testLogin1 = testLogin();
			TgpdResult result = testAuto();
			String testLogin2 = testLogin();
			String filename = i + ".==========>\r\n";
			String content1 = "统计结果: " + result.toString() + "\r\n";
			String content2 = "正常游戏统计结果1: " + map1.toString()+ "\r\n";
			String content3 = "正常游戏统计结果2: " + map2.toString()+ "\r\n";
			String content4 = "正常游戏统计结果3: " + map3.toString()+ "\r\n";
			String content5 = "免费游戏统计结果1: " + freeMap1.toString()+ "\r\n";
			String content6 = "免费游戏统计结果2: " + freeMap2.toString()+ "\r\n";
			String content7 = "免费游戏统计结果3: " + freeMap3.toString()+ "\r\n";
			String content8 = "游戏开始金币: " + testLogin1.toString()+ "\r\n";
			String content9 = "游戏结束金币: " + testLogin2.toString()+ "\r\n";
			String output = filename + content1 + content2 + content3 + content4 
					+ content5 
					+ content6 
					+ content7 
					+ content8 + content9;
			FileWriter fw = new FileWriter(file,true);    
			fw.write(output,0,output.length());   
			String clean = clean();
			fw.write(clean,0,clean.length());   
			fw.flush();
		}
		testLogin();
	}
	public  String clean(){
		gameCount = 0;
		type = 0;
		// 正常游戏下注次数
		gameCount1 = 0;
		gameCount2 = 0;
		gameCount3 = 0;
		// 正常游戏中奖次数
		totalCount1 = 0;
		totalCount2 = 0;
		totalCount3= 0;
		// 正常游戏奖励
		totalReward1 = 0;
		totalReward2 = 0;
		totalReward3 = 0;
		// 免费游戏触发次数
		freeCount1 = 0;
		freeCount2 = 0;
		freeCount3 = 0;
		// 免费游戏总次数
		totoalFreeCount1 = 0;
		totoalFreeCount2 = 0;
		totoalFreeCount3 = 0;
		// 免费游戏奖励
		freeGameReward1 = 0;
		freeGameReward2 = 0;
		freeGameReward3 = 0;
		// 正常游戏奖励信息(K：图标名称 V：K：中奖个数 V：中奖次数)
		map1.clear();
		map2.clear();
		map3.clear();
		// 免费游戏奖励信息
		freeMap1.clear();
		freeMap2.clear();
		freeMap3.clear();
		String result =  "[清除数据成功!当前数据 : ]"
				+ "gameCount = " + gameCount
				+ "gameCount1 = " + gameCount1
				+ "gameCount2 = " + gameCount2
				+ "gameCount3 = " + gameCount3
				+ ", totalCount1 = " + totalCount1
				+ ", totalCount2 = " + totalCount2
				+ ", totalCount3 = " + totalCount3
				+ ", totalReward1 = " + totalReward1
				+ ", totalReward2 = " + totalReward2
				+ ", totalReward3 = " + totalReward3
				+ ", freeCount1 = " + freeCount1
				+ ", freeCount2 = " + freeCount2
				+ ", freeCount3 = " + freeCount3
				+ ", totoalFreeCount1 = " + totoalFreeCount1
				+ ", totoalFreeCount2 = " + totoalFreeCount2
				+ ", totoalFreeCount3 = " + totoalFreeCount3
				+ ", freeGameReward1 = " + freeGameReward1
				+ ", freeGameReward2 = " + freeGameReward2
				+ ", freeGameReward3 = " + freeGameReward3
				+ ", map1 = " + map1.size()
				+ ", map2 = " + map2.size()
				+ ", map3 = " + map3.size()
				+ ", freeMap1 = " + freeMap1.size()
				+ ", freeMap2 = " + freeMap2.size()
				+ ", freeMap3 = " + freeMap3.size()
				+"\r\n"
				;
		return result;
	}
	/**
	 * 自动测试
	 * @return 
	 * @throws InterruptedException 
	 */
	public  TgpdResult testAuto() throws InterruptedException{
		// 统计结果
		TgpdResult yzhxResult = new TgpdResult();
		// 开始测试
		for (int i = 0; i < betCount; i++) {
			//Thread.sleep(20L);
			gameCount ++;
			System.out.println("目前自动运行次数: " + gameCount);
			if (type == 0) {
				type = 1;
			}
			//System.out.println("本次正常游戏的type===========" +  type);
			TgpdGameBetResult jo = testStartGame(line,lineBet,type);
			TgpdGameBetResult result = jo;
			//System.out.println("正常游戏result: " + result);
			if (type == 1) {
				gameCount1 ++;
			} else if (type == 2) {
				gameCount2 ++;
			} else {
				gameCount3 ++;
			}
			dataRecord(result,type);
			
			if (!result.isReplenish()) {
				Integer temp = new Integer(type);
				type = result.getType();
				if (type == 1 && (temp - type) == 2) {
					roundCount ++;
					if (roundCount == 100) {
						betCount = i;
					}
					System.out.println("已经完成了"+ roundCount +"局游戏");
				}
				//System.out.println("本次正常游戏未掉落,更新后的type===========" +  type);
			}
			
			
			int free = 0;
			if (result.isReplenish()) {
				while (true) {
					//Thread.sleep(20L);
					//System.out.println("本次掉落游戏的type===========" +  type);
					TgpdGameBetResult dorpjo = dorpGame(type);
					TgpdGameBetResult dorpresult = dorpjo;
					//System.out.println("掉落游戏result: " + dorpresult);
					dataRecord(dorpresult,type);
					
					if(dorpresult.isFree()){
						free ++ ;
					}
					if (!dorpresult.isReplenish()) {
						Integer temp = new Integer(type);
						type = dorpresult.getType();
						if (type == 1 && (temp - type) == 2) {
							roundCount ++;
							if (roundCount == 100) {
								betCount = i;
							}
							System.out.println("已经完成了"+ roundCount +"局游戏");
						}
						//System.out.println("本次掉落游戏更新后的type===========" +  type);
						break;
					}
				}
			}
			
			if (result.isFree() || free > 0) {
				if (type == 1) {
					freeCount1 ++;
				} else if (type == 2) {
					freeCount2 ++;
				} else {
					freeCount3 ++;
				}
				while (true) {
					//Thread.sleep(20L);
					TgpdGameBetResult freejo = freeGame(type);
					TgpdGameBetResult freeresult = freejo;
					//System.out.println("免费游戏result: " + freeresult);
					freedataRecord(freeresult,type);
					if (!freeresult.isReplenish()) {
						type = freeresult.getType();
						//System.out.println("本次免费游戏未掉落,更新后的type===========" +  type);
					}
					//type = freeresult.getType();
					if (freeresult.isReplenish()) {
						while (true) {
							//Thread.sleep(20L);
							TgpdGameBetResult dorpjo1 = dorpGame(type);
							TgpdGameBetResult dorpresult1 = dorpjo1;
							//System.out.println("免费游戏中掉落result: " + dorpresult1);
							freedataRecord(dorpresult1,type);
							if (!dorpresult1.isReplenish()) {
								type = dorpresult1.getType();
								//System.out.println("本次免费掉落游戏更新后的type===========" +  type);
								break;
							}
						}
					}
					if (freeresult.getFreeNum() == 0) {
						break;
					}
				}
			}
		}
		
		yzhxResult.setBetCount(betCount);
		yzhxResult.setTotalBet(lineBet*line*(gameCount1 + gameCount2 + gameCount3));
		yzhxResult.setGameCount(gameCount);
		yzhxResult.setGameCount1(gameCount1);
		yzhxResult.setGameCount2(gameCount2);
		yzhxResult.setGameCount3(gameCount3);
		yzhxResult.setTotalCount1(totalCount1);
		yzhxResult.setTotalCount2(totalCount2);
		yzhxResult.setTotalCount3(totalCount3);
		yzhxResult.setTotalreward1(totalReward1);
		yzhxResult.setTotalreward2(totalReward2);
		yzhxResult.setTotalreward3(totalReward3);
		yzhxResult.setFreeCount1(freeCount1);
		yzhxResult.setFreeCount2(freeCount2);
		yzhxResult.setFreeCount3(freeCount3);
		yzhxResult.setTotoalFreeCount1(totoalFreeCount1);
		yzhxResult.setTotoalFreeCount2(totoalFreeCount2);
		yzhxResult.setTotoalFreeCount3(totoalFreeCount3);
		yzhxResult.setFreeGameReward1(freeGameReward1);
		yzhxResult.setFreeGameReward2(freeGameReward2);
		yzhxResult.setFreeGameReward3(freeGameReward3);
		yzhxResult.setTotalIncomelv((totalReward1 
				+ totalReward2 
				+ totalReward3
				+ freeGameReward1 
				+ freeGameReward2 
				+ freeGameReward3) /(lineBet*line*(gameCount1 + gameCount2 + gameCount3)));
		yzhxResult.setTotalRewardlv(NumberTool.divide((totalCount1 + totalCount2 + totalCount3), betCount).doubleValue());
		System.out.println("统计结果: " + yzhxResult);
		System.out.println("正常游戏统计结果: " + map1);
		System.out.println("正常游戏统计结果: " + map2);
		System.out.println("正常游戏统计结果: " + map3);
		return yzhxResult;
	}
	
	/**
	 * 测试老虎机登陆
	 * @return 
	 */
	public  String testLogin(){
		int messageid = 2800;
		
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
		int messageid = 2801;
		
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
	public  TgpdGameBetResult testStartGame(int line,double lineBet,int type){
		int messageid = 2802;
		
		MessageClient mc = new MessageClient();
		mc.setMessageid(messageid);
		mc.setUuid(uuid);
		mc.setSid("");
		mc.setType(0);
		Map<String, String> map = new HashMap<>();
		map.put("messageid", String.valueOf(messageid));
		map.put("roleid", roleid);
		map.put("num", String.valueOf(line));
		map.put("type", String.valueOf(type));
		map.put("jetton", String.valueOf(lineBet));
		map.put("uuid", uuid);
		mc.setParams(map);
		TgpdGameBetResult result = (TgpdGameBetResult)mainProcess.mainProcessMsg(mc);
		return result;
		}
	
	/**
	 * 免费游戏
	 * @return 
	 */
	public  TgpdGameBetResult freeGame(int type){
		int messageid = 2805;
		
		MessageClient mc = new MessageClient();
		mc.setMessageid(messageid);
		mc.setUuid(uuid);
		mc.setSid("");
		mc.setType(0);
		Map<String, String> map = new HashMap<>();
		map.put("messageid", String.valueOf(messageid));
		map.put("roleid", roleid);
		map.put("num", String.valueOf(line));
		map.put("type", String.valueOf(type));
		map.put("uuid", uuid);
		mc.setParams(map);
		TgpdGameBetResult result = (TgpdGameBetResult) mainProcess.mainProcessMsg(mc);
		return result;
		}
	
	/**
	 * 掉落游戏
	 * @return 
	 */
	public  TgpdGameBetResult dorpGame(int type){
		int messageid = 2803;
		
		MessageClient mc = new MessageClient();
		mc.setMessageid(messageid);
		mc.setUuid(uuid);
		mc.setSid("");
		mc.setType(0);
		Map<String, String> map = new HashMap<>();
		map.put("messageid", String.valueOf(messageid));
		map.put("roleid", roleid);
		map.put("type", String.valueOf(type));
		map.put("uuid", uuid);
		mc.setParams(map);
		TgpdGameBetResult result = (TgpdGameBetResult) mainProcess.mainProcessMsg(mc);
		return result;
		}
	
	/**
	 * 正常游戏数据记录
	 * @param yzhxResult 
	 */
	public  void dataRecord(TgpdGameBetResult result, int type){
		//System.out.println("当前的result ===========:" + result);
		//System.out.println("当前的type ===========:" + type);
		List<WinLineInfo> winLine = result.getWinLine();
		double rewardcoin = result.getRewardcoin();
		if (type == 1) {
			if (rewardcoin > 0) {
				totalCount1 ++;
			}
			totalReward1 += rewardcoin;
		} else if (type == 2) {
			if (rewardcoin > 0) {
				totalCount2 ++;
			}
			totalReward2 += rewardcoin;
		} else {
			if (rewardcoin > 0) {
				totalCount3 ++;
			}
			totalReward3 += rewardcoin;
		}
		
		if (winLine != null && winLine.size() > 0) {
			for (WinLineInfo wl : winLine) {
				//System.out.println("单条获胜的线路" + wl);
				String icon = wl.getIcon().getName();
				int num = wl.getNum();
				double reward = wl.getReward();
				//System.out.println("icon : " + icon);
				//System.out.println("num" + num);
				//System.out.println("reward" + reward);
				if(type == 1){
					if (map1.containsKey(icon)) {
						Map<Integer, Integer> map2 = map1.get(icon);
						if (map2.containsKey(num)) {
							map2.put(num, map2.get(num) + 1);
						} else {
							map2.put(num, 1);
						}
					} else{
						Map<Integer, Integer> count = new HashMap<>();
						count.put(num, 1);
						map1.put(icon, count);
					}
				}
				if(type == 2){
					if (map2.containsKey(icon)) {
						Map<Integer, Integer> map3 = map2.get(icon);
						if (map3.containsKey(num)) {
							map3.put(num, map3.get(num) + 1);
						} else {
							map3.put(num, 1);
						}
					} else{
						Map<Integer, Integer> count = new HashMap<>();
						count.put(num, 1);
						map2.put(icon, count);
					}
				}
				if(type == 3){
					if (map3.containsKey(icon)) {
						Map<Integer, Integer> map2 = map3.get(icon);
						if (map2.containsKey(num)) {
							map2.put(num, map2.get(num) + 1);
						} else {
							map2.put(num, 1);
						}
					} else{
						Map<Integer, Integer> count = new HashMap<>();
						count.put(num, 1);
						map3.put(icon, count);
					}
				}
			}
		}
	}
	
	/**
	 * 免费游戏数据记录
	 * @param yzhxResult 
	 */
	public  void freedataRecord(TgpdGameBetResult result, int type){
		List<WinLineInfo> winLine = result.getWinLine();
		double rewardcoin = result.getRewardcoin();
		if (type == 1) {
			if (rewardcoin > 0) {
				totoalFreeCount1 ++;
			}
			freeGameReward1 += rewardcoin;
		} else if (type == 2) {
			if (rewardcoin > 0) {
				totoalFreeCount2 ++;
			}
			freeGameReward2 += rewardcoin;
		} else {
			if (rewardcoin > 0) {
				totoalFreeCount3 ++;
			}
			freeGameReward3 += rewardcoin;
		}
		
		if (winLine != null && winLine.size() > 0) {
			for (WinLineInfo wl : winLine) {
				//System.out.println("单条获胜的线路" + wl);
				String icon = wl.getIcon().getName();
				int num = wl.getNum();
				double reward = wl.getReward();
				//System.out.println("icon : " + icon);
				//System.out.println("num" + num);
				//System.out.println("reward" + reward);
				if(type == 1){
					if (freeMap1.containsKey(icon)) {
						Map<Integer, Integer> map2 = freeMap1.get(icon);
						if (map2.containsKey(num)) {
							map2.put(num, map2.get(num) + 1);
						} else {
							map2.put(num, 1);
						}
					} else{
						Map<Integer, Integer> count = new HashMap<>();
						count.put(num, 1);
						freeMap1.put(icon, count);
					}
				}
				if(type == 2){
					if (freeMap2.containsKey(icon)) {
						Map<Integer, Integer> map3 = freeMap2.get(icon);
						if (map3.containsKey(num)) {
							map3.put(num, map3.get(num) + 1);
						} else {
							map3.put(num, 1);
						}
					} else{
						Map<Integer, Integer> count = new HashMap<>();
						count.put(num, 1);
						freeMap2.put(icon, count);
					}
				}
				if(type == 3){
					if (freeMap3.containsKey(icon)) {
						Map<Integer, Integer> map2 = freeMap3.get(icon);
						if (map2.containsKey(num)) {
							map2.put(num, map2.get(num) + 1);
						} else {
							map2.put(num, 1);
						}
					} else{
						Map<Integer, Integer> count = new HashMap<>();
						count.put(num, 1);
						freeMap3.put(icon, count);
					}
				}
			}
		}
	}
	
}
