package com.dmg.zhajinhuaserver.model.bean;


/**
 * 自定义规则, 用来保存玩法规则
 * @author: hxf
 * @Date 2018/5/22
 */
public class CustomRule {
	private int baoXiBet;//豹子喜钱倍数
	private int shunXiBet;//顺金喜钱倍数
	private int firstRule = 1;//先手规则
	private int menRule = 2;//闷牌规则
	private int rule235 = 2;//235规则
	private boolean ruleA23;//A23规则
	private boolean canIn;//中途可进
	private boolean overTurns;//增加下注轮数
	public CustomRule(GameRoom room) {
    	int rule = room.getRule();
    	int extraRule = room.getExtraRule();
    	//最大局数
    	/*if((rule & 0x1) != 0) {
    		room.setTotalRound(10);
    		room.setCost(3);
    	}
    	if((rule & 0x2) != 0) {
    		room.setTotalRound(20);
    		room.setCost(5);
    	}
    	 */
    	//喜钱
    	if((rule & 0x10) != 0) {
    		baoXiBet = 2;
    		shunXiBet = 1;
    	}
    	if((rule & 0x20) != 0) {
    		baoXiBet = 4;
    		shunXiBet = 2;
    	}
    	if((rule & 0x40) != 0) {
    		baoXiBet = 8;
    		shunXiBet = 4;
    	}
    	if((rule & 0x80) != 0) {
    		baoXiBet = 10;
    		shunXiBet = 5;
    	}
    	//先手顺序
    	if((rule & 0x100) != 0) {
    		firstRule = 0;//赢家下家先
    	}
//    	if((rule & 0x200) != 0) {
//    		firstRule = 1;//赢家先（默认）
//    	}
    	if((rule & 0x400) != 0) {
    		firstRule = 2;//轮流上
    	}
    	//支付
    	if((rule & 0x1000) != 0) {
    		//房主支付
    		room.setCostType(0);
    	}
    	if((rule & 0x2000) != 0) {
    		//大赢家支付
    		room.setCostType(1);
    	}
    	if((rule & 0x4000) != 0) {
    		//aa支付
    		room.setCostType(2);
    	}
    	//闷牌规则
    	if((rule & 0x10000) != 0) {
    		menRule = 0;//首轮必门
    	}
    	if((rule & 0x20000) != 0) {
    		menRule = 1;//首家必闷
    	}
//    	if((rule & 0x40000) != 0) {
//    		menRule = 2;//（默认）可不闷
//    	}
    	//其他规则
    	if((rule & 0x100000) != 0) {
    		canIn = true;//中途可入
    	}
    	if((rule & 0x200000) != 0) {
    		overTurns = true;//轮数加倍
    	}
    	//235规则
    	if((extraRule & 0x1) != 0) {
    		rule235 = 0;//253大豹子
    	}
    	if((extraRule & 0x2) != 0) {
    		rule235 = 1;//235 大AAA
    	}
    	//A23规则
    	if((extraRule & 0x10) != 0) {
    		ruleA23 = true;//123 最小顺子
    	}
    	if((extraRule & 0x100) != 0) {
    		room.setUseRoomCard(true);
    	}
//    	if((extraRule & 0x20) != 0) {
//    		ruleA23 = false;//默认最大顺子
//    	}
    }
    public int getBaoXiBet() {
		return baoXiBet;
	}

	public void setBaoXiBet(int baoXiBet) {
		this.baoXiBet = baoXiBet;
	}

	public int getShunXiBet() {
		return shunXiBet;
	}

	public void setShunXiBet(int shunXiBet) {
		this.shunXiBet = shunXiBet;
	}

	public int getFirstRule() {
		return firstRule;
	}

	public void setFirstRule(int firstRule) {
		this.firstRule = firstRule;
	}

	public int getMenRule() {
		return menRule;
	}

	public void setMenRule(int menRule) {
		this.menRule = menRule;
	}

	public int getRule235() {
		return rule235;
	}

	public void setRule235(int rule235) {
		this.rule235 = rule235;
	}

	public boolean isRuleA23() {
		return ruleA23;
	}
	public void setRuleA23(boolean ruleA23) {
		this.ruleA23 = ruleA23;
	}
	public boolean isCanIn() {
		return canIn;
	}
	public void setCanIn(boolean canIn) {
		this.canIn = canIn;
	}
	public boolean isOverTurns() {
		return overTurns;
	}
	public void setOverTurns(boolean overTurns) {
		this.overTurns = overTurns;
	}


	
}
