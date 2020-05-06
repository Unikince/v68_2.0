package com.dmg.gameconfigserver.controller.financing;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.annotation.NoNeedLoginMapping;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.model.bean.finance.PlatformRechargeLogBean;
import com.dmg.gameconfigserver.model.dto.finance.PlatformRechargeDTO;
import com.dmg.gameconfigserver.service.financing.PlatformRechargeLogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author mice
 * @Date 2019/12/27 10:11
 * @Version V1.0
 **/
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "finance/platformRecharge")
public class PlatformRechargeController {

    @Autowired
    private PlatformRechargeLogService platformRechargeLogService;

    @PostMapping("/getList")
    public Result queryPlatfromRechargePage(@RequestBody PlatformRechargeDTO platformRechargeDTO){
        IPage<PlatformRechargeLogBean> platformRechargeLogBeanIPage = platformRechargeLogService.queryPlatfromRechargePage(platformRechargeDTO);
        return Result.success(platformRechargeLogBeanIPage);
    }
}