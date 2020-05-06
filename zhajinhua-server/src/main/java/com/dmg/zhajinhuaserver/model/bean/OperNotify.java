package com.dmg.zhajinhuaserver.model.bean;

import com.dmg.zhajinhuaserver.config.D;
import com.dmg.zhajinhuaserver.config.Config;

import java.util.*;


/**
 * 通知玩家吃碰杠胡过
 * 
 * @author Administrator
 *
 */
public class OperNotify {
	public int time;
	/** 玩家位置 */
	public int seatId;
	/** 可以操作的玩家 */
	public Set<Integer> beSeatIdList = new HashSet<>();
	/**下注限制*/
	public int wagerLimit;
	/** 是否能看牌 */
	public boolean canSeeCard;
	/** 是否能比牌 */
	public boolean canCompare;
	/** 是否能弃牌 */
	public boolean canDisCard;
	/** 是否能加注 */
	public boolean canAddChips;
	/** 是否能跟注 */
	public boolean canFollowChips;

	@SuppressWarnings("rawtypes")
	public Map toMap(Seat seat,boolean bool) {
		Map<String, Object> ret = new HashMap<>();
		List<Integer> operList=new ArrayList<>();
		int operInfo = 0;
		if (canSeeCard){
			operInfo = Config.PokerOperState.SEECARDS;
			operList.add(operInfo);	
		}
		if (canCompare){
			operInfo = Config.PokerOperState.COMPARECARD;
			operList.add(operInfo);	
		}
		if (canDisCard){
			operInfo = Config.PokerOperState.DISCARD;
			operList.add(operInfo);	
		}
		if (canAddChips){
			operInfo = Config.PokerOperState.ADDCHIPS;
			operList.add(operInfo);
		}
		if (canFollowChips){
			operInfo = Config.PokerOperState.FOLLOWCHIPS;
			operList.add(operInfo);				
		}
		if(seat.getActionEndTime() < System.currentTimeMillis() && bool) {
			seat.setActionEndTime(System.currentTimeMillis() + ZhaJinHuaD.PLAYER_CARD_TIME);
		}
		seat.setActionOper(operList);
		ret.put(D.PLAYER_BASE_ACTION_OPER, operList);
		ret.put(D.TABLE_ACTION_SEAT_INDEX, seatId);
		ret.put(D.PLAYER_RID, seat.getPlayer().getRoleId());
		ret.put(D.PLAYER_CANCOMPARE_CARD_PLAYERLIST, beSeatIdList);
		ret.put(D.SEAT_ACTION_TO_TIME, seat.getActionEndTime());
		return ret;
	}

	/**
	 * 是否有操作
	 * 
	 * @return
	 */
	public boolean havaOper() {
		return canSeeCard || canCompare || canDisCard || canAddChips||canFollowChips;
	}

	/**
	 * 初始化操作
	 * 
	 * @return
	 */
	public void initOper() {
		canSeeCard = false;
		canCompare = false;
		canDisCard = false;
		canAddChips = false;
		canFollowChips = false;
		beSeatIdList.clear();
	}

	public void clear() {
		seatId = 0;
		initOper();
	}

	@Override
	public String toString() {
		return "OperNotify [time=" + time + ", seatId=" + seatId + ", beSeatIdList=" + beSeatIdList + ", wagerLimit="
				+ wagerLimit + ", canSeeCard=" + canSeeCard + ", canCompare=" + canCompare + ", canDisCard="
				+ canDisCard + ", canAddChips=" + canAddChips + ", canFollowChips=" + canFollowChips + "]";
	}
	
}