package com.dmg.bcbm.core.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dmg.bcbm.SpringContextUtil;
import com.dmg.bcbm.core.abs.role.Role;
import com.dmg.bcbm.core.config.D;
import com.dmg.bcbm.logic.entity.bcbm.Banker;
import com.dmg.bcbm.logic.entity.bcbm.BaseRobot;
import com.dmg.bcbm.logic.entity.bcbm.BmbcWinInfo;
import com.dmg.bcbm.logic.entity.bcbm.Room;
import com.dmg.bcbm.logic.entity.bcbm.UserBetInfo;
import com.dmg.bcbm.logic.service.RobotCacheService;
import com.dmg.gameconfigserverapi.dto.BairenGameConfigDTO;
import com.dmg.gameconfigserverapi.feign.GameConfigService;
import com.zyhy.common_server.util.RandomUtil;
import com.zyhy.common_server.util.StringUtils;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
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
public class RoleManager {

	private static RoleManager instance = new RoleManager();
	 private static Logger logger = LoggerFactory.getLogger(RoleManager.class);
	// 当前庄家
	private Banker banker = new Banker();
	// 申请上庄列表
	private List<String> bankerList = new ArrayList<>();
	// 在线玩家列表(K:玩家通讯信息 V: roleId)
	private ConcurrentHashMap<ChannelHandlerContext, String> onlineList = new ConcurrentHashMap<>();
	// 保存玩家信息(K:roleId V: 角色详细信息)
	private ConcurrentHashMap<String, Role> roleMap = new ConcurrentHashMap<>();
	// 机器人列表
	private Map<String, BaseRobot> robotList = new HashMap<>();
	// 游戏记录
	private List<BmbcWinInfo> gameRecord = new ArrayList<>();
	// 游戏配置信息
	@Getter
	private ConcurrentHashMap<Integer, BairenGameConfigDTO> gameConfig = new ConcurrentHashMap<>(); //单个玩家的总下注金额
	// 下注信息
	private double totalBet = 0; //当局的总下注金额
	private ConcurrentHashMap<String, Double> roleTotalBet = new ConcurrentHashMap<>(); //单个玩家的总下注金额
	private ConcurrentHashMap<Integer, CopyOnWriteArrayList<Double>> carBet = new ConcurrentHashMap<>(); // 单个车的下注信息
	private ConcurrentHashMap<Integer, Map<String, Double>> gameBetInfo = new ConcurrentHashMap<>(); // 单车单人下注信息,用于结算的下注详细信息
	private ConcurrentHashMap<String, UserBetInfo> lastBetInfo = new ConcurrentHashMap<>(); // 每个玩家的上回合下注记录
	private ConcurrentHashMap<String, UserBetInfo> currentBetInfo = new ConcurrentHashMap<>(); // 每个玩家当前回合下注记录
	private RobotCacheService robotCacheService = ServiceManager.instance().get(RobotCacheService.class);
	private RoleManager() {
	}

	public static RoleManager instance() {
		return instance;
	}
	
	/*public void init(){
		// 设置系统庄家
		getBanker().setRoleid(D.SYSTEMBANKER);
		getBanker().setNickName(D.SYSTEMBANKER);
		// 添加机器人
		addRobot(D.INITNUM);
	}*/
	/**
	 * 清除下注缓存数据
	 */
	public void cleanBetInfo(){
		totalBet = 0;
		roleTotalBet.clear();
		carBet.clear();
		gameBetInfo.clear();
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
	public void removeChannel(ChannelHandlerContext ctx,int roomId) {
		String roleId = onlineList.get(ctx);
		if (StringUtils.isEmpty(roleId)) {
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
			int state = StateTimerManager.instance().getTimer(roomId).getState();
			if (state == 1) {
				if (bankerList.size() > 0) {
					// 玩家上庄
					String roleid = bankerList.remove(0);
					Role role2 = getRole(roleid);
					banker.setRoleid(roleid);
					banker.setNickName(role2.getNickName());
					banker.setCount(0);
				} else {
					// 系统上庄
					banker.setRoleid(D.SYSTEMBANKER);
					banker.setNickName(D.SYSTEMBANKER);
					banker.setCount(0);
					banker.setGold(0);
				}
				// 通知玩家庄家掉线
				for (ChannelHandlerContext ctxs : onlineList.keySet()) {
					ctxs.writeAndFlush(ByteHelper.createFrameMessage
							(JsonUtil.create().cmd("removeChannel").errorCode("庄家掉线").put("currentBanker", banker.getRoleid()).toJsonString()));
				}
			}
		}
		logger.info("玩家{}退出房间,当前房间人数为: {}人" , roleId , getChannelTotal());
	}
	
	/**
	 * 添加机器人
	 * @param num
	 */
	/*public void addRobot(int num){
		if (num <= 0) {
			return;
		}
		for (int i = 0; i < num; i++) {
			int userId = RandomUtil.getRandom(0, 300);
	        BaseRobot robot = this.robotCacheService.getRobot(userId + 101000);
			robotList.put(String.valueOf(robot.getRoleId()), robot);
		}
	}*/
	
	/**
	 * 获取机器人数量
	 * @return
	 */
	public int getRobotNum(){
		return robotList.size();
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
		GameConfigService gameConfigService = SpringContextUtil.getBean(GameConfigService.class);
		List<BairenGameConfigDTO> bairenGameConfigData = gameConfigService.getBairenGameConfigData("7");
		for (BairenGameConfigDTO dto : bairenGameConfigData) {
			this.gameConfig.put(dto.getFileId(), dto);
		}
	}
	
	public ConcurrentHashMap<String, Role> getRoleMap() {
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

	public ConcurrentHashMap<String, Double> getRoleTotalBet() {
		return roleTotalBet;
	}

	public void setRoleTotalBet(ConcurrentHashMap<String, Double> roleTotalBet) {
		this.roleTotalBet = roleTotalBet;
	}

	public ConcurrentHashMap<Integer, CopyOnWriteArrayList<Double>> getCarBet() {
		return carBet;
	}

	public void setCarBet(ConcurrentHashMap<Integer, CopyOnWriteArrayList<Double>> carBet) {
		this.carBet = carBet;
	}

	public ConcurrentHashMap<Integer, Map<String, Double>> getGameBetInfo() {
		return gameBetInfo;
	}

	public void setGameBetInfo(ConcurrentHashMap<Integer, Map<String, Double>> gameBetInfo) {
		this.gameBetInfo = gameBetInfo;
	}

	public ConcurrentHashMap<String, UserBetInfo> getLastBetInfo() {
		return lastBetInfo;
	}

	public void setLastBetInfo(ConcurrentHashMap<String, UserBetInfo> lastBetInfo) {
		this.lastBetInfo = lastBetInfo;
	}

	public ConcurrentHashMap<String, UserBetInfo> getCurrentBetInfo() {
		return currentBetInfo;
	}

	public void setCurrentBetInfo(ConcurrentHashMap<String, UserBetInfo> currentBetInfo) {
		this.currentBetInfo = currentBetInfo;
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


	public ConcurrentHashMap<ChannelHandlerContext, String> getOnlineList() {
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
