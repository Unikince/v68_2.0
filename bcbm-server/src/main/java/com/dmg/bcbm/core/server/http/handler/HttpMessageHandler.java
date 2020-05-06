/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.dmg.bcbm.core.server.http.handler;

import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.ReferenceCountUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dmg.bcbm.core.manager.HttpManager;

import util.StringHelper;
import util.json.JsonUtil;

public class HttpMessageHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(HttpMessageHandler.class);

    /**
     * Buffer that stores the response content
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            if (request.uri().equals("/favicon.ico")) {
                return;
            }
            if (HttpUtil.is100ContinueExpected(request)) {
                send100Continue(ctx);
            }
            Map<String, String> paramMap = new HashMap<>();
            if (request.method() == HttpMethod.POST) {
                HttpHeaders headers = request.headers();
                String dataType = headers.get("Content-Type");
                if (dataType.contains("form-data")) {
                    HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(request);
                    HttpContent chunk = (HttpContent) msg;
                    decoder.offer(chunk);
                    List<InterfaceHttpData> list = decoder.getBodyHttpDatas();
                    for (InterfaceHttpData data : list) {
                        Attribute param = (Attribute) data;
                        try {
                            paramMap.put(param.getName(), param.getValue());
                        } catch (IOException e) {
                            logger.error("解析POST请求数据出错{}", e);
                        }
                    }
                } else {
                    int len = request.content().readableBytes();
                    if (len > 0) {
                        byte[] content = new byte[len];
                        request.content().readBytes(content);
                        try {
                            String string = new String(content, "UTF-8");
                            String value = URLDecoder.decode(string, "UTF-8");
                            paramMap = parsingString(value, paramMap);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else if (request.method() == HttpMethod.GET) {
                QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.uri());
                Map<String, List<String>> params = queryStringDecoder.parameters();

                if (!params.isEmpty()) {
                    for (Entry<String, List<String>> p : params.entrySet()) {
                        String key = p.getKey();
                        List<String> vals = p.getValue();
                        for (String val : vals) {
                            paramMap.put(key, val);
                        }
                    }
                }
            }
            //logger.info("--receive http message:uri:{}, method:{}, params:{}", request.uri(), request.method(),
                   //JsonUtil.toJSONString(paramMap));
            String uri = request.uri();
            String path = "";
            if (!StringHelper.isEmpty(uri)) {
                String[] kv = uri.split("\\?");
                path = kv[0];
            }

            if (ctx.channel().remoteAddress() != null) {
                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                if (address != null) {
                    if (address.getAddress() != null) {
                        String ip = address.getAddress().getHostAddress();
                        paramMap.put("ctxIP", ip);
                    }
                }
            }
            HttpReader httpReader = HttpManager.instance().getHttpNet(path);
            if (httpReader != null) {
                httpReader.readMessage(paramMap, new HttpSender(ctx));
            } else {
                new HttpSender(ctx).send(JsonUtil.create().put("error", "没有找到对应的http请求处理器:" + request.uri()).toJsonString());
                logger.error("没有找到对应的http请求处理器:{}", request.uri());
            }
        }
        ReferenceCountUtil.release(msg);
    }

    private Map<String, String> parsingString(String parsing, Map<String, String> maps) {
        if (maps == null) {
            maps = new HashMap<>();
        }
        if (parsing != null) {
            String[] requestMessageKeyAndValue = parsing.split("&");
            if (requestMessageKeyAndValue.length > 0) {
                for (String message : requestMessageKeyAndValue) {
                	if (message == null || "".equals(message)) {
						continue;
					}
                	int firstIndex = message.indexOf("=");
                    String key = message.substring(0, firstIndex);
                    String value = message.substring(firstIndex + 1, message.length());
                    maps.put(key, value);
                }
            }
        }
        return maps;
    }
    
    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, CONTINUE);
        ctx.write(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
