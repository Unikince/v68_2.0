package com.dmg.gameconfigserver.model.vo.finance;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description
 * @Author mice
 * @Date 2019/12/31 15:55
 * @Version V1.0
 **/
@Data
public class OperateOrderVO {
    @NotNull
    private Long operatorId;
    @NotNull
    private Long id;

    private String remark;
}