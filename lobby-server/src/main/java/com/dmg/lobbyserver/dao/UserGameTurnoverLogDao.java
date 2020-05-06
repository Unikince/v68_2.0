package com.dmg.lobbyserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.lobbyserver.dao.bean.UserGameTurnoverLogBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户游戏流水日志
 * 
 * @author mice
 * @email .com
 * @date 2019-06-17 18:38:38
 */
@Mapper
public interface UserGameTurnoverLogDao extends BaseMapper<UserGameTurnoverLogBean> {
    /**
     *  游戏记录
     * @param type
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("select user_id,game_type,turnover_number,payout_number,create_date from user_game_turnover_log where game_type=1 and create_date \n" +
        "BETWEEN #{startTime} and  #{endTime} and user_id=#{id}")
List<UserGameTurnoverLogBean> getGamerecord(@Param("type") int type, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("id") String id);
    /**
     * 查询本周流水
     */
	@Select("SELECT * FROM  user_game_turnover_log  WHERE  YEARWEEK(date_format(create_date,'%Y-%m-%d')) = YEARWEEK(now()) and user_id=#{id}")
    List<UserGameTurnoverLogBean> getrecordConsumes(@Param("id") Long id);
}
