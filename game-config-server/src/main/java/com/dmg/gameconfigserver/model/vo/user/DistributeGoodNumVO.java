package com.dmg.gameconfigserver.model.vo.user;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description
 * @Author mice
 * @Date 2020/3/25 15:25
 * @Version V1.0
 **/
@Data
public class DistributeGoodNumVO {
    @NotNull
    private Long userId;
    @NotNull
    private Long operatorId;
    @NotNull
    private Long goodNumber;
}