package com.dmg.bcbm.core.server.websocket.handler;

import com.dmg.bcbm.core.manager.RoleManager;
import com.dmg.bcbm.core.manager.RoomManager;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author zhuqd
 * @Date 2017年7月18日
 * @Description 客户端连接超时处理
 */
public class NetIdleHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent e = (IdleStateEvent) evt;
			if (e.state() == IdleState.READER_IDLE) {
				RoomManager.intance().removeChannel(ctx);
			} else if (e.state() == IdleState.WRITER_IDLE) {

			}
			
		}
	}

}
