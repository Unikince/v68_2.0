package com.dmg.niuniuserver.service;

import com.dmg.niuniuserver.model.bean.GameRoom;
import com.dmg.niuniuserver.model.bean.Seat;

/**
 * @Description 动作处理
 * @Author mice
 * @Date 2019/7/2 19:13
 * @Version V1.0
 **/

public interface PlayActionService {

    /**
     * @description: 开牌操作
     * @param gameRoom
     * @param seat
     * @param cmd
     * @return void
     * @author mice
     * @date 2019/7/2
    */
    void kaiPai(GameRoom gameRoom, Seat seat, String cmd);

    /**
     * @description: 下注操作
     * @param seat
     * @param multiple
     * @param room
     * @return void
     * @author mice
     * @date 2019/7/2
    */
    void xiaZhu(Seat seat, int multiple, GameRoom room);

    /**
     * @description: 抢庄操作
     * @param seat
     * @param multiple
     * @return void
     * @author mice
     * @date 2019/7/2
    */
    void qiangZhuang(Seat seat, int multiple);

}