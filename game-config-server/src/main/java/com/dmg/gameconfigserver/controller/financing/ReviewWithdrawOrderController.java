package com.dmg.gameconfigserver.controller.financing;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.annotation.NoNeedLoginMapping;
import com.dmg.gameconfigserver.common.constant.ActionLogTemplateConstant;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.controller.BaseController;
import com.dmg.gameconfigserver.model.bean.finance.WithdrawOrderBean;
import com.dmg.gameconfigserver.model.dto.finance.QueryWithdrawOrderDTO;
import com.dmg.gameconfigserver.model.dto.finance.ReviewWithdrawOrderDTO;
import com.dmg.gameconfigserver.model.dto.sys.SysActionLogDTO;
import com.dmg.gameconfigserver.model.vo.finance.ApplyReviewWithdrawOrderVO;
import com.dmg.gameconfigserver.model.vo.finance.ReviewWithdrawOrderVO;
import com.dmg.gameconfigserver.service.financing.WithdrawOrderService;
import com.dmg.gameconfigserver.service.sys.SysActionLogService;
import com.dmg.server.common.enums.WithdrawOrderStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @Description
 * @Author mice
 * @Date 2019/12/27 10:11
 * @Version V1.0
 **/
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "finance/reviewWithdrawOrder")
public class ReviewWithdrawOrderController extends BaseController {

    @Autowired
    private WithdrawOrderService withdrawOrderService;
    @Autowired
    private SysActionLogService sysActionLogService;

    @PostMapping("/getList")
    public Result queryWithdrawOrderPage(@RequestBody QueryWithdrawOrderDTO queryWithdrawOrderDTO){
        queryWithdrawOrderDTO.setOrderStatus(WithdrawOrderStatusEnum.WAITE_REVIEW.getCode());
        IPage<WithdrawOrderBean> withdrawOrderBeanIPage = withdrawOrderService.queryReviewWithdrawOrderPage(queryWithdrawOrderDTO);
        return Result.success(withdrawOrderBeanIPage);
    }

    @PostMapping("/applyReviewOrder")
    public Result applyReviewOrder(@Validated @RequestBody ApplyReviewWithdrawOrderVO vo){
        ReviewWithdrawOrderDTO reviewWithdrawOrderDTO = withdrawOrderService.applyReviewOrder(vo.getReviewerId(),vo.getId());
        return Result.success(reviewWithdrawOrderDTO);
    }

    @PostMapping("/reviewOrder")
    public Result reviewOrder(@Validated @RequestBody ReviewWithdrawOrderVO vo){
        String orderId = withdrawOrderService.reviewOrder(vo.getReviewerId(),vo.getId(),vo.getReviewStatus(),vo.getRemark());
        //推送操作日志
        if (vo.getReviewStatus()!=0){
            String agree = vo.getReviewStatus()==5? "拒绝": "通过";
            sysActionLogService.pushActionLog(SysActionLogDTO.builder()
                    .actionDesc(String.format(ActionLogTemplateConstant.REVIEW_WITHDRAW_ORDER, agree,orderId))
                    .createUser(Long.valueOf(vo.getReviewerId()))
                    .loginIp(getIpAddr()).build());
        }

        return Result.success();
    }


}