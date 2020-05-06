package com.dmg.gameconfigserver.model.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description
 * @Author mice
 * @Date 2019/11/14 14:38
 * @Version V1.0
 **/
@Data
public class AppInfoVO {
    /**
     * app版本
     */
    @NotNull(message = "密码不能为空")
    private String appVersion;
    /**
     * 更新人id
     */
    @NotNull(message = "密码不能为空")
    private Long updatorId;
}