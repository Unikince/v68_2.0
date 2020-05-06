package com.dmg.doudizhuserver.core.msg;

import static com.alibaba.fastjson.serializer.SerializerFeature.DisableCircularReferenceDetect;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullListAsEmpty;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullStringAsEmpty;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dmg.doudizhuserver.core.net.socket.SocketMgr;
import com.dmg.doudizhuserver.core.net.socket.SocketServer;

/**
 * 消息推送
 */
@Service
public class MessagePush {
    /** Socket服务管理 */
    @Autowired
    private SocketMgr socketMgr;

    /**
     * 推送消息,消息数据为空
     *
     * @param appId 玩家id
     * @param m 协议号
     * @param res 消息结果
     */
    public void pushMsg(long appId, String m, int res) {
        this.pushMsg(appId, m, res, null);
    }

    /**
     * 推送消息,消息结果为0
     *
     * @param appId 玩家id
     * @param m 协议号
     * @param msg 消息数据
     */
    public void pushMsg(long appId, String m, Object msg) {
        this.pushMsg(appId, m, 0, msg);
    }

    /**
     * 推送消息
     *
     * @param appId 玩家id
     * @param m 协议号
     * @param res 消息结果
     * @param msg 消息数据
     */
    public void pushMsg(long appId, String m, int res, Object msg) {
        SocketServer socket = this.socketMgr.getSocket(appId);
        if (socket == null) {
            return;
        }
        MessageResult ressageResult = new MessageResult(m, res, msg);
        String pushData = JSON.toJSONString(ressageResult, WriteNullStringAsEmpty, WriteNullListAsEmpty, DisableCircularReferenceDetect);
        socket.pushMsg(pushData);
    }

    /**
     * 推送消息,消息数据为空
     *
     * @param appIds 玩家id集合
     * @param m 协议号
     * @param res 消息结果
     */
    public void pushMsg(Set<Long> appIds, String m, int res) {
        if (appIds == null || appIds.size() == 0) {
            return;
        }
        this.pushMsg(appIds, m, res, null);
    }

    /**
     * 推送消息,消息数据为空
     *
     * @param appIds 玩家id集合
     * @param m 协议号
     * @param res 消息结果
     */
    public void pushMsg(List<Long> appIds, String m, int res) {
        if (appIds == null || appIds.size() == 0) {
            return;
        }
        this.pushMsg(appIds, m, res, null);
    }

    /**
     * 推送消息,消息数据为空
     *
     * @param appIds 玩家id集合
     * @param m 协议号
     * @param res 消息结果
     */
    public void pushMsg(long[] appIds, String m, int res) {
        if (appIds == null || appIds.length == 0) {
            return;
        }
        this.pushMsg(appIds, m, res, null);
    }

    /**
     * 推送消息,消息数据为空
     *
     * @param appIds 玩家id集合
     * @param m 协议号
     * @param res 消息结果
     */
    public void pushMsg(Long[] appIds, String m, int res) {
        if (appIds == null || appIds.length == 0) {
            return;
        }
        this.pushMsg(appIds, m, res, null);
    }

    /**
     * 推送消息,消息结果为0
     *
     * @param appIds 玩家id集合
     * @param m 协议号
     * @param msg 消息数据
     */
    public void pushMsg(Set<Long> appIds, String m, Object msg) {
        if (appIds == null || appIds.size() == 0) {
            return;
        }
        this.pushMsg(appIds, m, 0, msg);
    }

    /**
     * 推送消息,消息结果为0
     *
     * @param appIds 玩家id集合
     * @param m 协议号
     * @param msg 消息数据
     */
    public void pushMsg(List<Long> appIds, String m, Object msg) {
        if (appIds == null || appIds.size() == 0) {
            return;
        }
        this.pushMsg(appIds, m, 0, msg);
    }

    /**
     * 推送消息,消息结果为0
     *
     * @param appIds 玩家id集合
     * @param m 协议号
     * @param msg 消息数据
     */
    public void pushMsg(long[] appIds, String m, Object msg) {
        if (appIds == null || appIds.length == 0) {
            return;
        }
        this.pushMsg(appIds, m, 0, msg);
    }

    /**
     * 推送消息,消息结果为0
     *
     * @param appIds 玩家id集合
     * @param m 协议号
     * @param msg 消息数据
     */
    public void pushMsg(Long[] appIds, String m, Object msg) {
        if (appIds == null || appIds.length == 0) {
            return;
        }
        this.pushMsg(appIds, m, 0, msg);
    }

    /**
     * 推送消息
     *
     * @param appIds 玩家id集合
     * @param m 协议号
     * @param res 消息结果
     * @param msg 消息数据
     */
    public void pushMsg(Set<Long> appIds, String m, int res, Object msg) {
        if (appIds == null || appIds.size() == 0) {
            return;
        }
        this.pushMsg(appIds.toArray(new Long[appIds.size()]), m, res, msg);
    }

    /**
     * 推送消息
     *
     * @param appIds 玩家id集合
     * @param m 协议号
     * @param res 消息结果
     * @param msg 消息数据
     */
    public void pushMsg(List<Long> appIds, String m, int res, Object msg) {
        if (appIds == null || appIds.size() == 0) {
            return;
        }
        this.pushMsg(appIds.toArray(new Long[appIds.size()]), m, res, msg);
    }

    /**
     * 推送消息
     *
     * @param appIds 玩家id集合
     * @param m 协议号
     * @param res 消息结果
     * @param msg 消息数据
     */
    public void pushMsg(long[] appIds, String m, int res, Object msg) {
        if (appIds == null || appIds.length == 0) {
            return;
        }
        Long[] appIds2 = new Long[appIds.length];
        for (int i = 0; i < appIds.length; i++) {
            appIds2[i] = appIds[i];
        }
        this.pushMsg(appIds2, m, res, msg);
    }

    /**
     * 推送消息
     *
     * @param appIds 玩家id集合
     * @param m 协议号
     * @param res 消息结果
     * @param msg 消息数据
     */
    public void pushMsg(Long[] appIds, String m, int res, Object msg) {
        if (appIds == null || appIds.length == 0) {
            return;
        }
        MessageResult ressageResult = new MessageResult(m, res, msg);
        String pushData = JSON.toJSONString(ressageResult, WriteNullStringAsEmpty, WriteNullListAsEmpty, DisableCircularReferenceDetect);
        for (Long appId : appIds) {
            if (appId == null) {
                continue;
            }
            SocketServer socket = this.socketMgr.getSocket(appId);
            if (socket == null) {
                continue;
            }
            socket.pushMsg(pushData);
        }
    }
}
