package com.zyhy.lhj_server.bgmanagement.service;

import java.util.List;

import com.zyhy.lhj_server.bgmanagement.entity.PayoutLimit;
import com.zyhy.lhj_server.bgmanagement.entity.SoltGameInfo;

public interface BgManagementService {
	
	/**
	 * 初始化老虎机游戏信息
	 * @throws Exception
	 */
	void initAddSoltGameInfo() throws Exception;
	/**
	 * 初始化奖池配置信息
	 * @throws Exception 
	 */
	void initPool() throws Exception;
	
	/**
	 * 初始化赔率奖池信息
	 * @throws Exception 
	 */
	void initOddsPool() throws Exception;
	
	/**
	 * 查询下注列表
	 * @return
	 */
	List<Double> queryBetList(String gamename);
	
	/**
	 * 查询水果机赔率
	 */
	double querySgjCurrentOdds(SoltGameInfo gameInfo);
	/**
	 * 查询老虎机赔率
	 */
	double queryLhjCurrentOdds(SoltGameInfo gameInfo);
	/**
	 * 查询是否满足中奖条件
	 * @return
	 */
	PayoutLimit queryWinlimit();
	/**
	 * 查询玩家是否满足中奖条件
	 */
	boolean queryPlayerWinlimit(String roleid);
	
}
	
