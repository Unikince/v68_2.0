package com.dmg.gameconfigserver.model.dto.config;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description
 * @Author mice
 * @Date 2019/9/26 19:46
 * @Version V1.0
 **/
@Data
public class BairenGameConfigDTO {

    /**
     * 场次id
     */
    private Integer fileId;
    /**
     * 汇率
     */
    private BigDecimal exchangeRate;

    /**
     * 控制配置
     */
    BairenControlConfigDTO bairenControlConfigDTO;

    /**
     * 游戏场次配置
     */
    BairenFileConfigDTO bairenFileConfigDTO;

    /**
     * 水池参数
     */
    private List<WaterPoolConfigDTO> waterPoolConfigDTOS;
    @Data
    public static class WaterPoolConfigDTO{
        /**
         * 顺序
         */
        private Integer waterOrder;
        /**
         * 低水位线
         */
        private Long waterLow;
        /**
         * 高
         */
        private Long waterHigh;
        /**
         * 胜率
         */
        private Double controlExecuteRate;
    }

}