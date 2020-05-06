package com.dmg.zhajinhuaserver.service.cache;

import com.dmg.zhajinhuaserver.common.constant.RegionConstant;
import com.dmg.zhajinhuaserver.model.dto.RobotActionDTO;
import org.springframework.cache.annotation.Cacheable;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:44 2019/9/30
 */
public interface RobotActionCacheService {

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 10:47 2019/9/30
     **/
    @Cacheable(cacheNames = RegionConstant.ROBOT_ACTION, key = "#isSee", unless = "#result == null")
    RobotActionDTO getRobotActionByIsSee(Boolean isSee);

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 10:47 2019/9/30
     **/
    @Cacheable(cacheNames = RegionConstant.ROBOT_ACTION, key = "#cardType+'_'+#card", unless = "#result == null")
    RobotActionDTO getRobotActionByCard(Integer cardType, Integer card);

}
