package com.dmg.clubserver.exception;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: ChenHao
 * @Date: 2018/11/27  17:40
 */
@Slf4j
@Data
public class ScoketBusinessException extends RuntimeException {
    private static final long serialVersionUID = 2332608236621015980L;

    private String code;
    // 协议号
    private String cmd;

    public ScoketBusinessException(String cmd, String code, String message) {
        super(message);
        this.code = code;
        this.cmd = cmd;
    }


}