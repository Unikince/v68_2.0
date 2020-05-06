package com.zyhy.lhj_server.bgmanagement.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.zyhy.lhj_server.bgmanagement.entity.GamePoolConfig;
import com.zyhy.lhj_server.bgmanagement.entity.OddsPoolConfig;
import com.zyhy.lhj_server.bgmanagement.entity.PayoutLimit;
import com.zyhy.lhj_server.bgmanagement.entity.PayoutPlayerLimit;
import com.zyhy.lhj_server.bgmanagement.entity.SoltGameInfo;
import com.zyhy.lhj_server.bgmanagement.entity.SoltGameInventoryConfig;
import com.zyhy.lhj_server.bgmanagement.feign.dto.LhjGameConfigDTO;
import com.zyhy.lhj_server.bgmanagement.feign.dto.LhjInventoryControlDTO;

/**
 * 后台缓存管理
 * @author zhuqd
 */
public class CacheManager {
	private static CacheManager instance = new CacheManager();
	private static Logger LOG = LoggerFactory.getLogger(CacheManager.class);
	private double exchangeRate;
	// 游戏信息(子游戏的所有配置)
	private Map<Integer, LhjGameConfigDTO> gameInfo = new ConcurrentHashMap<>();
	// 派奖条件
	private Map<Integer, PayoutLimit> payLimit = new ConcurrentHashMap<>(); 
	// 玩家派奖条件
	private Map<Integer, PayoutPlayerLimit> playerPayLimit = new ConcurrentHashMap<>(); 
	// 子游戏奖池信息
	private Map<Integer, Map<String, GamePoolConfig>> poolConfig = new ConcurrentHashMap<>(); 
	// 赔率奖池信息
	private Map<String, OddsPoolConfig> oddsPoolConfig = new ConcurrentHashMap<>(); 
	// 子游戏信息
	private Map<String, SoltGameInfo> soltGameInfo = new ConcurrentHashMap<>(); 
	// 子游戏库存配置信息
	private Map<Integer, Map<Integer, SoltGameInventoryConfig>> soltGameInventoryConfig = new ConcurrentHashMap<>(); 
	// 子游戏库存控制信息
	private Map<Integer, LhjInventoryControlDTO> soltGameInventoryControlInfo = new ConcurrentHashMap<>(); 
	
	//private ConcurrentHashMap<Integer, CopyOnWriteArrayList<Double>> carBet = new ConcurrentHashMap<>(); 
	//private ConcurrentHashMap<Integer, Map<String, Double>> gameBetInfo = new ConcurrentHashMap<>(); 
	private CacheManager() {
	}
	public static CacheManager instance() {
		return instance;
	}
	
	public void init(){
	}
	
	/**
	 * 更新游戏信息
	 */
	public void updateGameInfo(LhjGameConfigDTO dto){
		gameInfo.put(dto.getGameId(), dto);
	}
	
	/**
	 * 获取所有游戏信息
	 * @return 
	 */
	public Map<Integer, LhjGameConfigDTO> getGameInfo(){
		return gameInfo;
	}
	
	/**
	 * 获取单个游戏信息
	 * @return 
	 */
	public LhjGameConfigDTO getGameInfo(Integer gameId){
		return gameInfo.get(gameId);
	}

	/**
	 * 更新派奖条件
	 */
	public void updatePayLimit(PayoutLimit payLimitInfo){
		payLimit.put(payLimitInfo.getNumber(), payLimitInfo);
	}
	
	/**
	 * 获取派奖条件
	 * @return 
	 */
	public Map<Integer, PayoutLimit> getPayLimit(){
		return payLimit;
	}
	
	/**
	 * 删除派奖条件
	 */
	public void delPayLimit(int number){
		payLimit.remove(number);
	}
	
	/**
	 * 更新玩家派奖条件
	 */
	public void updatePlayerPayLimit(PayoutPlayerLimit payPlayerLimitInfo){
		playerPayLimit.put(payPlayerLimitInfo.getNumber(), payPlayerLimitInfo);
	}
	
	/**
	 * 获取玩家派奖条件
	 * @return 
	 */
	public Map<Integer, PayoutPlayerLimit> getPlayerPayLimit(){
		return playerPayLimit;
	}
	
	/**
	 * 删除玩家派奖条件
	 */
	public void delPlayerPayLimit(int number){
		playerPayLimit.remove(number);
	}
	
	/**
	 * 更新年年有余,糖果派对奖池信息
	 */
	public void updatePoolConfig(GamePoolConfig config){
		if (poolConfig.containsKey(config.getGameId())) {
			Map<String, GamePoolConfig> map = poolConfig.get(config.getGameId());
			map.put(config.getPoolName(), config);
		} else {
			Map<String, GamePoolConfig> map = new ConcurrentHashMap<>();
			map.put(config.getPoolName(), config);
			poolConfig.put(config.getGameId(), map);
		}
	}
	
	/**
	 * 获取年年有余,糖果派对奖池信息
	 * @return 
	 */
	public Map<String, GamePoolConfig> getPoolConfig(int gameId){
		return poolConfig.get(gameId);
	}
	
	/**
	 * 更新赔率奖池配置
	 * @return
	 */
	public void updateOddsPoolConfig(OddsPoolConfig config){
		oddsPoolConfig.put("oddsPoolConfig", config);
	}
	
	/**
	 * 获取赔率奖池配置
	 * @return
	 */
	public OddsPoolConfig getOddsPoolConfig(){
		return oddsPoolConfig.get("oddsPoolConfig");
	}
	
	/**
	 * 更新子游戏信息
	 */
	public void updateGameInfo(SoltGameInfo gameInfo){
		soltGameInfo.put(gameInfo.getRedisName(), gameInfo);
	}
	
	/**
	 * 更新所有子游戏信息
	 */
	public void updateAllGameInfo(List<SoltGameInfo> allGameInfo){
		if (allGameInfo.size() > 0) {
			for (SoltGameInfo gameInfo : allGameInfo) {
				updateGameInfo(gameInfo);
			}
		}
	}
	
	/**
	 * 获取子游戏信息
	 * @return 
	 */
	public SoltGameInfo getGameInfo(String gameRedisName){
		return soltGameInfo.get(gameRedisName);
	}
	
	/**
	 * 获取所有子游戏信息
	 * @return 
	 */
	public Map<String, SoltGameInfo> getAllGameInfo(){
		return soltGameInfo;
	}
	
	/**
	 * 初始化添加所有子游戏库存配置信息
	 * @param inventoryInfo
	 */
	public void updateAllSoltGameInventoryConfig(List<SoltGameInventoryConfig> inventoryInfo){
		for (SoltGameInventoryConfig config : inventoryInfo) {
			if (soltGameInventoryConfig.containsKey(config.getGameId())) {
				Map<Integer, SoltGameInventoryConfig> map = soltGameInventoryConfig.get(config.getGameId());
				map.put(config.getNumber(), config);
			} else {
				Map<Integer, SoltGameInventoryConfig> map = new ConcurrentHashMap<>();
				map.put(config.getNumber(), config);
				soltGameInventoryConfig.put(config.getGameId(), map);
			}
		}
	}
	
	/**
	 * 更新单个子游戏库存配置信息
	 * @param inventoryInfo
	 */
	public void updateSoltGameInventoryConfig(SoltGameInventoryConfig config){
		if (soltGameInventoryConfig.containsKey(config.getGameId())) {
			Map<Integer, SoltGameInventoryConfig> map = soltGameInventoryConfig.get(config.getGameId());
			map.put(config.getNumber(), config);
		} else {
			Map<Integer, SoltGameInventoryConfig> map = new ConcurrentHashMap<>();
			map.put(config.getNumber(), config);
			soltGameInventoryConfig.put(config.getGameId(), map);
		}
	}
	
	/**
	 * 获取单个子游戏库存配置信息
	 * @param inventoryInfo
	 * @return 
	 */
	public Map<Integer, SoltGameInventoryConfig> getSoltGameInventoryConfig(int gameId){
		if (soltGameInventoryConfig.containsKey(gameId)) {
			return soltGameInventoryConfig.get(gameId);
		}
		return null; 
	}
	
	
	/**
	 * 获取库存控制信息
	 * @param gameId
	 * @return
	 */
	public LhjInventoryControlDTO getSoltGameInventoryControlInfo(int gameId){
		return soltGameInventoryControlInfo.get(gameId);
	}
	
	/**
	 *更新库存控制信息
	 * @param gameId
	 * @return
	 */
	public void updateSoltGameInventoryControlInfo(LhjInventoryControlDTO dto){
		soltGameInventoryControlInfo.put(dto.getGameId(), dto);
	}
	
	
	
	/**
	 * 删除单个子游戏库存配置信息
	 * @param inventoryInfo
	 */
	public void delSoltGameInventoryConfig(int gameId, int number){
		if (soltGameInventoryConfig.containsKey(gameId)) {
			Map<Integer, SoltGameInventoryConfig> map = soltGameInventoryConfig.get(gameId);
			map.remove(number);
		} 
	}
	public double getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	
}
