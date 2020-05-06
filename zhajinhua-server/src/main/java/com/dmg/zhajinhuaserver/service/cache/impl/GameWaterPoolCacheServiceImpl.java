package com.dmg.zhajinhuaserver.service.cache.impl;

import com.dmg.common.core.web.BaseResultEnum;
import com.dmg.common.core.web.Result;
import com.dmg.server.common.model.dto.GameWaterPoolDTO;
import com.dmg.zhajinhuaserver.model.dto.GameWaterPoolByWaterDTO;
import com.dmg.zhajinhuaserver.service.cache.GameWaterPoolCacheService;
import com.dmg.zhajinhuaserver.service.config.GameWaterPoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 16:13 2019/9/29
 */
@Slf4j
@Service
public class GameWaterPoolCacheServiceImpl implements GameWaterPoolCacheService {

    @Autowired
    private GameWaterPoolService gameWaterPoolService;

    @Override
    public GameWaterPoolDTO getGameWaterPool(BigDecimal water, Integer gameId, Integer roomLevel) {
        GameWaterPoolByWaterDTO gameWaterPoolByWaterDTO = new GameWaterPoolByWaterDTO();
        gameWaterPoolByWaterDTO.setGameId(gameId);
        gameWaterPoolByWaterDTO.setRoomLevel(roomLevel);
        gameWaterPoolByWaterDTO.setWater(water);
        log.info("调用game-config服务查询水池信息req：{}", gameWaterPoolByWaterDTO.toString());
        Result<GameWaterPoolDTO> result = gameWaterPoolService.getInfoByWater(gameWaterPoolByWaterDTO);
        log.info("调用game-config服务查询水池信息resp：{}", result.toString());
        if (BaseResultEnum.SUCCESS.getCode().toString().equals(result.getCode())) {
            return result.getData();
        }
        return null;
    }
}
