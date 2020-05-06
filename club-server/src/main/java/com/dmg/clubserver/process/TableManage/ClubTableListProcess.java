package com.dmg.clubserver.process.TableManage;

import static com.dmg.clubserver.config.MessageConfig.CLUB_TABLE_LIST;

import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hutool.core.collection.CollectionUtil;

import com.alibaba.fastjson.JSONObject;
import com.dmg.clubserver.model.table.Table;
import com.dmg.clubserver.model.table.TableManager;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.result.MessageResult;

/**
 * @Description 俱乐部牌桌列表
 * @Author mice
 * @Date 2019/6/1 10:12
 * @Version V1.0
 **/
@Service
public class ClubTableListProcess implements AbstractMessageHandler {

    @Override
    public String getMessageId() {
        return CLUB_TABLE_LIST;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
    	Map<Integer, Table> tableMap =TableManager.instance().getTables(params.getInteger("clubId"));
    	if(CollectionUtil.isEmpty(tableMap)){
    		result.setMsg("");
    	}else{
    		result.setMsg(JSONObject.toJSON(tableMap));
    	}
    }
}
