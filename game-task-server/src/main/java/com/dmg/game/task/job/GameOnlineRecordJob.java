package com.dmg.game.task.job;

import com.dmg.common.core.config.RedisRegionConfig;
import com.dmg.common.core.util.RedisUtil;
import com.dmg.game.task.model.bean.GameOnlineRecordBean;
import com.dmg.game.task.model.vo.ConditionDetailVO;
import com.dmg.game.task.service.GameOnlineRecordService;
import com.dmg.game.task.service.cache.GameFileService;
import com.dmg.server.common.enums.PlaceEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 9:48 2020/3/16
 */
@EnableScheduling
@Component
@Slf4j
public class GameOnlineRecordJob {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private GameOnlineRecordService gameOnlineRecordService;

    @Autowired
    private GameFileService gameFileService;

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 15:42 2020/3/13
     **/
    @Scheduled(cron = "0 */1 * * * ?")
    public void execute() {
        log.info("==>定时器记录在线数据");
        Date recordDate = new Date();
        List<GameOnlineRecordBean> gameOnlineRecordBeanList = new ArrayList<>();
        List<String> keys = new ArrayList<>(redisUtil.keys(RedisRegionConfig.ONLINE_NUM_KEY + ":" + "*"));
        log.info("{}", keys.toString());
        List<String> data = redisUtil.multiGet(keys);
        log.info("{}", data.toString());

        List<ConditionDetailVO> gameFileVOList = gameFileService.getCondition();
        if (gameFileVOList == null || gameFileVOList.size() < 1) {
            return;
        }
        GameOnlineRecordBean lobby = null;
        Integer online = 0;
        for (ConditionDetailVO gameFileVO : gameFileVOList) {
            if (gameFileVO.getFiles() != null && gameFileVO.getFiles().size() > 0) {
                for (ConditionDetailVO.Detail file : gameFileVO.getFiles()) {
                    String value = "0";
                    for (int i = 0; i < keys.size() && i < data.size(); i++) {
                        String[] key = keys.get(i).substring((RedisRegionConfig.ONLINE_NUM_KEY + ":").length()).split("_");
                        if (key == null || key.length < 2) {
                            continue;
                        }
                        if (key[0].equals(gameFileVO.getCode().toString()) && key[1].equals(file.getCode().toString())) {
                            value = data.get(i);
                            break;
                        }
                    }
                    online += Integer.parseInt(value);
                    gameOnlineRecordBeanList.add(getGameOnlineRecordBean(recordDate, gameFileVO, file, value));
                }
            } else {
                String value = "0";
                for (int i = 0; i < keys.size() && i < data.size(); i++) {
                    String[] key = keys.get(i).substring((RedisRegionConfig.ONLINE_NUM_KEY + ":").length()).split("_");
                    if (key == null || key.length != 1) {
                        continue;
                    }
                    if (key[0].equals(gameFileVO.getCode().toString())) {
                        value = data.get(i);
                        break;
                    }
                }
                //处理大厅
                if (gameFileVO.getCode().intValue() == PlaceEnum.CODE_LOBBY.getCode().intValue()) {
                    lobby = getGameOnlineRecordBean(recordDate, gameFileVO, null, value);
                } else {
                    online += Integer.parseInt(value);
                    gameOnlineRecordBeanList.add(getGameOnlineRecordBean(recordDate, gameFileVO, null, value));
                }
            }
        }
        lobby.setOnlineNum(lobby.getOnlineNum() - online);
        gameOnlineRecordBeanList.add(lobby);
        if (gameOnlineRecordBeanList != null && gameOnlineRecordBeanList.size() > 0) {
            gameOnlineRecordService.insert(gameOnlineRecordBeanList);
        }
    }

    private GameOnlineRecordBean getGameOnlineRecordBean(Date recordDate, ConditionDetailVO gameFileVO, ConditionDetailVO.Detail detail, String value) {
        GameOnlineRecordBean gameOnlineRecordBean = GameOnlineRecordBean.builder()
                .recordDate(recordDate)
                .onlineNum(new Integer(value))
                .fileId(detail == null ? null : detail.getCode())
                .placeName(gameFileVO.getValue())
                .fileName(detail == null ? null : detail.getValue())
                .place(gameFileVO.getCode()).build();
        gameOnlineRecordBean.setCreateDate(new Date());
        gameOnlineRecordBean.setModifyDate(new Date());
        return gameOnlineRecordBean;
    }

}
