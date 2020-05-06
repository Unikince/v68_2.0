package com.dmg.gameconfigserver.controller.bjl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dmg.gameconfigserver.annotation.NoNeedLoginMapping;
import com.dmg.gameconfigserver.service.bjl.BjlFeignService;
import com.dmg.gameconfigserverapi.bjl.dto.BjlTableDTO;

/**
 * 百家乐配置feign获取接口
 */
@RestController
@RequestMapping("/bjl/feign")
public class BjlFeignController {
    @Autowired
    private BjlFeignService bjlFeignService;

    /** 场次配置 */
    @NoNeedLoginMapping
    @GetMapping(value = "/bjlTable")
    public List<BjlTableDTO> getBjlTable() {
        return this.bjlFeignService.getBjlTable();
    }
}