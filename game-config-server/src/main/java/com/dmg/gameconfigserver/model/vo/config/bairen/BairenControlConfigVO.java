package com.dmg.gameconfigserver.model.vo.config.bairen;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description
 * @Author mice
 * @Date 2019/9/27 11:17
 * @Version V1.0
 **/
@Data
public class BairenControlConfigVO {
    /**
     *
     */
    private Integer id;
    /**
     * 游戏类型
     */
    @NotNull(message = "fileBaseConfigId不能为空")
    private Integer fileBaseConfigId;
    /**
     * 通杀闪避概率
     */
    @NotNull(message = "allWinDodgeRate不能为空")
    private Double allWinDodgeRate;
    /**
     * 通赔闪避概率
     */
    @NotNull(message = "allLoseDodgeRate不能为空")
    private Double allLoseDodgeRate;
    
    /**
     * 连开起始概率
     */
    private Double continueOpenStartRate;
    /**
     * 连开递增概率
     */
    private Double continueOpenAddRate;
    /**
     * 连开触发次数下限
     */
    private Integer continueOpenTriggerCount;
    /**
     * 最大赔付
     */
    @NotNull(message = "maxPayout不能为空")
    private Integer maxPayout;
    /**
     * 牌型赔率
     */
    @NotNull(message = "cardTypeMultiple不能为空")
    private JSONObject cardTypeMultiple;
}