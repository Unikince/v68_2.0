package com.dmg.gameconfigserver.annotation;

import java.lang.annotation.*;

/**
 * @Author liubo
 * @Description //TODO 注解作用于类和字段上
 * @Date 16:12 2020/1/8
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface ColumnName {

    /**
     * @return 名称
     */
    String name() default "";
}
