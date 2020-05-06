/**
 * 
 */
package com.zyhy.lhj_server.service.xywjs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import com.zyhy.common_lhj.logic.BaseLogic;
import com.zyhy.common_server.util.RandomUtil;
import com.zyhy.lhj_server.game.xywjs.IconEnum;
import com.zyhy.lhj_server.game.xywjs.LuckyRateEnum;
import com.zyhy.lhj_server.game.xywjs.smallRewardRateEnum;
import com.zyhy.lhj_server.game.xywjs.poi.template.FruitOdds;
import com.zyhy.lhj_server.prcess.result.xywjs.XywjsBeginGameResult;
import com.zyhy.lhj_server.prcess.result.xywjs.XywjsLuckyGameResult;

@Service
public class XywjsGameService extends BaseLogic {
	
	/**
	 * 执行水果机逻辑
	 * @param baseinfos 图标信息
	 * @param betMap 下注信息
	 * @return
	 */
	public XywjsBeginGameResult computerGameProcess(List<FruitOdds> baseinfos, Map<Integer, Double> betMap) {
		XywjsBeginGameResult result = new XywjsBeginGameResult();
		// 获取中奖图标
		FruitOdds icon = getIcon(baseinfos);
		
		//TODO 测试用code
		/*for (FruitOdds fruitOdds : baseinfos) {
			if (fruitOdds.getSign() == 9) {
				icon = fruitOdds;
			}
		}*/
		
		// 中lucky奖励
		if (icon.getSign() == 9 || icon.getSign() == 21) {
			LuckyRateEnum lucky = LuckyRateEnum.getLucky(icon.getType());
			
			//TODO 测试用code
			//LuckyRateEnum lucky = LuckyRateEnum.getLucky(10); // 测试用概率
			//LuckyRateEnum lucky = LuckyRateEnum.LUCKY7;  //开火车
			
			List<XywjsLuckyGameResult> luckyResultList = luckyGameProcess(lucky);
			result.setLuckyModel(lucky.getId());
			result.setLuckyresults(luckyResultList);
			result.setZhuanpanid(icon.getSign());
		} else {
			// 普通游戏
			result.setZhuanpanid(icon.getSign());
		}
		
		// 普通游戏结算
		if (result.getZhuanpanid() > 0 && result.getZhuanpanid() != 9 && result.getZhuanpanid() != 21) {
			double reward = getReward(result.getZhuanpanid(), baseinfos, betMap);
			result.setWinnumber(reward);
		} else { // lucky结果结算
			double totalReward = 0;
			if (result.getLuckyModel() != LuckyRateEnum.LUCKY13.getId()) {
				for (XywjsLuckyGameResult luckyResult : result.getLuckyresults()) {
					double reward = getReward(luckyResult.getZhuanpanid(), baseinfos, betMap);
					totalReward += reward;
				}
			}
			result.setWinnumber(totalReward);
		}
		
		return result;
	}
	
	/**
	 * 结算奖励
	 * @param id
	 * @param baseinfos
	 * @param betMap
	 * @return
	 */
	private double getReward(int id, List<FruitOdds> baseinfos, Map<Integer, Double> betMap){
		FruitOdds icon = null;
		double reward = 0;
		for (FruitOdds odds : baseinfos) {
			if (odds.getSign() == id) {
				icon = odds;
			}
		}
		if (icon != null) {
			for (Integer betId : betMap.keySet()) {
				if (icon.getBet() == betId) {
					reward = icon.getMultiple() * betMap.get(betId);
				}
			}
		}
		return reward;
	}
	
	/**
	 * 获取中奖图标
	 * @param baseinfos
	 * @return
	 */
	private FruitOdds getIcon(List<FruitOdds> baseinfos){
		int total = 0;
		for (FruitOdds odds : baseinfos) {
			total += odds.getOdds();
		}
		int num = RandomUtil.getRandom(1, total);
		int v = 0;
		FruitOdds fo = null;
		Iterator<FruitOdds> iterator = baseinfos.iterator();
		while (iterator.hasNext()) {
			FruitOdds next = iterator.next();
			int w = v + next.getOdds();
			if (num >= v && num < w) {
				fo = next;
				break;
			} else {
				v = w;
			}
		}
		
		if (fo == null) {
			return getIcon(baseinfos);
		}
		return fo;
	}
	
	/**
	 * 小奖模式
	 * @param betMap 
	 * @param baseinfos 
	 * @return 
	 */
	public XywjsBeginGameResult smallRewardGameProcess(List<FruitOdds> baseinfos, Map<Integer, Double> betMap){
		XywjsBeginGameResult result = new XywjsBeginGameResult();
		// 获取图标赔率
		Map<Integer, Integer> oddsMap = new HashMap<>(); // 图标赔率
		baseinfos.forEach(fo -> oddsMap.put(fo.getSign(), fo.getMultiple()));
		// 玩家下注的图标
		List<Integer> betIcon = this.getBetIcon(betMap);
		// 玩家未下注的图标及赔率
		Map<Integer, Integer> noBetIcon = getNoBetIcon(oddsMap, betIcon);
		List<Entry<Integer, Integer>> noBetIconList = this.mapSort(noBetIcon);
		// 获取小奖模式id
		int smallRewardId = smallRewardRateEnum.getSmallRewardModel(1).getId(); 
		if (noBetIconList.isEmpty()) smallRewardId = 8;
		switch (smallRewardId) {
		case 1: // 随机(不中奖)
			int random1 = RandomUtil.getRandom(1, 100);
			if (random1 <= 50) { // 展示最高倍率
				result.setZhuanpanid(noBetIconList.get(0).getKey());
			} else { // 随机展示一个
				result.setZhuanpanid(noBetIconList.get(RandomUtil.getRandom(0, noBetIconList.size() - 1)).getKey());
			}
			break;
		case 2: // lucky模式1:随机1
			int random2 = RandomUtil.getRandom(1, 100);
			if (random2 <= 50) { // 展示最高倍率
				result.setZhuanpanid(noBetIconList.get(0).getKey());
			} else { // lucky模式1:随机1
				List<XywjsLuckyGameResult> luckyResultList = new ArrayList<>();
				XywjsLuckyGameResult luckyResult = new XywjsLuckyGameResult();
				luckyResult.setZhuanpanid(noBetIconList.get(RandomUtil.getRandom(0, noBetIconList.size() - 1)).getKey());
				luckyResultList.add(luckyResult);
				result.setLuckyModel(LuckyRateEnum.LUCKY1.getId());
				result.setLuckyresults(luckyResultList);
				if (RandomUtil.getRandom(1, 100) % 2 == 0) {
					result.setZhuanpanid(9);
				} else {
					result.setZhuanpanid(21);
				}
			}
			break;
		case 3: // lucky模式2:随机2
			int random3 = RandomUtil.getRandom(1, 100);
			if (random3 <= 50) { // 展示最高倍率
				result.setZhuanpanid(noBetIconList.get(0).getKey());
			} else { // lucky模式2:随机2
				List<XywjsLuckyGameResult> luckyResultList = new ArrayList<>();
				for (int i = 0; i < 2; i++) {
					XywjsLuckyGameResult luckyResult = new XywjsLuckyGameResult();
					if (i == 1) {
						int id = 0;
						for (Entry<Integer, Integer> entry : noBetIconList) {
							if (entry.getValue() == 3) {
								id = entry.getKey();
								break;
							}
						}
						if (id == 0) {
							for (Integer icon : oddsMap.keySet()) {
								if (oddsMap.get(icon) == 3) {
									id = oddsMap.get(icon);
								}
							}
						}
						luckyResult.setZhuanpanid(id);
					} else { 
						luckyResult.setZhuanpanid(noBetIconList.get(RandomUtil.getRandom(0, noBetIconList.size() - 1)).getKey());
					}
					luckyResultList.add(luckyResult);
				}
				result.setLuckyModel(LuckyRateEnum.LUCKY2.getId());
				result.setLuckyresults(luckyResultList);
				if (RandomUtil.getRandom(1, 100) % 2 == 0) {
					result.setZhuanpanid(9);
				} else {
					result.setZhuanpanid(21);
				}
			}
			break;
		case 4: // lucky模式3:随机3
			int random4 = RandomUtil.getRandom(1, 100);
			if (random4 <= 50) { // 展示最高倍率
				result.setZhuanpanid(noBetIconList.get(0).getKey());
			} else { // lucky模式3:随机3
				List<XywjsLuckyGameResult> luckyResultList = new ArrayList<>();
				for (int i = 0; i < 3; i++) {
					XywjsLuckyGameResult luckyResult = new XywjsLuckyGameResult();
					if (i == 0) {
						luckyResult.setZhuanpanid(noBetIconList.get(RandomUtil.getRandom(0, noBetIconList.size() - 1)).getKey());
					} else { 
						int id = 0;
						Iterator<Entry<Integer, Integer>> iterator = noBetIconList.iterator();
						while (iterator.hasNext()) {
							Entry<Integer, Integer> next = iterator.next();
							if (next.getValue() == 3) {
								id = next.getKey();
								iterator.remove();
								break;
							}
						}
						
						if (id == 0) {
							for (Integer icon : oddsMap.keySet()) {
								if (oddsMap.get(icon) == 3) {
									id = oddsMap.get(icon);
								}
							}
						}
						luckyResult.setZhuanpanid(id);
					}
					luckyResultList.add(luckyResult);
				}
				result.setLuckyModel(LuckyRateEnum.LUCKY3.getId());
				result.setLuckyresults(luckyResultList);
				if (RandomUtil.getRandom(1, 100) % 2 == 0) {
					result.setZhuanpanid(9);
				} else {
					result.setZhuanpanid(21);
				}
			}
			break;
		case 5: // 大四喜
			if (betMap.containsKey(IconEnum.ICON8.getId())) {
				List<XywjsLuckyGameResult> luckyResultList = new ArrayList<>();
				result.setLuckyModel(LuckyRateEnum.LUCKY13.getId());
				result.setLuckyresults(luckyResultList);
			} else {
				List<XywjsLuckyGameResult> luckyResultList = new ArrayList<>();
				int[] fruit2 = {4,10,16,22};
				for (int i = 0; i < fruit2.length; i++) {
					XywjsLuckyGameResult luckyResult = new XywjsLuckyGameResult();
					luckyResult.setZhuanpanid(fruit2[i]);
					luckyResultList.add(luckyResult);
				}
				result.setLuckyModel(LuckyRateEnum.LUCKY4.getId());
				result.setLuckyresults(luckyResultList);
			}
			if (RandomUtil.getRandom(1, 100) % 2 == 0) {
				result.setZhuanpanid(9);
			} else {
				result.setZhuanpanid(21);
			}
			break;
		case 6: // 小三元
			if ((betMap.containsKey(IconEnum.ICON5.getId()) && betMap.containsKey(IconEnum.ICON6.getId())) 
					|| (betMap.containsKey(IconEnum.ICON6.getId()) && betMap.containsKey(IconEnum.ICON7.getId()))
					|| (betMap.containsKey(IconEnum.ICON5.getId()) && betMap.containsKey(IconEnum.ICON7.getId()))) {
				List<XywjsLuckyGameResult> luckyResultList = new ArrayList<>();
				result.setLuckyModel(LuckyRateEnum.LUCKY13.getId());
				result.setLuckyresults(luckyResultList);
			} else {
				List<XywjsLuckyGameResult> luckyResultList = new ArrayList<>();
				int[] fruit3 = {1,6,12};
				for (int i = 0; i < fruit3.length; i++) {
					XywjsLuckyGameResult luckyResult = new XywjsLuckyGameResult();
					luckyResult.setZhuanpanid(fruit3[i]);
					luckyResultList.add(luckyResult);
				}
				result.setLuckyModel(LuckyRateEnum.LUCKY5.getId());
				result.setLuckyresults(luckyResultList);
			}
			if (RandomUtil.getRandom(1, 100) % 2 == 0) {
				result.setZhuanpanid(9);
			} else {
				result.setZhuanpanid(21);
			}
			break;
		case 7: // 大三元
			if (betMap.containsKey(IconEnum.ICON2.getId())
					|| betMap.containsKey(IconEnum.ICON3.getId())) {
				List<XywjsLuckyGameResult> luckyResultList = new ArrayList<>();
				result.setLuckyModel(LuckyRateEnum.LUCKY13.getId());
				result.setLuckyresults(luckyResultList);
			} else {
				List<XywjsLuckyGameResult> luckyResultList = new ArrayList<>();
				int[] fruit4 = {7,15,19};
				for (int i = 0; i < fruit4.length; i++) {
					XywjsLuckyGameResult luckyResult = new XywjsLuckyGameResult();
					luckyResult.setZhuanpanid(fruit4[i]);
					luckyResultList.add(luckyResult);
				}
				result.setLuckyModel(LuckyRateEnum.LUCKY6.getId());
				result.setLuckyresults(luckyResultList);
			}
			if (RandomUtil.getRandom(1, 100) % 2 == 0) {
				result.setZhuanpanid(9);
			} else {
				result.setZhuanpanid(21);
			}
			break;
		case 8: // LOSE
			result = getLuckyLoseModel();
			break;
		default:
			break;
		}
		
		// 普通游戏结算
		if (result.getZhuanpanid() > 0 && result.getZhuanpanid() != 9 && result.getZhuanpanid() != 21) {
			double reward = getReward(result.getZhuanpanid(), baseinfos, betMap);
			result.setWinnumber(reward);
		} else { // lucky结果结算
			double totalReward = 0;
			if (result.getLuckyModel() != LuckyRateEnum.LUCKY13.getId()) {
				for (XywjsLuckyGameResult luckyResult : result.getLuckyresults()) {
					double reward = getReward(luckyResult.getZhuanpanid(), baseinfos, betMap);
					totalReward += reward;
				}
			}
			result.setWinnumber(totalReward);
		}
		return result;
	}
	
	/**
	 * 执行lucky的游戏逻辑
	 * @param lucky
	 * @param betMap 
	 * @return
	 */
	private List<XywjsLuckyGameResult> luckyGameProcess(LuckyRateEnum lucky) {
		int luckyId = lucky.getId();
		List<XywjsLuckyGameResult> luckyResult = new ArrayList<>();
		switch (luckyId) {
		case 1: // 随机1
		case 2: // 随机2
		case 3: // 随机3
		case 8: // 天龙八部 (随机8)
		case 9: // 九莲宝灯 (随机9)
		case 10: // 仙女散花 (随机6)
			if (luckyId == 10) {
				luckyId = 6;
			}
			int[] fruit1 = {1,2,3,4,5,6,7,8,10,11,12,13,14,15,16,17,18,19,20,22,23,24};
			List<Integer> temp1 = new ArrayList<>();
			for (Integer integer : fruit1) {
				temp1.add(integer);
			}
			for (int i = 0; i < luckyId; i++) {
				XywjsLuckyGameResult result = new XywjsLuckyGameResult();
				result.setZhuanpanid(temp1.remove(RandomUtil.getRandom(0, temp1.size() - 1)));
				luckyResult.add(result);
			}
			break;
		case 4: // 大四喜
			int[] fruit2 = {4,10,16,22};
			for (int i = 0; i < fruit2.length; i++) {
				XywjsLuckyGameResult result = new XywjsLuckyGameResult();
				result.setZhuanpanid(fruit2[i]);
				luckyResult.add(result);
			}
			break;
		case 5: // 小三元
			int[] fruit3 = {1,6,12};
			for (int i = 0; i < fruit3.length; i++) {
				XywjsLuckyGameResult result = new XywjsLuckyGameResult();
				result.setZhuanpanid(fruit3[i]);
				luckyResult.add(result);
			}
			break;
		case 6: // 大三元
			int[] fruit4 = {7,15,19};
			for (int i = 0; i < fruit4.length; i++) {
				XywjsLuckyGameResult result = new XywjsLuckyGameResult();
				result.setZhuanpanid(fruit4[i]);
				luckyResult.add(result);
			}
			break;
		case 7: // 开火车
			int[] fruit5 = {22,23,24,1,2,3,4,5,6,7,8};
			int[] fruit6 = {10,11,12,13,14,15,16,17,18,19,20};
			int random1 = 6;
			int random2 = RandomUtil.getRandom(0, 1);
			if (random2 > 0) {
				int index = RandomUtil.getRandom(0, fruit5.length - random1);
				for (int i = index; i < index + random1; i++) {
					XywjsLuckyGameResult result = new XywjsLuckyGameResult();
					result.setZhuanpanid(fruit5[i]);
					luckyResult.add(result);
				}
			} else {
				int index = RandomUtil.getRandom(0, fruit6.length - random1);
				for (int i = index; i < index + random1; i++) {
					XywjsLuckyGameResult result = new XywjsLuckyGameResult();
					result.setZhuanpanid(fruit6[i]);
					luckyResult.add(result);
				}
			}
			break;
		case 11: // 纵横四海
			List<Integer[]> list = new ArrayList<>();
			Integer[] fruit7 = {24,1,2,3,4,5,6};
			Integer[] fruit8 = {6,7,8,9,10,11,12};
			Integer[] fruit9 = {12,13,14,15,16,17,18};
			Integer[] fruit10 = {18,19,20,21,22,23,24};
			list.add(fruit7);
			list.add(fruit8);
			list.add(fruit9);
			list.add(fruit10);
			Integer[] fruit11 = list.get(RandomUtil.getRandom(0, list.size() - 1));
			for (Integer integer : fruit11) {
				XywjsLuckyGameResult result = new XywjsLuckyGameResult();
				result.setZhuanpanid(integer);
				luckyResult.add(result);
			}
			break;
		case 12: // 大满贯
			int[] fruit12 = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24};
			for (Integer integer : fruit12) {
				XywjsLuckyGameResult result = new XywjsLuckyGameResult();
				result.setZhuanpanid(integer);
				luckyResult.add(result);
			}
			break;
		case 13: // LOSE
			break;	
		default:
			break;
		}
		if (luckyId != 7) {
			Collections.shuffle(luckyResult);
		}
		return luckyResult;
	}
	
	/**
	 * 将map的value值从大到小排序
	 */
	private List<Entry<Integer, Integer>> mapSort(Map<Integer, Integer> map){
		// 排序
		List<Map.Entry<Integer, Integer>> sortMap = new ArrayList<>(map.entrySet());
		Collections.sort(sortMap, new Comparator<Map.Entry<Integer, Integer>>() {//根据value排序
			@Override
			public int compare(Map.Entry<Integer, Integer> o1,
					Map.Entry<Integer, Integer> o2) {
				double result = o2.getValue() - o1.getValue();
				if(result > 0)
					return 1;
				else if(result == 0)
					return 0;
				else 
					return -1;
			}
		});
		return sortMap;
	}
	
	/**
	 * 获取下注的图标
	 * @return 
	 */
	private List<Integer> getBetIcon(Map<Integer, Double> betMap){
		List<Integer> icons = new ArrayList<>();
		for (Integer id : betMap.keySet()) {
			int[] iconsById = IconEnum.getIconsById(id);
			for (int icon : iconsById) {
				icons.add(icon);
			}
		}
		return icons;
	}
	
	/**
	 * 获取未下注的图标及赔率
	 * @return 
	 */
	private Map<Integer, Integer> getNoBetIcon(Map<Integer, Integer> oddsMap,List<Integer> betIcon){
		Map<Integer, Integer> noBetIcon = new HashMap<>();
		for (Integer icon : oddsMap.keySet()) {
			if (betIcon.contains(icon) || oddsMap.get(icon) == 0) continue;
			noBetIcon.put(icon, oddsMap.get(icon));
		}
		return noBetIcon;
	}
	
	/**
	 *  设定为lucky中的lose模式
	 * @return 
	 */
	public XywjsBeginGameResult getLuckyLoseModel(){
		XywjsBeginGameResult result = new XywjsBeginGameResult();
		result.setLuckyModel(LuckyRateEnum.LUCKY13.getId());
		result.setLuckyresults(new ArrayList<>());
		if (RandomUtil.getRandom(1, 100) % 2 == 0) {
			result.setZhuanpanid(9);
		} else {
			result.setZhuanpanid(21);
		}
		result.setWinnumber(0);
		return result;
	}
	
}
