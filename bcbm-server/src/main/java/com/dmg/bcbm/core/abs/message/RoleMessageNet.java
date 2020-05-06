package com.dmg.bcbm.core.abs.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.dmg.bcbm.core.abs.role.Role;
import com.dmg.bcbm.core.config.D;
import com.dmg.bcbm.core.manager.RoleManager;
import com.zyhy.common_server.util.StringUtils;

import io.netty.channel.ChannelHandlerContext;
import util.ByteHelper;
import util.json.JsonUtil;

/**
 * @author zhuqd
 * @Date 2017年7月24日
 * @Desc
 */
public abstract class RoleMessageNet extends MessageNet {
	private static Logger logger = LoggerFactory.getLogger(RoleMessageNet.class);
	protected ChannelHandlerContext ctx;
	protected JSONObject message;
	protected String roleId;

	@Override
	public void init(Object... args) {
		this.ctx = (ChannelHandlerContext) args[0];
		this.message = (JSONObject) args[1];
		this.roleId = message.getString("roleId");
		//
		if (StringUtils.isEmpty(roleId)) {
			roleId = RoleManager.instance().getRoleId(ctx);
			message.put("roleId", roleId);
		}
		ChannelHandlerContext ctx1 = RoleManager.instance().getChannel(roleId);
		if (ctx1 == null) {
			logger.error("channel is null. roleId:{}", roleId);
			ctx.writeAndFlush(ByteHelper.createMessageByte(JsonUtil.create().cmd("504").setForward(D.HALL_NEED_FORWARD).errorCode("504").toJsonString()));
			return;
		}
		if (ctx != ctx1) {
			// 防止客户端通未经授权或者没有经过正常流程发生一些消息导致服务器处理错误
			logger.error("channel is change. roleId:{}", roleId);
			ctx.writeAndFlush(ByteHelper.createMessageByte(JsonUtil.create().cmd("504").setForward(D.HALL_NEED_FORWARD).errorCode("504").toJsonString()));
			return;
		}
	}

	@Override
	public void go() {
		if (StringUtils.isEmpty(roleId)) {
			return;
		}
		Role role = RoleManager.instance().getRole(roleId);
		if (role == null) {
			logger.error("not find role int cache. roleId:{}", roleId);
			ctx.writeAndFlush(ByteHelper.createMessageByte(JsonUtil.create().cmd("504").setForward(D.HALL_NEED_FORWARD).errorCode("504").toJsonString()));
			return;
		}
		go(role, message);
	}

	/**
	 * 处理消息
	 * 
	 * @param message
	 */
	protected abstract void go(Role role, JSONObject message);

}
