package com.dmg.zhajinhuaserver.service.config;

import com.dmg.common.core.web.Result;
import com.dmg.zhajinhuaserver.model.dto.GameFileConfigDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 9:45 2019/10/11
 */
@FeignClient(value = "game-config-server")
public interface GameFileConfigService {

    /**
     * @Author liubo
     * @Description //TODO 根据游戏id查询房间配置
     * @Date 9:48 2019/10/11
     **/
    @GetMapping("/game-config-api/game/config/gameFile/getGameFileByGameId")
    Result<List<GameFileConfigDTO>> getGameFileConfigByGameId(@RequestParam("gameId") Integer gameId);
}
