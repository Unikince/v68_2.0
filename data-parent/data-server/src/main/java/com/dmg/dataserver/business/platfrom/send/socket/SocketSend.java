package com.dmg.dataserver.business.platfrom.send.socket;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dmg.dataserver.common.platfrom.SocketClient;

@Service
public class SocketSend {
    @Autowired
    private SocketClient socket;

    /**
     * 修改金币
     *
     * @param gameId 游戏id
     * @param userId 用户id
     * @param decGold 改变的金币
     */
    public void changeGold(int type, long userId, BigDecimal changeGold) {
        JSONObject data = new JSONObject();
        data.put("type", type);
        data.put("userId", userId);
        data.put("gold", changeGold);
        this.socket.send("0001", data);
    }
}
