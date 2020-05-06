package com.dmg.game.task.service.config;

import com.dmg.common.core.web.Result;
import com.dmg.game.task.model.vo.ConditionDetailVO;
import com.dmg.game.task.model.vo.GameFileVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 9:45 2019/10/11
 */
@FeignClient(value = "game-config-server")
public interface GameInfoService {

    /**
     * @Author liubo
     * @Description 获取游戏场次信息//TODO
     * @Date 17:49 2020/3/13
     **/
    @GetMapping("/game-config-api/gameInfo/gameFile")
    Result<List<GameFileVO>> gameFile();

    /**
     * @Author liubo
     * @Description 获取实时在线游戏场次信息//TODO
     * @Date 17:49 2020/3/13
     **/
    @GetMapping("/game-config-api/home/online/getCondition")
    Result<List<ConditionDetailVO>> getCondition();
}
