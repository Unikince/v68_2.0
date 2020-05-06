package com.dmg.lobbyserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.lobbyserver.dao.bean.TurnTableConfigBean;
import org.apache.ibatis.annotations.Mapper;

/**
 * 大转盘配置
 * 
 * @author mice
 * @email .com
 * @date 2019-06-19 15:40:44
 */
@Mapper
public interface TurnTableConfigDao extends BaseMapper<TurnTableConfigBean> {
	
}
