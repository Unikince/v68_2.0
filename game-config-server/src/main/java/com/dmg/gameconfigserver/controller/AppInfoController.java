package com.dmg.gameconfigserver.controller;

import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.annotation.NoAuthMapping;
import com.dmg.gameconfigserver.model.bean.AppInfoBean;
import com.dmg.gameconfigserver.model.vo.AppInfoVO;
import com.dmg.gameconfigserver.service.config.AppInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


/**
 * 
 *
 * @author mice
 * @email .com
 * @date 2019-11-14 14:07:20
 */
@RestController
@RequestMapping("appinfo")
public class AppInfoController {
    @Autowired
    private AppInfoService appInfoService;

    /**
     * 修改
     */
    @NoAuthMapping
    @RequestMapping("/update")
    public Result update(@RequestBody AppInfoVO vo){
        AppInfoBean appInfoBean = AppInfoBean.builder()
                .id(1)
                .appVersion(vo.getAppVersion())
                .updatorId(vo.getUpdatorId())
                .updateDate(new Date()).build();
        appInfoService.updateAppInfo(appInfoBean);
        return Result.success();
    }


}
