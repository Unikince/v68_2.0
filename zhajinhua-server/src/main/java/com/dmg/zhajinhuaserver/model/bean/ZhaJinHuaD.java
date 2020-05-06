package com.dmg.zhajinhuaserver.model.bean;


public class ZhaJinHuaD{
//	------------- action message variable ----------------------------
	/** 房主主动开始游戏 */
	public static final String REQUEST_START_GAME = "start_game";
	/** 操作类型 */
	public static final String ACTION_TYPE = "action_type";
	/** 操作类型集 */
	public static final String ACTION_TYPES = "action_types";
	/** 比牌椅子 */
	public static final String COMPARED_SEAT = "compared_seat";
	/** 下注金额 */
	public static final String CHIPS = "chips";
	/** 房间血拼状态 */
	public static final String IS_RUSH = "is_rush";
	/** 血拼id */
	public static final String RUSH_ID = "rush_id";
	/** 血拼筹码 */
	public static final String RUSH_CHIPS = "rush_chips";
	/** 血拼剩余次数 */
	public static final String RUSH_LEFTTIME = "rush_lefttime";
	/** 加注集*/
	public static final String ADD_CHIPS = "add_chips";
	/** 战绩输赢分数 */
	public static final String WIN= "win";
	/** 战绩时间 */
	public static final String TIME= "time";
	/** 战绩牌型 */
	public static final String CARD_TYPE= "card_type";
	/** 战绩手牌 */
	public static final String HAND_CARDS= "hand_cards";
	/** 比牌输家 */
	public static final String LOSER_RID= "loserRid";
	/** 被比牌玩家 */
	public static final String TARGET_RID= "targetRid";
	/** 剩余筹码*/
	public static final String TOTALSCORE= "totalscore";
	/** 血拼使用钻石数量*/
	public static final String DIAMONDS= "diamonds";
	/** 下注列表*/
	public static final String BET_CHIPS_LIST= "bet_chips_list";
	public static final String BET_MUL_LIST= "betMulList";
//	------------- action time ----------------------------
	/** 下注倒计时 */
	public static final long PLAYER_CARD_TIME = 20000;
	/** 解散倒计时 */
	public static final long DISSOLVE_ROOM_TIME = 30000;
	/** 请求过于频繁 */
	public static final String PLAYER_ASK_TOO_FAST = "10050";
	/** 不能开始游戏 */
	public static final String CANNOT_START_GAME = "10051";
	/** 请求CMD错误 */
	public static final String PLAYER_ASK_WRONG = "10060";
	/** 不能离开房间 */
	public static final String PLAYER_DONOT_LEAVEROOM = "10061";
	
	/**
     * 豹子喜钱倍数
     */
    public static final int ROOM_BAOREWARD_MULTIPLE = 4;
    /**
     * 顺金喜钱倍数
     */
    public static final int ROOM_SHUNREWARD_MULTIPLE = 2;
}
