package com.dmg.gameconfigserver.dao.config;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.gameconfigserver.model.bean.config.FileBaseConfigBean;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author mice
 * @email .com
 * @date 2019-09-30 11:32:55
 */
@Mapper
public interface FileBaseConfigDao extends BaseMapper<FileBaseConfigBean> {
	
}
