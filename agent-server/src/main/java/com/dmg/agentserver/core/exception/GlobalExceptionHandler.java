package com.dmg.agentserver.core.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.dmg.agentserviceapi.core.error.ErrorResult;
import com.dmg.agentserviceapi.core.error.ResultAgent;

import lombok.extern.log4j.Log4j2;

/**
 * 全局异常处理
 */
@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Throwable.class)
    public ResultAgent<Void> customExceptionHandler(HttpServletRequest request, HttpServletResponse response, final Exception ex) {
        BusinessException be = null;
        if (ex instanceof BusinessException) {
            be = (BusinessException) ex;
        } else {
            be = BusinessException.create(ex);
        }
        log.error("", be);
        IErrorEnum errorEnum = be.getErrorEnum();
        String details = be.getDetails();
        ErrorResult errorResult = new ErrorResult(errorEnum.getCode(), errorEnum.getDesc(), details);
        ResultAgent<Void> result = new ResultAgent<>();
        result.setError(errorResult);
        return result;
    }
}