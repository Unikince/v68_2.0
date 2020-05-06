package com.dmg.lobbyserver.model.vo;

import lombok.Data;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/19 17:01
 * @Version V1.0
 **/
@Data
public class LoginLobbyVO {
    private Long userId;
    private String sign;
}