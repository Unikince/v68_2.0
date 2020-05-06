package com.dmg.gameconfigserver.controller.financing;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.common.constant.ActionLogTemplateConstant;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.controller.BaseController;
import com.dmg.gameconfigserver.model.bean.finance.PlatformRechargeLogBean;
import com.dmg.gameconfigserver.model.bean.finance.WithdrawOrderBean;
import com.dmg.gameconfigserver.model.dto.finance.QueryAbnormalOrderDTO;
import com.dmg.gameconfigserver.model.dto.sys.SysActionLogDTO;
import com.dmg.gameconfigserver.model.vo.finance.OperateOrderVO;
import com.dmg.gameconfigserver.service.financing.PlatformRechargeLogService;
import com.dmg.gameconfigserver.service.financing.WithdrawOrderService;

import com.dmg.gameconfigserver.service.sys.SysActionLogService;
import com.dmg.server.common.enums.PlatformRechargeStatusEnum;
import com.dmg.server.common.enums.WithdrawOrderStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @Description
 * @Author mice
 * @Date 2019/12/31 13:49
 * @Version V1.0
 **/
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "finance/abnormalOrder")
public class AbnormalOrderController extends BaseController {

    @Autowired
    private WithdrawOrderService withdrawOrderService;
    @Autowired
    private PlatformRechargeLogService platformRechargeLogService;
    @Autowired
    private SysActionLogService sysActionLogService;

    
    @PostMapping("/recharge/getList")
    public Result queryRechargeOrderPage(@RequestBody QueryAbnormalOrderDTO queryAbnormalOrderDTO){
        IPage<PlatformRechargeLogBean> withdrawOrderBeanIPage = platformRechargeLogService.queryAbnormalOrder(queryAbnormalOrderDTO);
        return Result.success(withdrawOrderBeanIPage);
    }

    
    @PostMapping("/withdraw/getList")
    public Result queryWithdrawOrderPage(@RequestBody QueryAbnormalOrderDTO queryAbnormalOrderDTO){
        IPage<WithdrawOrderBean> abnormalOrderIPage = withdrawOrderService.queryAbnormalOrder(queryAbnormalOrderDTO);
        return Result.success(abnormalOrderIPage);
    }

    
    @PostMapping("/recharge/replenish")
    public Result replenish(@RequestBody OperateOrderVO vo){
        String orderId = platformRechargeLogService.replenishOrder(vo.getId(),vo.getOperatorId());
        //推送操作日志
        sysActionLogService.pushActionLog(SysActionLogDTO.builder()
                .actionDesc(String.format(ActionLogTemplateConstant.DEAL_ABNORMAL_ORDER, orderId, PlatformRechargeStatusEnum.REPLENISH_SUCCESS.getMsg()))
                .createUser(vo.getOperatorId())
                .loginIp(getIpAddr()).build());
        return Result.success();
    }

    
    @PostMapping("/withdraw/setFinish")
    public Result setFinish(@RequestBody OperateOrderVO vo){
        String orderId = withdrawOrderService.setFinish(vo.getId(),vo.getOperatorId());
        //推送操作日志
        sysActionLogService.pushActionLog(SysActionLogDTO.builder()
                .actionDesc(String.format(ActionLogTemplateConstant.DEAL_ABNORMAL_ORDER, orderId, WithdrawOrderStatusEnum.FINISH.getMsg()))
                .createUser(vo.getOperatorId())
                .loginIp(getIpAddr()).build());
        return Result.success();
    }

    
    @PostMapping("/withdraw/appropriateAgain")
    public Result appropriateAgain(@RequestBody OperateOrderVO vo){
        withdrawOrderService.appropriateAgain(vo.getId(),vo.getOperatorId());
        return Result.success();
    }

    
    @PostMapping("/withdraw/refuse")
    public Result refuse(@RequestBody OperateOrderVO vo){
        String orderId = withdrawOrderService.refuse(vo.getId(),vo.getOperatorId(),vo.getRemark());
        //推送操作日志
        sysActionLogService.pushActionLog(SysActionLogDTO.builder()
                .actionDesc(String.format(ActionLogTemplateConstant.DEAL_ABNORMAL_ORDER, orderId, WithdrawOrderStatusEnum.REFUSE_WITHDRAW.getMsg()))
                .createUser(vo.getOperatorId())
                .loginIp(getIpAddr()).build());
        return Result.success();
    }




}