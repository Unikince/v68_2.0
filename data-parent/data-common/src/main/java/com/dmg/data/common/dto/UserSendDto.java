package com.dmg.data.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 用户数据发送 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSendDto {
    /** 玩家id */
    private long userId;
}
