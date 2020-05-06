package com.dmg.lobbyserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.lobbyserver.dao.bean.VnnnRechargeLogBean;
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
public interface VnnnRechargeLogDao extends BaseMapper<VnnnRechargeLogBean> {

    @Select("select sum(item_price) from t_vnnn_recharge_log where to_days(create_date) = to_days(now())")
    Long rechargeValue();

    @Select("select count(DISTINCT user_code) from t_vnnn_recharge_log where to_days(create_date) = to_days(now())")
    long rechargePlayer();
	
}
