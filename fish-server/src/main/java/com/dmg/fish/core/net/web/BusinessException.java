package com.dmg.fish.core.net.web;

public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 2332608236621015980L;

    private String code;

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
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