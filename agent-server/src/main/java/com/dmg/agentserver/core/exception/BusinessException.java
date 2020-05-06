package com.dmg.agentserver.core.exception;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

/**
 * 自定义异常
 */
public class BusinessException extends RuntimeException {
    /** 串行版本标识 */
    private static final long serialVersionUID = 1L;

    /** 错误枚举 */
    private IErrorEnum errorEnum;
    /** 错误详情 */
    private String details;

    /**
     * 构造方法
     *
     * @param errorEnum 错误枚举
     * @param details 错误详情
     * @param throwable 上级错误
     * @param message 错误消息
     */
    private BusinessException(IErrorEnum errorEnum, String details, Throwable throwable, String message) {
        super(message, throwable);
        this.errorEnum = errorEnum;
        this.details = details;
    }

    /**
     * 构造方法
     *
     * @param errorEnum 错误枚举
     * @param details 错误详情
     * @param message 错误消息
     */
    private BusinessException(IErrorEnum errorEnum, String details, String message) {
        super(message);
        this.errorEnum = errorEnum;
        this.details = details;
    }

    /**
     * 创建
     *
     * @param errorEnum 错误枚举
     * @param throwable 上级错误
     * @param details 错误详情
     */
    public static BusinessException create(IErrorEnum errorEnum, String details, Throwable throwable) {
        String message = genMessage(errorEnum, details);
        return new BusinessException(errorEnum, details, throwable, message);
    }

    /**
     * 创建
     *
     * @param errorEnum 错误枚举
     * @param throwable 上级错误
     */
    public static BusinessException create(IErrorEnum errorEnum, Throwable throwable) {
        String message = genMessage(errorEnum, "");
        return new BusinessException(errorEnum, "", throwable, message);
    }

    /**
     * 创建
     *
     * @param details 错误详情
     * @param throwable 上级错误
     */
    public static BusinessException create(String details, Throwable throwable) {
        String message = genMessage(CommErrorEnum.OTHER_ERROR, "");
        return new BusinessException(CommErrorEnum.OTHER_ERROR, details, throwable, message);
    }

    /**
     * 创建
     *
     * @param throwable 上级错误
     */
    public static BusinessException create(Throwable throwable) {
        String message = genMessage(CommErrorEnum.UNKNOWN_ERROR, "");
        return new BusinessException(CommErrorEnum.UNKNOWN_ERROR, "", throwable, message);
    }

    /**
     * 创建
     *
     * @param errorEnum 错误枚举
     * @param details 错误详情
     */
    public static BusinessException create(IErrorEnum errorEnum, String details) {
        String message = genMessage(errorEnum, details);
        return new BusinessException(errorEnum, details, message);
    }

    /**
     * 创建
     *
     * @param errorEnum 错误枚举
     */
    public static BusinessException create(IErrorEnum errorEnum) {
        String message = genMessage(errorEnum, "");
        return new BusinessException(errorEnum, "", message);
    }

    /**
     * 创建
     *
     * @param details 错误详情
     */
    public static BusinessException create(String details) {
        String message = genMessage(CommErrorEnum.OTHER_ERROR, "");
        return new BusinessException(CommErrorEnum.OTHER_ERROR, details, message);
    }

    /**
     * 创建
     */
    public static BusinessException create() {
        String message = genMessage(CommErrorEnum.UNKNOWN_ERROR, "");
        return new BusinessException(CommErrorEnum.UNKNOWN_ERROR, "", message);
    }

    /**
     * 构建错误消息
     *
     * @param errorEnum 错误枚举
     * @param details 错误详情
     */
    private static String genMessage(IErrorEnum errorEnum, String details) {
        Map<String, Object> errorMsg = new HashMap<>();
        errorMsg.put("code", errorEnum.getCode());
        errorMsg.put("desc", errorEnum.getDesc());
        errorMsg.put("details", details);
        return JSON.toJSONString(errorMsg);
    }

    /**
     * 获取：错误枚举
     *
     * @return 错误枚举
     */
    public IErrorEnum getErrorEnum() {
        return this.errorEnum;
    }

    /**
     * 设置：错误枚举
     *
     * @param errorEnum 错误枚举
     */
    public void setErrorEnum(IErrorEnum errorEnum) {
        this.errorEnum = errorEnum;
    }

    /**
     * 获取：错误详情
     *
     * @return 错误详情
     */
    public String getDetails() {
        return this.details;
    }

    /**
     * 设置：错误详情
     *
     * @param details 错误详情
     */
    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * 获取：串行版本标识
     *
     * @return 串行版本标识
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}