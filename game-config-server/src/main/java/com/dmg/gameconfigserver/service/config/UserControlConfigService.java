package com.dmg.gameconfigserver.service.config;


import java.util.List;

import com.dmg.gameconfigserver.model.bean.config.UserControlConfigBean;
import com.dmg.gameconfigserver.model.dto.config.UserControlConfigDTO;
import com.dmg.gameconfigserver.model.dto.config.UserControlListDTO;

/**
 * 
 *
 * @author mice
 * @email .com
 * @date 2019-09-30 11:32:55
 */
public interface UserControlConfigService {

    /**
     * @description: 获取用户控制配置
     * @author mice
     * @date 2019/10/10
    */
	
    List<UserControlConfigDTO> getUserControlConfigList();

    void saveOne(UserControlConfigBean userControlConfigBean);
    
    void updateWithActionLog(UserControlConfigBean userControlConfigBean, String loginIp, Long sysUserId);
    
    void updateOne(UserControlConfigBean userControlConfigBean);

    void deleteOne(int id);
    
    int getModel(UserControlListDTO userControlListDTO);
    
}

