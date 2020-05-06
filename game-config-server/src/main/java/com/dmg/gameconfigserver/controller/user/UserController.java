package com.dmg.gameconfigserver.controller.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.common.constant.ActionLogTemplateConstant;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.controller.BaseController;
import com.dmg.gameconfigserver.model.dto.sys.SysActionLogDTO;
import com.dmg.gameconfigserver.model.dto.user.UserDTO;
import com.dmg.gameconfigserver.model.vo.user.UserDetailVO;
import com.dmg.gameconfigserver.model.vo.user.UserListVO;
import com.dmg.gameconfigserver.service.sys.SysActionLogService;
import com.dmg.gameconfigserver.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


/**
 * @Author liubo
 * @Description //TODO
 * @Date 15:17 2019/11/20
 */
@Slf4j
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "member/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private SysActionLogService sysActionLogService;

    @PostMapping("/getList")
    public Result getAllList(@RequestBody @Validated UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        IPage<UserListVO> userList = userService.getUserPage(userDTO);
        return Result.success(userList);
    }

    @GetMapping("/getOne/{id}")
    public Result getUserInfo(@PathVariable("id") Integer id) {
        if (id == null) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), "id cannot be empty");
        }
        UserDetailVO userInfo = userService.getUserInfo(id);
        return Result.success(userInfo);
    }

    @PostMapping("/updateOne")
    public Result updateOne(@RequestBody @Validated UserDetailVO userInfo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        userService.update(userInfo);
        if (userInfo.getUser() != null) {
            if (StringUtils.isNotEmpty(userInfo.getUser().getPassword())) {
                try{
                //推送操作日志
                sysActionLogService.pushActionLog(SysActionLogDTO.builder()
                        .actionDesc(String.format(ActionLogTemplateConstant.UPDATE_USER_LOGIN_PASSWORD, String.valueOf(userInfo.getUser().getId())))
                        .createUser(getUserId())
                        .loginIp(getIpAddr()).build());
                } catch (Exception e) {
                    log.error("{}", e);
                }
            }
        }
        return Result.success();
    }

}
