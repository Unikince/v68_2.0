package com.zyhy.lhj_server.game.lqjx.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zyhy.common_lhj.process.MainProcess;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.common_server.result.MessageClient;
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.common_server.util.RandomUtil;
@Service
public class lqjxTest {
	private  String roleid = "802";
	private  String uuid = "1";
	@Autowired
	private MainProcess mainProcess;
	// 下注线数
	private   int line = 1;
	// 下注线注
	private   double lineBet = 1;
	// 下注次数
	private   int betCount = 100000;
	// 测试轮数
	private   int round = 1;
	// 当前游戏运行次数
	private  int currentcount;
	// 正常游戏次数
	private   int gameCount = 0;
	// 正常游戏中奖次数
	private   int totalCount = 0;
	private   int totalCountwild = 0;
	// 正常游戏奖励
	private  double totalReward = 0;
	private  double totalRewardwild = 0;
	private  double ohertotalRewardwild = 0;
	// 正常游戏掉落次数
	private   int dorpgameCount = 0;
	// 正常游戏diaol中奖次数
	private   int dorptotalCount = 0;
	// 正常游戏掉落奖励
	private  double dorptotalReward = 0;
	// 免费游戏触发次数
	private   int freeCount = 0;
	// 免费游戏总次数
	private   int totoalFreeCount = 0;
	// 免费游戏奖励次数
	private   double freeRewardCount = 0;
	// 免费游戏奖励
	private   double freeGameReward = 0;
	// 免费游戏掉落次数
	private   int dorpfreeCount = 0;
	// 免费游戏掉落中奖次数
	private   int dorptotoalFreeCount = 0;
	// 免费游戏掉落总奖励
	private   double dorpfreeGameReward = 0;
	
	// 正常游戏奖励信息(K：图标名称 V：K：中奖个数 V：中奖次数)
	private   Map<String, Map<Integer, Integer>> map = new HashMap<>();
	private   Map<String, Map<Integer, Integer>> map1 = new HashMap<>();
	private   Map<String, Map<Integer, Integer>> tMap21 = new HashMap<>();
	// 免费游戏奖励信息
	private   Map<String, Map<Integer, Integer>> freeMap = new HashMap<>();
	private   Map<String, Map<Integer, Integer>> freeMap1 = new HashMap<>();
	// 免费游戏掉落加倍统计
	private  double dorpfreereward =0;
	private   Map<String, Map<String, Map<Integer, Integer>>> dorpfreeMap2 = new HashMap<>();
	// 全是wild次数统计
	private   Map<Integer, Integer> tMap11 = new HashMap<>();
	private   Map<Integer, Double> tMap12 = new HashMap<>();
	//private static  Map<String, Double> tMap22 = new HashMap<>();
	//private static  Map<String, Map<Integer, Integer>> tMap31 = new HashMap<>();
	//private static  Map<String, Double> tMap32 = new HashMap<>();
	//private static  Map<String, Map<Integer, Integer>> tMap41 = new HashMap<>();
	//private static  Map<String, Double> tMap42 = new HashMap<>();
	
	public  void initTest() throws IOException, InterruptedException {
		testOpenGame();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd _HH_mm");
		Date date = new Date(System.currentTimeMillis());
		String format = sdf.format(date);
		String path = "C:\\Users\\Administrator\\Desktop\\shuzhi\\lqjx\\1";
		String name = "lqjx-" + format +".txt";
		File file = new File(path, name);
		System.out.println(file.createNewFile());
		for (int i = 1; i <= round; i++) {
			String testLogin1 = testLogin();
			lqjxResult result = testAuto();
			String testLogin2 = testLogin();
			String filename = i + ".==========>\r\n";
			String content1 = "统计结果: " + result.toString() + "\r\n";
			String content2 = "正常游戏次数统计结果: " + map.toString()+ "\r\n";
			String content3 = "正常掉落游戏统计结果: " + map1.toString()+ "\r\n";
			String content4 = "免费游戏次数统计结果: " + freeMap.toString()+ "\r\n";
			String content5 = "免费掉落游戏统计结果: " + freeMap1.toString()+ "\r\n";
			String content6 = "免费掉落加倍奖励次数统计结果: " + dorpfreeMap2.toString()+ "\r\n";
			String content7 = "免费掉落加倍奖励金额统计结果: " + dorpfreereward + "\r\n";
			String content8 = "全是wild次数统计结果: " + tMap11.toString()+ "\r\n";
			String content9 = "全是wild金额统计结果: " + tMap12.toString()+ "\r\n";
			String content10 = "包含wild次数统计结果(正常游戏): " + tMap21.toString()+ "\r\n";
			//String content11 = "单独scatter奖励统计结果(正常游戏掉落): " + tMap22.toString()+ "\r\n";
			//String content12 = "单独scatter次数统计结果(免费游戏): " + tMap31.toString()+ "\r\n";
			//String content13 = "单独scatter奖励统计结果(免费游戏): " + tMap32.toString()+ "\r\n";
			//String content14 = "单独scatter次数统计结果(免费游戏掉落): " + tMap41.toString()+ "\r\n";
			//String content15 = "单独scatter奖励统计结果(免费游戏掉落): " + tMap42.toString()+ "\r\n";
			String content16 = "游戏开始金币: " + testLogin1.toString()+ "\r\n";
			String content17 = "游戏结束金币: " + testLogin2.toString()+ "\r\n";
			String output = filename + content1 + content2 + content3 + content4 + content5 
					+ content6 
					+ content7 
					+ content8 
					+ content9
					+ content10
					//+ content11+ content12+ content13+ content14+ content15
					+ content16+ content17;
			FileWriter fw = new FileWriter(file,true);    
			fw.write(output,0,output.length());   
			String clean = clean();
			fw.write(clean,0,clean.length());   
			fw.flush();
		}
		testLogin();
	}
	public  String clean(){
		// 正常游戏次数
		gameCount = 0;
		// 正常游戏中奖次数
		totalCount = 0;
		totalCountwild = 0;
		// 正常游戏奖励
		totalReward = 0;
		totalRewardwild = 0;
		// 正常游戏掉落次数
		dorpgameCount = 0;
		// 正常游戏diaol中奖次数
		dorptotalCount = 0;
		// 正常游戏掉落奖励
		dorptotalReward = 0;
		// 免费游戏触发次数
		freeCount = 0;
		// 免费游戏总次数
		totoalFreeCount = 0;
		// 免费游戏奖励次数
		freeRewardCount = 0;
		// 免费游戏奖励
		freeGameReward = 0;
		// 免费游戏掉落触发次数
		dorpfreeCount = 0;
		// 免费游戏掉落总次数
		dorptotoalFreeCount = 0;
		// 免费游戏掉落奖励
		dorpfreeGameReward = 0;
		// 正常游戏奖励信息(K：图标名称 V：K：中奖个数 V：中奖次数)
		map.clear();
		map1.clear();
		// 免费游戏奖励信息
		freeMap.clear();
		freeMap1.clear();
		dorpfreereward = 0;
		dorpfreeMap2.clear();
		tMap11.clear();
		tMap12.clear();
		tMap21.clear();
		//tMap22.clear();
		//tMap31.clear();
		//tMap32.clear();
		//tMap41.clear();
		//tMap42.clear();
		String result =  "[清除数据成功!当前数据 : ]"
				+ "gameCount = " + gameCount
				+ ", totalCount = " + totalCount
				+ ", totalCountwild = " + totalCountwild
				+ ", totalReward = " + totalReward
				+ ", totalRewardwild = " + totalRewardwild
				+ "dorpgameCount = " + dorpgameCount
				+ ", dorptotalCount = " + dorptotalCount
				+ ", dorptotalReward = " + dorptotalReward
				+ ", freeCount = " + freeCount
				+ ", freeRewardCount = " + freeRewardCount
				+ ", totoalFreeCount = " + totoalFreeCount
				+ ", freeGameReward = " + freeGameReward
				+ ", dorpfreeCount = " + dorpfreeCount
				+ ", dorptotoalFreeCount = " + dorptotoalFreeCount
				+ ", dorpfreeGameReward = " + dorpfreeGameReward
				+ ", map = " + map.size()
				+ ", map1 = " + map1.size()
				+ ", freeMap = " + freeMap.size()
				+ ", freeMap1 = " + freeMap1.size()
				+ ", dorpfreeMap1 = " + dorpfreereward
				+ ", dorpfreeMap2 = " + dorpfreeMap2.size()
				+ ", tMap11 = " + tMap11.size()
				+ ", tMap12 = " + tMap12.size()
				+ ", tMap21 = " + tMap21.size()
				//+ ", tMap22 = " + tMap22.size()
				//+ ", tMap31 = " + tMap31.size()
				//+ ", tMap32 = " + tMap32.size()
				//+ ", tMap41 = " + tMap41.size()
				//+ ", tMap42 = " + tMap42.size()
				+"\r\n"
				;
		return result;
	}
	/**
	 * 自动测试
	 * @return 
	 * @throws InterruptedException 
	 */
	public  lqjxResult testAuto() throws InterruptedException{
		// 统计结果
		lqjxResult yzhxResult = new lqjxResult();
		// 开始测试
		for (int i = 0; i < betCount; i++) {
			//Thread.sleep(20L);
			JSONObject result = testStartGame(line,lineBet);
			//System.out.println("正常游戏result: " + result);
			boolean replenish = result.getBooleanValue("replenish");
			boolean scatter = result.getBooleanValue("scatter");
			
			dataRecord(result);
			
			while (replenish) {
				JSONObject dorpGameResult = dorpGame();
				//System.out.println("掉落游戏result: " + dorpGameResult);
				dorpdataRecord1(dorpGameResult);
				replenish = dorpGameResult.getBooleanValue("replenish");
			}
			
			if (scatter) {
				freeCount ++ ;
				int num = 1;
				while (num != 0) {
					JSONObject freeRsult = testStartGame(line,lineBet);
					//System.out.println("免费游戏result: " + freeRsult);
					freedataRecord(freeRsult);
					int scatterNum = freeRsult.getIntValue("scatterNum");
					num = scatterNum;
					boolean replenish2 = freeRsult.getBooleanValue("replenish");
					while (replenish2) {
						JSONObject dorpGameResult2 = dorpGame();
						int mul = dorpGameResult2.getIntValue("mul");
						String rewardmul = String.valueOf(dorpGameResult2.getIntValue("rewardmul"));
						if (mul > 0) {
							dorpdataRecord3(dorpGameResult2,rewardmul);
						} else {
							dorpdataRecord2(dorpGameResult2);
						}
						
						replenish2 = dorpGameResult2.getBooleanValue("replenish");
					}
				}
			}
			currentcount ++;
			System.out.println("目前自动运行次数: " + currentcount);
		}
		yzhxResult.setBetCount(betCount);
		yzhxResult.setTotalBet(lineBet*50*line*gameCount);
		yzhxResult.setGameCount(gameCount);
		yzhxResult.setTotalCount(totalCount);
		yzhxResult.setTotalCountwild(totalCountwild);
		yzhxResult.setTotalreward(totalReward);
		yzhxResult.setTotalRewardwild(totalRewardwild);
		yzhxResult.setDorpgameCount(dorpgameCount);
		yzhxResult.setDorptotalCount(dorptotalCount);
		yzhxResult.setDorptotalReward(dorptotalReward);
		yzhxResult.setFreeCount(freeCount);
		yzhxResult.setOhertotalRewardwild(tMap12.get(1) + tMap12.get(2));
		yzhxResult.setFreeRewardCount(freeRewardCount);
		yzhxResult.setTotoalFreeCount(totoalFreeCount);
		yzhxResult.setFreeGameReward(freeGameReward);
		yzhxResult.setDorpfreeCount(dorpfreeCount);
		yzhxResult.setDorptotoalFreeCount(dorptotoalFreeCount);
		yzhxResult.setDorpfreeGameReward(dorpfreeGameReward);
		yzhxResult.setTotalIncomelv((yzhxResult.getOhertotalRewardwild() +  totalReward + totalRewardwild + dorptotalReward + freeGameReward + dorpfreeGameReward + dorpfreereward)/(lineBet*50*line*gameCount));
		yzhxResult.setTotalRewardlv(NumberTool.divide((totalCount + totalCountwild + dorptotalCount + freeRewardCount + dorptotoalFreeCount), gameCount).doubleValue());
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
		int messageid = 2400;
		
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
		int messageid = 2401;
		
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
	public  JSONObject testStartGame(int line,double lineBet){
		int messageid = 2402;
		
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
		HttpMessageResult result = mainProcess.mainProcessMsg(mc);
		return JSONObject.parseObject(JSONObject.toJSONString(result));
	}
	
	/**
	 * 掉落游戏
	 * @return 
	 */
	public  JSONObject dorpGame(){
		int messageid = 2403;
		
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
		return JSONObject.parseObject(JSONObject.toJSONString(result));
		
	}
	
	/**
	 * 正常游戏数据记录
	 * @param yzhxResult 
	 */
	public  void dataRecord(JSONObject data){
		double rewardcoin = data.getDoubleValue("rewardcoin");
		JSONArray jsonArray = data.getJSONArray("god");
		if (jsonArray.size() > 0) {
			wilddataRecord(data);
			return;
		}
		gameCount ++;
		totalReward += rewardcoin;
		if (rewardcoin > 0) {
			totalCount ++;
			Double scattericon = data.getDouble("scattericon");
			int scattericonnum = data.getIntValue("scattericonnum");
			if (scattericon > 0) {
				scatterCount1(scattericon,scattericonnum);
			}
		}
		JSONArray rewardInfo = data.getJSONArray("winInfo");
		if (rewardInfo.size() > 0) {
			for (Object rewardInfo2 : rewardInfo) {
				JSONObject parseObject = JSONObject.parseObject(rewardInfo2.toString());
				//System.out.println("rewardInfo2===============>" + parseObject);
				int winNum = parseObject.getIntValue("winNum");
				int num = parseObject.getIntValue("num");
				String icon = parseObject.getString("icon");
				if (map.containsKey(icon)) {
					Map<Integer, Integer> map2 = map.get(icon);
					if (map2.containsKey(winNum)) {
						map2.put(winNum, map2.get(winNum) + num);
					} else {
						map2.put(winNum, num);
					}
				} else{
					Map<Integer, Integer> count = new HashMap<>();
					count.put(winNum, num);
					map.put(icon, count);
				}
			}
		}
	}
	
	/**
	 * 正常游戏带wild数据记录
	 * @param yzhxResult 
	 */
	public  void wilddataRecord(JSONObject data){
		JSONArray jsonArray = data.getJSONArray("god");
		double rewardcoin = data.getDoubleValue("rewardcoin");
		gameCount ++;
		// 统计全部wild,金额只统计了1,2
		if (jsonArray.size() > 0) {
			if (tMap11.containsKey(jsonArray.size())) {
				tMap11.put(jsonArray.size(), tMap11.get(jsonArray.size()) + 1);
			} else {
				tMap11.put(jsonArray.size(), 1);
			}
			
			if (tMap12.containsKey(jsonArray.size())) {
				tMap12.put(jsonArray.size(), tMap12.get(jsonArray.size()) + rewardcoin);
			} else {
				tMap12.put(jsonArray.size(), rewardcoin);
			}
		}
		// 单独统计3列wild与金额
		if (jsonArray.size() > 2) {
			totalRewardwild += rewardcoin;
			if (rewardcoin > 0) {
				totalCountwild ++;
				Double scattericon = data.getDouble("scattericon");
				int scattericonnum = data.getIntValue("scattericonnum");
				if (scattericon > 0) {
					scatterCountwild(scattericon,scattericonnum);
				}
			}
			JSONArray rewardInfo = data.getJSONArray("winInfo");
			if (rewardInfo.size() > 0) {
				for (Object rewardInfo2 : rewardInfo) {
					JSONObject parseObject = JSONObject.parseObject(rewardInfo2.toString());
					//System.out.println("rewardInfo2===============>" + parseObject);
					int winNum = parseObject.getIntValue("winNum");
					int num = parseObject.getIntValue("num");
					String icon = parseObject.getString("icon");
					if (tMap21.containsKey(icon)) {
						Map<Integer, Integer> map2 = tMap21.get(icon);
						if (map2.containsKey(winNum)) {
							map2.put(winNum, map2.get(winNum) + num);
						} else {
							map2.put(winNum, num);
						}
					} else{
						Map<Integer, Integer> count = new HashMap<>();
						count.put(winNum, num);
						tMap21.put(icon, count);
					}
				}
			}
		}
	}
	
	/**
	 * 正常游戏掉落数据记录
	 * @param yzhxResult 
	 */
	public  void dorpdataRecord1(JSONObject data){
		double rewardcoin = data.getDoubleValue("rewardcoin");
		dorpgameCount ++;
		dorptotalReward += rewardcoin;
		//System.out.println("本次奖励===============>" + rewardcoin);
		if (rewardcoin > 0) {
			dorptotalCount ++;
			Double scattericon = data.getDouble("scattericon");
			int scattericonnum = data.getIntValue("scattericonnum");
			if (scattericon > 0) {
				scatterCount2(scattericon,scattericonnum);
			}
		}
		JSONArray rewardInfo = data.getJSONArray("winInfo");
		if (rewardInfo.size() > 0) {
			for (Object rewardInfo2 : rewardInfo) {
				JSONObject parseObject = JSONObject.parseObject(rewardInfo2.toString());
				//System.out.println("rewardInfo2===============>" + parseObject);
				int winNum = parseObject.getIntValue("winNum");
				int num = parseObject.getIntValue("num");
				String icon = parseObject.getString("icon");
				if (map1.containsKey(icon)) {
					Map<Integer, Integer> map2 = map1.get(icon);
					if (map2.containsKey(winNum)) {
						map2.put(winNum, map2.get(winNum) + num);
					} else {
						map2.put(winNum, num);
					}
				} else{
					Map<Integer, Integer> count = new HashMap<>();
					count.put(winNum, num);
					map1.put(icon, count);
				}
			}
		}
	}
	
	/**
	 * 免费游戏数据记录
	 * @param yzhxResult 
	 */
	public  void freedataRecord(JSONObject data){
		double rewardcoin = data.getDoubleValue("rewardcoin");
		totoalFreeCount ++;
		freeGameReward += rewardcoin;
		//System.out.println("本次奖励===============>" + rewardcoin);
		if (rewardcoin > 0) {
			freeRewardCount ++;
			Double scattericon = data.getDouble("scattericon");
			int scattericonnum = data.getIntValue("scattericonnum");
			if (scattericon > 0) {
				scatterCount3(scattericon,scattericonnum);
			}
		}
		JSONArray rewardInfo = data.getJSONArray("winInfo");
		if (rewardInfo.size() > 0) {
			for (Object rewardInfo2 : rewardInfo) {
				JSONObject parseObject = JSONObject.parseObject(rewardInfo2.toString());
				//System.out.println("rewardInfo2===============>" + parseObject);
				int winNum = parseObject.getIntValue("winNum");
				int num = parseObject.getIntValue("num");
				String icon = parseObject.getString("icon");
				if (freeMap.containsKey(icon)) {
					Map<Integer, Integer> map2 = freeMap.get(icon);
					if (map2.containsKey(winNum)) {
						map2.put(winNum, map2.get(winNum) + num);
					} else {
						map2.put(winNum, num);
					}
				} else{
					Map<Integer, Integer> count = new HashMap<>();
					count.put(winNum, num);
					freeMap.put(icon, count);
				}
			}
		}
	}
	
	/**
	 * 免费游戏掉落数据记录
	 * @param yzhxResult 
	 */
	public  void dorpdataRecord2(JSONObject data){
		double rewardcoin = data.getDoubleValue("rewardcoin");
		dorpfreeCount ++;
		dorpfreeGameReward += rewardcoin;
		//System.out.println("本次奖励===============>" + rewardcoin);
		
		if (rewardcoin > 0) {
			dorptotoalFreeCount ++;
			Double scattericon = data.getDouble("scattericon");
			int scattericonnum = data.getIntValue("scattericonnum");
			if (scattericon > 0) {
				scatterCount4(scattericon,scattericonnum);
			}
		}
		JSONArray rewardInfo = data.getJSONArray("winInfo");
		if (rewardInfo.size() > 0) {
			for (Object rewardInfo2 : rewardInfo) {
				JSONObject parseObject = JSONObject.parseObject(rewardInfo2.toString());
				//System.out.println("rewardInfo2===============>" + parseObject);
				int winNum = parseObject.getIntValue("winNum");
				int num = parseObject.getIntValue("num");
				String icon = parseObject.getString("icon");
				if (freeMap1.containsKey(icon)) {
					Map<Integer, Integer> map2 = freeMap1.get(icon);
					if (map2.containsKey(winNum)) {
						map2.put(winNum, map2.get(winNum) + num);
					} else {
						map2.put(winNum, num);
					}
				} else{
					Map<Integer, Integer> count = new HashMap<>();
					count.put(winNum, num);
					freeMap1.put(icon, count);
				}
			}
		}
	}
	
	
	/**
	 * 免费游戏掉落加倍奖励数据记录
	 * @param rewardmul 
	 * @param yzhxResult 
	 */
	public  void dorpdataRecord3(JSONObject data, String rewardmul){
		double rewardcoin = data.getDoubleValue("rewardcoin");
		dorpfreeCount ++;
		dorpfreereward += rewardcoin;
		Map<String, Map<Integer, Integer>> dorpfreeMap1 = new HashMap<>();
		//System.out.println("本次奖励===============>" + rewardcoin);
		if (rewardcoin > 0) {
			dorptotoalFreeCount ++;
			Double scattericon = data.getDouble("scattericon");
			int scattericonnum = data.getIntValue("scattericonnum");
			if (scattericon > 0) {
				scatterCount5(scattericon,scattericonnum,dorpfreeMap1);
			}
		}
		JSONArray rewardInfo = data.getJSONArray("winInfo");
		if (rewardInfo.size() > 0) {
			for (Object rewardInfo2 : rewardInfo) {
				JSONObject parseObject = JSONObject.parseObject(rewardInfo2.toString());
				//System.out.println("rewardInfo2===============>" + parseObject);
				int winNum = parseObject.getIntValue("winNum");
				int num = parseObject.getIntValue("num");
				String icon = parseObject.getString("icon");
				if (dorpfreeMap1.containsKey(icon)) {
					Map<Integer, Integer> map2 = dorpfreeMap1.get(icon);
					if (map2.containsKey(winNum)) {
						map2.put(winNum, map2.get(winNum) + num);
					} else {
						map2.put(winNum, num);
					}
				} else{
					Map<Integer, Integer> count = new HashMap<>();
					count.put(winNum, num);
					dorpfreeMap1.put(icon, count);
				}
			}
		}
		dorpfreeMap2.put(rewardmul + "|" + RandomUtil.getRandom(10000, 99999), dorpfreeMap1);
	}
	
	public  void scatterCount1(Double reward, int num){
		String icon = "scatter";
		
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
		
		/*if (tMap11.containsKey(icon)) {
			Map<Integer, Integer> map2 = tMap11.get(icon);
			if (map2.containsKey(num)) {
				map2.put(num, map2.get(num) + 1);
			} else {
				map2.put(num, 1);
			}
		} else{
			Map<Integer, Integer> count = new HashMap<>();
			count.put(num, 1);
			tMap11.put(icon, count);
		}
		
		if (tMap12.containsKey(icon)) {
			tMap12.put(icon, tMap12.get(icon) + reward );
		} else {
			tMap12.put(icon, reward);
		}*/
		
	}
	
	
	public  void scatterCountwild(Double reward, int num){
		String icon = "scatter";
		
		if (tMap21.containsKey(icon)) {
			Map<Integer, Integer> map2 = tMap21.get(icon);
			if (map2.containsKey(num)) {
				map2.put(num, map2.get(num) + 1);
			} else {
				map2.put(num, 1);
			}
		} else{
			Map<Integer, Integer> count = new HashMap<>();
			count.put(num, 1);
			tMap21.put(icon, count);
		}
		
		/*if (tMap11.containsKey(icon)) {
			Map<Integer, Integer> map2 = tMap11.get(icon);
			if (map2.containsKey(num)) {
				map2.put(num, map2.get(num) + 1);
			} else {
				map2.put(num, 1);
			}
		} else{
			Map<Integer, Integer> count = new HashMap<>();
			count.put(num, 1);
			tMap11.put(icon, count);
		}
		
		if (tMap12.containsKey(icon)) {
			tMap12.put(icon, tMap12.get(icon) + reward );
		} else {
			tMap12.put(icon, reward);
		}*/
		
	}
	
	public  void scatterCount2(Double reward, int num){
		String icon = "scatter";
		
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
		
		
		
		/*if (tMap21.containsKey(icon)) {
			Map<Integer, Integer> map2 = tMap21.get(icon);
			if (map2.containsKey(num)) {
				map2.put(num, map2.get(num) + 1);
			} else {
				map2.put(num, 1);
			}
		} else{
			Map<Integer, Integer> count = new HashMap<>();
			count.put(num, 1);
			tMap21.put(icon, count);
		}
		
		if (tMap22.containsKey(icon)) {
			tMap22.put(icon, tMap22.get(icon) + reward );
		} else {
			tMap22.put(icon, reward);
		}*/
	}
	
	public  void scatterCount3(Double reward, int num){
		String icon = "scatter";
		
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
		
		/*if (tMap31.containsKey(icon)) {
			Map<Integer, Integer> map2 = tMap31.get(icon);
			if (map2.containsKey(num)) {
				map2.put(num, map2.get(num) + 1);
			} else {
				map2.put(num, 1);
			}
		} else{
			Map<Integer, Integer> count = new HashMap<>();
			count.put(num, 1);
			tMap31.put(icon, count);
		}
		
		if (tMap32.containsKey(icon)) {
			tMap32.put(icon, tMap32.get(icon) + reward );
		} else {
			tMap32.put(icon, reward);
		}*/
	}
	
	public  void scatterCount4(Double reward, int num){
		String icon = "scatter";
		
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
		
		/*if (tMap41.containsKey(icon)) {
			Map<Integer, Integer> map2 = tMap41.get(icon);
			if (map2.containsKey(num)) {
				map2.put(num, map2.get(num) + 1);
			} else {
				map2.put(num, 1);
			}
		} else{
			Map<Integer, Integer> count = new HashMap<>();
			count.put(num, 1);
			tMap41.put(icon, count);
		}
		
		if (tMap42.containsKey(icon)) {
			tMap42.put(icon, tMap42.get(icon) + reward );
		} else {
			tMap42.put(icon, reward);
		}*/
	}
	
	public  void scatterCount5(Double reward, int num, Map<String, Map<Integer, Integer>> dorpfreeMap1){
		String icon = "scatter";
		
		if (dorpfreeMap1.containsKey(icon)) {
			Map<Integer, Integer> map2 = dorpfreeMap1.get(icon);
			if (map2.containsKey(num)) {
				map2.put(num, map2.get(num) + 1);
			} else {
				map2.put(num, 1);
			}
		} else{
			Map<Integer, Integer> count = new HashMap<>();
			count.put(num, 1);
			dorpfreeMap1.put(icon, count);
		}
		
		/*if (tMap41.containsKey(icon)) {
			Map<Integer, Integer> map2 = tMap41.get(icon);
			if (map2.containsKey(num)) {
				map2.put(num, map2.get(num) + 1);
			} else {
				map2.put(num, 1);
			}
		} else{
			Map<Integer, Integer> count = new HashMap<>();
			count.put(num, 1);
			tMap41.put(icon, count);
		}
		
		if (tMap42.containsKey(icon)) {
			tMap42.put(icon, tMap42.get(icon) + reward );
		} else {
			tMap42.put(icon, reward);
		}*/
	}
	
}
