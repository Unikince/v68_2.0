package com.dmg.bairenniuniuserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.bairenniuniuserver.dao.bean.UserGameRecordBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户游戏战绩
 */
@Mapper
public interface UserGameRecordDao extends BaseMapper<UserGameRecordBean> {
    /**
     * 展示最近30条游戏记录
     */
    @Select("select *from user_game_record where user_id=#{id} and game_type=#{type} order by end_time desc limit 0,30" )
    List<UserGameRecordBean> getRecord(@Param("id") Long userId, @Param("type") Integer type);
}
