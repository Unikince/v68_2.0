/**
 * 
 */
package com.zyhy.common_server.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.zyhy.common_server.result.ErrorResponseResult;

/**
 * @author Administrator
 * 全局异常处理
 */
public class ZgqpGameExceptionHandler extends ResponseEntityExceptionHandler{

	/**
     * 定义要捕获的异常 可以多个 @ExceptionHandler({})
     * @param request  request
     * @param e        exception
     * @param response response
     * @return 响应结果
     */
    @ExceptionHandler(ZgqpGameException.class)
    public ErrorResponseResult zgqpGameExceptionHandler(HttpServletRequest request, final Exception e, HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        ZgqpGameException exception = (ZgqpGameException) e;
        return new ErrorResponseResult(exception.getRet(), exception.getMessage());
    }

    /**
     * 捕获  RuntimeException 异常
     * @param request  request
     * @param e        exception
     * @param response response
     * @return 响应结果
     */
    @ExceptionHandler(RuntimeException.class)
    public ErrorResponseResult runtimeExceptionHandler(HttpServletRequest request, final Exception e, HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        RuntimeException exception = (RuntimeException) e;
        return new ErrorResponseResult(400, exception.getMessage());
    }
}
