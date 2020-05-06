package com.dmg.gameconfigserver.controller.sys;

import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.controller.BaseController;
import com.dmg.gameconfigserver.model.dto.sys.SysActionLogPageDTO;
import com.dmg.gameconfigserver.service.sys.SysActionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * @Author liubo
 * @Description //TODO 操作日志管理
 * @Date 13:58 2019/11/6
 */
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "sys/actionLog")
public class SysActionLogController extends BaseController {

    @Autowired
    private SysActionLogService sysActionLogService;

    @PostMapping("/getList")
    public Result getAllList(@RequestBody SysActionLogPageDTO sysActionLogPageDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        return Result.success(sysActionLogService.getActionLogList(sysActionLogPageDTO));
    }
}
