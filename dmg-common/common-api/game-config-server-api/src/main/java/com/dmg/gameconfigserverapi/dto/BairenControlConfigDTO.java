package com.dmg.gameconfigserverapi.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @Description
 * @Author mice
 * @Date 2019/9/26 19:46
 * @Version V1.0
 **/
@Data
public class BairenControlConfigDTO {
    /**
     * 基础配置id
     */
    private Integer fileBaseConfigId;
    /**
     * 通杀闪避概率
     */
    private Double allWinDodgeRate;
    /**
     * 通赔闪避概率
     */
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
    private Integer maxPayout;
    /**
     * 最大赔付参考值
     */
    private Integer maxPayoutReferenceValue;
    /**
     * 牌型赔率
     */
    private JSONObject cardTypeMultiple;

}