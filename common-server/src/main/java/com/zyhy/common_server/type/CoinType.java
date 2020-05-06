package com.zyhy.common_server.type;

public enum CoinType {
	
//	RECHARGE_GOLD("钻石充值","ingot_log_","钻石"),
//	REDEEM_GOLD_COIN("钻石兑换游戏币","ingot_log_","钻石"),
//	INGOT_EXCHANGE("兑换游戏币","ingot_log_","游戏币"),
//
//	COIN_CHOUJIANG("游戏币抽奖","ingot_log_","游戏币"),
//	FIRST_RECHARGE_REWARD_BINDLIJIN("首充奖励绑定礼金", "ingot_log", "礼金"),
//	FIRST_RECHARGE_REWARD_COIN("首充奖励游戏币", "ingot_log", "游戏币"),
//	SHARE_GAME_REWARD_COIN("分享游戏奖励游戏币", "ingot_log", "游戏币"),
//	LOOK_GAME_REWARD_COIN("关注公众号奖励游戏币", "ingot_log", "游戏币"),
//	DIBAO_REWARD_INFO("领取低保补助", "ingot_log", "游戏币"),
//	SECOND_DIBAO_REWARD_INFO("首页低保补助", "ingot_log", "游戏币"),
//	SAFEBOX_UPDATE_INGOT("更新保险箱游戏币","game_log_", "游戏币"),
//
//	UPDATE_USER_NAME_INFO("修改用户昵称","ingot","游戏币"),
//
//	UPDATE_USER_COIN_BET("下注","ingot_log_","游戏币"),
//	UPDATE_USER_COIN_DOUBLE("加倍","ingot_log_","游戏币"),
//	UPDATE_USER_COIN_WIN("胜利","ingot_log_","游戏币"),
//
//	DDZ_CREATEROOM_FANGZHU("房主支付房费","ingot_log_","元宝"),
//	DDZ_CREATEROOM_AAZHI("AA支付房费","ingot_log_","元宝"),
//	DDZ_CREATEROOM_DAYINGJIA("大赢家支付房费","ingot_log_","元宝"),
//	DDZ_EXITROOM_AAZHI("退还AA支付房费", "ingot_log", "元宝"),
//	DDZ_EXITROOM_FANGZHU("退还房主支付房费", "ingot_log", "元宝"),
//	DDZ_GOLDMATCH_MENPIAO("匹配模式门票", "ingot_log", "游戏币"),
//	DDZ_GOLDMATCH_BATTLEREWARD("匹配模式奖励", "ingot_log", "游戏币"),
//	DDZ_UPDATE_GIFT_TICKET("使用兑换码", "ingot_log", "游戏币"),
//	DDZ_LOTTERY_TICKET_ZYKJ("中悦兑换游戏币", "ingot_log", "游戏币"),
//	//签到
//	QIANDAO_UPDATE_COIN("签到获取游戏币","ingot_log_","游戏币"),
//
//	//分享奖励
//	SHARE_BONUS_LINGQU("领取分享获得游戏币","ingot_log_","游戏币"),
//	// 疯狂点球扣除场地费
//	FKDQ_USE_GAME("疯狂点球扣除场地费", "ingot_log_", "游戏币"),
//	// 疯狂点球对战胜利奖励
//	FKDQ_REWARD_GAME("疯狂点球对战胜利奖励", "ingot_log_", "游戏币"),

	DDZ_DIZHU("斗地主底分", "ingot_log_", "金币"),
	DDZ_SETTLEMENT("斗地主结算", "ingot_log_", "金币"),
	DDZ_QUIT("斗地主退出房间", "ingot_log_", "金币"),
	MAHJONG_DIZHU("麻将底分", "ingot_log_", "金币"),
	MAHJONG_SETTLEMENT("麻将结算", "ingot_log_", "金币"),
	MAHJONG_QUIT("麻将退出房间", "ingot_log_", "金币"),
	NIUNIU_BET("牛牛下注", "ingot_log_", "金币"),
	NIUNIU_SETTLEMENT("牛牛结算", "ingot_log_", "金币"),
	NIUNIU_TONGBI_SETTLEMENT("牛牛通比结算", "ingot_log_", "金币"),
	NIUNIU_QUIT("牛牛退出房间", "ingot_log_", "金币"),
	ZJH_DIZHU("扎金花底分", "ingot_log_", "金币"),
	ZJH_BET("扎金花下注", "ingot_log_", "金币"),
	ZJH_SETTLEMENT("炸金花结算", "ingot_log_", "金币"),
	ZJH_QUIT("炸金花退出房间", "ingot_log_", "金币"),

	;
	
	private CoinType(String value,String table,String type){
		this.value = value;
		this.table = table;
		this.type = type;
	}
	private String value;
	private String table;
	private String type;
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}

