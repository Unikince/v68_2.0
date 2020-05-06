package com.dmg.game.task.job;

import com.dmg.common.core.config.RedisRegionConfig;
import com.dmg.common.core.util.RedisUtil;
import com.dmg.game.task.model.bean.GamePumpRecordBean;
import com.dmg.game.task.model.vo.GameFileVO;
import com.dmg.game.task.service.GamePumpRecordService;
import com.dmg.game.task.service.cache.GameFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 15:41 2020/3/13
 */
@EnableScheduling
@Component
@Slf4j
public class GamePumpRecordJob {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private GamePumpRecordService gamePumpRecordService;

    @Autowired
    private GameFileService gameFileService;

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 15:42 2020/3/13
     **/
    @Scheduled(cron = "0 */1 * * * ?")
    public void execute() {
        log.info("==>定时器记录水池数据");
        Date recordDate = new Date();
        List<GamePumpRecordBean> gamePumpRecordBeanList = new ArrayList<>();
        List<String> keys = new ArrayList<>(redisUtil.keys(RedisRegionConfig.FILE_WIN_GOLD_KEY + ":" + "*"));
        log.info("{}", keys.toString());
        List<String> data = redisUtil.multiGet(keys);
        log.info("{}", data.toString());

        List<GameFileVO> gameFileVOList = gameFileService.getGameFile();
        if (gameFileVOList == null || gameFileVOList.size() < 1) {
            return;
        }
        gameFileVOList.forEach(gameFileVO -> {
            BigDecimal pump = BigDecimal.ZERO;
            for (int i = 0; i < keys.size() && i < data.size(); i++) {
                String[] key = keys.get(i).substring((RedisRegionConfig.FILE_WIN_GOLD_KEY + ":").length()).split("_");
                if (key == null || key.length < 2) {
                    continue;
                }
                if (key[0].equals(gameFileVO.getGameId().toString()) && key[1].equals(gameFileVO.getFileId().toString())) {
                    pump = new BigDecimal(String.valueOf(data.get(i)));
                    break;
                }
            }
            gamePumpRecordBeanList.add(getGamePumpRecordBean(recordDate, gameFileVO, pump));
        });
        if (gamePumpRecordBeanList != null && gamePumpRecordBeanList.size() > 0) {
            gamePumpRecordService.insert(gamePumpRecordBeanList);
        }
    }

    private GamePumpRecordBean getGamePumpRecordBean(Date recordDate, GameFileVO gameFileVO, BigDecimal pump) {
        GamePumpRecordBean gamePumpRecordBean = GamePumpRecordBean.builder()
                .recordDate(recordDate)
                .pump(pump.setScale(2, BigDecimal.ROUND_DOWN))
                .fileId(gameFileVO.getFileId())
                .gameName(gameFileVO.getGameName())
                .fileName(gameFileVO.getFileName())
                .gameId(gameFileVO.getGameId()).build();
        gamePumpRecordBean.setCreateDate(new Date());
        gamePumpRecordBean.setModifyDate(new Date());
        return gamePumpRecordBean;
    }

}
