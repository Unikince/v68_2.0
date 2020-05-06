package com.dmg.clubserver.process.majiong;

import com.alibaba.fastjson.JSONObject;
import com.dmg.clubserver.model.table.Table;
import com.dmg.clubserver.model.table.TableManager;
import com.dmg.clubserver.model.vo.GameStartVO;
import com.dmg.clubserver.model.vo.QuitTableVO;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.result.MessageResult;
import com.dmg.clubserver.tcp.manager.LocationManager;
import com.dmg.clubserver.tcp.server.MyWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.dmg.clubserver.config.MessageConfig.*;

/**
 * @Description 游戏开始
 * @Author mice
 * @Date 2019/6/1 10:12
 * @Version V1.0
 **/
@Service
@Slf4j
public class GameStartProcess implements AbstractMessageHandler {
    private static LocationManager locationManager;
    @Autowired
    public void setLocationManager(LocationManager locationManager) {
        GameStartProcess.locationManager = locationManager;
    }

    @Override
    public String getMessageId() {
        return GAME_START;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        GameStartVO vo = params.toJavaObject(GameStartVO.class);
        Table table = TableManager.instance().getTable(vo.getClubId(),vo.getRoomId());
        table.setTableStatus(1);
        table.setCurrentRound(1);
        if (table != null){
            // 游戏开始
            MessageResult messageResult = new MessageResult(1, "", GAME_START_NTC);
            table.getSeatMap().values().forEach(seat -> {
                MyWebSocket myWebSocket = locationManager.getWebSocket(seat.getRoleId());
                if (myWebSocket != null) {
                    myWebSocket.sendMessage(JSONObject.toJSONString(messageResult));
                }
            });
        }
    }
}
