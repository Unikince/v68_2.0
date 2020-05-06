package com.dmg.lobbyserver.controller;

import com.dmg.lobbyserver.common.util.HttpServletUtil;
import com.dmg.lobbyserver.model.dto.HttpLoginDTO;
import com.dmg.lobbyserver.model.vo.*;
import com.dmg.lobbyserver.result.Result;
import com.dmg.lobbyserver.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description 登录
 * @Author mice
 * @Date 2019/6/18 9:45
 * @Version V1.0
 **/
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * @description: 游客登录
     * @param userId
     * @return com.dmg.lobbyserver.result.Result
     * @author mice
     * @date 2019/6/18
    */
    @PostMapping("/touristLogin")
    public Result touristLogin(HttpServletRequest servletRequest,@RequestParam("deviceCode")String deviceCode,@RequestParam("userId") Long userId){
        HttpLoginDTO httpLoginDTO = loginService.touristLogin(deviceCode,userId, HttpServletUtil.getIpAddr(servletRequest));
        return Result.success(httpLoginDTO);
    }

    /**
     * @description: 人脸 手势 指纹登录
     * @param
     * @return com.dmg.lobbyserver.result.Result
     * @author mice
     * @date 2019/6/18
     */
    @PostMapping("/keyLogin")
    public Result keyLogin(@Validated @ModelAttribute KeyLoginVO vo){
        HttpLoginDTO httpLoginDTO = loginService.keyLogin(vo.getDeviceCode(),vo.getUserId(),vo.getSign());
        return Result.success(httpLoginDTO);
    }

    /**
     * @description: 微信登录
     * @param
     * @return com.dmg.lobbyserver.result.Result
     * @author mice
     * @date 2019/6/18
     */
    @PostMapping("/weChatLogin")
    public Result weChatLogin(@Validated @ModelAttribute WeChatLoginVO vo){
        HttpLoginDTO httpLoginDTO = loginService.weChatLogin(vo.getDeviceCode(),vo.getCode(),vo.getDeviceType());
        return Result.success(httpLoginDTO);
    }

    /**
     * @description: 账号登录
     * @param
     * @return com.dmg.lobbyserver.result.Result
     * @author mice
     * @date 2019/6/18
     */
    @PostMapping("/userNameLogin")
    public Result userNameLogin(@Validated @ModelAttribute UserNameLoginVO vo){
        HttpLoginDTO httpLoginDTO = loginService.userNameLogin(vo.getDeviceCode(),vo.getUserName(),vo.getPassword());
        return Result.success(httpLoginDTO);
    }

    /**
     * @description: 账号注册
     * @param
     * @return com.dmg.lobbyserver.result.Result
     * @author mice
     * @date 2019/6/18
     */
    @PostMapping("/userNameRegiste")
    public Result userNameRegiste(HttpServletRequest servletRequest,@Validated @ModelAttribute UserNameLoginVO vo){
        HttpLoginDTO httpLoginDTO = loginService.userNameRegiste(vo.getDeviceCode(),vo.getUserName(),vo.getPassword(),HttpServletUtil.getIpAddr(servletRequest));
        return Result.success(httpLoginDTO);
    }

    /**
     * @description: 一键注册
     * @param
     * @return com.dmg.lobbyserver.result.Result
     * @author mice
     * @date 2019/6/18
     */
    @PostMapping("/oneClickRegiste")
    public Result oneClickRegiste(HttpServletRequest servletRequest,@Validated @ModelAttribute OneClickRegisteVO vo){
        HttpLoginDTO httpLoginDTO = loginService.oneClickRegiste(vo.getPhone(),vo.getDeviceCode(),HttpServletUtil.getIpAddr(servletRequest));
        return Result.success(httpLoginDTO);
    }

    /**
     * @description: 一键登录
     * @param
     * @return com.dmg.lobbyserver.result.Result
     * @author mice
     * @date 2019/6/18
     */
    @PostMapping("/oneClickLogin")
    public Result oneClickLogin(HttpServletRequest servletRequest, @Validated @ModelAttribute OneClickLoginVO vo){
        HttpLoginDTO httpLoginDTO = loginService.oneClickLogin(vo.getPhone(),vo.getDeviceCode(),HttpServletUtil.getIpAddr(servletRequest));
        return Result.success(httpLoginDTO);
    }

    /**
     * @description: 验证码登录
     * @param
     * @return com.dmg.lobbyserver.result.Result
     * @author mice
     * @date 2019/6/18
     */
    @PostMapping("/validateCodeLogin")
    public Result validateCodeLogin(HttpServletRequest servletRequest,@Validated @ModelAttribute ValidateCodeLoginVO vo){
        HttpLoginDTO httpLoginDTO = loginService.validateCodeLogin(vo.getDeviceCode(),vo.getPhone(),vo.getValidateCode(),HttpServletUtil.getIpAddr(servletRequest));
        return Result.success(httpLoginDTO);
    }

    /**
     * @description: 检查手机号和用户名是否一致
     * @param
     * @return com.dmg.lobbyserver.result.Result
     * @author mice
     * @date 2019/6/18
     */
    @PostMapping("/checkPhoneUserName")
    public Result checkPhoneUserName(@Validated @ModelAttribute CheckPhoneUserNameVO vo){
        boolean success = loginService.checkPhoneUserName(vo.getPhone(),vo.getUserName(),vo.getValidateCode());
        return Result.success(success);
    }

    /**
     * @description: 修改密码
     * @param
     * @return com.dmg.lobbyserver.result.Result
     * @author mice
     * @date 2019/6/18
     */
    @PostMapping("/changePassword")
    public Result changePassword(@Validated @ModelAttribute ChangePasswordVO vo){
        boolean success = loginService.changePassword(vo.getUserName(),vo.getPassword());
        return Result.success(success);
    }


}