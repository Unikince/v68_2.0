package com.dmg.bcbm.logic.def;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import com.dmg.bcbm.core.abs.def.UniqueDef;
import com.dmg.bcbm.core.abs.def.type.IDefType;
import com.dmg.bcbm.core.annotation.Def;

/**
 * @author zhuqd
 * @Date 2017年7月24日
 * @Desc 服务器配置
 */
@Def("server_config.properties")
@Component
@RefreshScope
public class ServerDef extends UniqueDef<IDefType> {

	@Value("${bcbm.server.debug}")
	private boolean debug;
	@Value("${bcbm.server.class_path}")
	private String classPath;
	@Value("${server.host}")
	private String host;
	@Value("${bcbm.server.websocket_port}")
	private int websocketPort;
	@Value("${bcbm.server.tcp_port}")
	private int tcpPort;
	@Value("${bcbm.server.http_port}")
	private int httpPort;
	@Value("${bcbm.server.count_message_cost_time}")
	private boolean countMessageCostTime;
	@Value("${bcbm.server.second_max_message}")
	private int secondMaxMessage;
	
	//v68_lobby_server
	@Value("${v68_lobby_host}")
	private String v68_lobby_host;
	@Value("${v68_lobby_port}")
	private String v68_lobby_port;

	@Override
	public DefType type() {
		return DefType.SERVER;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getWebsocketPort() {
		return websocketPort;
	}

	public void setWebsocketPort(int websocketPort) {
		this.websocketPort = websocketPort;
	}

	public int getTcpPort() {
		return tcpPort;
	}

	public void setTcpPort(int tcpPort) {
		this.tcpPort = tcpPort;
	}

	public int getHttpPort() {
		return httpPort;
	}

	public void setHttpPort(int httpPort) {
		this.httpPort = httpPort;
	}

	public boolean isCountMessageCostTime() {
		return countMessageCostTime;
	}

	public void setCountMessageCostTime(boolean countMessageCostTime) {
		this.countMessageCostTime = countMessageCostTime;
	}

	public String getClassPath() {
		return classPath;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

	public int getSecondMaxMessage() {
		return secondMaxMessage;
	}

	public void setSecondMaxMessage(int secondMaxMessage) {
		this.secondMaxMessage = secondMaxMessage;
	}

	public String getV68_lobby_host() {
		return v68_lobby_host;
	}

	public void setV68_lobby_host(String v68_lobby_host) {
		this.v68_lobby_host = v68_lobby_host;
	}

	public String getV68_lobby_port() {
		return v68_lobby_port;
	}

	public void setV68_lobby_port(String v68_lobby_port) {
		this.v68_lobby_port = v68_lobby_port;
	}

}
