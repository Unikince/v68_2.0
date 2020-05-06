package com.dmg.redblackwarserver.common.exception;

import com.dmg.redblackwarserver.common.result.Result;
import com.dmg.redblackwarserver.common.result.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
    public Result<String> businessException(BusinessException e) {
        log.error("==>业务异常{}", e.getMessage(), e);
        return new Result<String>(e.getCode(), e.getMessage());
    }

    /**
     * 处理参数绑定异常
     *
     * @return 处理响应结果
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    private Result<?> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("==>参数异常{}", ex.getMessage(), ex);
        return Result.error(ResultEnum.PARAM_ERROR.getCode()+"",ResultEnum.PARAM_ERROR.getMsg());
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.OK)
    private Result<?> bindException(BindException ex) {
        log.error("==>参数异常{}", ex.getMessage(), ex);
        return Result.error(ResultEnum.PARAM_ERROR.getCode()+"",ResultEnum.PARAM_ERROR.getMsg());
    }
}