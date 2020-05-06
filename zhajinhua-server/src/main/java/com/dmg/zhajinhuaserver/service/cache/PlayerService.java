package com.dmg.zhajinhuaserver.service.cache;

import java.math.BigDecimal;

import com.dmg.data.common.dto.BetRecvDto;
import com.dmg.zhajinhuaserver.model.bean.Player;

public interface PlayerService {

    Player getPlayerPlatform(long playerId);

    Player getPlayer(Long userId);

    Player update(Player player);

    /**
     * @Author liubo
     * @Description //TODO 减少金币
     * @Date 14:08 2019/11/7
     **/
    BetRecvDto decreaseGold(BigDecimal changeGold, long playerId);

    void increaseGold(BigDecimal changeGold, long playerId);

    void syncRoom(int roomId, long userId);

}