package com.dmg.lobbyserver.model.vo;

import lombok.Data;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/20 16:41
 * @Version V1.0
 **/
@Data
public class SetStrongBoxPasswordVO {
    private Long userId;
    private String password;
}