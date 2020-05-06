package com.dmg.game.task.service.impl;

import com.dmg.game.task.dao.GameOnlineRecordDao;
import com.dmg.game.task.model.bean.GameOnlineRecordBean;
import com.dmg.game.task.service.GameOnlineRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:27 2020/3/16
 */
@Service
public class GameOnlineRecordServiceImpl implements GameOnlineRecordService {

    @Autowired
    private GameOnlineRecordDao gameOnlineRecordDao;

    @Override
    public void insert(List<GameOnlineRecordBean> gameOnlineRecordBeanList) {
        gameOnlineRecordDao.insertBatch(gameOnlineRecordBeanList);
    }
}
