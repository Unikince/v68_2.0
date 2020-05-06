package com.dmg.bcbm.logic.service;

import com.dmg.bcbm.logic.entity.SimplePlayer;
import com.dmg.data.client.NettySend;

public interface UserService {
	 /**
	  * 查询用户信息
	  * @param roleid
	  */
	SimplePlayer getUserInfo(String roleid, String uuid,int roomId);
	
	/**
	 * 下注
	 * @param roleid
	 * @param betcoin
	 * @param roundId
	 * @param uuid
	 * @throws Exception 
	 */
	void bet(String roleid, double betcoin, String uuid,int roomId);
	
	/**
	 * 派彩
	 * @param roleid
	 * @param betcoin
	 * @param roundId
	 * @param uuid
	 * @throws Exception
	 */
	void payout(String roleid, double betcoin, String roundId, String uuid,int roomId) ;
	

	void init(NettySend nettySend);
	
	/**
	 * 进入同步房间
	 * @param roomLevel
	 * @param roomId
	 * @param userId
	 */
	void syncRoomFromJoin(int roomLevel, int roomId, long userId);
	/**
	 * 退出同步房间
	 * @param roomLevel
	 * @param roomId
	 * @param userId
	 */
	void syncRoomFromExit(long userId);
}
