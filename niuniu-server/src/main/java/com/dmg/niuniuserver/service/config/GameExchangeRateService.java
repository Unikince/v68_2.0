package com.dmg.niuniuserver.service.config;

import com.dmg.common.core.web.Result;
import com.dmg.niuniuserver.model.dto.GameExchangeRateDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;



/**
 * @Author liubo
 * @Description //TODO
 * @Date 16:39 2019/9/28
 */
@FeignClient(value = "game-config-server")
public interface GameExchangeRateService {

    /**
     * @Author liubo
     * @Description //TODO 查询汇率信息
     * @Date 13:03 2019/12/5
     **/
    @GetMapping("/game-config-api/game/config/exchangeRate/getGameExchangeRate/{code}")
    Result<GameExchangeRateDTO> getInfoByWater(@RequestParam("code") String code);

}
