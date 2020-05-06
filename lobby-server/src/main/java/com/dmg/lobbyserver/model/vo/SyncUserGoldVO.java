package com.dmg.lobbyserver.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/4 16:37
 * @Version V1.0
 **/
@Data
public class SyncUserGoldVO {
    private Long userId;
    /**类型,小于100为游戏,101渠道充值,102人工充值,103活动赠送,104任务赠送*/
    private Integer type;
    private BigDecimal gold;
}