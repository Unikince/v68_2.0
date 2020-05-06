package com.dmg.zhajinhuaserver.service.cache;

import com.dmg.zhajinhuaserver.model.bean.Robot;
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
     * @param userId
     * @return Robot
     * @description: 获取机器人信息
     * @author mice
     * @date 2019/7/3
     */
    Robot getRobot(Long userId, int grade);

    /**
     * @param robot
     * @return Robot
     * @description: 更新机器人信息
     * @author mice
     * @date 2019/7/3
     */
    Robot update(Robot robot);
}