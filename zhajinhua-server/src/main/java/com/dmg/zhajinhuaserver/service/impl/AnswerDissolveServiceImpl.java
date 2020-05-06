package com.dmg.zhajinhuaserver.service.impl;
import com.alibaba.fastjson.JSONObject;
import com.dmg.zhajinhuaserver.config.D;
import com.dmg.zhajinhuaserver.config.MessageConfig;
import com.dmg.zhajinhuaserver.manager.TimerManager;
import com.dmg.zhajinhuaserver.model.bean.*;
import com.dmg.zhajinhuaserver.result.MessageResult;
import com.dmg.zhajinhuaserver.result.ResultEnum;
import com.dmg.zhajinhuaserver.service.AnswerDissolveService;
import com.dmg.zhajinhuaserver.service.PushService;
import com.dmg.zhajinhuaserver.service.RoomService;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

import static com.dmg.zhajinhuaserver.config.MessageConfig.ANSWER_DISOLUT_ROOM_NTC;
import static com.dmg.zhajinhuaserver.config.MessageConfig.SUCCESS_DISOLUT_ROOM_NTC;

/**
 * @Description
 * @Author jock
 * @Date 2019/7/9 0009
 * @Version V1.0
 **/
@Service
public class AnswerDissolveServiceImpl implements AnswerDissolveService {
    private  static Logger log= LoggerFactory.getLogger(AnswerDissolveServiceImpl.class);
    @Autowired
    RoomService roomService ;
    @Autowired
    PushService pushService ;
    @Override
    public void answerDissolveRoom(Player player, boolean agree) {
        try {
            GameRoom room = roomService.getRoom(player.getRoomId());
            Map<Integer, Boolean> dissolveMap = room.getDissolveMap();
            int seat = roomService.getPlaySeat(room, player);
            if (seat == 0)
            {
                pushService.push((long) player.getRoleId(), MessageConfig.ANSWER_DISOLUT_ROOM, ResultEnum.PLAYER_HAS_NO_SEAT.getCode());
                return;
            }
            dissolveMap.put(seat, agree);
            pushService.push(player.getRoleId(),MessageConfig.ANSWER_DISOLUT_ROOM);
            Map<String, Object> map = new HashMap<>();
            map.put(D.PLAYER_RID, player.getRoleId());
            map.put("agree", agree);
            MessageResult messageResult = new MessageResult(ANSWER_DISOLUT_ROOM_NTC,map);
            pushService.broadcast(messageResult,room);
            log.info("===dissolve,seat:{},agree:{}", seat, agree);
            if (agree)
            {
                int agreePlayer = 0;
                for (Boolean bool : dissolveMap.values())
                {
                    if (bool)
                    {
                        agreePlayer += 1;
                    }
                }
                int count = 0;
                for (Seat data : room.getSeatMap().values())
                {
                    if(data.isPlayed())
                    {
                        count++;
                    }
                }
                boolean bool = room.getGameRoomTypeId() == D.PRIVATE_FIELD && agreePlayer >= count;
                if (agreePlayer >= room.getSeatMap().size() || bool)
                {
                    roomService.dissolveRoomSuccess(room);
                    if(room.getGameRoomTypeId() != D.PRIVATE_FIELD)
                    {
                        Map<String, Object> ret = new HashMap<>();
                        ret.put("dissolve", true);
                        messageResult = new MessageResult(SUCCESS_DISOLUT_ROOM_NTC,ret);
                        pushService.broadcast(messageResult,room);
                    }
                }
            }
            else
            {
                room.setDissolveTime(0);
                room.setDissolveRid(0);
                dissolveMap.clear();
                Map<String, Object> ret = new HashMap<>();
                ret.put("dissolve", false);
                for (Seat data : room.getSeatMap().values())
                {
                    Player player1 = data.getPlayer();
                    if (player1 != null)
                    {
                        if(data.isPlayed())
                        {
                            pushService.push( player.getRoleId(), SUCCESS_DISOLUT_ROOM_NTC, ResultEnum.PLAYER_HAS_NO_SEAT.getCode());
                        }
                    }
                }
                try {
                    TimerManager.instance().stopTimeWork(room.getDissolveTaskId());
                } catch (SchedulerException e) {
                    log.error("stop dissolve task fail:{}", e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
