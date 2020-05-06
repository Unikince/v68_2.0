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
import com.dmg.gameconfigserver.model.dto.lhj.LhjPlayerPayLimitConfigDTO;
import com.dmg.gameconfigserver.model.vo.lhj.LhjBonusConfigVO;
import com.dmg.gameconfigserver.model.vo.lhj.LhjFieldConfigVO;
import com.dmg.gameconfigserver.model.vo.lhj.LhjPayLimitConfigVO;
import com.dmg.gameconfigserver.model.vo.lhj.LhjPlayerPayLimitConfigVO;
import com.dmg.gameconfigserver.service.lhj.LhjBonusConfigService;
import com.dmg.gameconfigserver.service.lhj.LhjFieldConfigService;
import com.dmg.gameconfigserver.service.lhj.LhjPayLimitConfigService;
import com.dmg.gameconfigserver.service.lhj.LhjPlayerPayLimitConfigService;

/**
 * @Description
 * @Author mice
 * @Date 2019/9/27 10:14
 * @Version V1.0
 **/
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "game/config/lhjPlayerPayLimitConfig")
public class LhjPlayerPayLimitConfigController {

    @Autowired
    private LhjPlayerPayLimitConfigService lhjPlayerPayLimitConfigService;

    
    @GetMapping("/getAll")
    public Result getLhjPlayerPayLimitConfig(){
    	List<LhjPlayerPayLimitConfigDTO> lhjPlayerPayLimitConfigDTOList = lhjPlayerPayLimitConfigService.queryPayPlayerLimitInfo();
        return Result.success(lhjPlayerPayLimitConfigDTOList);
    }
    
    @PostMapping("/saveOne")
    public Result saveLhjPlayerPayLimitConfig(@RequestBody @Validated LhjPlayerPayLimitConfigVO vo) {
    	LhjPlayerPayLimitConfigDTO lhjPlayerPayLimitConfigDTO = new LhjPlayerPayLimitConfigDTO();
        BeanUtils.copyProperties(vo, lhjPlayerPayLimitConfigDTO);
        int add = lhjPlayerPayLimitConfigService.addPayPlayerLimitInfo(lhjPlayerPayLimitConfigDTO);
        if (add == 0) {
        	return Result.error(BaseResultEnum.PARAM_ERROR.getCode() + "",BaseResultEnum.PARAM_ERROR.getMsg());
		}
        return Result.success();
    }

    @PostMapping("/updateOne")
    public Result updateLhjPlayerPayLimitConfig(@RequestBody @Validated LhjPlayerPayLimitConfigVO vo){
    	LhjPlayerPayLimitConfigDTO lhjPlayerPayLimitConfigDTO = new LhjPlayerPayLimitConfigDTO();
        BeanUtils.copyProperties(vo, lhjPlayerPayLimitConfigDTO);
        int update = lhjPlayerPayLimitConfigService.updatePlayerPayLimitInfo(lhjPlayerPayLimitConfigDTO);
        if (update == 0) {
        	return Result.error(BaseResultEnum.PARAM_ERROR.getCode() + "",BaseResultEnum.PARAM_ERROR.getMsg());
		}
        return Result.success();
    }

    @GetMapping("/delete/{fileBaseConfigId}")
    public Result deleteLhjPlayerPayLimitConfig(@PathVariable("fileBaseConfigId") Integer fileBaseConfigId){
    	int del = lhjPlayerPayLimitConfigService.delPayPlayerLimitInfo(fileBaseConfigId);
    	if (del == 0) {
         	return Result.error(BaseResultEnum.PARAM_ERROR.getCode() + "",BaseResultEnum.PARAM_ERROR.getMsg());
 		}
        return Result.success();
    }
    
}