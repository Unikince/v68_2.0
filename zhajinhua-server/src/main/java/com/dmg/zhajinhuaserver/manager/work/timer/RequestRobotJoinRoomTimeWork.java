package com.dmg.zhajinhuaserver.manager.work.timer;


import com.dmg.zhajinhuaserver.config.D;
import com.dmg.zhajinhuaserver.config.Config;
import com.dmg.zhajinhuaserver.manager.RoomManager;
import com.dmg.zhajinhuaserver.manager.TimerManager;
import com.dmg.zhajinhuaserver.manager.work.delay.AutoJoinRobotsDelayWork;
import com.dmg.zhajinhuaserver.model.bean.GameRoom;
import com.dmg.zhajinhuaserver.model.bean.Robot;
import com.dmg.zhajinhuaserver.model.bean.Room;
import com.dmg.zhajinhuaserver.model.bean.Seat;
import com.dmg.zhajinhuaserver.model.constants.GameConfig;
import com.zyhy.common_server.work.Cron;
import com.zyhy.common_server.work.TimeWork;
import org.quartz.SchedulerException;

import java.util.Map;


/**
 * @author zhuqd
 * @Date 2017年12月11日
 * @Desc 清除异常房间
 */
@Cron("*/3 * * * * ?")
public class RequestRobotJoinRoomTimeWork extends TimeWork {

    @Override
    public void init(Object... args) {

    }

    @Override
    public void go() {
        Map<Integer, GameRoom> roomMap = RoomManager.instance().getRoomMap();
        for (int roomId : roomMap.keySet()) {
            GameRoom room = RoomManager.instance().getRoom(roomId);
            if (room.getGameRoomTypeId() == D.FREE_FIELD && room.getSeatMap().size() > 0 && room.getSeatMap().size() < room.getTotalPlayer()) {
                int random = (int) (Math.random() * 100);
                boolean canIn = false;
                for (Seat seat : room.getSeatMap().values()) {
                    if (!(seat.getPlayer() instanceof Robot)) {
                        canIn = true;
                        break;
                    }
                }
                boolean bool = false;
                switch (room.getSeatMap().size()) {
                    case 1:
                        if (random < GameConfig.robotPercents.get(Config.RobotPercent.IN_ONE_PLAYER)) {
                            bool = true;
                        }
                        break;
                    case 2:
                        if (random <= GameConfig.robotPercents.get(Config.RobotPercent.IN_TWO_PLAYER)) {
                            bool = true;
                        }
                        break;
                    case 3:
                        if (random < GameConfig.robotPercents.get(Config.RobotPercent.IN_THREE_PLAYER)) {
                            bool = true;
                        }
                        break;
                    case 4:
                        if (random < GameConfig.robotPercents.get(Config.RobotPercent.IN_FOUR_PLAYER)) {
                            bool = true;
                        }
                        break;
                    case 5:
                        if (random < GameConfig.robotPercents.get(Config.RobotPercent.IN_FIVE_PLAYER)) {
                            bool = true;
                        }
                        break;
                    default:
                        break;
                }
                if (bool && canIn) {
                    try {
                        TimerManager.instance().submitDelayWork(AutoJoinRobotsDelayWork.class, 100, room.getRoomId(), true);
                    } catch (SchedulerException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
