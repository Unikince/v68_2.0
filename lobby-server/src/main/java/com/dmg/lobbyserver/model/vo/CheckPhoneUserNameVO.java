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
public class CheckPhoneUserNameVO {
    @NotBlank
    private String phone;
    @NotBlank
    private String userName;
    @NotBlank
    private String validateCode;

}