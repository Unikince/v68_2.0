package com.dmg.fish.business.handler.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dmg.fish.business.logic.FishMgr;
import com.dmg.fish.business.logic.FishMsgMgr;
import com.dmg.fish.business.model.room.Seat;
import com.dmg.fish.core.msg.ReqPbMessageHandler;
import com.google.protobuf.Message;

/**
 * 玩家进入恢复游戏场景(本身是不需要的,客户端有资源加载的过程,如果进入房间后发送场景数据客户端资源还未加载完资源)
 */
@Component("201115")
public final class ReqRestoreHandler implements ReqPbMessageHandler {

    @Autowired
    private FishMsgMgr fishMsgMgr;
    @Autowired
    private FishMgr fishMgr;

    @Override
    public void action(Object player, Message message) {
        Seat seat = this.fishMgr.getPlayerSeat((Long) player);
        seat.ready = true;
        seat.preFireTime = System.currentTimeMillis();
        this.fishMsgMgr.sendRestoreMsg(seat);
    }

}
