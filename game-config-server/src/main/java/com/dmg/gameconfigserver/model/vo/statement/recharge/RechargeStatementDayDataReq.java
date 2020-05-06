package com.dmg.gameconfigserver.model.vo.statement.recharge;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.dmg.gameconfigserver.model.dto.PageReqDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 充值报表_每日数据_请求
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RechargeStatementDayDataReq extends PageReqDTO {
    /** 充值类型(人工充值、渠道充值) */
    @NotNull(message = "type不能为空")
    private String type;
    /** 查询起始时间(包含) */
    private Date startDate;
    /** 查询结束时间(包含) */
    private Date endDate;
}