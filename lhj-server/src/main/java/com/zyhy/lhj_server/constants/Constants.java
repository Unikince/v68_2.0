/**
 * 
 */
package com.zyhy.lhj_server.constants;

/**
 * @author linanjun
 *
 */
public class Constants {
	// 服务名
	public static  final String LHJGAME_NAME = "老虎机";
	// 运行环境
	public static   boolean ISDEBUG = false;
	// 平台类型
	public static  final String PLATFORM_EBET = "1";
	public static  final String PLATFORM_V68 = "2";
	// 查询战绩
	public static  final String LHJGAME_RECORD = "LHJGAME_RECORD_";
	// 下注类型
	public static  final int BETTYPE0 = 0;  // 免费
	public static  final int BETTYPE1 = 1; // 正常
	// 游戏记录类型
	public static  final String RECORDTYPE1 = "game";
	public static  final String RECORDTYPE2 = "free";
	public static  final String RECORDTYPE3 = "special";
	// 游客账号记录保存时间
	public static  final int RECORD_TIME_1HOUR = 60*60*1; // 一小时
	// 正式账号记录保存时间
	public static  final int RECORD_TIME_30DAY = 60*60*24*30; // 一个月
	// 掉落数据保存时间
	public static  final int RECORD_TIME_5MIN = 60*5; // 五分钟

	
	 //TODO 古怪猴子
	// 服务名
	public static final String GGHZ_SERVER_NAME = "gghz";
	// 游戏名
	public static final String GGHZ_GAME_NAME = "好运猴子";
	public static final String GGHZ_REDIS_LHJ_GGHZ_JACKPOOL = "REDIS_LHJ_GGHZ_JACKPOOL";
	public static final String GGHZ_REDIS_LHJ_GGHZ_USERLUCK = "REDIS_LHJ_GGHZ_USERLUCK";
	
	/**
	 * 年年有余
	 * */
	// 服务名
	public static final String NNYY_SERVER_NAME = "nnyy";
	// 游戏名
	public static final String NNYY_GAME_NAME = "年年有财";
	// 回合id
	public static final String NNYY_GAME_ROUNDID = "REDIS_LHJ_NNYY_ROUNDID_";
	//奖池
	public static final String NNYY_REDIS_LHJ_NNYY_DRAGON_POOL = "REDIS_LHJ_NNYY_DRAGON_POOL";
	//奖池游戏资格
	public static final String NNYY_REDIS_DRAGON_GAME = "REDIS_LHJ_NNYY_DP_";
	//奖池游戏数据
	public static final String NNYY_REDIS_DRAGON_GAME_DATA = "REDIS_LHJ_NNYY_DP_D_";
	
	 // TODO 招财进宝
	// 服务名
	public static final String ZCJB_SERVER_NAME = "ZCJB";
	// 游戏名
	public static final String ZCJB_GAME_NAME = "招财进宝";
	
	/**
	 * 高速公路
	 * */
	// 服务名
	public static final String GSGL_SERVER_NAME = "gsgl";
	// 游戏名
	public static final String GSGL_GAME_NAME = "极速公路";
	//BONUS
	public static final String GSGL_REDIS_BONUS = "REDIS_LHJ_GSGL_BONUS_";
	
	//BONUS
	public static final String GSGL_REDIS_LHJ_GSGL_USERBET = "REDIS_LHJ_GSGL_USERBET_";
	
	/**
	 * 船长的宝藏
	 * */
	// 服务名
	public static final String CZDBZ_SERVER_NAME = "czdbz";
	// 游戏名
	public static final String CZDBZ_GAME_NAME = "船长的宝藏";
	
	/**
	 * 冰球突破
	 * */
	// 服务名
	public static final String BQTP_SERVER_NAME = "bqtp";
	// 游戏名
	public static final String BQTP_GAME_NAME = "冰球突破";

	// 免费游戏
	public static final String BQTPREDIS_SCATTER = "REDIS_LHJ_BQTP_SCATTER_";

	// 补充数据
	public static final String BQTPREDIS_REPLENISH = "REDIS_LHJ_BQTP_REPLENISH_";

	// 几列全是wild
	public static final String BQTPREDIS_WILDS = "REDIS_LHJ_BQTP_WILDS_";
	
	/**
	 * 阿拉斯加捕鱼
	 * */
	// 服务名
	public static final String ALSJ_SERVER_NAME = "alsj";
	// 游戏名
	public static final String ALSJ_GAME_NAME = "阿拉斯加捕鱼";
	//免费游戏
	public static final String ALSJREDIS_SCATTER = "REDIS_LHJ_ALSJ_SCATTER_";
	//BONUS飞鱼红利
	public static final String ALSJREDIS_BONUS = "REDIS_LHJ_ALSJ_BONUS_";
	
	/**
	 * 三倍猴子
	 * */
	// 服务名
	public static final String SBHZ_SERVER_NAME = "sbhz";
	// 游戏名
	public static final String SBHZ_GAME_NAME = "三倍猴子";
	
	//猴子重转
	public static final String SBHZ_REDIS_WILD = "REDIS_LHJ_SBHZ_WILD_";
	
	 // TODO 不朽情缘
	// 服务名
	public static final String BXQY_SERVER_NAME = "BXQY";
	// 游戏名
	public static final String BXQY_GAME_NAME = "不朽情缘";
	//免费游戏
	public static final String BXQY_REDIS_SCATTER = "REDIS_LHJ_BXQY_SCATTER_";
	//补充数据
	public static final String BXQY_REDIS_REPLENISH = "REDIS_LHJ_BXQY_REPLENISH_";
	//几列全是wild
	public static final String BXQY_REDIS_WILDS = "REDIS_LHJ_BXQY_WILDS_";
	
	/**
	 * 孙悟空
	 * */
	// 服务名
	public static final String SWK_SERVER_NAME = "swk";
	// 游戏名
	public static final String SWK_GAME_NAME = "孙悟空";
	
	public static final String SWK_REDIS_LHJ_SWK_SCATTER = "REDIS_LHJ_SWK_SCATTER_";
	
	/**
	 * 疯狂麻将
	 * */
	// 服务名
	public static final String FKMJ_SERVER_NAME = "fkmj";
	// 游戏名
	public static final String FKMJ_GAME_NAME = "疯狂麻将";
	//BONUS游戏
	public static final String FKMJ_REDIS_LHJ_FKMJ_BONUS = "REDIS_LHJ_FKMJ_BONUS_";
	// 补充数据
	public static final String FKMJ_REDIS_LHJ_FKMJ_REPLENISH = "REDIS_LHJ_FKMJ_REPLENISH_";
	
	/**
	 * 黄金武士
	 * */
	// 服务名
	public static final String HJWS_SERVER_NAME = "hjws";
	// 游戏名
	public static final String HJWS_GAME_NAME = "黄金武士";
	//免费游戏
	public static final String HJWS_REDIS_SCATTER = "REDIS_LHJ_HJWS_SCATTER_";
	
	/**
	 * 玉皇大帝
	 * */
	// 服务名
	public static final String YHDD_SERVER_NAME = "yhdd";
	// 游戏名
	public static final String YHDD_GAME_NAME = "玉皇大帝";
	//BONUS
	public static final String YHDD_REDIS_BONUS = "REDIS_LHJ_YHDD_BONUS_";
	//BONUS
	public static final String YHDD_REDIS_LHJ_GSGL_USERBET = "REDIS_LHJ_YHDD_USERBET_";
	
	/**
	 * 篮球巨星
	 * */
	// 服务名
	public static final String LQJX_SERVER_NAME = "lqjx";
	// 游戏名
	public static final String LQJX_GAME_NAME = "篮球巨星";
	//免费游戏
	public static final String LQJX_REDIS_SCATTER = "REDIS_LHJ_LQJX_SCATTER_";
	
	//补充数据
	public static final String LQJX_REDIS_REPLENISH = "REDIS_LHJ_LQJX_REPLENISH_";
	
	//几列全是wild
	public static final String LQJX_REDIS_WILDS = "REDIS_LHJ_LQJX_WILDS_";
	
	
	/**
	 * 龙龙龙
	 * */
	public static final String LLL_SERVER_NAME = "lll";
	public static final String LLL_GAME_NAME = "龙龙龙";
	
	public static final String LLL_REDIS_LHJ_GGHZ_JACKPOOL = "REDIS_LHJ_LLL_JACKPOOL";
	
	public static final String LLL_REDIS_LHJ_GGHZ_USERLUCK = "REDIS_LHJ_LLL_USERLUCK";
	
	
	/**
	 * 埃及旋转
	 * */
	// 服务名
	public static final String AJXZ_SERVER_NAME = "ajxz";
	// 游戏名
	public static final String AJXZ_GAME_NAME = "埃及旋转";
	//免费游戏
	public static final String AJXZ_REDIS_LHJ_AJXZ_FREE = "REDIS_LHJ_AJXZ_FREE_";
	//补充数据
	public static final String AJXZ_REDIS_REPLENISH = "REDIS_LHJ_AJXZ_REPLENISH_";
	
	/**
	 * 亚洲幻想
	 * */
	// 服务名
	public static final String YZHX_SERVER_NAME = "yzhx";
	// 游戏名
	public static final String YZHX_GAME_NAME = "亚洲幻想";
	//免费游戏
	public static final String YZHX_REDIS_LHJ_YZHX_FREE = "REDIS_LHJ_YZHX_FREE_";
	//补充数据
	public static final String YZHX_REDIS_REPLENISH = "REDIS_LHJ_YZHX_REPLENISH_";
	
	/**
	 * 糖果派对
	 * */
	// 服务名
	public static final String TGPD_SERVER_NAME = "tgpd";
	// 游戏名
	public static final String TGPD_GAME_NAME = "糖果派对";
	// 游戏信息
	public static final String REDIS_LHJ_TGPD_GAMEINFO = "REDIS_LHJ_TGPD_GAMEINFO_";
	//免费游戏
	public static final String REDIS_LHJ_TGPD_FREE = "REDIS_LHJ_TGPD_FREE_";
	//补充数据
	public static final String REDIS_LHJ_TGPD_REPLENISH = "REDIS_LHJ_TGPD_REPLENISH_";
	//奖池
	public static final String REDIS_LHJ_TGPD_DRAGON_POOL = "REDIS_LHJ_TGPD_DRAGON_POOL";
	//奖池游戏数据
	public static final String REDIS_LHJ_TGPD_DRAGON_DATA = "REDIS_LHJ_TGPD_DRAGON_DATA_";
	// 心跳数据
	public static final String REDIS_LHJ_TGPD_HEART_DATA = "REDIS_LHJ_TGPD_HEART_DATA_";
	
	 // TODO 疯狂水果机
	// 服务名
	public static final String XYWJS_SERVER_NAME = "XYWJS";
	// 游戏名
	public static final String XYWJS_GAME_NAME = "幸运维加斯";
	// 单项下注上限
	public static final  double MAX_PUT_GAME = 99999;

	
	
}
