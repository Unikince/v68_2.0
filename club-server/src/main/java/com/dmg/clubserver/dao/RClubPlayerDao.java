package com.dmg.clubserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.clubserver.dao.bean.RClubPlayerBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 俱乐部成员关系表
 * 
 * @author mice
 * @email .com
 * @date 2019-05-25 14:18:52
 */
@Mapper
public interface RClubPlayerDao extends BaseMapper<RClubPlayerBean> {

    /**
     * @description: 通过roleId 查询已经加入的俱乐部id
     * @param roleId
     * @return java.util.List<java.lang.Integer>
     * @author mice
     * @date 2019/5/28
    */
    @Select("select club_id from r_club_player where role_id = #{roleId}")
    List<Integer> selectHasJionClubIdByRoleId(Integer roleId);

    /**
     * @description: 通过clubId 查询已经加入的玩家id
     * @param clubId
     * @return java.util.List<java.lang.Integer>
     * @author mice
     * @date 2019/6/04
     */
    @Select("select role_id from r_club_player where club_id = #{club_id}")
    List<Integer> selectRoleIdsByClubId(Integer clubId);

	
}
