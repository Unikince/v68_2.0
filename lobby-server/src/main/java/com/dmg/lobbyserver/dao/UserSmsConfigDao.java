package com.dmg.lobbyserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.lobbyserver.dao.bean.UserSmsConfigBean;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户短信配置表
 * 
 * @author mice
 * @email .com
 * @date 2019-06-21 17:12:08
 */
@Mapper
public interface UserSmsConfigDao extends BaseMapper<UserSmsConfigBean> {
	
}
