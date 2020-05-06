package com.dmg.gameconfigserver.controller.sys;

import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.controller.BaseController;
import com.dmg.gameconfigserver.model.dto.CommonRespDTO;
import com.dmg.gameconfigserver.model.dto.sys.SysWhiteDTO;
import com.dmg.gameconfigserver.model.dto.sys.SysWhitePageDTO;
import com.dmg.gameconfigserver.model.vo.group.SaveValid;
import com.dmg.gameconfigserver.model.vo.group.UpdateValid;
import com.dmg.gameconfigserver.service.sys.SysWhiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author liubo
 * @Description //TODO 白名单管理
 * @Date 13:58 2019/11/6
 */
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "sys/white")
public class SysWhiteController extends BaseController {

    @Autowired
    private SysWhiteService sysWhiteService;

    @PostMapping("/getList")
    public Result getAllList(@RequestBody SysWhitePageDTO sysWhitePageDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        return Result.success(sysWhiteService.getWhiteList(sysWhitePageDTO));
    }

    @PostMapping("/saveOne")
    public Result register(@RequestBody @Validated({SaveValid.class}) SysWhiteDTO sysWhiteDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        Boolean result = sysWhiteService.insert(sysWhiteDTO, getUserId());
        if (!result) {
            return Result.error(ResultEnum.HAS_EXIT.getCode().toString(), ResultEnum.HAS_EXIT.getMsg());
        }
        return Result.success();
    }

    @PostMapping("/updateOne")
    public Result update(@RequestBody @Validated({UpdateValid.class}) SysWhiteDTO sysWhiteDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        CommonRespDTO result = sysWhiteService.update(sysWhiteDTO, getIpAddr(), getUserId());
        if (!result.getStatus()) {
            return Result.error(result.getResult().getCode().toString(), result.getResult().getMsg());
        }
        return Result.success();
    }

    @GetMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Long id) {
        if (id == null) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), "id cannot be empty");
        }
        sysWhiteService.deleteById(id);
        return Result.success();
    }
}
