/**
 * 
 */
package com.dmg.zhajinhuaserver.logic;


import com.dmg.zhajinhuaserver.config.Config;

/**
 * @author Administrator
 * 机器人行为概率
 */
public enum RobotActionState {

	// 加注_初始概率
	ADD_BET_UP_LV(10),
	// 加注_轮次增加概率
	ADD_BET_ADD_LOOP_UP_LV(5),
	// 加注_游戏胜利增加概率
	ADD_BET_SUCCESS_UP_LV(5),
	// 加注_游戏失败降低概率
	ADD_BET_FAIL_DOWN_LV(-10),
	
	// 正常游戏_初始概率
	COMMON_GAME_INIT_LV(100),
	// 正常游戏_加注降低概率
	COMMON_GAME_ADD_BET_DOWN_LV(-5),
	// 正常游戏_轮次降低概率
	COMMON_GAME_ADD_LOOP_DOWN_LV(-5),
	// 正常游戏_散牌降低概率
	COMMON_GAME_DANZHANG_DOWN_LV(-10),
	// 正常游戏_对子降低概率
	COMMON_GAME_DUIZI_DOWN_LV(-5),
	// 正常游戏_顺子每轮概率
	COMMON_GAME_SHUNZI_DOWN_LV(40),
	// 正常游戏_同花每轮概率
	COMMON_GAME_TONGHUA_UP_LV(60),
	// 正常游戏_同花顺每轮概率
	COMMON_GAME_JINHUA_UP_LV(80),
	// 正常游戏_豹子每轮概率
	COMMON_GAME_BAOZI_UP_LV(100),
	// 闷牌_初始概率
	MEN_POKER_INIT_LV(50),
	// 闷牌_游戏胜利降低概率
	MEN_POKER_SUCCESS_DOWN_LV(10),
	// 闷牌_游戏失败增加概率
	MEN_POKER_FAIL_UP_LV(-5),
	// 闷牌_加注增加概率
	MEN_POKER_ADD_BET_UP_LV(-2),
	// 闷牌_比牌降低概率
	MEN_POKER_PEEP_POKER_DOWN_LV(-10),
	// 比牌_初始概率
	BATTLE_POKER_INIT_LV(0),
	// 比牌_轮次增加概率
	BATTLE_POKER_ADD_LOOP_UP_LV(5),
	// 比牌_加注增加概率
	BATTLE_POKER_ADD_BET_UP_LV(5),
	// 比牌_偷看手牌增加概率(及时止损,不能太假直接丢牌)
	BATTLE_POKER_PEEP_POKER_UP_LV(50),
	// 大牌装小牌_初始概率
	ZB_BIG_INIT_LV(50),
	// 大牌装小牌_游戏胜利降低概率
	ZB_BIG_SUCCESS_DOWN_LV(-10),
	// 大牌装小牌_游戏失败增加概率
	ZB_BIG_FAIL_UP_LV(2),
	// 大牌装小牌_轮次降低概率
	ZB_BIG_ADD_LOOP_DOWN_LV(-2),
	// 大牌装小牌_加注降低概率
	ZB_BIG_ADD_BET_DOWN_LV(-2),
	// 小牌装大牌_初始概率
	ZB_SMALL_INIT_LV(50),
	// 小牌装大牌_游戏胜利增加概率
	ZB_SMALL_SUCCESS_UP_LV(10),
	// 小牌装大牌_游戏失败降低概率
	ZB_SMALL_FAIL_DOWN_LV(-2),
	// 小牌装大牌_轮次降低概率
	ZB_SMALL_ADD_LOOP_DOWN_LV(-2),
	// 小牌装大牌_加注降低概率
	ZB_SMALL_ADD_BET_DOWN_LV(-2),
	// 偷看手牌_初始概率
	PEEP_POKER_INIT_LV(20),
	// 偷看手牌_游戏胜利降低概率
	PEEP_POKER_SUCCESS_DOWN_LV(-5),
	// 偷看手牌_游戏失败增加概率
	PEEP_POKER_FAIL_UP_LV(5);
	private int value;
	private RobotActionState(int value){
		this.value = value;
	}
	/**
	 * 获取指定牌型对于正常游戏概率的影响
	 * @param handCardsType
	 * @return
	 */
	public static int checkCombinationByType(int handCardsType){
		Config.Combination combination = Config.Combination.forValue(handCardsType);
		switch (combination) {
		case HIGHCARD:
			return RobotActionState.COMMON_GAME_DANZHANG_DOWN_LV.getValue();
		case PAIR:
			return RobotActionState.COMMON_GAME_DUIZI_DOWN_LV.getValue();
		case PROGRESSION:
			return RobotActionState.COMMON_GAME_SHUNZI_DOWN_LV.getValue();
		case FLUSH:
			return RobotActionState.COMMON_GAME_TONGHUA_UP_LV.getValue();
		case STRAIGHTFLUSH:
			return RobotActionState.COMMON_GAME_JINHUA_UP_LV.getValue();
		case LEOPARD:
			return RobotActionState.COMMON_GAME_BAOZI_UP_LV.getValue();
		default:
			return 0;
		}
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
}
