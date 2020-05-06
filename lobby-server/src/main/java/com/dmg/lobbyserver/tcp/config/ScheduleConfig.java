/**
 *  注意：本内容仅限于大拇哥互娱科技有限公司内部传阅，禁止外泄以及用于其他的商业目 
 */
package com.dmg.lobbyserver.tcp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executors;

/**
 * @className:--ScheduleConfig
 * @author:-----Vito
 * @date:-------2019年3月18日 下午5:35:36
 * @version:----1.0
 * @Description:定时器配置(5个线程聚合推送)
 */
@Configuration
public class ScheduleConfig implements SchedulingConfigurer {
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setScheduler(Executors.newScheduledThreadPool(5));
	}
}
