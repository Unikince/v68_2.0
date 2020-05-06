package com.dmg.gameconfigserverapi.fish.dto;

import java.io.Serializable;

/**
 * 捕鱼库存控制
 */
public class FishCtrlStockDTO implements Serializable {
    /** 控制变化值key */
    public static final String CHANGE_REDIS_KEY = "fish:stock_ctrl:room_";

    /** 串行版本标识 */
    private static final long serialVersionUID = 1L;
    /** id(场次id) */
    private Integer id;
    /** 目标 */
    private Long target;
    /** 启用模型 */
    private Integer model;
    /** 类型(1>=,2>,3<=,4<) */
    private Integer type;
    /** 流水 */
    private Long runningWater;
    /** 是否启用 */
    private boolean status;

    /**
     * 获取：id(场次id)
     *
     * @return id(场次id)
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * 设置：id(场次id)
     *
     * @param id id(场次id)
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取：目标
     *
     * @return 目标
     */
    public Long getTarget() {
        return this.target;
    }

    /**
     * 设置：目标
     *
     * @param target 目标
     */
    public void setTarget(Long target) {
        this.target = target;
    }

    /**
     * 获取：启用模型
     *
     * @return 启用模型
     */
    public Integer getModel() {
        return this.model;
    }

    /**
     * 设置：启用模型
     *
     * @param model 启用模型
     */
    public void setModel(Integer model) {
        this.model = model;
    }

    /**
     * 获取：类型(1>=,2>,3<=,4<)
     *
     * @return 类型(1>=,2>,3<=,4<)
     */
    public Integer getType() {
        return this.type;
    }

    /**
     * 设置：类型(1>=,2>,3<=,4<)
     *
     * @param type 类型(1>=,2>,3<=,4<)
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取：流水
     *
     * @return 流水
     */
    public Long getRunningWater() {
        return this.runningWater;
    }

    /**
     * 设置：流水
     *
     * @param runningWater 流水
     */
    public void setRunningWater(Long runningWater) {
        this.runningWater = runningWater;
    }

    /**
     * 获取：是否启用
     *
     * @return 是否启用
     */
    public boolean isStatus() {
        return this.status;
    }

    /**
     * 设置：是否启用
     *
     * @param status 是否启用
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * 获取：控制变化值key
     *
     * @return 控制变化值key
     */
    public static String getChangeRedisKey() {
        return CHANGE_REDIS_KEY;
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
