package com.dmg.bcbm.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Date: 2015年10月14日 下午6:05:46
 * @Author: zhuqd
 * @Description: RMI标识
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RMI {

	/**
	 * rmi的注册路径
	 * 
	 * @return
	 */
	String value();
}
