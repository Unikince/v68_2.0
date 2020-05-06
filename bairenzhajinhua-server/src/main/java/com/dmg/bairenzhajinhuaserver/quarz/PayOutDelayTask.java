package com.dmg.bairenzhajinhuaserver.quarz;

import java.math.BigDecimal;
import java.util.LinkedList;

import com.dmg.bairenzhajinhuaserver.common.utils.SpringContextUtil;
import com.dmg.bairenzhajinhuaserver.common.work.DelayTimeWork;
import com.dmg.bairenzhajinhuaserver.manager.RoomManager;
import com.dmg.bairenzhajinhuaserver.model.Room;
import com.dmg.bairenzhajinhuaserver.service.cache.PlayerService;

public class PayOutDelayTask extends DelayTimeWork {
    private int roomId;
    private long id;
    private double gold;
    private BigDecimal betChip;
    LinkedList<Object> allTablePokerList;
    LinkedList<Object> winTableList;


    @Override
    public void init(Object... args) {
        this.roomId = (int) args[0];
        this.id = (int) args[1];
        this.gold = (double) args[2];
        this.betChip = (BigDecimal) args[3];
        this.allTablePokerList = (LinkedList<Object>) args[4];
        this.winTableList = (LinkedList<Object>) args[5];
    }

    @Override
    public void go() {
       /* PlayerService service = SpringContextUtil.getBean(PlayerService.class);
        Room room = RoomManager.intance().getRoom(this.roomId);
        service.increaseGold(room.getRoomId(), room.getCurRound(), this.gold,  this.id, this.betChip.doubleValue(),allTablePokerList,winTableList);
    */


    }
}
