package com.dmg.game.task.service.impl;

import com.dmg.game.task.dao.GamePumpRecordDao;
import com.dmg.game.task.model.bean.GamePumpRecordBean;
import com.dmg.game.task.service.GamePumpRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 15:39 2020/3/13
 */
@Slf4j
@Service
public class GamePumpRecordServiceImpl implements GamePumpRecordService {

    @Autowired
    private GamePumpRecordDao gamePumpRecordDao;

    @Override
    public void insert(List<GamePumpRecordBean> gamePumpRecordBeanList) {
        gamePumpRecordDao.insertBatch(gamePumpRecordBeanList);
    }
}
