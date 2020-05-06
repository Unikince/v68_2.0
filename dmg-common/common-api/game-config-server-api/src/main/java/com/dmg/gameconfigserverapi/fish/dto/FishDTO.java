package com.dmg.gameconfigserverapi.fish.dto;

import java.io.Serializable;

/**
 * 捕鱼-鱼
 */
public class FishDTO implements Serializable {
    /** 串行版本标识 */
    private static final long serialVersionUID = 1L;
    /** 鱼id */
    private int id;
    /** 鱼类型(1:倍数鱼,3:同类炸弹(鱼王)) */
    private int type;
    /** 倍数鱼最小倍率 */
    private int minMultiple;
    /** 倍数鱼最大倍率 */
    private int maxMultiple;
    /** 描述 */
    private String description;

    /**
     * 获取：鱼id
     *
     * @return 鱼id
     */
    public int getId() {
        return this.id;
    }

    /**
     * 设置：鱼id
     *
     * @param id 鱼id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 获取：鱼类型(1:倍数鱼,3:同类炸弹(鱼王))
     *
     * @return 鱼类型(1:倍数鱼,3:同类炸弹(鱼王))
     */
    public int getType() {
        return this.type;
    }

    /**
     * 设置：鱼类型(1:倍数鱼,3:同类炸弹(鱼王))
     *
     * @param type 鱼类型(1:倍数鱼,3:同类炸弹(鱼王))
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * 获取：倍数鱼最小倍率
     *
     * @return 倍数鱼最小倍率
     */
    public int getMinMultiple() {
        return this.minMultiple;
    }

    /**
     * 设置：倍数鱼最小倍率
     *
     * @param minMultiple 倍数鱼最小倍率
     */
    public void setMinMultiple(int minMultiple) {
        this.minMultiple = minMultiple;
    }

    /**
     * 获取：倍数鱼最大倍率
     *
     * @return 倍数鱼最大倍率
     */
    public int getMaxMultiple() {
        return this.maxMultiple;
    }

    /**
     * 设置：倍数鱼最大倍率
     *
     * @param maxMultiple 倍数鱼最大倍率
     */
    public void setMaxMultiple(int maxMultiple) {
        this.maxMultiple = maxMultiple;
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
