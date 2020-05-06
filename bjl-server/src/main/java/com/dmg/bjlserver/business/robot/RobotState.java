package com.dmg.bjlserver.business.robot;

/**
 * 机器人状态枚举类
 *
 * @author wh
 */
public enum RobotState {
    /**
     * 无状态
     */
    NONE,
    /**
     * 等待进入房间
     */
    WAIT_ENTER,
    /**
     * 游戏
     */
    GAME,
    /**
     * 等待退出房间
     */
    WAIT_EXIT,
    /**
     * 正在退出
     */
    DO_EXIT,
    /**
     * 已退出房间，等待释放
     */
    LOCK,
}
