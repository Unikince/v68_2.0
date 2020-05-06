package com.dmg.doudizhuserver.business.config.server;

import java.math.BigDecimal;
import java.util.List;

import com.dmg.server.common.model.dto.GameWaterPoolDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.dmg.common.core.web.BaseResultEnum;
import com.dmg.common.core.web.Result;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PlatfromConfigCacheService {
    @Autowired
    private PlatfromConfigService platfromConfigService;

    @Cacheable(cacheNames = "room_file_base", key = "#gameId", unless = "#result == null")
    public List<GameFileConfigDTO> getGameFileConfigByGameId(Integer gameId) {
        log.info("调用game-config服务查询游戏房间信息req：{}", gameId);
        Result<List<GameFileConfigDTO>> result = this.platfromConfigService.getGameFileConfigByGameId(gameId);
        log.info("调用game-config服务查询游戏房间信息resp：{}", result.toString());
        if (BaseResultEnum.SUCCESS.getCode().toString().equals(result.getCode())) {
            return result.getData();
        }
        return null;
    }

    @Cacheable(cacheNames = "game_water_pool", key = "#gameId+'_'+#roomLevel+'_'+#water", unless = "#result == null")
    public GameWaterPoolDTO getGameWaterPool(BigDecimal water, Integer gameId, Integer roomLevel) {
        GameWaterPoolByWaterDTO gameWaterPoolByWaterDTO = new GameWaterPoolByWaterDTO();
        gameWaterPoolByWaterDTO.setGameId(gameId);
        gameWaterPoolByWaterDTO.setRoomLevel(roomLevel);
        gameWaterPoolByWaterDTO.setWater(water);
        log.info("调用game-config服务查询水池信息req：{}", gameWaterPoolByWaterDTO.toString());
        Result<GameWaterPoolDTO> result = this.platfromConfigService.getInfoByWater(gameWaterPoolByWaterDTO);
        log.info("调用game-config服务查询水池信息resp：{}", result.toString());
        if (BaseResultEnum.SUCCESS.getCode().toString().equals(result.getCode())) {
            log.info("成功");
            return result.getData();
        }
        return null;
    }
}
