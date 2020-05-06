package com.zyhy.lhj_server.bgmanagement.service.imp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dmg.server.common.model.dto.GameRecordDTO;
import com.zyhy.common_lhj.pool.DragonPool;
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.common_server.util.RandomUtil;
import com.zyhy.lhj_server.bgmanagement.config.MessageIdEnum;
import com.zyhy.lhj_server.bgmanagement.dao.imp.BgManagementDaoImp;
import com.zyhy.lhj_server.bgmanagement.entity.GamePoolConfig;
import com.zyhy.lhj_server.bgmanagement.entity.OddsPoolConfig;
import com.zyhy.lhj_server.bgmanagement.entity.PayoutLimit;
import com.zyhy.lhj_server.bgmanagement.entity.PayoutPlayerLimit;
import com.zyhy.lhj_server.bgmanagement.entity.SoltGameInfo;
import com.zyhy.lhj_server.bgmanagement.entity.SoltGameInventoryConfig;
import com.zyhy.lhj_server.bgmanagement.entity.SoltGameList;
import com.zyhy.lhj_server.bgmanagement.entity.palyerWaterRecord;
import com.zyhy.lhj_server.bgmanagement.entity.totalWaterRecord;
import com.zyhy.lhj_server.bgmanagement.feign.LhjGameConfigService;
import com.zyhy.lhj_server.bgmanagement.feign.dto.LhjFieldConfigDTO;
import com.zyhy.lhj_server.bgmanagement.feign.dto.LhjGameConfigDTO;
import com.zyhy.lhj_server.bgmanagement.feign.dto.LhjInventoryConfigDTO;
import com.zyhy.lhj_server.bgmanagement.feign.dto.LhjInventoryControlDTO;
import com.zyhy.lhj_server.bgmanagement.feign.dto.LhjOddsPoolConfigDTO;
import com.zyhy.lhj_server.bgmanagement.feign.dto.LhjPayLimitConfigDTO;
import com.zyhy.lhj_server.bgmanagement.feign.dto.LhjPlayerPayLimitConfigDTO;
import com.zyhy.lhj_server.bgmanagement.feign.dto.UserControlListDTO;
import com.zyhy.lhj_server.bgmanagement.manager.CacheManager;
import com.zyhy.lhj_server.bgmanagement.manager.WorkManager;
import com.zyhy.lhj_server.bgmanagement.service.BgManagementService;
import com.zyhy.lhj_server.game.GameOddsEnum;
import com.zyhy.lhj_server.game.UpdaeInventory;
import com.zyhy.lhj_server.game.tgpd.TgpdPoolManager;
import com.zyhy.lhj_server.game.xywjs.XywjsOddsEnum;
import com.zyhy.lhj_server.manager.pool.PoolManager;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class BgManagementServiceImp implements BgManagementService {
	@Autowired
	private BgManagementDaoImp bgManagementDaoImp;
	@Autowired
	private PoolManager poolManager;
	@Autowired
	private TgpdPoolManager tgpdPoolManager;
	@Autowired
	private LhjGameConfigService lhjGameConfigService;
	private static final Logger LOG = LoggerFactory.getLogger(BgManagementServiceImp.class);
	private CacheManager cache = CacheManager.instance();
	private final int tgpdId = 18;
	private final int  nnyyId = 2;
	
	public void init() throws Exception{
		// 获取所有游戏信息添加到缓存
		List<SoltGameList> soltList = bgManagementDaoImp.getSoltList();
		for (SoltGameList game : soltList) {
			LhjGameConfigDTO lhjGameConfigData = lhjGameConfigService.getLhjGameConfigData(game.getId());
			if (lhjGameConfigData != null) {
				cache.updateGameInfo(lhjGameConfigData);
				LOG.info(" {} =====> GameInfo Init Success! data = {}", lhjGameConfigData.getGameId() ,lhjGameConfigData);
			}
		}
		// 初始化派奖条件
		initPayLimit();
		// 年年有余,糖果派对奖池初始化
		initPool();
		// 赔率奖池初始化
		initOddsPool();
		// 老虎机游戏信息初始化
		initAddSoltGameInfo();
		// 子游戏库存信息初始化
		initAddAllSoltGameInventoryConfig();
		// 库存控制信息初始化
		initInventoryControlInfo();
		// 获取汇率
		getExchangeRate();
	}
	
	/**
	 * 获取汇率
	 * @throws Exception 
	 */
	public void getExchangeRate() throws Exception{
		List<SoltGameList> soltList = bgManagementDaoImp.getSoltList();
		if (soltList.size() > 0) {
			LhjGameConfigDTO lhjGameConfigDTO = lhjGameConfigService.getLhjGameConfigData(soltList.get(0).getId());
			cache.setExchangeRate(lhjGameConfigDTO.getExchangeRate());
			LOG.info("当前游戏的汇率为 =====>" + cache.getExchangeRate());
		} else {
			throw new Exception("getExchangeRate failed!");
		}
		
	}
	
	/**
	 * 获取点控模型
	 * @param roleid
	 * @return
	 */
	public int getPointControlModel(String roleid){
		return lhjGameConfigService.getPointControlModel(Long.valueOf(roleid));
	}
	
	/**
	 * 获取玩家点控数据
	 * @param roleid
	 * @return 
	 */
	public UserControlListDTO getUserPointControlData(String roleid){
		return lhjGameConfigService.getUserControlInfo(Long.valueOf(roleid));
	}
	/**
	 * 获取自控模型
	 * @param roleid
	 * @return
	 */
	public int getAutoControlModel(String roleid){
		return lhjGameConfigService.getAutoControlModel(Long.valueOf(roleid));
	}
	
	/**
	 * 获取库存控制模型
	 * @return 
	 */
	public int getInventoryControlModel(int gameId,String roleid){
		int iconId = 0;
		LhjInventoryControlDTO controlinfo = getInventoryControlInfo(gameId);
		if (controlinfo != null) {
			// 设置的库存
	    	double setInventory = controlinfo.getSetInventory();
	    	// 设置的库存不为0就是有库存控制
	    	if (setInventory != 0) { 
	    		// 判断设置使用模型是否正确
	    		boolean checkModel = false;
	    		// 设置的模型
	    		int model = controlinfo.getModel();
	    		if (model > 0) {
	    			if (setInventory > 0 && model < 4) { // 抽水应使用1,2,3
	        			checkModel = true;
	    			} else if (setInventory < 0 && model > 4) { // 放水应使用5,6,7
	    				checkModel = true;
	    			}
				}
	    		if (checkModel) {
	    	   		// 设置的流水
	        		double waterValue = controlinfo.getWaterValue();
	        		// 玩家的总流水
	        		double totalWater = getUserPointControlData(roleid).getTotalBet().doubleValue();
	        		// 比较类型(1:大于,2:大于等于,3:小于,4:小于等于)
	        		int type = controlinfo.getType();
	        		// 判断玩家流水是否符合被控制类型
	        		switch (type) {
	    			case 1:
	    				if (totalWater > waterValue) {
	    					iconId = model;
	    				}
	    				break;
	    			case 2:
	    				if (totalWater >= waterValue) {
	    					iconId = model;
	    				}
	    				break;
	    			case 3:
	    				if (totalWater < waterValue) {
	    					iconId = model;
	    				}
	    				break;
	    			case 4:
	    				if (totalWater <= waterValue) {
	    					iconId = model;
	    				}
	    				break;
	    			}
				}
			}
		}
		return iconId;
	}
	
	/**
	 * 获取老虎机的系统自控模型
	 */
	public int getLhjSystemControlModel(String redisName,int lastRoundIconId){
		int iconId = 0;
		// 获取当前游戏信息
		SoltGameInfo gameInfo = queryGameInfo(redisName);
    	LOG.info("LhjGameInfo =====>" + gameInfo);
    	double odds = queryLhjCurrentOdds(gameInfo);
    	iconId = GameOddsEnum.getIdByOdds(odds);
    	// 如果图标是升序,就继续使用上一局的图标
    	if (lastRoundIconId > 0 && iconId > lastRoundIconId && iconId < 4) {
             iconId = lastRoundIconId;
        }
    	// 如果没有取到图标,就继续使用上一局图标
		if (iconId == 0 && lastRoundIconId > 0) {
	         iconId = lastRoundIconId;
	    } else if (iconId == 0 && lastRoundIconId == 0) { // 如果都没有取到图标,就使用第4套图标
	        iconId = 4;
	    }
		return iconId;
	}
	
	/**
	 * 获取水果机的系统自控模型
	 */
	public int getSgjSystemControlModel(String redisName,int lastRoundIconId){
		int iconId = 0;
		// 获取当前游戏信息
		SoltGameInfo gameInfo = queryGameInfo(redisName);
    	LOG.info("SgjGameInfo =====>" + gameInfo);
    	double odds = querySgjCurrentOdds(gameInfo);
    	iconId = XywjsOddsEnum.getIdByOdds(odds);
    	// 如果图标是升序,就继续使用上一局的图标
    	if (lastRoundIconId > 0 && iconId > lastRoundIconId && iconId < 4) {
             iconId = lastRoundIconId;
        }
    	// 如果没有取到图标,就继续使用上一局图标
    	 if (iconId == 0 && lastRoundIconId > 0) {
             iconId = lastRoundIconId;
         } else if (iconId == 0 && lastRoundIconId == 0) { // 如果都没有取到图标,就使用第4套图标
             iconId = 4;
         }
		return iconId;
	}
	
	/**
	 * 加载派奖条件到缓存
	 */
	public void initPayLimit(){
		Map<Integer, LhjGameConfigDTO> gameInfo = cache.getGameInfo();
		Integer gameId = 0;
		if (gameInfo .size() > 0) {
			for (Integer id : gameInfo.keySet()) {
				gameId = id;
				break;
			}
		}
		
		if (gameId > 0) {
			// 派奖条件
			List<LhjPayLimitConfigDTO> lhjPayLimitConfigList = gameInfo.get(gameId).getLhjPayLimitConfigList();
			if (lhjPayLimitConfigList != null) {
				if (lhjPayLimitConfigList.size() > 0) {
					for (LhjPayLimitConfigDTO dto : lhjPayLimitConfigList) {
						PayoutLimit limit = new PayoutLimit();
						BeanUtils.copyProperties(dto, limit);
						cache.updatePayLimit(limit);
						LOG.info("派奖条件{} : =====> {} !",limit.getNumber(),limit);
					}
				}
			}
			
			// 玩家派奖条件
			List<LhjPlayerPayLimitConfigDTO> lhjPlayerPayLimitConfigList = gameInfo.get(gameId).getLhjPlayerPayLimitConfigList();
			if (lhjPlayerPayLimitConfigList != null) {
				if (lhjPlayerPayLimitConfigList.size() > 0) {
					for (LhjPlayerPayLimitConfigDTO dto : lhjPlayerPayLimitConfigList) {
						PayoutPlayerLimit limit = new PayoutPlayerLimit();
						BeanUtils.copyProperties(dto, limit);
						cache.updatePlayerPayLimit(limit);
						LOG.info("玩家派奖条件{} : =====> {} !",limit.getNumber(),limit);
					}
				}
			}
		}	
		LOG.info("=====> PayLimit Init Success!");
	}
	
	/**
	 * 年年有鱼和糖果派对奖池初始化
	 * @throws Exception 
	 */
	@Override
	public void initPool() throws Exception {
		/*List<LhjBonusConfigDTO> nnyyGameInfo = cache.getGameInfo(nnyyId).getLhjBonusConfigList();
		List<LhjBonusConfigDTO> tgpdGameInfo = cache.getGameInfo(tgpdId).getLhjBonusConfigList();
		
		if (nnyyGameInfo.size() <= 0) {
			throw new Exception("nnyyGamePool Init failed!");
		} else {
			for (LhjBonusConfigDTO dto : nnyyGameInfo) {
				GamePoolConfig config = new GamePoolConfig();
				BeanUtils.copyProperties(dto, config);
				cache.updatePoolConfig(config);
				LOG.info("GamePoolinfo =====> " + config);
			}
			LOG.info("=====> GamePool Init Success!");
		}
		
		if (tgpdGameInfo.size() <= 0) {
			throw new Exception("tgpdGamePool Init failed!");
		} else {
			for (LhjBonusConfigDTO dto : tgpdGameInfo) {
				GamePoolConfig config = new GamePoolConfig();
				BeanUtils.copyProperties(dto, config);
				cache.updatePoolConfig(config);
				LOG.info("GamePoolinfo =====> " + config);
			}
			LOG.info("=====> GamePool Init Success!");
		}*/
	}
	
	/**
	 * 初始化赔率奖池
	 * @throws Exception 
	 */
	@Override
	public void initOddsPool() throws Exception {
		Map<Integer, LhjGameConfigDTO> gameInfo = cache.getGameInfo();
		Integer gameId = 0;
		if (gameInfo .size() > 0) {
			for (Integer id : gameInfo.keySet()) {
				gameId = id;
				break;
			}
		}
		if (gameId > 0) {
			// 赔率奖池配置
			LhjOddsPoolConfigDTO dto = gameInfo.get(gameId).getLhjOddsPoolConfig();
			if (dto != null) {
				OddsPoolConfig oddsPoolInfo = new OddsPoolConfig();
				BeanUtils.copyProperties(dto, oddsPoolInfo);
				cache.updateOddsPoolConfig(oddsPoolInfo);
				LOG.info("OddsPool Init Success! =====> {}",oddsPoolInfo);
			} else {
				throw new Exception("OddsPool Init failed!");
			}
		} else {
			throw new Exception("OddsPool Init failed!");
		}
	}
	
	/**
	 * 初始化添加老虎机游戏信息
	 * @throws Exception 
	 */
	public void initAddSoltGameInfo() throws Exception{
		Map<Integer, LhjGameConfigDTO> gameInfo = cache.getGameInfo();
		if (gameInfo.size() > 0) {
			for (Integer gameId : gameInfo.keySet()) {
				LhjGameConfigDTO lhjGameConfigDTO = gameInfo.get(gameId);
				if (lhjGameConfigDTO != null) {
					LhjFieldConfigDTO lhjFieldConfig = lhjGameConfigDTO.getLhjFieldConfig();
					SoltGameInfo info = new SoltGameInfo();
					BeanUtils.copyProperties(lhjFieldConfig, info);
					cache.updateGameInfo(info);
					LOG.info(" {} =====> subGameInfo Init Success! info = {}", info.getGameId() ,info);
				} else {
					throw new Exception("subGameInfo Init failed!");
				}
			}
		} else {
			throw new Exception("subGameInfo Init failed!");
		}
	}
	
	/**
	 * 将所有游戏库存配置添加到缓存
	 * @throws Exception 
	 */
	public void initAddAllSoltGameInventoryConfig() throws Exception{
		Map<Integer, LhjGameConfigDTO> gameInfo = cache.getGameInfo();
		if (gameInfo .size() > 0) {
			for (LhjGameConfigDTO dto : gameInfo.values()) {
				List<LhjInventoryConfigDTO> lhjInventoryConfig = dto.getLhjInventoryConfig();
				if (lhjInventoryConfig != null && lhjInventoryConfig.size() > 0) {
					List<SoltGameInventoryConfig> inventoryConfigList = new ArrayList<>();
					for (LhjInventoryConfigDTO InventoryConfigDTO : lhjInventoryConfig) {
						SoltGameInventoryConfig config = new SoltGameInventoryConfig();
						BeanUtils.copyProperties(InventoryConfigDTO, config);
						inventoryConfigList.add(config);
					}
					cache.updateAllSoltGameInventoryConfig(inventoryConfigList);
					LOG.info("InventoryConfig Init Success! =====> {}",inventoryConfigList);
				}
			}
		} else {
			throw new Exception("InventoryConfig Init failed!");
		}
	}

	/**
	 * 初始化库存控制信息
	 */
	public void initInventoryControlInfo(){
		Map<Integer, LhjGameConfigDTO> gameInfo = cache.getGameInfo();
		if (gameInfo.size() > 0) {
			for (LhjGameConfigDTO dto : gameInfo.values()) {
				LhjInventoryControlDTO lhjInventoryControl = dto.getLhjInventoryControl();
				if (lhjInventoryControl != null) {
					cache.updateSoltGameInventoryControlInfo(lhjInventoryControl);
					LOG.info("InventoryControl Init Success! =====> {}",lhjInventoryControl);
				}
			}
		}
	}
	
	/**
	 * 获取游戏库存控制信息
	 * @param gameId
	 * @return
	 */
	public LhjInventoryControlDTO getInventoryControlInfo(int gameId){
		return cache.getSoltGameInventoryControlInfo(gameId);
	}
	
	/**
	 * 更新游戏库存控制信息
	 * @param gameId
	 * @return
	 */
	public void updateInventoryControlInfo(double gold,int gameId){
		LhjInventoryControlDTO dto = getInventoryControlInfo(gameId);
		// 处于库存控制状态才进行更新
		if (dto.getSetInventory() != 0 && dto.getModel() > 0) {
			// 更新后的输赢值
			dto.setCurrentWinLoseValue(NumberTool.add(dto.getCurrentWinLoseValue(), gold).doubleValue());
			//抽水,设置的库存值应为+,当前的输赢值应为-
			if (dto.getSetInventory() > 0 && dto.getCurrentWinLoseValue() < 0) { 
				// 抽水完成
				if (Math.abs(dto.getCurrentWinLoseValue()) >= dto.getSetInventory()) { 
					dto.setSetInventory(0);
					dto.setCurrentWinLoseValue(0);
					dto.setModel(0);
					dto.setWaterValue(0);
					dto.setType(1);
				}
			} else if (dto.getSetInventory() < 0) { // 放水,设置的库存值应为-,当前的输赢值应为+
				// 放水完成
				if (dto.getCurrentWinLoseValue() >= Math.abs(dto.getSetInventory())) {
					dto.setSetInventory(0);
					dto.setCurrentWinLoseValue(0);
					dto.setModel(0);
					dto.setWaterValue(0);
					dto.setType(1);
				}
			}
			cache.updateSoltGameInventoryControlInfo(dto);
			int update = bgManagementDaoImp.updateInventoryControl(dto);
			if (update > 0) {
				log.info("updateSoltGameInventoryControlInfo =====> Success!");
			} else {
				log.info("updateSoltGameInventoryControlInfo =====> Failed!");
			}
		}
	}
	
	
	/**
	 * 查询下注列表
	 */
	@Override
	public List<Double> queryBetList(String gamename) {
		SoltGameInfo info = cache.getGameInfo(gamename);
		String betList = info.getBetList();
		System.out.println("BetList:" + betList);
		if (betList.startsWith("\"")) {
			String sub1 = betList.substring(1, betList.length());
			if (sub1.endsWith("\"")) {
				String sub2 = sub1.substring(0, sub1.length() - 1);
				return JSONObject.parseArray(sub2, Double.class);
			}
		}
		return JSONObject.parseArray(betList, Double.class);
	}
	
	/**
	 * 查询指定游戏信息
	 * @return
	 */
	public SoltGameInfo queryGameInfo(String gamename) {
		return cache.getGameInfo(gamename);
	}
	
	/**
	 * 查询水果机赔率
	 * @return
	 */
	@Override
	public double querySgjCurrentOdds(SoltGameInfo gameInfo) {
		Map<Integer, SoltGameInventoryConfig> inventory = cache.getSoltGameInventoryConfig(gameInfo.getGameId());
		if (inventory == null) {
			return 0.90;
		}
		double odds = gameInfo.getOdds();
		for (SoltGameInventoryConfig config : inventory.values()) {
			if (odds > config.getSvalue() && odds <  config.getBvalue()) {
				return config.getOdds();
			}
		} 
		return 0.90;
	}
	
	/**
	 * 查询老虎机赔率
	 * @return
	 */
	@Override
	public double queryLhjCurrentOdds(SoltGameInfo gameInfo) {
		Map<Integer, SoltGameInventoryConfig> inventory = cache.getSoltGameInventoryConfig(gameInfo.getGameId());
		if (inventory == null) {
			return 0.95;
		}
		double odds = gameInfo.getOdds();
		for (SoltGameInventoryConfig config : inventory.values()) {
			if (odds > config.getSvalue() && odds <  config.getBvalue()) {
				return config.getOdds();
			}
		} 
		return 0.95;
	}

	/**
	 * 查询赔率奖池是否处于派奖状态
	 */
	@Override
	public PayoutLimit queryWinlimit() {
		OddsPoolConfig oddsInfo = cache.getOddsPoolConfig();
		if (oddsInfo == null) {
			 oddsInfo = bgManagementDaoImp.queryOddsPoolInfo();
		}
		// 赔率奖池信息
		PayoutLimit pl = null;
		int state = oddsInfo.getState();
		if (state == 1) {
			// 当前累计金额
			double currentAmount = oddsInfo.getCurrentAmount();
			Map<Integer, PayoutLimit> limits = cache.getPayLimit();
			if (limits.size() > 0) {
				pl = findNumber(limits, currentAmount);
				//System.out.println("符合条件的派奖条件:" + pl);
			}
		}
		return pl;
	}
	
	/**
	 * 查找赔率奖池开启区间
	 * @param limits
	 * @param currentAmount
	 * @return
	 */
	private PayoutLimit findNumber(Map<Integer, PayoutLimit> limits,double currentAmount){
		/*for (PayoutLimit limit : limits.values()) {
			System.out.println("当前设置的派奖条件:" + limit);
		}
		System.out.println("当前赔率奖池的金额:" + currentAmount);*/
		List<Double> amounts = new ArrayList<Double>();
		for (PayoutLimit limt : limits.values()) {
			amounts.add(limt.getPayLowLimit());
		}
		Collections.sort(amounts);
		//System.out.println("金额的排序:" + amounts);
		double a = 0;
		for (int i = 0; i < amounts.size(); i++) {
			if (amounts.get(i) > currentAmount) {
				if (i > 0) {
					a = amounts.get(i - 1);
				}
				break;
			}
			if (i == amounts.size() - 1 && amounts.get(i) < currentAmount) {
				a = amounts.get(i);
			}
		}
		//System.out.println("被选中的金额:" + a);
		if (a > 0) {
			for (PayoutLimit limit : limits.values()) {
				if (limit.getPayLowLimit() == a) {
					return limit;
				}
			}
		}
		return null;
	}
	
	/**
	 * 判断玩家是否满足赔率奖池派奖条件
	 */
	@Override
	public boolean queryPlayerWinlimit(String roleid) {
		// 玩家派奖条件
		Map<Integer, PayoutPlayerLimit> payPlayerLimit = cache.getPlayerPayLimit();
		// 查询玩家流水记录
		palyerWaterRecord playerWaterRecord = bgManagementDaoImp.queryPlayerWaterRecord(roleid);
		// 查询总流水
		totalWaterRecord totalWaterRecord = bgManagementDaoImp.queryTotalWaterRecord();
		//System.out.println(" 玩家派奖条件" + payPlayerLimit);
		//System.out.println(" 玩家流水记录" + playerWaterRecord);
		//System.out.println(" 总流水记录" + totalWaterRecord);
		if (payPlayerLimit.size() <= 0) {
			return true;
		} else {
			for (PayoutPlayerLimit ppl : payPlayerLimit.values()) {
				if (checkLimit(ppl,playerWaterRecord,totalWaterRecord)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 年年有余糖果派对添加奖金到奖池
	 * @param gameId
	 * @return
	 */
	public Map<String, Double> addBet(int gameId,Double totalBet){
		Map<String, Double> betMap = new HashMap<>();
		List<GamePoolConfig> poolConfig = bgManagementDaoImp.queryAllPoolConfig(gameId);
		if (poolConfig.size() > 0) {
			for (GamePoolConfig gpc : poolConfig) {
				if (totalBet > gpc.getLowBet()) {
					betMap.put(gpc.getPoolName(), totalBet * gpc.getPoolTotalRatio());
				}
			}
		}
		return betMap;
	}
	
	/**
	 * 年年有余糖果派对检查是否中奖池游戏
	 * @return 
	 */
	public Map<String, Double> checkPoolGame(int gameId){
		DragonPool pool = null;
		if (gameId == 2) {
			pool = poolManager.getPool();
		} else {
			pool = tgpdPoolManager.getPool();
		}
		Map<String, Double> reward = new HashMap<>();
		List<GamePoolConfig> poolConfig = bgManagementDaoImp.queryAllPoolConfig(gameId);
		Map<String, Double> openLowMap = new HashMap<>();
		Map<String, GamePoolConfig> poolMap = new HashMap<>();
		if (poolConfig.size() > 0) {
			for (GamePoolConfig gpc : poolConfig) {
				openLowMap.put(gpc.getPoolName(), gpc.getPoolOpenLow());
				poolMap.put(gpc.getPoolName(), gpc);
			}
		}
		if (pool.getMini() > openLowMap.get(DragonPool.MINI)) {
			GamePoolConfig gamePoolConfig = poolMap.get(DragonPool.MINI);
			double random = RandomUtil.getRandom(1.0,10000.0);
			if (random > 10000 - (gamePoolConfig.getBonusLv() * 100)) {
				reward.put(DragonPool.MINI, pool.getMini() * gamePoolConfig.getRewardRatio());
				return reward;
			}
		}
		if (pool.getMinor() > openLowMap.get(DragonPool.MINOR)) {
			GamePoolConfig gamePoolConfig = poolMap.get(DragonPool.MINOR);
			double random = RandomUtil.getRandom(1.0,10000.0);
			if (random > 10000 - (gamePoolConfig.getBonusLv() * 100)) {
				reward.put(DragonPool.MINOR, pool.getMinor() * gamePoolConfig.getRewardRatio());
				return reward;
			}
		}
		if (pool.getMajor() > openLowMap.get(DragonPool.MAJOR)) {
			GamePoolConfig gamePoolConfig = poolMap.get(DragonPool.MAJOR);
			double random = RandomUtil.getRandom(1.0,10000.0);
			if (random > 10000 - (gamePoolConfig.getBonusLv() * 100)) {
				reward.put(DragonPool.MAJOR, pool.getMajor() * gamePoolConfig.getRewardRatio());
				return reward;
			}
		}
		if (pool.getGrand() > openLowMap.get(DragonPool.GRAND)) {
			GamePoolConfig gamePoolConfig = poolMap.get(DragonPool.GRAND);
			double random = RandomUtil.getRandom(1.0,10000.0);
			if (random > 10000 - (gamePoolConfig.getBonusLv() * 100)) {
				reward.put(DragonPool.GRAND, pool.getGrand() * gamePoolConfig.getRewardRatio());
				return reward;
			}
		}
		return reward;
	}
	
	/**
	 * 年年有余和糖果派对初始化金额
	 * @return 
	 */
	public Map<String, Double> initAmount(int gameId){
		Map<String, Double> init = new HashMap<>();
		List<GamePoolConfig> poolConfig = bgManagementDaoImp.queryAllPoolConfig(gameId);
		if (poolConfig.size() > 0) {
			for (GamePoolConfig config : poolConfig) {
				init.put(config.getPoolName(), config.getInitAmount());
			}
		}
		return init;
	}
	
	private boolean checkLimit(PayoutPlayerLimit ppl, palyerWaterRecord pwr, totalWaterRecord twr){
		
		double totalWin = 0;
		double todayWin = 0;
		double totalBet = 0;
		double todayBet = 0;
		double totalWinlv = 0;
		double todayWinlv = 0;
		
		if (pwr != null) {
			// 累计输赢下限
			totalWin = pwr.getTotalWin();
			// 今日输赢下限
			todayWin = pwr.getTodayWin();
			// 累计赔率上限
			totalWinlv = pwr.getTotalWinlv();
			// 今日赔率上限
			todayWinlv = pwr.getTodayWinlv();
		}
		
		if (twr != null) {
			// 累计流水下限
			totalBet = twr.getTotalBet();
			// 今日流水下限
			todayBet = twr.getTodayBet();
		}
		
		if (totalWin < 0) {
			totalWin = Math.abs(totalWin);
		}
		
		if (todayWin < 0) {
			todayWin = Math.abs(todayWin);
		}
		
		
		if (totalWin >= ppl.getTotalLowLimit()
				&& todayWin >= ppl.getDayLowLimit()
				&& totalBet >= ppl.getTotalWaterLow()
				&& todayBet >= ppl.getDayWaterLow()
				&& totalWinlv >= ppl.getOddsTotalHight()
				&& todayWinlv >= ppl.getDayOddsHight()) {
			return true;
		}
		return false;
	}
	
	/**
	 *验证是否能获取大奖
	 * @param gameInfo
	 * @param reward
	 * @return
	 */
	public boolean checkBigReward(SoltGameInfo gameInfo, double reward){
		SoltGameInfo queryGameInfo = queryGameInfo(gameInfo.getRedisName());
		double totalPay = queryGameInfo.getTotalPay();
		double totalBet = queryGameInfo.getTotalBet();
		double checkOdds = queryGameInfo.getCheckOdds();
		if ((totalPay + reward)/totalBet <= checkOdds) {
			return true;
		}
		return false;
	}
	
	/**
	 * 更新所有的游戏配置
	 */
	public void updateAllGameConfig(){
		try {
			/*// 场次配置
			cache.updateAllGameInfo(bgManagementDaoImp.queryAllGameInfo());
			// 库存配置
			bgManagementDaoImp.queryAllInventoryInfo().forEach(v -> cache.updateSoltGameInventoryConfig(v));
			// 库存控制
			initInventoryControlInfo();
			// 彩金配置
			bgManagementDaoImp.queryAllPoolConfig(tgpdId).forEach(v -> cache.updatePoolConfig(v));
			bgManagementDaoImp.queryAllPoolConfig(nnyyId).forEach(v -> cache.updatePoolConfig(v));
			// 赔率奖池
			cache.updateOddsPoolConfig(bgManagementDaoImp.queryOddsPoolInfo());
			// 派奖条件
			bgManagementDaoImp.queryPayLimitInfo().forEach(v -> cache.updatePayLimit(v));
			// 玩家派奖条件
			bgManagementDaoImp.queryPayPlayerLimitInfo().forEach(v -> cache.updatePlayerPayLimit(v));*/
			init();
			log.info("=====> 更新所有游戏配置成功!");
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=====> 更新所有游戏配置失败!");
		}
	}
	
	/**
	 * 更新库存
	 * @param record
	 */
	public void updaeInventory(GameRecordDTO<?> record){
		// TODO 更新单个游戏库存信息
		SoltGameInfo gameInfo = CacheManager.instance().getGameInfo(MessageIdEnum.getRedisNamebyName(record.getGameName()));
		gameInfo.setTotalBet(NumberTool.add(gameInfo.getTotalBet(), record.getBetsGold()).doubleValue()); // 更新总下注
		double changGole = NumberTool.add(record.getBetsGold(), record.getWinLosGold()).doubleValue();
		gameInfo.setTotalPay(NumberTool.add(gameInfo.getTotalPay(), changGole).doubleValue()); // 更新总派彩
		gameInfo.setInventory(NumberTool.subtract(gameInfo.getTotalBet(), gameInfo.getTotalPay()).doubleValue()); // 更新库存
		// 更新赔率
		gameInfo.setOdds(NumberTool.divide(gameInfo.getTotalPay(), gameInfo.getTotalBet()).doubleValue());
		CacheManager.instance().updateGameInfo(gameInfo); //更新缓存
		WorkManager.instance().submit(UpdaeInventory.class, record); // 更新数据库以及redis
	}
	
}
