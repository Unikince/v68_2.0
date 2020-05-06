package com.dmg.zhajinhuaserver.service.impl;

import com.dmg.zhajinhuaserver.config.Config;
import com.dmg.zhajinhuaserver.manager.RoomManager;
import com.dmg.zhajinhuaserver.model.bean.GameRoom;
import com.dmg.zhajinhuaserver.model.bean.Player;
import com.dmg.zhajinhuaserver.model.bean.RoundRecord;
import com.dmg.zhajinhuaserver.result.MessageResult;
import com.dmg.zhajinhuaserver.result.ResultEnum;
import com.dmg.zhajinhuaserver.service.PushService;
import com.dmg.zhajinhuaserver.service.QueryRoundRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.dmg.zhajinhuaserver.config.MessageConfig.QUERY_WATCHLIST;

/**
 * @author mice
 * @description: 战绩查询
 * @return
 * @date 2019/7/10
 */
@Service
@Slf4j
public class QueryRoundRecordServiceImpl implements QueryRoundRecordService {
    @Autowired
    private PushService pushService;
    @Override
    public void queryRoundRecord(Player player, int type) {
        GameRoom room = RoomManager.instance().getRoom(player.getRoomId());
        if (room == null) {
            pushService.push(player.getRoleId(),QUERY_WATCHLIST, ResultEnum.ROOM_NO_EXIST.getCode());
            return;
        }
        List<RoundRecord> list = new ArrayList<>();
        if (type == 0) { // 查询所有玩家上一局的战绩
            int round = room.getRound();
            if (room.getRoomStatus() == Config.RoomStatus.GAME) {
                round -= 1;
            }
            list = room.getRoundRecordMap().get(round);
            if (list == null) {
                list = Collections.emptyList();
            }
        } else {
            int totalRound = room.getRound();
            if (room.getRoomStatus() == Config.RoomStatus.GAME) {
                totalRound -= 1;
            }
            for (int round = 1; round <= totalRound; round++) {
                List<RoundRecord> list0 = room.getRoundRecordMap().get(round);
                if (list0 != null) {
                    for (RoundRecord record : list0) {
                        if (record.getRoleId() == player.getRoleId()) {
                            list.add(record);
                        }
                    }
                }
            }
        }
        MessageResult messageResult = new MessageResult(QUERY_WATCHLIST,list);
        pushService.push(player.getRoleId(), messageResult);
    }
}
