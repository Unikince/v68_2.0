package com.dmg.gameconfigserver.dao.finance;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.gameconfigserver.model.bean.finance.PersionRechargeLogBean;

/**
 * 人工充值
 */
@DS("v68")
public interface PersionRechargeLogDao extends BaseMapper<PersionRechargeLogBean> {
    @Select("select deal_user from t_persion_recharge_log where deal_user is not null group by deal_user")
    public List<String> allDealUser();

    @Update("update t_persion_recharge_log set status=3,deal_date=now(),deal_remark='超时拒绝' where status=1 and create_date<#{lastDay}")
    public void autoPersionRechargeAuditDeny(@Param("lastDay") Date lastDay);
}
