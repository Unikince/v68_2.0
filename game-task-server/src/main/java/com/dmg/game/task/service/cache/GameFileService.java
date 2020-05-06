package com.dmg.game.task.service.cache;

import com.alibaba.fastjson.JSON;
import com.dmg.common.core.config.RedisRegionConfig;
import com.dmg.common.core.util.RedisUtil;
import com.dmg.common.core.web.BaseResultEnum;
import com.dmg.common.core.web.Result;
import com.dmg.game.task.model.vo.ConditionDetailVO;
import com.dmg.game.task.model.vo.GameFileVO;
import com.dmg.game.task.service.config.GameInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 17:53 2020/3/13
 */
@Slf4j
@Component
public class GameFileService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private GameInfoService gameInfoService;

    private static final Long time = Long.parseLong("5184000");


    public List<GameFileVO> getGameFile() {
        try {
            Object object = redisUtil.get(RedisRegionConfig.GAME_FILE_INFO);
            if (object != null) {
                return JSON.parseArray(object.toString(), GameFileVO.class);
            }
            log.info("调用game-config-server服务查询【游戏场次信息】");
            Result<List<GameFileVO>> result = gameInfoService.gameFile();
            log.info("调用game-config-server服务查询【游戏场次信息】resp：{}", result.toString());
            if (!BaseResultEnum.SUCCESS.getCode().toString().equals(result.getCode())) {
                return null;
            }
            redisUtil.set(RedisRegionConfig.GAME_FILE_INFO, JSON.toJSON(result.getData()).toString(), time);
            return result.getData();
        } catch (Exception e) {
            log.error("获取游戏场次信息出现异常：{}", e);
            return null;
        }
    }

    public List<ConditionDetailVO> getCondition() {
        try {
            Object object = redisUtil.get(RedisRegionConfig.ONLINE_GAME_FILE_INFO);
            if (object != null) {
                return JSON.parseArray(object.toString(), ConditionDetailVO.class);
            }
            log.info("调用game-config-server服务查询【游戏实时在线场次信息】");
            Result<List<ConditionDetailVO>> result = gameInfoService.getCondition();
            log.info("调用game-config-server服务查询【游戏实时在线场次信息】resp：{}", result.toString());
            if (!BaseResultEnum.SUCCESS.getCode().toString().equals(result.getCode())) {
                return null;
            }
            redisUtil.set(RedisRegionConfig.ONLINE_GAME_FILE_INFO, JSON.toJSON(result.getData()).toString(), time);
            return result.getData();
        } catch (Exception e) {
            log.error("获取游戏实时在线场次信息出现异常：{}", e);
            return null;
        }
    }
}
