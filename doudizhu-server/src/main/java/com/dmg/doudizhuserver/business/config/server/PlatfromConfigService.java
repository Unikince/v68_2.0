package com.dmg.doudizhuserver.business.config.server;

import java.util.List;

import com.dmg.server.common.model.dto.GameWaterPoolDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.dmg.common.core.web.Result;

@FeignClient(value = "game-config-server")
public interface PlatfromConfigService {
    @GetMapping("/game-config-api/game/config/gameFile/getGameFileByGameId")
    Result<List<GameFileConfigDTO>> getGameFileConfigByGameId(@RequestParam("gameId") Integer gameId);

    @PostMapping("/game-config-api/game/config/gameWaterPool/getInfoByWater")
    Result<GameWaterPoolDTO> getInfoByWater(@RequestBody GameWaterPoolByWaterDTO gameWaterPoolByWaterDTO);
}
