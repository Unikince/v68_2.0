package com.dmg.bcbm.core.server.tcp.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @Date: 2015年11月2日 下午2:38:59
 * @Author: zhuqd
 * @Description:超时处理
 */
public class GameServerIdleHandler extends ChannelInboundHandlerAdapter {

	private static Logger logger = LoggerFactory.getLogger(GameServerIdleHandler.class);

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent e = (IdleStateEvent) evt;
			if (e.state() == IdleState.READER_IDLE) {
				logger.error("server read idle,disconect user");
			} else if (e.state() == IdleState.WRITER_IDLE) {
				logger.warn("server write idle..ctx:{}, evt:{}", ctx.channel(), evt);
			}
			ctx.disconnect();
		}
	}

}
