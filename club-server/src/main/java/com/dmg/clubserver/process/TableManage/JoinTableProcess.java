package com.dmg.clubserver.process.TableManage;

import static com.dmg.clubserver.config.MessageConfig.JOIN_TABLE;
import lombok.extern.slf4j.Slf4j;

import org.java_websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dmg.clubserver.model.RoleInfoBean;
import com.dmg.clubserver.model.table.Seat;
import com.dmg.clubserver.model.table.Table;
import com.dmg.clubserver.model.table.TableManager;
import com.dmg.clubserver.model.vo.JoinTableVO;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.result.MessageResult;
import com.dmg.clubserver.result.ResultEnum;
import com.dmg.clubserver.service.SynchronousPlayerDataService;
import com.dmg.clubserver.tcp.manager.LocationManager;

/**
 * @Description 加入俱乐部房间
 * @Author mice
 * @Date 2019/6/1 10:12
 * @Version V1.0
 **/
@Service
@Slf4j
public class JoinTableProcess implements AbstractMessageHandler {
    private static LocationManager locationManager;
    @Autowired
    public void setLocationManager(LocationManager locationManager) {
        JoinTableProcess.locationManager = locationManager;
    }

    @Autowired
    private WebSocketClient webSocketClient;
    @Autowired
    private SynchronousPlayerDataService synchronousPlayerDataService;

    @Override
    public String getMessageId() {
        return JOIN_TABLE;
    }

    // 未使用
    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        JoinTableVO vo = params.toJavaObject(JoinTableVO.class);

        Table table = TableManager.instance().getTable(vo.getClubId(), vo.getRoomId());
        if (table == null) {
            result.setRes(ResultEnum.TABLE_HAS_BE_DISSOLVE.getCode());
            return;
        }
        if (table.getCurrentPlayerNum() == table.getPlayerNumLimit()) {
            result.setRes(ResultEnum.TABLE_PLAYER_NUM_LIMIT.getCode());
            return;
        }
        for (int i = 1; i <= table.getPlayerNumLimit(); i++) {
            if (table.getSeatMap().get(i) == null) {
                RoleInfoBean roleInfoBean = synchronousPlayerDataService.getOnePlayerInfo(vo.getRoleId()+"");
                Seat seat = new Seat();
                seat.setSeatNum(i);
                seat.setOnlineStatus(1);
                seat.setRoleId(vo.getRoleId());
                seat.setNickName(roleInfoBean.getNickName());
                seat.setHeadImage(roleInfoBean.getHeadImage());
                table.getSeatMap().put(i,seat);
                break;
            }
        }
        webSocketClient.send(vo.getJoinMsg());

    }
}
