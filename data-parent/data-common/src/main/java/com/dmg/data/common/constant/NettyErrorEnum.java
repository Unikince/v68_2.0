package com.dmg.data.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误枚举
 */
@Getter
@AllArgsConstructor
public enum NettyErrorEnum {
    /** 未知异常 */
    UNKNOWN(1001, "未知异常"),
    /** 网络错误 */
    SOCKET_ERROR(1002, "网络错误"),
    /** 同步等待超时 */
    SYNC_WAIT_OVER(1003, "同步等待超时"),
    /** 参数错误 */
    PARAM_ERROR(1004, "参数错误"),
    /** 玩家未找到 */
    PLAYER_NOT_FIND(2001, "玩家未找到"),
    /** 金币异常 */
    GOLD_ERROR(3001, "金币异常"),
    /** 金币不足 */
    GOLD_NOT_ENOUGH(3002, "金币不足"),

    ;

    /** 编码 */
    private int code;
    /** 描述 */
    private String desc;
}
