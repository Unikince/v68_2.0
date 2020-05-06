package com.dmg.dataserver.common.net.web;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

public class Result implements Serializable {

    private static final long serialVersionUID = 5940505252493230845L;
    private String code;
    private String msg;
    private Object data;

    public Result(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result(String code, Object data) {
        this.data = data;
    }

    public static Result error(String code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static Result success(Object data) {
        Result result = new Result(BaseResultEnum.SUCCESS.getCode() + "", BaseResultEnum.SUCCESS.getMsg(), data);
        return result;
    }

    public static Result success() {
        Result result = new Result(BaseResultEnum.SUCCESS.getCode().toString(), BaseResultEnum.SUCCESS.getMsg(), null);
        return result;
    }

    public Result() {
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}