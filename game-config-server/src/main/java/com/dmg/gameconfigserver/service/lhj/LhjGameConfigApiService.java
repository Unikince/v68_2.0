package com.dmg.gameconfigserver.service.lhj;

import java.util.List;

import com.dmg.gameconfigserver.model.dto.lhj.LhjGameConfigDTO;
import com.dmg.gameconfigserver.model.dto.lhj.LhjGameListDTO;

/**
 * 
 *
 * @author mice
 * @email .com
 * @date 2019-09-26 17:09:15
 */
public interface LhjGameConfigApiService {

    /**
     * @description: 获取老虎机游戏配置参数
     * @param gameId
     * @return com.dmg.gameconfigserver.model.dto.config.BairenGameConfigDTO
     * @author mice
     * @date 2019/9/27
    */
    LhjGameConfigDTO getGameConfig(int gameId);
    
    List<LhjGameListDTO> getGameList();
}

