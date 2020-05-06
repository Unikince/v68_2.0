package com.dmg.gameconfigserver.model.vo.config.bairen;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description
 * @Author mice
 * @Date 2019/9/27 11:51
 * @Version V1.0
 **/
@Data
public class BairenWaterPoolConfigVO {

    private Integer id;
    /**
     * 基本场次
     */
    @NotNull(message = "fileBaseConfigId不能为空")
    private Integer fileBaseConfigId;
    /**
     * 顺序
     */
    @NotNull(message = "waterOrder不能为空")
    private Integer waterOrder;
    /**
     * 低水位线
     */
    @NotNull(message = "waterLow不能为空")
    private Long waterLow;
    /**
     * 高
     */
    @NotNull(message = "waterHigh不能为空")
    private Long waterHigh;
    /**
     * 胜率
     */
    @NotNull(message = "controlExecuteRate不能为空")
    private Double controlExecuteRate;
}