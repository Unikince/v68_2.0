package com.dmg.bjlserver.core.work;

public interface RejectedExecutionHandler {
    void rejectedExecution(Runnable r, Worker worker);
}
