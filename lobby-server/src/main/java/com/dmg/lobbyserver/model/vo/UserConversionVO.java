package com.dmg.lobbyserver.model.vo;

import lombok.Data;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @Description
 * @Author  JOCK
 * @Date 2019/6/25 0025
 * @Version V1.0
 **/
@Data
public class UserConversionVO {
    @NotBlank
    private Long integral;//积分
    @NotBlank
    private Integer proportion;//兑换比列
    @NotBlank
    private Integer userCondition;//用户进度
    @NotBlank
    private Integer  allCondition;//总进度

}
