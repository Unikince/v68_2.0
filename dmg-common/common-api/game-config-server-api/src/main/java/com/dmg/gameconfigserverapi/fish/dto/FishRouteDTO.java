package com.dmg.gameconfigserverapi.fish.dto;

import java.io.Serializable;

/**
 * 捕鱼-鱼路线图
 */
public class FishRouteDTO implements Serializable {
    /** 串行版本标识 */
    private static final long serialVersionUID = 1L;
    /** 路径id */
    private Integer id;
    /** 路径配置数据(json) */
    private String data;
    /** 描述 */
    private String description;

    /**
     * 获取：路径id
     *
     * @return 路径id
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * 设置：路径id
     *
     * @param id 路径id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取：路径配置数据(json)
     *
     * @return 路径配置数据(json)
     */
    public String getData() {
        return this.data;
    }

    /**
     * 设置：路径配置数据(json)
     *
     * @param data 路径配置数据(json)
     */
    public void setData(String data) {
        this.data = data;
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
