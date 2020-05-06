package com.dmg.gameconfigserver.controller.statement;

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
import com.dmg.gameconfigserver.model.vo.statement.withdraw.WithdrawStatementCollectReq;
import com.dmg.gameconfigserver.model.vo.statement.withdraw.WithdrawStatementCollectRes;
import com.dmg.gameconfigserver.model.vo.statement.withdraw.WithdrawStatementDayDataReq;
import com.dmg.gameconfigserver.model.vo.statement.withdraw.WithdrawStatementDayDataRes;
import com.dmg.gameconfigserver.service.statement.WithdrawStatementService;

/**
 * 提现报表
 */
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "statement/withdraw")
public class WithdrawStatementController extends BaseController {
    @Autowired
    private WithdrawStatementService service;
    
    /** 汇总 */
    @PostMapping("/collect")
    @SuppressWarnings("rawtypes")
    public Result collect(@Validated @RequestBody WithdrawStatementCollectReq reqVo) {
        WithdrawStatementCollectRes resVo = this.service.collect(reqVo);
        return Result.success(resVo);
    }

    /** 每日数据 */
    @PostMapping("/dayData")
    @SuppressWarnings("rawtypes")
    public Result dayData(@Validated @RequestBody WithdrawStatementDayDataReq reqVo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        IPage<WithdrawStatementDayDataRes> resVo = this.service.dayData(reqVo);
        return Result.success(resVo);
    }
}
