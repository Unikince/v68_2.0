package com.dmg.clubserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.clubserver.dao.bean.ClubGameRecordBean;
import com.dmg.clubserver.model.dto.LeaderboardDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 俱乐部战绩
 * 
 * @author mice
 * @email .com
 * @date 2019-05-25 14:18:53
 */
@Mapper
public interface ClubGameRecordDao extends BaseMapper<ClubGameRecordBean> {

    @Select("select * from club_game_record where  record_type=1 and end_date > #{date} and game_id in" +
            "        (select DISTINCT(game_id) from club_game_record where  FIND_IN_SET(club_id,#{clubIds}))")
    List<ClubGameRecordBean> selectGameRecordByClubId(Map<String,Object> map);

    /**
     * @description: 查询游戏详情
     * @param gameId
     * @return java.util.List<com.dmg.game.base.mybatis.bean.ClubGameRecordBean>
     * @author mice
     * @date 2019/4/17
     */
    @Select("select * from club_game_record where record_type = 2 and game_id = #{gameId} order by round asc")
    List<ClubGameRecordBean> selectDetailByGameId(Long gameId);

    @Select("select role_id as roleId,sum(score) as winScore,count(*) as totalRound from club_game_record where record_type = 2 and club_id = #{clubId}   and end_date like #{date} GROUP BY role_id ORDER BY  totalRound desc")
    List<LeaderboardDTO> selectLeaderboard(@Param("clubId")Integer clubId, @Param("date")String date);

    @Select("select DISTINCT(game_id) from club_game_record where record_type = 1 and club_id = #{clubId}   and end_date between #{startDate} and #{endDate}")
    List<Integer> selectGameId(@Param("clubId")Integer clubId, @Param("startDate")Date startDate, @Param("endDate")Date endDate);

    @Select("select max(id) from club_game_record where record_type = 1 and game_id = #{gameId}")
    Integer selectId(String gameId);

    @Select("select sum(room_card_consume_num) from club_game_record where FIND_IN_SET(id,#{ids})")
    Integer selectCostRoomCard(String ids);
	
}
