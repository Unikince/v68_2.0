package com.dmg.gameconfigserver.controller.api;

import com.dmg.common.core.util.RedisUtil;
import com.dmg.gameconfigserver.annotation.NoNeedLoginMapping;
import com.dmg.gameconfigserver.common.constant.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.dmg.common.core.config.RedisRegionConfig.USER_WHITE_DEVICE_CODE;

/**
 * @description:
 * @author mice
 * @date 2020/3/20
*/
@RequestMapping(Constant.CONTEXT_PATH + "userWhiteApi")
@RestController
public class UserWhiteApiController {
    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("getOne/{deviceCode}")
    @NoNeedLoginMapping
    public Boolean getWhiteStatusByDeviceCode(@PathVariable("deviceCode")String deviceCode){
        return redisUtil.hasKey(USER_WHITE_DEVICE_CODE+":"+deviceCode);
    }
}