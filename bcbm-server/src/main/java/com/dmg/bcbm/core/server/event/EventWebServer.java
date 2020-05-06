/**
 * 
 */
package com.dmg.bcbm.core.server.event;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import com.dmg.bcbm.core.server.event.logic.EventInfoLogic;

/**
 * @author Administrator
 * 
 */
public class EventWebServer extends WebSocketClient {

	public EventWebServer(URI serverUri) {
		super(serverUri);
	}

	@Override
	public void onClose(int arg0, String arg1, boolean arg2) {
		//EventInfoLogic.getInstance().restartConnEventServer();
	}

	@Override
	public void onError(Exception arg0) {
		arg0.printStackTrace();
		//EventInfoLogic.getInstance().restartConnEventServer();
	}

	@Override
	public void onMessage(String message) {
	}

	@Override
	public void onOpen(ServerHandshake arg0) {
		System.out.println("打开链接");
	}
}