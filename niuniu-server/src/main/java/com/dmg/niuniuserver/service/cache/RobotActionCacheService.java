package com.dmg.niuniuserver.service.cache;

import com.dmg.niuniuserver.constant.RegionConstant;
import com.dmg.niuniuserver.model.dto.NiuniuRobotActionDTO;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 16:17 2019/9/29
 */
public interface RobotActionCacheService {

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 16:13 2019/9/29
     **/
    @Cacheable(cacheNames = RegionConstant.ROBOT_ACTION, key = "#card", unless = "#result == null")
    List<NiuniuRobotActionDTO> getRobInfoByCard(Integer card);

    /**
     * @Author liubo
     * @Description //TODO 
     * @Date 16:21 2019/9/29
     **/
    @Cacheable(cacheNames = RegionConstant.ROBOT_ACTION, key = "#card+'_'+#robType", unless = "#result == null")
    List<NiuniuRobotActionDTO> getPressureInfoByRob(Integer card,Integer robType);
}
