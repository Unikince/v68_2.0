package com.dmg.niuniuserver.model.constants;

/**
	 * 机器人概率
	 * @author Administrator
	 *
	 */
	public class RobotPercent {
		public static final int OUT_LOST_MONEY  = 0; // 输钱金额到达退出
		public static final int OUT_WIN_MONEY   = 1; // 赢钱金额到达退出
		public static final int OUT_PLAY_ROUNDS = 2; // 玩牌轮数到达退出
		public static final int IN_ONE_PLAYER   = 3; // 一个玩家加入几率
		public static final int IN_TWO_PLAYER   = 4; // 2个玩家加入几率
		public static final int IN_THREE_PLAYER = 5; // 3个玩家加入几率
		public static final int IN_FOUR_PLAYER  = 6; // 4个玩家加入几率
		public static final int IN_FIVE_PLAYER  = 7; // 5个玩家加入几率
		public static final int IN_ROBOT_LIMIT  = 8; // 一个房间允许最大加入机器人个数
	}