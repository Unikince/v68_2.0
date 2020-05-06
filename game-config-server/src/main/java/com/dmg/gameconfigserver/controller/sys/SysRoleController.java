package com.dmg.gameconfigserver.controller.sys;

import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.controller.BaseController;
import com.dmg.gameconfigserver.model.dto.CommonRespDTO;
import com.dmg.gameconfigserver.model.dto.sys.SysRoleDTO;
import com.dmg.gameconfigserver.model.dto.sys.SysRolePageDTO;
import com.dmg.gameconfigserver.model.vo.group.SaveValid;
import com.dmg.gameconfigserver.model.vo.group.UpdateValid;
import com.dmg.gameconfigserver.service.sys.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author liubo
 * @Description //TODO 角色管理
 * @Date 13:58 2019/11/6
 */
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "sys/role")
public class SysRoleController extends BaseController {

    @Autowired
    private SysRoleService sysRoleService;

    @PostMapping("/getList")
    public Result getAllList(@RequestBody SysRolePageDTO sysRolePageDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        return Result.success(sysRoleService.getRoleList(sysRolePageDTO));
    }

    @GetMapping("/getAll")
    public Result getAllList() {
        return Result.success(sysRoleService.getAllList());
    }

    @GetMapping("/getOne/{id}")
    public Result getInfoById(@PathVariable("id") Long id) {
        if (id == null) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), "fileBaseId cannot be empty");
        }
        return Result.success(sysRoleService.getInfoById(id));
    }

    @PostMapping("/saveOne")
    public Result register(@RequestBody @Validated({SaveValid.class}) SysRoleDTO sysRoleDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        Boolean result = sysRoleService.insert(sysRoleDTO, getUserId());
        if (!result) {
            return Result.error(ResultEnum.HAS_EXIT.getCode().toString(), ResultEnum.HAS_EXIT.getMsg());
        }
        return Result.success();
    }

    @PostMapping("/updateOne")
    public Result update(@RequestBody @Validated({UpdateValid.class}) SysRoleDTO sysRoleDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        CommonRespDTO result = sysRoleService.update(sysRoleDTO, getUserId());
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
        sysRoleService.deleteById(id);
        return Result.success();
    }
}
