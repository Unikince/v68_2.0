package com.dmg.gameconfigserverapi.feign;

import com.dmg.gameconfigserverapi.dto.BairenGameConfigDTO;
import com.dmg.gameconfigserverapi.dto.GameInfoDTO;
import com.dmg.server.common.model.dto.UserControlInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author mice
 * @Date 2019/9/29 16:25
 * @Version V1.0
 **/
@FeignClient(value = "GAME-CONFIG-SERVER",path = "/game-config-api")
public interface GameConfigService {

    /**
     * @description: 获取百人场游戏配置信息
     * @param gameId
     * @return java.util.List<com.dmg.gameconfigserverapi.dto.BairenGameConfigDTO>
     * @author mice
     * @date 2019/10/17
    */
    @GetMapping(value = "/bairenGameConfig/getBairenGameConfigData/{gameId}")
    List<BairenGameConfigDTO> getBairenGameConfigData(@PathVariable("gameId") String gameId);

    /**
     * @description: 获取平台游戏列表信息
     * @param
     * @return java.util.List<com.dmg.gameconfigserverapi.dto.GameInfoDTO>
     * @author mice
     * @date 2019/10/17
    */
    @GetMapping(value = "/gameInfoApi/gameInfoList")
    List<GameInfoDTO> getGameInfoList();

    /**
     * @description: 获取玩家点控模型
     * @param userIds
     * @return java.util.Map<java.lang.Long,com.dmg.server.common.model.dto.UserControlInfoDTO>
     * @author mice
     * @date 2020/2/26
    */
    @GetMapping(value = "/game/config/userControl/getModel")
    Map<Long, UserControlInfoDTO> getModelByUserIds(@RequestBody List<Long> userIds);


}