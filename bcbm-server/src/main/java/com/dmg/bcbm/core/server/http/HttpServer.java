package com.dmg.bcbm.core.server.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dmg.bcbm.core.server.http.handler.HttpMessageHandler;

/**
 * @Date: 2015年8月20日 下午4:12:06
 * @Author: zhuqd
 * @Description:
 */
public class HttpServer implements Runnable {
	private int port;
	private ChannelFuture serverChannel;

	private static final Logger LOG = LoggerFactory.getLogger(HttpServer.class);

	public HttpServer() {
	}

	/**
	 * server start
	 * 
	 * @param port
	 */
	public void bind(int port) {
		this.port = port;
		Thread server = new Thread(this);
		server.start();
	}

	@Override
	public void run() {
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(workGroup, workGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new HttpServerCodec());
							ch.pipeline().addLast(new HttpObjectAggregator(65536));
							ch.pipeline().addLast(new ChunkedWriteHandler());
							// http请求处理器
							ch.pipeline().addLast(new HttpMessageHandler());
						}
					}).childOption(ChannelOption.SO_KEEPALIVE, false);

			serverChannel = b.bind(port).sync();
			LOG.info("[LOBBY HTTP SERVER]STARTUP[OK]PORT[{}]", port);
			serverChannel.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			workGroup.shutdownGracefully();
		}
	}

	public void close() {
		serverChannel.channel().close();
		LOG.info("[LOBBY HTTP SERVER]CLOSED[OK]PORT[{}]", port);
	}

	public int getPort() {
		return port;
	}

}
