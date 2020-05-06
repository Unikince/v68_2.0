package com.dmg.doudizhuserver.core.net.web;

import java.io.Serializable;

import lombok.Data;

/**
 * @Author: ChenHao
 * @Date: 2018/5/22 17:16
 */
@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 5275072466067684895L;
    private String code;
    private String msg;
    private T data;

    public Result(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result(String code, T data) {
        this.data = data;
    }

    public static <T> Result<T> error(String code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>("1", "成功", data);
        return result;
    }

    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.code = "1";
        result.msg = "成功";
        return result;
    }

    public Result() {
    }
}