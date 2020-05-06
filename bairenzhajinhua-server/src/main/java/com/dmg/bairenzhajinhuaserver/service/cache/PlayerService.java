package com.dmg.bairenzhajinhuaserver.service.cache;

import com.dmg.bairenzhajinhuaserver.common.model.BasePlayer;
import com.dmg.data.common.dto.BetSendDto;
import com.dmg.data.common.dto.BetRecvDto;
import com.dmg.data.common.dto.SettleRecvDto;
import com.dmg.data.common.dto.SettleSendDto;

public interface PlayerService {

    int getRoomLevel(long playerId);

    BasePlayer getPlayerPlatform(long playerId);

    BetRecvDto bet(BetSendDto betSendDto);

    SettleRecvDto settle(SettleSendDto sendDto);

    void syncRoom(int roomId, long userId,int roomLevel);

    BasePlayer getPlayer(long playerId);

    BasePlayer updatePlayer(BasePlayer player);
}