package com.dmg.bairenzhajinhuaserver.manager;

import com.dmg.bairenzhajinhuaserver.common.utils.ClassUtils;
import com.dmg.bairenzhajinhuaserver.common.work.Cron;
import com.dmg.bairenzhajinhuaserver.common.work.Delay;
import com.dmg.bairenzhajinhuaserver.common.work.TimeWork;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Date: 2015年9月30日 下午5:14:50
 * @Author: zhuqd
 * @Description: 定时任务管理
 */
public class TimerManager {

	private static TimerManager instance = new TimerManager();
	private SchedulerFactory sf;
	private Scheduler sched;
	private AtomicLong identification = new AtomicLong();

	private static final Logger LOG = LoggerFactory.getLogger(TimerManager.class);

	private TimerManager() {
	}

	public static TimerManager instance() {
		return instance;
	}

	/**
	 * 初始化定时器
	 * 
	 *
	 * @throws SchedulerException
	 * @throws ClassNotFoundException
	 */
	public void init() throws SchedulerException, ClassNotFoundException {
		identification.set(System.currentTimeMillis());
		sf = new StdSchedulerFactory();
		sched = sf.getScheduler();
		sched.start();
		findTimer();
	}

	/**
	 * @throws SchedulerException
	 * @throws ClassNotFoundException
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void findTimer() throws SchedulerException, ClassNotFoundException {
		Collection<Class<?>> classes = ClassUtils.searchByAnnotation("com/dmg", Cron.class);
		for (Class<?> clazz : classes) {
			submit((Class<? extends TimeWork>) clazz);
			LOG.info("[TIMER]INIT[{}]", clazz.getName());
		}
	}

	/**
	 * 停止任务
	 * 
	 * @param id
	 * @throws SchedulerException
	 */
	public void stopTimeWork(long id) throws SchedulerException {
		JobKey key = new JobKey("job" + id);
		sched.deleteJob(key);
	}

	/**
	 * 暂停计时器
	 * 
	 * @param id
	 * @throws SchedulerException
	 */
	public void pauseTimeWork(long id) throws SchedulerException {
		JobKey key = new JobKey("job" + id);
		sched.pauseJob(key);

	}

	/**
	 * 唤醒暂停的计时器
	 * 
	 * @param id
	 * @throws SchedulerException
	 */
	public void resumeTimeWork(long id) throws SchedulerException {
		JobKey key = new JobKey("job" + id);
		sched.resumeJob(key);
	}

	/**
	 * 提交一个定时任务
	 * 
	 * @param clazz
	 * @param args
	 * @throws SchedulerException
	 * @return id 任务id
	 */
	public long submit(Class<? extends TimeWork> clazz, Object... args) throws SchedulerException {
		Cron cron = clazz.getAnnotation(Cron.class);
		if (cron == null) {
			throw new RuntimeException("timer job has no Cron annotation");
		}
		String exp = cron.value();
		if (StringUtils.isEmpty(exp)) {
			throw new RuntimeException("timer job Cron expression is null");
		}
		long id = identification.incrementAndGet();
		JobDetail job = JobBuilder.newJob(clazz).withIdentity("job" + id).build();
		job.getJobDataMap().put("args", args);
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger" + id).startNow()
				.withSchedule(CronScheduleBuilder.cronSchedule(exp)).build();
		sched.scheduleJob(job, trigger);
		return id;
	}

	/**
	 * 提交任务
	 * 
	 * @param timeWork
	 * @param args
	 * @throws SchedulerException
	 */
	public long submit(TimeWork timeWork, Object... args) throws SchedulerException {
		return submit(timeWork.getClass(), args);
	}

	/**
	 * 提交延时任务
	 * 
	 * @param args
	 * @throws SchedulerException
	 * @return id 任务id
	 */
	public long submitRepeatWork(Class<? extends TimeWork> clazz, Object... args) throws SchedulerException {
		Delay delayAnno = clazz.getAnnotation(Delay.class);
		int delay = delayAnno.delay();
		int loop = delayAnno.loop();
		int interval = delayAnno.intreval();
		long id = identification.incrementAndGet();
		//
		JobDetail job = JobBuilder.newJob(clazz).withIdentity("job" + id).build();
		job.getJobDataMap().put("args", args);
		Trigger trigger = null;
		if (delay == 0) {
			trigger = TriggerBuilder.newTrigger().withIdentity("trigger" + id).startNow().withSchedule(
					SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(interval).withRepeatCount(loop))
					.build();
		} else {
			Date startDate = new Date(System.currentTimeMillis() + delay);
			trigger = TriggerBuilder.newTrigger().withIdentity("trigger" + id).startAt(startDate).withSchedule(
					SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(interval).withRepeatCount(loop))
					.build();
		}
		sched.scheduleJob(job, trigger);
		return id;
	}

	/**
	 * 提交延时任务
	 * 
	 * @param clazz
	 * @param delay
	 * --毫秒
	 * @param args
	 * @throws SchedulerException
	 * @return id 任务id
	 */
	public long submitDelayWork(Class<? extends TimeWork> clazz, int delay, Object... args) throws SchedulerException {
		long id = identification.incrementAndGet();
		JobDetail job = JobBuilder.newJob(clazz).withIdentity("job" + id).build();
		job.getJobDataMap().put("args", args);
		Date start = new Date(System.currentTimeMillis() + delay);
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger" + id).startAt(start).build();
		sched.scheduleJob(job, trigger);
		return id;
	}

	/**
	 * 提延时任务
	 * 
	 * @param clazz
	 * @param delay
	 *            延迟delay毫秒开始后执行第一次任务
	 * @param interval
	 *            任务间隔
	 * @param loop
	 *            任务执行次数
	 * @param args
	 * @throws SchedulerException
	 * @return id 任务id
	 */
	public long submitDelayLoopWork(Class<? extends TimeWork> clazz, int delay, int interval, int loop, Object... args)
			throws SchedulerException {
		long id = identification.incrementAndGet();
		JobDetail job = JobBuilder.newJob(clazz).withIdentity("job" + id).build();
		job.getJobDataMap().put("args", args);
		Trigger trigger = null;
		if (delay == 0) {
			trigger = TriggerBuilder.newTrigger().withIdentity("trigger" + id).startNow().withSchedule(
					SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(interval).withRepeatCount(loop))
					.build();
		} else {
			Date startDate = new Date(System.currentTimeMillis() + delay);
			trigger = TriggerBuilder.newTrigger().withIdentity("trigger" + id).startAt(startDate).withSchedule(
					SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(interval).withRepeatCount(loop))
					.build();
		}
		sched.scheduleJob(job, trigger);
		return id;
	}
}
