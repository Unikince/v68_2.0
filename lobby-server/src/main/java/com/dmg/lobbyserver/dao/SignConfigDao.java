package com.dmg.lobbyserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.lobbyserver.dao.bean.SignConfigBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 签到配置表
 * 
 * @author mice
 */
@Mapper
public interface SignConfigDao extends BaseMapper<SignConfigBean> {

    @Select("select * from sign_config order by sign_day asc")
    List<SignConfigBean> findAll();
    @Select("select * from sign_config where sign_day = #{dayOfWeek}")
    SignConfigBean findByDay(Integer dayOfWeek);
}
