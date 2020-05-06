package com.dmg.bjlserver.core.msg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmg.bjlserver.business.robot.RobotMgr;
import com.dmg.bjlserver.core.net.socket.SocketMgr;
import com.dmg.bjlserver.core.net.socket.SocketServer;
import com.google.protobuf.Message;

/**
 * 消息推送
 */
@Service
public class MessagePush {
    /** Socket服务管理 */
    @Autowired
    private SocketMgr socketMgr;

    public void writeMsg(Long playerId, int msgId, Message msg) {
        RobotMgr.getInstance().putMessage(playerId, msgId, msg);
        SocketServer socket = this.socketMgr.getSocket(playerId);
        if (socket == null) {
            return;
        }
        if ((playerId != 0) && (msg != null)) {
            socket.pushMsg0(msgId, msg);
        }
    }
}
