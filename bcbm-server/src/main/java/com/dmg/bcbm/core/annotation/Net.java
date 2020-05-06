package com.dmg.bcbm.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Date: 2015年10月7日 上午10:51:59
 * @Author: zhuqd
 * @Description: websocket的网络消息
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Net {

	/**
	 * 消息号
	 * 
	 * @return
	 */
	String value();
}
