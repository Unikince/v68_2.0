package com.dmg.gameconfigserver.service.api;

import com.dmg.gameconfigserverapi.dto.GameInfoDTO;

import java.util.List;

/**
 * @Description
 * @Author mice
 * @Date 2019/10/17 11:25
 * @Version V1.0
 **/
public interface GameInfoApiService {

    /**
     * @description: 获取游戏列表信息
     * @param
     * @return java.util.List<com.dmg.gameconfigserverapi.dto.GameInfoDTO>
     * @author mice
     * @date 2019/10/8
     */
    List<GameInfoDTO> getGameInfoList();
}