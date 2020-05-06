package com.dmg.agentserviceapi.core.error;

import lombok.Data;

/**
 * 结果返回
 */
@Data
public class ResultAgent<T> {
    /** 是否成功，成功则data有数据，失败则error有数据 */
    private boolean success;
    /** 正确结果 */
    private T data;
    /** 错误结果 */
    private ErrorResult error;

    /**
     * 成功
     *
     * @param data 正确结果
     */
    public static <T> ResultAgent<T> success(T data) {
        ResultAgent<T> result = new ResultAgent<>();
        result.success = true;
        result.data = data;
        result.error = null;
        return result;
    }

    /**
     * 成功
     */
    public static ResultAgent<Void> success() {
        ResultAgent<Void> result = new ResultAgent<>();
        result.success = true;
        result.data = null;
        result.error = null;
        return result;
    }
}