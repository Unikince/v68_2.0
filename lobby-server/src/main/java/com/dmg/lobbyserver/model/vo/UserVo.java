package com.dmg.lobbyserver.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Description
 * @Author JOCK
 * @Date 2019/6/18 0018
 * @Version V1.0
 **/
@Data
public class UserVo {
    //真实姓名
    private String RealName;
    //电话号码
    private String Phone;
    //旧密码
    private String OldPassword;
    //新密码
    private String NewPassword;
   //生日
    private Date Birth;
    //游戏名称
    private String UserName;
    //验证码
    private String code;
    //旧邮箱
        private String oldEmail;
    //新邮箱
    private String newEmail;
    //邮箱验证码
    private String emailCode;

}
