package com.dmg.fish.core.work;

public interface RejectedExecutionHandler {
    void rejectedExecution(Runnable r, Worker worker);
}
