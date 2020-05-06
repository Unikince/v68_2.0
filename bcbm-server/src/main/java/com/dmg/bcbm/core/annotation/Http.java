package com.dmg.bcbm.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Date: 2015年11月19日 下午5:16:02
 * @Author: zhuqd
 * @Description: Http请求消息
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Http {

	/**
	 * 返回路径
	 * 
	 * @return
	 */
	String value();
}
