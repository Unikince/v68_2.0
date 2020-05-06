package com.zyhy.lhj_server.manager.timerWork.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Date: 2015年9月30日 下午5:07:17
 * @Author: zhuqd
 * @Description:corn表达式,配置定时任务
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Cron {
	/**
	 * corn表达式
	 * 
	 * @return
	 */
	String value();
}
