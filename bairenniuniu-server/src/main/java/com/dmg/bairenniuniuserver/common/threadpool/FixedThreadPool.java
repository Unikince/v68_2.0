package com.dmg.bairenniuniuserver.common.threadpool;

import java.util.concurrent.*;

/**
 * @Description
 * @Author mice
 * @Date 2020/2/26 10:30
 * @Version V1.0
 **/
public class FixedThreadPool {

    private static final ExecutorService executorService = new ThreadPoolExecutor(1,
            1,
            0,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1024,false));

    public static void submit(Runnable task){
        executorService.submit(task);
    }
}