package com.dmg.doudizhuserver.business.model;

/**
 * 斗地主游戏阶段枚举
 */
public enum Stage {
    WAIT(1, "等待"), DEAL_CARDS(2, "准备发牌"), CALL_LANDLORD(3, "叫地主"), GAME(4, "游戏"),;

    public final int val;
    public final String desc;

    private Stage(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }

}
