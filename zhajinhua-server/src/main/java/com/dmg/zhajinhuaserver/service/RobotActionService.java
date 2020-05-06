package com.dmg.zhajinhuaserver.service;

import com.dmg.zhajinhuaserver.model.bean.GameRoom;
import com.dmg.zhajinhuaserver.model.bean.Seat;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 15:08 2019/9/30
 */
public interface RobotActionService {

    /**
     * @Author liubo
     * @Description //TODO 获取机器人操作
     * @Date 15:08 2019/9/30
     **/
    int getRobotAction(GameRoom room, Seat seat);

    /**
     * @Author liubo
     * @Description //TODO 获取机器人是否看牌
     * @Date 15:08 2019/9/30
     **/
    Boolean getRobotActionIsSee(GameRoom room, Seat seat);
}
