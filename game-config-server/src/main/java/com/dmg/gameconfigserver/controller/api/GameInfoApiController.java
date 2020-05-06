package com.dmg.gameconfigserver.controller.api;

import com.dmg.gameconfigserver.annotation.NoNeedLoginMapping;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.service.api.GameInfoApiService;
import com.dmg.gameconfigserverapi.dto.GameInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description
 * @Author mice
 * @Date 2019/9/27 14:43
 * @Version V1.0
 **/
@RequestMapping(Constant.CONTEXT_PATH + "gameInfoApi")
@RestController
public class GameInfoApiController {
    @Autowired
    private GameInfoApiService gameInfoApiService;

    @GetMapping("gameInfoList")
    @NoNeedLoginMapping
    public List<GameInfoDTO> getGameInfoList(){
        List<GameInfoDTO> gameInfoDTOS = gameInfoApiService.getGameInfoList();
        return gameInfoDTOS;
    }



}