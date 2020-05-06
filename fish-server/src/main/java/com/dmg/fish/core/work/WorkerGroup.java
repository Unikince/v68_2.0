package com.dmg.fish.core.work;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class WorkerGroup {
    private static int DEFAULT_SIZE = Runtime.getRuntime().availableProcessors() + 1;
    private static int groupIndex;
    private List<Worker> list = new ArrayList<>();
    private int index;
    private int size;

    public WorkerGroup(String name, int size) {
        this.size = size;
        for (int i = 0; i < size; i++) {
            this.list.add(new Worker("Thread_" + groupIndex + "_" + name + "_" + i));
        }
        groupIndex++;
    }

    public WorkerGroup(String name) {
        this(name, DEFAULT_SIZE);
    }

    public WorkerGroup(int size) {
        this("DEFAULT", size);
    }

    public WorkerGroup() {
        this("DEFAULT", DEFAULT_SIZE);
    }

    public synchronized Worker getWork() {
        Worker work = this.list.get(this.index);
        this.index = this.index + 1;
        if (this.index == this.size) {
            this.index = 0;
        }
        return work;
    }

    public void execute(Runnable command) {
        this.getWork().execute(command);
    }

    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return this.getWork().schedule(command, delay, unit);
    }

    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        return this.getWork().schedule(callable, delay, unit);
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return this.getWork().scheduleAtFixedRate(command, initialDelay, period, unit);
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return this.getWork().scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }

    public void shutdown() {
        for (Worker twork : this.list) {
            twork.shutdown();
        }
    }
}
