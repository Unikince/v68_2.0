package com.dmg.lobbyserver.service;

import com.dmg.lobbyserver.model.dto.HttpLoginDTO;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/18 11:30
 * @Version V1.0
 **/
public interface LoginService {

    /**
     * @description: 游客登录
     * @param userId
     * @return com.dmg.lobbyserver.model.dto.HttpLoginDTO
     * @author mice
     * @date 2019/6/18
    */
    HttpLoginDTO touristLogin(String deviceCode,Long userId,String ip);

    /**
     * 微信登录
     *
     * @param code
     * @param deviceType
     */
    HttpLoginDTO weChatLogin(String deviceCode,String code, int deviceType);

    /**
     * 账号登录
     *
     * @param userName
     * @param password
     */
    HttpLoginDTO userNameLogin(String deviceCode,String userName, String password);

    /**
     * 账号注册
     *
     * @param userName
     * @param password
     */
    HttpLoginDTO userNameRegiste(String deviceCode,String userName, String password,String ip);

    /**
     * 手机一键注册
     *
     * @param phone
     * @param deviceCode
     */
    HttpLoginDTO oneClickRegiste(String phone, String deviceCode,String ip);

    /**
     * 手机一键登录
     *
     * @param phone
     * @param deviceCode
     */
    HttpLoginDTO oneClickLogin(String phone, String deviceCode,String ip);

    /**
     * 验证码登录
     *
     * @param phone
     * @param validateCode
     */
    HttpLoginDTO validateCodeLogin(String deviceCode,String phone, String validateCode,String ip);

    /**
     * 人脸 手势 指纹登录
     *
     * @param userId
     * @param sign
     */
    HttpLoginDTO keyLogin(String deviceCode,Long userId, String sign);

    /**
     * 检查手机号和用户名是否一致
     *
     * @param phone
     * @param userName
     * @param validateCode
     */
    Boolean checkPhoneUserName(String phone, String userName, String validateCode);

    /**
     * 修改密码
     *
     * @param userName
     * @param password
     */
    boolean changePassword(String userName, String password);




}