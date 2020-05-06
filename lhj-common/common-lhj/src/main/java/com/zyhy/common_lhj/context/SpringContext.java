/**
 * 
 */
package com.zyhy.common_lhj.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author ASUS
 *
 */
@Component
public class SpringContext implements ApplicationContextAware {
 
    private static ApplicationContext context = null;
    
    private static boolean isLocal = false;
 
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        context = applicationContext;
        setActiveProfile();
    }
 
 
 
    /// 获取当前环境
    private static void setActiveProfile() {
    	String profile = context.getEnvironment().getActiveProfiles()[0];
    	if(profile.indexOf("local") != -1){
    		isLocal = true;
    	}
    }
    
    public static boolean isLocal(){
    	return isLocal;
    }
}