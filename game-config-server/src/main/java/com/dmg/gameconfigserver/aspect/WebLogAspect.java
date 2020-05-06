package com.dmg.gameconfigserver.aspect;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @Author liubo
 * @Description //TODO 打印日志
 * @Date 16:27 2019/11/25
 */
@Slf4j
@Aspect
@Component
public class WebLogAspect {

    @Pointcut("execution(public * com.dmg.gameconfigserver.controller..*.*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        log.info("{} , {} : req data : {}", method.getDeclaringClass().getName(), method.getName(),
                (joinPoint == null || joinPoint.getArgs() == null || joinPoint.getArgs().length < 1) ? null : new Gson().toJson(joinPoint.getArgs()[0]));
    }

    @AfterReturning(returning = "response", pointcut = "webLog()")
    public void doAfterReturning(JoinPoint joinPoint, Object response) {
        if (response != null) {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            log.info("{} , {} : resp data : {}", method.getDeclaringClass().getName(), method.getName(), new Gson().toJson(response));
        }
    }
}
