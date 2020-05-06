package com.dmg.zhajinhuaserver.service.cache;

import com.dmg.zhajinhuaserver.common.constant.RegionConstant;
import com.dmg.zhajinhuaserver.model.dto.GameFileConfigDTO;
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
