package com.zyhy.lhj_server.game.xywjs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.zyhy.common_server.util.RandomUtil;

/**
 * lucky的概率
 * @author Administrator
 */
public enum LuckyRateEnum {
	//分别对应1-7套基础概率,第8套为爆奖模式概率
	/*LUCKY1(1, "RANDOM1", 1000, 1000, 1000, 1000, 1000, 1000, 1500, 0), // 随机1
	LUCKY2(2, "RANDOM2", 1500, 1500, 1500, 1500, 1500, 1500, 1500, 0), // 随机2
	LUCKY3(3, "RANDOM3", 1000, 1000, 1000, 1000, 1500, 1500, 1500, 500), // 随机3
	LUCKY4(4, "大四喜", 1500, 1500, 1500, 1500, 1000, 1500, 1500, 0),
	LUCKY5(5, "小三元", 1000, 1000, 1000, 1000, 1000, 1200, 1200, 2000),
	LUCKY6(6, "大三元", 1000, 1000, 1000, 1000, 1000, 1000, 1200, 2000),
	LUCKY7(7, "开火车", 0, 0, 0, 0, 1000, 1000, 1200, 1000),
	LUCKY8(8, "天龙八部", 0, 0, 0, 0, 0, 100, 100, 1000), // 随机8
	LUCKY9(9, "九莲宝灯", 0, 0, 0, 0, 0, 100, 100, 1000), // 随机9
	LUCKY10(10, "仙女散花", 0, 0, 0, 0, 0, 100, 100, 1000), // 随机6
	LUCKY11(11, "纵横四海", 0, 0, 0, 0, 0, 0, 100, 1000),
	LUCKY12(12, "大满贯", 0, 0, 0, 0, 0, 0, 0, 500),
	LUCKY13(13, "LOSE", 3000, 3000,3000, 3000, 2000, 1000, 0, 0),*/
	
	LUCKY1(1, "RANDOM1", 8000, 8000, 8000, 3000, 3000, 3000, 2000, 0), // 随机1
	LUCKY2(2, "RANDOM2", 2000, 2000, 2000, 1500, 1500, 1500, 2000, 0), // 随机2
	LUCKY3(3, "RANDOM3", 0, 0, 0, 1500, 1500, 1500, 2000, 1000), // 随机3
	LUCKY4(4, "大四喜", 0, 0, 0, 2000, 2000, 2000, 2000, 500),
	LUCKY5(5, "小三元", 0, 0, 0, 1000, 1000, 1000, 1000, 1500),
	LUCKY6(6, "大三元", 0, 0, 0, 1000, 1000, 1000, 1000, 1500),
	LUCKY7(7, "开火车", 0, 0, 0, 0, 0, 0, 0, 1000),
	LUCKY8(8, "天龙八部", 0, 0, 0, 0, 0, 0, 0, 1000), // 随机8
	LUCKY9(9, "九莲宝灯", 0, 0, 0, 0, 0, 0, 0, 1000), // 随机9
	LUCKY10(10, "仙女散花", 0, 0, 0, 0, 0, 0, 0, 1000), // 随机6
	LUCKY11(11, "纵横四海", 0, 0, 0, 0, 0, 0, 0, 1000),
	LUCKY12(12, "大满贯", 0, 0, 0, 0, 0, 0, 0, 500),
	LUCKY13(13, "LOSE", 0, 0,0, 0, 0, 0, 0, 0),
	
	;
	
	// lucky奖励id
	private int id;
	// 名字
	private String name;
	// 中奖概率
	private int[] odds;
	
	private LuckyRateEnum(int id, String name, int ... odds) {
		this.id = id;
		this.name = name;
		this.odds = odds;
	}

	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public int[] getOdds() {
		return odds;
	}

	/**
	 * 获取中奖的lucky
	 * @param type: 使用第几套lucky概率
	 * @return
	 */
	public static LuckyRateEnum getLucky(int type){
		if (type == 0) {
			type = 4;
		}
		// 爆奖模式使用第8套概率
		if (type == 10) {
			type = 8;
		}
		List<LuckyRateEnum> list = new ArrayList<>();
		int total = 0;
		for (LuckyRateEnum e : values()) {
			total += e.getOdds()[type - 1];
			list.add(e);
		}
		int num = RandomUtil.getRandom(1, total);
		int v = 0;
		LuckyRateEnum le = null;
		Iterator<LuckyRateEnum> iterator = list.iterator();
		while (iterator.hasNext()) {
			LuckyRateEnum next = iterator.next();
			int w = v + next.getOdds()[type - 1];
			if (num >= v && num < w) {
				le = next;
				break;
			} else {
				v = w;
			}
		}
		
		if (le == null) {
			return getLucky(type);
		}
		return le;
	}
	
}
