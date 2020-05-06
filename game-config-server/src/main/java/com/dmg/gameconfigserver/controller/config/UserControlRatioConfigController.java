package com.dmg.gameconfigserver.controller.config;

import java.math.BigDecimal;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.controller.BaseController;
import com.dmg.gameconfigserver.model.bean.config.UserControlRatioConfigBean;
import com.dmg.gameconfigserver.model.dto.config.UserControlRatioConfigDTO;
import com.dmg.gameconfigserver.model.vo.config.UserControlRatioConfigVO;
import com.dmg.gameconfigserver.service.config.UserControlRatioConfigService;

/**
 * @Description
 * @Author mice
 * @Date 2019/9/27 10:14
 * @Version V1.0
 **/
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "game/config/userControlRatioConfig")
public class UserControlRatioConfigController extends BaseController{

    @Autowired
    private UserControlRatioConfigService userControlRatioConfigService;

    @GetMapping("/selectOne")
    public Result getUserControlRatioConfig(){
    	UserControlRatioConfigDTO userControlRatioConfigDTO = userControlRatioConfigService.selectOne();
        return Result.success(userControlRatioConfigDTO);
    }
    
    @PostMapping("/updateOne")
    public Result updateUserControlRatioConfig(@RequestBody @Validated UserControlRatioConfigVO vo){
    	UserControlRatioConfigBean userControlRatioConfigBean = new UserControlRatioConfigBean();
        BeanUtils.copyProperties(vo, userControlRatioConfigBean);
        userControlRatioConfigBean.setWaterRatio(new BigDecimal(1/(1-userControlRatioConfigBean.getBaseRewardRate().doubleValue())) );
        userControlRatioConfigService.updateWithActionLog(userControlRatioConfigBean,getIpAddr(),getUserId());
        return Result.success();
    }

}