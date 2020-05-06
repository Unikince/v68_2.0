package com.dmg.doudizhuserver.core.net.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: ChenHao
 * @Date: 2018/11/27 17:40
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 业务异常
     *
     * @param e 参数非法异常对象
     * @return 处理响应结果
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Object> businessException(BusinessException e) {
        log.error("==>业务异常{}", e.getMessage(), e);
        return new Result<>(e.getCode(), e.getMessage(), null);
    }
}