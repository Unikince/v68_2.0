package com.dmg.lobbyserver.model.vo;

import lombok.Data;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/24 11:13
 * @Version V1.0
 **/
@Data
public class ValidatePasswordVO {
    private Long userId;
    private String password;
}