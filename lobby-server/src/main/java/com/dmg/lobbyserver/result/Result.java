package com.dmg.lobbyserver.result;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * @Author: ChenHao
 * @Date: 2018/5/22 17:16
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 5940505252493230845L;
    private Integer code;
    private String msg;
    private T data;

    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Result error(Integer code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static Result success(Object data) {
        Result result = new Result(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),data);
        return result;
    }

    public static Result success() {
        Result result = new Result(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),null);
        return result;
    }


    public Result() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

   /* @Override
    public String toString() {
        return "{\"code\": \"" + code + "\",\"msg\": \"" + msg + "\",\"data\": " + data + "}";
    }*/
    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}