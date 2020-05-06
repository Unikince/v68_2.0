/**
 *  注意：本内容仅限于大拇哥互娱科技有限公司内部传阅，禁止外泄以及用于其他的商业目 
 */
package com.dmg.clubserver.tcp.server;

import com.alibaba.fastjson.JSONObject;
import com.dmg.clubserver.exception.ScoketBusinessException;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.result.MessageResult;
import com.dmg.clubserver.result.ResultEnum;
import com.dmg.clubserver.tcp.manager.LocationManager;
import com.zyhy.common_server.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * @className:--MyWebSocket
 * @author:-----Vito
 * @date:-------2019年3月18日 下午4:16:12
 * @version:----1.0
 * @Description:websocket
 */
@ServerEndpoint(value = "/websocket/{appId}")
@Component
@Slf4j
public class MyWebSocket {
	/*
	 * 以下为解决websocket不能注入问题 ------start----------
	 */
	private static LocationManager locationManager;
	@Autowired
	public void setLocationManager(LocationManager locationManager) {
		MyWebSocket.locationManager = locationManager;
	}
	private static MainProcess mainProcess;
	@Autowired
	public void setMainProcess(MainProcess mainProcess) {
		MyWebSocket.mainProcess = mainProcess;
	}
	
	/*
	 * ---------end----------
	 */

	// 0当前定位(字符串-9527默认只连接了服务器)
	private String location = "-9527";

	// 与某个客户端的连接会话，需要通过它来给客户端发送数据
	private Session session;
	
	private String appId; 

	/**
	 * appid 用户id
	 * 连接建立成功调用的方法
	 */
	@OnOpen
	public void onOpen(@PathParam("appId") String appId, Session session) {
		log.info("有新连接到来：" + appId);
		this.session = session;
		this.appId = appId;
		locationManager.addUserMap(appId, this);
		MessageResult res = new MessageResult(1, "连接成功","1");
		sendMessage(JsonUtils.object2json(res));
		log.info("连接加入! 当前在线人数为"+locationManager.getPushSize());
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose() {
		locationManager.close(this.appId);
		log.info("有一连接关闭！当前在线人数为" + locationManager.getPushSize());
	}

	/**
	 * 收到客户端消息后调用的方法
	 * @param message 客户端发送过来的消息
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		log.info("来自客户端的消息:" + message);
		JSONObject jo = JSONObject.parseObject(message);
		JSONObject data = jo.getJSONObject("data");
		/*Map<String, String> params = new HashMap<String, String>();
		Iterator<Entry<String, Object>> ite = jo.entrySet().iterator();
		while (ite.hasNext()) {
			Entry<String, Object> entry = ite.next();
			params.put(entry.getKey(), entry.getValue().toString());
		}*/
		String messageId = jo.getString("m");
		AbstractMessageHandler handler = mainProcess.getHandler(messageId);

		if (handler == null) {
			MessageResult result = new MessageResult(1, "消息号未找到,messageId=" + messageId,"");
			sendMessage(JsonUtils.object2json(result));
		}else {
			MessageResult result = new MessageResult(messageId);
			handler.messageHandler(appId, data,result);
			sendMessage(JsonUtils.object2json(result));
		}
	}

	/**
	 * 发生错误时调用
	 **/
	@OnError
	public void onError(Session session, Throwable exception) {
		exception.printStackTrace();
		String result = "";
		if (exception instanceof ScoketBusinessException){
			result = MessageResult.error(((ScoketBusinessException) exception).getCmd(),((ScoketBusinessException) exception).getCode());
		}else {
			result = MessageResult.errorStr(ResultEnum.SYSTEM_EXCEPTION.getCode());

		}
		sendMessage(result);

	}
	
	public void sendMessage(String message) {
		try {
			if (this.session.isOpen()) {
				this.session.getBasicRemote().sendText(message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/*public Object getRequestParam(Object o,JSONObject message) {
		Method[] methods = o.getClass().getMethods();
		Object requestParam = null;
		for (int i=0;i<methods.length;i++){
			if (methods[i].getName().equals("messageHandler")){
				Method messageHandler = methods[i];
				Class[] params = messageHandler.getParameterTypes();
				System.out.println(params[1].getName());
				JSONObject data = message.getJSONObject("data");
				requestParam = data.toJavaObject(params[1]);
				try{
					Method method =  params[1].getSuperclass().getDeclaredMethod("setM", new Class[]{String.class});
					method.invoke(requestParam, new Object[]{message.getString("m")});
				}catch(Exception ex){
					ex.printStackTrace();
				}
				break;
			}
		}
		return requestParam;
	}*/

}
