package com.dmg.bairenniuniuserver.service.cache;

import com.dmg.bairenniuniuserver.common.model.BasePlayer;
import com.dmg.data.common.dto.BetRecvDto;
import com.dmg.data.common.dto.BetSendDto;
import com.dmg.data.common.dto.SettleRecvDto;
import com.dmg.data.common.dto.SettleSendDto;

public interface PlayerService {

    public int getRoomLevel(long playerId);

    public BasePlayer getPlayerPlatform(long playerId);

    BetRecvDto bet(BetSendDto betSendDto);

    SettleRecvDto settle(SettleSendDto sendDto);

    public void syncRoom(int roomId, long userId,int roomLevel);

    public BasePlayer getPlayer(long playerId);

    public BasePlayer updatePlayer(BasePlayer player);
}