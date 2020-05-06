package com.dmg.clubserver.process.majiong;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.clubserver.config.ClubLogType;
import com.dmg.clubserver.dao.ClubDao;
import com.dmg.clubserver.dao.bean.ClubBean;
import com.dmg.clubserver.model.table.Table;
import com.dmg.clubserver.model.table.TableManager;
import com.dmg.clubserver.model.vo.DealRoomVO;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.result.MessageResult;
import com.dmg.clubserver.service.ClubLogService;
import com.dmg.clubserver.tcp.manager.LocationManager;
import com.dmg.clubserver.tcp.server.MyWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.dmg.clubserver.config.MessageConfig.DEAL_TABLE;
import static com.dmg.clubserver.config.MessageConfig.DEAL_TABLE_NTC;

/**
 * @Description 删除俱乐部房间
 * @Author mice
 * @Date 2019/6/1 10:12
 * @Version V1.0
 **/
@Service
@Slf4j
public class DealTableProcess implements AbstractMessageHandler {
    @Autowired
    private ClubDao clubDao;
    @Autowired
    private ClubLogService clubLogService;
    private static LocationManager locationManager;
    @Autowired
    public void setLocationManager(LocationManager locationManager) {
        DealTableProcess.locationManager = locationManager;
    }

    @Override
    public String getMessageId() {
        return DEAL_TABLE;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        DealRoomVO vo = params.toJavaObject(DealRoomVO.class);
        Table table = TableManager.instance().getTable(vo.getClubId(),vo.getRoomId());
        if (table!=null && table.getSeatMap()!=null){
            if (!vo.isKou()){
                ClubBean clubBean = clubDao.selectOne(new LambdaQueryWrapper<ClubBean>().eq(ClubBean::getClubId,vo.getClubId()));
                clubBean.setRoomCard(clubBean.getRoomCard()+table.getCostRoomCard());
                // 返还房卡
                clubDao.updateById(clubBean);
                // 解散房间日志
                clubLogService.saveLog(table.getCreatorId(),null,
                        ClubLogType.DELETE_TABLE.getKey(),vo.getClubId(),table.getGameType()+"");
            }
            // 通知玩家退出牌桌
            MessageResult messageResult = new MessageResult(1, "", DEAL_TABLE_NTC);
            table.getSeatMap().values().forEach(seat -> {
                MyWebSocket myWebSocket = locationManager.getWebSocket(seat.getRoleId());
                if (myWebSocket != null) {
                    myWebSocket.sendMessage(JSONObject.toJSONString(messageResult));
                }
            });
            TableManager.instance().removeTable(vo.getClubId(),vo.getRoomId());
            // 默认3个空桌子 前3个桌子不删除
            if (table.getTableNum() <= 3){
                Table newtable = new Table();
                newtable.setTableNum(table.getTableNum());

                TableManager.instance().getTables(vo.getClubId()).put(table.getTableNum(),newtable);
            }
        }
    }
}