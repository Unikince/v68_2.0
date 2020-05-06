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
import org.springframework.web.bind.annotation.RestController;

import com.dmg.common.core.web.BaseResultEnum;
import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.model.dto.lhj.LhjBonusConfigDTO;
import com.dmg.gameconfigserver.model.dto.lhj.LhjFieldConfigDTO;
import com.dmg.gameconfigserver.model.vo.lhj.LhjBonusConfigVO;
import com.dmg.gameconfigserver.model.vo.lhj.LhjFieldConfigVO;
import com.dmg.gameconfigserver.service.lhj.LhjBonusConfigService;
import com.dmg.gameconfigserver.service.lhj.LhjFieldConfigService;

/**
 * @Description
 * @Author mice
 * @Date 2019/9/27 10:14
 * @Version V1.0
 **/
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "game/config/lhjBonusConfig")
public class LhjBonusConfigController {

    @Autowired
    private LhjBonusConfigService lhjBonusConfigService;

    
    @GetMapping("/getOne/{fileBaseConfigId}")
    public Result getOneLhjBonusConfig(@PathVariable("fileBaseConfigId") Integer fileBaseConfigId){
    	List<LhjBonusConfigDTO> gamePoolConfigDTOList = lhjBonusConfigService.queryGamePoolConfig(fileBaseConfigId);
        return Result.success(gamePoolConfigDTOList);
    }
    
    //@PostMapping("/saveOne")
    public Result saveLhjBonusConfig(@RequestBody @Validated LhjBonusConfigVO vo) {
    	LhjBonusConfigDTO lhjBonusConfigDTO = new LhjBonusConfigDTO();
        BeanUtils.copyProperties(vo, lhjBonusConfigDTO);
        int add = lhjBonusConfigService.addPoolConfig(lhjBonusConfigDTO);
        if (add == 0) {
        	return Result.error(BaseResultEnum.PARAM_ERROR.getCode() + "",BaseResultEnum.PARAM_ERROR.getMsg());
		}
        return Result.success();
    }

    @PostMapping("/updateOne")
    public Result updateLhjBonusConfig(@RequestBody @Validated LhjBonusConfigVO vo){
    	LhjBonusConfigDTO lhjBonusConfigDTO = new LhjBonusConfigDTO();
        BeanUtils.copyProperties(vo, lhjBonusConfigDTO);
        int update = lhjBonusConfigService.updatePoolConfig(lhjBonusConfigDTO);
        if (update == 0) {
        	return Result.error(BaseResultEnum.PARAM_ERROR.getCode() + "",BaseResultEnum.PARAM_ERROR.getMsg());
		}
        return Result.success();
    }

}