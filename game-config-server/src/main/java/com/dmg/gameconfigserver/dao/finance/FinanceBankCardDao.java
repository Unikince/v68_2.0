package com.dmg.gameconfigserver.dao.finance;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.gameconfigserver.model.bean.finance.FinanceBankCardBean;

/**
 * 财务银行卡
 */
@DS("v68")
public interface FinanceBankCardDao extends BaseMapper<FinanceBankCardBean> {
    @Update("update finance_bank_card set id=#{newId} where id=#{oldId}")
    public void sortId(@Param("oldId") long oldId, @Param("newId") long newId);

}
