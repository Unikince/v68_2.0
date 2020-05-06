package com.dmg.gameconfigserver.model.vo.statement.player;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 玩家充值提款信息--用于用户详情
 */
@Data
public class PlayerChargeAndWithdrawInfo {
    /*** 总充值 */
    private BigDecimal sumRecharge;
    /*** 总提款 */
    private BigDecimal sumWithdraw;
    /*** 提存差 */
    private BigDecimal diffRechargeSubWithdraw;
}
