package com.dmg.lobbyserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.lobbyserver.dao.bean.UserPromotionLogBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户优惠日志
 * 
 * @author mice
 * @email .com
 * @date 2019-06-17 18:38:38
 */
@Mapper
public interface UserPromotionLogDao extends BaseMapper<UserPromotionLogBean> {
    @Select("select user_id,promotion_type,promotion_number,create_date from user_promotion_log where promotion_type=#{type} " +
            " and create_date Between #{startTime} and #{endTime} and user_id=#{id}")
    List<UserPromotionLogBean> getUserPromotion(@Param("type") int type, @Param("startTime")String startTime, @Param("endTime") String endTime, @Param("id") String id);
	
}
