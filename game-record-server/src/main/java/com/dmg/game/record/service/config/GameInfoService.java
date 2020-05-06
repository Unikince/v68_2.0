package com.dmg.game.record.service.config;

import com.dmg.common.core.web.Result;
import com.dmg.game.record.model.dto.GameInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 9:45 2019/10/11
 */
@FeignClient(value = "game-config-server")
public interface GameInfoService {

    /**
     * @Author liubo
     * @Description //TODO 根据游戏id查询房间配置
     * @Date 9:48 2019/10/11
     **/
    @GetMapping("/game-config-api/gameInfo/detail/{gameId}")
    Result<GameInfoDTO> gameInfo(@RequestParam("gameId") Integer gameId);
}
