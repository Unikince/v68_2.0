/**
 * 
 */
package com.zyhy.lhj_server.game;

/**
 * @author ASUS
 *
 */
public enum GameNameEnum {
	GGHZ("001","好运猴子"),
	NNYY("002","年年有财"),
	ZCTZ("003","招财进宝"),
	GSGL("004","极速公路"),
	CZDBZ("005","船长的宝藏"),
	BQTP("006","冰球突破"),
	ALSJ("007","阿拉斯加捕鱼"),
	SBHZ("008","三倍猴子"),
	BXLM("009","不朽情缘"),
	SWK("010","孙悟空"),
	FKMJ("011","疯狂麻将"),
	HJWS("012","黄金武士"),
	YHDD("013","玉皇大帝"),
	LQJX("014","篮球巨星"),
	LLL("015","龙龙龙"),
	AJXZ("016","埃及旋转"),
	YZHX("017","亚洲幻想"),
	QZNW("018","权杖女王"),
	HSCS("019","好事成双"),
	TGPD("020","糖果派对"),
	;
	
	// 游戏id
	private String gameId;
	// 游戏名字
	private String gameName;

	private GameNameEnum(String gameId, String gameName) {
		this.gameId = gameId;
		this.gameName = gameName;
	}
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public static String getById(String id) {
		for(GameNameEnum v : values()){
			if(v.getGameId().equals(id)){
				return v.getGameName();
			}
		}
		return null;
	}
}
