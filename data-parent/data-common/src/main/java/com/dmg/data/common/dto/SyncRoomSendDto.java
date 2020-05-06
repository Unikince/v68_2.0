package com.dmg.data.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 同步房间发送 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SyncRoomSendDto {
    /** 游戏id */
    private int gameId;
    /** 房间级别 */
    private int roomLevel;
    /** 房间id */
    private int roomId;
    /** 玩家id */
    private long userId;
}
