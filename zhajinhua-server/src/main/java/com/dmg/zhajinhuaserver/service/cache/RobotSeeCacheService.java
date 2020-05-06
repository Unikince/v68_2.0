package com.dmg.zhajinhuaserver.service.cache;

import com.dmg.zhajinhuaserver.common.constant.RegionConstant;
import com.dmg.zhajinhuaserver.model.dto.RobotSeeDTO;
import org.springframework.cache.annotation.Cacheable;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:13 2019/9/30
 */
public interface RobotSeeCacheService {

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 10:20 2019/9/30
     **/
    @Cacheable(cacheNames = RegionConstant.ROBOT_SEE, key = "#round", unless = "#result == null")
    RobotSeeDTO getRobotSeeByRound(Integer round);
}
