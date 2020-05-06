package com.dmg.clubserver.process.TableManage;

import static com.dmg.clubserver.config.MessageConfig.DELETE_CLUB_TABLE;

import org.java_websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dmg.clubserver.model.table.Table;
import com.dmg.clubserver.model.table.TableManager;
import com.dmg.clubserver.model.vo.ChangeTableVO;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.result.MessageResult;
import com.dmg.clubserver.result.ResultEnum;

/**
 * @Description 删除俱乐部房间
 * @Author mice
 * @Date 2019/6/1 10:12
 * @Version V1.0
 **/
@Service
public class DeleteTableProcess implements AbstractMessageHandler {
    @Autowired
    private WebSocketClient webSocketClient;

    @Override
    public String getMessageId() {
        return DELETE_CLUB_TABLE;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        ChangeTableVO vo = params.toJavaObject(ChangeTableVO.class);
        Table table = TableManager.instance().getTable(vo.getClubId(), vo.getRoomId());
        if(table == null){
        	result.setRes(ResultEnum.PARAM_ERROR.getCode());
        	return;
        }
        if (table.getSeatMap()!=null && table.getSeatMap().size()>0){
            result.setRes(ResultEnum.CLUB_TABLE_HAS_PLAYER.getCode());
            return;
        }
        params.put("roleId",userid);
        webSocketClient.send(params.getJSONObject("tableMsg").toJSONString());
    }
}
