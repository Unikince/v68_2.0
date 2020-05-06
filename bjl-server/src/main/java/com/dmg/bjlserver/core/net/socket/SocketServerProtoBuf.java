package com.dmg.bjlserver.core.net.socket;

import java.io.IOException;
import java.nio.ByteBuffer;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import com.dmg.common.pb.java.Bjl;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;

/**
 * Socket服务,消息使用protobuf传输
 */
@Slf4j
@Component
@ServerEndpoint(value = "/websocket/{playerId}")
public class SocketServerProtoBuf extends SocketServer {
    /**
     * 收到客户端消息
     *
     * @param msg 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(ByteBuffer msg) {
        log.debug("收到客户端[{}]的消息", this.playerId);
        int msgId = msg.getInt();

        com.google.protobuf.Message body = null;
        try {
            body = this.protobufParse(msgId, msg);
        } catch (InvalidProtocolBufferException e) {
            log.error("消息错误,ID["+msgId+"]", e);
            return;
        }
        this.doMessage("" + msgId, body);
    }

    private com.google.protobuf.Message protobufParse(int msgId, ByteBuffer msg) throws InvalidProtocolBufferException {
        com.google.protobuf.Message body = null;
        switch (msgId) {
            case Bjl.BjlMessageId.ReqBet_ID_VALUE:
                body = Bjl.ReqBet.parseFrom(msg);
                break;
            case Bjl.BjlMessageId.ReqEnterRoom_ID_VALUE:
                body = Bjl.ReqEnterRoom.parseFrom(msg);
                break;
            case Bjl.BjlMessageId.ReqExitRoom_ID_VALUE:
                body = Bjl.ReqExitRoom.parseFrom(msg);
                break;
            case Bjl.BjlMessageId.ReqOtherPlayers_ID_VALUE:
                body = Bjl.ReqOtherPlayers.parseFrom(msg);
                break;
            case Bjl.BjlMessageId.ReqPlayerContinueBet_ID_VALUE:
                body = Bjl.ReqPlayerContinueBet.parseFrom(msg);
                break;
            case Bjl.BjlMessageId.ReqProfitList_ID_VALUE:
                body = Bjl.ReqProfitList.parseFrom(msg);
                break;
            case Bjl.BjlMessageId.ReqRevokeBet_ID_VALUE:
                body = Bjl.ReqRevokeBet.parseFrom(msg);
                break;
            case Bjl.BjlMessageId.ReqRooms_ID_VALUE:
                body = Bjl.ReqRooms.parseFrom(msg);
                break;
            case Bjl.BjlMessageId.ReqSeatDetailed_ID_VALUE:
                body = Bjl.ReqSeatDetailed.parseFrom(msg);
                break;
            case Bjl.BjlMessageId.ResShuffleInfo_ID_VALUE:
                body = Bjl.ResShuffleInfo.parseFrom(msg);
                break;
            default:
                break;
        }
        return body;
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose() {
        if (SocketServer.socketMgr.remove(this.playerId, this)) {
			ByteBuf byteBuf = Unpooled.buffer(4);
            byteBuf.writeInt(998877);
            this.onMessage(byteBuf.nioBuffer());
            log.info("连接[{}]关闭,当前在线人数为[{}]", this.playerId, SocketServer.socketMgr.size());
        }
    }

    @Override
    public synchronized void pushMsg(int msgId, Message msg) {
        // 消息体的字节缓冲（默认大小为256字节）
        ByteBuf byteBuf = Unpooled.buffer();
        // 消息id
        byteBuf.writeInt(msgId);
        // 消息体
        byteBuf.writeBytes(msg.toByteArray());

        try {
            synchronized(session){
                if (this.session.isOpen()) {
                    this.session.getBasicRemote().sendBinary(byteBuf.nioBuffer());
                }
            }
        } catch (IOException e) {
            log.error("消息推送错误,玩家[" + this.playerId + "],消息id[" + msgId + "]", e);
        }
    }
}
