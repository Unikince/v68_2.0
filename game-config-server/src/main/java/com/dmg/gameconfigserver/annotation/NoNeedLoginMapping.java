package com.dmg.gameconfigserver.annotation;

import java.lang.annotation.*;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 17:12 2019/11/6
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoNeedLoginMapping {
}
