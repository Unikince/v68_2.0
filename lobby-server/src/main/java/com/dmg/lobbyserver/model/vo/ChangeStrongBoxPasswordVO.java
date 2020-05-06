package com.dmg.lobbyserver.model.vo;

import lombok.Data;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/20 16:56
 * @Version V1.0
 **/
@Data
public class ChangeStrongBoxPasswordVO {
    private Long userId;
    private String phone;
    private String validateCode;
    private String password;
}