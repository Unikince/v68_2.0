package com.dmg.bjlserver.business.model.game;

/**
 *
 */
public class SeatPlayer {
    /** 玩家ID */
    private long playerId;
    /** 玩家昵称 */
    private String nickName;
    /** 头像 */
    private String icon;
    /** 金币(上局) */
    private long gold;
    /** 输赢总金币（当日） */
    private long dayGoldTotal;

    /**
     * 获取：玩家ID
     *
     * @return 玩家ID
     */
    public long getPlayerId() {
        return this.playerId;
    }

    /**
     * 设置：玩家ID
     *
     * @param playerId 玩家ID
     */
    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    /**
     * 获取：玩家昵称
     *
     * @return 玩家昵称
     */
    public String getNickName() {
        return this.nickName;
    }

    /**
     * 设置：玩家昵称
     *
     * @param nickName 玩家昵称
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * 获取：头像
     *
     * @return 头像
     */
    public String getIcon() {
        return this.icon;
    }

    /**
     * 设置：头像
     *
     * @param icon 头像
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * 获取：金币(上局)
     *
     * @return 金币(上局)
     */
    public long getGold() {
        return this.gold;
    }

    /**
     * 设置：金币(上局)
     *
     * @param gold 金币(上局)
     */
    public void setGold(long gold) {
        this.gold = gold;
    }

    /**
     * 获取：输赢总金币（当日）
     *
     * @return 输赢总金币（当日）
     */
    public long getDayGoldTotal() {
        return this.dayGoldTotal;
    }

    /**
     * 设置：输赢总金币（当日）
     *
     * @param dayGoldTotal 输赢总金币（当日）
     */
    public void setDayGoldTotal(long dayGoldTotal) {
        this.dayGoldTotal = dayGoldTotal;
    }

}
