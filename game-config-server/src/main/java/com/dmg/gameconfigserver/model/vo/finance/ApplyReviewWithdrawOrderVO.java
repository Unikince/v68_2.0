package com.dmg.gameconfigserver.model.vo.finance;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description
 * @Author mice
 * @Date 2019/12/30 20:31
 * @Version V1.0
 **/
@Data
public class ApplyReviewWithdrawOrderVO {
    @NotNull
    private Integer reviewerId;
    @NotNull
    private Long id;
}