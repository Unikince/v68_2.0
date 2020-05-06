package com.dmg.lobbyserver.model.vo;

import lombok.Data;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @Description
 * @Author jock
 * @Date 2019/6/19 0019
 * @Version V1.0
 **/
@Data
public class UserPhoneVo {
    @NotBlank
    private String OldPhone;//旧手机号
    @NotBlank
    private String NewPhone;//新手机号
    @NotBlank
    private String verification;//验证码
}
