package com.dmg.gameconfigserver.dao.finance;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.gameconfigserver.model.bean.finance.PlatformRechargeLogBean;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author mice
 * @email .com
 * @date 2019-11-26 17:51:48
 */
@Mapper
@DS("v68")
public interface PlatformRechargeLogDao extends BaseMapper<PlatformRechargeLogBean> {

    @Select("select sum(item_price) from t_platform_recharge_log where to_days(create_date) = to_days(now())")
    Long rechargeValue();

    @Select("select count(DISTINCT user_code) from t_platform_recharge_log where to_days(create_date) = to_days(now())")
    long rechargePlayer();

    @Select("SELECT DISTINCT id FROM t_platform_recharge_log WHERE order_status = 15 AND arrive_date IS NOT NULL AND to_days(arrive_date) = to_days(now())")
    List<Long> countTodayRecharge();

    @Select("<script> SELECT COUNT(DISTINCT id) FROM t_platform_recharge_log WHERE order_status = 15 " +
            "AND arrive_date IS NOT NULL AND to_days(arrive_date) &lt; to_days(now()) " +
            "AND id IN <foreach item='item' index='index' collection='list' open='(' separator=',' close=')'> #{item} </foreach>"
            + "</script>")
    Long countRecharge(@Param("list") List<Long> id);

    @Select("SELECT SUM(recharge_amount) FROM t_platform_recharge_log WHERE order_status = 15 AND arrive_date IS NOT NULL AND to_days(arrive_date) = to_days(now())")
    BigDecimal countTodayRechargeAmount();

    @Select("SELECT SUM(b.recharge_amount) FROM (SELECT * FROM (SELECT recharge_amount, user_id FROM t_platform_recharge_log WHERE order_status = 15 AND arrive_date IS NOT NULL AND to_days(arrive_date) = to_days(now()) ORDER BY arrive_date asc) a GROUP BY a.user_id ORDER BY NULL) b")
    BigDecimal countTodayFirstRechargeAmount();


}
