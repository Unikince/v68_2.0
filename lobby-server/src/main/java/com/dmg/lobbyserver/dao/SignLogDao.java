package com.dmg.lobbyserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.lobbyserver.dao.bean.SignLogBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 签到记录表
 * 
 * @author mice
 * email .com
 * date 2019-06-17 18:38:38
 */
@Mapper
public interface SignLogDao extends BaseMapper<SignLogBean> {

    @Select("select * from sign_log where user_id = #{userId} order by sign_day")
    List<SignLogBean> findAllByUserId(Long userId);
    @Select("select count(*) from sign_log where user_id = #{userId} and sign_day = #{dayOfWeek}")
    Integer findByUserIdAndDay(@Param("userId") Long userId, @Param("dayOfWeek") Integer dayOfWeek);
}
