package com.dmg.gameconfigserver.controller.config;

import java.util.List;

import org.apache.ibatis.annotations.Update;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.controller.BaseController;
import com.dmg.gameconfigserver.model.bean.config.UserControlConfigBean;
import com.dmg.gameconfigserver.model.dto.config.UserControlConfigDTO;
import com.dmg.gameconfigserver.model.dto.config.UserControlRatioConfigDTO;
import com.dmg.gameconfigserver.model.vo.config.UserControlConfigVO;
import com.dmg.gameconfigserver.model.vo.group.SaveValid;
import com.dmg.gameconfigserver.model.vo.group.UpdateValid;
import com.dmg.gameconfigserver.service.config.UserControlConfigService;
import com.dmg.gameconfigserver.service.config.UserControlRatioConfigService;

/**
 * @Description
 * @Author mice
 * @Date 2019/9/27 10:14
 * @Version V1.0
 **/
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "game/config/userControlConfig")
public class UserControlConfigController extends BaseController{

    @Autowired
    private UserControlConfigService userControlConfigService;
    @Autowired
    private UserControlRatioConfigService userControlRatioConfigService;

    @GetMapping("/getAll")
    public Result getUserControlConfigList(){
    	List<UserControlConfigDTO> userControlConfigList = userControlConfigService.getUserControlConfigList();
        return Result.success(userControlConfigList);
    }
    
    @PostMapping("/saveOne")
    public Result saveUserControlConfig(@RequestBody @Validated(SaveValid.class) UserControlConfigVO vo){
    	UserControlRatioConfigDTO userControlRatioConfigDTO = userControlRatioConfigService.selectOne();
    	UserControlConfigBean userControlConfigBean = new UserControlConfigBean();
        BeanUtils.copyProperties(vo, userControlConfigBean);
        userControlConfigBean.setBaseRewardRate(userControlRatioConfigDTO.getBaseRewardRate());
        userControlConfigBean.setAutoControlValue(userControlRatioConfigDTO.getAutoControlValue());
        userControlConfigBean.setWaterRatio(userControlRatioConfigDTO.getWaterRatio());
        userControlConfigService.saveOne(userControlConfigBean);
        return Result.success();
    }
    
    @PostMapping("/updateOne")
    public Result updateUserControlConfig(@RequestBody @Validated(UpdateValid.class) UserControlConfigVO vo){
    	UserControlConfigBean userControlConfigBean = new UserControlConfigBean();
        BeanUtils.copyProperties(vo, userControlConfigBean);
        userControlConfigService.updateWithActionLog(userControlConfigBean,getIpAddr(),getUserId());
        return Result.success();
    }

    @GetMapping("/deleteOne/{id}")
    public Result deleteUserControlConfig(@PathVariable("id") Integer id){
    	userControlConfigService.deleteOne(id);
        return Result.success();
    }
    
}