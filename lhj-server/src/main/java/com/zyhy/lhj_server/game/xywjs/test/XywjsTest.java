package com.zyhy.lhj_server.game.xywjs.test;

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
import com.zyhy.common_lhj.process.MainProcess;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.common_server.result.MessageClient;
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.lhj_server.prcess.result.xywjs.XywjsBeginGameResult;
import com.zyhy.lhj_server.prcess.result.xywjs.XywjsLuckyGameResult;
@Service
public class XywjsTest {
	private  String roleid = "802";
	private  String uuid = "1";
	@Autowired
	private MainProcess mainProcess;
	// 正常游戏总奖励
	private   double totalReward = 0;
	//lucky游戏总次数
	private   int luckyFreeCount = 0;
	// lucky游戏中奖次数
	private   int luckyCount = 0;
	// lucky游戏总奖励
	private   double luckyGameReward = 0;
	// 正常游戏中奖次数
	private   int totalCount = 0;
	// 下注次数
	private   int betCount = 10000;
	// 测试轮数
	private   int round = 1;
	// 正常游戏次数
	private   int gameCount = 0;
	// 下注线数
	private   int line = 9;
	// 下注线注
	private   String lineBet = "1,1,1,1,1,1,1,1";
	private   double lineBet1 = 8;
	// 正常游戏奖励信息(K：图标名称 V：K：中奖个数 V：中奖次数)
	private   Map<Integer, Integer> countMap = new HashMap<>();
	private   Map<Integer, Double> rewardMap = new HashMap<>();
	// lucky游戏奖励信息
	private   Map<Integer, Integer> luckycountMap = new HashMap<>();
	private   Map<Integer, Double> luckyrewardMap = new HashMap<>();
	
	public  void initTest() throws IOException, InterruptedException {
		testOpenGame();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd _HH_mm");
		Date date = new Date(System.currentTimeMillis());
		String format = sdf.format(date);
		String path = "F:\\BACKUP\\工作备份\\BACKUP\\desktop\\work\\数值\\2.0\\xywjs\\2";
		String name = "xywjs-" + format +".txt";
		File file = new File(path, name);
		System.out.println(file.createNewFile());
		for (int i = 1; i <= round; i++) {
			String testLogin1 = testLogin();
			XywjsResult result = testAuto();
			String testLogin2 = testLogin();
			String filename = i + ".==========>\r\n";
			String content1 = "统计结果: " + result.toString() + "\r\n";
			String content2 = "正常游戏统计结果: " + countMap.toString()+ "\r\n";
			String content3 = "正常游戏金币统计结果: " + rewardMap.toString()+ "\r\n";
			String content4 = "lucky游戏统计结果: " + luckycountMap.toString()+ "\r\n";
			String content5 = "lucky游戏金币统计结果: " + luckyrewardMap.toString()+ "\r\n";
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
		// 正常游戏奖励
		totalReward = 0;
		// 免费游戏触发次数
		luckyCount = 0;
		// 免费游戏总次数
		luckyFreeCount = 0;
		// 免费游戏奖励
		luckyGameReward = 0;
		// 正常游戏中奖次数
		totalCount = 0;
		// 正常游戏次数
		gameCount = 0;
		
		countMap.clear();
		rewardMap.clear();
		luckycountMap.clear();
		luckyrewardMap.clear();
		String result =  "[清除数据成功!当前数据 : ]"
				+ "totalReward = " + totalReward
				+ ", freeCount = " + luckyCount
				+ ", totoalFreeCount = " + luckyFreeCount
				+ ", luckyGameReward = " + luckyGameReward
				+ ", totalCount = " + totalCount
				+ ", gameCount = " + gameCount
				+ ", countMap = " + countMap.size()
				+ ", rewardMap = " + rewardMap.size()
				+ ", luckycountMap = " + luckycountMap.size()
				+ ", luckyrewardMap = " + luckyrewardMap.size()
				+"\r\n"
				;
		return result;
	}
	
	/**
	 * 自动测试
	 * @return 
	 * @throws InterruptedException 
	 */
	public  XywjsResult testAuto() throws InterruptedException{
		// 统计结果
		XywjsResult yzhxResult = new XywjsResult();
		// 开始测试
		for (int i = 0; i < betCount; i++) {
			//Thread.sleep(5L);
			System.out.println("目前自动运行次数: " + gameCount);
			XywjsBeginGameResult result = testStartGame(line,lineBet);
			dataRecord(result);
			//System.out.println("正常游戏result: " + result);
		}
		yzhxResult.setBetCount(betCount);
		yzhxResult.setTotalBet(lineBet1*betCount);
		yzhxResult.setTotalCount(totalCount);
		yzhxResult.setTotalreward(totalReward);
		yzhxResult.setFreeCount(luckyCount);
		yzhxResult.setTotoalFreeCount(luckyFreeCount);
		yzhxResult.setFreeGameReward(luckyGameReward);
		yzhxResult.setGameCount(gameCount);
		yzhxResult.setTotalIncomelv((totalReward + luckyGameReward)/(lineBet1*betCount));
		yzhxResult.setTotalRewardlv(NumberTool.divide((totalCount + luckyCount), betCount).doubleValue());
		return yzhxResult;
	}
	
	/**
	 * 测试老虎机登陆
	 */
	public  String testLogin(){
		int messageid = 2900;
		
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
		int messageid = 2901;
		
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
	public  XywjsBeginGameResult testStartGame(int line,String lineBet){
		int messageid = 2902;
		
		MessageClient mc = new MessageClient();
		mc.setMessageid(messageid);
		mc.setUuid(uuid);
		mc.setSid("");
		mc.setType(0);
		Map<String, String> map = new HashMap<>();
		map.put("messageid", String.valueOf(messageid));
		map.put("roleid", roleid);
		map.put("xiazhu", lineBet);
		map.put("uuid", uuid);
		mc.setParams(map);
		XywjsBeginGameResult result = (XywjsBeginGameResult) mainProcess.mainProcessMsg(mc);
		return result;
	}
	
	/**
	 * 正常游戏数据记录
	 * @param yzhxResult 
	 */
	public  void dataRecord(XywjsBeginGameResult result){
		
		if (result.getLuckyresults().size() > 0) {
			luckyRecord(result);
			return;
		}
		
		gameCount ++; 
		double rewardcoin = result.getWinnumber();
		totalReward += rewardcoin;
		if (rewardcoin > 0) {
			totalCount += 1;
			
			int id = result.getZhuanpanid();
			if (countMap.containsKey(id)) {
				countMap.put(id, countMap.get(id) + 1);
			} else {
				countMap.put(id, 1);
			}
			
			if (rewardMap.containsKey(id)) {
				rewardMap.put(id, rewardMap.get(id) + rewardcoin);
			} else {
				rewardMap.put(id, rewardcoin);
			}
		}
	}
	
	/**
	 * lucky游戏数据记录
	 * @param yzhxResult 
	 */
	public  void luckyRecord(XywjsBeginGameResult result){
		luckyFreeCount ++; 
		double rewardcoin = result.getWinnumber();
		luckyGameReward += rewardcoin;
		if (rewardcoin > 0) {
			luckyCount += 1;
			//List<XywjsLuckyGameResult> luckyresults = result.getLuckyresults();
				
			//int id = re.getZhuanpanid();
			//double reward = re.getWinnumber();
			int luckyModel = result.getLuckyModel();
			
			if (luckycountMap.containsKey(luckyModel)) {
				luckycountMap.put(luckyModel, luckycountMap.get(luckyModel) + 1);
			} else {
				luckycountMap.put(luckyModel, 1);
			}
			
			if (luckyrewardMap.containsKey(luckyModel)) {
				luckyrewardMap.put(luckyModel, luckyrewardMap.get(luckyModel) + rewardcoin);
			} else {
				luckyrewardMap.put(luckyModel, rewardcoin);
			}
		}
	}
	
}
