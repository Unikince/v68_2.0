package com.dmg.doudizhuserver.core.net.web;

/**
 * @Author: ChenHao
 * @Date: 2018/11/27 17:40
 */
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 2332608236621015980L;

    private String code;

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message) {
        super(message);
        this.code = "2";
    }

    public BusinessException() {
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}