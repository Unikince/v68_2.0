package com.dmg.lobbyserver.model.dto;

import lombok.Data;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/19 18:07
 * @Version V1.0
 **/
@Data
public class LobbyActivityInfoDTO {
    // 新手礼包
    private boolean newUser=true;
    // 绑定有礼
    private boolean bindPhone=true;
    private boolean withdraw=false;
}