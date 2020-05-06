package com.dmg.gameconfigserver.service.config;


import com.dmg.gameconfigserver.model.bean.config.UserControlListBean;
import com.dmg.gameconfigserver.model.bean.config.UserControlRatioConfigBean;
import com.dmg.gameconfigserver.model.dto.config.UserControlRatioConfigDTO;

/**
 * 
 *
 * @author mice
 * @email .com
 * @date 2019-09-30 11:32:55
 */
public interface UserControlRatioConfigService {

    /**
     * @description: 获取用户控制配置
     * @author mice
     * @date 2019/10/10
    */
	

	UserControlRatioConfigDTO selectOne();

	void updateWithActionLog(UserControlRatioConfigBean userControlRatioConfigBean, String loginIp, Long sysUserId);
	
    void update(UserControlRatioConfigBean userControlRatioConfigBean);

}

