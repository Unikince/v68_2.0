package com.dmg.bcbm.core.config;

/**
 * 常量
 *
 * @author CharlesLee
 */
public class D {
	/**
     * 需要转发常量
     */
    public static final int HALL_NEED_FORWARD = 1;
    public static final int ZJH_NEED_FORWARD = 2;
    public static final int MJ_NEED_FORWARD = 3;
    public static final int NIOU_NIOU_NEED_FORWARD = 4;
    public static final int DDZ_NEED_FORWARD = 5;
    
    // 平台类型
 	public static  final String PLATFORM_EBET = "1";
 	public static  final String PLATFORM_V68 = "2";
    
    /**
     * 奔驰宝马常量
     */
 	// 服务名称
 	public static final String SERVERNAME = "bcbm";
 	// 游戏名称
 	public static final String GAMENAME = "奔驰宝马";
 	// 游戏ID
 	public static final String GAME_ID = "7";
 	// 默认UUID
  	public static final String UUID = "2";
    // 房间最低人数
    public static final int LOWNUM = 40;
    // 初始化机器人数量 
    public static final int INITNUM = 20;
    // 游戏状态时间(1下注2开奖3休息)
    public static final int GAMESTATE1 = 15;
    public static final int GAMESTATE2 = 15;
    public static final int GAMESTATE3 = 5;
    // 系统做庄
    public static final String SYSTEMBANKER = "Banker";
    // 系统庄金币
    public static final int SYSTEMBANKERGOLD = 8880000;
    
    // 上庄操作类型
    public static final String BANKERTYPE1 = "1";
    public static final String BANKERTYPE2 = "2";
    
    //加入房间
    public static final String JOINROOM = "joinRoom";
     //申请上庄
    public static final String APPLYBANKER = "applyBanker";
    //下注
    public static final String BETINFO = "betInfo";
    public static final String BETTYPE1 = "1"; // 玩家下注
    public static final String BETTYPE2 = "2"; // 机器人下注
    //继续下注
    public static final String CONTINUEBET = "continueBet";
    //机器人下注
    public static final String ROBOTBET = "robotBet";
  //机器人下注
    public static final String ROBOTBETTYPE = "2";
    // 机器人游戏局数限制
    public static final int ROBOTPLAYROUNDLIMIT = 10;
    // 机器人带入金币最低金额
    public static final double ROBOTGOLDMIN = 100000;
    // 机器人带入金币最高金额
    public static final double ROBOTGOLDMAX = 1000000;
    // 庄家列表
    public static final String BANKERLIST = "bankerList";
    // 默认抽水比例
    public static final double DEFAULTPUMPRATE = 5;
    
    /**
     * 所有数据通用的CMD
     */
    public static final String CMD = "cmd";

    /**
     * 玩家心跳处理
     */
    public static final String HEARTBEAT_TIME = "heart";
    /**
     * 时间
     */
    public static final String HALL_HEART_TIME = "time";
    /**
     * 是否转发
     */
    public static final String HALL_WHETHER_FORWARD = "forward";
    /**
     * 加入房间
     */
    public static final String JION_ROOM = "enterroom";
}
