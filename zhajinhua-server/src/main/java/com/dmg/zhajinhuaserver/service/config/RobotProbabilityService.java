package com.dmg.zhajinhuaserver.service.config;

import com.dmg.common.core.web.Result;
import com.dmg.zhajinhuaserver.model.dto.RobotProbabilityDTO;
import com.dmg.zhajinhuaserver.service.config.fallback.RobotProbabilityFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 9:48 2019/9/30
 */
@FeignClient(value = "game-config-server", fallbackFactory = RobotProbabilityFallbackFactory.class)
public interface RobotProbabilityService {

    /**
     * @Author liubo
     * @Description //TODO 查询机器人基础概率
     * @Date 10:05 2019/9/30
     **/
    @GetMapping("/game-config-api/zjhRobotProbability/getList")
    Result<List<RobotProbabilityDTO>> getList();
}
