package com.dmg.clubserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.clubserver.dao.bean.ClubJoinRequestBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 俱乐部申请表
 * 
 * @author mice
 * @email .com
 * @date 2019-05-25 14:18:53
 */
@Mapper
public interface ClubJoinRequestDao extends BaseMapper<ClubJoinRequestBean> {

    /**
     * @description: 通过roleId 查询已经申请的俱乐部id
     * @param roleId
     * @return java.util.List<java.lang.Integer>
     * @author mice
     * @date 2019/5/28
     */
    @Select("select club_id from club_join_request where requestor_id = #{roleId}")
    List<Integer> selectHasRequestClubIdByRoleId(Integer roleId);
	
}
