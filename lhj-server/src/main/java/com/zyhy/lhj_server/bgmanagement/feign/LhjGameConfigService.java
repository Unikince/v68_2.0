package com.zyhy.lhj_server.bgmanagement.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.zyhy.lhj_server.bgmanagement.feign.dto.LhjGameConfigDTO;
import com.zyhy.lhj_server.bgmanagement.feign.dto.UserControlListDTO;

/**
 * @Description
 * @Author mice
 * @Date 2019/9/29 16:25
 * @Version V1.0
 **/
@FeignClient(value = "GAME-CONFIG-SERVER",path = "/game-config-api")
public interface LhjGameConfigService {

    /**
     * @description: 获取老虎机游戏配置信息
    */
	
    @GetMapping("/game/config/lhjGameConfig/getLhjGameConfigData/{gameId}")
    LhjGameConfigDTO getLhjGameConfigData(@PathVariable("gameId") Integer gameId);
    
    /**
     * @description: 获取点控对象信息
    */
    @GetMapping("/game/config/userControl/getUserControlInfo/{userId}")
    UserControlListDTO getUserControlInfo(@PathVariable("userId") Long userId);
    
    /**
     * @description: 获取点控状态模型
     */
    @GetMapping("/game/config/userControl/getPointControlModel/{userId}")
    int getPointControlModel(@PathVariable("userId") Long userId);
    
    /**
     * @description: 获取自控状态模型
     */
    @GetMapping("/game/config/userControl/getAutoControlModel/{userId}")
    int getAutoControlModel(@PathVariable("userId") Long userId);
}