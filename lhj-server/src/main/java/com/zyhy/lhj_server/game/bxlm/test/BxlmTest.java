package com.zyhy.lhj_server.game.bxlm.test;

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
public class BxlmTest {
	private  String roleid = "829";
	private  String uuid = "1";
	@Autowired
	private MainProcess mainProcess;
	// 下注线数
	private  int line = 1;
	// 下注线注
	private  double lineBet = 1;
	// 下注次数
	private  int betCount = 10000;
	// 测试轮数
	private  int round = 5;
	// 正常游戏次数
	private  int gameCount = 0;
	// 免费游戏触发次数
	private  int freeCount = 0;
	// 正常游戏中奖次数
	private  int totalCount = 0;
	// 免费游戏中奖次数
	private  int freetotalCount = 0;
	// 正常游戏奖励
	private double totalReward = 0;
	// 正常游戏加倍奖励
	private double wildtotalReward = 0;
	// 免费游戏奖励
	private  double freetotalReward = 0;
	private  int wild1count = 0;
	private  int wild2count = 0;
	private  int wild3count = 0;
	private  int wild4count = 0;
	private  int wild5count = 0;
	
	// 正常游戏奖励信息(K：图标名称 V：K：中奖个数 V：中奖次数)
	private   Map<String, Map<Integer, Integer>> map = new HashMap<>();
	private   Map<String, Map<Integer, Double>> mapreward = new HashMap<>();
	private   Map<String, Map<Integer, Integer>> wildx2 = new HashMap<>();
	private   Map<String, Map<Integer, Double>> wildx2reward = new HashMap<>();
	private   Map<String, Map<Integer, Integer>> wild1 = new HashMap<>();
	private   Map<String, Map<Integer, Double>> wild1reward = new HashMap<>();
	private  double wild1total;
	private   Map<String, Map<Integer, Integer>> wild2 = new HashMap<>();
	private   Map<String, Map<Integer, Double>> wild2reward = new HashMap<>();
	private  double wild2total;
	private   Map<String, Map<Integer, Integer>> wild3 = new HashMap<>();
	private   Map<String, Map<Integer, Double>> wild3reward = new HashMap<>();
	private  double wild3total;
	private   Map<String, Map<Integer, Integer>> wild4 = new HashMap<>();
	private   Map<String, Map<Integer, Double>> wild4reward = new HashMap<>();
	private  double wild4total;
	private   Map<String, Map<Integer, Integer>> wild5 = new HashMap<>();
	private   Map<String, Map<Integer, Double>> wild5reward = new HashMap<>();
	private  double wild5total;
	// 免费游戏奖励信息
	private   Map<String, Map<Integer, Integer>> wildx2countMap = new HashMap<>();
	private   Map<String, Map<Integer, Double>>wildx2rewardMap = new HashMap<>();
	private   Map<String, Map<Integer, Integer>> free1countMap = new HashMap<>();
	private   Map<String, Map<Integer, Double>> free1rewardMap = new HashMap<>();
	private   Map<String, Map<Integer, Integer>> free2countMap = new HashMap<>();
	private   Map<String, Map<Integer, Double>> free2rewardMap = new HashMap<>();
	private   Map<String, Map<Integer, Integer>> free3countMap = new HashMap<>();
	private   Map<String, Map<Integer, Double>> free3rewardMap = new HashMap<>();
	private   Map<String, Map<Integer, Integer>> free4countMap = new HashMap<>();
	private   Map<String, Map<Integer, Double>> free4rewardMap = new HashMap<>();
	private   Map<String, Map<Integer, Integer>> dorpcountMap = new HashMap<>();
	private   Map<String, Map<Integer, Double>> dorprewardMap = new HashMap<>();
	
	public void initTest() throws IOException, InterruptedException {
		testOpenGame();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd _HH_mm");
		Date date = new Date(System.currentTimeMillis());
		String format = sdf.format(date);
		String path = "F:\\BACKUP\\工作备份\\BACKUP\\desktop\\work\\数值\\1.0\\bxlm\\4";
		String name = "bxlm-" + format +".txt";
		System.out.println(name);
		File file = new File(path, name);
		System.out.println(file.createNewFile());
		for (int i = 1; i <= round; i++) {
			String testLogin1 = testLogin();
			testOpenGame();
			BxlmResult result = testAuto();
			String testLogin2 = testLogin();
			String filename = i + ".==========>\r\n";
			String content1 = "统计结果: " + result.toString() + "\r\n";
			String content2 = "正常游戏次数统计结果: " + map.toString()+ "\r\n";
			String content3 = "正常游戏奖励统计结果: " + mapreward.toString()+ "\r\n";
			String content4 = "正常游戏wild加倍次数统计结果: " + wildx2.toString()+ "\r\n";
			String content5 = "正常游戏wild奖励加倍金额统计结果: " + wildx2reward.toString()+ "\r\n";
			String content6 = "正常游戏wild1次数统计结果: " + wild1.toString()+ "\r\n";
			String content7 = "正常游戏wild1奖励统计结果: " + wild1reward.toString()+ "\r\n";
			String content8 = "正常游戏wild2次数统计结果: " + wild2.toString()+ "\r\n";
			String content9 = "正常游戏wild2奖励统计结果: " + wild2reward.toString()+ "\r\n";
			String content10 = "正常游戏wild3次数统计结果: " + wild3.toString()+ "\r\n";
			String content11 = "正常游戏wild3奖励统计结果: " + wild3reward.toString()+ "\r\n";
			String content12 = "正常游戏wild4次数统计结果: " + wild4.toString()+ "\r\n";
			String content13 = "正常游戏wild4奖励统计结果: " + wild4reward.toString()+ "\r\n";
			String content14 = "正常游戏wild5次数统计结果: " + wild5.toString()+ "\r\n";
			String content15 = "正常游戏wild5奖励统计结果: " + wild5reward.toString()+ "\r\n";
			String content16 = "免费游戏model1次数统计结果: " + free1countMap.toString()+ "\r\n";
			String content17 = "免费游戏model1奖励统计结果: " + free1rewardMap.toString()+ "\r\n";
			String content18 = "免费游戏model2次数统计结果: " + free2countMap.toString()+ "\r\n";
			String content19 = "免费游戏model2奖励统计结果: " + free2rewardMap.toString()+ "\r\n";
			String content20 = "免费游戏model3次数统计结果: " + free3countMap.toString()+ "\r\n";
			String content21 = "免费游戏model3奖励统计结果: " + free3rewardMap.toString()+ "\r\n";
			String content22 = "免费游戏model4次数统计结果: " + free4countMap.toString()+ "\r\n";
			String content23 = "免费游戏model4奖励统计结果: " + free4rewardMap.toString()+ "\r\n";
			String content24 = "免费游戏wild加倍次数统计结果: " + wildx2countMap.toString()+ "\r\n";
			String content25 = "免费游戏wild加倍奖励统计结果: " + wildx2rewardMap.toString()+ "\r\n";
			String content26 = "免费游戏掉落次数统计结果: " + dorpcountMap.toString()+ "\r\n";
			String content27 = "免费游戏掉落奖励统计结果: " + dorprewardMap.toString()+ "\r\n";
			String content28 = "游戏开始金币: " + testLogin1.toString()+ "\r\n";
			String content29 = "游戏结束金币: " + testLogin2.toString()+ "\r\n";
			String output = filename 
					+ content1 
					+ content2 
					+ content3 
					+ content4 
					+ content5 
					+ content6 
					+ content7 
					+ content8 
					+ content9 
					+ content10 
					+ content11 
					+ content12 
					+ content13 
					+ content14 
					+ content15 
					+ content16 
					+ content17 
					+ content18 
					+ content19 
					+ content20 
					+ content21 
					+ content22
					+ content23 
					+ content24 
					+ content25 
					+ content26 
					+ content27 
					+ content28 
					+ content29
					;
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
		// 免费游戏次数
		freeCount = 0;
		// 正常游戏中奖次数
		totalCount = 0;
		// 免费游戏中奖次数
		freetotalCount = 0;
		// 正常游戏奖励
		totalReward = 0;
		wild1count = 0;
		wild2count = 0;
		wild3count = 0;
		wild4count = 0;
		wild5count = 0;
		wild1total = 0;
		wild2total = 0;
		wild3total = 0;
		wild4total = 0;
		wild5total = 0;
		wildtotalReward = 0;
		// 免费游戏奖励
		freetotalReward = 0;
		map.clear();
		mapreward.clear();
		wildx2.clear();
		wildx2reward.clear();
		wild1.clear();
		wild1reward.clear();
		wild2.clear();
		wild2reward.clear();
		wild3.clear();
		wild3reward.clear();
		wild4.clear();
		wild4reward.clear();
		wild5.clear();
		free1countMap.clear();
		free1rewardMap.clear();
		free2countMap.clear();
		free2rewardMap.clear();
		free3countMap.clear();
		free3rewardMap.clear();
		free4countMap.clear();
		free4rewardMap.clear();
		dorpcountMap.clear();
		dorprewardMap.clear();
		wildx2countMap.clear();
		wildx2rewardMap.clear();
		String result =  "[清除数据成功!当前数据 : ]"
				+ "gameCount = " + gameCount
				+ ", freeCount = " + freeCount
				+ ", totalCount = " + totalCount
				+ ", wild1count = " + wild1count
				+ ", wild2count = " + wild2count
				+ ", wild3count = " + wild3count
				+ ", wild4count = " + wild4count
				+ ", wild5count = " + wild5count
				+ ", wild1total = " + wild1total
				+ ", wild2total = " + wild2total
				+ ", wild3total = " + wild3total
				+ ", wild4total = " + wild4total
				+ ", wild5total = " + wild5total
				+ ", freetotalCount = " + freetotalCount
				+ ", totalReward = " + totalReward
				+ ", freetotalReward = " + freetotalReward
				+ ", wildtotalReward = " + wildtotalReward
				+ ", map = " + map.size()
				+ ", mapreward = " + mapreward.size()
				+ ", wild1 = " + wild1.size()
				+ ", wild1reward = " + wild1reward.size()
				+ ", wild2 = " + wild2.size()
				+ ", wild2reward = " + wild2reward.size()
				+ ", wild3 = " + wild3.size()
				+ ", wild3reward = " + wild3reward.size()
				+ ", wild4 = " + wild4.size()
				+ ", wild4reward = " + wild4reward.size()
				+ ", wild5 = " + wild5.size()
				+ ", free1countMap = " + free1countMap.size()
				+ ", free1rewardMap = " + free1rewardMap.size()
				+ ", free2countMap = " + free2countMap.size()
				+ ", free2rewardMap = " + free2rewardMap.size()
				+ ", free3countMap = " + free3countMap.size()
				+ ", free3rewardMap = " + free3rewardMap.size()
				+ ", free4countMap = " + free4countMap.size()
				+ ", free4rewardMap = " + free4rewardMap.size()
				+ ", dorpcountMap = " + dorpcountMap.size()
				+ ", dorprewardMap = " + dorprewardMap.size()
				+ ", wildx2countMap = " + dorprewardMap.size()
				+ ", wildx2rewardMap = " + dorprewardMap.size()
				+"\r\n"
				;
		return result;
	}
	/**
	 * 自动测试
	 * @return 
	 * @throws InterruptedException 
	 */
	public  BxlmResult testAuto() throws InterruptedException{
		// 统计结果
		BxlmResult yzhxResult = new BxlmResult();
		// 开始测试
		for (int i = 0; i < betCount; i++) {
			JSONObject result = testStartGame(line,lineBet);
			if (result != null) {
				//System.out.println("正常游戏result: " + result);
				boolean scatter = result.getBooleanValue("scatter");
				JSONArray jsonArray = result.getJSONArray("god");
				if (jsonArray != null) {
					if (jsonArray.size() ==  0) {
						dataRecord0(result);
					}
					if (jsonArray.size() ==  1) {
						dataRecord1(result);
					}
					if (jsonArray.size() ==  2) {
						dataRecord2(result);
					}
					if (jsonArray.size() ==  3) {
						dataRecord3(result);
					}
					if (jsonArray.size() ==  4) {
						dataRecord4(result);
					}
					if (jsonArray.size() ==  5) {
						dataRecord5(result);
					}
				}
				
				if (scatter) {
					freeCount ++ ;
					JSONObject freeRsult = testFreeGame1();
					//System.out.println("免费游戏模式选择result: " + freeRsult);
					JSONObject scatterinfo = freeRsult.getJSONObject("scatter");
					int model = scatterinfo.getIntValue("model");
					int num = scatterinfo.getIntValue("num");
					while (num != 0) {
						JSONObject freeRsult2 = testStartGame(line,lineBet);
						//System.out.println("免费游戏result: " + freeRsult2);
						int scatterNum = freeRsult2.getIntValue("scatterNum");
						if (model == 1) {
							freedataRecord1(freeRsult2);
						}
						if (model == 2) {
							freedataRecord2(freeRsult2);
						}
						if (model == 3) {
							freedataRecord3(freeRsult2);
						}
						if (model == 4) {
							freedataRecord4(freeRsult2);
						}
						num = scatterNum;
						boolean replenish = freeRsult2.getBooleanValue("replenish");
						while (replenish) {
							JSONObject dorpGameResult = dorpGame();
							//System.out.println("掉落游戏result: " + dorpGameResult);
							replenish = dorpGameResult.getBooleanValue("replenish");
							dorpdataRecord(dorpGameResult);
						}
					}
				}
				gameCount ++;
				System.out.println("目前自动运行次数: " + gameCount);
			}
			yzhxResult.setBetCount(betCount);
			yzhxResult.setTotalBet(lineBet*30*line*gameCount);
			yzhxResult.setTotalCount(totalCount);
			yzhxResult.setTotalreward(totalReward);
			yzhxResult.setFreeCount(freeCount);
			yzhxResult.setWild1count(wild1count);
			yzhxResult.setWild2count(wild2count);
			yzhxResult.setWild3count(wild3count);
			yzhxResult.setWild4count(wild4count);
			yzhxResult.setWild5count(wild5count);
			yzhxResult.setWild1total(wild1total);
			yzhxResult.setWild2total(wild2total);
			yzhxResult.setWild3total(wild3total);
			yzhxResult.setWild4total(wild4total);
			yzhxResult.setWild5total(wild5total);
			yzhxResult.setWildtotalReward(wildtotalReward);
			yzhxResult.setFreetotalCount(freetotalCount);
			yzhxResult.setFreetotalReward(freetotalReward);
			yzhxResult.setGameCount(gameCount);
			yzhxResult.setTotalIncomelv((totalReward + freetotalReward)/(lineBet*30*line*gameCount));
			yzhxResult.setTotalRewardlv(NumberTool.divide(totalCount, betCount).doubleValue());
			//System.out.println("统计结果: " + yzhxResult);
			//System.out.println("正常游戏统计结果: " + map);
			}
		return yzhxResult;
	}
	
	/**
	 * 测试老虎机登陆
	 * @return 
	 */
	public  String testLogin(){
		int messageid = 1900;
		
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
		int messageid = 1901;
		
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
	 * @throws InterruptedException 
	 */
	public  JSONObject testStartGame(int line,double lineBet) throws InterruptedException{
		int messageid = 1902;
		
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
	 * @throws InterruptedException 
	 */
	public  JSONObject dorpGame() throws InterruptedException{
		int messageid = 1903;
		
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
	 * 免费模式选择
	 * @return 
	 * @throws InterruptedException 
	 */
	public  JSONObject testFreeGame1() throws InterruptedException{
		int messageid = 1904;
		
		MessageClient mc = new MessageClient();
		mc.setMessageid(messageid);
		mc.setUuid(uuid);
		mc.setSid("");
		mc.setType(0);
		Map<String, String> map = new HashMap<>();
		map.put("messageid", String.valueOf(messageid));
		map.put("roleid", roleid);
		map.put("model", String.valueOf(RandomUtil.getRandom(1, 4)));
		map.put("uuid", uuid);
		mc.setParams(map);
		HttpMessageResult result = mainProcess.mainProcessMsg(mc);
		return JSONObject.parseObject(JSONObject.toJSONString(result));
		}
	
	
	/**
	 * 正常游戏数据记录
	 * @param yzhxResult 
	 */
	public  void dataRecord0(JSONObject data){
		JSONArray info = data.getJSONArray("winInfo");
		double rewardcoin = data.getDoubleValue("rewardcoin");
		totalReward += rewardcoin;
		//System.out.println("本次奖励===============>" + rewardcoin);
		if (info != null) {
			if (info.size() > 0) {
				for (Object rewardInfo2 : info) {
					JSONObject parseObject = JSONObject.parseObject(rewardInfo2.toString());
					JSONArray jsonArray = parseObject.getJSONArray("winIcon");
					int wild = 0;
					for (Object object : jsonArray) {
						JSONObject parseObject2 = JSONObject.parseObject(object.toString());
						String icon2 = parseObject2.getString("icon");
						if (icon2.equalsIgnoreCase("WILD")) {
							wild++;
						}
					}
					if (wild > 0) {
						wildx2dataRecord0(parseObject);
						continue;
					}
					totalCount ++;
					//System.out.println("rewardInfo2===============>" + parseObject);
					int winNum = parseObject.getIntValue("winNum");
					int num = parseObject.getIntValue("num");
					String icon = parseObject.getString("icon");
					Double reward = parseObject.getDouble("reward");
					
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
					
					if (mapreward.containsKey(icon)) {
						Map<Integer, Double> map2 = mapreward.get(icon);
						if (map2.containsKey(winNum)) {
							map2.put(winNum, map2.get(winNum) + reward);
						} else {
							map2.put(winNum, reward);
						}
					} else{
						Map<Integer, Double> count = new HashMap<>();
						count.put(winNum, reward);
						mapreward.put(icon, count);
					}
				}
			}
		}
	}
	
	/**
	 * 正常游戏数据记录
	 * @param yzhxResult 
	 */
	public  void dataRecord1(JSONObject data){
		double rewardcoin = data.getDoubleValue("rewardcoin");
		totalReward += rewardcoin;
		wild1total += rewardcoin;
		wild1count ++;
		//System.out.println("本次奖励===============>" + rewardcoin);
		JSONArray rewardInfo = data.getJSONArray("winInfo");
		if (rewardInfo.size() > 0) {
			totalCount ++;
			for (Object rewardInfo2 : rewardInfo) {
				JSONObject parseObject = JSONObject.parseObject(rewardInfo2.toString());
				//System.out.println("rewardInfo2===============>" + parseObject);
				int winNum = parseObject.getIntValue("winNum");
				int num = parseObject.getIntValue("num");
				String icon = parseObject.getString("icon");
				Double reward = parseObject.getDouble("reward");
				
				if (wild1.containsKey(icon)) {
					Map<Integer, Integer> map2 = wild1.get(icon);
					if (map2.containsKey(winNum)) {
						map2.put(winNum, map2.get(winNum) + num);
					} else {
						map2.put(winNum, num);
					}
				} else{
					Map<Integer, Integer> count = new HashMap<>();
					count.put(winNum, num);
					wild1.put(icon, count);
				}
				
				if (wild1reward.containsKey(icon)) {
					Map<Integer, Double> map2 = wild1reward.get(icon);
					if (map2.containsKey(winNum)) {
						map2.put(winNum, map2.get(winNum) + reward);
					} else {
						map2.put(winNum, reward);
					}
				} else{
					Map<Integer, Double> count = new HashMap<>();
					count.put(winNum, reward);
					wild1reward.put(icon, count);
				}
			}
		}
	}
	
	
	/**
	 * 正常游戏数据记录
	 * @param yzhxResult 
	 */
	public  void dataRecord2(JSONObject data){
		double rewardcoin = data.getDoubleValue("rewardcoin");
		totalReward += rewardcoin;
		wild2total += rewardcoin;
		wild2count ++;
		//System.out.println("本次奖励===============>" + rewardcoin);
		JSONArray rewardInfo = data.getJSONArray("winInfo");
		if (rewardInfo.size() > 0) {
			totalCount ++;
			for (Object rewardInfo2 : rewardInfo) {
				JSONObject parseObject = JSONObject.parseObject(rewardInfo2.toString());
				//System.out.println("rewardInfo2===============>" + parseObject);
				int winNum = parseObject.getIntValue("winNum");
				int num = parseObject.getIntValue("num");
				String icon = parseObject.getString("icon");
				Double reward = parseObject.getDouble("reward");
				
				if (wild2.containsKey(icon)) {
					Map<Integer, Integer> map2 = wild2.get(icon);
					if (map2.containsKey(winNum)) {
						map2.put(winNum, map2.get(winNum) + num);
					} else {
						map2.put(winNum, num);
					}
				} else{
					Map<Integer, Integer> count = new HashMap<>();
					count.put(winNum, num);
					wild2.put(icon, count);
				}
				
				if (wild2reward.containsKey(icon)) {
					Map<Integer, Double> map2 = wild2reward.get(icon);
					if (map2.containsKey(winNum)) {
						map2.put(winNum, map2.get(winNum) + reward);
					} else {
						map2.put(winNum, reward);
					}
				} else{
					Map<Integer, Double> count = new HashMap<>();
					count.put(winNum, reward);
					wild2reward.put(icon, count);
				}
				
			}
		}
	}
	
	
	/**
	 * 正常游戏数据记录
	 * @param yzhxResult 
	 */
	public  void dataRecord3(JSONObject data){
		double rewardcoin = data.getDoubleValue("rewardcoin");
		totalReward += rewardcoin;
		wild3total += rewardcoin;
		wild3count ++;
		//System.out.println("本次奖励===============>" + rewardcoin);
		JSONArray rewardInfo = data.getJSONArray("winInfo");
		if (rewardInfo.size() > 0) {
			totalCount ++;
			for (Object rewardInfo2 : rewardInfo) {
				JSONObject parseObject = JSONObject.parseObject(rewardInfo2.toString());
				//System.out.println("rewardInfo2===============>" + parseObject);
				int winNum = parseObject.getIntValue("winNum");
				int num = parseObject.getIntValue("num");
				String icon = parseObject.getString("icon");
				Double reward = parseObject.getDouble("reward");
				
				if (wild3.containsKey(icon)) {
					Map<Integer, Integer> map2 = wild3.get(icon);
					if (map2.containsKey(winNum)) {
						map2.put(winNum, map2.get(winNum) + num);
					} else {
						map2.put(winNum, num);
					}
				} else{
					Map<Integer, Integer> count = new HashMap<>();
					count.put(winNum, num);
					wild3.put(icon, count);
				}
				
				if (wild3reward.containsKey(icon)) {
					Map<Integer, Double> map2 = wild3reward.get(icon);
					if (map2.containsKey(winNum)) {
						map2.put(winNum, map2.get(winNum) + reward);
					} else {
						map2.put(winNum, reward);
					}
				} else{
					Map<Integer, Double> count = new HashMap<>();
					count.put(winNum, reward);
					wild3reward.put(icon, count);
				}
				
			}
		}
	}
	
	/**
	 * 正常游戏数据记录
	 * @param yzhxResult 
	 */
	public  void dataRecord4(JSONObject data){
		double rewardcoin = data.getDoubleValue("rewardcoin");
		totalReward += rewardcoin;
		wild4total += rewardcoin;
		wild4count ++;
		//System.out.println("本次奖励===============>" + rewardcoin);
		JSONArray rewardInfo = data.getJSONArray("winInfo");
		if (rewardInfo.size() > 0) {
			totalCount ++;
			for (Object rewardInfo2 : rewardInfo) {
				JSONObject parseObject = JSONObject.parseObject(rewardInfo2.toString());
				//System.out.println("rewardInfo2===============>" + parseObject);
				int winNum = parseObject.getIntValue("winNum");
				int num = parseObject.getIntValue("num");
				String icon = parseObject.getString("icon");
				Double reward = parseObject.getDouble("reward");
				
				if (wild4.containsKey(icon)) {
					Map<Integer, Integer> map2 = wild4.get(icon);
					if (map2.containsKey(winNum)) {
						map2.put(winNum, map2.get(winNum) + num);
					} else {
						map2.put(winNum, num);
					}
				} else{
					Map<Integer, Integer> count = new HashMap<>();
					count.put(winNum, num);
					wild4.put(icon, count);
				}
				
				if (wild4reward.containsKey(icon)) {
					Map<Integer, Double> map2 = wild4reward.get(icon);
					if (map2.containsKey(winNum)) {
						map2.put(winNum, map2.get(winNum) + reward);
					} else {
						map2.put(winNum, reward);
					}
				} else{
					Map<Integer, Double> count = new HashMap<>();
					count.put(winNum, reward);
					wild4reward.put(icon, count);
				}
				
			}
		}
	}
	
	/**
	 * 正常游戏数据记录
	 * @param yzhxResult 
	 */
	public  void dataRecord5(JSONObject data){
		double rewardcoin = data.getDoubleValue("rewardcoin");
		totalReward += rewardcoin;
		wild5total += rewardcoin;
		wild5count ++;
		//System.out.println("本次奖励===============>" + rewardcoin);
		JSONArray rewardInfo = data.getJSONArray("winInfo");
		if (rewardInfo.size() > 0) {
			totalCount ++;
			for (Object rewardInfo2 : rewardInfo) {
				JSONObject parseObject = JSONObject.parseObject(rewardInfo2.toString());
				//System.out.println("rewardInfo2===============>" + parseObject);
				int winNum = parseObject.getIntValue("winNum");
				int num = parseObject.getIntValue("num");
				String icon = parseObject.getString("icon");
				Double reward = parseObject.getDouble("reward");
				
				if (wild5.containsKey(icon)) {
					Map<Integer, Integer> map2 = wild5.get(icon);
					if (map2.containsKey(winNum)) {
						map2.put(winNum, map2.get(winNum) + num);
					} else {
						map2.put(winNum, num);
					}
				} else{
					Map<Integer, Integer> count = new HashMap<>();
					count.put(winNum, num);
					wild5.put(icon, count);
				}
				
				if (wild5reward.containsKey(icon)) {
					Map<Integer, Double> map2 = wild5reward.get(icon);
					if (map2.containsKey(winNum)) {
						map2.put(winNum, map2.get(winNum) + reward);
					} else {
						map2.put(winNum, reward);
					}
				} else{
					Map<Integer, Double> count = new HashMap<>();
					count.put(winNum, reward);
					wild5reward.put(icon, count);
				}
				
			}
		}
	}
	
	
	/**
	 * 免费游戏数据记录
	 * @param yzhxResult 
	 */
	public  void freedataRecord1(JSONObject data){
		JSONArray info = data.getJSONArray("winInfo");
		double rewardcoin = data.getDoubleValue("rewardcoin");
		freetotalReward += rewardcoin;
		//System.out.println("本次奖励===============>" + rewardcoin);
		if (info.size() > 0) {
			for (Object rewardInfo2 : info) {
				JSONObject parseObject = JSONObject.parseObject(rewardInfo2.toString());
				JSONArray jsonArray = parseObject.getJSONArray("winIcon");
				int wild = 0;
				for (Object object : jsonArray) {
					JSONObject parseObject2 = JSONObject.parseObject(object.toString());
					String icon2 = parseObject2.getString("icon");
					if (icon2.equalsIgnoreCase("WILD")) {
						wild++;
					}
				}
				if (wild > 0) {
					wildx2freedataRecord0(parseObject);
					continue;
				}
				freetotalCount ++;
				//System.out.println("rewardInfo2===============>" + parseObject);
				int winNum = parseObject.getIntValue("winNum");
				int num = parseObject.getIntValue("num");
				String icon = parseObject.getString("icon");
				Double reward = parseObject.getDouble("reward");
				
				if (free1countMap.containsKey(icon)) {
					Map<Integer, Integer> map2 = free1countMap.get(icon);
					if (map2.containsKey(winNum)) {
						map2.put(winNum, map2.get(winNum) + num);
					} else {
						map2.put(winNum, num);
					}
				} else{
					Map<Integer, Integer> count = new HashMap<>();
					count.put(winNum, num);
					free1countMap.put(icon, count);
				}
				
				if (free1rewardMap.containsKey(icon)) {
					Map<Integer, Double> map2 = free1rewardMap.get(icon);
					if (map2.containsKey(winNum)) {
						map2.put(winNum, map2.get(winNum) + reward);
					} else {
						map2.put(winNum, reward);
					}
				} else{
					Map<Integer, Double> count = new HashMap<>();
					count.put(winNum, reward);
					free1rewardMap.put(icon, count);
				}
			}
		}
		

	}
	
	/**
	 * 免费游戏数据记录
	 * @param yzhxResult 
	 */
	public  void freedataRecord2(JSONObject data){
		double rewardcoin = data.getDoubleValue("rewardcoin");
		freetotalReward += rewardcoin;
		//System.out.println("本次奖励===============>" + rewardcoin);
		if (data != null) {
			JSONArray info = data.getJSONArray("winInfo");
			if (info.size() > 0) {
				for (Object rewardInfo2 : info) {
					JSONObject parseObject = JSONObject.parseObject(rewardInfo2.toString());
					JSONArray jsonArray = parseObject.getJSONArray("winIcon");
					int wild = 0;
					for (Object object : jsonArray) {
						JSONObject parseObject2 = JSONObject.parseObject(object.toString());
						String icon2 = parseObject2.getString("icon");
						if (icon2.equalsIgnoreCase("WILD")) {
							wild++;
						}
					}
					if (wild > 0) {
						wildx2freedataRecord0(parseObject);
						continue;
					}
					freetotalCount ++;
					//System.out.println("rewardInfo2===============>" + parseObject);
					int winNum = parseObject.getIntValue("winNum");
					int num = parseObject.getIntValue("num");
					String icon = parseObject.getString("icon");
					Double reward = parseObject.getDouble("reward");
					
					if (free2countMap.containsKey(icon)) {
						Map<Integer, Integer> map2 = free2countMap.get(icon);
						if (map2.containsKey(winNum)) {
							map2.put(winNum, map2.get(winNum) + num);
						} else {
							map2.put(winNum, num);
						}
					} else{
						Map<Integer, Integer> count = new HashMap<>();
						count.put(winNum, num);
						free2countMap.put(icon, count);
					}
					
					if (free2rewardMap.containsKey(icon)) {
						Map<Integer, Double> map2 = free2rewardMap.get(icon);
						if (map2.containsKey(winNum)) {
							map2.put(winNum, map2.get(winNum) + reward);
						} else {
							map2.put(winNum, reward);
						}
					} else{
						Map<Integer, Double> count = new HashMap<>();
						count.put(winNum, reward);
						free2rewardMap.put(icon, count);
					}
				}
			}
		}
	}
	
	/**
	 * 免费游戏数据记录
	 * @param yzhxResult 
	 */
	public  void freedataRecord3(JSONObject data){
		double rewardcoin = data.getDoubleValue("rewardcoin");
		freetotalReward += rewardcoin;
		//System.out.println("本次奖励===============>" + rewardcoin);
		JSONArray info = data.getJSONArray("winInfo");
		if (info.size() > 0) {
			for (Object rewardInfo2 : info) {
				JSONObject parseObject = JSONObject.parseObject(rewardInfo2.toString());
				JSONArray jsonArray = parseObject.getJSONArray("winIcon");
				int wild = 0;
				for (Object object : jsonArray) {
					JSONObject parseObject2 = JSONObject.parseObject(object.toString());
					String icon2 = parseObject2.getString("icon");
					if (icon2.equalsIgnoreCase("WILD")) {
						wild++;
					}
				}
				if (wild > 0) {
					wildx2freedataRecord0(parseObject);
					continue;
				}
	
				freetotalCount ++;
				//System.out.println("rewardInfo2===============>" + parseObject);
				int winNum = parseObject.getIntValue("winNum");
				int num = parseObject.getIntValue("num");
				String icon = parseObject.getString("icon");
				Double reward = parseObject.getDouble("reward");
				
				if (free3countMap.containsKey(icon)) {
					Map<Integer, Integer> map2 = free3countMap.get(icon);
					if (map2.containsKey(winNum)) {
						map2.put(winNum, map2.get(winNum) + num);
					} else {
						map2.put(winNum, num);
					}
				} else{
					Map<Integer, Integer> count = new HashMap<>();
					count.put(winNum, num);
					free3countMap.put(icon, count);
				}
				
				if (free3rewardMap.containsKey(icon)) {
					Map<Integer, Double> map2 = free3rewardMap.get(icon);
					if (map2.containsKey(winNum)) {
						map2.put(winNum, map2.get(winNum) + reward);
					} else {
						map2.put(winNum, reward);
					}
				} else{
					Map<Integer, Double> count = new HashMap<>();
					count.put(winNum, reward);
					free3rewardMap.put(icon, count);
				}
			}
		}
	}
	
	/**
	 * 免费游戏数据记录
	 * @param yzhxResult 
	 */
	public  void freedataRecord4(JSONObject data){
		double rewardcoin = data.getDoubleValue("rewardcoin");
		freetotalReward += rewardcoin;
		//System.out.println("本次奖励===============>" + rewardcoin);
		JSONArray info = data.getJSONArray("winInfo");
		if (info.size() > 0) {
			for (Object rewardInfo2 : info) {
				JSONObject parseObject = JSONObject.parseObject(rewardInfo2.toString());
				JSONArray jsonArray = parseObject.getJSONArray("winIcon");
				int wild = 0;
				for (Object object : jsonArray) {
					JSONObject parseObject2 = JSONObject.parseObject(object.toString());
					String icon2 = parseObject2.getString("icon");
					if (icon2.equalsIgnoreCase("WILD")) {
						wild++;
					}
				}
				if (wild > 0) {
					wildx2freedataRecord0(parseObject);
					continue;
				}
	
				freetotalCount ++;
				//System.out.println("rewardInfo2===============>" + parseObject);
				int winNum = parseObject.getIntValue("winNum");
				int num = parseObject.getIntValue("num");
				String icon = parseObject.getString("icon");
				Double reward = parseObject.getDouble("reward");
				
				if (free4countMap.containsKey(icon)) {
					Map<Integer, Integer> map2 = free4countMap.get(icon);
					if (map2.containsKey(winNum)) {
						map2.put(winNum, map2.get(winNum) + num);
					} else {
						map2.put(winNum, num);
					}
				} else{
					Map<Integer, Integer> count = new HashMap<>();
					count.put(winNum, num);
					free4countMap.put(icon, count);
				}
				
				if (free4rewardMap.containsKey(icon)) {
					Map<Integer, Double> map2 = free4rewardMap.get(icon);
					if (map2.containsKey(winNum)) {
						map2.put(winNum, map2.get(winNum) + reward);
					} else {
						map2.put(winNum, reward);
					}
				} else{
					Map<Integer, Double> count = new HashMap<>();
					count.put(winNum, reward);
					free4rewardMap.put(icon, count);
				}
			}
		}
		

	}
	
	
	/**
	 * 免费游戏掉落数据记录
	 * @param yzhxResult 
	 */
	public  void dorpdataRecord(JSONObject data){
		double rewardcoin = data.getDoubleValue("reward");
		freetotalReward += rewardcoin;
		//System.out.println("本次奖励===============>" + rewardcoin);
		JSONArray info = data.getJSONArray("winInfo");
		if (info.size() > 0) {
			for (Object rewardInfo2 : info) {
				JSONObject parseObject = JSONObject.parseObject(rewardInfo2.toString());
				JSONArray jsonArray = parseObject.getJSONArray("winIcon");
				int wild = 0;
				for (Object object : jsonArray) {
					JSONObject parseObject2 = JSONObject.parseObject(object.toString());
					String icon2 = parseObject2.getString("icon");
					if (icon2.equalsIgnoreCase("WILD")) {
						wild++;
					}
				}
				if (wild > 0) {
					wildx2freedataRecord0(parseObject);
					continue;
				}

				freetotalCount ++;
				//System.out.println("rewardInfo2===============>" + parseObject);
				int winNum = parseObject.getIntValue("winNum");
				int num = parseObject.getIntValue("num");
				String icon = parseObject.getString("icon");
				Double reward = parseObject.getDouble("reward");
				
				if (dorpcountMap.containsKey(icon)) {
					Map<Integer, Integer> map2 = dorpcountMap.get(icon);
					if (map2.containsKey(winNum)) {
						map2.put(winNum, map2.get(winNum) + num);
					} else {
						map2.put(winNum, num);
					}
				} else{
					Map<Integer, Integer> count = new HashMap<>();
					count.put(winNum, num);
					dorpcountMap.put(icon, count);
				}
				
				if (dorprewardMap.containsKey(icon)) {
					Map<Integer, Double> map2 = dorprewardMap.get(icon);
					if (map2.containsKey(winNum)) {
						map2.put(winNum, map2.get(winNum) + reward);
					} else {
						map2.put(winNum, reward);
					}
				} else{
					Map<Integer, Double> count = new HashMap<>();
					count.put(winNum, reward);
					dorprewardMap.put(icon, count);
				}
			}
		}
	}
	
	/**
	 * 正常游戏加倍记录
	 * @param yzhxResult 
	 */
	public  void wildx2dataRecord0(JSONObject data){
		System.out.println("wild加倍奖励统计传入的数据 =====>" + data);
		double rewardcoin = data.getDoubleValue("reward");
		wildtotalReward += rewardcoin;
		//System.out.println("本次奖励===============>" + rewardcoin);
		totalCount ++;
		JSONObject parseObject = data;
		//System.out.println("rewardInfo2===============>" + parseObject);
		int winNum = parseObject.getIntValue("winNum");
		int num = parseObject.getIntValue("num");
		String icon = parseObject.getString("icon");
		Double reward = parseObject.getDouble("reward");
		
		if (wildx2.containsKey(icon)) {
			Map<Integer, Integer> map2 = wildx2.get(icon);
			if (map2.containsKey(winNum)) {
				map2.put(winNum, map2.get(winNum) + num);
			} else {
				map2.put(winNum, num);
			}
		} else{
			Map<Integer, Integer> count = new HashMap<>();
			count.put(winNum, num);
			wildx2.put(icon, count);
		}
		
		if (wildx2reward.containsKey(icon)) {
			Map<Integer, Double> map2 = wildx2reward.get(icon);
			if (map2.containsKey(winNum)) {
				map2.put(winNum, map2.get(winNum) + reward);
			} else {
				map2.put(winNum, reward);
			}
		} else{
			Map<Integer, Double> count = new HashMap<>();
			count.put(winNum, reward);
			wildx2reward.put(icon, count);
		}
	}
	
	/**
	 * 免费游戏加倍记录
	 * @param yzhxResult 
	 */
	public  void wildx2freedataRecord0(JSONObject data){
		double rewardcoin = data.getDoubleValue("reward");
		//System.out.println("本次奖励===============>" + rewardcoin);
		freetotalCount ++;
		JSONObject parseObject = data;
		//System.out.println("rewardInfo2===============>" + parseObject);
		int winNum = parseObject.getIntValue("winNum");
		int num = parseObject.getIntValue("num");
		String icon = parseObject.getString("icon");
		Double reward = parseObject.getDouble("reward");
		
		if (wildx2countMap.containsKey(icon)) {
			Map<Integer, Integer> map2 = wildx2countMap.get(icon);
			if (map2.containsKey(winNum)) {
				map2.put(winNum, map2.get(winNum) + num);
			} else {
				map2.put(winNum, num);
			}
		} else{
			Map<Integer, Integer> count = new HashMap<>();
			count.put(winNum, num);
			wildx2countMap.put(icon, count);
		}
		
		if (wildx2rewardMap.containsKey(icon)) {
			Map<Integer, Double> map2 = wildx2rewardMap.get(icon);
			if (map2.containsKey(winNum)) {
				map2.put(winNum, map2.get(winNum) + reward);
			} else {
				map2.put(winNum, reward);
			}
		} else{
			Map<Integer, Double> count = new HashMap<>();
			count.put(winNum, reward);
			wildx2rewardMap.put(icon, count);
		}
	}
	
}
