package com.zyhy.lhj_server.game.alsj.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zyhy.common_lhj.process.MainProcess;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.common_server.result.MessageClient;
import com.zyhy.common_server.util.NumberTool;
@Service
public class AlsjTest {
	private  String roleid = "802";
	private  String uuid = "1";
	@Autowired
	private MainProcess mainProcess;
	// 下注硬币数
	private   int line = 1;
	// 下注线注
	private   double lineBet = 0.01;
	// 线注翻倍
	private   double linelv = 30;
	// 下注次数
	private   int betCount = 100000;
	// 测试轮数
	private   int round = 1;
	// 正常游戏次数
	private   int gameCount = 0;
	// 正常游戏中奖次数
	private   int totalCount = 0;
	// 正常游戏奖励
	private  double totalReward = 0;
	// 免费游戏触发次数
	private   int freeCount = 0;
	// 免费游戏总次数
	private   int totoalFreeCount = 0;
	// 免费游戏奖励
	private   double freeGameReward = 0;
	//红利游戏触发次数
	private   int bonusCount = 0;
	// 免费游戏奖励
	private   double bonusReward = 0;
	
	// 正常游戏奖励信息(K：图标名称 V：K：中奖个数 V：中奖次数)
	private   Map<String, Map<Integer, Integer>> map = new HashMap<>();
	// 免费游戏奖励信息
	private   Map<String, Map<Integer, Integer>> freeMap = new HashMap<>();
	// 特殊奖励统计
	private   List<String> tMap = new ArrayList< >();
	//private static  Map<String, Double> tMap1 = new HashMap<>();
	
	public  void initTest() throws IOException, InterruptedException {
		testOpenGame();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd _HH_mm");
		Date date = new Date(System.currentTimeMillis());
		String format = sdf.format(date);
		String path = "C:\\Users\\Administrator\\Desktop\\shuzhi\\alsj\\1";
		String name = "alsj-" + format +".txt";
		File file = new File(path, name);
		System.out.println(file.createNewFile());
		for (int i = 1; i <= round; i++) {
			String testLogin1 = testLogin();
			AlsjResult result = testAuto();
			String testLogin2 = testLogin();
			String filename = i + ".==========>\r\n";
			String content1 = "统计结果: " + result.toString() + "\r\n";
			String content2 = "正常游戏次数统计结果: " + map.toString()+ "\r\n";
			String content3 = "免费游戏次数统计结果: " + freeMap.toString()+ "\r\n";
			//String content4 = "wild次数统计结果: " + tMap.toString()+ "\r\n";
			//String content5 = "特殊奖励金额统计结果: " +tMap1.toString()+ "\r\n";
			String content6 = "游戏开始金币: " + testLogin1.toString()+ "\r\n";
			String content7 = "游戏结束金币: " + testLogin2.toString()+ "\r\n";
			String output = filename + content1 + content2 + content3 
					//+ content4 
					//+ content5 
					+ content6 + content7;
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
		//红利游戏触发次数
		bonusCount = 0;
		// 免费游戏奖励
		bonusReward = 0;
		// 正常游戏奖励信息(K：图标名称 V：K：中奖个数 V：中奖次数)
		map.clear();
		// 免费游戏奖励信息
		freeMap.clear();
		tMap.clear();
		//tMap1.clear();
		String result =  "[清除数据成功!当前数据 : ]"
				+ "gameCount = " + gameCount
				+ ", totalCount = " + totalCount
				+ ", totalReward = " + totalReward
				+ ", freeCount = " + freeCount
				+ ", totoalFreeCount = " + totoalFreeCount
				+ ", freeGameReward = " + freeGameReward
				+ ", bonusCount = " + bonusCount
				+ ", bonusReward = " + bonusReward
				+ ", map = " + map.size()
				+ ", freeMap = " + freeMap.size()
				+ ", tMap = " + tMap.size()
				//+ ", tMap1 = " + tMap1.size()
				+"\r\n"
				;
		return result;
	}
	/**
	 * 自动测试
	 * @return 
	 * @throws InterruptedException 
	 */
	public  AlsjResult testAuto() throws InterruptedException{
		// 统计结果
		AlsjResult yzhxResult = new AlsjResult();
		// 开始测试
		for (int i = 0; i < betCount; i++) {
			gameCount ++;
			System.out.println("目前自动运行次数: " + gameCount);
			JSONObject result = testStartGame(line,lineBet);
			//System.out.println("正常游戏result: " + result);
			dataRecord(result);
			//Double usercoin = result.getDouble("usercoin");
			//System.out.println("用户当前金币: " + usercoin);
			boolean bonus = result.getBooleanValue("bonus");
			boolean scatter = result.getBooleanValue("scatter");
			if (scatter) {
				freeCount ++ ;
				int scatterNum = result.getIntValue("scatterNum");
				while (scatterNum != 0) {
					JSONObject freeResult = testStartGame(line,lineBet);
					//System.out.println("免费游戏result: " + result);
					freeDataRecord(freeResult);
					int scatterNum2 = freeResult.getIntValue("scatterNum");
					scatterNum = scatterNum2;
				} 
			}
			if (bonus) {
				bonusCount ++;
				JSONObject freeRsult = testFreeGame1();
				//System.out.println("红利游戏result: " + freeRsult);
				double rewardAllCoin = freeRsult.getDoubleValue("rewardAllCoin");
				bonusReward += rewardAllCoin;
			}
		}

		yzhxResult.setBetCount(betCount);
		yzhxResult.setTotalBet(lineBet*linelv*line*gameCount);
		yzhxResult.setTotalCount(totalCount);
		yzhxResult.setTotalreward(totalReward);
		yzhxResult.setFreeCount(freeCount);
		yzhxResult.setTotoalFreeCount(totoalFreeCount);
		yzhxResult.setFreeGameReward(freeGameReward);
		yzhxResult.setGameCount(gameCount);
		yzhxResult.setBonusCount(bonusCount);
		yzhxResult.setBonusReward(bonusReward);
		yzhxResult.setTotalIncomelv((totalReward + freeGameReward + bonusReward)/(lineBet*linelv*line*gameCount));
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
		int messageid = 1700;
		
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
		int messageid = 1701;
		
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
		int messageid = 1702;
		
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
	 * 红利游戏
	 * @return 
	 */
	public  JSONObject testFreeGame1(){
		int messageid = 1703;
		
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
		
		Double scattericon = data.getDouble("scattericon");
		int scattericonnum = data.getIntValue("scattericonnum");
		if (scattericon > 0) {
			scatterCount1(scattericon,scattericonnum);
		}
		Double bonusicon = data.getDouble("bonusicon");
		int bonusiconnum = data.getIntValue("bonusiconnum");
		if (bonusicon > 0) {
			bonusCount1(bonusicon,bonusiconnum);
		}
		Double wildicon = data.getDouble("wildicon");
		int wildiconnum = data.getIntValue("wildiconnum");
		if (wildicon > 0) {
			wildCount1(wildicon,wildiconnum);
		}
		
		totalReward += rewardcoin;
		//System.out.println("本次奖励===============>" + rewardcoin);
		JSONArray rewardInfo = data.getJSONArray("rewardInfo");
		if (rewardInfo.size() > 0) {
			for (Object rewardInfo2 : rewardInfo) {
				JSONObject parseObject = JSONObject.parseObject(rewardInfo2.toString());
				//System.out.println("rewardInfo2===============>" + parseObject);
				
				JSONArray jsonArray = parseObject.getJSONArray("winIcon");
				int wild = 0;
				for (Object object : jsonArray) {
					JSONObject parseObject2 = JSONObject.parseObject(object.toString());
					String icon2 = parseObject2.getString("icon");
					if (!icon2.equalsIgnoreCase("WILD")) {
						break;
					}
					wild++;
				}
				if (wild > 2) {
					tMap.add(jsonArray.toJSONString());
				}
				
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
	 * 免费游戏数据记录
	 * @param yzhxResult 
	 */
	public  void freeDataRecord(JSONObject data){
		double rewardcoin = data.getDoubleValue("rewardcoin");
		
		
		Double scattericon = data.getDouble("scattericon");
		int scattericonnum = data.getIntValue("scattericonnum");
		if (scattericon > 0) {
			scatterCount2(scattericon,scattericonnum);
		}
		Double bonusicon = data.getDouble("bonusicon");
		int bonusiconnum = data.getIntValue("bonusiconnum");
		if (bonusicon > 0) {
			bonusCount2(bonusicon,bonusiconnum);
		}
		Double wildicon = data.getDouble("wildicon");
		int wildiconnum = data.getIntValue("wildiconnum");
		if (wildicon > 0) {
			wildCount2(wildicon,wildiconnum);
		}
		
		freeGameReward += rewardcoin;
		//System.out.println("本次奖励===============>" + rewardcoin);
		JSONArray rewardInfo = data.getJSONArray("rewardInfo");
		if (rewardInfo.size() > 0) {
			for (Object rewardInfo2 : rewardInfo) {
				JSONObject parseObject = JSONObject.parseObject(rewardInfo2.toString());
				//System.out.println("rewardInfo2===============>" + parseObject);
				
				JSONArray jsonArray = parseObject.getJSONArray("winIcon");
				int wild = 0;
				for (Object object : jsonArray) {
					JSONObject parseObject2 = JSONObject.parseObject(object.toString());
					String icon2 = parseObject2.getString("icon");
					if (!icon2.equalsIgnoreCase("WILD")) {
						break;
					}
					wild++;
				}
				if (wild > 2) {
					tMap.add(jsonArray.toJSONString());
				}
				
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
		
		/*if (tMap1.containsKey(icon)) {
			tMap1.put(icon, tMap1.get(icon) + reward );
		} else {
			tMap1.put(icon, reward);
		}*/
		
	}
	
	public  void scatterCount2(Double reward, int num){
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
		
		/*if (tMap1.containsKey(icon)) {
			tMap1.put(icon, tMap1.get(icon) + reward );
		} else {
			tMap1.put(icon, reward);
		}*/
		
	}
	
	
	
	public  void bonusCount1(Double reward, int num){
		String icon = "bonus";
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
		
	/*	if (tMap1.containsKey(icon)) {
			tMap1.put(icon, tMap1.get(icon) + reward );
		} else {
			tMap1.put(icon, reward);
		}*/
	}
	
	public  void bonusCount2(Double reward, int num){
		String icon = "bonus";
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
		
	/*	if (tMap1.containsKey(icon)) {
			tMap1.put(icon, tMap1.get(icon) + reward );
		} else {
			tMap1.put(icon, reward);
		}*/
	}
	
	public  void wildCount1(Double reward, int num){
		String icon = "wild";
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
		
		/*if (tMap1.containsKey(icon)) {
			tMap1.put(icon, tMap1.get(icon) + reward );
		} else {
			tMap1.put(icon, reward);
		}*/
	}
	
	public  void wildCount2(Double reward, int num){
		String icon = "wild";
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
		
		/*if (tMap1.containsKey(icon)) {
			tMap1.put(icon, tMap1.get(icon) + reward );
		} else {
			tMap1.put(icon, reward);
		}*/
	}
	
}
