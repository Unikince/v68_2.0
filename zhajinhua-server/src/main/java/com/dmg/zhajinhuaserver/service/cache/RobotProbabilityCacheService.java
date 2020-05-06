package com.dmg.zhajinhuaserver.service.cache;

import com.dmg.zhajinhuaserver.common.constant.RegionConstant;
import com.dmg.zhajinhuaserver.model.dto.RobotProbabilityDTO;
import org.springframework.cache.annotation.Cacheable;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 9:56 2019/9/30
 */
public interface RobotProbabilityCacheService {

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 16:13 2019/9/29
     **/
    @Cacheable(cacheNames = RegionConstant.ROBOT_PROBABILITY, unless = "#result == null")
    RobotProbabilityDTO getRobotProbability();
}
