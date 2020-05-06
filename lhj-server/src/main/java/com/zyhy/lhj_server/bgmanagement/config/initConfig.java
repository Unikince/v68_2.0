package com.zyhy.lhj_server.bgmanagement.config;
/**
 * 游戏初始化配置
 * @author Administrator
 *
 */
public class initConfig {
		/**
		 * 游戏信息配置
		 */
		// 准入金额
		public static final double inAmount = 100;
		// 当前状态,0为关闭,1为开启
		public static  final  int state = 1;
		// 当前玩家数量
		public static  final  int playerNumber = 0;
		// 总下注
		public static  final  double totalBet = 0;
		// 总赔付
		public static  final  double totalPay = 0;
		// 当前库存
		public static  final  double inventory = 0;
		// 当前赔率
		public static  final  double Odds = 0.95;
		// 人数上限
		public static  final  int totalPlayerNumber = 1000;
		// 赢家抽水
		public static   final  int winReward = 0;
		
		/**
		 * 赔率奖池配置
		 */
		//奖池id
		public static final int PoolId = 1;
		// 奖池名称
		public static final String poolName = "oddsPool";
		// 累计比例
		public static final double poolTotalRatio = 0.02;
		// 当前奖池金额
		public static final double currentAmount = 0;
		// 当前状态,0为关闭,1为开启
		public static final int state1 = 1;
		// 派奖次数
		public static final int payCount = 0;
		// 派奖金额
		public static final double payTotal = 0;
		// 平均派奖金额
		public static final double averageAmount = 0;
		// 最后派奖时间
		public static final String lastPayTime = "";
}
