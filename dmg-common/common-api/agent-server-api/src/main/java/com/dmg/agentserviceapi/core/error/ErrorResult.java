package com.dmg.agentserviceapi.core.error;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

/**
 * 错误结果
 */
@Data
public class ErrorResult {
    /** 错误码 */
    private int code;
    /** 错误描述 */
    private String desc;
    /** 错误详情 */
    private String details;

    /**
     * 默认构造方法
     */
    public ErrorResult() {

    }

    /**
     * 构造方法
     *
     * @param code 错误码
     * @param desc 错误描述
     * @param details 错误详情
     */
    public ErrorResult(int code, String desc, String details) {
        this.code = code;
        this.desc = desc;
        this.details = details;
    }

    /**
     * 获取错误消息
     */
    public String getMsg() {
        if (StringUtils.isBlank(this.details)) {
            return this.desc;
        } else {
            return this.desc + "--" + this.details;
        }
    }
}
