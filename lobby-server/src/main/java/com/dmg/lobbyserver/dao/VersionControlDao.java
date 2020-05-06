package com.dmg.lobbyserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.lobbyserver.dao.bean.VersionControlBean;
import org.apache.ibatis.annotations.Mapper;

/**
 * 版本控制
 * 
 * @author mice
 * @email .com
 * @date 2019-06-18 11:23:54
 */
@Mapper
public interface VersionControlDao extends BaseMapper<VersionControlBean> {
	
}
