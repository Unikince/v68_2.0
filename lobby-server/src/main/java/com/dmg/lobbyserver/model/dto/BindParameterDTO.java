package com.dmg.lobbyserver.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Author:刘将军
 * Time:2019/6/20 15:06
 * Created by IntelliJ IDEA Community
 */
@Data
public class BindParameterDTO implements Serializable {
    /**
     * 电话号码
     */
    private String phone;
    /**
     * 密码
     */
    private String password;
    /**
     * 验证码
     */
    private String validateCode;
}
