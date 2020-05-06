package com.dmg.gameconfigserver.dao.config;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.gameconfigserver.model.bean.config.UserControlRecordBean;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 14:07 2019/10/11
 */
@DS("v68")
public interface UserControlRecordDao extends BaseMapper<UserControlRecordBean> {
	
}
