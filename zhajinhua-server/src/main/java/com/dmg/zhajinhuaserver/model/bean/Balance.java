package com.dmg.zhajinhuaserver.model.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhuqd
 * @Date 2017年8月29日
 * @Desc 结算数据
 */
@Data
public class Balance {
    private long roleId;
    private int round; // 局数
    private BigDecimal totalscore; // 总赢分
    private BigDecimal winscore; // 赢分
    private boolean iswin; // 是否是赢家
    private int card_type; //牌型
    private List<Poker> hand_cards; //手牌
    private long takebackscore; //返现
    private int seat_index; //座位号
    private String rolename; //玩家名字
    private BigDecimal pumpMoney;//抽水金额
	private BigDecimal betChips;//下注金额
    private Boolean isRobot;

    public Balance(long roleId, int round, String rolename) {
        this.roleId = roleId;
        this.round = round;
        this.totalscore = BigDecimal.ZERO;
        this.rolename = rolename;
        this.winscore = BigDecimal.ZERO;
        this.pumpMoney = BigDecimal.ZERO;
    }
}
