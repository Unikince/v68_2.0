package com.dmg.lobbyserver.service.config;

import com.dmg.common.core.web.Result;
import com.dmg.lobbyserver.model.vo.GameInfoVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 11:07 2019/11/29
 */
@FeignClient(value = "game-config-server", path = "/game-config-api/gameInfo/")
public interface GameInfoService {

    /**
     * @Author liubo
     * @Description //TODO 查询游戏信息
     * @Date 11:15 2019/11/29
     **/
    @GetMapping("gameOpen")
    Result<List<GameInfoVO>> getGameOpen();
}