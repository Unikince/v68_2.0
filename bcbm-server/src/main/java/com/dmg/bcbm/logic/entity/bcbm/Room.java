package com.dmg.bcbm.logic.entity.bcbm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dmg.bcbm.SpringContextUtil;
import com.dmg.bcbm.core.abs.role.Role;
import com.dmg.bcbm.core.config.D;
import com.dmg.bcbm.core.manager.ServiceManager;
import com.dmg.bcbm.core.manager.StateTimerManager;
import com.dmg.bcbm.logic.entity.SimplePlayer;
import com.dmg.bcbm.logic.service.RobotCacheService;
import com.dmg.bcbm.logic.service.UserService;
import com.dmg.gameconfigserverapi.dto.BairenFileConfigDTO;
import com.dmg.gameconfigserverapi.dto.BairenGameConfigDTO;
import com.dmg.gameconfigserverapi.feign.GameConfigService;
import com.google.common.collect.Lists;
import com.zyhy.common_server.util.RandomUtil;
import com.zyhy.common_server.util.StringUtils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ConcurrentSet;
import lombok.Getter;
import lombok.Setter;
import util.ByteHelper;
import util.json.JsonUtil;

/**
 * @author zhuqd
 * @Date 2017年7月24日
 */
/**
 * @author Administrator
 *
 */
public class Room extends BaseRoom {
	private static Logger logger = LoggerFactory.getLogger(Room.class);
	private RobotCacheService robotCacheService = ServiceManager.instance().get(RobotCacheService.class);
	@Setter
	private GameConfigService gameConfigService;
	// 当前庄家
	private Banker banker = new Banker();
	// 申请上庄列表
	private List<String> bankerList = new CopyOnWriteArrayList<>();
	// 在线玩家列表(K:玩家通讯信息 V: roleId)
	private Map<ChannelHandlerContext, String> onlineList = new ConcurrentHashMap<>();
	// 保存玩家信息(K:roleId V: 角色详细信息)
	private Map<String, Role> roleMap = new ConcurrentHashMap<>();
	// 机器人列表
	private Map<String, BaseRobot> robotList = new ConcurrentHashMap<>();
	// 游戏记录
	private List<BmbcWinInfo> gameRecord = new CopyOnWriteArrayList<>();
	// 游戏配置信息
	@Getter
	private Map<Integer, BairenGameConfigDTO> gameConfig = new ConcurrentHashMap<>(); //单个玩家的总下注金额
	// 下注信息
	private double totalBet = 0; //当局的总下注金额	
	@Getter // 当局下注信息
	private Map<Integer, Map<String, List<Double>>> currentBetInfo = new ConcurrentHashMap<>(); //Map<车辆, Map<下注角色, <每次下注筹码>>>
	@Getter // 上一局下注信息
	private Map<Integer, Map<String, List<Double>>> lastBetInfo = new ConcurrentHashMap<>();
	@Getter // 上一局游戏结果
	private Map<Integer, mainGameProcessResult> lastGameResult = new ConcurrentHashMap<>();
	@Getter
	Map<String, Double> startBetGoldMap = new ConcurrentHashMap<>(); // 玩家开始下注前的金币
	
	/**
	 * 投注
	 */
	public void bet(int car, double bet, String roleId){
		if (currentBetInfo.containsKey(car)) {
			Map<String, List<Double>> map = currentBetInfo.get(car);
			if (map.containsKey(roleId)) {
				map.get(roleId).add(bet);
			} else {
				List<Double> list = new CopyOnWriteArrayList<>();
				list.add(bet);
				map.put(roleId, list);
			}
		} else {
			Map<String, List<Double>> map = new ConcurrentHashMap<>();
			List<Double> list = new CopyOnWriteArrayList<>();
			list.add(bet);
			map.put(roleId, list);
			currentBetInfo.put(car, map);
		}
	}
	
	/**
	 * 获取单人投注总额
	 * @return 
	 */
	public double getPlayerBetTotal(String roleId){
		double total = 0;
		if (currentBetInfo.size() > 0) {
			for (Map<String, List<Double>> infos : currentBetInfo.values()) {
				if (infos.containsKey(roleId)) {
					total += infos.get(roleId).stream().mapToDouble(Double::doubleValue).sum();
				}
			}
		}
		return total;
	}
	
	/**
	 * 获取单人投注详情
	 * @return 
	 */
	public Map<Integer, Double> getPlayerBetInfo(String roleId){
		Map<Integer, Double> infos = new ConcurrentHashMap<>();
		if (currentBetInfo.size() > 0) {
			for (Integer car : currentBetInfo.keySet()) {
				Map<String, List<Double>> carMap = currentBetInfo.get(car);
				if (carMap.containsKey(roleId)) {
					List<Double> betList = carMap.get(roleId);
					infos.put(car, betList.stream().mapToDouble(Double::doubleValue).sum());
				}
			}
		}
		return infos;
	}
	
	
	/**
	 * 获取本局总投注,排除机器人
	 * @return
	 */
	public double getTotalBetExcludeRobot(){
		double totalBet = 0;
		Map<Integer, Double> carBetTotal = getCarBetTotalExcludeRobot();
		if (carBetTotal.size() > 0) {
			for (Double bet : carBetTotal.values()) {
				totalBet += bet;
			}
		}
		return totalBet;
	}
	
	/**
	 * 获取本局总投注,排除真人玩家
	 * @return
	 */
	public double getTotalBetExcludePlayer(){
		double totalBet = 0;
		Map<Integer, Double> carBetTotal = getCarBetTotalExcludePlayer();
		if (carBetTotal.size() > 0) {
			for (Double bet : carBetTotal.values()) {
				totalBet += bet;
			}
		}
		return totalBet;
	}
	
	/**
	 * 获取本局总投注,包含机器人
	 * @return
	 */
	public double getTotalBetContainsRobot(){
		double totalBet = 0;
		Map<Integer, Double> carBetTotal = getCarBetTotalContainsRobot();
		if (carBetTotal.size() > 0) {
			for (Double bet : carBetTotal.values()) {
				totalBet += bet;
			}
		}
		return totalBet;
	}
	
	/**
	 * 获取单个车的投注总额,排除机器人
	 * @return 
	 */
	public Map<Integer, Double> getCarBetTotalExcludeRobot(){
		Map<Integer, Double> carTotalBet = new ConcurrentHashMap<>();
		if (currentBetInfo.size() > 0) {
			for (Integer car : currentBetInfo.keySet()) {
				Map<String, List<Double>> map = currentBetInfo.get(car);
				double total = 0;
				for (String roleId : map.keySet()) {
					if (getRole(roleId) == null) continue;  // 排除掉机器人
					List<Double> bets = map.get(roleId);
					total += bets.stream().mapToDouble(Double::doubleValue).sum();
				}
				carTotalBet.put(car, total);
			}
		}
		return carTotalBet;
	}
	
	/**
	 * 获取单个车的投注总额,排除真人玩家
	 * @return 
	 */
	public Map<Integer, Double> getCarBetTotalExcludePlayer(){
		Map<Integer, Double> carTotalBet = new ConcurrentHashMap<>();
		if (currentBetInfo.size() > 0) {
			for (Integer car : currentBetInfo.keySet()) {
				Map<String, List<Double>> map = currentBetInfo.get(car);
				double total = 0;
				for (String roleId : map.keySet()) {
					if (getRole(roleId) != null) continue;  // 排除真人玩家
					List<Double> bets = map.get(roleId);
					total += bets.stream().mapToDouble(Double::doubleValue).sum();
				}
				carTotalBet.put(car, total);
			}
		}
		return carTotalBet;
	}
	
	/**
	 * 获取单个车的投注总额,包含机器人
	 * @return 
	 */
	public Map<Integer, Double> getCarBetTotalContainsRobot(){
		Map<Integer, Double> carTotalBet = new ConcurrentHashMap<>();
		if (currentBetInfo.size() > 0) {
			for (Integer car : currentBetInfo.keySet()) {
				Map<String, List<Double>> map = currentBetInfo.get(car);
				double total = 0;
				for (String roleId : map.keySet()) {
					List<Double> bets = map.get(roleId);
					total += bets.stream().mapToDouble(Double::doubleValue).sum();
				}
				carTotalBet.put(car, total);
			}
		}
		return carTotalBet;
	}
	
	/**
	 * 获取当前台红值
	 * @return 
	 */
	public double getCurrentRedValue(){
		Map<Integer, Double> carBetTotal = getCarBetTotalContainsRobot();
		double totalRedValue = 0;
		for (Integer car : carBetTotal.keySet()) {
			totalRedValue += carBetTotal.get(car) * CarTypeEnum.getLvbyId(this.getRoomId(),car);
		}
		return totalRedValue;
	}
	
	
	/**
	 * 获取车辆的每一次单次投注
	 * @return 
	 */
	public Map<Integer, List<Double>> getCarSingleBet(){
		Map<Integer, List<Double>> carBet = new ConcurrentHashMap<>();
		if (currentBetInfo.size() > 0) {
			for (Integer car : currentBetInfo.keySet()) {
				Map<String, List<Double>> map = currentBetInfo.get(car);
				List<Double> list = new CopyOnWriteArrayList<>();
				for (List<Double> bets : map.values()) {
					list.addAll(bets);
				}
				carBet.put(car, list);
			}
		}
		return carBet;
	}
	
	/**
	 * 获取车辆的每一次单次投注级别
	 * @return 
	 */
	public Map<Integer, List<Integer>> getCarSingleBetForLevel(){
		Map<Integer, List<Integer>> carBet = new ConcurrentHashMap<>();
		if (currentBetInfo.size() > 0) {
			BairenGameConfigDTO gameConfig = getGameConfig().get(1); // 获取游戏配置
			BairenFileConfigDTO bairenFileConfigDTO = gameConfig.getBairenFileConfigDTO(); // 场次配置
			
			// 获取赌注列表
			String betChips = bairenFileConfigDTO.getBetChips();
			String[] split = betChips.split("\\,");
			List<Double> betList = new CopyOnWriteArrayList<>();
			for (String b : split) {
				betList.add(Double.valueOf(b));
			}
			
			for (Integer car : currentBetInfo.keySet()) {
				Map<String, List<Double>> map = currentBetInfo.get(car);
				List<Integer> list = new CopyOnWriteArrayList<>();
				for (List<Double> bets : map.values()) {
					bets.forEach(v -> {
						list.add(betList.indexOf(v));
					});
				}
				carBet.put(car, list);
			}
		}
		return carBet;
	}
	
	/**
	 * 保存上一局游戏结果
	 * @param result
	 */
	public void saveLastGameResult(mainGameProcessResult result){
		this.lastGameResult.put(1, result);
	}
	/**
	 * 保存上一局所有投注信息
	 */
	public void saveBetInfo(){
		if (currentBetInfo.size() > 0) {
			for (Integer car : currentBetInfo.keySet()) {
				lastBetInfo.put(car, currentBetInfo.remove(car));
			}
		}
	}
	
	/**
	 * 保存玩家下注之前的金币
	 * @return 
	 */
	public void savePlayerBeforeBetinfo(){
		startBetGoldMap.clear();
		for (Role role : this.getRoleMap().values()) {
			startBetGoldMap.put(role.getRoleId(), role.getGold());
		}
		for (BaseRobot robot : this.robotList.values()) {
			startBetGoldMap.put(robot.getRoleId(), robot.getGold().doubleValue());
		}
	}
	
	/**
	 * 获取玩家上一局投注信息
	 * @return 
	 */
	public UserBetInfo getPlayerLastBetInfo(String roleid){
		if (lastBetInfo.size() > 0) {
			Map<Integer, List<Double>> infos = new ConcurrentHashMap<>();
			for (Integer car : lastBetInfo.keySet()) {
				Map<String, List<Double>> map = lastBetInfo.get(car);
				if (map.containsKey(roleid)) {
					infos.put(car, map.get(roleid));
				}
			}
			
			if (infos.size() > 0) {
				double total = 0;
				for (List<Double> bets : infos.values()) {
					total += bets.stream().mapToDouble(Double::doubleValue).sum();
				}
				UserBetInfo info = new UserBetInfo();
				info.setRoleid(roleid);
				info.setTotalBet(total);
				info.setBetinfo(infos);
				return info;
			}
		}
		return null;
	}
	
	/**
	 * 获取玩家当前的投注信息
	 * @return 
	 */
	public UserBetInfo getPlayerCurrentBetInfo(String roleid){
		if (currentBetInfo.size() > 0) {
			Map<Integer, List<Double>> infos = new ConcurrentHashMap<>();
			for (Integer car : currentBetInfo.keySet()) {
				Map<String, List<Double>> map = currentBetInfo.get(car);
				if (map.containsKey(roleid)) {
					double betSum = map.get(roleid).stream().mapToDouble(Double::doubleValue).sum();
					ArrayList<Double> newArrayList = Lists.newArrayList(betSum);
					infos.put(car, newArrayList);
				}
			}
			
			if (infos.size() > 0) {
				UserBetInfo info = new UserBetInfo();
				info.setRoleid(roleid);
				info.setBetinfo(infos);
				return info;
			}
		}
		return null;
	}
	
	/**
	 * 获取所有玩家上一局投注信息
	 * @return 
	 */
	public Map<String, UserBetInfo> getAllPlayerLastBetInfo(){
		Map<String, UserBetInfo> betInfo = new ConcurrentHashMap<>();
		if (lastBetInfo.size() > 0) {
			Set<String> betPlayer = getBetPlayer();
			if (betPlayer.size() > 0) {
				for (String roleId : betPlayer) {
					UserBetInfo info = getPlayerLastBetInfo(roleId);
					if (info != null) {
						betInfo.put(roleId, info);
					}
				}
			}
		}
		return betInfo;
	}
	
	/**
	 * 获取所有投注的玩家
	 * @return 
	 */
	public Set<String> getBetPlayer(){
		Set<String> set = new CopyOnWriteArraySet<>();
		if (currentBetInfo.size() > 0) {
			for (Map<String, List<Double>> infos : currentBetInfo.values()) {
				for (String roleId : infos.keySet()) {
					set.add(roleId);
				}
			}
		}
		return set;
	}
	
	/**
	 * 删除指定玩家的本局下注信息
	 */
	public void deleteBetInfoByUserId(String roleId){
		for (Map<String, List<Double>> map : currentBetInfo.values()) {
			map.remove(roleId);
		}
	}
	
	/**
	 * 清除下注缓存数据
	 */
	public void cleanBetInfo(){
		totalBet = 0;
		lastBetInfo.clear();
	}

	/**
	 * 添加玩家
	 * 
	 * @param role
	 */
	public void addRole(Role role) {
		roleMap.put(role.getRoleId(), role);
	}
	
	/**
	 * 获取玩家
	 * 
	 * @param roleId
	 * @return
	 */
	public Role getRole(String roleId) {
		return roleMap.get(roleId);
	}
	
	/**
	 * 获取玩家的通信信息
	 * 
	 * @param roleId
	 * @return
	 */
	public ChannelHandlerContext getChannel(String roleId) {
		Role role = roleMap.get(roleId);
		if (role == null) {
			return null;
		}
		return role.getCtx();
	}

	/**
	 * 添加玩家通信信息
	 * 
	 * @param roleId
	 * @param ctx
	 */
	public void addRoleChannel(ChannelHandlerContext ctx, String roleId) {
		if (onlineList.size() > 0) {
			for (ChannelHandlerContext context : onlineList.keySet()) {
				if (roleId.equals(onlineList.get(context))) {
					onlineList.remove(context);
				}
			}
		}
		onlineList.put(ctx, roleId);
	}
	
	/**
	 * 获取玩家id
	 * @param ctx
	 * @return
	 */
	public String getRoleId(ChannelHandlerContext ctx) {
		return onlineList.get(ctx);
	}
	
	/**
	 * 当前在线的玩家
	 * 
	 * @param roleId
	 * @return
	 */
	public int getChannelTotal() {
		return onlineList.size();
	}
	
	/**
	 * 当前在线的玩家(包括机器人)
	 * 
	 * @param roleId
	 * @return
	 */
	public int getTotalNum() {
		return getChannelTotal() + getRobotNum();
	}
	/**
	 * 玩家断线操作
	 * 
	 * @param ctx
	 */
	public void removeChannel(ChannelHandlerContext ctx) {
		UserService userService = ServiceManager.instance().get(UserService.class);
		String roleId = onlineList.get(ctx);
		if (StringUtils.isEmpty(roleId)) {
			userService.syncRoomFromExit(Long.parseLong(roleId));
			return;
		}
		// 移除通讯信息
		onlineList.remove(ctx);
		// 从庄家排队列表移除
		bankerList.remove(roleId);
		// 设置角色状态
		Role role = roleMap.get(roleId);
		role.setActive(false);
		role.setLastActiveTime(System.currentTimeMillis());
		// 如果为庄家,下注状态下直接下庄
		if (roleId.equals(banker.getRoleid())) {
			int state = StateTimerManager.instance().getTimer(this.getRoomId()).getState();
			if (state == 1) {
				if (bankerList.size() > 0) {
					// 玩家上庄
					String roleid = bankerList.remove(0);
					Role role2 = getRole(roleid);
					banker.setRoleid(roleid);
					banker.setNickName(role2.getNickName());
					banker.setGold(role2.getGold());
					banker.setCount(0);
				} else {
					// 系统上庄
					banker.setRoleid(D.SYSTEMBANKER);
					banker.setNickName(D.SYSTEMBANKER);
					banker.setCount(0);
					banker.setGold(D.SYSTEMBANKERGOLD);
				}
				// 通知玩家庄家掉线
				for (ChannelHandlerContext ctxs : onlineList.keySet()) {
					ctxs.writeAndFlush(ByteHelper.createFrameMessage
							(JsonUtil.create().cmd("removeChannel").put("roomId", this.getRoomId()).errorCode("庄家掉线").put("currentBanker", banker).toJsonString()));
				}
			}
		}
		userService.syncRoomFromExit(Long.parseLong(roleId));
		logger.info("room = " + this.getRoomId() + " =====>" + "玩家{}退出房间,当前房间人数为: {}人" , roleId , getChannelTotal());
	}
	
	/**
	 * 添加机器人
	 * @param num
	 */
	public void addRobot(int num){
		try {
			if (num <= 0) {
				return;
			}
			for (int i = 0; i < num; i++) {
				int userId = RandomUtil.getRandom(0, 300);
		        BaseRobot robot = this.robotCacheService.getRobot(this.getRoomId(),userId + 101000);
				robotList.put(String.valueOf(robot.getRoleId()), robot);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("room = " + this.getRoomId() + " =====>" + "添加机器人失败!");
		}
		
	}
	
	/**
	 * 获取机器人数量
	 * @return
	 */
	public int getRobotNum(){
		return robotList.size();
	}
	
	/**
	 * 获取机器人
	 * @return
	 */
	public BaseRobot getRobot(String robotId){
		return robotList.get(robotId);
	}
	
	/**
	 * 删除机器人
	 * @param num 
	 */
	public void delRobot(int num){
		Set<String> keySet = robotList.keySet();
		Iterator<String> iterator = keySet.iterator();
		for (int i = 0; i < num; i++) {
			if (iterator.hasNext()) {
				iterator.next();
				iterator.remove();
			}
		}
	}
	
	/**
	 * 删除机器人
	 * @param num 
	 */
	public void delRobot(String roleId){
		robotList.remove(roleId);
	}
	
	/**
	 * 更新游戏的配置
	 */
	public void updateGameconfig(){
		try {
			if (gameConfigService == null) {
				gameConfigService = SpringContextUtil.getBean(GameConfigService.class);
			}
			List<BairenGameConfigDTO> bairenGameConfigData = gameConfigService.getBairenGameConfigData(D.GAME_ID);
			for (BairenGameConfigDTO dto : bairenGameConfigData) {
				this.gameConfig.put(dto.getFileId(), dto);
				logger.debug("room: {} ,updateGameConfig {}",this.getRoomId(),dto);
			}
			//logger.info("room: {} ,updateGameConfig =====> success!",this.getRoomId());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("room: {} ,updateGameConfig =====> failed!",this.getRoomId());
		}
	}
	
	public Map<String, Role> getRoleMap() {
		return roleMap;
	}
	public void setRoleMap(ConcurrentHashMap<String, Role> roleMap) {
		this.roleMap = roleMap;
	}

	public Banker getBanker() {
		return banker;
	}

	public void setBanker(Banker banker) {
		this.banker = banker;
	}

	public List<String> getBankerList() {
		return bankerList;
	}
	public void setBankerList(List<String> bankerList) {
		this.bankerList = bankerList;
	}


	public List<BmbcWinInfo> getGameRecord() {
		return gameRecord;
	}
	public void setGameRecord(List<BmbcWinInfo> gameRecord) {
		this.gameRecord = gameRecord;
	}
	public double getTotalBet() {
		return totalBet;
	}
	public void setTotalBet(double totalBet) {
		this.totalBet = totalBet;
	}


	public Map<ChannelHandlerContext, String> getOnlineList() {
		return onlineList;
	}

	public void setOnlineList(ConcurrentHashMap<ChannelHandlerContext, String> onlineList) {
		this.onlineList = onlineList;
	}

	public Map<String, BaseRobot> getRobotList() {
		return robotList;
	}

	public void setRobotList(Map<String, BaseRobot> robotList) {
		this.robotList = robotList;
	}
	
}
