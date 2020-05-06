package com.dmg.gameconfigserverapi.feign;

import com.dmg.gameconfigserverapi.dto.SysMarqueeConfigDTO;
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
@FeignClient(value = "GAME-CONFIG-SERVER", path = "/game-config-api")
public interface MarqueeConfigService {

    /**
     * @param marqueeType 1:普通公告 2:停服公告
     * @return java.util.List<com.dmg.gameconfigserverapi.dto.SysMarqueeConfigDTO>
     * @description: 获取跑马灯公告
     * @author mice
     * @date 2019/10/25
     */
    @GetMapping(value = "/sysMarqueeConfigApi/list/{marqueeType}")
    List<SysMarqueeConfigDTO> getResourceServerList(@PathVariable("marqueeType") Integer marqueeType);

}