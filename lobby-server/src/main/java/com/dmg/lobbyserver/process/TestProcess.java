package com.dmg.lobbyserver.process;

import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.tcp.manager.LocationManager;
import com.dmg.lobbyserver.tcp.server.MyWebSocket;
import org.java_websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullListAsEmpty;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullStringAsEmpty;

/**
 * @Description
 * @Author mice
 * @Date 2019/5/25 16:00
 * @Version V1.0
 **/
@Service
public class TestProcess implements AbstractMessageHandler{
    private static LocationManager locationManager;

    @Autowired
    public void setLocationManager(LocationManager locationManager) {
        TestProcess.locationManager = locationManager;
    }

    @Override
    public String getMessageId() {
        return "1";
    }


    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        MyWebSocket myWebSocket = locationManager.getWebSocket(999999991L);
        if (myWebSocket != null) {
            JSONObject msg = new JSONObject();
            msg.put("m",1001);
            JSONObject data = new JSONObject();
            data.put("userId",userid);
            data.put("gold",1234567);
            msg.put("data",data);
            myWebSocket.sendMessage(JSONObject.toJSONString(msg,WriteNullStringAsEmpty,WriteNullListAsEmpty));
        }
    }
}