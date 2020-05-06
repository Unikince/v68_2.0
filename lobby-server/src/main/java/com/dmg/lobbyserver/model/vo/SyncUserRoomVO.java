package com.dmg.lobbyserver.model.vo;

import lombok.Data;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/6 19:51
 * @Version V1.0
 **/
@Data
public class SyncUserRoomVO {
    private Long userId;
    private Integer gameType;
    private Integer roomId;
}