package com.dmg.gameconfigserver.model.vo.fish;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
 * 获取捕鱼库存控制
 */
public class FishCtrlStockReq implements Serializable {
    /** 串行版本标识 */
    private static final long serialVersionUID = 1L;
    /** 场次ID */
    @NotNull(message = "场次ID不能为空")
    private Integer id;

    /**
     * 获取：场次ID
     *
     * @return 场次ID
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * 设置：场次ID
     *
     * @param id 场次ID
     */
    public void setId(Integer id) {
        this.id = id;
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
