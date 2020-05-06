package com.dmg.gameconfigserver.controller.home;

import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.annotation.NoAuthMapping;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.controller.BaseController;
import com.dmg.gameconfigserver.service.home.GameTodaySumService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 16:48 2020/3/16
 */
@Slf4j
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "home/today")
public class GameTodaySumController extends BaseController {

    @Autowired
    private GameTodaySumService gameTodaySumService;

    @NoAuthMapping
    @GetMapping("/getSum")
    public Result getSum() {
        return Result.success(gameTodaySumService.getGameTodaySun());
    }

}
