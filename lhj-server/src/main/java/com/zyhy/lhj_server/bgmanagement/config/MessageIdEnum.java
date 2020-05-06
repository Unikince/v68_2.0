/**
 * 
 */
package com.zyhy.lhj_server.bgmanagement.config;

/**
 * @author ASUS
 *
 */
public enum MessageIdEnum {
	HYHZ( 1 , 0, "HYHZ","好运猴子", 1100),
	NNYC( 2 , 0, "NNYC","年年有财", 1200),
	ZCJB( 3 , 13, "ZCJB","招财进宝", 1300),
	JSGL( 4 , 0, "JSGL","极速公路", 1400),
	CZDBZ( 5 , 0, "CZDBZ","船长的宝藏", 1500),
	BQTP( 6 , 0, "BQTP","冰球突破", 1600),
	ALSJBY( 7 , 0, "ALSJBY","阿拉斯加捕鱼", 1700),
	SBHZ( 8 , 0, "SBHZ","三倍猴子", 1800),
	BXQY( 9 , 12, "BXQY","不朽情缘", 1900),
	SWK( 10 , 0, "SWK","孙悟空", 2000),
	FKMJ( 11 , 0, "FKMJ","疯狂麻将", 2100),
	HJWS( 12 , 0, "HJWS","黄金武士", 2200),
	YHDD( 13, 0,  "YHDD","玉皇大帝", 2300),
	LQJX( 14 , 0, "LQJX","篮球巨星", 2400),
	LLL( 15 , 0, "LLL","龙龙龙", 2500),
	AJXZ( 16 , 0, "AJXZ","埃及旋转", 2600),
	YZHX( 17 , 0, "YZHX","亚洲幻想", 2700),
	TGPD( 18 , 0, "TGPD","糖果派对", 2800),
	XYWJS( 19 , 11, "XYWJS","幸运维加斯", 2900),
	;
	
	// 老虎机的游戏id
	private int gameId;
	// 2.0后台配置的id
	private int bgId;
	// 游戏名字
	private String redisName;
	// 游戏名字
	private String gameName;
	// 登陆消息号
	private int loginMessageId;

	private MessageIdEnum(int gameId, int bgId, String redisName, String gameName, int loginMessageId) {
		this.gameId = gameId;
		this.bgId = bgId;
		this.redisName = redisName;
		this.gameName = gameName;
		this.loginMessageId = loginMessageId;
	}
	
	public int getGameId() {
		return gameId;
	}
	public int getBgId() {
		return bgId;
	}
	public String getRedisName() {
		return redisName;
	}
	public String getGameName() {
		return gameName;
	}
	public int getLoginMessageId() {
		return loginMessageId;
	}

	/**
	 * 通过消息号获取redis缓存名称
	 * @param messageId
	 * @return
	 */
	public static String getRedisNameByMessageId(int messageId){
		for (MessageIdEnum e : values()) {
			if (e.getLoginMessageId() == messageId) {
				return e.getRedisName();
			}
		}
		return null;
	}
	
	public static String getRedisNamebyName(String gameName){
		for(MessageIdEnum v : values()){
			if(v.getGameName().equals(gameName)){
				return v.getRedisName();
			}
		}
		return null;
	}
	
	public static String getNameById(int id) {
		for(MessageIdEnum v : values()){
			if(v.getGameId() == id){
				return v.getGameName();
			}
		}
		return null;
	}
	
	public static int getIdByName(String gameName) {
		for(MessageIdEnum v : values()){
			if(v.getGameName().equals(gameName)){
				return v.getGameId();
			}
		}
		return 0;
	}
	
}
