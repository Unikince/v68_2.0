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
import com.dmg.gameconfigserver.model.vo.statement.player.PlayerStatementCollectReq;
import com.dmg.gameconfigserver.model.vo.statement.player.PlayerStatementCollectRes;
import com.dmg.gameconfigserver.model.vo.statement.player.PlayerStatementDatailsCollectDayDataReq;
import com.dmg.gameconfigserver.model.vo.statement.player.PlayerStatementDatailsCollectDayDataRes;
import com.dmg.gameconfigserver.model.vo.statement.player.PlayerStatementDatailsCollectReq;
import com.dmg.gameconfigserver.model.vo.statement.player.PlayerStatementDatailsCollectRes;
import com.dmg.gameconfigserver.model.vo.statement.player.PlayerStatementDayDataDetailsReq;
import com.dmg.gameconfigserver.model.vo.statement.player.PlayerStatementDayDataDetailsRes;
import com.dmg.gameconfigserver.model.vo.statement.player.PlayerStatementDayDataReq;
import com.dmg.gameconfigserver.model.vo.statement.player.PlayerStatementDayDataRes;
import com.dmg.gameconfigserver.service.statement.PlayerStatementService;

/**
 * 玩家报表
 */
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "statement/player")
public class PlayerStatementController extends BaseController {
    @Autowired
    private PlayerStatementService service;

    /** 汇总 */
    @PostMapping("/collect")
    @SuppressWarnings("rawtypes")
    public Result collect(@Validated @RequestBody PlayerStatementCollectReq reqVo) {
        PlayerStatementCollectRes resVo = this.service.collect(reqVo);
        return Result.success(resVo);
    }

    /** 每日数据 */
    @PostMapping("/dayData")
    @SuppressWarnings("rawtypes")
    public Result dayData(@Validated @RequestBody PlayerStatementDayDataReq reqVo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        IPage<PlayerStatementDayDataRes> resVo = this.service.dayData(reqVo);
        return Result.success(resVo);
    }

    /** 每日数据_游戏详情 */
    @PostMapping("/dayDataDetails")
    @SuppressWarnings("rawtypes")
    public Result dayDataDetails(@Validated @RequestBody PlayerStatementDayDataDetailsReq reqVo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        IPage<PlayerStatementDayDataDetailsRes> resVo = this.service.dayDataDetails(reqVo);
        return Result.success(resVo);
    }

    /** 游戏详情 */
    @PostMapping("/datailsCollect")
    @SuppressWarnings("rawtypes")
    public Result datailsCollect(@Validated @RequestBody PlayerStatementDatailsCollectReq reqVo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        IPage<PlayerStatementDatailsCollectRes> resVo = this.service.datailsCollect(reqVo);
        return Result.success(resVo);
    }

    /** 游戏详情_每日数据 */
    @PostMapping("/datailsCollectDayData")
    @SuppressWarnings("rawtypes")
    public Result datailsCollectDayData(@Validated @RequestBody PlayerStatementDatailsCollectDayDataReq reqVo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        IPage<PlayerStatementDatailsCollectDayDataRes> resVo = this.service.datailsCollectDayData(reqVo);
        return Result.success(resVo);
    }
}
