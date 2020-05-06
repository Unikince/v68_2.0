package com.dmg.bcbm.core.abs.message;

import com.alibaba.fastjson.JSONObject;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author zhuqd
 * @Date 2017年7月24日
 * @Desc
 */
public abstract class NoLoginMessageNet extends MessageNet {

	protected ChannelHandlerContext ctx;
	protected JSONObject message;

	@Override
	public void init(Object... args) {
		this.ctx = (ChannelHandlerContext) args[0];
		this.message = (JSONObject) args[1];
	}

	@Override
	public void go() {
		go(ctx, message);
	}

	/**
	 * 处理消息
	 * 
	 * @param ctx
	 * @param message
	 */
	public abstract void go(ChannelHandlerContext ctx, JSONObject message);

}
