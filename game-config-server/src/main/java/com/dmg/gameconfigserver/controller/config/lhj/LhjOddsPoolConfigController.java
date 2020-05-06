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
import com.dmg.gameconfigserver.model.dto.lhj.LhjOddsPoolConfigDTO;
import com.dmg.gameconfigserver.model.vo.lhj.LhjBonusConfigVO;
import com.dmg.gameconfigserver.model.vo.lhj.LhjFieldConfigVO;
import com.dmg.gameconfigserver.model.vo.lhj.LhjOddsPoolConfigVO;
import com.dmg.gameconfigserver.service.lhj.LhjBonusConfigService;
import com.dmg.gameconfigserver.service.lhj.LhjFieldConfigService;
import com.dmg.gameconfigserver.service.lhj.LhjOddsPoolConfigService;

/**
 * @Description
 * @Author mice
 * @Date 2019/9/27 10:14
 * @Version V1.0
 **/
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "game/config/lhjOddsPoolConfig")
public class LhjOddsPoolConfigController {

    @Autowired
    private LhjOddsPoolConfigService lhjOddsPoolConfigService;

    
    @GetMapping("/getOne")
    public Result getLhjOddsPoolConfig(){
    	LhjOddsPoolConfigDTO lhjOddsPoolConfigDTO = lhjOddsPoolConfigService.queryOddsPoolInfo();
        return Result.success(lhjOddsPoolConfigDTO);
    }
    
    //@PostMapping("/saveOne")
    public Result saveLhjOddsPoolConfig(@RequestBody @Validated LhjOddsPoolConfigVO vo) {
    	LhjOddsPoolConfigDTO LhjOddsPoolConfigDTO = new LhjOddsPoolConfigDTO();
        BeanUtils.copyProperties(vo, LhjOddsPoolConfigDTO);
        int add = lhjOddsPoolConfigService.addOddsPoolInfo(LhjOddsPoolConfigDTO);
        if (add == 0) {
        	return Result.error(BaseResultEnum.PARAM_ERROR.getCode() + "",BaseResultEnum.PARAM_ERROR.getMsg());
		}
        return Result.success();
    }

    @PostMapping("/updateOne")
    public Result updateLhjOddsPoolConfig(@RequestBody @Validated LhjOddsPoolConfigVO vo){
    	LhjOddsPoolConfigDTO LhjOddsPoolConfigDTO = new LhjOddsPoolConfigDTO();
        BeanUtils.copyProperties(vo, LhjOddsPoolConfigDTO);
    	int update = lhjOddsPoolConfigService.updateOddsPoolInfo(LhjOddsPoolConfigDTO);
        if (update == 0) {
        	return Result.error(BaseResultEnum.PARAM_ERROR.getCode() + "",BaseResultEnum.PARAM_ERROR.getMsg());
		}
        return Result.success();
    }

}