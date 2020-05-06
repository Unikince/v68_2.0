package com.dmg.gameconfigserver.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dmg.gameconfigserver.annotation.NoNeedLoginMapping;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.model.dto.lhj.LhjGameConfigDTO;
import com.dmg.gameconfigserver.model.dto.lhj.LhjGameListDTO;
import com.dmg.gameconfigserver.service.lhj.LhjGameConfigApiService;

/**
 * @Description
 * @Author mice
 * @Date 2019/10/10 10:27
 * @Version V1.0
 **/
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "game/config/lhjGameConfig")
public class LhjGameConfigApiController {
    @Autowired
    private LhjGameConfigApiService lhjGameConfigApiService;

    @NoNeedLoginMapping
    @GetMapping("/getLhjGameConfigData/{gameId}")
    public LhjGameConfigDTO getLhjGameConfigData(@PathVariable("gameId") Integer gameId){
        LhjGameConfigDTO lhjGameConfigDTO = lhjGameConfigApiService.getGameConfig(gameId);
        return lhjGameConfigDTO;
    }
    
    @NoNeedLoginMapping
    @GetMapping("/getLhjGameList")
    public List<LhjGameListDTO> getLhjGameList(){
    	List<LhjGameListDTO> lhjGameList = lhjGameConfigApiService.getGameList();
        return lhjGameList;
    }
}