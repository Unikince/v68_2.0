package com.dmg.data.common.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.dmg.data.common.constant.NettyErrorEnum;
import com.dmg.data.common.message.NettyMsgException;

/**
 * 异步转同步
 */
public class SyncUtil {
    private static ConcurrentHashMap<String, SyncUtil> map = new ConcurrentHashMap<>();
    private CountDownLatch latch = new CountDownLatch(1);
    private String response;

    /**
     * 获取响应结果，直到有结果或者超过指定时间就返回
     *
     * @param unique 消息唯一code
     * @param overTime 超时时间(秒)
     * @return 数据
     */
    public static String get(String unique, int overTime) {
        try {
            SyncUtil sync = new SyncUtil();
            SyncUtil oldSync = map.putIfAbsent(unique, sync);
            if (oldSync != null) {
                sync = oldSync;
            }
            return sync.get(overTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new NettyMsgException(NettyErrorEnum.SYNC_WAIT_OVER, unique, e);
        } finally {
            map.remove(unique);
        }
    }

    /**
     * 获取响应结果，直到有结果或者超过5秒
     *
     * @param unique 消息唯一code
     * @return 数据
     */
    public static String get(String unique) {
        return get(unique, 5);
    }

    /**
     * 写入消息数据
     *
     * @param unique 消息唯一code
     * @param response 消息数据
     */
    public static void set(String unique, String response) {
        SyncUtil sync = new SyncUtil();
        SyncUtil oldSync = map.putIfAbsent(unique, sync);
        if (oldSync != null) {
            sync = oldSync;
        }
        sync.setResponse(response);
    }

    /**
     * 获取响应结果，直到有结果或者超过指定时间就返回
     *
     * @param timeout 超时时间
     * @param unit 超时时间单位
     * @return 响应结果
     */
    private String get(long timeout, TimeUnit unit) throws InterruptedException {
        if (this.latch.await(timeout, unit)) {
            return this.response;
        }
        return null;
    }

    /**
     * 用于设置响应结果，并且做countDown操作，通知请求线程
     *
     * @param response 响应结果
     */
    private void setResponse(String response) {
        this.response = response;
        this.latch.countDown();
    }

}