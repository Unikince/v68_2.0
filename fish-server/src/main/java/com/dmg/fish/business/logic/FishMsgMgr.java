package com.dmg.fish.business.logic;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmg.fish.business.config.dic.FishRoomDic;
import com.dmg.fish.business.config.wrapper.FishRoomWrapper;
import com.dmg.fish.business.model.FishScence;
import com.dmg.fish.business.model.fish.Fish;
import com.dmg.fish.business.model.room.Seat;
import com.dmg.fish.business.model.room.Table;
import com.dmg.fish.business.platform.model.Player;
import com.dmg.fish.business.platform.service.PlayerService;
import com.dmg.fish.core.msg.MessagePush;
import com.google.protobuf.Message;

/**
 * 返回逻辑
 */
@Service
public class FishMsgMgr {
    @Autowired
    private MessagePush serverHandler;
    @Autowired
    private FishRoomDic roomDic;
    @Autowired
    private PlayerService playerService;

    /**
     * 发送心跳
     */
    public void sendHeartbeat(Long playerId) {
        com.dmg.common.pb.java.Fish.ResHeartbeat.Builder msg = com.dmg.common.pb.java.Fish.ResHeartbeat.newBuilder();
        this.serverHandler.writeMsg(playerId, com.dmg.common.pb.java.Fish.FishMessageId.ResHeartbeat_ID_VALUE, msg.build());
    }

    /**
     * 座位信息
     *
     * @param seat
     * @return
     */
    private com.dmg.common.pb.java.Fish.SeatInfo seatInfo(Seat seat) {
        com.dmg.common.pb.java.Fish.SeatInfo.Builder seatInfo = com.dmg.common.pb.java.Fish.SeatInfo.newBuilder();
        seatInfo.setBatteryScore(this.changeGold(seat.batteryScore));
        int batteryScoreIndex = this.roomDic.get(seat.table.room.id).getBatteryScoresList().indexOf(seat.batteryScore);
        seatInfo.setBatteryScoreIndex(batteryScoreIndex + 1);
        seatInfo.setOrder(seat.order);
        seatInfo.setOnline(seat.online);
        seatInfo.setGold(this.changeGold(seat.gold));
        if (seat.icon != null) {
            seatInfo.setIcon(seat.icon);
        }
        seatInfo.setNickName(seat.nickName);
        seatInfo.setPlayerId(seat.playerId);
        seatInfo.setSex(seat.sex);
        return seatInfo.build();
    }

    /**
     * 发送房间消息
     *
     * @param playerId
     */
    public void sendRoomsMsg(Long playerId) {
        com.dmg.common.pb.java.Fish.ResRooms.Builder msg = com.dmg.common.pb.java.Fish.ResRooms.newBuilder();
        for (FishRoomWrapper roomConfig : this.roomDic.values()) {
            if (!roomConfig.isOpen()) {
                continue;
            }
            com.dmg.common.pb.java.Fish.RoomInfo.Builder roomInfo = com.dmg.common.pb.java.Fish.RoomInfo.newBuilder();
            roomInfo.setId(roomConfig.getId());
            roomInfo.setName(roomConfig.getName());
            roomInfo.setKickTime(roomConfig.getKickTime());
            roomInfo.setGoldLimitLower(this.changeGold(roomConfig.getGoldLimitLower()));
            roomInfo.setGoldLimitUpper(this.changeGold(roomConfig.getGoldLimitUpper()));
            roomInfo.setBatteryScoreLower(this.changeGold(roomConfig.getBatteryScoresTree().first()));
            roomInfo.setBatteryScoreUpper(this.changeGold(roomConfig.getBatteryScoresTree().last()));
            msg.addRooms(roomInfo.build());
        }
        com.dmg.common.pb.java.Fish.PlayerInfo.Builder playerInfo = com.dmg.common.pb.java.Fish.PlayerInfo.newBuilder();
        Player player = this.playerService.getPlayerPlatform(playerId);
        playerInfo.setId(player.getId());
        playerInfo.setName(player.getNickname());
        playerInfo.setGold(this.changeGold(player.getGold()));
        if (StringUtils.isNotBlank(player.getHeadImg())) {
            playerInfo.setAvatar(player.getHeadImg());
        }
        playerInfo.setSex(player.getSex());
        msg.setPlayerInfo(playerInfo);
        this.serverHandler.writeMsg(playerId, com.dmg.common.pb.java.Fish.FishMessageId.ResRooms_ID_VALUE, msg.build());
    }

    /**
     * 检查是否进入房间
     */
    public void sendCheckRoomMsg(Long playerId, boolean flag) {
        com.dmg.common.pb.java.Fish.ResCheckRoom.Builder msg = com.dmg.common.pb.java.Fish.ResCheckRoom.newBuilder();
        msg.setFlag(flag);
        this.serverHandler.writeMsg(playerId, com.dmg.common.pb.java.Fish.FishMessageId.ResCheckRoom_ID_VALUE, msg.build());
    }

    /**
     * 发送进入房间结果消息
     *
     * @param seat
     */
    public void sendEnterRoomMsg(Seat seat, com.dmg.common.pb.java.Fish.FishCode code) {
        com.dmg.common.pb.java.Fish.ResEnterRoom.Builder msg = com.dmg.common.pb.java.Fish.ResEnterRoom.newBuilder();
        msg.setRoomId(seat.table.room.id);
        msg.setCode(code);
        this.serverHandler.writeMsg(seat.playerId, com.dmg.common.pb.java.Fish.FishMessageId.ResEnterRoom_ID_VALUE, msg.build());
    }

    /**
     * 发送进入房间结果消息
     *
     * @param seat
     */
    public void sendEnterRoomMsg(int roomId, long playerId) {
        com.dmg.common.pb.java.Fish.ResEnterRoom.Builder msg = com.dmg.common.pb.java.Fish.ResEnterRoom.newBuilder();
        msg.setRoomId(roomId);
        msg.setCode(com.dmg.common.pb.java.Fish.FishCode.FULL_PLAYER);
        this.serverHandler.writeMsg(playerId, com.dmg.common.pb.java.Fish.FishMessageId.ResEnterRoom_ID_VALUE, msg.build());
    }

    /**
     * 发送退出桌子消息
     *
     * @param seat
     */
    public void sendExitRoomMsg(Seat seat) {
        com.dmg.common.pb.java.Fish.ResExitRoom.Builder msg = com.dmg.common.pb.java.Fish.ResExitRoom.newBuilder();
        msg.setPlayerId(seat.playerId);

        List<Seat> seats = seat.table.seats;
        for (int i = 0; i < seats.size(); i++) {
            Seat s = seats.get(i);
            if ((s.playerId > 0) && s.ready) {
                this.serverHandler.writeMsg(s.playerId, com.dmg.common.pb.java.Fish.FishMessageId.ResExitRoom_ID_VALUE, msg.build());
            }
        }
    }

    /**
     * 发送其他人进入桌子消息
     *
     * @param seat
     */
    public void sendOtherEnterTableMsg(Seat seat) {
        com.dmg.common.pb.java.Fish.ResOtherEnterTable.Builder msg = com.dmg.common.pb.java.Fish.ResOtherEnterTable.newBuilder();
        msg.setSeat(this.seatInfo(seat));
        for (Seat s : seat.table.seats) {
            if ((s.playerId > 0) && (s.playerId != seat.playerId)) {
                this.serverHandler.writeMsg(s.playerId, com.dmg.common.pb.java.Fish.FishMessageId.ResOtherEnterTable_ID_VALUE, msg.build());
            }
        }
    }

    /**
     * 发送开炮结果消息
     *
     * @param seat
     * @param bulletId
     * @param angle
     */
    public void sendFireMsg(Seat seat, int bulletId, int angle, int fishId) {
        com.dmg.common.pb.java.Fish.ResFire.Builder msg = com.dmg.common.pb.java.Fish.ResFire.newBuilder();
        msg.setPlayerId(seat.playerId);
        msg.setAngle(angle);
        msg.setBulletId(bulletId);
        msg.setFishId(fishId);

        this.sendTablePlayersMsg(seat.table, com.dmg.common.pb.java.Fish.FishMessageId.ResFire_ID_VALUE, msg.build());
    }

    /**
     * 发送切换炮台消息
     *
     * @param seat
     */
    public void sendBatteryChangeMsg(Seat seat) {
        com.dmg.common.pb.java.Fish.ResBatteryChange.Builder msg = com.dmg.common.pb.java.Fish.ResBatteryChange.newBuilder();
        msg.setScore(this.changeGold(seat.batteryScore));
        int batteryScoreIndex = this.roomDic.get(seat.table.room.id).getBatteryScoresList().indexOf(seat.batteryScore);
        msg.setScoreIndex(batteryScoreIndex + 1);
        msg.setPlayerId(seat.playerId);

        this.sendTablePlayersMsg(seat.table, com.dmg.common.pb.java.Fish.FishMessageId.ResBatteryChange_ID_VALUE, msg.build());
    }

    /**
     * 发送子弹打死鱼消息
     *
     * @param seat
     * @param fishScores 被打死的鱼分数(key：鱼id,val:鱼的分数)
     */
    public void sendDieMsg(Seat seat, Map<Integer, Long> fishScores) {
        com.dmg.common.pb.java.Fish.ResDie.Builder msg = com.dmg.common.pb.java.Fish.ResDie.newBuilder();
        FishScence scence = seat.table.scence();
        msg.setPlayerId(seat.playerId);

        for (Entry<Integer, Long> diedFishScore : fishScores.entrySet()) {
            msg.addFishsBuilder().setFishId(diedFishScore.getKey()).setScore(this.changeGold(diedFishScore.getValue())).setKind(scence.fishs.get(diedFishScore.getKey()).fishId);
        }

        this.sendTablePlayersMsg(seat.table, com.dmg.common.pb.java.Fish.FishMessageId.ResDie_ID_VALUE, msg.build());
    }

    /**
     * 向玩家发送游戏恢复消息
     *
     * @param seat
     */
    public void sendRestoreMsg(Seat seat) {
        Table table = seat.table;
        FishScence scence = table.scence();
        com.dmg.common.pb.java.Fish.ResRestore.Builder msg = com.dmg.common.pb.java.Fish.ResRestore.newBuilder();
        msg.setScenceId(scence.id);
        for (Fish fish : scence.fishs.values()) {
            if (!fish.died) {
                com.dmg.common.pb.java.Fish.FishInfo.Builder finshInfo = msg.addFishsBuilder();
                finshInfo.setFishId(fish.id);
                finshInfo.setRoute(fish.route.id);
                finshInfo.setKind(fish.fishId);
                finshInfo.setX((int) fish.start.x);
                finshInfo.setY((int) fish.start.y);
                finshInfo.setTime(fish.liveTime());
                finshInfo.setSpeed(fish.speed);
                finshInfo.build();
            }
        }
        for (Seat s : seat.table.seats) {
            if (s.playerId > 0) {
                msg.addSeats(this.seatInfo(s));
            }
        }

        this.serverHandler.writeMsg(seat.playerId, com.dmg.common.pb.java.Fish.FishMessageId.ResRestore_ID_VALUE, msg.build());
    }

    /**
     * 向桌子中的所有玩家发送场景数据消息
     *
     * @param table
     */
    public void sendScenceMsg(Table table) {
        FishScence scence = table.scence();
        com.dmg.common.pb.java.Fish.ResScence.Builder msg = com.dmg.common.pb.java.Fish.ResScence.newBuilder();

        msg.setScenceId(scence.id);

        for (Fish fish : scence.fishs.values()) {
            if (!fish.died) {
                com.dmg.common.pb.java.Fish.FishInfo.Builder finshInfo = com.dmg.common.pb.java.Fish.FishInfo.newBuilder();
                finshInfo.setFishId(fish.id);
                finshInfo.setRoute(fish.route.id);
                finshInfo.setKind(fish.fishId);
                finshInfo.setX((int) fish.start.x);
                finshInfo.setY((int) fish.start.y);
                finshInfo.setTime(fish.liveTime());
                finshInfo.setSpeed(fish.speed);

                msg.addFishs(finshInfo);
            }
        }
        this.sendTablePlayersMsg(table, com.dmg.common.pb.java.Fish.FishMessageId.ResScence_ID_VALUE, msg.build());
    }

    /**
     * 发送锁定鱼
     *
     * @param seat
     * @param fishId
     */
    public void sendLockMsg(Seat seat, int fishId) {
        com.dmg.common.pb.java.Fish.ResLock.Builder msg = com.dmg.common.pb.java.Fish.ResLock.newBuilder();
        msg.setPlayerId(seat.playerId);
        msg.setFishId(fishId);
        this.sendTablePlayersMsg(seat.table, com.dmg.common.pb.java.Fish.FishMessageId.ResLock_ID_VALUE, msg.build());
    }

    /**
     * 发送取消锁定消息
     *
     * @param seat
     */
    public void sendCancelLockMsg(Seat seat) {
        com.dmg.common.pb.java.Fish.ResCancelLock.Builder msg = com.dmg.common.pb.java.Fish.ResCancelLock.newBuilder();
        msg.setPlayerId(seat.playerId);
        this.sendTablePlayersMsg(seat.table, com.dmg.common.pb.java.Fish.FishMessageId.ResCancelLock_ID_VALUE, msg.build());
    }

    /**
     * 发送金币变化消息
     *
     * @param seat
     */
    public void sendGoldChangeMsg(Seat seat) {
        com.dmg.common.pb.java.Fish.ResGoldChange.Builder msg = com.dmg.common.pb.java.Fish.ResGoldChange.newBuilder();
        msg.setPlayerId(seat.playerId);
        msg.setGold(this.changeGold(seat.gold));
        this.sendTablePlayersMsg(seat.table, com.dmg.common.pb.java.Fish.FishMessageId.ResGoldChange_ID_VALUE, msg.build());
    }

    /**
     * 发送金币充值消息(选场大厅使用)
     */
    public void sendGoldPayMsg(Seat seat, long payGold) {
        com.dmg.common.pb.java.Fish.ResGoldPay.Builder msg = com.dmg.common.pb.java.Fish.ResGoldPay.newBuilder();
        msg.setPlayerId(seat.playerId);
        msg.setGold(this.changeGold(seat.gold));
        msg.setPaygold(this.changeGold(payGold));
        this.sendTablePlayersMsg(seat.table, com.dmg.common.pb.java.Fish.FishMessageId.ResGoldPay_ID_VALUE, msg.build());
    }

    /**
     * 发送金币充值消息
     */
    public void sendGoldPayMsg(long playerId, long gold, long payGold) {
        com.dmg.common.pb.java.Fish.ResGoldPay.Builder msg = com.dmg.common.pb.java.Fish.ResGoldPay.newBuilder();
        msg.setPlayerId(playerId);
        msg.setGold(this.changeGold(gold));
        msg.setPaygold(this.changeGold(payGold));
        this.serverHandler.writeMsg(playerId, com.dmg.common.pb.java.Fish.FishMessageId.ResGoldPay_ID_VALUE, msg.build());
    }

    /**
     * @param table
     * @Description:发送代码碰撞玩家列表更新消息
     */
    public void sendInsteadPlayersMsg(Table table) {
        com.dmg.common.pb.java.Fish.ResInsteadPlayers.Builder msg = com.dmg.common.pb.java.Fish.ResInsteadPlayers.newBuilder();
        for (Seat s : table.seats) {
            if (s.robot && (s.playerId > 0)) {
                msg.addPlayers(s.playerId);
            }
        }
        this.serverHandler.writeMsg(table.leader.playerId, com.dmg.common.pb.java.Fish.FishMessageId.ResInsteadPlayers_ID_VALUE, msg.build());
    }

    /**
     * 发送超时退出
     *
     * @param playerId
     */
    public void sendTimeoutExitMsg(long playerId) {
        this.serverHandler.writeMsg(playerId, com.dmg.common.pb.java.Fish.FishMessageId.ResTimeoutExit_ID_VALUE, com.dmg.common.pb.java.Fish.ResTimeoutExit.newBuilder().setCode(com.dmg.common.pb.java.Fish.FishCode.TIMEOUT_EXIT).build());
    }

    /**
     * 发送服务器维护退出
     *
     * @param playerId
     */
    public void sendStopServerExitMsg(long playerId) {
        this.serverHandler.writeMsg(playerId, com.dmg.common.pb.java.Fish.FishMessageId.ResStopServiceExit_ID_VALUE, com.dmg.common.pb.java.Fish.ResStopServiceExit.newBuilder().setCode(com.dmg.common.pb.java.Fish.FishCode.STOP_SERVER).build());
    }

    /**
     * 向同桌的玩家(包括自己)发送消息
     */
    public void sendTablePlayersMsg(Table table, int msgId, Message msg) {
        List<Seat> seats = table.seats;
        for (int i = 0; i < seats.size(); i++) {
            Seat seat = seats.get(i);
            if ((seat.playerId > 0) && seat.ready) {
                this.serverHandler.writeMsg(seat.playerId, msgId, msg);
            }
        }
    }

    /** 转换金币从分到元 */
    public double changeGold(long gold) {
        BigDecimal result = new BigDecimal(gold);
        result = result.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
        return result.doubleValue();
    }
}
