package com.dmg.gameconfigserver.controller.fish;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dmg.gameconfigserver.annotation.NoNeedLoginMapping;
import com.dmg.gameconfigserver.service.fish.FishFeignService;
import com.dmg.gameconfigserverapi.fish.dto.FishCtrlReturnRateDTO;
import com.dmg.gameconfigserverapi.fish.dto.FishCtrlStockDTO;
import com.dmg.gameconfigserverapi.fish.dto.FishDTO;
import com.dmg.gameconfigserverapi.fish.dto.FishRoomDTO;
import com.dmg.gameconfigserverapi.fish.dto.FishRouteDTO;
import com.dmg.gameconfigserverapi.fish.dto.FishScenceDTO;
import com.dmg.gameconfigserverapi.fish.dto.FishStrategyDTO;

import cn.hutool.json.JSONUtil;

/**
 * 捕鱼配置feign获取接口
 */
@RestController
@RequestMapping("/fish/feign")
public class FishFeignController {
    @Autowired
    private FishFeignService fishFeignService;

    /** 鱼 */
    @NoNeedLoginMapping
    @GetMapping(value = "/fish")
    public List<FishDTO> getFish() {
        return this.fishFeignService.getFish();

    }

    /** 房间配置 */
    @NoNeedLoginMapping
    @GetMapping(value = "/fishRoom")
    public List<FishRoomDTO> getFishRoom() {
        return this.fishFeignService.getFishRoom();
    }

    /** 返奖率控制 */
    @NoNeedLoginMapping
    @GetMapping(value = "/fishCtrlReturnRate")
    public List<FishCtrlReturnRateDTO> getFishCtrlReturnRate() {
        return this.fishFeignService.getFishCtrlReturnRate();
    }

    /** 库存控制 */
    @NoNeedLoginMapping
    @GetMapping(value = "/fishCtrlStock")
    public List<FishCtrlStockDTO> getFishCtrlStock() {
        return this.fishFeignService.getFishCtrlStock();
    }

    /** 库存控制更新 */
    @NoNeedLoginMapping
    @PostMapping(value = "/updateFishCtrlStock")
    public void updateFishCtrlStock(@RequestParam("params") String params) {
        @SuppressWarnings("unchecked")
        Map<String, String> paramsMap = JSONUtil.toBean(params, new HashMap<String, String>().getClass());
        this.fishFeignService.updateFishCtrlStock(paramsMap);
    }

    /** 鱼路线图 */
    @NoNeedLoginMapping
    @GetMapping(value = "/fishRoute")
    public List<FishRouteDTO> getFishRoute() {
        return this.fishFeignService.getFishRoute();
    }

    /** 场景 */
    @NoNeedLoginMapping
    @GetMapping(value = "/fishScence")
    public List<FishScenceDTO> getFishScence() {
        return this.fishFeignService.getFishScence();
    }

    /** 刷鱼策略 */
    @NoNeedLoginMapping
    @GetMapping(value = "/fishStrategy")
    public List<FishStrategyDTO> getFishStrategy() {
        return this.fishFeignService.getFishStrategy();
    }
}