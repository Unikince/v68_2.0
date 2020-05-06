package com.dmg.gameconfigserverapi.fish.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dmg.gameconfigserverapi.fish.dto.FishCtrlReturnRateDTO;
import com.dmg.gameconfigserverapi.fish.dto.FishCtrlStockDTO;
import com.dmg.gameconfigserverapi.fish.dto.FishDTO;
import com.dmg.gameconfigserverapi.fish.dto.FishRoomDTO;
import com.dmg.gameconfigserverapi.fish.dto.FishRouteDTO;
import com.dmg.gameconfigserverapi.fish.dto.FishScenceDTO;
import com.dmg.gameconfigserverapi.fish.dto.FishStrategyDTO;

/**
 * 捕鱼配置获取接口
 */
@FeignClient(value = "GAME-CONFIG-SERVER", path = "/fish/feign")
public interface FishConfigFeign {
    /** 鱼 */
    @GetMapping(value = "/fish")
    List<FishDTO> getFish();

    /** 房间配置 */
    @GetMapping(value = "/fishRoom")
    List<FishRoomDTO> getFishRoom();

    /** 返奖率控制 */
    @GetMapping(value = "/fishCtrlReturnRate")
    List<FishCtrlReturnRateDTO> getFishCtrlReturnRate();

    /** 库存控制 */
    @GetMapping(value = "/fishCtrlStock")
    List<FishCtrlStockDTO> getFishCtrlStock();

    /** 库存控制更新 */
    @PostMapping(value = "/updateFishCtrlStock")
    void updateFishCtrlStock(@RequestParam("params") String params);

    /** 鱼路线图 */
    @GetMapping(value = "/fishRoute")
    List<FishRouteDTO> getFishRoute();

    /** 场景 */
    @GetMapping(value = "/fishScence")
    List<FishScenceDTO> getFishScence();

    /** 刷鱼策略 */
    @GetMapping(value = "/fishStrategy")
    List<FishStrategyDTO> getFishStrategy();
}
