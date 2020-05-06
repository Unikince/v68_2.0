package com.dmg.gameconfigserverapi.fish.dto;

import java.io.Serializable;

/**
 * 捕鱼场景
 */
public class FishScenceDTO implements Serializable {
    /** 串行版本标识 */
    private static final long serialVersionUID = 1L;
    /** 场景id */
    private Integer id;
    /** 刷鱼策略 */
    private String strategys;
    /** 持续时间(秒) */
    private Integer time;
    /** 描述 */
    private String description;

    /**
     * 获取：场景id
     *
     * @return 场景id
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * 设置：场景id
     *
     * @param id 场景id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取：刷鱼策略
     *
     * @return 刷鱼策略
     */
    public String getStrategys() {
        return this.strategys;
    }

    /**
     * 设置：刷鱼策略
     *
     * @param strategys 刷鱼策略
     */
    public void setStrategys(String strategys) {
        this.strategys = strategys;
    }

    /**
     * 获取：持续时间(秒)
     *
     * @return 持续时间(秒)
     */
    public Integer getTime() {
        return this.time;
    }

    /**
     * 设置：持续时间(秒)
     *
     * @param time 持续时间(秒)
     */
    public void setTime(Integer time) {
        this.time = time;
    }

    /**
     * 获取：描述
     *
     * @return 描述
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * 设置：描述
     *
     * @param description 描述
     */
    public void setDescription(String description) {
        this.description = description;
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
