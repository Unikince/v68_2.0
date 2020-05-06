package com.dmg.clubserver.process.majiong;

import com.alibaba.fastjson.JSONObject;
import com.dmg.clubserver.model.table.Seat;
import com.dmg.clubserver.model.table.Table;
import com.dmg.clubserver.model.table.TableManager;
import com.dmg.clubserver.model.vo.DealTableVO;
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
 * @Description 退出俱乐部房间
 * @Author mice
 * @Date 2019/6/1 10:12
 * @Version V1.0
 **/
@Service
@Slf4j
public class QuitTableProcess implements AbstractMessageHandler {
    private static LocationManager locationManager;
    @Autowired
    public void setLocationManager(LocationManager locationManager) {
        QuitTableProcess.locationManager = locationManager;
    }

    @Override
    public String getMessageId() {
        return QUIT_TABLE;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        log.info("退出房间回调:{}",params.toJSONString());
        QuitTableVO vo = params.toJavaObject(QuitTableVO.class);
        Table table = TableManager.instance().getTable(vo.getClubId(),vo.getRoomId());
        if (table!=null && table.getSeatMap()!=null){
            for (Integer seatOrder : table.getSeatMap().keySet()){
                if (table.getSeatMap().get(seatOrder).getRoleId().equals(vo.getRoleId())){
                    table.getSeatMap().remove(seatOrder);
                    log.info("移除房间号为:{},牌桌号为:{},座位号为:{},roleId为:{} 的玩家",vo.getClubId(),table.getTableNum(),seatOrder,vo.getRoleId());
                    break;
                }
            }
            // 通知玩家有人退出牌桌
            MessageResult messageResult = new MessageResult(1, vo.getRoleId(), QUIT_TABLE_NTC);
            table.getSeatMap().values().forEach(seat -> {
                MyWebSocket myWebSocket = locationManager.getWebSocket(seat.getRoleId());
                if (myWebSocket != null) {
                    myWebSocket.sendMessage(JSONObject.toJSONString(messageResult));
                }
            });
        }
    }
}
