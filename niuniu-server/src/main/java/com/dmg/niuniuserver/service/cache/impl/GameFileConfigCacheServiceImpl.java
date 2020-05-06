package com.dmg.niuniuserver.service.cache.impl;

import com.dmg.common.core.web.BaseResultEnum;
import com.dmg.common.core.web.Result;
import com.dmg.niuniuserver.model.dto.GameFileConfigDTO;
import com.dmg.niuniuserver.service.cache.GameFileConfigCacheService;
import com.dmg.niuniuserver.service.config.GameFileConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 13:40 2019/10/11
 */
@Slf4j
@Service
public class GameFileConfigCacheServiceImpl implements GameFileConfigCacheService {

    @Autowired
    private GameFileConfigService fileBaseConfigService;

    @Override
    public List<GameFileConfigDTO> getGameFileConfigByGameId(Integer gameId) {
        log.info("调用game-config服务查询游戏房间信息req：{}", gameId);
        Result<List<GameFileConfigDTO>> result = fileBaseConfigService.getGameFileConfigByGameId(gameId);
        log.info("调用game-config服务查询游戏房间信息resp：{}", result.toString());
        if (BaseResultEnum.SUCCESS.getCode().toString().equals(result.getCode())) {
            return result.getData();
        }
        return null;
    }
}
