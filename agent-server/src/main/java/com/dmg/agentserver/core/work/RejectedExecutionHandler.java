package com.dmg.agentserver.core.work;

public interface RejectedExecutionHandler {
    void rejectedExecution(Runnable r, Worker worker);
}
