package com.dmg.niuniuserver.service.cache;

import com.dmg.niuniuserver.constant.RegionConstant;
import com.dmg.server.common.model.dto.GameWaterPoolDTO;
import org.springframework.cache.annotation.Cacheable;

import java.math.BigDecimal;

/**
 * @Author liubo
 * @Description //TODO 牛牛水池配置
 * @Date 16:09 2019/9/29
 */
public interface GameWaterPoolCacheService {

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 16:13 2019/9/29
     **/
    @Cacheable(cacheNames = RegionConstant.GAME_WATER_POOL, key = "#gameId+'_'+#roomLevel+'_'+#water", unless = "#result == null")
    GameWaterPoolDTO getGameWaterPool(BigDecimal water, Integer gameId, Integer roomLevel);
}
