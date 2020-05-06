package com.zyhy.lhj_server.game.xywjs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.zyhy.common_server.util.RandomUtil;

/**
 * 小奖模式的概率
 * @author Administrator
 */
public enum smallRewardRateEnum {
	SMALL1(1, "RANDOM", 20), // 随机
	SMALL2(2, "RANDOM1", 10), // 随机1
	SMALL3(3, "RANDOM2", 10), // 随机2
	SMALL4(4, "RANDOM3", 10), // 随机3
	SMALL5(5, "大四喜", 10),
	SMALL6(6, "小三元", 10),
	SMALL7(7, "大三元", 10),
	SMALL8(8, "LOSE", 20),
	;
	
	// 小奖奖励id
	private int id;
	// 名字
	private String name;
	// 中奖概率
	private int[] odds;
	
	private smallRewardRateEnum(int id, String name, int ... odds) {
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
	 * 获取小奖模式
	 * @param type: 使用第几套smallReward概率
	 * @return
	 */
	public static smallRewardRateEnum getSmallRewardModel(int type){
		if (type == 0) {
			type = 1;
		}
		List<smallRewardRateEnum> list = new ArrayList<>();
		int total = 0;
		for (smallRewardRateEnum e : values()) {
			total += e.getOdds()[type - 1];
			list.add(e);
		}
		int num = RandomUtil.getRandom(1, total);
		int v = 0;
		smallRewardRateEnum le = null;
		Iterator<smallRewardRateEnum> iterator = list.iterator();
		while (iterator.hasNext()) {
			smallRewardRateEnum next = iterator.next();
			int w = v + next.getOdds()[type - 1];
			if (num >= v && num < w) {
				le = next;
				break;
			} else {
				v = w;
			}
		}
		
		if (le == null) {
			return getSmallRewardModel(type);
		}
		return le;
	}
	
}
