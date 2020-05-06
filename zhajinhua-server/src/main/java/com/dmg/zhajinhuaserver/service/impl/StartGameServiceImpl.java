package com.dmg.zhajinhuaserver.service.impl;

import com.dmg.zhajinhuaserver.config.Config;
import com.dmg.zhajinhuaserver.config.D;
import com.dmg.zhajinhuaserver.manager.RoomManager;
import com.dmg.zhajinhuaserver.model.bean.GameRoom;
import com.dmg.zhajinhuaserver.model.bean.Player;
import com.dmg.zhajinhuaserver.model.bean.Robot;
import com.dmg.zhajinhuaserver.model.bean.Seat;
import com.dmg.zhajinhuaserver.result.MessageResult;
import com.dmg.zhajinhuaserver.result.ResultEnum;
import com.dmg.zhajinhuaserver.service.PushService;
import com.dmg.zhajinhuaserver.service.RoomService;
import com.dmg.zhajinhuaserver.service.StartGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.dmg.zhajinhuaserver.config.MessageConfig.*;

/**
 * @Description
 * @Author Administrator
 * @Date 2019/7/10 0010
 * @Version V1.0
 **/
@Service
public class StartGameServiceImpl implements StartGameService {

    @Autowired
    private RoomService roomService;

    @Autowired
    private PushService pushService;


    @Override
    public void startGame(Player player, int roomId) {
        GameRoom room = RoomManager.instance().getRoom(roomId);
        int readyCount = 0;
        for (Seat seat : room.getSeatMap().values()) {
            if (seat.isReady()) {
                readyCount++;
            }
        }
        if (readyCount >= 2) {
            for (Seat seat : room.getSeatMap().values()) {
                if (seat == null || !seat.isReady()) {
                    continue;
                }
                if (seat.getDecBaseScore() == BigDecimal.ZERO) {
                    if (!(seat.getPlayer() instanceof Robot)) {
                    }
                    seat.setDecBaseScore(BigDecimal.valueOf(room.getBaseScore()));
                }
                seat.setPlayed(true);
                seat.setState(Config.SeatState.STATE_PLAYING);
                seat.setChipsRemain(seat.getChipsRemain() - room.getBaseScore());
                room.getBetList().add(room.getBaseScore());
                seat.setBetChips(room.getBaseScore());
                room.setTotalChips(room.getTotalChips() + room.getBaseScore());
            }
            room.setLastBetChips(room.getBaseScore());
            room.setRoomStatus(Config.RoomStatus.GAME);
            room.setRound(room.getRound() + 1);
            pushService.push(player.getRoleId(), REQUEST_START_GAME);
            roomService.setBanker(room);
            Map<String, Object> tableInfo = roomService.roomMsg(room, null);
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put(D.TABLE_INFO, tableInfo);
            MessageResult messageResult = new MessageResult(GAME_START_NTC, resultMap);
            pushService.broadcast(messageResult, room);
            roomService.distributePoker(room);
        } else {
            pushService.push(player.getRoleId(), REQUEST_START_GAME, ResultEnum.CANNOT_START_GAME.getCode());
        }
    }
}
