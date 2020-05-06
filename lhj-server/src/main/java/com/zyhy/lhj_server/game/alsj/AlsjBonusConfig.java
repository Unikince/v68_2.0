/**
 * 
 */
package com.zyhy.lhj_server.game.alsj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zyhy.common_server.util.RandomUtil;

/**
 * @author ASUS
 *
 */
public class AlsjBonusConfig {
	// 奖励次数的几率
	private int free_12 = 10;
	private int free_13 = 10;
	private int free_14 = 10;
	private int free_15 = 10;
	private int free_16 = 10;
	private int free_17 = 10;
	private int free_18 = 5;
	private int free_19 = 5;
	private int free_20 = 5;
	private int free_21 = 5;
	private int free_22 = 5;
	private int free_23 = 5;
	private int free_24 = 5;
	private int free_25 = 5;
	// 奖励倍数的几率
	private int freelv_2 = 2500;
	private int freelv_3 = 2000;
	private int freelv_4 = 1500;
	private int freelv_5 = 1200;
	private int freelv_6 = 1000;
	private int freelv_7 = 800;
	private int freelv_8 = 500;
	private int freelv_9 = 150;
	private int freelv_10 = 100;
	private int freelv_11 = 90;
	private int freelv_12 = 80;
	private int freelv_13 = 50;
	private int freelv_14 = 20;
	private int freelv_15 = 10;
	// 再次中免费奖励次数
	private final int reFreeNum = 20;
	
	/**
	 * 获取免费次数
	 * @return
	 */
	public int getNum(){
		Map<Integer, Integer> num = new HashMap<>();
		num.put(12, free_12);
		num.put(13, free_13);
		num.put(14, free_14);
		num.put(15, free_15);
		num.put(16, free_16);
		num.put(17, free_17);
		num.put(18, free_18);
		num.put(19, free_19);
		num.put(20, free_20);
		num.put(21, free_21);
		num.put(22, free_22);
		num.put(23, free_23);
		num.put(24, free_24);
		num.put(25, free_25);
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
		lv.put(7, freelv_7);
		lv.put(8, freelv_8);
		lv.put(9, freelv_9);
		lv.put(10, freelv_10);
		lv.put(11, freelv_11);
		lv.put(12, freelv_12);
		lv.put(13, freelv_13);
		lv.put(14, freelv_14);
		lv.put(15, freelv_15);
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
