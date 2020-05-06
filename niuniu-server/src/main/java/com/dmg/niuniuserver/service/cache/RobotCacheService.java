package com.dmg.niuniuserver.service.cache;

import com.dmg.niuniuserver.model.bean.Robot;
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
     * @return Robot
     * @author mice
     * @date 2019/7/3
    */
    Robot getRobot(Long userId);

    /**
     * @description: 更新机器人信息
     * @param robot
     * @return Robot
     * @author mice
     * @date 2019/7/3
    */
    Robot update(Robot robot);
}