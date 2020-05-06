package com.dmg.niuniuserver.service.config;

import com.dmg.common.core.web.Result;
import com.dmg.niuniuserver.model.dto.NiuniuRobotActionDTO;
import com.dmg.niuniuserver.service.config.fallback.RobotActionFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO 牛牛机器人动作概率配置查询
 * @Date 14:04 2019/9/29
 */
@FeignClient(value = "game-config-server", fallbackFactory = RobotActionFallbackFactory.class)
public interface RobotActionService {

    /**
     * @Author liubo
     * @Description //TODO 根据牌型查询抢配置信息
     * @Date 9:49 2019/9/29
     **/
    @GetMapping("/game-config-api/niuniuRobotAction/getRobInfoByCard")
    Result<List<NiuniuRobotActionDTO>> getRobInfoByCard(@RequestParam("card") Integer card);

    /**
     * @Author liubo
     * @Description //TODO 根据抢类型查询押配置信息
     * @Date 9:49 2019/9/29
     **/
    @GetMapping("/game-config-api/niuniuRobotAction/getPressureInfoByRob")
    Result<List<NiuniuRobotActionDTO>> getPressureInfoByRob(@RequestParam("card") Integer card,
                                                            @RequestParam("robType") Integer robType);

    /**
     * @Author liubo
     * @Description //TODO 查询押配置信息
     * @Date 9:49 2019/9/29
     **/
    @GetMapping("/game-config-api/niuniuRobotAction/getList")
    Result<List<NiuniuRobotActionDTO>> getList();
}
