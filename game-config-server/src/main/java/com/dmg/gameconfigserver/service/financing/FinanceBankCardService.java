package com.dmg.gameconfigserver.service.financing;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.gameconfigserver.model.bean.finance.FinanceBankCardBean;
import com.dmg.gameconfigserver.model.vo.finance.FinanceBankCardListRecv;

/**
 * 财务银行卡
 */
public interface FinanceBankCardService {
    /** 新增 */
    void saveOne(FinanceBankCardBean bean);

    /** 删除 */
    void delete(Long id);

    /** 更新 */
    void updateOne(FinanceBankCardBean bean);

    /** 获取 */
    FinanceBankCardBean get(Long id);

    /** 获取列表 */
    IPage<FinanceBankCardBean> getList(FinanceBankCardListRecv reqVo);

    /** 充值 */
    void payMoney(Long id, BigDecimal money);

}
