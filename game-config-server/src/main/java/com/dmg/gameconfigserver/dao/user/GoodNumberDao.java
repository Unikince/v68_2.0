package com.dmg.gameconfigserver.dao.user;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.gameconfigserver.model.bean.user.GoodNumberBean;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author mice
 * @email .com
 * @date 2020-03-25 11:28:56
 */
@Mapper
@DS("v68")
public interface GoodNumberDao extends BaseMapper<GoodNumberBean> {
	
}
