package com.dmg.clubserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.clubserver.dao.bean.ClubLogBean;
import org.apache.ibatis.annotations.Mapper;

/**
 * 俱乐部记录
 * 
 * @author mice
 * @email .com
 * @date 2019-05-25 14:18:53
 */
@Mapper
public interface ClubLogDao extends BaseMapper<ClubLogBean> {
	
}
