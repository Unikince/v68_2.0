package com.dmg.clubserver.process.TableManage;

import static com.dmg.clubserver.config.MessageConfig.*;

import org.java_websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dmg.clubserver.model.table.TableManager;
import com.dmg.clubserver.model.vo.ChangeTableVO;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.result.MessageResult;
import com.dmg.clubserver.result.ResultEnum;

/**
 * @Description 牌桌规则变更
 * @Author mice
 * @Date 2019/6/1 10:12
 * @Version V1.0
 **/
@Service
public class ClubTableChangeProcess implements AbstractMessageHandler {
    @Autowired
    private WebSocketClient webSocketClient;

    @Override
    public String getMessageId() {
        return CLUB_TABLE_CHANGE;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
    	ChangeTableVO vo = params.toJavaObject(ChangeTableVO.class);
        Integer tableCount = TableManager.instance().tableCount(vo.getClubId());
        if (tableCount>=20){
            result.setRes(ResultEnum.CLUB_TABLE_LIMIT.getCode());
            return;
        }
        TableManager.instance().removeTable(vo.getClubId(), vo.getRoomId());
        
        params.put("roleId",userid);
        JSONObject m = new JSONObject();
        m.put("roleId", userid);
        m.put("cmd", "dissolut_room");
        m.put("forward", 3);
        m.put("data", null);
        webSocketClient.send(m.toJSONString());
        webSocketClient.send(params.getJSONObject("tableMsg").toJSONString());
    }
}
