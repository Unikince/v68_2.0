package com.dmg.lobbyserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.lobbyserver.dao.bean.WithdrawOrderBean;
import com.dmg.lobbyserver.model.dto.WithdrawOrderDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * 
 * 
 * @author mice
 * @email .com
 * @date 2019-12-30 14:07:58
 */
@Mapper
public interface WithdrawOrderDao extends BaseMapper<WithdrawOrderBean> {

    @Select("select @curRank := @curRank + 1 as rank,t.withdraw_amount,t.service_charges,t.account, t.bank_type,t.apply_date,t.order_status " +
            "from t_withdraw_order t,(select @curRank := 0) r where t.user_id=#{userId} ORDER BY t.apply_date desc LIMIT 0,#{limit}")
    List<WithdrawOrderDTO> selectOrder(@Param("userId")Long userId,@Param("limit")int limit);

    @Select("SELECT sum(withdraw_amount) from t_withdraw_order where user_id = #{userId} and to_days(apply_date) = to_days(now())")
    BigDecimal sumWithdrawTotalToday(Long userId);

    @Select("SELECT count(*) from t_withdraw_order where user_id = #{userId} and to_days(apply_date) = to_days(now())")
    Integer countWithdrawalTimeToday(Long userId);
}
