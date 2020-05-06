package com.dmg.lobbyserver.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author jock
 * @Date 2019/6/20 0020
 * @Version V1.0
 **/
@Data
public class SysVipPrivilegeConfigDTO {
    private Map<Integer,List<Double>> washCodeRatioMap;
    /**
     *  洗码金额
     */
    private BigDecimal monney;
}
