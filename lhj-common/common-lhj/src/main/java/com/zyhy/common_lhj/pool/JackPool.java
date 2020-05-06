/**
 * 
 */
package com.zyhy.common_lhj.pool;

import com.alibaba.fastjson.annotation.JSONField;
import com.zyhy.common_server.util.RandomUtil;

/**
 * @author ASUS
 *
 */
public class JackPool {
	// 订单编号
	private int orderNum = 100000000;
	// 奖池当前水位
	private double currentnum = 1000000;
	// 奖池当前水位杀率
	private int currentlv = 0;
	// 奖池溢出的水量
	@JSONField(serialize = false)
	private transient double alloverflownum;
	// 奖池高警戒水位
	private int highwarringnum = 1500000;
	// 高警戒水位预计算概率
	//private int highwarringlv = 50;
	private int highwarringlv = 10;
	// 奖池溢出水位
	private int overflownum = 2000000;
	// 溢出水位预计算概率
	private int overflowlv = 80;
	// 奖池低警戒水位
	private int lowwarringnum = 500000;
	// 低警戒水位预计算概率
	//private int lowwarringlv = 50;
	private int lowwarringlv = 90;
	// 奖池通杀水位
	private int killallnum = 200000;
	// 通杀水位预计算概率
	private int killalllv = 100;
	// 奖池开关状态0=开,1=关
	private int openstatus = 0;
	// 0=正常游戏,1=放水游戏,2=通杀游戏
	@JSONField(serialize = false)
	private transient Integer status;
	
	public static final int STATUS_KILL = 2;
	
	public static final int STATUS_WIN = 1;
	
	public static final int STATUS_NORMAL = 0;
	
	/**
	 * @return 0=正常游戏,1=放水游戏,2=通杀游戏
	 */
	private int checkKillStatus(){
		if (openstatus == 1) {
			return 0;
		}
		int randomnum = RandomUtil.getRandom(0, 100);
		double num = currentnum;
		if (num >= overflownum) {
			// 水位溢出,判断是否放水
			if (randomnum < overflowlv) {
				return 1;
			}
		}else if (num < overflownum && num >= highwarringnum) {
			// 高警戒水位,判断是否放水
			if (randomnum < highwarringlv) {
				return 1;
			}
		}else if (num < highwarringnum && num >= lowwarringnum) {
			// 正常水位
			if (randomnum < currentlv) {
				return 2;
			}
		}else if (num < lowwarringnum && num >= killallnum) {
			// 低警戒水位,判断是否通杀
			if (randomnum < lowwarringlv) {
				return 2;
			}
		}else {
			// 通杀水位,判断是否通杀
			if (randomnum < killalllv) {
				return 2;
			}
		}
		return 0;
	}

	/**
	 * 0=正常游戏,1=放水游戏,2=通杀游戏
	 * @return
	 */
	
	public Integer getStatus() {
		if(status == null){
			status = checkKillStatus();
		}
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public int getOpenstatus() {
		return openstatus;
	}
	public void setOpenstatus(int openstatus) {
		this.openstatus = openstatus;
	}
	public double getAlloverflownum() {
		if(currentnum > overflownum){
			return currentnum - overflownum;
		}
		return 0;
	}
	public void setAlloverflownum(double alloverflownum) {
		this.alloverflownum = alloverflownum;
	}
	public int getHighwarringlv() {
		return highwarringlv;
	}
	public void setHighwarringlv(int highwarringlv) {
		this.highwarringlv = highwarringlv;
	}
	public int getOverflowlv() {
		return overflowlv;
	}
	public void setOverflowlv(int overflowlv) {
		this.overflowlv = overflowlv;
	}
	public int getLowwarringlv() {
		return lowwarringlv;
	}
	public void setLowwarringlv(int lowwarringlv) {
		this.lowwarringlv = lowwarringlv;
	}
	public int getKillalllv() {
		return killalllv;
	}
	public void setKillalllv(int killalllv) {
		this.killalllv = killalllv;
	}
	public double getCurrentnum() {
		return currentnum;
	}
	public void setCurrentnum(double currentnum) {
		this.currentnum = currentnum;
	}
	public int getHighwarringnum() {
		return highwarringnum;
	}
	public void setHighwarringnum(int highwarringnum) {
		this.highwarringnum = highwarringnum;
	}
	public int getOverflownum() {
		return overflownum;
	}
	public void setOverflownum(int overflownum) {
		this.overflownum = overflownum;
	}
	public int getLowwarringnum() {
		return lowwarringnum;
	}
	public void setLowwarringnum(int lowwarringnum) {
		this.lowwarringnum = lowwarringnum;
	}
	public int getKillallnum() {
		return killallnum;
	}
	public void setKillallnum(int killallnum) {
		this.killallnum = killallnum;
	}
	
	public int getCurrentlv() {
		return currentlv;
	}
	public void setCurrentlv(int currentlv) {
		this.currentlv = currentlv;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	@Override
	public String toString() {
		return "JackPool [orderNum=" + orderNum + ", currentnum=" + currentnum + ", currentlv=" + currentlv
				+ ", highwarringnum=" + highwarringnum + ", highwarringlv=" + highwarringlv + ", overflownum="
				+ overflownum + ", overflowlv=" + overflowlv + ", lowwarringnum=" + lowwarringnum + ", lowwarringlv="
				+ lowwarringlv + ", killallnum=" + killallnum + ", killalllv=" + killalllv + ", openstatus="
				+ openstatus + "]";
	}


	
}
