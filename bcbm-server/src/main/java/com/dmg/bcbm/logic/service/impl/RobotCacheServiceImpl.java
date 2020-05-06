package com.dmg.bcbm.logic.service.impl;


import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.dmg.bcbm.core.annotation.Service;
import com.dmg.bcbm.core.config.D;
import com.dmg.bcbm.core.manager.DaoManager;
import com.dmg.bcbm.core.manager.RoomManager;
import com.dmg.bcbm.logic.dao.RobotBean;
import com.dmg.bcbm.logic.dao.RobotDao;
import com.dmg.bcbm.logic.entity.bcbm.BaseRobot;
import com.dmg.bcbm.logic.entity.bcbm.Room;
import com.dmg.bcbm.logic.service.RobotCacheService;
import com.dmg.gameconfigserverapi.dto.BairenControlConfigDTO;
import com.dmg.gameconfigserverapi.dto.BairenFileConfigDTO;
import com.dmg.gameconfigserverapi.dto.BairenGameConfigDTO;
import com.dmg.gameconfigserverapi.dto.BairenGameConfigDTO.WaterPoolConfigDTO;
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.common_server.util.RandomUtil;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/1 18:47
 * @Version V1.0
 **/
@Service
public class RobotCacheServiceImpl implements RobotCacheService {

    public BaseRobot getRobot(int roomId,int userId) {
    	RobotDao robotDao = DaoManager.instance().get(RobotDao.class);
        RobotBean robotBean = robotDao.queryRobot(userId);
        if (robotBean == null){
            return null;
        }
        Room room = RoomManager.intance().getRoomById(roomId); // 获取房间
        BairenGameConfigDTO gameConfig = room.getGameConfig().get(1); // 获取游戏配置
        if (gameConfig == null) {
			return null;
		}
		
        BaseRobot robot = new BaseRobot();
        robot.setRoleId(String.valueOf(robotBean.getUserId()));
        robot.setNickname(robotBean.getNickname());
        BigDecimal exchangeRate = gameConfig.getExchangeRate();
        BigDecimal robotGold = NumberTool.multiply(RandomUtil.getRandom(D.ROBOTGOLDMIN,D.ROBOTGOLDMAX),exchangeRate);
        robot.setGold(robotGold);
        return robot;
    }

    public BaseRobot update(BaseRobot robot){

        return robot;
    }
}