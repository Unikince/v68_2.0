package com.dmg.lobbyserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.lobbyserver.dao.bean.IntegralExchangeLogBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 积分兑换日志表
 * 
 * @author mice
 * @email .com
 * @date 2019-06-17 18:38:38
 */
@Mapper
public interface IntegralExchangeLogDao extends BaseMapper<IntegralExchangeLogBean> {
    /**
     * 查询最近7天 的数据
     * @return
     */
	@Select("SELECT * FROM integral_exchange_log where user_id= #{userId} and DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= exchange_date ORDER BY exchange_date DESC")
    List<IntegralExchangeLogBean> getIntegrals(Long userId);
}
