package com.dmg.gameconfigserver.dao.app;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.gameconfigserver.model.bean.AppInfoBean;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author mice
 * @email .com
 * @date 2019-11-14 14:07:20
 */
@Mapper
public interface AppInfoDao extends BaseMapper<AppInfoBean> {
	
}
