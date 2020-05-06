package com.dmg.gameconfigserver.controller.api;

import com.dmg.gameconfigserver.annotation.NoNeedLoginMapping;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.service.api.ResourceServerApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description
 * @Author mice
 * @Date 2019/9/27 14:43
 * @Version V1.0
 **/
@RequestMapping(Constant.CONTEXT_PATH + "resourceServerApi")
@RestController
public class ResourceServerApiController {
    @Autowired
    private ResourceServerApiService resourceServerApiService;

    @NoNeedLoginMapping
    @GetMapping("resourceServerList/{deployEnvironment}")
    public List<String> getResourceServerList(@PathVariable("deployEnvironment") Integer deployEnvironment){
        List<String> resourceServerList = resourceServerApiService.getResourceServerList(deployEnvironment);
        return resourceServerList;
    }

}