package com.zyhy.lhj_server.game.lll.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zyhy.common_lhj.process.MainProcess;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.common_server.result.MessageClient;
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.lhj_server.prcess.result.lll.LllGameBetResult;
@Service
public class LllTest {
	private  String roleid = "802";
	private  String uuid = "1";
	@Autowired
	private MainProcess mainProcess;
	// 正常游戏奖励
	private   double totalReward = 0;
	// 免费游戏触发次数
	private   int freeCount = 0;
	// 免费游戏总次数
	private   int totoalFreeCount = 0;
	// 免费游戏奖励
	private   double freeGameReward = 0;
	// 正常游戏中奖次数
	private   int totalCount = 0;
	// T1出现次数
	private   int t1 = 0;
	// T1总奖励
	private   double t1reward = 0;
	// T2出现次数
	private   int t2 = 0;
	// T2总奖励
	private   double t2reward = 0;
	// T3出现次数
	private   int t3 = 0;
	// T3总奖励
	private   double t3reward = 0;
	// 任意3个次数
	private   int any3 = 0;
	// 任意3个次数奖励
	private   double any3reward = 0;
	// 下注次数
	private   int betCount = 100000;
	// 测试轮数
	private   int round = 1;
	// 正常游戏次数
	private   int gameCount = 0;
	// 下注线数
	private   int line = 1;
	// 下注线注
	private   double lineBet = 1;
	// 正常游戏奖励信息(K：图标名称 V：K：中奖个数 V：中奖次数)
	private   Map<String, Map<Integer, Integer>> map = new HashMap<>();
	// 免费游戏奖励信息
	private   Map<Integer, Map<Integer, Double>> freeMap = new HashMap<>();
	
	public  void initTest() throws IOException, InterruptedException {
		testOpenGame();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd _HH_mm");
		Date date = new Date(System.currentTimeMillis());
		String format = sdf.format(date);
		String path = "C:\\Users\\Administrator\\Desktop\\shuzhi\\lll\\1";
		String name = "lll-" + format +".txt";
		File file = new File(path, name);
		System.out.println(file.createNewFile());
		for (int i = 1; i <= round; i++) {
			LllResult result = testAuto();
			String filename = i + ".==========>\r\n";
			String content1 = "统计结果: " + result.toString() + "\r\n";
			String content2 = "正常游戏统计结果: " + map.toString()+ "\r\n";
			String content3 = "免费游戏统计结果: " + freeMap.toString()+ "\r\n";
			String output = filename + content1 + content2 + content3;
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
		freeCount = 0;
		// 免费游戏总次数
		totoalFreeCount = 0;
		// 免费游戏奖励
		freeGameReward = 0;
		// 正常游戏中奖次数
		totalCount = 0;
		// T1出现次数
		t1 = 0;
		// T1总奖励
		t1reward = 0;
		// T2出现次数
		t2 = 0;
		// T2总奖励
		t2reward = 0;
		// T3出现次数
		t3 = 0;
		// T3总奖励
		t3reward = 0;
		// 任意3个次数
		any3 = 0;
		// 任意3个次数奖励
		any3reward = 0;
		// 正常游戏次数
		gameCount = 0;
		// 正常游戏奖励信息(K：图标名称 V：K：中奖个数 V：中奖次数)
		map.clear();
		// 免费游戏奖励信息
		freeMap.clear();
		String result =  "[清除数据成功!当前数据 : ]"
				+ "totalReward = " + totalReward
				+ ", freeCount = " + freeCount
				+ ", totoalFreeCount = " + totoalFreeCount
				+ ", freeGameReward = " + freeGameReward
				+ ", totalCount = " + totalCount
				+ ", t1 = " + t1
				+ ", t1reward = " + t1reward
				+ ", t2 = " + t2
				+ ", t2reward = " + t2reward
				+ ", t3 = " + t3
				+ ", t3reward = " + t3reward
				+ ", any3 = " + any3
				+ ", any3reward = " + any3reward
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
	public  LllResult testAuto() throws InterruptedException{
		// 统计结果
		LllResult yzhxResult = new LllResult();
		// 开始测试
		for (int i = 0; i < betCount; i++) {
			gameCount ++;
			System.out.println("目前自动运行次数: " + gameCount);
			LllGameBetResult result = testStartGame(line,lineBet);
			dataRecord(result);
			//System.out.println("正常游戏result: " + result);
		}
		yzhxResult.setBetCount(betCount);
		yzhxResult.setTotalBet(lineBet*line*betCount);
		yzhxResult.setTotalCount(totalCount);
		yzhxResult.setTotalreward(totalReward);
		yzhxResult.setFreeCount(freeCount);
		yzhxResult.setTotoalFreeCount(totoalFreeCount);
		yzhxResult.setFreeGameReward(freeGameReward);
		yzhxResult.setGameCount(gameCount);
		yzhxResult.setT1(t1);
		yzhxResult.setT1reward(t1reward);
		yzhxResult.setT2(t2);
		yzhxResult.setT2reward(t2reward);
		yzhxResult.setT3(t3);
		yzhxResult.setT3reward(t3reward);
		yzhxResult.setAny3(any3);
		yzhxResult.setAny3reward(any3reward);
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
	public String testLogin(){
		int messageid = 2500;
		
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
		int messageid = 2501;
		
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
	public  LllGameBetResult testStartGame(int line,double lineBet){
		int messageid = 2502;
		
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
		LllGameBetResult result = (LllGameBetResult) mainProcess.mainProcessMsg(mc);
		return result;
	}
	
	/**
	 * 正常游戏数据记录
	 * @param yzhxResult 
	 */
	public  void dataRecord(LllGameBetResult result){
		LllGameBetResult parseObject = result;
		//System.out.println("LllGameBetResult===================" + parseObject);
		double rewardcoin = parseObject.getRewardcoin();
		if (rewardcoin > 0) {
			totalCount += 1;
			totalReward += rewardcoin;
			
			com.zyhy.lhj_server.game.lll.LllWinInfo winrouteinfos = parseObject.getWinrouteinfos();
			String icon1 = winrouteinfos.getIndex1().getRoller().getIcon().getName();
			String icon2 = winrouteinfos.getIndex2().getRoller().getIcon().getName();
			String icon3 = winrouteinfos.getIndex3().getRoller().getIcon().getName();
			
			if(icon1.equals(icon2) && icon2.equals(icon3) ){
				if(icon1.equals("T1")){
					t1 ++;
					t1reward += rewardcoin;
				}
				if(icon1.equals("T2")){
					t2 ++;
					t2reward += rewardcoin;
				}
				if(icon1.equals("T3")){
					t3 ++;
					t3reward += rewardcoin;
				}
			} else {
				any3 ++ ;
				any3reward += rewardcoin;
			}
			
		}
	}
}
