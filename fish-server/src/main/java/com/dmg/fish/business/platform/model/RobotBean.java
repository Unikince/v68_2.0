package com.dmg.fish.business.platform.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 机器人实体类
 */
@TableName("robot")
public class RobotBean implements Serializable {
    /** 串行版本标识 */
    private static final long serialVersionUID = 1L;
    /** 机器人id */
    @TableId(type = IdType.AUTO)
    private long id;
    /** 头像地址 */
    private String headImg;
    /** 昵称 */
    private String nickname;
    /** 性别 */
    private Integer sex;
    /** 金币 */
    private Integer gold;

    /**
     * 获取：机器人id
     *
     * @return 机器人id
     */
    public long getId() {
        return this.id;
    }

    /**
     * 设置：机器人id
     *
     * @param id 机器人id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * 获取：头像地址
     *
     * @return 头像地址
     */
    public String getHeadImg() {
        return this.headImg;
    }

    /**
     * 设置：头像地址
     *
     * @param headImg 头像地址
     */
    public void setHeadImg(String headImg) {
        this.headImg = headImg;
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
     * 获取：性别
     *
     * @return 性别
     */
    public Integer getSex() {
        return this.sex;
    }

    /**
     * 设置：性别
     *
     * @param sex 性别
     */
    public void setSex(Integer sex) {
        this.sex = sex;
    }

    /**
     * 获取：金币
     *
     * @return 金币
     */
    public Integer getGold() {
        return this.gold;
    }

    /**
     * 设置：金币
     *
     * @param gold 金币
     */
    public void setGold(Integer gold) {
        this.gold = gold;
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
