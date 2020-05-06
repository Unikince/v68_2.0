package com.dmg.clubserver.process.majiong;

import com.alibaba.fastjson.JSONObject;
import com.dmg.clubserver.model.RoleInfoBean;
import com.dmg.clubserver.model.table.Seat;
import com.dmg.clubserver.model.table.Table;
import com.dmg.clubserver.model.table.TableManager;
import com.dmg.clubserver.model.vo.JoinTableSuccessVO;
import com.dmg.clubserver.model.vo.JoinTableVO;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.result.MessageResult;
import com.dmg.clubserver.result.ResultEnum;
import com.dmg.clubserver.service.SynchronousPlayerDataService;
import com.dmg.clubserver.tcp.manager.LocationManager;
import com.dmg.clubserver.tcp.server.MyWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.dmg.clubserver.config.MessageConfig.*;

/**
 * @Description 加入俱乐部房间成功
 * @Author mice
 * @Date 2019/6/1 10:12
 * @Version V1.0
 **/
@Service
@Slf4j
public class JoinTableSuccessProcess implements AbstractMessageHandler {
    private static LocationManager locationManager;
    @Autowired
    public void setLocationManager(LocationManager locationManager) {
        JoinTableSuccessProcess.locationManager = locationManager;
    }

    @Autowired
    private WebSocketClient webSocketClient;
    @Autowired
    private SynchronousPlayerDataService synchronousPlayerDataService;

    @Override
    public String getMessageId() {
        return JOIN_TABLE_SUCCESS;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        JoinTableSuccessVO vo = params.toJavaObject(JoinTableSuccessVO.class);
        Table table = TableManager.instance().getTable(vo.getClubId(), vo.getRoomId());
        table.setCurrentPlayerNum(table.getCurrentPlayerNum()+1);
        RoleInfoBean roleInfoBean = synchronousPlayerDataService.getOnePlayerInfo(vo.getRoleId()+"");
        Seat seat = new Seat();
        seat.setSeatNum(vo.getSeatNum());
        seat.setOnlineStatus(1);
        seat.setRoleId(vo.getRoleId());
        seat.setNickName(roleInfoBean.getNickName());
        seat.setHeadImage(roleInfoBean.getHeadImage());
        Integer seatNum = TableManager.instance().getSeatNum(vo.getClubId(), vo.getRoomId());
        table.getSeatMap().put(seatNum,seat);
        // 通知玩家有人加入牌桌
        MessageResult messageResult = new MessageResult(1, table, JOIN_TABLE_NTC);
        table.getSeatMap().values().forEach(s -> {
            MyWebSocket myWebSocket = locationManager.getWebSocket(s.getRoleId());
            if (myWebSocket != null) {
                myWebSocket.sendMessage(JSONObject.toJSONString(messageResult));
            }
        });
    }
}
