package com.dmg.niuniuserver.manager.work;

import com.dmg.niuniuserver.SpringContextUtil;
import com.dmg.niuniuserver.manager.RoomManager;
import com.dmg.niuniuserver.model.bean.GameRoom;
import com.dmg.niuniuserver.model.bean.Robot;
import com.dmg.niuniuserver.model.bean.Seat;
import com.dmg.niuniuserver.service.action.JoinRoomService;
import com.dmg.niuniuserver.service.cache.RobotCacheService;
import com.zyhy.common_server.work.DelayTimeWork;

import cn.hutool.core.util.RandomUtil;

public class AutoJoinRobotsDelayWork extends DelayTimeWork {

    private int roomId;
    private boolean bool;

    @Override
    public void init(Object... args) {
        this.roomId = (int) args[0];
        this.bool = (boolean) args[1];
    }

    @Override
    public void go() {
        RobotCacheService robotCacheService = SpringContextUtil.getBean(RobotCacheService.class);
        JoinRoomService joinRoomService = SpringContextUtil.getBean(JoinRoomService.class);
        if (!this.bool) {
            return;
        }
        int roomid = 0;
        GameRoom room = RoomManager.instance().getRoom(this.roomId);
        if (room != null && room.getSeatMap().size() < room.getTotalPlayer()) {
            roomid = this.roomId;
        } else {
            return;
        }
        boolean bool = false;
        int robotCount = 0;
        for (Seat seat : room.getSeatMap().values()) {
            if (seat.getPlayer() instanceof Robot) {
                robotCount++;
            }
        }
        if (robotCount < 3) {
            bool = true;
        }
        if (bool) {
            long userId = RandomUtil.randomLong(300) + 1;
            Robot robot = robotCacheService.getRobot(userId + 101000);
            int count = 0;
            while (robot.getRoomId() != 0) {
                if (robot.getRoomId() == room.getRoomId()) {
                    boolean flag = false;
                    for (Seat value : room.getSeatMap().values()) {
                        if (value.getPlayer() instanceof Robot && value.getPlayer().getUserId().intValue() == robot.getUserId().intValue()) {
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        robot.setRoomId(0);
                        robotCacheService.update(robot);
                    }
                }
                count++;
                userId = RandomUtil.randomLong(300) + 1;
                robot = robotCacheService.getRobot(userId + 101000);
                if (count > 300) {
                    return;
                }
            }
            if (room.getSeatMap().size() < room.getTotalPlayer()) {
                room.setPlayerNumber(room.getPlayerNumber() + 1);
                joinRoomService.joinRoom(robot, roomid);
            }
        }
    }
}
