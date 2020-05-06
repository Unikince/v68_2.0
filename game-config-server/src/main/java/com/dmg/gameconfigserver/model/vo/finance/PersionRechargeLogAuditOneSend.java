package com.dmg.gameconfigserver.model.vo.finance;

import com.dmg.gameconfigserver.model.bean.finance.PersionRechargeLogBean;
import com.dmg.gameconfigserver.model.vo.user.UserDetailVO;

import lombok.Data;

/**
 * 充值审核订单单个
 */
@Data
public class PersionRechargeLogAuditOneSend {
    private PersionRechargeLogBean persionRechargeLog;
    private UserDetailVO userDetail;
}
