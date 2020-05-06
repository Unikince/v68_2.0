package com.dmg.niuniuserver.manager.init;


import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.dmg.niuniuserver.model.constants.GameConfig;
import com.dmg.niuniuserver.service.config.GameFileConfigService;
import com.dmg.server.common.model.dto.GameWaterPoolDTO;
import com.dmg.server.common.util.GameOnlineChangeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.dmg.common.core.util.RedisUtil;
import com.dmg.common.core.web.BaseResultEnum;
import com.dmg.common.core.web.Result;
import com.dmg.niuniuserver.config.NiouNiou;
import com.dmg.niuniuserver.constant.Constant;
import com.dmg.niuniuserver.constant.RegionConstant;
import com.dmg.niuniuserver.manager.RoomManager;
import com.dmg.niuniuserver.manager.TimerManager;
import com.dmg.niuniuserver.manager.WorkManager;
import com.dmg.niuniuserver.model.bean.GameRoom;
import com.dmg.niuniuserver.model.dto.GameFileConfigDTO;
import com.dmg.niuniuserver.model.dto.NiuniuRobotActionDTO;
import com.dmg.niuniuserver.service.IdGeneratorService;
import com.dmg.niuniuserver.service.config.GameWaterPoolService;
import com.dmg.niuniuserver.service.config.RobotActionService;
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
    private RobotActionService robotActionService;

    @Override
    public void run(String... args) throws Exception {
        TimerManager.instance().init();
        WorkManager.instance().init(QueueType.values());
        Result<List<GameFileConfigDTO>> resultGameFile = this.gameFileConfigService.getGameFileConfigByGameId(Constant.GAME_ID);
        if (BaseResultEnum.SUCCESS.getCode().toString().equals(resultGameFile.getCode())) {
            log.info("====================>初始化房间");
            for (GameFileConfigDTO gameFileConfigDTO : resultGameFile.getData()) {
                for (int i = 1; i <= NiouNiou.FREE_FILED_NUMBER; i++) {
                    GameRoom room = new GameRoom();
                    room.setRoomId(this.idGeneratorService.getRoomId());
                    room.setTime(-1);
                    // room.setTotalBet(gameFileConfigDTO.getTotalBet());
                    room.setBaseScore(gameFileConfigDTO.getBaseScore());
                    room.setUpperLimit(gameFileConfigDTO.getUpperLimit());
                    room.setLowerLimit(gameFileConfigDTO.getLowerLimit());
                    room.setTotalPlayer(NiouNiou.FRIED_GOLDEN_FLOWER_PEOPLE_NUMBER_LIMIT);
                    room.setRoomType(GameConfig.FREE_FIELD);
                    room.setLevel(gameFileConfigDTO.getFileId());
                    room.setLevelName(gameFileConfigDTO.getFileName());
                    room.setPumpRate(gameFileConfigDTO.getPumpRate());
                    room.setReadyTime(gameFileConfigDTO.getReadyTime() * 1000);
                    room.setDiscard(Arrays.asList(gameFileConfigDTO.getDiscards().split(",")).stream().mapToInt(Integer::parseInt).toArray());
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
            log.info("初始化水池信息[完成]:{}", bol);
        }
        log.info("====================>初始化机器人动作信息");
        Result<List<NiuniuRobotActionDTO>> robotActionResult = this.robotActionService.getList();
        if (BaseResultEnum.SUCCESS.getCode().toString().equals(robotActionResult.getCode())) {
            List data = robotActionResult.getData();
            Boolean bol = this.redisUtil.lSet(RegionConstant.ROBOT_ACTION, data);
            log.info("初始化机器人动作信息[完成]:{}", bol);
        }

    }
}