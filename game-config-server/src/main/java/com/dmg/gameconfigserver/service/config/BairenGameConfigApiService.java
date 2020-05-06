package com.dmg.gameconfigserver.service.config;

import com.dmg.gameconfigserver.model.dto.config.BairenGameConfigDTO;

import java.util.List;

/**
 * 
 *
 * @author mice
 * @email .com
 * @date 2019-09-26 17:09:15
 */
public interface BairenGameConfigApiService {

    /**
     * @description: 获取百人游戏配置参数
     * @param gameId
     * @return com.dmg.gameconfigserver.model.dto.config.BairenGameConfigDTO
     * @author mice
     * @date 2019/9/27
    */
    List<BairenGameConfigDTO> getGameConfig(int gameId);
}

