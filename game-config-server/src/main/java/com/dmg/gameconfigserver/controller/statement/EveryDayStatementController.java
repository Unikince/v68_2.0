package com.dmg.gameconfigserver.controller.statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
import com.dmg.gameconfigserver.model.vo.statement.everyday.EveryDayStatementCollectReq;
import com.dmg.gameconfigserver.model.vo.statement.everyday.EveryDayStatementCollectRes;
import com.dmg.gameconfigserver.model.vo.statement.everyday.EveryDayStatementDayDataReq;
import com.dmg.gameconfigserver.model.vo.statement.everyday.EveryDayStatementDayDataRes;
import com.dmg.gameconfigserver.service.statement.EveryDayStatementService;

/**
 * 每日报表
 */
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "statement/everyDay")
public class EveryDayStatementController extends BaseController {
    @Autowired
    private EveryDayStatementService service;

    /** 每日自动生成昨日数据 */
    @Scheduled(cron = "0 0 0 1/1 * ? ")
    public void yesterdayData() {
        service.yesterdayData();
    }
    
    /** 汇总 */
    @PostMapping("/collect")
    @SuppressWarnings("rawtypes")
    public Result collect(@Validated @RequestBody EveryDayStatementCollectReq reqVo) {
        EveryDayStatementCollectRes resVo = this.service.collect(reqVo);
        return Result.success(resVo);
    }

    /** 每日数据 */
    @PostMapping("/dayData")
    @SuppressWarnings("rawtypes")
    public Result dayData(@Validated @RequestBody EveryDayStatementDayDataReq reqVo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        IPage<EveryDayStatementDayDataRes> resVo = this.service.dayData(reqVo);
        return Result.success(resVo);
    }
}
