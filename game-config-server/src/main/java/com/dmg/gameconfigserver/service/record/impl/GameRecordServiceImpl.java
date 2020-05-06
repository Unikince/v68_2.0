package com.dmg.gameconfigserver.service.record.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.gameconfigserver.dao.record.GameRecordDao;
import com.dmg.gameconfigserver.model.bean.record.GameRecordBean;
import com.dmg.gameconfigserver.model.dto.record.GameRecordDTO;
import com.dmg.gameconfigserver.service.record.GameRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 15:01 2019/11/20
 */
@Service
public class GameRecordServiceImpl implements GameRecordService {

    @Autowired
    private GameRecordDao gameRecordDao;

    @Override
    public IPage<GameRecordBean> getGameRecord(GameRecordDTO gameRecordDTO) {
        Page<GameRecordBean> pageParam = new Page<>(gameRecordDTO.getCurrent(), gameRecordDTO.getSize()); // 当前页码，每页条数
        LambdaQueryWrapper<GameRecordBean> query = new LambdaQueryWrapper<GameRecordBean>()
                .eq(GameRecordBean::getGameId, gameRecordDTO.getGameId())
                .orderByDesc(GameRecordBean::getGameDate);
        if (gameRecordDTO.getUserId() != null) {
            query.eq(GameRecordBean::getUserId, gameRecordDTO.getUserId());
        }
        query.eq(GameRecordBean::getIsRobot, false);
        if (gameRecordDTO.getStartDate() != null) {
            query.ge(GameRecordBean::getGameDate, gameRecordDTO.getStartDate());
        }
        if (gameRecordDTO.getEndDate() != null) {
            query.le(GameRecordBean::getGameDate, gameRecordDTO.getEndDate());
        }
        IPage<GameRecordBean> pageResult = gameRecordDao.selectPage(pageParam, query);
        return pageResult;
    }

    @Override
    public BigDecimal getTodayServiceCharge() {
        BigDecimal data = gameRecordDao.getTodayServiceCharge();
        return data == null ? BigDecimal.ZERO : data;
    }

    @Override
    public BigDecimal getTodayProfit() {
        BigDecimal data = gameRecordDao.getTodayProfit();
        return data == null ? BigDecimal.ZERO : data;
    }
}
