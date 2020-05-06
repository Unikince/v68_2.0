package com.zyhy.lhj_server.game.hjws.test;

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
public class HjwsTest {
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
	private   int round = 2;
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
	// 正常游戏奖励信息(K：图标名称 V：K：中奖个数 V：中奖次数)
	private   Map<String, Map<Integer, Integer>> map = new HashMap<>();
	// 免费游戏奖励信息
	private   Map<Integer, Map<Integer, Double>> freeMap = new HashMap<>();
	// 特殊模式次数和奖励
	private   Map<Integer, Double> specialMap = new HashMap<>();
	
	public  void initTest() throws IOException, InterruptedException {
		testOpenGame();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd _HH_mm");
		Date date = new Date(System.currentTimeMillis());
		String format = sdf.format(date);
		String path = "C:\\Users\\Administrator\\Desktop\\shuzhi\\hjws\\1";
		String name = "hjws-" + format +".txt";
		File file = new File(path, name);
		System.out.println(file.createNewFile());
		for (int i = 1; i <= round; i++) {
			String testLogin1 = testLogin();
			HjwsResult result = testAuto();
			String testLogin2 = testLogin();
			String filename = i + ".==========>\r\n";
			String content1 = "统计结果: " + result.toString() + "\r\n";
			String content2 = "正常游戏统计结果: " + map.toString()+ "\r\n";
			String content3 = "免费游戏统计结果: " + freeMap.toString()+ "\r\n";
			String content4 = "特殊模式统计结果: " + specialMap.toString()+ "\r\n";
			String content5 = "游戏开始金币: " + testLogin1.toString()+ "\r\n";
			String content6 = "游戏结束金币: " + testLogin2.toString()+ "\r\n";
			String output = filename + content1 + content2 + content3 + content4 + content5 + content6; 
			FileWriter fw = new FileWriter(file,true);    
			fw.write(output,0,output.length());   
			String clean = clean();
			fw.write(clean,0,clean.length());   
			fw.flush();
		}
		testLogin();
	}
	public  String clean(){
		// 正常游戏奖励
		totalReward = 0;
		// 免费游戏触发次数
		freeCount = 0;
		// 免费游戏总次数
		totoalFreeCount = 0;
		// 免费游戏奖励
		freeGameReward = 0;
		// 正常游戏中奖次数
		totalCount = 0;
		// 正常游戏次数
		gameCount = 0;
		// 正常游戏奖励信息(K：图标名称 V：K：中奖个数 V：中奖次数)
		map.clear();
		// 免费游戏奖励信息
		freeMap.clear();
		// 特殊模式次数和奖励
		specialMap.clear();
		String result =  "[清除数据成功!当前数据 : ]"
				+ "totalReward = " + totalReward
				+ ", freeCount = " + freeCount
				+ ", totoalFreeCount = " + totoalFreeCount
				+ ", freeGameReward = " + freeGameReward
				+ ", totalCount = " + totalCount
				+ ", gameCount = " + gameCount
				+ ", map = " + map.size()
				+ ", freeMap = " + freeMap.size()
				+ ", specialMap = " + specialMap.size()
				+"\r\n"
				;
		return result;
	}
	/**
	 * 自动测试
	 * @return 
	 * @throws InterruptedException 
	 */
	public  HjwsResult testAuto() throws InterruptedException{
		// 统计结果
		HjwsResult yzhxResult = new HjwsResult();
		// 开始测试
		for (int i = 0; i < betCount; i++) {
			//Thread.sleep(20L);
			JSONObject result = testStartGame(line,lineBet);
			//System.out.println("正常游戏result: " + result);
			Double usercoin = result.getDouble("usercoin");
			//System.out.println("用户当前金币: " + usercoin);
			boolean scatter = result.getBooleanValue("scatter");
			dataRecord(result);
			if (scatter) {
				freeCount ++ ;
				JSONObject freeRsult = testFreeGame1();
				//System.out.println("免费游戏模式选择result: " + freeRsult);
				JSONObject scatterinfo = freeRsult.getJSONObject("scatter");
				int model = scatterinfo.getIntValue("model");
				int num = scatterinfo.getIntValue("num");
				double totalreward = 0;
				while (num != 0) {
					JSONObject freeRsult2 = testStartGame(line,lineBet);
					//System.out.println("免费游戏result: " + freeRsult2);
					
					Boolean special = freeRsult2.getBoolean("special");
					Double specialRward = freeRsult2.getDouble("specialRward");
					if (special) {
						if (specialMap.size() > 0) {
							for (Integer count : specialMap.keySet()) {
								int newcount = count + 1;
								double specialreward = specialMap.get(count) + specialRward;
								specialMap.clear();
								specialMap.put(newcount, specialreward);
								break;
							}
						} else {
							specialMap.put(1, specialRward);
						}
					}
					double rewardcoin = freeRsult2.getDoubleValue("rewardcoin");
					totalreward += rewardcoin;
					int scatterNum = freeRsult2.getIntValue("scatterNum");
					num = scatterNum;
				}
				freeGameReward += totalreward;
				if (freeMap.containsKey(model)) {
					Map<Integer, Double> map2 = freeMap.get(model);
					for (Integer c : map2.keySet()) {
						int count = c + 1;
						double reward = map2.get(c) + totalreward;
						map2.clear();
						map2.put(count, reward);
					}
				} else {
					Map<Integer, Double> map2 = new HashMap<>();
					map2.put(1, totalreward);
					freeMap.put(model, map2);
				}
			}
			gameCount ++;
			System.out.println("目前自动运行次数: " + gameCount);
		}
		yzhxResult.setBetCount(betCount);
		yzhxResult.setTotalBet(lineBet*25*line*gameCount);
		yzhxResult.setTotalCount(totalCount);
		yzhxResult.setTotalreward(totalReward);
		yzhxResult.setFreeCount(freeCount);
		yzhxResult.setTotoalFreeCount(totoalFreeCount);
		yzhxResult.setFreeGameReward(freeGameReward);
		yzhxResult.setGameCount(gameCount);
		yzhxResult.setTotalIncomelv((totalReward + freeGameReward)/(lineBet*25*line*gameCount));
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
		int messageid = 2200;
		
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
		int messageid = 2201;
		
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
		 int messageid = 2202;
		
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
	 * 免费模式选择
	 * @return 
	 */
	public  JSONObject testFreeGame1(){
		int messageid = 2203;
		
		MessageClient mc = new MessageClient();
		mc.setMessageid(messageid);
		mc.setUuid(uuid);
		mc.setSid("");
		mc.setType(0);
		Map<String, String> map = new HashMap<>();
		map.put("messageid", String.valueOf(messageid));
		map.put("roleid", roleid);
		map.put("model", String.valueOf(RandomUtil.getRandom(1, 5)));
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
		totalReward += rewardcoin;
		//System.out.println("本次奖励===============>" + rewardcoin);
		JSONArray rewardInfo = data.getJSONArray("winInfo");
		if (rewardInfo != null) {
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
	}
}
