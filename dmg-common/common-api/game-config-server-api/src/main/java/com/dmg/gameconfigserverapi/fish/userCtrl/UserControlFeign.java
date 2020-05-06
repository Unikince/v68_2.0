package com.dmg.gameconfigserverapi.fish.userCtrl;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "GAME-CONFIG-SERVER", path = "/game-config-api")
public interface UserControlFeign {
    /**
     * 获取点控状态模型
     */
    @GetMapping("/game/config/userControl/getPointControlModel/{userId}")
    int getPointControlModel(@PathVariable("userId") Long userId);

    /**
     * 获取自控状态模型
     */
    @GetMapping("/game/config/userControl/getAutoControlModel/{userId}")
    int getAutoControlModel(@PathVariable("userId") Long userId);

    /**
     * 获取点控对象信息
     */
    @GetMapping("/game/config/userControl/getUserControlInfo/{userId}")
    UserControlListDTO getUserControlInfo(@PathVariable("userId") Long userId);

}