/**
 * 
 */
package com.zyhy.lhj_server.bgmanagement.constants;

/**
 * @author linanjun 消息号
 */
public class SoltMessageConstants {
	public static final int SOLTTEST = 001;  // 老虎机游戏测试
	/**
	 *增加删除
	 * */
	public static final int QUERYALLGAMEINFO = 101;  // 获取所有游戏信息
	public static final int SOLTLIST = 102;  // 获取老虎机游戏列表
	public static final int ADDSOLTINFO = 103;  // 添加老虎机游戏
	public static final int DELSOLTINFO = 104;  // 删除老虎机游戏
	/**
	 * 场次配置
	 */
	public static final int QUERYSINGLEGAMEINFO = 201;  // 查询单个游戏信息
	public static final int UPDATESLOTINFO = 202;  // 更新游戏信息
	public static final int OPENALLSOLTGAME = 203;  // 开启所有老虎机
	public static final int COLSEALLSOLTGAME = 204;  // 关闭所有老虎机
	
	
	/**
	 * 库存配置
	 */
	public static final int QUERYINVENTORYINFO = 301;  // 查询单个游戏库存配置
	public static final int ADDINVENTORYINFO = 302;  // 添加库存配置
	public static final int DELINVENTORYINFO = 303;  // 删除库存配置
	
	/**
	 * 彩金配置
	 */
	public static final int QUERYPOOLCONFIG = 401;  // 查询彩金配置
	public static final int UPDATEPOOLCONFIG = 402;  // 更新彩金配置
	
	/**
	 * 赔率奖池
	 */
	public static final int QUERYODDSPOOLINFO = 501;  // 查询赔率奖池信息
	public static final int UPDATEODDSPOOLINFO = 509;  // 更新赔率奖池信息
	
	public static final int QUERYPAYLIMITINFO = 502;  // 查询派奖条件信息
	public static final int ADDPAYLIMITINFO = 503;  // 添加派奖条件信息
	public static final int UPDATEPAYLIMITINFO = 510;  // 更新派奖条件信息
	public static final int DELPAYLIMITINFO = 504;  // 删除派奖条件信息
	
	
	public static final int QUERYPAYPLAYERLIMITINFO = 505;  // 查询玩家派奖条件信息
	public static final int ADDPAYPLAYERLIMITINFO = 506;  // 添加玩家派奖条件信息
	public static final int UPDATEPALYERPAYLIMITINFO = 511;  // 更新玩家派奖条件信息
	public static final int DELPAYPLAYERLIMITINFO = 507;  // 删除玩家派奖条件信息
	
	public static final int QUERYPLAYERRECORD = 508;  // 查询玩家派奖记录
}
