package com.dmg.zhajinhuaserver.service;


import com.dmg.zhajinhuaserver.model.bean.Player;

/**
 * @Description 出牌
 * @Author jock
 * @Date 2019/7/10 0010
 * @Version V1.0
 **/
public interface StartGameService {
    /**
     * 开始游戏
     * @param roomId
     */
    void startGame(Player player, int roomId);

}
