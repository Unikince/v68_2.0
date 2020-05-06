package com.dmg.game.record.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.common.core.util.RedisUtil;
import com.dmg.common.core.web.BaseResultEnum;
import com.dmg.common.core.web.Result;
import com.dmg.game.record.common.constant.Constant;
import com.dmg.game.record.dao.GameRecordDao;
import com.dmg.game.record.model.GameRecordBean;
import com.dmg.game.record.model.dto.GameInfoDTO;
import com.dmg.game.record.model.dto.GameRecordQueryDTO;
import com.dmg.game.record.model.vo.GameRecordVO;
import com.dmg.game.record.service.GameRecordService;
import com.dmg.game.record.service.config.GameInfoService;
import com.dmg.server.common.util.DmgBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 11:59 2019/11/19
 */
@Slf4j
@Service
public class GameRecordServiceImpl implements GameRecordService {

    @Autowired
    private GameRecordDao gameRecordDao;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private GameInfoService gameInfoService;

    private static final Long time = Long.parseLong("5184000");

    @Override
    public Boolean insertGameRecord(GameRecordBean gameRecordBean) {
        if (gameRecordBean == null || StringUtils.isEmpty(gameRecordBean.getRoundCode()) || gameRecordBean.getUserId() == null || gameRecordBean.getGameId() == null) {
            return false;
        }
        gameRecordBean.setCreateDate(new Date());
        gameRecordBean.setModifyDate(new Date());
        gameRecordBean.setGameName(this.getGameName(gameRecordBean.getGameId()));
        gameRecordDao.insert(gameRecordBean);
        return true;
    }

    @Override
    public List<GameRecordVO> getGameRecordInfo(GameRecordQueryDTO gameRecordQueryDTO) {
        List<GameRecordBean> gameRecordBeanList = gameRecordDao.selectList(new LambdaQueryWrapper<GameRecordBean>()
                .eq(GameRecordBean::getUserId, gameRecordQueryDTO.getUserId())
                .eq(GameRecordBean::getGameId, gameRecordQueryDTO.getGameId())
                .ge(GameRecordBean::getGameDate, gameRecordQueryDTO.getStartTime())
                .le(GameRecordBean::getGameDate, gameRecordQueryDTO.getEndTime())
                .orderByDesc(GameRecordBean::getGameDate)
                .last(" LIMIT 50"));
        if (gameRecordBeanList == null || gameRecordBeanList.size() < 1) {
            return null;
        }
        return DmgBeanUtils.copyList(gameRecordBeanList, GameRecordVO.class);
    }

    private String getGameName(Integer gameId) {
        try {
            Object object = redisUtil.get(Constant.GAME_INFO + ":" + gameId);
            if (object != null) {
                return String.valueOf(object);
            }
            log.info("调用game-config-server服务查询【游戏信息】req：{}", gameId);
            Result<GameInfoDTO> result = gameInfoService.gameInfo(gameId);
            log.info("调用game-config-server服务查询【游戏信息】resp：{}", result.toString());
            if (!BaseResultEnum.SUCCESS.getCode().toString().equals(result.getCode())) {
                return null;
            }
            redisUtil.set(Constant.GAME_INFO + ":" + gameId, result.getData().getGameName(), time);
            return result.getData().getGameName();
        } catch (Exception e) {
            log.error("获取游戏名称出现异常：{}", e);
            return null;
        }
    }
}
