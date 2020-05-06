package com.dmg.agentserver.core.exception;

/**
 * 错误枚举接口
 */
public interface IErrorEnum {
    /**
     * 获取：错误码
     *
     * @return 错误码
     */
    public int getCode();

    /**
     * 获取：错误描述
     *
     * @return 错误描述
     */
    public String getDesc();
}
