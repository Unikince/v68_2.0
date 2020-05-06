package com.dmg.clubserver.process.majiong;

import com.alibaba.fastjson.JSONObject;
import com.dmg.clubserver.model.table.Table;
import com.dmg.clubserver.model.table.TableManager;
import com.dmg.clubserver.model.vo.GameStartVO;
import com.dmg.clubserver.model.vo.NextRoundVO;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.result.MessageResult;
import com.dmg.clubserver.tcp.manager.LocationManager;
import com.dmg.clubserver.tcp.server.MyWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.dmg.clubserver.config.MessageConfig.*;

/**
 * @Description 下一局
 * @Author mice
 * @Date 2019/6/1 10:12
 * @Version V1.0
 **/
@Service
@Slf4j
public class NextRoundProcess implements AbstractMessageHandler {
    private static LocationManager locationManager;
    @Autowired
    public void setLocationManager(LocationManager locationManager) {
        NextRoundProcess.locationManager = locationManager;
    }

    @Override
    public String getMessageId() {
        return NEXT_ROUND;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        NextRoundVO vo = params.toJavaObject(NextRoundVO.class);
        Table table = TableManager.instance().getTable(vo.getClubId(),vo.getRoomId());
        table.setCurrentRound(vo.getRound());
        if (table != null){
            // 下一局 更新牌桌局数
            MessageResult messageResult = new MessageResult(1, table, UPDATE_TABLE_INFO_NTC);
            table.getSeatMap().values().forEach(seat -> {
                MyWebSocket myWebSocket = locationManager.getWebSocket(seat.getRoleId());
                if (myWebSocket != null) {
                    myWebSocket.sendMessage(JSONObject.toJSONString(messageResult));
                }
            });
        }
    }
}
