package com.zyhy.lhj_server.game.gghz.test;

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

@Service
public class gghzTest {
	private  String roleid = "802";
	private  String uuid = "1";
	@Autowired
	private MainProcess mainProcess;
	// 下注次数
	private   int betCount = 100000;
	// 测试轮数
	private   int round = 1;
	// 下注线数
	private   int line = 1;
	// 下注线注
	private   double lineBet = 0.5;
	
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
	// 正常游戏奖励信息(K：图标名称 V：K：中奖次数 V：中奖总金额)
	private   Map<String, Integer> map = new HashMap<>();
	// 免费游戏奖励信息
	private   Map<Integer, Map<Integer, Double>> freeMap = new HashMap<>();
	// 单轴图标出现次数统计
	private   Map<String, Map<Integer, Integer>> iconCount = new HashMap<>();
	
	public  void initTest() throws IOException, InterruptedException {
		testOpenGame();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd _HH_mm");
		Date date = new Date(System.currentTimeMillis());
		String format = sdf.format(date);
		String path = "C:\\Users\\Administrator\\Desktop\\shuzhi\\gghz\\1";
		String name = "gghz-" + format +".txt";
		File file = new File(path, name);
		System.out.println(file.createNewFile());
		for (int i = 1; i <= round; i++) {
			String testLogin1 = testLogin();
			gghzResult result = testAuto();
			String testLogin2 = testLogin();
			String filename = i + ".==========>\r\n";
			String content1 = "统计结果: " + result.toString() + "\r\n";
			String content2 = "正常游戏统计结果: " + map.toString()+ "\r\n";
			String content3 = "免费游戏统计结果: " + freeMap.toString()+ "\r\n";
			String content4 = "单轴图标统计结果: " + iconCount.toString()+ "\r\n";
			String content5 = "游戏开始金币: " + testLogin1.toString()+ "\r\n";
			String content6 = "游戏结束金币: " + testLogin2.toString()+ "\r\n";
			String output = filename + content1 + content2 + content3 + content4 + content5 + content6;
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
		// 单轴图标统计
		iconCount.clear();
		String result =  "[清除数据成功!当前数据 : ]"
				+ "totalReward = " + totalReward
				+ ", freeCount = " + freeCount
				+ ", totoalFreeCount = " + totoalFreeCount
				+ ", freeGameReward = " + freeGameReward
				+ ", totalCount = " + totalCount
				+ ", gameCount = " + gameCount
				+ ", map = " + map.size()
				+ ", freeMap = " + freeMap.size()
				+ ", iconCount = " + iconCount.size()
				+"\r\n"
				;
		return result;
	}
	
	/**
	 * 自动测试
	 * @return 
	 * @throws InterruptedException 
	 */
	public  gghzResult testAuto() throws InterruptedException{
		// 统计结果
		gghzResult yzhxResult = new gghzResult();
		// 开始测试
		for (int i = 0; i < betCount; i++) {
			//Thread.sleep(20L);
			gameCount ++;
			System.out.println("目前自动运行次数: " + gameCount);
			JSONObject result = testStartGame(line,lineBet);
			//System.out.println("正常游戏result: " + result);
			dataRecord(result);
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
		System.out.println("单轴图标统计结果: " + iconCount);
		return yzhxResult;
	}
	
	/**
	 * 测试老虎机登陆
	 * @return 
	 */
	public String testLogin(){
		int messageid = 1100;
		
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
		int messageid = 1101;
		
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
		int messageid = 1102;
		
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
	 * 正常游戏数据记录
	 * @param yzhxResult 
	 */
	public  void dataRecord(JSONObject data){
		GghzGameBetResult parseObject = JSONObject.parseObject(data.toString(), GghzGameBetResult.class);
		//System.out.println("GghzGameBetResult===================" + parseObject);
		List<GghzWindowInfo> windowinfos = parseObject.getWindowinfos();
		for (GghzWindowInfo wl : windowinfos) {
			if (wl.getIndex() == 2) {
				int id = wl.getId();
				String icon = wl.getRoller().getIcon();
				if (iconCount.containsKey(icon)) {
					Map<Integer, Integer> map2 = iconCount.get(icon);
					if (map2.containsKey(id)) {
						map2.put(id, map2.get(id) + 1);
					} else {
						map2.put(id, 1);
					}
				} else {
					Map<Integer, Integer> map2 = new HashMap<>();
					map2.put(id, 1);
					iconCount.put(icon, map2);
				}
			}
			
		}
		double rewardcoin = parseObject.getRewardcoin();
		if (rewardcoin > 0) {
			totalCount += 1;
			totalReward += rewardcoin;
			GghzWinInfo winrouteinfos = parseObject.getWinrouteinfos();
			//System.out.println("winrouteinfos===================" + winrouteinfos);
			String icon1 = winrouteinfos.getIndex1().getRoller().getIcon();
			String icon2 = winrouteinfos.getIndex2().getRoller().getIcon();
			String icon3 = winrouteinfos.getIndex3().getRoller().getIcon();
			if(icon1.equals(icon2) && icon2.equals(icon3) ){
				if (map.containsKey(icon1)) {
					Integer integer = map.get(icon1);
					map.put(icon1, integer + 1);
				} else{
					map.put(icon1, 1);
				}
			} 
				
		}
	}
}
