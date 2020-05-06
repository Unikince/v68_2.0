package com.zyhy.lhj_server.bgmanagement.dao;

import java.util.List;
import java.util.Map;

import com.zyhy.lhj_server.bgmanagement.config.GamePoolConfigEnum;
import com.zyhy.lhj_server.bgmanagement.entity.GamePoolConfig;
import com.zyhy.lhj_server.bgmanagement.entity.OddsPoolConfig;
import com.zyhy.lhj_server.bgmanagement.entity.PayoutLimit;
import com.zyhy.lhj_server.bgmanagement.entity.PayoutPlayerLimit;
import com.zyhy.lhj_server.bgmanagement.entity.PayoutRecord;
import com.zyhy.lhj_server.bgmanagement.entity.SoltGameInfo;
import com.zyhy.lhj_server.bgmanagement.entity.SoltGameInventoryConfig;
import com.zyhy.lhj_server.bgmanagement.entity.SoltGameList;
import com.zyhy.lhj_server.bgmanagement.entity.palyerWaterRecord;
import com.zyhy.lhj_server.bgmanagement.entity.totalWaterRecord;
import com.zyhy.lhj_server.bgmanagement.feign.dto.LhjInventoryControlDTO;

public interface BgManagementDao {
	/**
	 * 查询所有游戏信息
	 * @return
	 */
	List<SoltGameInfo> queryAllGameInfo();
	/**
	 * 获取游戏列表
	 * @return
	 */
	List<SoltGameList> getSoltList();
	/**
	 * 增加游戏
	 * @param gameId
	 * @param number
	 * @return
	 * @throws Exception 
	 */
	int addSoltInfo(String gamename, int number) throws Exception;
	/**
	 * 删除游戏
	 * @param gameId
	 * @return 
	 */
	int delSoltInfo(int gameId);
	
	
	/**
	 * 查询单个游戏信息
	 * @return
	 */
	SoltGameInfo querySingleGameInfo(int gameId);
	
	/**
	 * 更新老虎机信息
	 * @param data
	 */
	int updateSoltInfo(SoltGameInfo data);
	
	/**
	 * 开启所有老虎机
	 * @param data
	 */
	int openAllsoltGame();
	/**
	 * 关闭所有老虎机
	 * @param data
	 */
	int closeAllsoltGame();
	
	/**
	 * 查询库存配置
	 * @param data
	 * @return 
	 */
	List<SoltGameInventoryConfig> queryInventoryInfo(int gameId);
	/**
	 * 添加库存配置
	 * @param data
	 * @return 
	 */
	int addInventoryInfo(SoltGameInventoryConfig data);
	/**
	 * 删除库存配置
	 * @param data
	 * @return 
	 */
	int delInventoryInfo(int gameId, int number);
	/**
	 * 查询库存控制配置
	 * @param data
	 * @return
	 */
	LhjInventoryControlDTO queryInventoryControl(int gameId);
	
	/**
	 * 更新库存控制配置
	 * @param data
	 * @return
	 */
	int updateInventoryControl(LhjInventoryControlDTO data);
	
	/**
	 * 获取游戏奖池配置
	 * @return
	 */
	GamePoolConfig querySinglePoolConfig(int gameId, String poolName);
	/**
	 * 添加奖池配置
	 * @param data
	 * @return
	 */
	int addPoolConfig(GamePoolConfigEnum data);
	/**
	 * 更新奖池配置
	 * @param data
	 * @return
	 */
	int updatePoolConfig(GamePoolConfig pc);
	
	/**
	 * 查询赔率奖池信息
	 * @return 
	 */
	OddsPoolConfig queryOddsPoolInfo();
	/**
	 * 更新赔率奖池信息
	 */
	int updateOddsPoolInfo(int state, double Ratio);
	/**
	 * 查询派奖条件
	 * @return 
	 */
	List<PayoutLimit> queryPayLimitInfo();
	/**
	 * 添加派奖条件
	 * @return 
	 */
	int addPayLimitInfo(PayoutLimit data);
	/**
	 * 更新派奖条件
	 * @return 
	 */
	int updatePayLimitInfo(PayoutLimit data);
	/**
	 * 删除派奖条件
	 */
	int delPayLimitInfo(int number);
	/**
	 * 查询玩家派奖条件
	 * @return 
	 */
	List<PayoutPlayerLimit> queryPayPlayerLimitInfo();
	/**
	 * 添加玩家派奖条件
	 * @return 
	 */
	int addPayPlayerLimitInfo(PayoutPlayerLimit data);
	/**
	 * 更新玩家派奖条件
	 * @return 
	 */
	int updatePlayerPayLimitInfo(PayoutPlayerLimit data);
	/**
	 * 删除玩家派奖条件
	 */
	int delPayPlayerLimitInfo(int number);
	
	/**
	 * 查询玩家单日赢取奖励
	 */
	double queryTodayWin(String roleid);
	
	/**
	 * 查询玩家当日下注金额
	 */
	double queryTodayBet(String roleid);
	
	/**
	 * 查询玩家当日的总收益
	 * @param roleid
	 * @return
	 */
	PayoutRecord queryTodayTotalReward(String roleid);
	
	/**
	 * 查询所有的老虎机日志
	 * @return 
	 */
	List<Map<String, Object>> queryAllLog();
	
	/**
	 *  查询派奖记录
	 */
	List<PayoutRecord> queryPlayerRecord(int page, int limit);
	
	/**
	 * 查询玩家总流水记录
	 */
	palyerWaterRecord queryPlayerWaterRecord(String roleid);
	
	/**
	 * 查询总流水
	 * @param roleid
	 * @return
	 */
	totalWaterRecord queryTotalWaterRecord();
	/**
	 * 添加玩家流水记录
	 * @param data
	 * @return
	 */
	int addPlayerWaterRecord(palyerWaterRecord data);
	
	/**
	 * 添加总流水记录
	 * @param data
	 * @return
	 */
	int addTotalWaterRecord(totalWaterRecord data);
	
	/**
	 * 更新玩家流水记录
	 * @param data
	 * @return 
	 */
	int updatePlayerWaterRecord();
	/**
	 * 更新总流水记录
	 * @param data
	 * @return 
	 */
	int updateTotalWaterRecord();
	
	
	
	
	
}
