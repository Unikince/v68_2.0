package com.dmg.bcbm.logic.entity.bcbm;

import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lombok.Data;

/**
 * @description:
 * @author mice
 * @date 2019/7/29
*/
@Data
public class BaseRoom implements Serializable {
    private static final long serialVersionUID = 1L;

    private int roomId; // 房间id
    private int curRound; // 游戏当前局数
    private int totalRound; // 总局数
    private long startGameTime; // 开始游戏时间(毫秒)
    private long createTime; // 房间创建时间(毫秒)
    private int playerNumber; // 房间中座位上的人数
    private int totalPlayer; // 房间总座位数
    private int level;// 当前房间等级(初,中,高...)
    private int roomStatus; // 房间状态
    private long countdownTime; // 动作倒计时
    private Lock lock = new ReentrantLock();
}
