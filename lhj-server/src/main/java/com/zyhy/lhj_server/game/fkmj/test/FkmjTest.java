package com.zyhy.lhj_server.game.fkmj.test;

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
import com.zyhy.common_server.util.RandomUtil;
import com.zyhy.lhj_server.game.fkmj.FkmjBonusInfo;
import com.zyhy.lhj_server.prcess.result.fkmj.FkmjGameBetResult;
@Service
public class FkmjTest {
	private  String roleid = "802";
	private  String uuid = "1";
	@Autowired
	private MainProcess mainProcess;
	// 下注线数
	private   int line = 25;
	// 下注线注
	private   double lineBet = 1;
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
	private  double dorptotalReward = 0;
	// 免费游戏触发次数
	private   int freeCount = 0;
	// 免费游戏总次数
	private   int totoalFreeCount = 0;
	// 免费游戏奖励
	private   double freeGameReward = 0;
	private   double dorpfreeGameReward = 0;
	// 正常游戏奖励信息(K：图标名称 V：K：中奖个数 V：中奖次数)
	private   Map<String, Map<Integer, Integer>> map = new HashMap<>();
	private   Map<String, Map<Integer, Integer>> map1 = new HashMap<>();
	// 免费游戏奖励信息
	private   Map<String, Map<Integer, Integer>> freeMap = new HashMap<>();
	private   Map<String, Map<Integer, Integer>> freeMap1 = new HashMap<>();
	
	// 免费游戏掉落加倍统计
	private  double dorpfreereward2 = 0;
	private   Map<String, Map<String, Map<Integer, Integer>>> dorpfreeMap2 = new HashMap<>();
	
	public  void initTest() throws IOException, InterruptedException {
		testOpenGame();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd _HH_mm");
		Date date = new Date(System.currentTimeMillis());
		String format = sdf.format(date);
		String path = "C:\\Users\\Administrator\\Desktop\\shuzhi\\fkmj\\1";
		String name = "fkmj-" + format +".txt";
		File file = new File(path, name);
		System.out.println(file.createNewFile());
		for (int i = 1; i <= round; i++) {
			String testLogin1 = testLogin();
			FkmjResult result = testAuto();
			String testLogin2 = testLogin();
			String filename = i + ".==========>\r\n";
			String content1 = "统计结果: " + result.toString() + "\r\n";
			String content2 = "正常游戏统计结果: " + map.toString()+ "\r\n";
			String content3 = "正常游戏掉落统计结果: " + map1.toString()+ "\r\n";
			String content4 = "免费游戏统计结果: " + freeMap.toString()+ "\r\n";
			String content5 = "免费游戏掉落统计结果: " + freeMap1.toString()+ "\r\n";
			String content6 = "免费游戏掉落加倍统计结果: " + dorpfreeMap2.toString()+ "\r\n";
			String content7 = "游戏开始金币: " + testLogin1.toString()+ "\r\n";
			String content8 = "游戏结束金币: " + testLogin2.toString()+ "\r\n";
			String output = filename + content1 + content2 + content3 + content4 + content5 + content6 + content7 + content8;
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
		dorptotalReward = 0;
		// 免费游戏触发次数
		freeCount = 0;
		// 免费游戏总次数
		totoalFreeCount = 0;
		// 免费游戏奖励
		freeGameReward = 0;
		dorpfreeGameReward = 0;
		dorpfreereward2 = 0;
		// 正常游戏中奖次数
		totalCount = 0;
		// 正常游戏次数
		gameCount = 0;
		// 正常游戏奖励信息(K：图标名称 V：K：中奖个数 V：中奖次数)
		map.clear();
		map1.clear();
		// 免费游戏奖励信息
		freeMap.clear();
		freeMap1.clear();
		dorpfreeMap2.clear();
		String result =  "[清除数据成功!当前数据 : ]"
				+ "totalReward = " + totalReward
				+ "dorptotalReward = " + dorptotalReward
				+ "dorpfreereward2 = " + dorpfreereward2
				+ ", freeCount = " + freeCount
				+ ", totoalFreeCount = " + totoalFreeCount
				+ ", freeGameReward = " + freeGameReward
				+ ", dorpfreeGameReward = " + dorpfreeGameReward
				+ ", totalCount = " + totalCount
				+ ", gameCount = " + gameCount
				+ ", map = " + map.size()
				+ ", map1 = " + map1.size()
				+ ", freeMap = " + freeMap.size()
				+ ", freeMap1 = " + freeMap1.size()
				+ ", dorpfreeMap2 = " + dorpfreeMap2.size()
				+"\r\n"
				;
		return result;
	}
	/**
	 * 自动测试
	 * @return 
	 * @throws InterruptedException 
	 */
	public  FkmjResult testAuto() throws InterruptedException{
		// 统计结果
		FkmjResult yzhxResult = new FkmjResult();
		// 开始测试
		int count = betCount;
		while (count != 0) {
				count --;
				//Thread.sleep(20L);
				FkmjGameBetResult result = testStartGame(line,lineBet);
				//System.out.println("正常游戏result: " + result);
				boolean bonus = result.isBonus();
				boolean drop = result.isDrop();
				FkmjBonusInfo bonusInfo = result.getBonusInfo();
				int bonusNum = result.getBonusNum();
				if (bonus) {
					freeCount ++ ;
					count += bonusNum;
					//System.out.println("获得的免费次数================" + bonusNum);
				}
				if (bonusInfo != null ) {
					freedataRecord(result);
					if (bonusNum == 0 && !drop) {
						Double rewardcoin = result.getRewardcoin();
						//System.out.println("本次免费的总奖励===============>" + rewardcoin);
						freeGameReward  += rewardcoin;
					}
				} else {
					gameCount ++;
					System.out.println("目前自动运行次数: " + gameCount);
					dataRecord(result);
				}
				if (drop) {
					while (true) {
						FkmjGameBetResult dropResult = testFreeGame1();
						//System.out.println("掉落游戏result: " + dropResult);
						boolean drop2 = dropResult.isDrop();
						int bonusNum2 = dropResult.getBonusNum();
						FkmjBonusInfo bonusInfo2 = dropResult.getBonusInfo();
						if (bonusInfo2 != null) {
							freedorpdataRecord(dropResult);
							if (bonusNum2 == 0 && !drop2) {
								double rewardcoin = dropResult.getRewardcoin();
								//System.out.println("本次免费的总奖励===============>" + rewardcoin);
								dorpfreeGameReward += rewardcoin;
							}
						} else {
							dorpdataRecord(dropResult);
						}
						if (!drop2) {
							break;
						}
					}
				}
		}
		yzhxResult.setBetCount(betCount);
		yzhxResult.setTotalBet(lineBet*line*betCount);
		yzhxResult.setTotalCount(totalCount);
		yzhxResult.setTotalreward(totalReward);
		yzhxResult.setDorptotalReward(dorptotalReward);
		yzhxResult.setFreeCount(freeCount);
		yzhxResult.setTotoalFreeCount(totoalFreeCount);
		yzhxResult.setFreeGameReward(freeGameReward);
		yzhxResult.setDorpfreereward2(dorpfreereward2);
		yzhxResult.setDorpfreeGameReward(dorpfreeGameReward);
		yzhxResult.setGameCount(gameCount);
		yzhxResult.setTotalIncomelv((dorptotalReward + totalReward + freeGameReward + dorpfreeGameReward)/(lineBet*line*betCount));
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
		int messageid = 2100;
		
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
		int messageid = 2101;
		
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
	public  FkmjGameBetResult testStartGame(int line,double lineBet){
		int messageid = 2102;
		
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
		FkmjGameBetResult result = (FkmjGameBetResult) mainProcess.mainProcessMsg(mc);
		return result;
	}
	
	/**
	 * 掉落
	 * @return 
	 */
	public  FkmjGameBetResult testFreeGame1(){
		int messageid = 2103;
		
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
		FkmjGameBetResult result = (FkmjGameBetResult) mainProcess.mainProcessMsg(mc);
		return result;
	}
	
	
	/**
	 * 正常游戏数据记录
	 * @param yzhxResult 
	 */
	public  void dataRecord(FkmjGameBetResult result){
		List<WinLineInfo> winrouteinfos = result.getWinrouteinfos();
		double rewardcoin = result.getRewardcoin();
		//System.out.println("本次奖励===============>" + rewardcoin);
		totalReward += rewardcoin;
		if (winrouteinfos.size() > 0) {
			totalCount += 1;
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
	
	/**
	 * 正常游戏掉落数据记录
	 * @param yzhxResult 
	 */
	public  void dorpdataRecord(FkmjGameBetResult dropResult){
		List<WinLineInfo> winrouteinfos = dropResult.getWinrouteinfos();
		double rewardcoin = dropResult.getRewardcoin();
		//System.out.println("本次奖励===============>" + rewardcoin);
		dorptotalReward += rewardcoin;
		if (winrouteinfos.size() > 0) {
			totalCount += 1;
			for (WinLineInfo wl : winrouteinfos) {
				//System.out.println("wl" + wl);
				String icon = wl.getIcon().getName();
				int num = wl.getNum();
				//System.out.println("icon" + icon);
				//System.out.println("num" + num);
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
		}
	}
	
	/**
	 * 免费游戏数据记录
	 * @param yzhxResult 
	 */
	public  void freedataRecord(FkmjGameBetResult result){
		List<WinLineInfo> winrouteinfos = result.getWinrouteinfos();
		double rewardcoin = result.getRewardcoin();
		//System.out.println("本次奖励===============>" + rewardcoin);
		if (winrouteinfos.size() > 0) {
			totalCount += 1;
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
	
	/**
	 * 免费游戏掉落数据记录
	 * @param yzhxResult 
	 */
	public  void freedorpdataRecord(FkmjGameBetResult dropResult){
		List<WinLineInfo> winrouteinfos = dropResult.getWinrouteinfos();
		double rewardcoin = dropResult.getRewardcoin();
		FkmjBonusInfo bonusInfo = dropResult.getBonusInfo();
		int count2 = bonusInfo.getCount();
		Map<String, Map<Integer, Integer>> dorpfreeMap1 = new HashMap<>();
		//System.out.println("本次奖励===============>" + rewardcoin);
		if (winrouteinfos.size() > 0) {
			totalCount += 1;
			for (WinLineInfo wl : winrouteinfos) {
				//System.out.println("wl" + wl);
				String icon = wl.getIcon().getName();
				int num = wl.getNum();
				//System.out.println("icon" + icon);
				//System.out.println("num" + num);
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
			}
		}
		
		if (count2 > 1) {
			dorpfreeMap2.put(count2 + "|" + RandomUtil.getRandom(10000, 99999), dorpfreeMap1);
			dorpfreereward2 += rewardcoin;
		}
	}
	
	
}
