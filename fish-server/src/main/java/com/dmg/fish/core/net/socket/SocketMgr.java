package com.dmg.fish.core.net.socket;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

/**
 * Socket服务管理
 */
@Service
public class SocketMgr {
    /** Socket服务集合 */
    private ConcurrentHashMap<String, SocketServer> socketMap = new ConcurrentHashMap<>();
    private List<ScheduledExecutorService> threads;
    private int index;
    private int size;

    @PostConstruct
    public void init() {
        this.threads = new ArrayList<>();
        this.size = Runtime.getRuntime().availableProcessors() * 4;
        for (int i = 0; i < this.size; i++) {
            this.threads.add(Executors.newSingleThreadScheduledExecutor());
        }
    }

    private synchronized ScheduledExecutorService getThread() {
        ScheduledExecutorService thread = this.threads.get(this.index);
        this.index = this.index + 1;
        if (this.index == this.size) {
            this.index = 0;
        }
        return thread;
    }

    /**
     * 获取所有socket服务
     *
     * @return socket服务集合
     */
    public List<SocketServer> getAllSocket() {
        List<SocketServer> list = new ArrayList<>(this.socketMap.values().size());
        list.addAll(this.socketMap.values());
        return list;
    }

    /**
     * 获取socket服务
     *
     * @param appId 玩家id
     * @return socket服务
     */
    public SocketServer getSocket(long appId) {
        return this.socketMap.get(String.valueOf(appId));
    }

    /**
     * 返回socket服务数量
     *
     * @return socket服务数量
     */
    public int size() {
        return this.socketMap.size();
    }

    /**
     * 添加socket服务
     *
     * @param appId 玩家id
     * @param socket socket服务
     */
    protected ScheduledExecutorService add(String appId, SocketServer socket) {
        SocketServer old = this.socketMap.remove(appId);
        if (old != null) {
            old.closeSession();
        }
        this.socketMap.put(appId, socket);
        return this.getThread();
    }

    /**
     * 移除socket服务
     *
     * @param appId 玩家id
     * @param socket 被移除的socket服务
     * @return 是否被移除
     */
    protected boolean remove(String appId, SocketServer socket) {
        if (this.socketMap.get(appId) == socket) {
            this.socketMap.remove(appId);
            return true;
        }
        return false;
    }
}
