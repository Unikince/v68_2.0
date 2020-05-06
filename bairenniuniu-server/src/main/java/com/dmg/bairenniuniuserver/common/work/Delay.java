package com.dmg.bairenniuniuserver.common.work;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Date: 2015年11月3日 上午10:20:49
 * @Author: zhuqd
 * @Description: 延时处理任务
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Delay {
	/**
	 * 延时(ms)
	 * 
	 * @return
	 */
	int delay();

	/**
	 * 执行次数
	 * 
	 * @return
	 */
	int loop();

	/**
	 * 间隔时间
	 * 
	 * @return
	 */
	int intreval();
}
