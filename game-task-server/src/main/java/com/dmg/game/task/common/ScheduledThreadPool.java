package com.dmg.game.task.common;

import java.util.concurrent.*;

/**
 * @Author liubo
 * @Description 线程池获取 //TODO
 * @Date 14:40 2020/3/13
 */
public class ScheduledThreadPool {

    private static volatile ScheduledThreadPool instance;

    private static ExecutorService executorService;

    private ScheduledThreadPool(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public static ScheduledThreadPool getInstance() {
        return getInstance(10);
    }

    public static ScheduledThreadPool getInstance(int nThreads) {
        if (null == instance) {
            synchronized (ScheduledThreadPool.class) {
                if (null == instance) {
                    instance = new ScheduledThreadPool(getExecutorService(nThreads));
                }
            }
        }
        return instance;
    }

    private static synchronized void syncInit(int nThreads) {
        if (executorService == null) {
            executorService = new ScheduledThreadPoolExecutor(nThreads);
        }
    }

    private static ExecutorService getExecutorService(int nThreads) {
        if (executorService == null) {
            syncInit(nThreads);
        }
        return executorService;
    }

    public void submit(Runnable task) {
        this.executorService.submit(task);
    }

    public ExecutorService getExecutorService() {
        return this.executorService;
    }

}
