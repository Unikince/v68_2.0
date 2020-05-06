package com.dmg.bcbm.core.server.tcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dmg.bcbm.core.manager.DefFactory;
import com.dmg.bcbm.core.server.tcp.handler.GameServerIdleHandler;
import com.dmg.bcbm.core.server.tcp.handler.ProtocolHandler;
import com.dmg.bcbm.logic.def.DefType;
import com.dmg.bcbm.logic.def.ServerDef;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @Date: 2015年10月7日 上午10:16:23
 * @Author: zhuqd
 * @Description:
 */
public class TCPServer implements Runnable {
	private static int MAX_MSG_PACK_SIZE = 10240;

	private int port;
	private ChannelFuture serverChannel;

	private static final Logger LOG = LoggerFactory.getLogger(TCPServer.class);

	public TCPServer() {
	}

	/**
	 * 启动服务器
	 */
	public void bootstrap() {
		ServerDef def = DefFactory.instance().getDef(DefType.SERVER);
		this.port = def.getTcpPort();
		Thread thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new IdleStateHandler(180, 0, 0)); 
							// 超时处理
							ch.pipeline().addLast(new GameServerIdleHandler());
							ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(MAX_MSG_PACK_SIZE, 0, 4, 0, 4));//
							ch.pipeline().addLast(new LengthFieldPrepender(4));
							// tcp消息处理
							ch.pipeline().addLast(new ProtocolHandler());
						}

					}).option(ChannelOption.SO_REUSEADDR, true)// socket重用
					.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)// 开启缓存池
					.childOption(ChannelOption.TCP_NODELAY, true)// 关闭TCP小包优化
					.childOption(ChannelOption.SO_KEEPALIVE, true)// 保持连接
					.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);//

			serverChannel = b.bind(port).sync();
			LOG.info("[LOBBY TCP SERVER]STARTUP[OK]PORT[{}]", port);
			serverChannel.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			workGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public void close() {
		serverChannel.channel().close();
		LOG.info("[LOBBY TCP SERVER]CLOSED[OK]PORT[{}]", port);
	}

	public int getPort() {
		return port;
	}

}
