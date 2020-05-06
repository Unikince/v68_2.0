package com.dmg.fish.core.net.socket;

import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullListAsEmpty;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullStringAsEmpty;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dmg.common.pb.java.Fish;
import com.google.protobuf.Message;
import com.googlecode.protobuf.format.JsonFormat;

import lombok.extern.slf4j.Slf4j;

/**
 * Socket服务,消息使用json传输
 */
@Slf4j
@Component
@ServerEndpoint(value = "/websocket/json/{playerId}")
public class SocketServerJson extends SocketServer {
    /**
     * 收到客户端消息
     *
     * @param msg 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String msg) {
        log.debug("收到客户端[{}]的消息[{}]", this.playerId, msg);
        JSONObject json = JSON.parseObject(msg);
        JSONObject data = json.getJSONObject("data");
        String msgId = json.getString("m");
        com.google.protobuf.Message body = null;
        try {
            if (data != null) {
                body = this.protobufParse(Integer.parseInt(msgId), data.toJSONString());
            }
        } catch (Exception e) {
            log.error("消息错误,消息["+msg+"]",e);
            return;
        }
        this.doMessage(msgId, body);
    }

    private com.google.protobuf.Message protobufParse(int msgId, String msg) throws Exception {
        com.google.protobuf.Message.Builder body = null;
        switch (msgId) {
            case Fish.FishMessageId.ReqRooms_ID_VALUE:
                body = Fish.ReqRooms.newBuilder();
                break;
            case Fish.FishMessageId.ReqEnterRoom_ID_VALUE:
                body = Fish.ReqEnterRoom.newBuilder();
                break;
            case Fish.FishMessageId.ReqFire_ID_VALUE:
                body = Fish.ReqFire.newBuilder();
                break;
            case Fish.FishMessageId.ReqHit_ID_VALUE:
                body = Fish.ReqHit.newBuilder();
                break;
            case Fish.FishMessageId.ReqPlusBattery_ID_VALUE:
                body = Fish.ReqPlusBattery.newBuilder();
                break;
            case Fish.FishMessageId.ReqMinusBattery_ID_VALUE:
                body = Fish.ReqMinusBattery.newBuilder();
                break;
            case Fish.FishMessageId.ReqExitRoom_ID_VALUE:
                body = Fish.ReqExitRoom.newBuilder();
                break;
            case Fish.FishMessageId.ReqRestore_ID_VALUE:
                body = Fish.ReqRestore.newBuilder();
                break;
            case Fish.FishMessageId.ReqLock_ID_VALUE:
                body = Fish.ReqLock.newBuilder();
                break;
            case Fish.FishMessageId.ReqCancelLock_ID_VALUE:
                body = Fish.ReqCancelLock.newBuilder();
                break;
            case Fish.FishMessageId.ReqCheckRoom_ID_VALUE:
                body = Fish.ReqCheckRoom.newBuilder();
                break;
            default:
                break;
        }
        if (body != null) {
            JsonFormat.merge(msg, body);
            return body.build();
        } else {
            return null;
        }

    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose() {
        if (SocketServer.socketMgr.remove(this.playerId, this)) {
            JSONObject json = new JSONObject();
            json.put("m", 998877);
            String jsonMsg = json.toJSONString();
            this.onMessage(jsonMsg);
            log.info("连接[{}]关闭,当前在线人数为[{}]", this.playerId, SocketServer.socketMgr.size());
        }
    }

    @Override
    public synchronized void pushMsg(int msgId, Message msg) {
        String data = JsonFormat.printToString(msg);
        JSONObject json = new JSONObject();
        json.put("m", msgId);
        json.put("data", JSON.parse(data));
        String jsonMsg = JSON.toJSONString(json, WriteNullStringAsEmpty, WriteNullListAsEmpty);
        try {
            synchronized(session){
                if (this.session.isOpen()) {
                    this.session.getBasicRemote().sendText(jsonMsg);
                }
            }
        } catch (IOException e) {
            log.error("消息推送错误,玩家[" + this.playerId + "],消息id[" + msgId + "]", e);
        }
    }
}
