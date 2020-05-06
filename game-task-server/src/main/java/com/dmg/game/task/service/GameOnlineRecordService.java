package com.dmg.game.task.service;

import com.dmg.game.task.model.bean.GameOnlineRecordBean;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 15:39 2020/3/13
 */
public interface GameOnlineRecordService {
    
    /**
     * @Author liubo
     * @Description //TODO 
     * @Date 15:47 2020/3/13
     **/
    void insert(List<GameOnlineRecordBean> gameOnlineRecordBeanList);
}
