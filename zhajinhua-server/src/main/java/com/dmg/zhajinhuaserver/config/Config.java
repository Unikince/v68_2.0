package com.dmg.zhajinhuaserver.config;

/**
 * @author hexf
 * @Date 2018年3月13日
 * @Desc 定义
 */
public class Config {
	/**
	 * 房间状态
	 * @author Administrator
	 *
	 */
	public static class RoomStatus {
		public static final int READY = 1; // 等待玩家准备状态
		public static final int GAME = 2; // 游戏状态
		public static final int WAIT = 3; // 创建后等待玩家加入状态
		public static final int STOP = 5; //停服状态
//		public static final int BALANCE = 4; //结算状态
	}
	/**
	 * 椅子当前状态
	 *
	 *
	 */
	public static class SeatState {
		public static final int STATE_RUN_AWAY = 1; // 逃跑
		public static final int STATE_WAIT_STAR = 2; // 等待开局,已准备
		public static final int STATE_PLAYING = 3; // 游戏中
		public static final int STATE_CALL = 4; // 跟注
		public static final int STATE_RAISE = 5; // 加注
		public static final int STATE_RUSH = 6; // 血拼
		public static final int STATE_FOLD = 7; // 弃牌
		public static final int STATE_COMPARE_FAIL = 8; // 比输牌
		public static final int STATE_WAIT_READY = 9; // 等待玩家准备
	}

	/**
	 * 玩家下注动作
	 */
	public static class PokerOperState {
		public static final int FOLLOWCHIPS = 1; // 跟注
		public static final int ADDCHIPS = 2; // 加注
		public static final int COMPARECARD = 3; // 比牌
		public static final int RUSH = 4; // 血拼
		public static final int FORCECOMPARE = 5; // 强制
		public static final int SEECARDS = 6; // 看牌
		public static final int AUTOCALL = 7; // 自动跟注
		public static final int DISCARD = 8; // 弃牌
		public static final int SHOWCARDS = 9; // 亮牌
	}
	/**
	 * 牌型枚举
	 *
	 */
	public enum Combination {
		/** 无 */
		NONE(0),
		/** 散牌 */
		HIGHCARD(1),
		/** 对子 */
		PAIR(2),
		/** 顺子 */
		PROGRESSION(3),
		/** 同花 */
		FLUSH(4),
		/** 同花顺 */
		STRAIGHTFLUSH(5),
		/** 豹子 */
		LEOPARD(6);

		private int intValue;
		private static java.util.HashMap<Integer, Combination> mappings;

		private static java.util.HashMap<Integer, Combination> getMappings() {
			if (mappings == null) {
				synchronized (Combination.class) {
					if (mappings == null) {
						mappings = new java.util.HashMap<Integer, Combination>();
					}
				}
			}
			return mappings;
		}

		private Combination(int value) {
			intValue = value;
			Combination.getMappings().put(value, this);
		}

		public int getValue() {
			return intValue;
		}

		public static Combination forValue(int value) {
			return getMappings().get(value);
		}
	}
	/**
	 * 离开房间原因
	 * @author Administrator
	 *
	 */
	public static class LeaveReason {
		public static final int LEAVE_NORMAL = 0; // 正常退出
		public static final int LEAVE_NOMONEY = 1; // 没钱
		public static final int LEAVE_OFFLINE = 2; // 离线
		public static final int LEAVE_ELSE = 3; // 其他
		public static final int LEAVE_SOLVE = 4; //强制解散
		public static final int LEAVE_NOREADY = 5; //未准备踢出
	}
	/**
	 * 机器人概率
	 * @author Administrator
	 *
	 */
	public static class RobotPercent {
		public static final int OUT_LOST_MONEY  = 0;  // 输钱金额到达退出
		public static final int OUT_WIN_MONEY   = 1;  // 赢钱金额到达退出
		public static final int OUT_PLAY_ROUNDS = 2;  // 玩牌轮数到达退出
		public static final int IN_ONE_PLAYER   = 3;  // 一个玩家加入几率
		public static final int IN_TWO_PLAYER   = 4;  // 2个玩家加入几率
		public static final int IN_THREE_PLAYER = 5;  // 3个玩家加入几率
		public static final int IN_FOUR_PLAYER  = 6;  // 4个玩家加入几率
		public static final int IN_FIVE_PLAYER  = 7;  // 5个玩家加入几率
		public static final int IN_ROBOT_LIMIT  = 8;  // 一个房间允许最大加入机器人个数
		public static final int ROBOT_SEECARDS  = 9;  // 每轮机器人看牌概率
		public static final int ROBOT_SEECINC   = 10; // 每轮机器人看牌概率递增量
		public static final int END = 11;//结束标志
	}
}
