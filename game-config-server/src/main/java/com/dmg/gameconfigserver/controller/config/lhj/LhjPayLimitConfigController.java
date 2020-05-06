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
import com.dmg.gameconfigserver.model.dto.lhj.LhjBonusConfigDTO;
import com.dmg.gameconfigserver.model.dto.lhj.LhjFieldConfigDTO;
import com.dmg.gameconfigserver.model.dto.lhj.LhjPayLimitConfigDTO;
import com.dmg.gameconfigserver.model.vo.lhj.LhjBonusConfigVO;
import com.dmg.gameconfigserver.model.vo.lhj.LhjFieldConfigVO;
import com.dmg.gameconfigserver.model.vo.lhj.LhjPayLimitConfigVO;
import com.dmg.gameconfigserver.service.lhj.LhjBonusConfigService;
import com.dmg.gameconfigserver.service.lhj.LhjFieldConfigService;
import com.dmg.gameconfigserver.service.lhj.LhjPayLimitConfigService;

/**
 * @Description
 * @Author mice
 * @Date 2019/9/27 10:14
 * @Version V1.0
 **/
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "game/config/lhjPayLimitConfig")
public class LhjPayLimitConfigController {

    @Autowired
    private LhjPayLimitConfigService lhjPayLimitConfigService;

    
    @GetMapping("/getAll")
    public Result getLhjPayLimitConfig(){
    	List<LhjPayLimitConfigDTO> lhjPayLimitConfigDTOList = lhjPayLimitConfigService.queryPayLimitInfo();
        return Result.success(lhjPayLimitConfigDTOList);
    }
    
    @PostMapping("/saveOne")
    public Result saveLhjPayLimitConfig(@RequestBody @Validated LhjPayLimitConfigVO vo) {
    	LhjPayLimitConfigDTO lhjPayLimitConfigDTO = new LhjPayLimitConfigDTO();
        BeanUtils.copyProperties(vo, lhjPayLimitConfigDTO);
        int add = lhjPayLimitConfigService.addPayLimitInfo(lhjPayLimitConfigDTO);
        if (add == 0) {
        	return Result.error(BaseResultEnum.PARAM_ERROR.getCode() + "",BaseResultEnum.PARAM_ERROR.getMsg());
		}
        return Result.success();
    }

    @PostMapping("/updateOne")
    public Result updateLhjPayLimitConfig(@RequestBody @Validated LhjPayLimitConfigVO vo){
    	LhjPayLimitConfigDTO lhjPayLimitConfigDTO = new LhjPayLimitConfigDTO();
        BeanUtils.copyProperties(vo, lhjPayLimitConfigDTO);
        int update = lhjPayLimitConfigService.updatePayLimitInfo(lhjPayLimitConfigDTO);
        if (update == 0) {
        	return Result.error(BaseResultEnum.PARAM_ERROR.getCode() + "",BaseResultEnum.PARAM_ERROR.getMsg());
		}
        return Result.success();
    }

    @GetMapping("/delete/{fileBaseConfigId}")
    public Result deleteLhjPayLimitConfig(@PathVariable("fileBaseConfigId") Integer fileBaseConfigId){
    	int del = lhjPayLimitConfigService.delPayLimitInfo(fileBaseConfigId);
    	if (del == 0) {
         	return Result.error(BaseResultEnum.PARAM_ERROR.getCode() + "",BaseResultEnum.PARAM_ERROR.getMsg());
 		}
        return Result.success();
    }
    
}