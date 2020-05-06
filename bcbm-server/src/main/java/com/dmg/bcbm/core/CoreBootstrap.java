package com.dmg.bcbm.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.dmg.bcbm.core.manager.DaoManager;
import com.dmg.bcbm.core.manager.DefFactory;
import com.dmg.bcbm.core.manager.HttpManager;
import com.dmg.bcbm.core.manager.NetManager;
import com.dmg.bcbm.core.manager.RoomManager;
import com.dmg.bcbm.core.manager.ServiceManager;
import com.dmg.bcbm.core.manager.StateTimerManager;
import com.dmg.bcbm.core.manager.TimerManager;
import com.dmg.bcbm.core.manager.WorkManager;
import com.dmg.bcbm.core.pool.DBPool;
import com.dmg.bcbm.core.pool.WaterPoolManager;
import com.dmg.bcbm.core.server.event.logic.EventInfoLogic;
import com.dmg.bcbm.core.server.http.HttpServer;
import com.dmg.bcbm.core.server.tcp.TCPServer;
import com.dmg.bcbm.core.server.websocket.WebsocketServer;
import com.dmg.bcbm.core.thread.ThreadWorkCounter;
import com.dmg.bcbm.logic.def.DefType;
import com.dmg.bcbm.logic.def.QueueType;
import com.dmg.bcbm.logic.def.ServerDef;
import com.dmg.bcbm.logic.service.UserService;
import com.dmg.data.client.NettySend;
import com.dmg.gameconfigserverapi.feign.GameConfigService;

/**
 * @author zhuqd
 * @Date 2017年7月18日
 */
@Component
public class CoreBootstrap {

	private static final Logger LOG = LoggerFactory.getLogger(CoreBootstrap.class);

	private WebsocketServer websocketServer;
	private TCPServer tcpServer;
	private HttpServer httpServer;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private DefFactory lobbyDefFactory;
	@Autowired
	private DBPool lobbyDBPool;
	@Autowired
	private StringRedisTemplate redisTemplate ;
	@Autowired
    private NettySend nettySend;
	@Autowired
	private GameConfigService gameConfigService;
	/**
	 * 初始化
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception {
		lobbyDefFactory.init();
		lobbyDBPool.init(jdbcTemplate);
		DaoManager.instance().init();
		ServiceManager.instance().init();
		NetManager.instance().init();
		HttpManager.instance().init();
		TimerManager.instance().init();
		WorkManager.instance().init(QueueType.values());
		ThreadWorkCounter.instance().init(true);
		ServerDef def = DefFactory.instance().getDef(DefType.SERVER);
		LOG.info("[SERVER INITIALIZED]DEBUG[{}]", def.isDebug());
		//RoleManager.instance().init();
		RoomManager.intance().init(gameConfigService); 
		WaterPoolManager.instance().load(redisTemplate); 
		// 启动计时器
		StateTimerManager.instance().init();
		// 初始化netty客户端
		UserService userService = ServiceManager.instance().get(UserService.class);
		userService.init(nettySend);
	}

	/**
	 * 启动websocket服务
	 */
	public void startWebsocketServer() {
		ServerDef def = DefFactory.instance().getDef(DefType.SERVER);
		websocketServer = new WebsocketServer();
		websocketServer.bind(def.getHost(), def.getWebsocketPort());
	}

	private void restartWebsocketServer() {
		if (websocketServer != null) {
			websocketServer.close();
			websocketServer = null;
		}
		startWebsocketServer();
	}

	/**
	 * 启动与游戏通信服务
	 */
	public void startLobbyServer() {
		tcpServer = new TCPServer();
		tcpServer.bootstrap();
	}

	/**
	 * 启动与外网通信
	 */
	public void startEventServer() {
		//EventInfoLogic.getInstance().startEventServer();
	}

	private void restartLobbyServer() {
		if (tcpServer != null) {
			tcpServer.close();
			tcpServer = null;
		}
		startLobbyServer();
	}

	/**
	 * 启动Http服务
	 */
	public void startHttpServer() {
		ServerDef def = DefFactory.instance().getDef(DefType.SERVER);
		if (def.getHttpPort() == 0) {
			return;
		}
		httpServer = new HttpServer();
		httpServer.bind(def.getHttpPort());
	}

	private void restartHttpServer() {
		if (httpServer != null) {
			httpServer.close();
			httpServer = null;
		}
		startHttpServer();
	}

	/**
	 * 启动服务器
	 * 
	 * @throws Exception
	 */
	public void start() throws Exception {
		init();
		startWebsocketServer();
		startLobbyServer();
		startHttpServer();
		startEventServer();
	}

	@EventListener(RefreshScopeRefreshedEvent.class)
	public void onApplicationEvent(RefreshScopeRefreshedEvent event) {
		ServerDef def = DefFactory.instance().getDef(DefType.SERVER);
		if (websocketServer == null
				|| (!websocketServer.getHost().equals(def.getHost()))
				|| websocketServer.getPort() != def.getWebsocketPort()) {
			restartWebsocketServer();
		}
		if (tcpServer == null || tcpServer.getPort() != def.getTcpPort()) {
			restartLobbyServer();
		}
		if (httpServer == null || httpServer.getPort() != def.getHttpPort()) {
			restartHttpServer();
		}
	}

}
