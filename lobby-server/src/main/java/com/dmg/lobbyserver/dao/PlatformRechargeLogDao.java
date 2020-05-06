package com.dmg.lobbyserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.lobbyserver.dao.bean.PlatformRechargeLogBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 
 * 
 * @author mice
 * @email .com
 * @date 2019-11-26 17:51:48
 */
@Mapper
public interface PlatformRechargeLogDao extends BaseMapper<PlatformRechargeLogBean> {

    @Select("select sum(item_price) from t_platform_recharge_log where to_days(create_date) = to_days(now())")
    Long rechargeValue();

    @Select("select count(DISTINCT user_code) from t_platform_recharge_log where to_days(create_date) = to_days(now())")
    long rechargePlayer();

    @Select("SELECT count(*) FROM t_platform_recharge_log WHERE user_id=#{userId} and order_status>=15 limit 1")
    boolean selectPlayerHasRecharge(Long userId);

    @Select("SELECT sum(recharge_amount) FROM t_platform_recharge_log WHERE user_id=#{userId} and order_status>=15")
    Long sumPlayerRecharge(Long userId);
	
}
