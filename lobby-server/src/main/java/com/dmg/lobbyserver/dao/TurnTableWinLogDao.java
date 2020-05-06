package com.dmg.lobbyserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.lobbyserver.dao.bean.TurnTableWinLogBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 大转盘中奖记录
 * 
 * @author mice
 * email .com
 * date 2019-06-19 17:25:24
 */
@Mapper
public interface TurnTableWinLogDao extends BaseMapper<TurnTableWinLogBean> {

    @Select("select count(*) from turn_table_win_log where win_user_id = #{userId} and date(win_date) = curdate()")
    Integer selectCountToday(@Param("userId") Long userId);
}
