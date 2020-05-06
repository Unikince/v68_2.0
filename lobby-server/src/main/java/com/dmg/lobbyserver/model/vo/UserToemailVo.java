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
public class UserToemailVo {
    @NotBlank
    private  String email;//邮箱账户
    @NotBlank
    private String Code;//邮箱验证码
}
