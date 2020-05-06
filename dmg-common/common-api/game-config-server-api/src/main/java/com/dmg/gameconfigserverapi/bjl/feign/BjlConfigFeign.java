package com.dmg.gameconfigserverapi.bjl.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.dmg.gameconfigserverapi.bjl.dto.BjlTableDTO;

/**
 * 百家乐配置获取接口
 */
@FeignClient(value = "GAME-CONFIG-SERVER", path = "/bjl/feign")
public interface BjlConfigFeign {

    /** 场次配置 */
    @GetMapping(value = "/bjlTable")
    List<BjlTableDTO> getBjlTable();
}