/**
 * 
 */
package com.zyhy.lhj_server.game.yhdd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zyhy.common_server.util.RandomUtil;

/**
 * @author ASUS
 *
 */
public class YhddBonusConfig {
	// 奖励次数的几率
	private int free_8 = 4000;
	private int free_9 = 3000;
	private int free_10 = 2000;
	private int free_11 = 600;
	private int free_12 = 290;
	private int free_13 = 102;
	private int free_14 = 5;
	private int free_15 = 2;
	private int free_16 = 1;
	// 奖励倍数的几率
	private int freelv_2 = 30;
	private int freelv_3 = 25;
	private int freelv_4 = 20;
	private int freelv_5 = 15;
	private int freelv_6 = 10;
	// 再次中免费奖励次数
	private final int reFreeNum = 20;
	
	/**
	 * 获取免费次数
	 * @return
	 */
	public int getNum(){
		Map<Integer, Integer> num = new HashMap<>();
		num.put(8, free_8);
		num.put(9, free_9);
		num.put(10, free_10);
		num.put(11, free_11);
		num.put(12, free_12);
		num.put(13, free_13);
		num.put(14, free_14);
		num.put(15, free_15);
		num.put(16, free_16);
		List<Integer> nums = new ArrayList<>();
		for (Integer free : num.keySet()) {
			for (int i = 0; i < num.get(free); i++) {
				nums.add(free);
			}
		}
		int random = RandomUtil.getRandom(0, (nums.size()-1));
		return nums.get(random);
	}
	/**
	 * 获取倍率
	 * @return
	 */
	public int getLv(){
		Map<Integer, Integer> lv = new HashMap<>();
		lv.put(2, freelv_2);
		lv.put(3, freelv_3);
		lv.put(4, freelv_4);
		lv.put(5, freelv_5);
		lv.put(6, freelv_6);
		List<Integer> lvs = new ArrayList<>();
		for (Integer freelv : lv.keySet()) {
			for (int i = 0; i < lv.get(freelv); i++) {
				lvs.add(freelv);
			}
		}
		int random = RandomUtil.getRandom(0, (lvs.size()-1));
		return lvs.get(random);
	}
	
	public int getReFreeNum() {
		return reFreeNum;
	}
	
}
