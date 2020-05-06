package com.dmg.gameconfigserver.controller.config;

import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.annotation.NoNeedLoginMapping;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.service.config.GameExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @Author liubo
 * @Description //TODO
 * @Date 11:12 2019/12/5
 */
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "game/config/exchangeRate")
public class GameExchangeRateController {

    @Autowired
    private GameExchangeRateService gameExchangeRateService;

    @NoNeedLoginMapping
    @GetMapping("/getGameExchangeRate/{code}")
    public Result getGameFileByGameId(@PathVariable("code") String code) {
        if (code == null) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), "code cannot be empty");
        }
        return Result.success(gameExchangeRateService.getExchangeRateByCode(code));
    }
}
