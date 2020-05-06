package com.dmg.lobbyserver.model.vo;

import lombok.Data;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/18 15:29
 * @Version V1.0
 **/
@Data
public class UserNameLoginVO {
    @NotBlank
    private String userName;
    @NotBlank
    private String password;
    private String deviceCode;
}