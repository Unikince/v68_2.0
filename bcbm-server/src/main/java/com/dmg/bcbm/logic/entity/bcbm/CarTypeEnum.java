package com.dmg.bcbm.logic.entity.bcbm;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.dmg.bcbm.core.manager.RoomManager;
import com.dmg.gameconfigserverapi.dto.BairenControlConfigDTO;
import com.dmg.gameconfigserverapi.dto.BairenFileConfigDTO;
import com.dmg.gameconfigserverapi.dto.BairenGameConfigDTO;
import com.zyhy.common_server.util.RandomUtil;

public enum CarTypeEnum {
	LBJN(1, "兰博基尼", 40, 100, 11, 12, 13, 14),
	FLL(3, "法拉利", 30, 150, 31, 32, 33, 34),
	BM(7, "宝马", 20, 200, 71, 72, 73, 74),
	BC(5, "奔驰", 10, 250, 51, 52, 53, 54),
	DZ(2, "大众", 5, 500, 21, 22, 23, 24),
	AD(6, "奥迪", 5, 600, 61, 62, 63, 64),
	FT(4, "丰田", 5, 700, 41, 42, 43, 44),
	BT(8, "本田", 5, 800, 81, 82, 83, 84);
	
	// 类型
	private int car;
	// 名称
	private String name;
	// 赔率
	private int lv;
	// 权重
	private int weight;
	// 中奖位置
	private int[] local;
	
	private CarTypeEnum(int car, String name, int lv, int weight, int... local) {
		this.car = car;
		this.name = name;
		this.lv = lv;
		this.weight = weight;
		this.local = local;
	}
	public int getCar() {
		return car;
	}
	public String getName() {
		return name;
	}
	public int getLv() {
		return lv;
	}
	public int getWeight() {
		return weight;
	}
	public int[] getLocal() {
		return local;
	}
	
	/**
	 * 通过车辆id获取车辆
	 * @return 
	 */
	public static CarTypeEnum getCarbyId(int id){
		for (CarTypeEnum e : values()) {
			if (e.getCar() == id) {
				return e;
			}
		}
		return null;
	}
	
	
	/**
	 * 通过车辆id获取赔率
	 * @return 
	 */
	public static int getLvbyId(int roomId,int id){
		// 获取房间
		Room room = RoomManager.intance().getRoomById(roomId);
		BairenGameConfigDTO gameConfig = room.getGameConfig().get(1); // 获取游戏配置
		BairenControlConfigDTO controlConfig = gameConfig.getBairenControlConfigDTO(); // 控制配置
		JSONObject cardTypeMultiple = controlConfig.getCardTypeMultiple();
		int carMultiple = cardTypeMultiple.getIntValue(String.valueOf(id - 1));
		if (carMultiple == 0) {
			for (CarTypeEnum e : values()) {
				if (e.getCar() == id) {
					carMultiple = e.getLv();
				}
			}
		}
		return carMultiple;
	}
	
	/**
	 * 根据id获取胜利的图标
	 * @return
	 */
	public static BmbcWinInfo getWinInfoByCarId(int id){
		BmbcWinInfo bw = new BmbcWinInfo();
		CarTypeEnum win = getCarbyId(id);
		bw.setCar(win.getCar());
		bw.setName(win.getName());
		bw.setLv(win.getLv());
		int random = RandomUtil.getRandom(0, 3);
		bw.setLocal(win.getLocal()[random]);
		return bw;
	}
	
	/**
	 * 根据权重获取胜利的图标
	 * @return
	 */
	public static BmbcWinInfo getWinInfoByWeight(){
		BmbcWinInfo bw = new BmbcWinInfo();
		List<CarTypeEnum> all = new ArrayList<>();
		for(CarTypeEnum e : values()){
			all.add(e);
		}
		int total = 0;
		for(CarTypeEnum e : all){
			total += e.getWeight();
		}
		int num = RandomUtil.getRandom(0, total);
		int v = 0;
		CarTypeEnum win = null;
		for(CarTypeEnum e : all){
			int w = v + e.getWeight();
			if(num >= v && num < w){
				win = e;
				break;
			}else{
				v = w;
			}
		}
		bw.setCar(win.getCar());
		bw.setName(win.getName());
		bw.setLv(win.getLv());
		int random = RandomUtil.getRandom(0, 3);
		bw.setLocal(win.getLocal()[random]);
		return bw;
	}
}
