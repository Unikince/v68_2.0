package com.dmg.zhajinhuaserver.manager.work.delay;

import cn.hutool.core.util.RandomUtil;
import com.dmg.zhajinhuaserver.SpringContextUtil;
import com.dmg.zhajinhuaserver.manager.RoomManager;
import com.dmg.zhajinhuaserver.model.bean.GameRoom;
import com.dmg.zhajinhuaserver.model.bean.Robot;
import com.dmg.zhajinhuaserver.model.bean.Seat;
import com.dmg.zhajinhuaserver.service.JoinRoomService;
import com.dmg.zhajinhuaserver.service.cache.RobotCacheService;
import com.zyhy.common_server.work.DelayTimeWork;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
        if (!bool) {
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
        RobotCacheService robotCacheService = SpringContextUtil.getBean(RobotCacheService.class);
        if (bool) {
            Long roleId = RandomUtil.randomLong(300) + 1;
            Robot robot = robotCacheService.getRobot(roleId + 101000L, room.getGrade());
            int count = 0;
            while (robot.getRoomId() != 0) {
                count++;
                roleId = RandomUtil.randomLong(300) + 1 + 101000;
                robot = robotCacheService.getRobot(roleId, room.getGrade());
                if (count > 300) {
                    log.info("机器人耗尽了");
                    return;
                }
            }
            // 是否添加到房间
            if (room.getSeatMap().size() < room.getTotalPlayer()) {
                // 初始化机器人行为概率
                //RobotAutoActionLogic.initRobotActionLv(robot);
                // 放入房间
                room.setPlayerNumber(room.getSeatMap().size() + 1);
                JoinRoomService joinRoomService = SpringContextUtil.getBean(JoinRoomService.class);
                joinRoomService.joinRoom(robot, roomid);
            } else {
                log.info("房间满了");
            }
        }
    }
}
