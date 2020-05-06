package com.dmg.bcbm.core.server.tcp.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dmg.bcbm.core.abs.message.MessageNet;
import com.dmg.bcbm.core.abs.role.Role;
import com.dmg.bcbm.core.config.D;
import com.dmg.bcbm.core.manager.NetManager;
import com.dmg.bcbm.core.manager.RoleManager;
import com.dmg.bcbm.core.manager.WorkManager;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import util.ByteHelper;
import util.json.JsonUtil;

/**
 * @Date: 2015年10月7日 上午10:22:36
 * @Author: zhuqd
 * @Description:
 */
public class ProtocolHandler extends ChannelInboundHandlerAdapter {

	private static Logger logger = LoggerFactory.getLogger(ProtocolHandler.class);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		buf.release();
		String message = new String(req,"utf-8");
		JSONObject json = null;
		try {
			json = JSON.parseObject(message);
		} catch (Exception e) {
			logger.error("game server message format error. {}", message, e);
			ctx.writeAndFlush(
					ByteHelper.createMessageByte(JsonUtil.create().cmd("500").setForward(D.HALL_NEED_FORWARD).errorCode("501").toJsonString()));
			return;
		}
		if (json == null || json.getString(D.CMD) == null) {
			logger.error("game server message has nod cmd code. {}", message);
			ctx.writeAndFlush(
					ByteHelper.createMessageByte(JsonUtil.create().cmd("500").setForward(D.HALL_NEED_FORWARD).errorCode("502").toJsonString()));
			return;
		}
		String cmd = json.getString(D.CMD);
		if (D.HEARTBEAT_TIME.equals(cmd)) {
			handleHearbeat(ctx, json);
			return;
		}
		
		logger.info("received messages " + message);

		int forward = json.getIntValue(D.HALL_WHETHER_FORWARD);
		if (forward == D.HALL_NEED_FORWARD) { 
			Class<MessageNet> net = NetManager.instance().get(cmd);
			if (net == null) {
				logger.error("game server message hanlder is null {}", message);
				ctx.writeAndFlush(
						ByteHelper.createMessageByte(JsonUtil.create().cmd("500").setForward(D.HALL_NEED_FORWARD).errorCode("503").toJsonString()));
				return;
			}
			WorkManager.instance().submit(net, ctx, json);
		} else if(forward > D.HALL_NEED_FORWARD) {// 进行消息转发
			transpondMessage(json);
		}
	}

	/**
	 * 转发消息
	 * 
	 * @param message
	 */
	private void transpondMessage(JSONObject message) {
		
		String roleId = message.getString("identify");
		Role role = RoleManager.instance().getRole(roleId);
		if (role != null && role.isActive()) {
			role.sendWebsocketMessage(message);
		}
	}

	/**
	 * 处理心跳
	 * 
	 * @param ctx
	 * @param json
	 */
	private void handleHearbeat(ChannelHandlerContext ctx, JSONObject json) {
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("ctx:{}", ctx.channel());
		logger.error("execption,{},{}", cause, cause.getMessage());
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		logger.info("a game server client come : {}", ctx.channel());
	}

}
