package com.dmg.niuniuserver.service.cache;

import com.dmg.niuniuserver.constant.RegionConstant;
import com.dmg.niuniuserver.model.dto.GameFileConfigDTO;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 13:38 2019/10/11
 */
public interface GameFileConfigCacheService {

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 13:39 2019/10/11
     **/
    @Cacheable(cacheNames = RegionConstant.ROOM_FILE_BASE, key = "#gameId", unless = "#result == null")
    List<GameFileConfigDTO> getGameFileConfigByGameId(Integer gameId);
}
