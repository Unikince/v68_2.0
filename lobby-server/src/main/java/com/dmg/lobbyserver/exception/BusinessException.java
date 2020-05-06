package com.dmg.lobbyserver.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: ChenHao
 * @Date: 2018/11/27  17:40
 */
@Slf4j
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 2332608236621015980L;

    private Integer code;

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}