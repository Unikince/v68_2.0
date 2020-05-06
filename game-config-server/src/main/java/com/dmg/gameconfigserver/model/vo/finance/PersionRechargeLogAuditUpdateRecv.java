package com.dmg.gameconfigserver.model.vo.finance;

import lombok.Data;

/**
 * 充值审核审核行为
 */
@Data
public class PersionRechargeLogAuditUpdateRecv {
    /** id */
    private Long id;
    /** 状态 1:等待审核,2:审核通过,3:审核拒绝 */
    private Integer status = 1;
    /** 处理备注 */
    private String dealRemark;
}
