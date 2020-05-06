package com.dmg.zhajinhuaserver.service;

import com.alibaba.fastjson.JSONObject;
import com.dmg.zhajinhuaserver.model.bean.Player;

/**
 * @Description
 * @Author jock
 * @Date 2019/7/10 0010
 * @Version V1.0
 **/
public interface ChatService {
    /**
     * 聊天
     *
     * @param player
     * @param message
     */
    void chat(Player player, JSONObject message);
}
