package com.dmg.bcbm.logic.entity.bcbm;

/**
 * @author Administrator
 * 后台战绩显示枚举
 */
public enum RecordEnum {
	LBJN(1, "兰博基尼", 40, 0, 0, 0),
	FLL(3, "法拉利", 30, 0, 0, 0),
	BM(7, "宝马", 20, 0, 0, 0),
	BC(5, "奔驰", 10, 0, 0, 0),
	DZ(2, "大众", 5, 0, 0, 0),
	AD(6, "奥迪", 5, 0, 0, 0),
	FT(4, "丰田", 5, 0, 0, 0),
	BT(8, "本田", 5, 0, 0, 0);
	
	// 类型
	private int car;
	// 名称
	private String name;
	// 赔率
	private int lv;
	// 本局下注
	private double bet;
	// 本局输赢
	private double winLoseGold;
	// 是否为中奖车辆 0不是,1是
	private int winCar;
	
	private RecordEnum(int car, String name, int lv, double bet, double winLoseGold, int winCar) {
		this.car = car;
		this.name = name;
		this.lv = lv;
		this.bet = bet;
		this.winLoseGold = winLoseGold;
		this.winCar = winCar;
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
	public void setLv(int lv) {
		this.lv = lv;
	}
	public double getBet() {
		return bet;
	}
	public void setBet(double bet) {
		this.bet = bet;
	}
	public double getWinLoseGold() {
		return winLoseGold;
	}
	public void setWinLoseGold(double winLoseGold) {
		this.winLoseGold = winLoseGold;
	}
	public int getWinCar() {
		return winCar;
	}
	public void setWinCar(int winCar) {
		this.winCar = winCar;
	}
}
