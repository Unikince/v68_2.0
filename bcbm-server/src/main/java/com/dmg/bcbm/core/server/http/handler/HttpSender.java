package com.dmg.bcbm.core.server.http.handler;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpSender {

    private Logger logger = LoggerFactory.getLogger(HttpSender.class);
    private ChannelHandlerContext ctx;

    public HttpSender(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    /**
     * 发送消息
     *
     * @param message
     */
    public void send(String message) {
        FullHttpResponse response = null;
        try {
            response = new DefaultFullHttpResponse(HTTP_1_1, OK,
                    Unpooled.wrappedBuffer(message.getBytes("utf-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.headers().set("Content-Type", "text/plain");
        response.headers().set("Content-Length", response.content().readableBytes());
        response.headers().set("Access-Control-Allow-Origin", "*");
        ctx.writeAndFlush(response);
        logger.info("--send HTTP message:{}", message);
    }
}
