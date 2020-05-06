package com.dmg.gameconfigserver.service.financing;

import com.dmg.gameconfigserver.model.vo.finance.FinancePropertiesVo;

/**
 * 财务属性
 */
public interface FinancePropertiesService {
    /** 更新 */
    void updateOne(FinancePropertiesVo vo);

    /** 获取 */
    FinancePropertiesVo get();
}
