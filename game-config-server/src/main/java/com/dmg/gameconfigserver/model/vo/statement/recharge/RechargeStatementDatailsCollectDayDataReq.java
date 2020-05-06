package com.dmg.gameconfigserver.model.vo.statement.recharge;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.dmg.gameconfigserver.model.dto.PageReqDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 充值报表_游戏详情_每日数据_请求
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RechargeStatementDatailsCollectDayDataReq extends PageReqDTO {
    /** 充值渠道 */
    @NotNull(message = "channel不能为空")
    private String channel;
    /** 查询起始时间(包含) */
    private Date startDate;
    /** 查询结束时间(包含) */
    private Date endDate;
}