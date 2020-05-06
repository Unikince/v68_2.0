package com.dmg.gameconfigserver.controller.bjl;

import com.dmg.gameconfigserver.common.constant.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.annotation.NoNeedLoginMapping;
import com.dmg.gameconfigserver.service.bjl.BjlConfigService;

/**
 * 百家乐配置接口
 */
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "bjl/config")
public class BjlConfigController {
    @Autowired
    private BjlConfigService bjlConfigService;

    @NoNeedLoginMapping
    @PostMapping("/update")
    @SuppressWarnings("rawtypes")
    public Result getFishRoomInfo() {
        this.bjlConfigService.update();
        return Result.success();
    }

}