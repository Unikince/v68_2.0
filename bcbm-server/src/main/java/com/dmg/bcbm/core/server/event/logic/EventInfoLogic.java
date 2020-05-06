package com.dmg.bcbm.core.server.event.logic;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dmg.bcbm.core.manager.DefFactory;
import com.dmg.bcbm.core.server.event.EventWebServer;
import com.dmg.bcbm.logic.def.DefType;
import com.dmg.bcbm.logic.def.ServerDef;

/**
 * @author Administrator
 * 外网连接
 */
public class EventInfoLogic {
	
	private static final Logger LOG = LoggerFactory.getLogger(EventInfoLogic.class);
	private EventWebServer webServer;
	private long lastConnTime;
	private static EventInfoLogic instance;
	
	public static EventInfoLogic getInstance(){
		if (instance == null) {
			instance = new EventInfoLogic();
		}
		return instance;
	}
	
	public void initWebSocketServer(EventWebServer webServer){
		this.webServer = webServer;
		this.lastConnTime = System.currentTimeMillis();
	}
	
	/**
	 * 启动与外网通信
	 */
	public void startEventServer() {
		/*try {
			ServerDef def = DefFactory.instance().getDef(DefType.SERVER);
			URI endpointURI = new URI("ws://" + def.getV68_20_lobby_host() + ":" + def.getV68_20_lobby_port() + "/websocket/99999996");
			EventWebServer webServer = new EventWebServer(endpointURI);
			boolean openstatus = webServer.connectBlocking();
			LOG.info("启动与外网通信openstatus:{}", openstatus);
			if (openstatus) {
				initWebSocketServer(webServer);
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
	}
	
	
	/**
	 * 重连
	 */
	public void restartConnEventServer(){
		// 判断上次连接时间是否大于1分钟
		if (lastConnTime == 0 || (lastConnTime + 60 * 1000) < System.currentTimeMillis()) {
			// 尝试重连
			startEventServer();
		}
	}

	public long getLastConnTime() {
		return lastConnTime;
	}
	public void setLastConnTime(long lastConnTime) {
		this.lastConnTime = lastConnTime;
	}
	public EventWebServer getWebServer() {
		return webServer;
	}
	public void setWebServer(EventWebServer webServer) {
		this.webServer = webServer;
	}
}
