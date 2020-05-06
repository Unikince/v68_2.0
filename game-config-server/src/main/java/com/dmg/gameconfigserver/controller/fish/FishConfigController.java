package com.dmg.gameconfigserver.controller.fish;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.model.bean.fish.FishCtrlReturnRateBean;
import com.dmg.gameconfigserver.model.bean.fish.FishCtrlStockBean;
import com.dmg.gameconfigserver.model.vo.fish.FishCtrlStockReq;
import com.dmg.gameconfigserver.model.vo.fish.FishRoomConfigGetReq;
import com.dmg.gameconfigserver.model.vo.fish.FishRoomConfigGetRes;
import com.dmg.gameconfigserver.model.vo.fish.FishRoomConfigUpdateReq;
import com.dmg.gameconfigserver.model.vo.fish.FishRoomInfoGetRes;
import com.dmg.gameconfigserver.service.fish.FishConfigService;

/**
 * 捕鱼配置接口
 */
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "game/config/fish")
public class FishConfigController {
    @Autowired
    private FishConfigService fishConfigService;

    /** 获取捕鱼房间信息 */
    @PostMapping("/getFishRoomInfo")
    @SuppressWarnings("rawtypes")
    public Result getFishRoomInfo() {
        List<FishRoomInfoGetRes> resVos = this.fishConfigService.getFishRoomInfo();
        return Result.success(resVos);
    }

    /** 获取捕鱼房间配置 */
    @PostMapping("/getFishRoomConfig")
    @SuppressWarnings("rawtypes")
    public Result getFishRoomConfig(@RequestBody FishRoomConfigGetReq reqVo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        FishRoomConfigGetRes resVo = this.fishConfigService.getFishRoomConfig(reqVo);
        return Result.success(resVo);
    }

    /** 修改捕鱼房间配置 */
    @PostMapping("/updateFishRoomConfig")
    @SuppressWarnings("rawtypes")
    public Result updateFishRoomConfig(@RequestBody FishRoomConfigUpdateReq reqVo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        this.fishConfigService.updateFishRoomConfig(reqVo);
        return Result.success();
    }

    /** 获取捕鱼返奖率控制 */
    @PostMapping("/getFishCtrlReturnRate")
    @SuppressWarnings("rawtypes")
    public Result getFishCtrlReturnRate() {
        List<FishCtrlReturnRateBean> resVos = this.fishConfigService.getFishCtrlReturnRate();
        return Result.success(resVos);
    }

    /** 修改捕鱼返奖率控制 */
    @PostMapping("/updateFishCtrlReturnRate")
    @SuppressWarnings("rawtypes")
    public Result updateFishCtrlReturnRate(@RequestBody List<FishCtrlReturnRateBean> beans) {
        this.fishConfigService.updateFishCtrlReturnRate(beans);
        return Result.success();
    }

    /** 获取捕鱼库存控制 */
    @PostMapping("/getFishCtrlStock")
    @SuppressWarnings("rawtypes")
    public Result getFishCtrlStock(@RequestBody FishCtrlStockReq req) {
        FishCtrlStockBean res = this.fishConfigService.getFishCtrlStock(req.getId());
        return Result.success(res);
    }

    /** 修改捕鱼库存控制 */
    @PostMapping("/updateFishCtrlStock")
    @SuppressWarnings("rawtypes")
    public Result updateFishCtrlStock(@RequestBody FishCtrlStockBean bean) {
        this.fishConfigService.updateFishCtrlStock(bean);
        return Result.success();
    }
}