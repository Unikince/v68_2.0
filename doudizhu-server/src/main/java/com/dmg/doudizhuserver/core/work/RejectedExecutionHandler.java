package com.dmg.doudizhuserver.core.work;

public interface RejectedExecutionHandler {
    void rejectedExecution(Runnable r, Worker worker);
}
