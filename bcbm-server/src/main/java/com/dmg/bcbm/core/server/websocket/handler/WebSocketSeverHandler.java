package com.dmg.bcbm.core.server.websocket.handler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dmg.bcbm.core.abs.message.MessageNet;
import com.dmg.bcbm.core.config.D;
import com.dmg.bcbm.core.manager.DefFactory;
import com.dmg.bcbm.core.manager.NetManager;
import com.dmg.bcbm.core.manager.RoleManager;
import com.dmg.bcbm.core.manager.RoomManager;
import com.dmg.bcbm.core.manager.WorkManager;
import com.dmg.bcbm.logic.def.DefType;
import com.dmg.bcbm.logic.def.ServerDef;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import util.ByteHelper;
import util.json.JsonUtil;

/**
 * @author zhuqd
 * @Date 2017年7月18日
 */
public class WebSocketSeverHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(WebSocketSeverHandler.class);
    private WebSocketServerHandshaker shaker;
    private int port;
    private String host;

    public WebSocketSeverHandler(int port, String host) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	//logger.info("channelRead message : " + msg);
        // 判断是否是传统的Http请求
        if (msg instanceof FullHttpRequest) {
            // 消息为http请求，第一次为握手
            shaker = HttpHandler.handler(ctx, (FullHttpRequest) msg, shaker, port, host);
            if (shaker == null) {
                logger.error("client try connect server fail: Handshaker fail");
            }
        } else if (msg instanceof WebSocketFrame) {
        	// 如果是socket消息就直接读取
            readMessage(ctx, (WebSocketFrame) msg);
        } else {
            logger.error("message is out of type: {}", msg);
        }

    }

    /**
     * 接收客户端消息
     *
     * @param ctx
     * @param msg
     */
    private void readMessage(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
    	System.out.println("接收到的socket消息" + msg);
    	if (msg instanceof CloseWebSocketFrame) {
    		 logger.warn("客户端请求关闭连接!");
    		channelUnregistered(ctx);
            return;
        }
        // 判断是否是ping值
        if (msg instanceof PingWebSocketFrame) {
            return;
        }
        if (!(msg instanceof TextWebSocketFrame)) {
            return;
        }
        
        // 接收到的消息
        String message = ((TextWebSocketFrame) msg).text();
        // 检查客户端消息发送频率
        boolean check = checkMsgPackageNum(ctx);
        if (!check) {
            // 客户端发送消息过于频繁，服务器主动断开服务器连接
            ctx.writeAndFlush(ByteHelper.createFrameMessage(JsonUtil.create().cmd("506").setForward(D.HALL_NEED_FORWARD).errorCode("506").toJsonString()));
            logger.error("MESSAGE ERROR [506]. message package is too many.ctx:{},mseeage:{}", ctx.channel(), message);
            return;
        }
        JSONObject json = null;
        try {
            json = JSON.parseObject(message);
            logger.info("socket接收到的消息: {}", json);
        } catch (Exception e) {
            ctx.writeAndFlush(
                    ByteHelper.createFrameMessage(JsonUtil.create().cmd("501").setForward(D.HALL_NEED_FORWARD).errorCode("501").toJsonString()));
            logger.error("MESSAGE ERROR [501] .message parse error");
            return;
        }
        if (json == null || json.getString("cmd") == null) {
            logger.error(" MESSAGE ERROR [502]. message data error.message is null or cmd is null .ctx:{},mseeage:{}",
                    ctx.channel(), message);
            ctx.writeAndFlush(
                    ByteHelper.createFrameMessage(JsonUtil.create().cmd("502").setForward(D.HALL_NEED_FORWARD).errorCode("502").toJsonString()));
            return;
        }
        // 处理消息
        String cmd = json.getString(D.CMD);
        // 心跳处理
        if (D.HEARTBEAT_TIME.equals(cmd)) { 
            JSONObject js = JsonUtil.create().put(D.HALL_HEART_TIME, System.currentTimeMillis()).getJsonObject();
            String jb = JsonUtil.create().cmd(D.HEARTBEAT_TIME).setForward(D.HALL_NEED_FORWARD).put(js).toJsonString();
            ctx.writeAndFlush(ByteHelper.createFrameMessage(jb));
            return;
        }
        // net处理
        int forward = json.getIntValue(D.HALL_WHETHER_FORWARD);
        if (forward == D.HALL_NEED_FORWARD) {
            Class<MessageNet> net = NetManager.instance().get(cmd);
            //当房号为100000开头时处理
            if(cmd.equals(D.JION_ROOM)) {
            	ctx.writeAndFlush(
                        ByteHelper.createFrameMessage(JsonUtil.create().cmd(D.JION_ROOM).setForward(D.HALL_NEED_FORWARD).errorCode("1000").toJsonString()));	
            	 return;
            }
            if (net == null) {
                ctx.writeAndFlush(
                        ByteHelper.createFrameMessage(JsonUtil.create().cmd("503").setForward(D.HALL_NEED_FORWARD).errorCode("503").toJsonString()));
                logger.error("MESSAGE ERROR [503] server has no net handler");
                return;
            }
            
            // 异步消息处理
            WorkManager.instance().submit(net, ctx, json);
        } 
    }


    private Map<ChannelHandlerContext, Integer> msgPackageCount = new HashMap<>();
    private Map<ChannelHandlerContext, Long> msgPackageLastCountTime = new HashMap<>();

    /**
     * 检查每秒消息报数量
     *
     * @param ctx
     * @return
     */
    private boolean checkMsgPackageNum(ChannelHandlerContext ctx) {
        if (msgPackageCount.get(ctx) == null) {
            msgPackageCount.put(ctx, 0);
            return true;
        }
        int count = msgPackageCount.get(ctx);
        ServerDef def = DefFactory.instance().getDef(DefType.SERVER);
        if (count > def.getSecondMaxMessage()) {
            msgPackageCount.remove(ctx);
            msgPackageLastCountTime.remove(ctx);
            logger.error("role send to many message , ctx:{}", ctx.channel());
            return false;
        }
        msgPackageCount.put(ctx, count + 1);
        long now = System.currentTimeMillis();
        //
        if (msgPackageLastCountTime.get(ctx) == null) {
            msgPackageLastCountTime.put(ctx, now);
            return true;
        }
        long lastTime = msgPackageLastCountTime.get(ctx);
        if (now - lastTime > 1000) {
            msgPackageLastCountTime.put(ctx, now);
            msgPackageCount.put(ctx, 0);
        }
        return true;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // TODO 客户端异常掉线处理
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 客户端建立连接主动推送时间
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
    	// TODO 连接成功
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
    	RoomManager.intance().removeChannel(ctx);
    }
}
