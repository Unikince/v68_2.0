package com.dmg.fish.business.platform.model;

import java.io.Serializable;

/**
 * 玩家信息
 */
public class Player implements Serializable {
    /** 串行版本标识 */
    private static final long serialVersionUID = 1L;
    /** 玩家id */
    private long id;
    /** 昵称 */
    private String nickname;
    /** 头像 */
    private String headImg;
    /** 性别 1:男 2:女 */
    private int sex;
    /** 金币 */
    private long gold;
    /** 是否是机器人 */
    private boolean robot;

    /**
     * 获取：玩家id
     *
     * @return 玩家id
     */
    public long getId() {
        return this.id;
    }

    /**
     * 设置：玩家id
     *
     * @param id 玩家id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * 获取：昵称
     *
     * @return 昵称
     */
    public String getNickname() {
        return this.nickname;
    }

    /**
     * 设置：昵称
     *
     * @param nickname 昵称
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 获取：头像
     * 
     * @return 头像
     */
    public String getHeadImg() {
        return this.headImg;
    }

    /**
     * 设置：头像
     * 
     * @param headImg 头像
     */
    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    /**
     * 获取：性别 1:男 2:女
     * 
     * @return 性别 1:男 2:女
     */
    public int getSex() {
        return this.sex;
    }

    /**
     * 设置：性别 1:男 2:女
     * 
     * @param sex 性别 1:男 2:女
     */
    public void setSex(int sex) {
        this.sex = sex;
    }

    /**
     * 获取：金币
     *
     * @return 金币
     */
    public long getGold() {
        return this.gold;
    }

    /**
     * 设置：金币
     *
     * @param gold 金币
     */
    public void setGold(long gold) {
        this.gold = gold;
    }

    /**
     * 获取：是否是机器人
     *
     * @return 是否是机器人
     */
    public boolean isRobot() {
        return this.robot;
    }

    /**
     * 设置：是否是机器人
     *
     * @param robot 是否是机器人
     */
    public void setRobot(boolean robot) {
        this.robot = robot;
    }

    /**
     * 获取：串行版本标识
     *
     * @return 串行版本标识
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
