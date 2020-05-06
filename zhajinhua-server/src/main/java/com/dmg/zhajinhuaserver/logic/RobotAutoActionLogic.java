/**
 * 
 */
package com.dmg.zhajinhuaserver.logic;

import com.dmg.zhajinhuaserver.config.D;
import com.dmg.zhajinhuaserver.SpringContextUtil;
import com.dmg.zhajinhuaserver.config.Config;
import com.dmg.zhajinhuaserver.model.bean.GameRoom;
import com.dmg.zhajinhuaserver.model.bean.Robot;
import com.dmg.zhajinhuaserver.model.bean.Seat;
import com.dmg.zhajinhuaserver.service.ALGService;
import com.zyhy.common_server.util.RandomUtil;

/**
 * @author Administrator
 * 机器人自动行为动作控制
 */
public class RobotAutoActionLogic {
	
	/**
	 * 初始化机器人行为概率
	 * @param robot
	 */
	public static void initRobotActionLv(Robot robot){
		// 闷牌概率
		robot.setMenPoker(RobotActionState.MEN_POKER_INIT_LV.getValue());
		// 比牌概率
		robot.setBattlePoker(RobotActionState.BATTLE_POKER_INIT_LV.getValue());
		// 大牌装小牌概率
		robot.setZhuangbigPoker(RobotActionState.ZB_BIG_INIT_LV.getValue());
		// 小牌装大牌概率
		robot.setZhuangsmallPoker(RobotActionState.ZB_SMALL_INIT_LV.getValue());
		// 正常游戏概率
		robot.setCommongamePoker(RobotActionState.COMMON_GAME_INIT_LV.getValue());
		// 偷看手牌概率
		robot.setPeepPoker(RobotActionState.PEEP_POKER_INIT_LV.getValue());
		// 加注概率
		robot.setAddbetGold(RobotActionState.ADD_BET_UP_LV.getValue());
	}
	
	/**
	 * 一轮结束机器人行为概率的变化
	 * @param room 房间信息
	 */
	public static void oneTurnEndChangeRobotActionLv(GameRoom room){
		for (Seat seat : room.getSeatMap().values()) {
			if (seat == null || !seat.isReady() || seat.isPass()) {
				continue;
			}
			if (seat.getPlayer() instanceof Robot) {
				// 机器人
				Robot robot = (Robot)seat.getPlayer();
				// 加注概率
				robot.setAddbetGold(robot.getAddbetGold() + RobotActionState.ADD_BET_ADD_LOOP_UP_LV.getValue());
				// 比牌概率
				robot.setBattlePoker(robot.getBattlePoker() + RobotActionState.BATTLE_POKER_ADD_LOOP_UP_LV.getValue());
				// 大牌装小牌概率
				robot.setZhuangbigPoker(robot.getZhuangbigPoker() + RobotActionState.ZB_BIG_ADD_LOOP_DOWN_LV.getValue());
				// 小牌装大牌概率
				robot.setZhuangsmallPoker(robot.getZhuangsmallPoker() + RobotActionState.ZB_SMALL_ADD_LOOP_DOWN_LV.getValue());
				// 正常游戏概率
				robot.setCommongamePoker(robot.getCommongamePoker() + RobotActionState.COMMON_GAME_ADD_LOOP_DOWN_LV.getValue());
				// 手牌每轮对游戏概率的影响(值越大游戏越稳)
				robot.setCommongamePoker(robot.getCommongamePoker() + RobotActionState.checkCombinationByType(seat.getHandCardsType()));
				// 手牌每轮对比牌概率的影响(值越小越不会去比牌)
				robot.setBattlePoker(robot.getBattlePoker() - RobotActionState.checkCombinationByType(seat.getHandCardsType()));
			}
		}
	}
	
	/**
	 * 一局结束机器人行为概率的变化
	 * @param room
	 * @param winSeatId
	 */
	public static void oneRoundEndChangeRobotActionLv(GameRoom room, int winSeatId){
		for (Seat seat : room.getSeatMap().values()) {
			if (seat == null || seat.getPlayer() == null || !seat.isReady()) 
			{
				continue;
			}
			if (seat.getPlayer() instanceof Robot) {
				// 机器人信息
				Robot robot = (Robot)seat.getPlayer();
				if (seat.getSeatId() == winSeatId) {
					// 加注概率
					robot.setAddbetGold(robot.getAddbetGold() + RobotActionState.ADD_BET_SUCCESS_UP_LV.getValue());
					// 看牌概率
					robot.setMenPoker(robot.getMenPoker() + RobotActionState.MEN_POKER_SUCCESS_DOWN_LV.getValue());
					// 大牌装小牌概率
					robot.setZhuangbigPoker(robot.getZhuangbigPoker() + RobotActionState.ZB_BIG_SUCCESS_DOWN_LV.getValue());
					// 小牌装大牌概率
					robot.setZhuangsmallPoker(robot.getZhuangsmallPoker() + RobotActionState.ZB_SMALL_SUCCESS_UP_LV.getValue());
					// 偷看手牌概率
					robot.setPeepPoker(robot.getPeepPoker() + RobotActionState.PEEP_POKER_SUCCESS_DOWN_LV.getValue());
				}else {
					// 加注概率
					robot.setAddbetGold(robot.getAddbetGold() + RobotActionState.ADD_BET_FAIL_DOWN_LV.getValue());
					// 看牌概率
					robot.setMenPoker(robot.getMenPoker() + RobotActionState.MEN_POKER_FAIL_UP_LV.getValue());
					// 大牌装小牌概率
					robot.setZhuangbigPoker(robot.getZhuangbigPoker() + RobotActionState.ZB_BIG_FAIL_UP_LV.getValue());
					// 小牌装大牌概率
					robot.setZhuangsmallPoker(robot.getZhuangsmallPoker() + RobotActionState.ZB_SMALL_FAIL_DOWN_LV.getValue());
					// 偷看手牌概率
					robot.setPeepPoker(robot.getPeepPoker() + RobotActionState.PEEP_POKER_FAIL_UP_LV.getValue());
				}
				// 正常初始概率
				robot.setCommongamePoker(RobotActionState.COMMON_GAME_INIT_LV.getValue());
				// 比牌初始概率
				robot.setBattlePoker(RobotActionState.BATTLE_POKER_INIT_LV.getValue());
			}
		}
	}
	
	/**
	 * 加注影响机器人行为概率的变化
	 * @param room
	 * @param seat
	 */
	public static void addBetChangeRobotActionLv(GameRoom room, Seat seat){
		for (Seat otherSeat : room.getSeatMap().values()) {
			if (otherSeat == null || otherSeat.getSeatId() == seat.getSeatId()) {
				continue;
			}
			if (otherSeat.getPlayer() instanceof Robot) {
				Robot otherRobot = (Robot)otherSeat.getPlayer();
				otherRobot.setMenPoker(otherRobot.getMenPoker() + RobotActionState.MEN_POKER_ADD_BET_UP_LV.getValue());
				otherRobot.setBattlePoker(otherRobot.getBattlePoker() + RobotActionState.BATTLE_POKER_ADD_BET_UP_LV.getValue());
				otherRobot.setCommongamePoker(otherRobot.getCommongamePoker() + RobotActionState.COMMON_GAME_ADD_BET_DOWN_LV.getValue());
				otherRobot.setZhuangbigPoker(otherRobot.getZhuangbigPoker() + RobotActionState.ZB_BIG_ADD_BET_DOWN_LV.getValue());
				otherRobot.setZhuangsmallPoker(otherRobot.getZhuangsmallPoker() + RobotActionState.ZB_SMALL_ADD_BET_DOWN_LV.getValue());
			}
		}
	}
	
	/**
	 * 选择机器人行为操作
	 * @param room
	 * @param seat
	 * @return
	 */
	public static int selectRobotAction(GameRoom room, Seat seat){
		// 偷看一家手牌
		checkPeepPoker(room, seat);
		// 判断当前轮次是否超过3轮触发比牌
		boolean battlepoker = isBattlePoker(room, seat);
		if (battlepoker) {
			// 触发比牌操作降低闷牌概率
			for (Seat seeSeat : room.getSeatMap().values()) {
				if (seeSeat.getPlayer() instanceof Robot) {
					Robot robot = (Robot)seeSeat.getPlayer();
					robot.setMenPoker(robot.getMenPoker() + RobotActionState.MEN_POKER_PEEP_POKER_DOWN_LV.getValue());
				}
			}
			return Config.PokerOperState.COMPARECARD;
		}
		// 判断是否看牌
		if (!seat.isHaveSeenCard()) {
			// 未看牌,检查是否继续闷牌
			boolean menpoker = isContinueMenPoker(seat);
			if (menpoker) {
				return isContinueAddChips(room, seat);
			}else {
				// 看牌
				return Config.PokerOperState.FOLLOWCHIPS;
			}
		}
		// 已看牌,判断是不是小牌装大牌
		boolean zhuangbigpoker = isZhuangBigPoker(seat);
		if (zhuangbigpoker) {
			// 装逼
			return isContinueAddChips(room, seat);
		}
		// 已看牌,判断是不是大牌装小牌
		boolean zhuangsmallpoker = isZhuangSmallPoker(seat);
		if (zhuangsmallpoker) {
			// 装逼
			return Config.PokerOperState.FOLLOWCHIPS;
		}
		// 已看牌,正常玩
		Robot robot = (Robot)seat.getPlayer();
		if (robot.getCommongamePoker() <= 0) {
			return Config.PokerOperState.DISCARD;
		}else {
			return isContinueAddChips(room, seat);
		}
	}
	
	/**
	 * 是否闷牌
	 * @param seat
	 * @return
	 */
	public static boolean isContinueMenPoker(Seat seat){
		Robot robot = (Robot)seat.getPlayer();
		if (robot.getMenPoker() <= 0) {
			// 闷牌
			return false;
		}else {
			int randomnum = RandomUtil.getRandom(0, 100);
			if (randomnum < robot.getMenPoker()) {
				// 看牌
				return false;
			}else {
				// 闷牌
				return true;
			}
		}
	}
	
	/**
	 * 是否加注
	 * @return
	 */
	private static int isContinueAddChips(GameRoom room, Seat seat){
		Robot robot = (Robot)seat.getPlayer();
		// 判断是跟注或者加注
		int addbetnum = RandomUtil.getRandom(0, 100);
		if (addbetnum < robot.getAddbetGold()) {
			if (room.getAddChipsBet() >= room.getBetMul().length) {
				return Config.PokerOperState.FOLLOWCHIPS;
			}else {
				return Config.PokerOperState.ADDCHIPS;
			}
		}else {
			return Config.PokerOperState.FOLLOWCHIPS;
		}
	}
	
	/**
	 * 是否小牌装大牌
	 * @param seat
	 * @return
	 */
	private static boolean isZhuangBigPoker(Seat seat){
		Config.Combination handCardsType = Config.Combination.forValue(seat.getHandCardsType());
		if (handCardsType != Config.Combination.HIGHCARD) {
			// 牌型无法触发装逼
			return false;
		}else {
			Robot robot = (Robot)seat.getPlayer();
			// 判断是否触发装逼
			if (robot.getZhuangbigPoker() <= 0) {
				// 莫装逼
				return false;
			}else {
				// 随机装逼
				int randomnum = RandomUtil.getRandom(0, 100);
				if (randomnum < robot.getZhuangbigPoker()) {
					return true;
				}else {
					return true;
				}
			}
		}
	}
	
	/**
	 * 是否大牌装小牌
	 * @param seat
	 * @return
	 */
	private static boolean isZhuangSmallPoker(Seat seat){
		Config.Combination handCardsType = Config.Combination.forValue(seat.getHandCardsType());
		if (handCardsType != Config.Combination.FLUSH
				&& handCardsType != Config.Combination.STRAIGHTFLUSH
				&& handCardsType != Config.Combination.LEOPARD) {
			// 牌型无法触发装逼
			return false;
		}else {
			Robot robot = (Robot)seat.getPlayer();
			// 判断是否触发装逼
			if (robot.getZhuangsmallPoker() <= 0) {
				// 莫装逼
				return false;
			}else {
				// 随机装逼
				int randomnum = RandomUtil.getRandom(0, 100);
				if (randomnum < robot.getZhuangsmallPoker()) {
					return true;
				}else {
					return true;
				}
			}
		}
	}
	
	/**
	 * 是否偷偷比牌
	 * @param room
	 * @param seat
	 */
	private static void checkPeepPoker(GameRoom room, Seat seat){
		Robot robot = (Robot)seat.getPlayer();
		if (robot.getPeepPoker() <= 0) {
			// 拒绝偷看手牌
			return;
		}else {
			// 随机偷看手牌
			int randomnum = RandomUtil.getRandom(0, 100);
			if (randomnum > robot.getPeepPoker()) {
				// 拒绝偷看手牌
				return;
			}
		}
		for (Seat peepSeat : room.getSeatMap().values()) {
			if (peepSeat == null || peepSeat.isPass() || !peepSeat.isReady() || peepSeat.getHand().size() != 3 || peepSeat.getSeatId() == seat.getSeatId()) {
				// 座位不存在,座位玩家已弃牌,座位玩家未准备,座位玩家没手牌,座位玩家等于偷看玩家
				continue;
			}
			// 偷偷比较手牌
			ALGService algService = SpringContextUtil.getBean(ALGService.class);
			boolean win = algService.operOneWin(room, seat.getHand(), peepSeat.getHand());
			if (win) {
				// 比牌赢了增加加注概率
				robot.setAddbetGold(robot.getAddbetGold() + RobotActionState.ADD_BET_ADD_LOOP_UP_LV.getValue());
				return;
			}else {
				// 输了影响机器人的看牌概率,比牌概率,正常游戏概率
				robot.setBattlePoker(robot.getBattlePoker() + RobotActionState.BATTLE_POKER_PEEP_POKER_UP_LV.getValue());
				return;
			}
		}
	}
	
	/**
	 * 是否触发比牌
	 * @param room
	 * @param seat
	 * @return
	 */
	public static boolean isBattlePoker(GameRoom room, Seat seat){
		if (room.getOperTurns() < 3) {
			// 不足3轮无法比牌
			return false;
		}
		if (room.getOperTurns() >= room.getCountTurns()) {
			// 15轮自动比牌
			return true;
		}
		Robot robot = (Robot)seat.getPlayer();
		if (robot.getBattlePoker() <= 0) {
			return false;
		}else {
			int randomnum = RandomUtil.getRandom(0, 100);
			if (randomnum < robot.getBattlePoker()) {
				return true;
			}else {
				return false;
			}
		}
	}

}
