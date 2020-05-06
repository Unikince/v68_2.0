package com.dmg.clubserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.clubserver.dao.bean.ClubKickOutLogBean;
import org.apache.ibatis.annotations.Mapper;

/**
 * 俱乐部踢人记录
 * 
 * @author mice
 * @email .com
 * @date 2019-05-28 17:23:48
 */
@Mapper
public interface ClubKickOutLogDao extends BaseMapper<ClubKickOutLogBean> {
	
}
