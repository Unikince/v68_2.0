/**
 * 
 */
package com.dmg.bcbm;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @author lnj
 *
 */
@Component
@Lazy(false)
public class SpringContextUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext;
 	private static boolean isLocal = false;	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		SpringContextUtil.applicationContext = applicationContext;
		 setActiveProfile();
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static Object getBean(String name) {
		return getApplicationContext().getBean(name);
	}

	public static <T> T getBean(Class<T> clazz) {
		return getApplicationContext().getBean(clazz);
	}

	public static <T> T getBean(String name, Class<T> clazz) {
		return getApplicationContext().getBean(name, clazz);
	}
	
	 /// 获取当前环境
    private static void setActiveProfile() {
    	String profile = getApplicationContext().getEnvironment().getActiveProfiles()[0];
    	if(profile.indexOf("local") != -1){
    		isLocal = true;
    	}
    }
    
    public static boolean isLocal(){
    	return isLocal;
    }
}
