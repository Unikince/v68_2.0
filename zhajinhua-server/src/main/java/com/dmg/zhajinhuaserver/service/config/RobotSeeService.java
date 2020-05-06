package com.dmg.zhajinhuaserver.service.config;

import com.dmg.common.core.web.Result;
import com.dmg.zhajinhuaserver.model.dto.RobotSeeDTO;
import com.dmg.zhajinhuaserver.service.config.fallback.RobotSeeFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:04 2019/9/30
 */
@FeignClient(value = "game-config-server", fallbackFactory = RobotSeeFallbackFactory.class)
public interface RobotSeeService {

    /**
     * @Author liubo
     * @Description //TODO 根据轮数查询是否看牌概率配置信息
     * @Date 10:10 2019/9/30
     **/
    @GetMapping("/game-config-api/zjhRobotSee/getRobotSeeByRound")
    Result<RobotSeeDTO> getRobotSeeByRound(@RequestParam("round") Integer round);

    /**
     * @Author liubo
     * @Description //TODO 查询是否看牌概率配置信息
     * @Date 10:05 2019/9/30
     **/
    @GetMapping("/game-config-api/zjhRobotSee/getList")
    Result<List<RobotSeeDTO>> getList();
}
