package com.dmg.gameconfigserver.service.api;

import java.util.List;
import java.util.Map;

import com.dmg.gameconfigserver.model.bean.config.UserControlListBean;
import com.dmg.gameconfigserver.model.dto.config.UserControlListDTO;
import com.dmg.server.common.model.dto.UserControlInfoDTO;

/**
 * 
 *
 * @author mice
 * @email .com
 * @date 2019-09-26 17:09:15
 */
public interface UserControlApiService {

    /**
     * @description: 获取玩家控制参数
    */
	UserControlListDTO getUserControlInfo(Long userId);
    /**
     * @description: 更新控制分数
    */
    void updateCurrentScore(UserControlListBean userControlListBean);
    
    /**
     * 获取自控模型
     */
    int getAutoControlModel(Long userId);
    
    /**
     * 获取点控或自控模型
     * @param userIds
     * @return
     */
    Map<Long, UserControlInfoDTO> getModlel(List<Long> userIds);
}

