package com.dmg.niuniuserver.manager.work;

import static com.dmg.niuniuserver.model.constants.GameConfig.FREE_FIELD;

import java.util.Map;

import org.quartz.SchedulerException;

import com.dmg.niuniuserver.config.RobotConfig;
import com.dmg.niuniuserver.manager.RoomManager;
import com.dmg.niuniuserver.manager.TimerManager;
import com.dmg.niuniuserver.model.bean.GameRoom;
import com.dmg.niuniuserver.model.bean.Robot;
import com.dmg.niuniuserver.model.bean.Seat;
import com.dmg.niuniuserver.model.constants.RobotPercent;
import com.zyhy.common_server.work.Cron;
import com.zyhy.common_server.work.TimeWork;

/**
 * @author zhuqd
 * @Date 2017年12月11日
 * @Desc 自动加入机器人
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
            if (room.getRoomType() == FREE_FIELD && room.getSeatMap().size() > 0 && room.getSeatMap().size() < room.getTotalPlayer()) {
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
                        if (random < RobotConfig.robotPercent.get(RobotPercent.IN_ONE_PLAYER)) {
                            bool = true;
                        }
                        break;
                    case 2:
                        if (random <= RobotConfig.robotPercent.get(RobotPercent.IN_TWO_PLAYER)) {
                            bool = true;
                        }
                        break;
                    case 3:
                        if (random < RobotConfig.robotPercent.get(RobotPercent.IN_THREE_PLAYER)) {
                            bool = true;
                        }
                        break;
                    case 4:
                        if (random < RobotConfig.robotPercent.get(RobotPercent.IN_FOUR_PLAYER)) {
                            bool = true;
                        }
                        break;
                    case 5:
                        if (random < RobotConfig.robotPercent.get(RobotPercent.IN_FIVE_PLAYER)) {
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
