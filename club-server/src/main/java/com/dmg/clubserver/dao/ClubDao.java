package com.dmg.clubserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.clubserver.dao.bean.ClubBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 俱乐部
 * 
 * @author mice
 * @email .com
 * @date 2019-05-25 14:18:53
 */
@Mapper
public interface ClubDao extends BaseMapper<ClubBean> {

    /**
     * @description: 查询最大club_id值
     * @param
     * @return int
     * @author mice
     * @date 2019/5/27
    */
    @Select("select max(club_id) from club")
    Integer selectMaxClubId();

    /**
     * @description: 俱乐部人数-1
     * @param
     * @return int
     * @author mice
     * @date 2019/5/27
     */
    @Update("update club set current_member_num = current_member_num-1 where club_id = #{clubId}")
    int subClubMember(Integer clubId);



}
