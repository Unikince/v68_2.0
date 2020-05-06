package com.dmg.gameconfigserver.controller.sys;

import com.dmg.common.core.web.Result;
import com.dmg.common.starter.rocketmq.core.producer.impl.DefautProducer;
import com.dmg.gameconfigserver.annotation.NoAuthMapping;
import com.dmg.gameconfigserver.annotation.NoNeedLoginMapping;
import com.dmg.gameconfigserver.model.dto.sys.ServerMaintainVO;
import com.dmg.gameconfigserver.model.vo.GameInfoVO;
import com.dmg.gameconfigserver.service.config.GameInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author mice
 * @Date 2019/12/25 17:34
 * @Version V1.0
 **/
@RestController
@Slf4j
public class ServerMaintainController {

    @Autowired
    private DefautProducer defautProducer;
    @Autowired
    private GameInfoService gameInfoService;

    @PostMapping("serverMaintain")
    @NoAuthMapping
    public Result serverMaintain(@RequestBody ServerMaintainVO vo){
        if (CollectionUtils.isEmpty(vo.getGameIds())){
            return Result.success();
        }
        GameInfoVO gameInfoVO = new GameInfoVO();
        gameInfoVO.setGameStatus(0);
        vo.getGameIds().forEach(gameId -> {
            try {
                defautProducer.send("MAINTAIN","MAINTAIN", String.valueOf(gameId));
                gameInfoVO.setGameId(gameId);
                gameInfoService.update(gameInfoVO);
            } catch (Exception e) {
                log.error("==> 维护信息发送至MQ失败");
                e.printStackTrace();
            }
        });
        return Result.success();
    }
}