package com.dmg.gameconfigserver.controller.statement;

import com.dmg.gameconfigserver.common.constant.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.controller.BaseController;
import com.dmg.gameconfigserver.model.vo.statement.game.GameStatementCollectReq;
import com.dmg.gameconfigserver.model.vo.statement.game.GameStatementCollectRes;
import com.dmg.gameconfigserver.model.vo.statement.game.GameStatementDatailsCollectDayDataReq;
import com.dmg.gameconfigserver.model.vo.statement.game.GameStatementDatailsCollectDayDataRes;
import com.dmg.gameconfigserver.model.vo.statement.game.GameStatementDatailsCollectReq;
import com.dmg.gameconfigserver.model.vo.statement.game.GameStatementDatailsCollectRes;
import com.dmg.gameconfigserver.model.vo.statement.game.GameStatementDayDataDetailsReq;
import com.dmg.gameconfigserver.model.vo.statement.game.GameStatementDayDataDetailsRes;
import com.dmg.gameconfigserver.model.vo.statement.game.GameStatementDayDataReq;
import com.dmg.gameconfigserver.model.vo.statement.game.GameStatementDayDataRes;
import com.dmg.gameconfigserver.service.statement.GameStatementService;

/**
 * 游戏报表
 */
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "statement/game")
public class GameStatementController extends BaseController {
    @Autowired
    private GameStatementService service;

    /** 汇总 */
    @PostMapping("/collect")
    @SuppressWarnings("rawtypes")
    public Result collect(@Validated @RequestBody GameStatementCollectReq reqVo) {
        GameStatementCollectRes resVo = this.service.collect(reqVo);
        return Result.success(resVo);
    }

    /** 每日数据 */
    @PostMapping("/dayData")
    @SuppressWarnings("rawtypes")
    public Result dayData(@Validated @RequestBody GameStatementDayDataReq reqVo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        IPage<GameStatementDayDataRes> resVo = this.service.dayData(reqVo);
        return Result.success(resVo);
    }

    /** 每日数据_游戏详情 */
    @PostMapping("/dayDataDetails")
    @SuppressWarnings("rawtypes")
    public Result dayData(@Validated @RequestBody GameStatementDayDataDetailsReq reqVo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        IPage<GameStatementDayDataDetailsRes> resVo = this.service.dayDataDetails(reqVo);
        return Result.success(resVo);
    }

    /** 游戏详情 */
    @PostMapping("/datailsCollect")
    @SuppressWarnings("rawtypes")
    public Result datailsCollect(@Validated @RequestBody GameStatementDatailsCollectReq reqVo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        IPage<GameStatementDatailsCollectRes> resVo = this.service.datailsCollect(reqVo);
        return Result.success(resVo);
    }

    /** 游戏详情_每日数据 */
    @PostMapping("/datailsCollectDayData")
    @SuppressWarnings("rawtypes")
    public Result datailsCollectDayData(@Validated @RequestBody GameStatementDatailsCollectDayDataReq reqVo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        IPage<GameStatementDatailsCollectDayDataRes> resVo = this.service.datailsCollectDayData(reqVo);
        return Result.success(resVo);
    }
}
