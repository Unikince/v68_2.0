package com.dmg.data.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 同步房间接收 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SyncRoomRecvDto {
    /** 状态码(0:成功,-1未知错误) */
    private int code;
}
