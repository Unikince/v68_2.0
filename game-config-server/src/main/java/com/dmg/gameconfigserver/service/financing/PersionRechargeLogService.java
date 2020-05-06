package com.dmg.gameconfigserver.service.financing;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.gameconfigserver.model.bean.finance.PersionRechargeLogBean;
import com.dmg.gameconfigserver.model.vo.finance.PersionRechargeLogAuditListRecv;
import com.dmg.gameconfigserver.model.vo.finance.PersionRechargeLogAuditOneSend;
import com.dmg.gameconfigserver.model.vo.finance.PersionRechargeLogAuditUpdateRecv;
import com.dmg.gameconfigserver.model.vo.finance.PersionRechargeLogCreateRecv;
import com.dmg.gameconfigserver.model.vo.finance.PersionRechargeLogOrderListRecv;

/**
 * 人工充值
 */
@Service
public interface PersionRechargeLogService {
    /** 创建订单 */
    void create(PersionRechargeLogCreateRecv reqVo, Long loginId);

    /** 人工充值订单列表 */
    IPage<PersionRechargeLogBean> persionRechargeOrderList(PersionRechargeLogOrderListRecv reqVo);

    /** 充值审核订单列表 */
    IPage<PersionRechargeLogBean> persionRechargeAuditList(PersionRechargeLogAuditListRecv reqVo);

    /** 充值审核人员列表 */
    List<String> persionRechargeAuditUserList();

    /** 充值审核订单单个 */
    PersionRechargeLogAuditOneSend persionRechargeAuditOne(Long id);

    /** 充值审核订单审核行为 */
    void persionRechargeAuditUpdate(PersionRechargeLogAuditUpdateRecv vo, Long loginId);

    /** 超时自动拒绝人工充值订单 */
    public void autoPersionRechargeAuditDeny();
}
