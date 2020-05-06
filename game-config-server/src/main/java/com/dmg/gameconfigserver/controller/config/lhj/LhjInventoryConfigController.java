package com.dmg.gameconfigserver.controller.config.lhj;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dmg.common.core.web.BaseResultEnum;
import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.model.dto.lhj.LhjInventoryConfigDTO;
import com.dmg.gameconfigserver.model.vo.lhj.LhjInventoryConfigVO;
import com.dmg.gameconfigserver.service.lhj.LhjInventoryConfigService;

/**
 * @Description
 * @Author mice
 * @Date 2019/9/27 10:14
 * @Version V1.0
 **/
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "game/config/lhjInventoryConfig")
public class LhjInventoryConfigController {

    @Autowired
    private LhjInventoryConfigService lhjInventoryConfigService;
    
    @GetMapping("/getOne/{fileBaseConfigId}")
    public Result getOneLhjInventoryConfig(@PathVariable("fileBaseConfigId") Integer fileBaseConfigId){
    	List<LhjInventoryConfigDTO> inventoryInfoList = lhjInventoryConfigService.queryInventoryInfo(fileBaseConfigId);
        return Result.success(inventoryInfoList);
    }
    
    @PostMapping("/saveOne")
    public Result saveLhjInventoryConfig(@RequestBody @Validated LhjInventoryConfigVO vo) {
    	LhjInventoryConfigDTO lhjInventoryConfigDTO = new LhjInventoryConfigDTO();
        BeanUtils.copyProperties(vo, lhjInventoryConfigDTO);
        int add = lhjInventoryConfigService.addInventoryInfo(lhjInventoryConfigDTO);
        if (add == 0) {
        	return Result.error(BaseResultEnum.PARAM_ERROR.getCode() + "",BaseResultEnum.PARAM_ERROR.getMsg());
		}
        return Result.success();
    }

    //@PostMapping("/updateOne")
    public Result updateLhjInventoryConfig(@RequestBody @Validated LhjInventoryConfigVO vo){
        return Result.success();
    }
    
    @PostMapping("/delete")
    public Result deleteLhjInventoryConfig(@RequestBody @Validated LhjInventoryConfigVO vo){
    	LhjInventoryConfigDTO lhjInventoryConfigDTO = new LhjInventoryConfigDTO();
        BeanUtils.copyProperties(vo, lhjInventoryConfigDTO);
    	int del = lhjInventoryConfigService.delInventoryInfo(lhjInventoryConfigDTO);
    	if (del == 0) {
         	return Result.error(BaseResultEnum.PARAM_ERROR.getCode() + "",BaseResultEnum.PARAM_ERROR.getMsg());
 		}
        return Result.success();
    }
}