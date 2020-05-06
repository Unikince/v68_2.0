package com.dmg.gameconfigserver.dao.finance;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.gameconfigserver.model.bean.finance.WithdrawOrderBean;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

/**
 * 
 * 
 * @author mice
 * @email .com
 * @date 2019-12-30 14:07:58
 */
@Mapper
@DS("v68")
public interface WithdrawOrderDao extends BaseMapper<WithdrawOrderBean> {

    @Update("update t_withdraw_order set lock_user_id = null where id = #{id}")
    void clearLockUser(long id);

    @Select("SELECT SUM(withdraw_amount) FROM t_withdraw_order WHERE order_status = #{status} AND account_date IS NOT NULL AND to_days(account_date) = to_days(now())")
    BigDecimal countTodayWithdraw(@Param("status") int status);
}
