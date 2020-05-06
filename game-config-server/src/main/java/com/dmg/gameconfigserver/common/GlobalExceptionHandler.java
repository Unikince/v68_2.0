package com.dmg.gameconfigserver.common;

import com.dmg.common.core.web.BusinessException;
import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

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
    public Result businessException(BusinessException e) {
        log.error("==>业务异常{}", e.getMessage(), e);
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理参数绑定异常
     *
     * @return 处理响应结果
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    private Result methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("==>参数异常{}", ex.getMessage(), ex);
        StringBuilder sb = new StringBuilder();
        List<FieldError> errorList = ex.getBindingResult().getFieldErrors();
        for (FieldError error : errorList) {
            sb.append(error.getDefaultMessage()+",");
        }
        return Result.error(ResultEnum.PARAM_ERROR.getCode()+"",sb.substring(0,sb.length()-1));
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.OK)
    private Result bindException(BindException ex) {
        log.error("==>参数异常{}", ex.getMessage(), ex);
        StringBuilder sb = new StringBuilder();
        List<FieldError> errorList = ex.getBindingResult().getFieldErrors();
        for (FieldError error : errorList) {
            sb.append(error.getDefaultMessage()+",");
        }
        return Result.error(ResultEnum.PARAM_ERROR.getCode()+"",sb.substring(0,sb.length()-1));
    }
}