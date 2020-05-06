package com.dmg.lobbyserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.lobbyserver.dao.bean.UserWithdrawalLogBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户提款日志
 * 
 * @author mice
 * @email .com
 * @date 2019-06-17 18:38:38
 */
@Mapper
public interface UserWithdrawalLogDao extends BaseMapper<UserWithdrawalLogBean> {
    @Select("select user_id,order_code,withdrawal_number,create_date,order_status from  user_withdrawal_log where order_status=#{type} " +
            "  and  create_date Between" +
            "#{startTime} and #{endTime}  and user_id=#{id}  ")
    List<UserWithdrawalLogBean> getUserWithdrawal(@Param("type") int type, @Param("startTime") String startTime, @Param("endTime") String endTime , @Param("id") String UserId);
	
}
