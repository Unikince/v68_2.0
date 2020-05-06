package com.dmg.gameconfigserver.controller.config.lhj;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import com.dmg.gameconfigserver.dao.lhj.LhjGameConfigDao;
import com.dmg.gameconfigserver.model.dto.lhj.LhjFieldConfigDTO;
import com.dmg.gameconfigserver.model.dto.lhj.LhjInventoryControlDTO;
import com.dmg.gameconfigserver.model.vo.lhj.LhjFieldConfigVO;
import com.dmg.gameconfigserver.service.lhj.LhjFieldConfigService;

/**
 * @Description
 * @Author mice
 * @Date 2019/9/27 10:14
 * @Version V1.0
 **/
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "game/config/lhjFieldConfig")
public class LhjFieldConfigController {

    @Autowired
    private LhjFieldConfigService lhjFieldConfigService;
	@Autowired
	@Qualifier("lhjGameConfigDaoImp")
    private LhjGameConfigDao lhjGameConfigDao;
    @GetMapping("/getAll")
    public Result getALLLhjFieldConfig(){
        List<LhjFieldConfigDTO> lhjFieldConfigDTOList = lhjFieldConfigService.queryAllGameInfo();
        for (LhjFieldConfigDTO dto : lhjFieldConfigDTOList) {
        	LhjInventoryControlDTO inventoryControl = lhjGameConfigDao.queryInventoryControl(dto.getGameId());
        	if (inventoryControl != null) {
        		dto.setSetInventory(inventoryControl.getSetInventory());
        		dto.setCurrentWinLoseValue(inventoryControl.getCurrentWinLoseValue());
			}
		}
        return Result.success(lhjFieldConfigDTOList);
    }
    
    @GetMapping("/getOne/{fileBaseConfigId}")
    public Result getOneLhjFieldConfig(@PathVariable("fileBaseConfigId") Integer fileBaseConfigId){
        LhjFieldConfigDTO lhjFieldConfigDTO = lhjFieldConfigService.querySingleGameInfo(fileBaseConfigId);
        return Result.success(lhjFieldConfigDTO);
    }
    
    @PostMapping("/saveOne")
    public Result saveLhjFieldConfig(@RequestBody @Validated LhjFieldConfigVO vo) {
    	LhjFieldConfigDTO lhjFieldConfigDTO = new LhjFieldConfigDTO();
        BeanUtils.copyProperties(vo, lhjFieldConfigDTO);
        int add = lhjFieldConfigService.addSoltInfo(lhjFieldConfigDTO);
        if (add == 0) {
        	return Result.error(BaseResultEnum.PARAM_ERROR.getCode() + "",BaseResultEnum.PARAM_ERROR.getMsg());
		}
        return Result.success();
    }

    @PostMapping("/updateOne")
    public Result updateLhjFieldConfig(@RequestBody @Validated LhjFieldConfigVO vo){
    	LhjFieldConfigDTO lhjFieldConfigDTO = new LhjFieldConfigDTO();
        BeanUtils.copyProperties(vo, lhjFieldConfigDTO);
        int update = lhjFieldConfigService.updateSoltInfo(lhjFieldConfigDTO);
        if (update == 0) {
        	return Result.error(BaseResultEnum.PARAM_ERROR.getCode() + "",BaseResultEnum.PARAM_ERROR.getMsg());
		}
        return Result.success();
    }

    @GetMapping("/delete/{fileBaseConfigId}")
    public Result deleteLhjFieldConfig(@PathVariable("fileBaseConfigId") Integer fileBaseConfigId){
    	int del = lhjFieldConfigService.delSoltInfo(fileBaseConfigId);
    	if (del == 0) {
         	return Result.error(BaseResultEnum.PARAM_ERROR.getCode() + "",BaseResultEnum.PARAM_ERROR.getMsg());
 		}
        return Result.success();
    }
}