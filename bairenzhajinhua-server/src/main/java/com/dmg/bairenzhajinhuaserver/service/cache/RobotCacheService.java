package com.dmg.bairenzhajinhuaserver.service.cache;

import com.dmg.bairenzhajinhuaserver.common.model.BaseRobot;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/1 18:47
 * @Version V1.0
 **/
@Service
public interface RobotCacheService {

    /**
     * @description: 获取机器人信息
     * @param userId
     * @return BaseRobot
     * @author mice
     * @date 2019/7/3
    */
    BaseRobot getRobot(int userId);

    /**
     * @description: 更新机器人信息
     * @param robot
     * @return BaseRobot
     * @author mice
     * @date 2019/7/3
    */
    BaseRobot update(BaseRobot robot);
}