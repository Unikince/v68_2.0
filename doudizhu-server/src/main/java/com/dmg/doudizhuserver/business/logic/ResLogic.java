package com.dmg.doudizhuserver.business.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmg.doudizhuserver.business.config.local.ConfigService;
import com.dmg.doudizhuserver.business.config.local.RoomConfig;
import com.dmg.doudizhuserver.business.model.Card;
import com.dmg.doudizhuserver.business.model.DdzCache;
import com.dmg.doudizhuserver.business.model.PlayCards;
import com.dmg.doudizhuserver.business.model.PlayerSettle;
import com.dmg.doudizhuserver.business.model.Room;
import com.dmg.doudizhuserver.business.model.Seat;
import com.dmg.doudizhuserver.business.model.Stage;
import com.dmg.doudizhuserver.common.platform.model.Player;
import com.dmg.doudizhuserver.common.platform.service.PlayerService;
import com.dmg.doudizhuserver.core.msg.MessagePush;

import lombok.extern.log4j.Log4j2;

/**
 * 返回逻辑
 */
@Log4j2
@Service
public class ResLogic {
    @Autowired
    private MessagePush push;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private ConfigService configService;

    /** 发送错误消息 */
    public void sendErrorMsg(long playerId, String m, int res, Object msg) {
        this.push.pushMsg(playerId, m, res, msg);
    }

    /** 登录游戏大厅 */
    public void Login(long playerId) {
        Player player = this.playerService.getPlayerPlatform(playerId);
        Map<String, Object> msgOut = new HashMap<>();
        msgOut.put("id", player.getId());
        msgOut.put("gold", player.getGold());
        msgOut.put("enter", DdzCache.playerSeats.get(playerId) != null);
        List<Map<String, Object>> roomListOut = new ArrayList<>();
        msgOut.put("roomList", roomListOut);
        for (RoomConfig config : this.configService.getRoomConfigs()) {
            if (!config.isOpen()) {
                continue;
            }
            Map<String, Object> roomInfoMapOut = new HashMap<>();
            roomListOut.add(roomInfoMapOut);
            roomInfoMapOut.put("id", config.getLevel());
            roomInfoMapOut.put("name", config.getName());
            roomInfoMapOut.put("lower", config.getBaseScore());
            roomInfoMapOut.put("baseScore", config.getEnterScore());
        }
        this.push.pushMsg(playerId, "Login", msgOut);
    }

    /** 发送进入房间消息 */
    public void sendEnterRoomMsg(Seat seat) {
        Map<String, Object> msgOut = new HashMap<>();
        Map<String, Object> roomInfoOut = new HashMap<>();// 房间信息
        msgOut.put("roomInfo", roomInfoOut);
        RoomConfig config = seat.room.config;
        roomInfoOut.put("id", config.getLevel());
        roomInfoOut.put("name", config.getName());
        roomInfoOut.put("lower", config.getEnterScore());
        roomInfoOut.put("baseScore", config.getBaseScore());

        List<Map<String, Object>> seatsListOut = new ArrayList<>();// 房间中的玩家信息集合
        msgOut.put("seats", seatsListOut);
        for (Seat tseat : seat.room.seats) {
            if (tseat.playerId == 0) {
                continue;
            }
            Map<String, Object> seatsMapOut = new HashMap<>();// 房间中的玩家信息集合
            seatsListOut.add(seatsMapOut);
            seatsMapOut.put("order", tseat.order);// 座位顺序
            seatsMapOut.put("playerId", tseat.playerId);// 玩家id
            seatsMapOut.put("nickname", tseat.nickname);// 昵称
            seatsMapOut.put("headImg", tseat.headImg);// 头像
            seatsMapOut.put("sex", tseat.sex);// 性别
            Player player = null;
            if (tseat.robot) {
                player = this.playerService.getRobot(tseat.playerId);
            } else {
                player = this.playerService.getPlayerPlatform(tseat.playerId);
            }
            seatsMapOut.put("gold", player.getGold());// 金币
        }
        this.push.pushMsg(seat.playerId, "EnterRoom", msgOut);
        this.sendOtherEnterTableMsg(seat);
    }

    /** 发送其他人进入房间消息 */
    private void sendOtherEnterTableMsg(Seat seat) {
        Map<String, Object> msgOut = new HashMap<>();
        msgOut.put("order", seat.order);// 座位顺序
        msgOut.put("playerId", seat.playerId);// 玩家id
        msgOut.put("nickname", seat.nickname);// 昵称
        msgOut.put("headImg", seat.headImg);// 头像
        msgOut.put("sex", seat.sex);// 性别

        Player player = null;
        if (seat.robot) {
            player = this.playerService.getRobot(seat.playerId);
        } else {
            player = this.playerService.getPlayerPlatform(seat.playerId);
        }
        msgOut.put("gold", player.getGold());// 金币
        this.push.pushMsg(this.getTablePlayerIds(seat.room, seat.playerId), "OtherEnterTable", msgOut);
    }

    /** 发送恢复现场消息(叫地主和游戏阶段有效，等待阶段掉线直接踢出) */
    public void sendRestoreMsg(Seat seat) {
        Map<String, Object> msgOut = new HashMap<>();
        Room room = seat.room;
        Stage stage = room.stage;
        RoomConfig config = room.config;
        if (stage == Stage.WAIT) {
            log.error("等待阶段不应该有断线重连消息");
            return;
        }
        msgOut.put("step", stage.val);// 2:叫地主,3:游戏
        Map<String, Object> roomInfoOut = new HashMap<>();// 房间信息
        msgOut.put("roomInfo", roomInfoOut);
        roomInfoOut.put("id", config.getLevel());
        roomInfoOut.put("name", config.getName());
        roomInfoOut.put("lower", config.getEnterScore());
        roomInfoOut.put("baseScore", config.getBaseScore());

        List<Map<String, Object>> seatsListOut = new ArrayList<>();// 房间中的玩家信息集合
        msgOut.put("seats", seatsListOut);
        for (Seat tseat : room.seats) {
            if (tseat.playerId == 0) {
                continue;
            }
            Map<String, Object> seatsMapOut = new HashMap<>();// 房间中的玩家信息集合
            seatsListOut.add(seatsMapOut);
            seatsMapOut.put("order", tseat.order);// 座位顺序
            seatsMapOut.put("playerId", tseat.playerId);// 玩家id
            seatsMapOut.put("nickname", tseat.nickname);// 昵称
            seatsMapOut.put("headImg", tseat.headImg);// 头像
            seatsMapOut.put("sex", tseat.sex);// 性别
            Player player = null;
            if (tseat.robot) {
                player = this.playerService.getRobot(tseat.playerId);
            } else {
                player = this.playerService.getPlayerPlatform(tseat.playerId);
            }
            seatsMapOut.put("gold", player.getGold());// 金币
            if (stage != Stage.DEAL_CARDS) {
                seatsMapOut.put("cardsNum", tseat.handCards.cards.size());// 牌的数量
                seatsMapOut.put("isAuto", tseat.auto);// 是否托管
            }
        }

        if (stage != Stage.DEAL_CARDS) {
            if (room.landlord != null) {
                msgOut.put("landlord", room.landlord.playerId);// 地主
                msgOut.put("callScore", room.landlord.callScore);// 地主倍数
            } else {
                msgOut.put("landlord", 0);// 地主
                msgOut.put("callScore", 0);// 地主倍数
            }

            List<Integer> cardsOut = new ArrayList<>();
            for (Card card : seat.handCards.getSortCards()) {
                cardsOut.add(card.id);
            }
            msgOut.put("cards", cardsOut);// 玩家的牌

            List<Integer> leftCardsOut = new ArrayList<>();
            for (Seat eveseat : room.seats) {
                leftCardsOut.addAll(Card.getOutCard(eveseat.handCards.getSortCards()));
            }
            msgOut.put("leftCards", leftCardsOut);// 剩余的牌
            if (stage == Stage.CALL_LANDLORD) {// 叫地主
                msgOut.put("nextCallLandlordPlayer", room.nextCallSeat.playerId);// 下一个叫地主玩家
                msgOut.put("nextCallLandlordLeftTime", room.future.getDelay(TimeUnit.SECONDS));// 下一个叫地主玩家操作剩余时间(秒)
            } else if (stage == Stage.GAME) {// 游戏中
                msgOut.put("hiddenCards", Card.getOutCard(room.hiddenCards));// 底牌

                msgOut.put("bombNum", room.bombNum);// 炸弹数

                msgOut.put("prePlayer", room.prePlaySeat.playerId);// 上一个出牌玩家
                PlayCards prePlayCards = room.prePlayCards;
                List<Integer> prePlayCardsOut = new ArrayList<>();
                int cardType = 0;
                if (prePlayCards != null) {
                    prePlayCardsOut.addAll(Card.getOutCard(prePlayCards.sortedCards));
                    cardType = prePlayCards.type.type;
                }
                msgOut.put("prePlayCards", prePlayCardsOut);// 上一个玩家出的牌
                msgOut.put("prePlayCardsType", cardType);// 上一个玩家出的牌的类型(1:单牌，2:对子,3:三不带,4:三代单，5:三带对,6:单顺,7:双顺,8:三顺,9:飞机带单,10:飞机带队,11:炸弹,12:王炸,13:四带单,14:四带队)

                msgOut.put("nextPlayer", room.nextPlaySeat.playerId); // 下一个出牌玩家
                msgOut.put("nextPlayerLeftTime", room.nextPlaySeat.future.getDelay(TimeUnit.SECONDS) > 0 ? room.nextPlaySeat.future.getDelay(TimeUnit.SECONDS) : 0);// 下一个出牌玩家操作剩余时间(秒)
            }
        }

        this.push.pushMsg(seat.playerId, "Restore", msgOut);
    }

    /** 发送即将开始消息 */
    public void sendWillDealCardsMsg(Room room) {
        this.push.pushMsg(this.getTablePlayerIds(room), "WillDealCards", null);
    }

    /** 发送叫地主消息 */
    public void sendCallLandlordMsg(Seat seat, int call) {
        Room room = seat.room;
        Map<String, Object> msgOut = new HashMap<>();
        msgOut.put("playerId", seat.playerId);// 叫牌的玩家
        msgOut.put("call", call);// 分数0,1,2,3
        msgOut.put("landlord", room.landlord == null ? 0 : room.landlord.playerId);// 地主玩家id
        msgOut.put("nextCallPlayer", room.nextCallSeat == null ? 0 : room.nextCallSeat.playerId);// 下一个叫牌的玩家(0代表没有,叫地主结束)
        this.push.pushMsg(this.getTablePlayerIds(room), "CallLandlord", msgOut);
    }

    /** 发送底牌消息 */
    public void sendHiddenCardsMsg(Room room) {
        this.push.pushMsg(this.getTablePlayerIds(room), "HiddenCards", Card.getOutCard(room.hiddenCards));
    }

    /** 发送自己牌 */
    public void sendDealCardsMsg(Room room) {
        long nextCallPlayer = room.nextCallSeat.playerId;
        for (Seat seat : room.seats) {
            if (!seat.online) {
                continue;
            }
            Map<String, Object> msgOut = new HashMap<>();
            msgOut.put("callPlayerId", nextCallPlayer);
            msgOut.put("cards", Card.getOutCard(seat.handCards.getSortCards()));
            this.push.pushMsg(seat.playerId, "DealCards", msgOut);
        }
    }

    /** 发送自己牌 */
    public void sendDealCardsMsg(Seat seat) {
        Map<String, Object> msgOut = new HashMap<>();
        msgOut.put("callPlayerId", null);
        msgOut.put("cards", Card.getOutCard(seat.handCards.getSortCards()));
        this.push.pushMsg(seat.playerId, "DealCards", msgOut);
    }

    /** 出牌 */
    public void sendPlayCardsMsg(Room room, Seat seat, PlayCards playCards) {
        Map<String, Object> msgOut = new HashMap<>();
        msgOut.put("playerId", seat.playerId);// 出牌的玩家

        if (playCards != null) {
            msgOut.put("nextPlayerId", (room.nextPlaySeat != null) && (seat.handCards.cards.size() != 0) ? room.nextPlaySeat.playerId : 0);// 下一个出牌玩家
            msgOut.put("cards", Card.getOutCard(playCards.sortedCards));// 玩家出的牌
            msgOut.put("cardsType", playCards.type.type);// 玩家出的牌的类型(1:单牌，2:对子,3:三不带,4:三代单，5:三带对,6:单顺,7:双顺,8:三顺,9:飞机带单,10:飞机带队,11:炸弹,12:王炸,13:四带单,14:四带队)
        } else {
            msgOut.put("cardsType", 0);// 玩家出的牌的类型
            msgOut.put("nextPlayerId", seat.room.nextPlaySeat.playerId);// 下一个出牌玩家
        }
        this.push.pushMsg(this.getTablePlayerIds(room), "PlayCards", msgOut);
    }

    /** 返回给客户端是否托管（群发） */
    public void sendAutoPlayMsg(Seat seat) {
        Map<String, Object> msgOut = new HashMap<>();
        msgOut.put("playerId", seat.playerId);// 玩家id
        msgOut.put("isAuto", seat.auto);// 是否托管
        this.push.pushMsg(this.getTablePlayerIds(seat.room), "AutoPlay", msgOut);
    }

    /**
     * 向同桌的玩家发送游戏结束消息
     */
    public void sendGameOverMsg(Room room, List<PlayerSettle> playerSettles) {
        Map<String, Object> msgOut = new HashMap<>();
        msgOut.put("baseScore", room.config.getBaseScore());// 底分
        msgOut.put("callScore", room.landlord.callScore);// 地主倍数
        msgOut.put("spring", room.spring ? 2 : 1);// 是否春天(包含反春)(值为1或者2)
        msgOut.put("bombNum", room.bombNum);// 炸弹数
        msgOut.put("playerBills", playerSettles);
        this.push.pushMsg(this.getTablePlayerIds(room), "GameOver", msgOut);
    }

    /** 发送退房间消息 */
    public void sendExitRoomMsg(Seat seat) {
        this.push.pushMsg(this.getTablePlayerIds(seat.room), "ExitRoom", 0, seat.playerId);
    }

    /** 发送退房间消息(强行) */
    public void sendExitRoomMsgForce(Seat seat) {
        this.push.pushMsg(seat.playerId, "ExitRoom", 0, seat.playerId);
    }

    /** 心跳 */
    public void sendHeartbeatLost(Seat seat) {
        this.push.pushMsg(seat.playerId, "9999", null);
    }

    /** 获取房间上玩家id集合 */
    private Set<Long> getTablePlayerIds(Room room, long... excludeIds) {
        Set<Long> excludeIdSet = new HashSet<>();
        if (excludeIds != null) {
            for (long id : excludeIds) {
                excludeIdSet.add(id);
            }
        }
        Set<Long> result = new HashSet<>();
        List<Seat> seats = room.seats;
        for (int i = 0; i < seats.size(); i++) {
            Seat seat = seats.get(i);
            long playerId = seat.playerId;
            if (playerId > 0 && seat.online && !excludeIdSet.contains(playerId)) {
                result.add(playerId);
            }
        }
        return result;
    }
}
