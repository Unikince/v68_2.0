package com.dmg.bcbm.logic.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.dmg.server.common.model.dto.GameRecordDTO;
import com.dmg.server.common.util.RoundIdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.alibaba.fastjson.JSONObject;
import com.dmg.bcbm.SpringContextUtil;
import com.dmg.bcbm.core.abs.role.Role;
import com.dmg.bcbm.core.annotation.Service;
import com.dmg.bcbm.core.config.D;
import com.dmg.bcbm.core.manager.RoomManager;
import com.dmg.bcbm.core.manager.ServiceManager;
import com.dmg.bcbm.core.pool.WaterPool;
import com.dmg.bcbm.core.pool.WaterPoolManager;
import com.dmg.bcbm.logic.entity.bcbm.Banker;
import com.dmg.bcbm.logic.entity.bcbm.BaseRobot;
import com.dmg.bcbm.logic.entity.bcbm.BmbcWinInfo;
import com.dmg.bcbm.logic.entity.bcbm.CarTypeEnum;
import com.dmg.bcbm.logic.entity.bcbm.PlayerRecordDTO;
import com.dmg.bcbm.logic.entity.bcbm.RecordEnum;
import com.dmg.bcbm.logic.entity.bcbm.Room;
import com.dmg.bcbm.logic.entity.bcbm.mainGameProcessResult;
import com.dmg.bcbm.logic.service.GameService;
import com.dmg.bcbm.logic.service.UserService;
import com.dmg.common.core.config.RedisRegionConfig;
import com.dmg.common.starter.rocketmq.core.producer.MQProducer;
import com.dmg.common.starter.rocketmq.core.producer.impl.DefautProducer;
import com.dmg.gameconfigserverapi.dto.BairenControlConfigDTO;
import com.dmg.gameconfigserverapi.dto.BairenFileConfigDTO;
import com.dmg.gameconfigserverapi.dto.BairenGameConfigDTO;
import com.dmg.gameconfigserverapi.dto.BairenGameConfigDTO.WaterPoolConfigDTO;
import com.google.common.collect.Lists;
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.common_server.util.RandomUtil;

import io.netty.channel.ChannelHandlerContext;
import util.ByteHelper;
import util.json.JsonUtil;
@Service
public class GameServiceImp implements GameService {
	private static final Logger LOG = LoggerFactory.getLogger(GameServiceImp.class);
	private AtomicInteger id = new AtomicInteger(100000000);
	@Override
	public void mainGameProcess(int roomId) throws Exception {
		mainGameProcessResult result = new mainGameProcessResult();
		StringRedisTemplate redisTemplate = SpringContextUtil.getBean(StringRedisTemplate.class);
		// 获取房间
		Room room = RoomManager.intance().getRoomById(roomId);
		String roundCode = RoundIdUtils.getGameRecordId(D.GAME_ID, String.valueOf(room.getRoomId())); // 回合id
		BairenGameConfigDTO gameConfig = room.getGameConfig().get(1); // 获取游戏配置
		LOG.info("room = " + room.getRoomId() + " =====>" + "当前游戏配置 : {} ! ",gameConfig);
		List<WaterPoolConfigDTO> waterPoolConfigDTOS = gameConfig.getWaterPoolConfigDTOS(); // 库存配置
		BairenFileConfigDTO bairenFileConfigDTO = gameConfig.getBairenFileConfigDTO(); // 场次配置
		BairenControlConfigDTO bairenControlConfigDTO = gameConfig.getBairenControlConfigDTO(); // 控制配置
		// 检查庄家是否在线
		UserService userService = ServiceManager.instance().get(UserService.class);
		Map<String, Role> roleMap = room.getRoleMap(); // 角色详情
		// 当前庄家
		Banker banker = room.getBanker();
		// 等待上庄列表
		//List<String> bankerList = room.getBankerList(); 
		// 是否为系统庄
		boolean isSystem = D.SYSTEMBANKER.equals(banker.getRoleid());
		// 获取图标
		BmbcWinInfo winInfo = null;
		// 系统庄
		if (isSystem) {
			// 下注金额
			Map<Integer, Double> carTotalBet = room.getCarBetTotalExcludeRobot(); // 单车下注金额,不包含机器人
			double totalBet = room.getTotalBetExcludeRobot(); // 总下注金额,不包含机器人
			// 计算每辆车中奖后的输赢值
			Map<String, Double> winCar = new HashMap<>(); // 赢的车
			Map<String, Double> loseCar = new HashMap<>(); // 输的车 
			if (carTotalBet.size() > 0) {
				for (Integer id : carTotalBet.keySet()) {
					Double carBet = carTotalBet.get(id);
					if (carBet == 0) continue;
					double win = totalBet - (CarTypeEnum.getLvbyId(room.getRoomId(),id) * carBet + carBet); 
					if (win > 0) {
						winCar.put(String.valueOf(id), win);
					} else if (win < 0) {
						loseCar.put(String.valueOf(id), win);
					}
				}
			}
			Map<String, Double> otherCar = getOtherCar(winCar,loseCar); // 没有输赢的车
			// 进行排序
			List<Entry<String, Double>> winCarList = mapSort(winCar);
			List<Entry<String, Double>> loseCarList = mapSort(loseCar);
			List<Entry<String, Double>> otherCarList = mapSort(otherCar);
			winCarList.forEach(v -> LOG.info("room = " + room.getRoomId() + " =====>" + "winCar" + v));
			loseCarList.forEach(v -> LOG.info("room = " + room.getRoomId() + " =====>" + "loseCar" + v));
			otherCarList.forEach(v -> LOG.info("room = " + room.getRoomId() + " =====>" + "otherCar" + v));
			//获取当前水位
			WaterPoolManager waterPoolManager = WaterPoolManager.instance();
			WaterPool pool = waterPoolManager.getPool();
			double currentnum = 0;
			if (pool != null) {
				currentnum = pool.getCurrentnum();
			}
			// 获取当前概率
			Double odds = getOdds(currentnum, waterPoolConfigDTOS);
			double random = RandomUtil.getRandom(1.0, 100.0); // 控制概率
			if (odds > 0d && random < odds) { // 控制系统赢
				if (winCarList.size() > 0) {
					// 随机取一个赢的结果
					winInfo = CarTypeEnum.getWinInfoByCarId(Integer.parseInt(winCarList.get(RandomUtil.getRandom(0, winCarList.size() - 1)).getKey()));
				} else {
					if (otherCarList.size() > 0) { // 如果没有赢的结果,就随机取一个输赢为0的结果
						winInfo = CarTypeEnum.getWinInfoByCarId(Integer.parseInt(otherCarList.get(RandomUtil.getRandom(0, otherCarList.size() - 1)).getKey()));
					} else { 
						if (loseCarList.size() > 0) { // 如果没有输赢为0的结果,就取一个输得最少的
							winInfo = CarTypeEnum.getWinInfoByCarId(Integer.parseInt(loseCarList.get(0).getKey()));
						} else {
							winInfo = CarTypeEnum.getWinInfoByWeight(); // 如果都没有就不做控制
						}
					}
				}
				LOG.info("room = " + room.getRoomId() + " =====>" + "currentnum : {} , " + "WaterControl : System Win!",currentnum);
			} else if (odds < 0d && random < Math.abs(odds)) { // 控制系统输
				if (loseCarList.size() > 0) {
					// 随机取一个输的结果
					winInfo = CarTypeEnum.getWinInfoByCarId(Integer.parseInt(loseCarList.get(RandomUtil.getRandom(0, loseCarList.size() - 1)).getKey()));
				} else {
					if (otherCarList.size() > 0) { // 如果没有输的结果,就随机取一个输赢为0的结果
						winInfo = CarTypeEnum.getWinInfoByCarId(Integer.parseInt(otherCarList.get(RandomUtil.getRandom(0, otherCarList.size() - 1)).getKey()));
					} else {
						if (winCarList.size() > 0) { // 如果没有输赢为0的结果,就取一个赢得最少的
							winInfo = CarTypeEnum.getWinInfoByCarId(Integer.parseInt(winCarList.get(winCarList.size() - 1).getKey()));
						} else {
							winInfo = CarTypeEnum.getWinInfoByWeight(); // 如果都没有就不做控制
						}
					}
				}
				LOG.info("room = " + room.getRoomId() + " =====>"  + "currentnum : {} , " + "WaterControl : System Lose!",currentnum );
			} else { // 不做控制
				winInfo = CarTypeEnum.getWinInfoByWeight();
				LOG.info("room = " + room.getRoomId() + " =====>"  + "currentnum : {} , " + "WaterControl :  Random!",currentnum);
			}
		} else { // 玩家庄
			Map<Integer, Double> carTotalBet = room.getCarBetTotalExcludePlayer(); // 单车下注金额,不包含真人玩家
			if (carTotalBet.size() > 0) {
				double totalBet = room.getTotalBetExcludePlayer(); // 总下注金额,不包真人玩家
				// 计算每辆车中奖后的输赢值
				Map<String, Double> winCar = new HashMap<>(); // 赢的车
				Map<String, Double> loseCar = new HashMap<>(); // 输的车 
				if (carTotalBet.size() > 0) {
					for (Integer id : carTotalBet.keySet()) {
						Double carBet = carTotalBet.get(id);
						if (carBet == 0) continue;
						double win = totalBet - (CarTypeEnum.getLvbyId(room.getRoomId(),id) * carBet + carBet); 
						if (win > 0) { // 大于0就为系统输
							loseCar.put(String.valueOf(id), win);
						} else if (win < 0) { // 大于0就为系统赢
							winCar.put(String.valueOf(id), win);
						}
					}
				}
				
				// 进行排序
				List<Entry<String, Double>> winCarList = mapSort(winCar);
				List<Entry<String, Double>> loseCarList = mapSort(loseCar);

				winCarList.forEach(v -> LOG.info("room = " + room.getRoomId() + " =====>" + "winCar" + v));
				loseCarList.forEach(v -> LOG.info("room = " + room.getRoomId() + " =====>" + "loseCar" + v));
				
				//获取当前水位
				WaterPoolManager waterPoolManager = WaterPoolManager.instance();
				WaterPool pool = waterPoolManager.getPool();
				double currentnum = 0;
				if (pool != null) {
					currentnum = pool.getCurrentnum();
				}
				LOG.info("room = " + room.getRoomId() + " =====>"  + "currentnum : {} ", currentnum );
				
				int winCarId = -1;
				if (currentnum > 0) { // 水位为正数就找出当前水位 - 输掉金额最接近0的车辆
					Entry<String, Double> entry = loseCarList.remove(0);
					double num = currentnum - entry.getValue();
					for (Entry<String, Double> e : loseCarList) {
						if (currentnum - e.getValue() > 0 && currentnum - e.getValue() < num) {
							entry = e;
						}
					}
					winCarId = Integer.valueOf(entry.getKey());
				} 
				
				if (currentnum < 0) { // 水位为负数就找出当前水位 + 赢取金额最接近0的车辆
					Entry<String, Double> entry = winCarList.remove(0);
					double num = currentnum + Math.abs(entry.getValue());
					for (Entry<String, Double> e : winCarList) {
						if (currentnum + Math.abs(e.getValue()) > 0 && currentnum + Math.abs(e.getValue()) < num) {
							entry = e;
						}
					}
					winCarId = Integer.valueOf(entry.getKey());
				}
				
				if (winCarId == -1) {
					winInfo = CarTypeEnum.getWinInfoByWeight();
					LOG.info("room = " + room.getRoomId() + " =====>"  + "winCarId = -1 ,randomCarId = {} , randomCarName = {} ,randomCarLV = {} ", winInfo.getCar(),winInfo.getName(),winInfo.getLv() );
				} else {
					winInfo = CarTypeEnum.getWinInfoByCarId(winCarId);
				}
			}
			winInfo = CarTypeEnum.getWinInfoByWeight(); // 全部为真人玩家就随机开奖
		}
		
		// 保证结果不为空
		if (winInfo == null) {
			winInfo = CarTypeEnum.getWinInfoByWeight();
		}
		
		// 保存记录
		/*try {
			WaterPoolManager.instance().updateWater();
			WaterPoolManager.instance().updateRecord(winInfo.getCar());
		} catch (Exception e) {
			e.printStackTrace();
			LOG.info("room = " + room.getRoomId() + " =====>" + "测试数据保存失败!" );
		}*/
		
		List<BmbcWinInfo> gameRecord = room.getGameRecord();
		if (gameRecord.size() < 20) {
			gameRecord.add(winInfo);
		} else {
			gameRecord.remove(0);
			gameRecord.add(winInfo);
		}
		
		// 回合id
		int roundIdNuber = id.incrementAndGet();
		String roundId = roomId + "|"+ roundIdNuber;
		if (roundIdNuber >= 999999999) {
			id.set(100000000);
		}
		
		// 结算
		String currentBanker = banker.getRoleid(); // 当前庄家
		Map<Integer, Map<String, List<Double>>> currentBetInfo = room.getCurrentBetInfo(); // 下注详情
		Map<String, Double> startBetGoldMap = room.getStartBetGoldMap(); // 玩家开始下注前的金币
		Map<String, Double> rewardMap = new HashMap<>(); // 奖励结果记录
		Map<String, Double> pumpMap = new HashMap<>(); // 玩家抽水记录
		
		double pumpRate = bairenFileConfigDTO.getPumpRate().doubleValue(); // 抽水比例
		if (pumpRate == 0) {
			pumpRate = D.DEFAULTPUMPRATE;
		}
		
		if (currentBetInfo.size() > 0) {
			// 获取胜利玩家
			Map<String, List<Double>> winMap = currentBetInfo.get(winInfo.getCar());
			LOG.info("roundCode = " + roundCode + " =====>" + "winMap : {}!",winMap);
			 // 系统庄结算
			if (currentBanker.equals(D.SYSTEMBANKER)) {
				// 派彩
				double totalPayout = 0; // 系统庄总赔付
				if (winMap != null) {
					for (String roleId : winMap.keySet()) {
						Role role = room.getRole(roleId);
						double playerBetTotal = winMap.get(roleId).stream().mapToDouble(Double::doubleValue).sum(); // 获胜车辆的单人总投注
		                // 赢得奖励 = 下注 x (赔率 + 下注)
						double reward = NumberTool.multiply(playerBetTotal, (winInfo.getLv() + 1)).doubleValue();
						double pumpMoney = NumberTool.multiply(NumberTool.subtract(reward, playerBetTotal),NumberTool.divide(pumpRate, 100)).doubleValue(); // 抽水金额
						double endReward = NumberTool.subtract(reward,pumpMoney).doubleValue(); // 最终奖励
						if (role != null) { // 排除机器人派彩
							userService.payout(roleId, endReward, roundId, roleMap.get(roleId).getUuid(),roomId);
							// 保存抽水信息
							pumpMap.put(roleId, pumpMoney);
			                redisTemplate.opsForValue().increment(RedisRegionConfig.FILE_PUMP_KEY + ":" + D.GAME_ID + "_" + room.getLevel(), pumpMoney);
			                rewardMap.put(room.getRole(roleId).getNickName(), endReward); // 保存奖励信息
			                LOG.info("roundCode = " + roundCode + " =====>" + "SystemBankerPayout :  roleId = {}, reward = {} , pumpMoney = {}, endReward = {} !",roleId,reward,pumpMoney,endReward);
						} else {
							rewardMap.put(room.getRobot(roleId).getNickname(), endReward); // 机器人保存奖励信息
						}
						totalPayout = NumberTool.add(totalPayout, endReward).doubleValue();
					}
				}
				double totalBetContainsRobot = room.getTotalBetContainsRobot(); // 总下注
				double totalReward = NumberTool.subtract(totalBetContainsRobot, totalPayout).doubleValue() ; // 赢赏为全场总投注 - 总赔付
				result.setBankerWinLoseGold(totalReward); // 设置系统庄输赢金币
			} else { // 玩家庄赔付赢赏
				// 赔付
				double totalPayout = 0; // 玩家庄总赔付
				if (winMap != null) {
					for (String roleId : winMap.keySet()) { // 庄家赔付
						double playerBetTotal = winMap.get(roleId).stream().mapToDouble(Double::doubleValue).sum(); // 获胜车辆的单人总投注
						double payout = NumberTool.multiply(playerBetTotal, (winInfo.getLv() + 1)).doubleValue(); // 应赔付金额
						totalPayout = NumberTool.add(totalPayout, payout).doubleValue();
						userService.bet(currentBanker, payout, roleMap.get(currentBanker).getUuid(),roomId); // 庄家进行赔付
						LOG.info("roundCode = " + roundCode + " =====>" + "PlayerBankerBet :  roleId = {}, bet = {}  !",currentBanker,payout);
						// 玩家赢赏
						double pumpMoney = NumberTool.multiply(NumberTool.subtract(payout, playerBetTotal), NumberTool.divide(pumpRate, 100)).doubleValue(); // 抽水金额
						double reward = NumberTool.subtract(payout, pumpMoney).doubleValue(); // 最终获取奖励
						String uuid = D.UUID;
						String nickName = "";
						Role role = roleMap.get(roleId);
						if (role != null) {
							uuid = role.getUuid();
							nickName = role.getNickName();
						} else {
							BaseRobot robot = room.getRobot(roleId);
							nickName = robot.getNickname();
						}
						userService.payout(roleId, reward, roundId, uuid,roomId); // 玩家获取赔付
						LOG.info("roundCode = " + roundCode + " =====>" + "PlayerBankerPayout :  roleId = {}, reward = {} , pumpMoney = {}, endReward = {} !",roleId,payout,pumpMoney,reward);
						// 保存抽水信息
						pumpMap.put(roleId, pumpMoney);
		                redisTemplate.opsForValue().increment(RedisRegionConfig.FILE_PUMP_KEY + ":" + D.GAME_ID + "_" + room.getLevel(), pumpMoney);
		                rewardMap.put(nickName, reward);
					}
				}
				// 玩家庄赢赏
				double totalBetContainsRobot = room.getTotalBetContainsRobot(); // 玩家庄赢赏=总下注
				if (totalBetContainsRobot <= 0) {
					totalBetContainsRobot = room.getTotalBet();
				}
				
				double winLoseGold = NumberTool.subtract(totalBetContainsRobot, totalPayout).doubleValue(); // 玩家庄输赢金额
				double pumpMoney = 0d;
				if (winLoseGold > 0) { // 玩家庄赢钱就抽水
					pumpMoney = NumberTool.multiply(winLoseGold, NumberTool.divide(pumpRate, 100)).doubleValue(); // 抽水金额
					totalBetContainsRobot =  NumberTool.subtract(totalBetContainsRobot, pumpMoney).doubleValue(); // 实际赢赏金额
					winLoseGold = NumberTool.subtract(winLoseGold, pumpMoney).doubleValue(); // 实际输赢金额
	                pumpMap.put(currentBanker, pumpMoney);
	                redisTemplate.opsForValue().increment(RedisRegionConfig.FILE_PUMP_KEY + ":" + D.GAME_ID + "_" + room.getLevel(), pumpMoney);
				}
				userService.payout(currentBanker, totalBetContainsRobot, roundId, roleMap.get(currentBanker).getUuid(),roomId);
				result.setBankerWinLoseGold(winLoseGold); // 设置玩家庄输赢金币
				LOG.info("roundCode = " + roundCode + " =====>" + "PlayerBankerInfo :    roleId = {}, totalReward = {}, totalPayout = {} , winLoseGold = {}, pumpMoney = {} !",currentBanker,totalBetContainsRobot,totalPayout,winLoseGold,pumpMoney);
			}
		}
		// 奖励排序
		List<Entry<String, Double>> rewardSort = mapSort(rewardMap);
		result.setGameRecord(gameRecord);
		result.setBmbcWinInfo(winInfo);
		result.setRewardSort(rewardSort);
		if (rewardSort.size() > 0) {
			result.setHighRewardName(rewardSort.get(0).getKey());
			result.setHighRewardGold(rewardSort.get(0).getValue());
		}
		// 通知所有玩家游戏结果
		Banker banker1 = room.getBanker();
		if (!banker1.getRoleid().equals(D.SYSTEMBANKER) ) {
			banker1.setGold(room.getRoleMap().get(banker1.getRoleid()).getGold());
		}
		if (room.getOnlineList().size() > 0) {
			for (ChannelHandlerContext ctxs : room.getOnlineList().keySet()) {
				if (ctxs == null) continue;
				String roleId = room.getOnlineList().get(ctxs);
				Role role = room.getRoleMap().get(roleId);
				if (role == null) continue;
				result.setRoleId(roleId);
				result.setBet(room.getPlayerBetTotal(roleId));
				result.setGold(role.getGold());
				if (startBetGoldMap.containsKey(roleId)) {
					result.setWinLoseGold(result.getGold() - startBetGoldMap.get(roleId));
				} else {
					result.setWinLoseGold(0);
				}
				result.setBanker1(banker1);
				ctxs.writeAndFlush(ByteHelper.createFrameMessage
						(JsonUtil.create().cmd("GameService").put("roomId", room.getRoomId()).put("result", result).toJsonString()));
			}
		}
		LOG.info("roundCode = " + roundCode + " =====>" + "GameResult : {}!",result.getBmbcWinInfo());
		room.getLastGameResult().put(1, result);
		buildLog(room, winInfo, startBetGoldMap, pumpRate,pumpMap,roundCode,currentBanker,result);
	}
	
	/**
	 * 构建日志
	 * @param room
	 * @param winInfo
	 * @param startBetGoldMap
	 * @param pumpRate
	 * @param currentBanker 
	 * @param result 
	 * @throws Exception 
	 */
	private void buildLog(Room room, BmbcWinInfo winInfo, Map<String, Double> startBetGoldMap, double pumpRate,
		Map<String, Double> pumpMap,String roundCode, String currentBanker, mainGameProcessResult result) throws Exception {
		// 日志记录
		MQProducer mqProducer = SpringContextUtil.getBean(DefautProducer.class);
		Set<String> betPlayer = room.getBetPlayer(); // 下注的玩家
		boolean isPlayerBanker = false; // 是否为玩家庄
		if (!D.SYSTEMBANKER.equals(currentBanker)) {
			betPlayer.add(currentBanker); // 添加庄家
			isPlayerBanker = true;
		}
		if (betPlayer.size() > 0) {
			for (String roleId : betPlayer) {
				// 是否为机器人
				boolean isRobot = false;
				Role role = room.getRole(roleId);
				if (role == null) {
					isRobot = true;
				}
				if (isRobot && !isPlayerBanker) continue;
				// 下注前金币 
				double startbalance = 0d;
				if (startBetGoldMap.containsKey(roleId)) {
					startbalance = startBetGoldMap.get(roleId);
				}
				String nickName = "";
				// 本局的输赢金币
				double change = 0d;
				double afterGameGold = 0d;
				if (role != null) { // 真实用户
					afterGameGold = role.getGold();
					nickName = role.getNickName();
					change = NumberTool.subtract(afterGameGold, startbalance).doubleValue(); // 本局的输赢
				} else { // 机器人
					BaseRobot robot = room.getRobot(roleId);
					afterGameGold = robot.getGold().doubleValue();
					nickName = robot.getNickname();
					change = NumberTool.subtract(afterGameGold, startbalance).doubleValue(); // 本局的输赢
				}
				// 服务费
				double serviceCharge = 0d;
				if (pumpMap.containsKey(roleId)) {
					serviceCharge = pumpMap.get(roleId);
				}
				ArrayList<PlayerRecordDTO> playerWinLoseInfo = getPlayerWinLoseInfo(roleId,room,winInfo,pumpRate,result); // 玩家的输赢信息
					
				GameRecordDTO<PlayerRecordDTO> record = GameRecordDTO.<PlayerRecordDTO>builder()
						.gameDate(new Date(System.currentTimeMillis()))
						.userId(Long.parseLong(roleId))
						.userName(nickName)
						.gameId(Integer.valueOf(D.GAME_ID))
						.gameName(D.GAMENAME)
						.fileId(1)
						.fileName("平民场")
						.gameResult(playerWinLoseInfo)
						.beforeGameGold(BigDecimal.valueOf(startbalance))
						.afterGameGold(BigDecimal.valueOf(afterGameGold))
						.betsGold(BigDecimal.valueOf(room.getPlayerBetTotal(roleId)))
						.winLosGold(BigDecimal.valueOf(change))
						.serviceCharge(BigDecimal.valueOf(serviceCharge))
						.isRobot(isRobot)
						.roundCode(roundCode)
						.build();
				//LOG.info("record =====> " + record);
				mqProducer.sendAsync(JSONObject.toJSONString(record));
			}
		}
	}
	
	/**
	 * 将map的value值从大到小排序
	 */
	private List<Entry<String, Double>> mapSort(Map<String, Double> map){
		// 排序
		List<Map.Entry<String, Double>> sortMap = new ArrayList<>(map.entrySet());
		Collections.sort(sortMap, new Comparator<Map.Entry<String, Double>>() {//根据value排序
			@Override
			public int compare(Map.Entry<String, Double> o1,
					Map.Entry<String, Double> o2) {
				double result = o2.getValue() - o1.getValue();
				if(result > 0)
					return 1;
				else if(result == 0)
					return 0;
				else 
					return -1;
			}
		});
		return sortMap;
	}
	
	/**
	 * 获取控制概率
	 * @param num 当前水位值
	 * @return 
	 */
	private Double getOdds(double num, List<WaterPoolConfigDTO> waterConfigs){
		double controlRate = 0;
		if (waterConfigs.size() > 0) {
			for (WaterPoolConfigDTO waterConfig : waterConfigs) {
				if (num >= waterConfig.getWaterLow() && num < waterConfig.getWaterHigh()) {
					controlRate = waterConfig.getControlExecuteRate();
				}
			}
		}
		return controlRate;
	}
	
	/**
	 * 计算没有输赢的car
	 * @return 
	 */
	private Map<String, Double> getOtherCar(Map<String, Double> winCar, Map<String, Double> loseCar){
		List<String> carlist = Lists.newArrayList("1","2","3","4","5","6","7","8");
		Iterator<String> iterator1 = carlist.iterator();
		while (iterator1.hasNext()) {
			String next = iterator1.next();
			for (String car : winCar.keySet()) {
				if (next.equals(car)) {
					iterator1.remove();
					break;
				}
			}
		}
		
		Iterator<String> iterator2 = carlist.iterator();
		while (iterator2.hasNext()) {
			String next = iterator2.next();
			for (String car : loseCar.keySet()) {
				if (next.equals(car)) {
					iterator2.remove();
					break;
				}
			}
		}
		Map<String, Double> otherCar = new HashMap<>();
		if (carlist.size() > 0) {
			carlist.forEach(v -> otherCar.put(v, 0d));
		}
		return otherCar;
	}
	
	/**
	 *  获取玩家输赢信息
	 */
	/**
	 * @param roleId 角色id
	 * @param room 房间
	 * @param winInfo 胜利信息
	 * @param pumpRate 抽水比例
	 * @param result 
	 * @return 
	 */
	private ArrayList<PlayerRecordDTO> getPlayerWinLoseInfo(String roleId,Room room,BmbcWinInfo winInfo,double pumpRate, mainGameProcessResult result){
		ArrayList<PlayerRecordDTO> newArrayList = Lists.newArrayList();
		for (RecordEnum e : RecordEnum.values()) {
			e.setLv(CarTypeEnum.getLvbyId(room.getRoomId(), e.getCar()));
			PlayerRecordDTO pr = new PlayerRecordDTO();
			pr.setCar(e.getCar());
			pr.setName(e.getName());
			pr.setLv(e.getLv());
			pr.setBet(e.getBet());
			pr.setWinLoseGold(e.getWinLoseGold());
			pr.setWinCar(e.getWinCar());
			pr.setBankerName(room.getBanker().getNickName());
			newArrayList.add(pr);
		}
		
		Map<Integer, Double> playerBetInfo = room.getPlayerBetInfo(roleId); // 玩家本局的投注信息
		if (playerBetInfo.size() > 0) { // 有下注信息的是玩家
			for (Integer car : playerBetInfo.keySet()) {
				if (car == winInfo.getCar()) {
					double bet = playerBetInfo.get(car); // 下注金额
					double reward = NumberTool.multiply(bet, NumberTool.add(winInfo.getLv(), 1)).doubleValue(); // 赢得奖励=下注*(赔率+下注)
					double pumpMoney = NumberTool.multiply(NumberTool.subtract(reward, bet), NumberTool.divide(pumpRate, 100)).doubleValue(); // 抽水金额
					double endReward = NumberTool.subtract(reward, pumpMoney).doubleValue(); // 最终的奖励=赢得奖励 - 抽水
					for (PlayerRecordDTO re : newArrayList) {
						if (re.getCar() == car) {
							re.setBet(bet);
							re.setWinLoseGold(endReward);
							re.setWinCar(1);
							break;
						} 
					}
				} else {
					double bet = playerBetInfo.get(car); // 下注金额
					for (PlayerRecordDTO re : newArrayList) {
						if (re.getCar() == car) {
							re.setBet(bet);
							re.setWinLoseGold(-bet);
							break;
						} 
					}
				}
			}
		} else { // 无下注信息的是庄家
			Map<Integer, Double> carBetInfo = room.getCarBetTotalContainsRobot();
			for (PlayerRecordDTO dto : newArrayList) {
				if (dto.getCar() == winInfo.getCar()) {
					dto.setWinCar(1);
					dto.setBet(carBetInfo.get(dto.getCar()));
					dto.setWinLoseGold(result.getBankerWinLoseGold());
				} else {
					Double bet = carBetInfo.get(dto.getCar());
					if (bet != null && bet > 0) {
						dto.setBet(bet);
						dto.setWinLoseGold(NumberTool.multiply(bet, NumberTool.subtract(1, NumberTool.divide(pumpRate, 100))).doubleValue());
					}
				}
			}
		}
		return newArrayList;
	}
	
}
