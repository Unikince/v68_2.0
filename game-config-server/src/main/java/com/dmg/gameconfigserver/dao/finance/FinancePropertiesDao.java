package com.dmg.gameconfigserver.dao.finance;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.gameconfigserver.model.bean.finance.FinancePropertiesBean;

/**
 * 财务属性
 */
@DS("v68")
public interface FinancePropertiesDao extends BaseMapper<FinancePropertiesBean> {

}
