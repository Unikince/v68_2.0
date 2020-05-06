package com.dmg.zhajinhuaserver.service;


import com.dmg.zhajinhuaserver.model.bean.Player;

/**
 * @Description
 * @Author jock
 * @Date 2019/7/9 0009
 * @Version V1.0
 **/
public interface AnswerDissolveService {
    /**
     * 处理玩家投票同意/拒绝解散房间
     *
     * @param player
     * @param agree
     */
    void answerDissolveRoom(Player player, boolean agree);
}
