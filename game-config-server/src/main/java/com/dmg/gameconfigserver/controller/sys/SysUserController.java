package com.dmg.gameconfigserver.controller.sys;

import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.annotation.NoAuthMapping;
import com.dmg.gameconfigserver.annotation.NoNeedLoginMapping;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.common.constant.ActionLogTemplateConstant;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.controller.BaseController;
import com.dmg.gameconfigserver.model.dto.sys.SysActionLogDTO;
import com.dmg.gameconfigserver.model.dto.sys.SysUserDTO;
import com.dmg.gameconfigserver.model.dto.sys.SysUserLoginLogDTO;
import com.dmg.gameconfigserver.model.dto.sys.SysUserPageDTO;
import com.dmg.gameconfigserver.model.vo.group.LoginValid;
import com.dmg.gameconfigserver.model.vo.group.SaveValid;
import com.dmg.gameconfigserver.model.vo.group.UpdateValid;
import com.dmg.gameconfigserver.model.vo.sys.SysUserVO;
import com.dmg.gameconfigserver.service.sys.SysActionLogService;
import com.dmg.gameconfigserver.service.sys.SysResourceService;
import com.dmg.gameconfigserver.service.sys.SysUserService;
import com.dmg.gameconfigserver.service.sys.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 13:58 2019/11/6
 */
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "sys/user")
public class SysUserController extends BaseController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysResourceService sysResourceService;

    @Autowired
    private SysActionLogService sysActionLogService;


    @NoNeedLoginMapping
    @PostMapping("/login")
    public Result login(@RequestBody @Validated({LoginValid.class}) SysUserDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        SysUserVO sysUserVO = sysUserService.getUser(dto.getUserName(), dto.getPassword());
        if (sysUserVO == null) {
            return Result.error(ResultEnum.LOGIN_ERROR.getCode().toString(), ResultEnum.LOGIN_ERROR.getMsg());
        }
        if (!sysUserVO.getStatus()) {
            return Result.error(ResultEnum.USER_STATUS_ERROR.getCode().toString(), ResultEnum.USER_STATUS_ERROR.getMsg());
        }
        Map<String, Object> map = new HashMap<>();
        map.put(Constant.LOGIN_TOKEN_HEADER_KEY, tokenService.createToken(sysUserVO.getId()));
        map.put("user", sysUserVO);
        String ip = getIpAddr();
        //推送操作日志
        sysActionLogService.pushActionLog(SysActionLogDTO.builder()
                .actionDesc(ActionLogTemplateConstant.SYS_USER_LOGIN)
                .createUser(sysUserVO.getId())
                .loginIp(ip).build());
        //推送登陆日志
        sysUserService.pushLogin(SysUserLoginLogDTO.builder()
                .loginDate(new Date())
                .userId(sysUserVO.getId())
                .ip(ip).build());
        return Result.success(map);
    }

    @PostMapping("/loginOut")
    @NoNeedLoginMapping
    public Result loginOut(@RequestHeader(Constant.LOGIN_TOKEN_HEADER_KEY) String token) {
        Assert.hasLength(token, "Token为空");
        tokenService.destroyToken(token, getUserId());
        return Result.success();
    }

    @GetMapping("/getResource")
    public Result getAllList() {
        return Result.success(sysResourceService.getInfoByUserId(getUserId()));
    }

    @PostMapping("/getList")
    public Result getAllList(@RequestBody @Validated SysUserPageDTO sysUserPageDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        return Result.success(sysUserService.getUserList(sysUserPageDTO));
    }

    @PostMapping("/saveOne")
    public Result register(@RequestBody @Validated({SaveValid.class}) SysUserDTO sysUserDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        Boolean result = sysUserService.insert(sysUserDTO, getUserId());
        if (!result) {
            return Result.error(ResultEnum.USER_EXIST.getCode().toString(), ResultEnum.USER_EXIST.getMsg());
        }
        return Result.success();
    }

    @PostMapping("/updateOne")
    public Result update(@RequestBody @Validated({UpdateValid.class}) SysUserDTO sysUserDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        Boolean result = sysUserService.update(sysUserDTO, getIpAddr(), getUserId());
        if (!result) {
            return Result.error(ResultEnum.USER_NO_EXIST.getCode().toString(), ResultEnum.USER_NO_EXIST.getMsg());
        }
        return Result.success();
    }

    @GetMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Long id) {
        if (id == null) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), "id cannot be empty");
        }
        if (id == 1) {
            return Result.error(ResultEnum.NOT_AUTH.getCode().toString(), ResultEnum.NOT_AUTH.getMsg());
        }
        sysUserService.deleteById(id);
        return Result.success();
    }

    @NoAuthMapping
    @GetMapping("/getAll")
    public Result getAll() {
        return Result.success(sysUserService.getAllList());
    }
}
