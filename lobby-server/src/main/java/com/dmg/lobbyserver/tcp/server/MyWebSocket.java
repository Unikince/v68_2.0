/**
 * 注意：本内容仅限于大拇哥互娱科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
package com.dmg.lobbyserver.tcp.server;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.dmg.common.core.config.RedisRegionConfig;
import com.dmg.lobbyserver.exception.ScoketBusinessException;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import com.dmg.lobbyserver.tcp.manager.LocationManager;
import com.dmg.server.common.enums.PlaceEnum;
import com.zyhy.common_server.util.JsonUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;

import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullListAsEmpty;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullStringAsEmpty;
import static com.dmg.lobbyserver.config.MessageConfig.Heart_Beat;

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

    private static StringRedisTemplate stringRedisTemplate;

    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        MyWebSocket.stringRedisTemplate = stringRedisTemplate;
    }

    /*
     * ---------end----------
     */

    // 0当前定位(字符串-9527默认只连接了服务器)
    private String location = "-9527";

    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    @Getter
    private Session session;

    private String appId;

    /**
     * appid 用户id
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(@PathParam("appId") String appId, Session session) {
        this.session = session;
        this.appId = appId;
        MyWebSocket myOldWebSocket = locationManager.getWebSocket(Long.parseLong(appId));
        if (myOldWebSocket != null) {
            log.info("客户端{}重复连接", appId);
            try {
                myOldWebSocket.getSession().close();
            } catch (IOException e) {
                log.error("{}", e);
            }
        }
        locationManager.addUserMap(appId, this);
        MessageResult res = new MessageResult(1, "连接成功", "1");
        sendMessage(JSONObject.toJSONString(res, WriteNullStringAsEmpty, WriteNullListAsEmpty));
        log.info("客户端{}连接加入! 当前在线人数为{}", appId, locationManager.getPushSize());
        stringRedisTemplate.opsForValue().set("session:" + appId, session.getId(), TimeUnit.DAYS.toDays(1));
        stringRedisTemplate.opsForValue().set(RedisRegionConfig.ONLINE_NUM_KEY + ":" + PlaceEnum.CODE_LOBBY.getCode(), String.valueOf(locationManager.getPushSize()));
    }


    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        locationManager.close(this.appId, session);
        log.info("客户端{}连接关闭！当前在线人数为{}", this.appId, locationManager.getPushSize());
        stringRedisTemplate.delete("session:" + appId);
        stringRedisTemplate.opsForValue().set(RedisRegionConfig.ONLINE_NUM_KEY + ":" + PlaceEnum.CODE_LOBBY.getCode(), String.valueOf(locationManager.getPushSize()));
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) throws ParseException {
        JSONObject jo = JSONObject.parseObject(message);
        JSONObject data = jo.getJSONObject("data");
        String messageId = jo.getString("m");
        AbstractMessageHandler handler = mainProcess.getHandler(messageId);
        if (!StringUtils.equals(messageId, Heart_Beat)) {
            log.info("来自客户端{}的消息:{}", appId, message);
        }
        if (handler == null) {
            MessageResult result = new MessageResult(1, "消息号未找到,messageId=" + messageId, "");
            sendMessage(JsonUtils.object2json(result));
        } else {
            MessageResult result = new MessageResult(messageId);
            handler.messageHandler(appId, data, result);
            ValueFilter filter = (object, name, value) -> {
                if (value instanceof BigDecimal || value instanceof Double || value instanceof Float) {
                    return new BigDecimal(value.toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
                }
                return value;
            };
            String resultStr = JSONObject.toJSONString(result, filter, WriteNullStringAsEmpty, WriteNullListAsEmpty);
            sendMessage(resultStr);
        }
    }

    /**
     * 发生错误时调用
     **/
    @OnError
    public void onError(Session session, Throwable exception) {
        String result = "";
        if (exception instanceof ScoketBusinessException) {
            result = MessageResult.error(((ScoketBusinessException) exception).getCmd(), ((ScoketBusinessException) exception).getCode());
        } else {
            result = MessageResult.errorStr(ResultEnum.SYSTEM_EXCEPTION.getCode());

        }
        log.error("socket发生异常,客户端{}==>{}", appId, exception.fillInStackTrace());
        sendMessage(result);

    }

    public void sendMessage(String message) {
        try {
            if (this.session.isOpen()) {
                //log.info("消息返回:{}", message);
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
