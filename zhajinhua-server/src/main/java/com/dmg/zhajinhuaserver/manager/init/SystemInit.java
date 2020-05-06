package com.dmg.zhajinhuaserver.manager.init;

import java.util.Arrays;
import java.util.List;

import com.dmg.server.common.model.dto.GameWaterPoolDTO;
import com.dmg.server.common.util.GameOnlineChangeUtils;
import com.dmg.zhajinhuaserver.service.config.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.dmg.common.core.util.RedisUtil;
import com.dmg.common.core.web.BaseResultEnum;
import com.dmg.common.core.web.Result;
import com.dmg.zhajinhuaserver.config.D;
import com.dmg.zhajinhuaserver.config.RoomConfig;
import com.dmg.zhajinhuaserver.common.constant.Constant;
import com.dmg.zhajinhuaserver.common.constant.RegionConstant;
import com.dmg.zhajinhuaserver.manager.RoomManager;
import com.dmg.zhajinhuaserver.manager.TimerManager;
import com.dmg.zhajinhuaserver.manager.WorkManager;
import com.dmg.zhajinhuaserver.model.bean.GameRoom;
import com.dmg.zhajinhuaserver.model.dto.GameFileConfigDTO;
import com.dmg.zhajinhuaserver.model.dto.RobotActionDTO;
import com.dmg.zhajinhuaserver.model.dto.RobotProbabilityDTO;
import com.dmg.zhajinhuaserver.model.dto.RobotSeeDTO;
import com.dmg.zhajinhuaserver.service.IdGeneratorService;
import com.zyhy.common_server.constants.QueueType;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/1 17:11
 * @Version V1.0
 **/
@Slf4j
@Component
public class SystemInit implements CommandLineRunner {

    @Autowired
    private IdGeneratorService idGeneratorService;

    @Autowired
    private GameFileConfigService gameFileConfigService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private GameWaterPoolService gameWaterPoolService;

    @Autowired
    private RobotActionConfigService robotActionConfigService;

    @Autowired
    private RobotProbabilityService robotProbabilityService;

    @Autowired
    private RobotSeeService robotSeeService;

    @Override
    public void run(String... args) throws Exception {
        TimerManager.instance().init();
        WorkManager.instance().init(QueueType.values());
        Result<List<GameFileConfigDTO>> resultGameFile = this.gameFileConfigService.getGameFileConfigByGameId(Constant.GAME_ID);
        if (BaseResultEnum.SUCCESS.getCode().toString().equals(resultGameFile.getCode())) {
            log.info("====================>初始化房间");
            for (GameFileConfigDTO gameFileConfigDTO : resultGameFile.getData()) {
                for (int i = 1; i <= RoomConfig.FREE_FILED_NUMBER; i++) {
                    GameRoom room = new GameRoom();
                    room.setRoomId(this.idGeneratorService.getRoomId());
                    room.setTime(-1);
                    // room.setTotalBet(gameFileConfigDTO.getTotalBet());
                    room.setBaseScore(gameFileConfigDTO.getBaseScore().intValue());
                    room.setUpperLimit(gameFileConfigDTO.getUpperLimit());
                    room.setLowerLimit(gameFileConfigDTO.getLowerLimit());
                    room.setTotalPlayer(RoomConfig.FRIED_GOLDEN_FLOWER_PEOPLE_NUMBER_LIMIT);
                    room.setGameTypeId(RoomConfig.FRIDE_GOLDEN_FLOWER_GAME_TYPE);
                    room.setGameRoomTypeId(D.FREE_FIELD);
                    room.setGrade(gameFileConfigDTO.getFileId());
                    room.setGradeName(gameFileConfigDTO.getFileName());
                    room.setCountTurns(gameFileConfigDTO.getRoundMax());
                    room.setPumpRate(gameFileConfigDTO.getPumpRate());
                    room.setReadyTime(gameFileConfigDTO.getReadyTime() * 1000);
                    room.setBetMul(Arrays.asList(gameFileConfigDTO.getBetChips().split(",")).stream().mapToInt(Integer::parseInt).toArray());
                    RoomManager.instance().addQuickRooom(room);
                }
                GameOnlineChangeUtils.initOnlineNum(Constant.GAME_ID, gameFileConfigDTO.getFileId());
            }
            log.info("初始化房间信息【完成】");
        }
        log.info("====================>初始化水池");
        Result<List<GameWaterPoolDTO>> result = this.gameWaterPoolService.getInfoByWater(Constant.GAME_ID);
        if (BaseResultEnum.SUCCESS.getCode().toString().equals(result.getCode())) {
            List data = result.getData();
            Boolean bol = this.redisUtil.lSet(RegionConstant.GAME_WATER_POOL + ":" + Constant.GAME_ID, data);
            log.info("初始化水池信息【完成】:{}", bol);
        }
        log.info("====================>初始化机器人动作");
        Result<List<RobotActionDTO>> resultRobotAction = this.robotActionConfigService.getList();
        if (BaseResultEnum.SUCCESS.getCode().toString().equals(resultRobotAction.getCode())) {
            List data = resultRobotAction.getData();
            Boolean bol = this.redisUtil.lSet(RegionConstant.ROBOT_ACTION, data);
            log.info("初始化机器人动作信息【完成】:{}", bol);
        }
        log.info("====================>初始化机器人基础概率");
        Result<List<RobotProbabilityDTO>> resultRobotProbability = this.robotProbabilityService.getList();
        if (BaseResultEnum.SUCCESS.getCode().toString().equals(resultRobotProbability.getCode())) {
            List data = resultRobotProbability.getData();
            Boolean bol = this.redisUtil.lSet(RegionConstant.ROBOT_PROBABILITY, data);
            log.info("初始化机器人基础概率信息【完成】:{}", bol);
        }
        log.info("====================>初始化是否看牌概率");
        Result<List<RobotSeeDTO>> resultRobotSee = this.robotSeeService.getList();
        if (BaseResultEnum.SUCCESS.getCode().toString().equals(resultRobotSee.getCode())) {
            List data = resultRobotSee.getData();
            Boolean bol = this.redisUtil.lSet(RegionConstant.ROBOT_SEE, data);
            log.info("初始化机器人是否看牌概率信息【完成】:{}", bol);
        }
    }
}