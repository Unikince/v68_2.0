package com.dmg.clubserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.clubserver.dao.bean.ClubInvitationBean;
import org.apache.ibatis.annotations.Mapper;

/**
 * 俱乐部邀请表
 * 
 * @author mice
 * @email .com
 * @date 2019-05-30 18:59:30
 */
@Mapper
public interface ClubInvitationDao extends BaseMapper<ClubInvitationBean> {
	
}
