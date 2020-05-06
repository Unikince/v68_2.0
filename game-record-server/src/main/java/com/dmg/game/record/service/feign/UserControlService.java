package com.dmg.game.record.service.feign;

import java.math.BigDecimal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description
 * @Author mice
 * @Date 2020/1/20 14:46
 * @Version V1.0
 **/
@FeignClient(value = "GAME-CONFIG-SERVER",path = "/game-config-api")
public interface UserControlService {

    /** 更新点控状态的当前分数 */
    @PostMapping("/game/config/userControl/updateCurrentScore")
    void updateCurrentScore(@RequestParam("userId") Long userId, @RequestParam("score") BigDecimal score);
}