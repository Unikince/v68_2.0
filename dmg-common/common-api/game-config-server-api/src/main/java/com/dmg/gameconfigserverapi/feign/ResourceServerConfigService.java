package com.dmg.gameconfigserverapi.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @Description
 * @Author mice
 * @Date 2019/9/29 16:25
 * @Version V1.0
 **/
@FeignClient(value = "GAME-CONFIG-SERVER",path = "/game-config-api")
public interface ResourceServerConfigService {

   /**
    * @description: 获取资源服务器列表
    * @param deployEnvironment 1:正式环境 2:测试环境
    * @return java.util.List<com.dmg.gameconfigserverapi.dto.BairenGameConfigDTO>
    * @author mice
    * @date 2019/10/25
   */
    @GetMapping(value = "/resourceServerApi/resourceServerList/{deployEnvironment}")
    List<String> getResourceServerList(@PathVariable("deployEnvironment") Integer deployEnvironment);

}