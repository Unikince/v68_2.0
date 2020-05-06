package com.dmg.zhajinhuaserver.service.impl;

import com.dmg.zhajinhuaserver.config.Config;
import com.dmg.zhajinhuaserver.config.MessageConfig;
import com.dmg.zhajinhuaserver.manager.RoomManager;
import com.dmg.zhajinhuaserver.manager.TimerManager;
import com.dmg.zhajinhuaserver.manager.work.delay.NotGameRoomClearDelayWork;
import com.dmg.zhajinhuaserver.model.bean.GameRoom;
import com.dmg.zhajinhuaserver.model.bean.Player;
import com.dmg.zhajinhuaserver.result.MessageResult;
import com.dmg.zhajinhuaserver.service.CreateRoomService;
import com.dmg.zhajinhuaserver.service.PushService;
import com.dmg.zhajinhuaserver.service.cache.PlayerService;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description 创建房间
 * @Author jock
 * @Date 2019/7/10 0010
 * @Version V1.0
 **/
@Service
public class CreateRoomServiceImpl implements CreateRoomService {
    private Logger logger = LoggerFactory.getLogger(CreateRoomServiceImpl.class);
    @Autowired
    PushService pushService;
    @Autowired
    private PlayerService playerCacheService;

    @Override
    public void createRoom(Player player, Map<String, Object> ruleMap) {
        if (player.getRoomId() > 0) {
            if (RoomManager.instance().getRoom(player.getRoomId()) == null) {
                player.setRoomId(0);
                playerCacheService.update(player);
            } else {
                logger.error("player in room:{}", player.getRoomId());
                MessageResult messageResult = new MessageResult(MessageConfig.CREATE_PRIVATE_ROOM, player.getRoomId());
                pushService.push(player.getRoleId(), messageResult);
                return;
            }
        }
        GameRoom room = new GameRoom().createPrivateRoom((int) player.getRoleId(), ruleMap);
        RoomManager.instance().addQuickRooom(room);
        room.setRoomStatus(Config.RoomStatus.READY);
        try {
            TimerManager.instance().submitDelayWork(NotGameRoomClearDelayWork.class, 15 * 60 * 1000, room.getRoomId());
        } catch (SchedulerException e) {
            logger.error("sbumit NotGameRoomClearDelayWork error :{}", e);
        }
    }
}
