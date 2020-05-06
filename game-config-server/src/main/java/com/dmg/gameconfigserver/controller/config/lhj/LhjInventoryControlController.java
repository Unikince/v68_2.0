package com.dmg.gameconfigserver.controller.config.lhj;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dmg.common.core.web.BaseResultEnum;
import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.model.dto.lhj.LhjInventoryControlDTO;
import com.dmg.gameconfigserver.model.vo.lhj.LhjInventoryControlVO;
import com.dmg.gameconfigserver.service.lhj.LhjInventoryControlService;

/**
 * @Description
 * @Author mice
 * @Date 2019/9/27 10:14
 * @Version V1.0
 **/
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "game/config/lhjInventoryControl")
public class LhjInventoryControlController {

    @Autowired
    private LhjInventoryControlService lhjInventoryControlService;
    
    @GetMapping("/getOne/{gameId}")
    public Result getOneLhjInventoryControl(@PathVariable("gameId") Integer gameId){
    	LhjInventoryControlDTO lhjInventoryControlDTO = lhjInventoryControlService.queryInventoryControl(gameId);
        return Result.success(lhjInventoryControlDTO);
    }

    @PostMapping("/updateOne")
    public Result updateLhjInventoryControl(@RequestBody @Validated LhjInventoryControlVO vo){
    	LhjInventoryControlDTO dto = new LhjInventoryControlDTO();
        BeanUtils.copyProperties(vo, dto);
    	int update = lhjInventoryControlService.updateInventoryControl(dto);
    	if (update != 0) {
    		  return Result.success();
		} else {
			  return Result.error(BaseResultEnum.PARAM_ERROR);
		}
    }
}