package com.dmg.gameconfigserver.controller.financing;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.annotation.NoNeedLoginMapping;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.model.bean.finance.PlatformRechargeLogBean;
import com.dmg.gameconfigserver.model.bean.finance.WithdrawOrderBean;
import com.dmg.gameconfigserver.model.dto.finance.PlatformRechargeDTO;
import com.dmg.gameconfigserver.model.dto.finance.QueryWithdrawOrderDTO;
import com.dmg.gameconfigserver.service.financing.PlatformRechargeLogService;
import com.dmg.gameconfigserver.service.financing.WithdrawOrderService;

import com.dmg.server.common.enums.WithdrawOrderStatusEnum;
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
@RequestMapping(Constant.CONTEXT_PATH + "finance/withdrawOrder")
public class WithdrawOrderController {

    @Autowired
    private WithdrawOrderService withdrawOrderService;

    @PostMapping("/getList")
    public Result queryWithdrawOrderPage(@RequestBody QueryWithdrawOrderDTO queryWithdrawOrderDTO){
        queryWithdrawOrderDTO.setOrderStatus(WithdrawOrderStatusEnum.WAITE_REVIEW.getCode());
        IPage<WithdrawOrderBean> withdrawOrderBeanIPage = withdrawOrderService.queryWithdrawOrderPage(queryWithdrawOrderDTO);
        return Result.success(withdrawOrderBeanIPage);
    }
}