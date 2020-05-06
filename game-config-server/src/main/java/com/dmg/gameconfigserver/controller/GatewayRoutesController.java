package com.dmg.gameconfigserver.controller;

import com.dmg.gameconfigserver.annotation.NoNeedLoginMapping;
import com.dmg.gameconfigserver.model.dto.gatewayroutes.GatewayRouteDefinition;
import com.dmg.gameconfigserver.service.gatewayroutes.GatewayRoutesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description
 * @Author mice
 * @Date 2019/12/3 15:04
 * @Version V1.0
 **/
@RestController
@RequestMapping("gatewayRoutes")
public class GatewayRoutesController {

    @Autowired
    private GatewayRoutesService gatewayRoutesService;

    @NoNeedLoginMapping
    @RequestMapping("list")
    public  List<GatewayRouteDefinition> getRoutes(){
        return gatewayRoutesService.getRoutes();
    }

    @NoNeedLoginMapping
    @RequestMapping("lastVersion")
    public  Long getLastVersion(){
        return gatewayRoutesService.getVersion();
    }

    @NoNeedLoginMapping
    @RequestMapping("updateVersion")
    public  void updateVersion(){
        gatewayRoutesService.updateVersion();
    }

    @NoNeedLoginMapping
    @RequestMapping("clostRoutes")
    public  void closeRoutes(@RequestParam("routes") String routes){
        gatewayRoutesService.closeRoutes(Arrays.asList(routes.split(",")));
        gatewayRoutesService.updateVersion();
    }


}