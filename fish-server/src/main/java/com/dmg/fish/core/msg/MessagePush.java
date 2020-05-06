package com.dmg.fish.core.msg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmg.fish.business.platform.service.PlayerService;
import com.dmg.fish.business.robot.RobotNetUtil;
import com.dmg.fish.core.net.socket.SocketMgr;
import com.dmg.fish.core.net.socket.SocketServer;
import com.google.protobuf.Message;

/**
 * 消息推送
 */
@Service
public class MessagePush {
    /** Socket服务管理 */
    @Autowired
    private SocketMgr socketMgr;
    @Autowired
    private RobotNetUtil robotNetUtil;

    public void writeMsg(Long playerId, int msgId, Message msg) {
        SocketServer socket = this.socketMgr.getSocket(playerId);
        if (socket != null) {
            if ((playerId != 0) && (msg != null)) {
                socket.pushMsg0(msgId, msg);
            }
        } else {
            if (playerId > PlayerService.ROBOT_ID_PREFIX) {
                this.robotNetUtil.recv(playerId, msgId, msg);
            }
        }
    }
}
