package com.dmg.lobbyserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.lobbyserver.dao.bean.UserRechargeLogBean;
import com.dmg.lobbyserver.dao.bean.VnnnRechargeLogBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * 用户充值日志
 * 
 * @author mice
 * @email .com
 * @date 2019-06-17 18:38:38
 */
@Mapper
public interface UserRechargeLogDao extends BaseMapper<VnnnRechargeLogBean> {

    /**
     * @description: 统计从某个时间点开始充值的总金额
     * @param userId
     * @param startDate
     * @return int
     * @author mice
     * @date 2019/6/20
    */
    @Select("select SUM(recharge_number) from user_recharge_log where user_id = #{userId} and create_date> #{startDate}")
    Integer countRechargeNumberFromDate(@Param("userId") Long userId, @Param("startDate")Date startDate);

    /**
     *  充值记录
     * @param startTime
     * @param endTime
     * @param type
     * @return
     */
    @Select("select user_id,recharge_type,recharge_number,create_date from user_recharge_log where  create_date Between #{startTime} and #{endTime} " +
            "and recharge_type=#{type} and user_id=#{id}")
    List<UserRechargeLogBean> getUserRechargeLog(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("type") int type, @Param("id") String id);

    /**
     *  查询本周内的数据
     * @return
     */
    @Select("SELECT * FROM user_recharge_log  WHERE YEARWEEK(date_format(create_date,'%Y-%m-%d')) = YEARWEEK(now())  and user_id=#{id}")
    List<UserRechargeLogBean> getrecordWeek(@Param("id") long id);



}
