package com.dmg.zhajinhuaserver.service.config;

import com.dmg.common.core.web.Result;
import com.dmg.server.common.model.dto.GameWaterPoolDTO;
import com.dmg.zhajinhuaserver.model.dto.GameWaterPoolByWaterDTO;
import com.dmg.zhajinhuaserver.service.config.fallback.GameWaterPoolFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * @Author liubo
 * @Description //TODO
 * @Date 16:39 2019/9/28
 */
@FeignClient(value = "game-config-server", fallbackFactory = GameWaterPoolFallbackFactory.class)
public interface GameWaterPoolService {

    /**
     * @Author liubo
     * @Description //TODO 根据房间等级及水池金额查询水池配置信息
     * @Date 9:49 2019/9/29
     **/
    @PostMapping("/game-config-api/game/config/gameWaterPool/getInfoByWater")
    Result<GameWaterPoolDTO> getInfoByWater(@RequestBody GameWaterPoolByWaterDTO gameWaterPoolByWaterDTO);

    /**
     * @Author liubo
     * @Description //TODO 根据房间等级及水池金额查询水池配置信息
     * @Date 9:49 2019/9/29
     **/
    @GetMapping("/game-config-api/game/config/gameWaterPool/getInfoByGame/{gameId}")
    Result<List<GameWaterPoolDTO>> getInfoByWater(@RequestParam("gameId") Integer gameId);

}
