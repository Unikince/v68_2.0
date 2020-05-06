package com.dmg.zhajinhuaserver.service.config;

import com.dmg.common.core.web.Result;
import com.dmg.zhajinhuaserver.model.dto.RobotActionDTO;
import com.dmg.zhajinhuaserver.service.config.fallback.RobotActionFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:21 2019/9/30
 */
@FeignClient(value = "game-config-server", fallbackFactory = RobotActionFallbackFactory.class)
public interface RobotActionConfigService {

    /**
     * @Author liubo
     * @Description //TODO 根据是否看牌查询机器人动作概率配置信息
     * @Date 10:10 2019/9/30
     **/
    @GetMapping("/game-config-api/zjhRobotAction/getRobotActionByIsSee")
    Result<RobotActionDTO> getRobotActionByIsSee(@RequestParam("isSee") Boolean isSee);

    /**
     * @Author liubo
     * @Description //TODO 根据牌值查询机器人动作概率配置信息
     * @Date 10:10 2019/9/30
     **/
    @GetMapping("/game-config-api/zjhRobotAction/getRobotActionByCard")
    Result<RobotActionDTO> getRobotActionByCard(@RequestParam("cardType") Integer cardType,
                                                @RequestParam("card") Integer card);

    /**
     * @Author liubo
     * @Description //TODO 查询所以数据
     * @Date 14:11 2019/10/15
     **/
    @GetMapping("/game-config-api/zjhRobotAction/getList")
    Result<List<RobotActionDTO>> getList();
}
