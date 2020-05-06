package com.dmg.bcbm.core.abs.role;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import util.StringHelper;

/**
 * @author zhuqd
 * @Date 2017年7月18日
 */
public class Role {
	private static Logger logger = LoggerFactory.getLogger(Role.class);
	protected String roleId;
	protected String uuid;
	protected String nickName;
	protected double gold;
	protected transient ChannelHandlerContext ctx;
	protected transient boolean isActive;
	protected transient long lastActiveTime; // 最近活动时间

	/**
	 * websocket协议消息返回
	 * 
	 * @param message
	 */
	public void sendWebsocketMessage(String message) {
		if (StringHelper.isEmpty(message)) {
			return;
		}
		if (!isActive) {
			return;
		}
		if (ctx == null || ctx.isRemoved()) {
			return;
		}
		ctx.writeAndFlush(new TextWebSocketFrame(message));
		logger.info("--send message:role:{}, {}", roleId, message);
	}

	/**
	 * websocket协议消息返回
	 * 
	 * @param message
	 */
	public void sendWebsocketMessage(JSONObject message) {
		sendWebsocketMessage(message.toJSONString());
	}

	public ChannelHandlerContext getCtx() {
		return ctx;
	}


	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public long getLastActiveTime() {
		return lastActiveTime;
	}

	public void setLastActiveTime(long lastActiveTime) {
		this.lastActiveTime = lastActiveTime;
	}

	public double getGold() {
		return gold;
	}

	public void setGold(double gold) {
		this.gold = gold;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
}
