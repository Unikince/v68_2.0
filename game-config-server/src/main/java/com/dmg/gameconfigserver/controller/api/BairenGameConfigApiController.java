package com.dmg.gameconfigserver.controller.api;

import com.dmg.gameconfigserver.annotation.NoNeedLoginMapping;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.model.dto.config.BairenGameConfigDTO;
import com.dmg.gameconfigserver.service.config.BairenGameConfigApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description
 * @Author mice
 * @Date 2019/10/10 10:27
 * @Version V1.0
 **/
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "bairenGameConfig")
public class BairenGameConfigApiController {
    @Autowired
    private BairenGameConfigApiService bairenGameConfigApiService;

    @NoNeedLoginMapping
    @GetMapping("/getBairenGameConfigData/{gameId}")
    public List<BairenGameConfigDTO> getBairenGameConfigData(@PathVariable("gameId") Integer gameId){
        List<BairenGameConfigDTO> bairenGameConfigDTOs = bairenGameConfigApiService.getGameConfig(gameId);
        return bairenGameConfigDTOs;
    }
}