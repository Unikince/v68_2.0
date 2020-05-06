package com.dmg.gameconfigserver.controller.statement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.controller.BaseController;
import com.dmg.gameconfigserver.model.vo.statement.recharge.RechargeStatementCollectReq;
import com.dmg.gameconfigserver.model.vo.statement.recharge.RechargeStatementCollectRes;
import com.dmg.gameconfigserver.model.vo.statement.recharge.RechargeStatementDatailsCollectDayDataReq;
import com.dmg.gameconfigserver.model.vo.statement.recharge.RechargeStatementDatailsCollectDayDataRes;
import com.dmg.gameconfigserver.model.vo.statement.recharge.RechargeStatementDatailsCollectReq;
import com.dmg.gameconfigserver.model.vo.statement.recharge.RechargeStatementDatailsCollectRes;
import com.dmg.gameconfigserver.model.vo.statement.recharge.RechargeStatementDayDataReq;
import com.dmg.gameconfigserver.model.vo.statement.recharge.RechargeStatementDayDataRes;
import com.dmg.gameconfigserver.service.statement.RechargeStatementService;

/**
 * 充值报表
 */
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "statement/recharge")
public class RechargeStatementController extends BaseController {
    @Autowired
    private RechargeStatementService service;

    /** 汇总 */
    @PostMapping("/collect")
    @SuppressWarnings("rawtypes")
    public Result collect(@Validated @RequestBody RechargeStatementCollectReq reqVo) {
        List<RechargeStatementCollectRes> resVos = this.service.collect(reqVo);
        return Result.success(resVos);
    }

    /** 每日数据 */
    @PostMapping("/dayData")
    @SuppressWarnings("rawtypes")
    public Result dayData(@Validated @RequestBody RechargeStatementDayDataReq reqVo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        IPage<RechargeStatementDayDataRes> resVo = this.service.dayData(reqVo);
        return Result.success(resVo);
    }

    /** 充值详情 */
    @PostMapping("/datailsCollect")
    @SuppressWarnings("rawtypes")
    public Result datailsCollect(@Validated @RequestBody RechargeStatementDatailsCollectReq reqVo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        IPage<RechargeStatementDatailsCollectRes> resVo = this.service.datailsCollect(reqVo);
        return Result.success(resVo);
    }

    /** 充值详情_每日数据 */
    @PostMapping("/datailsCollectDayData")
    @SuppressWarnings("rawtypes")
    public Result datailsCollectDayData(@Validated @RequestBody RechargeStatementDatailsCollectDayDataReq reqVo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        IPage<RechargeStatementDatailsCollectDayDataRes> resVo = this.service.datailsCollectDayData(reqVo);
        return Result.success(resVo);
    }
}
