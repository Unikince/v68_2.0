package com.dmg.gameconfigserver.service.config;


import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.gameconfigserver.model.bean.config.UserControlListBean;
import com.dmg.gameconfigserver.model.dto.config.UserControlListDTO;
import com.dmg.gameconfigserver.model.vo.config.UserControlListVO;

/**
 * 
 *
 * @author mice
 * @email .com
 * @date 2019-09-30 11:32:55
 */
public interface UserControlListService {

    /**
     * @description: 获取用户控制玩家列表
     * @author mice
     * @date 2019/10/10
    */
	
	IPage<UserControlListDTO> getUserControlList(UserControlListVO vo);
	
	UserControlListDTO getUserControlInfo(Long userId);

    void updateWithActionLog(UserControlListBean userControlListBean, String loginIp, Long sysUserId);
    
    void update(UserControlListBean userControlListBean);
    
    void updateState(Long userId, int state);
    
    List<UserControlListDTO> getUserPointControlInfoList(List<Long> userIds);

	
}

