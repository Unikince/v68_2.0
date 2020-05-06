package com.dmg.bairenlonghu.common.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description:
 * @author mice
 * @date 2019/7/29
*/
@Data
public class BaseSeat implements Serializable {

    private static final long serialVersionUID = 1L;
    private int seatIndex;
    private BasePlayer player;
    private boolean ready = false; // 是否准备好
    private BigDecimal curWinGold = new BigDecimal(0); // 当局赢分
    private BigDecimal totalWinGold = new BigDecimal(0); // 总赢分
    private boolean leave = false;//是否离开
    private int gameCount;// 该玩家已经在此房间游戏的局数
    private int bankGameCount;// 当庄游戏的局数
    private int bankGameCountLimit;// 当庄游戏的局数限制

}
