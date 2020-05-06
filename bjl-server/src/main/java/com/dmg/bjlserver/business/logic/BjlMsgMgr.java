package com.dmg.bjlserver.business.logic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dmg.bjlserver.business.config.dic.BjlTableDic;
import com.dmg.bjlserver.business.config.wrapper.BjlTableWrapper;
import com.dmg.bjlserver.business.model.Cache;
import com.dmg.bjlserver.business.model.Constant;
import com.dmg.bjlserver.business.model.game.GameStage;
import com.dmg.bjlserver.business.model.game.SeatPlayer;
import com.dmg.bjlserver.business.model.game.SeatRecord;
import com.dmg.bjlserver.business.model.room.Seat;
import com.dmg.bjlserver.business.model.room.Table;
import com.dmg.bjlserver.business.platform.model.Player;
import com.dmg.bjlserver.business.platform.service.PlayerService;
import com.dmg.bjlserver.core.msg.MessagePush;
import com.dmg.bjlserver.core.work.Worker;
import com.dmg.bjlserver.core.work.WorkerGroup;
import com.dmg.common.pb.java.Bjl;
import com.google.protobuf.Message;

import lombok.extern.log4j.Log4j2;

/**
 * 百家乐消息管理
 */
@Log4j2
@Component
public class BjlMsgMgr {
    @Autowired
    private Worker worker;
    @Autowired
    private BjlMgr bjlMgr;
    @Autowired
    private BjlTableDic roomDic;
    @Autowired
    private MessagePush serverHandler;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private WorkerGroup workerGroup;

    private List<Bjl.RoomInfo> roomInfos;

    public void roomInfoCache() {
        this.worker.scheduleWithFixedDelay(() -> {// 做定时器处理错误
            try {
                List<Bjl.RoomInfo> roomInfos = new ArrayList<>();
                for (BjlTableWrapper config : this.roomDic.values()) {
                    if (!config.isOpen()) { // 房间没有开启
                        continue;
                    }
                    Bjl.RoomInfo.Builder info = Bjl.RoomInfo.newBuilder();
                    info.setId(config.getId());
                    info.setName(config.getName());

                    List<Long> chipList = config.getChipList();
                    info.setBetLower(chipList.get(0));
                    info.setBetUpper(chipList.get(chipList.size() - 1));

                    Table table = Cache.getTable(config.getId());
                    info.addAllRouteInfo(table.routes);
                    info.setSeatNumber(table.playerNum());
                    info.setStage(table.stage.code);
                    info.setTime((int) table.stageFuture.getDelay(TimeUnit.SECONDS));
                    roomInfos.add(info.build());
                }
                this.roomInfos = roomInfos;
            } catch (Exception e) {
                log.error("", e);
            }
        }, 10000, 500, TimeUnit.MILLISECONDS);
    }

    /** 发送房间消息 */
    public void sendRoomsMsg(Long playerId) {
        Bjl.ResRooms.Builder msg = Bjl.ResRooms.newBuilder();
        msg.addAllRooms(this.roomInfos);
        Player player = this.playerService.getPlayer(playerId);
        if (player == null) {
            log.error("玩家[{}]未找到", playerId);
            return;
        }
        Bjl.PlayerInfo.Builder playerInfo = Bjl.PlayerInfo.newBuilder();
        playerInfo.setId(playerId);
        playerInfo.setName(player.getNickName());
        playerInfo.setGold(player.getGold());
        playerInfo.setSex(player.getSex());
        if (StringUtils.isNotBlank(player.getHeadImg())) {
            playerInfo.setAvatar(player.getHeadImg());
        }
        msg.setPlayerInfo(playerInfo);
        Seat seat = Cache.getSeat(playerId);
        msg.setCurRoomId(seat == null ? 0 : seat.table.id);
        this.serverHandler.writeMsg(playerId, Bjl.BjlMessageId.ResRooms_ID_VALUE, msg.build());
    }

    /**
     * 发送进入房间消息
     *
     * @param seat
     */
    public void sendEnterRoomMsg(Seat seat) {
        Table table = seat.table;
        int roomId = table.id;
        Bjl.ResEnterRoom.Builder msg = Bjl.ResEnterRoom.newBuilder();
        List<Integer> multiples = table.multiples;
        // 房间剩余牌数量
        msg.setRestCardNum(table.cards.size());
        msg.setTableIndex(table.id);

        msg.setRoomId(roomId);
        msg.setCards(this.bjlMgr.getHandCardInfo(table));

        msg.setBetLower(this.roomDic.get(table.id).getChipList().get(0));
        msg.setCode(Bjl.BjlCode.SUCCESS);

        for (int i = 0; i < multiples.size(); i++) {
            msg.addMultiple(multiples.get(i));
        }

        for (int i = 0; i < seat.areaBets.length; i++) {
            Bjl.AreaBetInfo.Builder area = Bjl.AreaBetInfo.newBuilder();
            area.setArea(i);
            area.setGold(seat.areaBets[i]);
            msg.addPlayerBets(area);
        }

        for (int i = 0; i < table.areaBets.length; i++) {
            Bjl.AreaBetInfo.Builder area = Bjl.AreaBetInfo.newBuilder();
            area.setArea(i);
            area.setGold(table.areaBets[i]);
            msg.addTableBets(area);
        }

        for (Seat s : table.rankSeats) {
            if (s != null) {
                msg.addRanks(s.seatInfo());
            }
        }

        for (int i = 0; i < table.playerBets.size(); i++) { // 天地合 下注区筹码 最近30条
            Bjl.AreaBetCardInfo.Builder seatScore = Bjl.AreaBetCardInfo.newBuilder();
            seatScore.setArea(i);
            Map<Long, List<Long>> map = table.playerBets.get(i);
            if (map != null) {
                map.forEach((k, v) -> {
                    List<Long> vv = new ArrayList<>(v);
                    try {
                        for (int t = 0; t < v.size(); t++) {
                            vv.add(v.get(t));
                        }
                    } catch (Exception e) {
                        log.error("", e);
                    }
                    for (Long bet : vv) {
                        seatScore.addPlayerId(k);
                        if (bet == null) {
                            continue;
                        }
                        seatScore.addGold(bet);
                    }
                });
            }
            msg.addTableBetCards(seatScore);
        }
        msg.setSeat(seat.seatInfo());
        msg.setStage(table.stage.code);
        msg.setTime((int) table.stageFuture.getDelay(TimeUnit.SECONDS));
        msg.setNoSeats(table.otherSeats.size());
        msg.setAutoSat(true);// 去掉自动坐下，客户端未清理，固定写true
        msg.setMultipleMax(multiples.get(multiples.size() - 1));

        msg.addAllRoutes(table.routes);
        if (table.stage == GameStage.GAMING) {
            msg.setBankerBalance(this.bankerBalance(table));
            msg.addAllRankPlayerBalances(this.rankPlayerBalances(table));
            msg.setPlayerGold(seat.totalWinGold());
        }

        msg.addAllChips(this.roomDic.get(table.id).getChipList());

        BjlTableWrapper roomBean = this.roomDic.get(seat.table.id);

        msg.setPlayerMaxEffectiveBet(roomBean.getPlayerMaxBet());
        msg.setTableMaxEffectiveBet(roomBean.getTableMaxBet());

        long seatLimitGold = 0;
        seatLimitGold += seat.areaBets[0] * table.multiples.get(0);
        seatLimitGold += Math.abs(seat.areaBets[1] * table.multiples.get(1) - seat.areaBets[2] * table.multiples.get(2));
        seatLimitGold += seat.areaBets[3] * table.multiples.get(3);
        seatLimitGold += seat.areaBets[4] * table.multiples.get(4);
        msg.setPlayerCurEffectiveBet(seatLimitGold);

        long tableLimitGold = 0;
        tableLimitGold += table.areaBets[0] * table.multiples.get(0);
        tableLimitGold += Math.abs(table.areaBets[1] * table.multiples.get(1) - table.areaBets[2] * table.multiples.get(2));
        tableLimitGold += table.areaBets[3] * table.multiples.get(3);
        tableLimitGold += table.areaBets[4] * table.multiples.get(4);
        msg.setTableCurEffectiveBet(tableLimitGold);

        msg.setGodOfGamblersFirstBetTable(table.godOfGamblersFirstBetTable);
        this.serverHandler.writeMsg(seat.playerId, Bjl.BjlMessageId.ResEnterRoom_ID_VALUE, msg.build());
    }

    /**
     * 发送退出房间消息
     *
     * @param seat
     */
    public void sendExitRoomMsg(Seat seat, Bjl.BjlCode code) {
        Bjl.ResExitRoom.Builder msg = Bjl.ResExitRoom.newBuilder();
        msg.setCode(code);
        this.serverHandler.writeMsg(seat.playerId, Bjl.BjlMessageId.ResExitRoom_ID_VALUE, msg.build());
    }

    /**
     * 发送退出房间消息
     *
     * @param seat
     */
    public void sendExitRoomMsg(long playerId, Bjl.BjlCode code) {
        Bjl.ResExitRoom.Builder msg = Bjl.ResExitRoom.newBuilder();
        msg.setCode(code);
        this.serverHandler.writeMsg(playerId, Bjl.BjlMessageId.ResExitRoom_ID_VALUE, msg.build());
    }

    /**
     * 发送退出房间消息
     *
     * @param seat
     */
    public void sendExitRoomMsg(Seat seat, int code) {
        Bjl.ResExitRoom.Builder msg = Bjl.ResExitRoom.newBuilder();
        msg.setCodeValue(code);
        this.serverHandler.writeMsg(seat.playerId, Bjl.BjlMessageId.ResExitRoom_ID_VALUE, msg.build());
    }

    /**
     * 发送玩家区域下注消息
     *
     * @param seat
     * @param area
     * @param gold
     */
    public void sendPlayerAreaBetMsg(Seat seat, int area, long gold) {
        Bjl.ResPlayerBet.Builder msg = Bjl.ResPlayerBet.newBuilder();
        Table table = seat.table;
        Bjl.AreaBetInfo.Builder bet = Bjl.AreaBetInfo.newBuilder();
        bet.setArea(area);
        bet.setGold(gold);
        msg.setBet(bet);
        msg.setCode(Bjl.BjlCode.SUCCESS);
        msg.setPlayerId(seat.playerId);

        for (int i = 0; i < table.areaBets.length; i++) {
            Bjl.AreaBetInfo.Builder areaBet = Bjl.AreaBetInfo.newBuilder();
            areaBet.setArea(i);
            areaBet.setGold(table.areaBets[i]);
            msg.addTableBets(areaBet);
        }

        long seatLimitGold = 0;
        seatLimitGold += seat.areaBets[0] * table.multiples.get(0);
        seatLimitGold += Math.abs(seat.areaBets[1] * table.multiples.get(1) - seat.areaBets[2] * table.multiples.get(2));
        seatLimitGold += seat.areaBets[3] * table.multiples.get(3);
        seatLimitGold += seat.areaBets[4] * table.multiples.get(4);
        msg.setPlayerCurEffectiveBet(seatLimitGold);

        long tableLimitGold = 0;
        tableLimitGold += table.areaBets[0] * table.multiples.get(0);
        tableLimitGold += Math.abs(table.areaBets[1] * table.multiples.get(1) - table.areaBets[2] * table.multiples.get(2));
        tableLimitGold += table.areaBets[3] * table.multiples.get(3);
        tableLimitGold += table.areaBets[4] * table.multiples.get(4);
        msg.setTableCurEffectiveBet(tableLimitGold);

        if (table.rankSeats.contains(seat)) {
            this.sendTablePlayersMsg(table, Bjl.BjlMessageId.ResPlayerBet_ID_VALUE, msg.build());
        } else {
            this.serverHandler.writeMsg(seat.playerId, Bjl.BjlMessageId.ResPlayerBet_ID_VALUE, msg.build());
        }
    }

    /**
     * 发送玩家区域下注 错误码消息
     **/
    public void sendPlayerAreaBetMsg(Long playerId, Bjl.BjlCode code) {
        Bjl.ResPlayerBet.Builder msg = Bjl.ResPlayerBet.newBuilder();
        msg.setCode(code);
        this.sendMsg(playerId, Bjl.BjlMessageId.ResPlayerBet_ID_VALUE, msg.build());
    }

    /**
     * 发送撤销筹码消息
     *
     * @param seat
     * @param totalBetGold
     */
    public void sendRevokeBetMsg(Seat seat, long totalBetGold) {
        Bjl.ResRevokeBet.Builder msg = Bjl.ResRevokeBet.newBuilder();
        msg.setPlayerId(seat.playerId);
        msg.setBetGold(totalBetGold);
        this.sendTablePlayersMsg(seat.table, Bjl.BjlMessageId.ResRevokeBet_ID_VALUE, msg.build());
    }

    /**
     * 向场次中的所有玩家发送场次区域下注消息
     *
     * @param table
     */
    public void sendOtherPlayersBetMsg(Table table) {
        if (table.isBet) {
            table.isBet = false;
            Bjl.ResOtherPlayersBet.Builder msg = Bjl.ResOtherPlayersBet.newBuilder();

            for (int i = 0; i < table.areaBets.length; i++) {
                Bjl.AreaBetInfo.Builder areaBet = Bjl.AreaBetInfo.newBuilder();
                areaBet.setArea(i);
                areaBet.setGold(table.areaBets[i]);
                msg.addTableBets(areaBet);
            }
            // 无座下注筹码 遍历 庄 闲 和 庄对 闲对 下注区
            for (int i = 0; i < table.areaBets.length; i++) {
                Bjl.AreaBetCardInfo.Builder seatScore = Bjl.AreaBetCardInfo.newBuilder();
                seatScore.setArea(i);
                List<Long> list = new ArrayList<>(table.noSeatCard.get(i));
                List<Long> listPlayer = new ArrayList<>(table.noSeatPlayerId.get(i));
                try {
                    if (list != null) {
                        for (Long l : list) {
                            if (l != null) {
                                seatScore.addGold(l);
                            }
                        }
                    }
                    if (listPlayer != null) {
                        for (Long l : listPlayer) {
                            if (l != null) {
                                seatScore.addPlayerId(l);
                            }
                        }
                    }
                    msg.addTableBetCards(seatScore);
                } catch (Exception e) {
                    log.error("", e);
                    this.workerGroup.schedule(() -> {
                        this.sendOtherPlayersBetMsg(table);
                    }, 2, TimeUnit.SECONDS);
                    return;
                }
                // 清理无座列表下注数据
                table.noSeatCard.get(i).clear();
                table.noSeatPlayerId.get(i).clear();
            }

            long tableLimitGold = 0;
            tableLimitGold += table.areaBets[0] * table.multiples.get(0);
            tableLimitGold += Math.abs(table.areaBets[1] * table.multiples.get(1) - table.areaBets[2] * table.multiples.get(2));
            tableLimitGold += table.areaBets[3] * table.multiples.get(3);
            tableLimitGold += table.areaBets[4] * table.multiples.get(4);
            msg.setTableCurEffectiveBet(tableLimitGold);

            this.sendTablePlayersMsg(table, Bjl.BjlMessageId.ResOtherPlayersBet_ID_VALUE, msg.build());
        }
    }

    /**
     * 庄家信息
     *
     * @param banker
     * @return
     */
    private Bjl.BankerInfo bankerInfo(Seat banker) {
        Bjl.BankerInfo.Builder bankerInfo = Bjl.BankerInfo.newBuilder();
        bankerInfo.setNickName(banker.nickName);
        bankerInfo.setPlayerId(banker.playerId);

        bankerInfo.setGold(banker.gold);
        if (banker.icon == null) {
            banker.icon = "";
        }
        bankerInfo.setIcon(banker.icon);

        return bankerInfo.build();
    }

    /**
     * 发送庄家信息
     *
     * @param table
     */
    public void sendBankerInfoMsg(Table table) {
        Bjl.ResBankerInfo.Builder msg = Bjl.ResBankerInfo.newBuilder();
        msg.setBanker(this.bankerInfo(table.banker));
        this.sendTablePlayersMsg(table, Bjl.BjlMessageId.ResBankerInfo_ID_VALUE, msg.build());
    }

    /**
     * 发送排名玩家消息
     *
     * @param table
     */
    public void sendRankPlayersMsg(Table table) {
        Bjl.ResRankPlayers.Builder msg = Bjl.ResRankPlayers.newBuilder();
        for (Seat rankSeat : table.rankSeats) {
            if (rankSeat != null) {
                msg.addSeats(rankSeat.seatInfo());
            }
        }

        this.sendTablePlayersMsg(table, Bjl.BjlMessageId.ResRankPlayers_ID_VALUE, msg.build());
    }

    /**
     * 发送无座玩家消息
     *
     * @param table
     */
    public void sendOtherPlayersMsg(long playerId) {
        Seat seat = Cache.getSeat(playerId);
        if (seat == null) {
            return;
        }
        Table table = seat.table;
        Bjl.ResOtherPlayers.Builder msg = Bjl.ResOtherPlayers.newBuilder();
        List<Seat> otherSeats = table.otherSeats;

        for (int i = 0; (i < otherSeats.size()) && (i < Constant.OTHER_SEATS_SHOW_LIMIT); i++) {
            Seat bjlSeat = otherSeats.get(i);
            if ((bjlSeat != null) && (bjlSeat != table.banker)) {
                msg.addSeats(otherSeats.get(i).seatInfo());
            }
        }

        this.serverHandler.writeMsg(playerId, Bjl.BjlMessageId.ResOtherPlayers_ID_VALUE, msg.build());
    }

    /**
     * 发送无座列表人数
     *
     * @param table
     */
    public void sendOtherPlayerNumMsg(Table table) {
        Bjl.ResOtherPlayerNum.Builder msg = Bjl.ResOtherPlayerNum.newBuilder();
        msg.setNum(table.otherSeats.size());

        this.sendTablePlayersMsg(table, Bjl.BjlMessageId.ResOtherPlayerNum_ID_VALUE, msg.build());
    }

    /**
     * 发送阶段消息
     *
     * @param table
     * @param stage
     * @param time
     */
    public void sendStageMsg(Table table, GameStage stage, int time) {
        Bjl.ResStage.Builder msg = Bjl.ResStage.newBuilder();
        msg.setStage(stage.code);
        msg.setTime(time);
        this.sendTablePlayersMsg(table, Bjl.BjlMessageId.ResStage_ID_VALUE, msg.build());
    }

    /**
     * 发送游戏结算消息
     *
     * @param table
     * @param route
     */
    public void sendBalanceMsg(Table table, Bjl.RouteInfo route) {
        List<Seat> seats = table.seats;
        // 庄家结算
        Bjl.BalanceInfo bankerBalance = this.bankerBalance(table);
        // 排名玩家结算
        List<Bjl.BalanceInfo> rankPlayerBalances = this.rankPlayerBalances(table);
        // 牌的信息
        Bjl.HandCardInfo handCardInfos = this.handCardInfos(table);

        Bjl.SeatInfoRecord winMaxPlayer = this.winMaxPlayer(table);

        for (int i = 0; i < seats.size(); i++) {
            Seat seat = seats.get(i);
            long playerId = seat.playerId;
            if (playerId > 0) {
                Bjl.ResBalance.Builder msg = Bjl.ResBalance.newBuilder();
                msg.setRoute(route);
                msg.setPlayerGold(seat.totalWinGold() - seat.totalBet());
                msg.setBankerBalance(bankerBalance);
                msg.addAllRankPlayerBalances(rankPlayerBalances);
                msg.setHandCards(handCardInfos);
                msg.setRestCardNum(table.cards.size());
                msg.setWinMaxPlayer(winMaxPlayer);
                this.serverHandler.writeMsg(seat.playerId, Bjl.BjlMessageId.ResBalance_ID_VALUE, msg.build());
            }
        }
    }

    /**
     * 获取HandCardInfo
     *
     * @param table
     * @return
     */
    private Bjl.HandCardInfo handCardInfos(Table table) {
        Bjl.HandCardInfo.Builder builder = Bjl.HandCardInfo.newBuilder();

        Bjl.CardGroup.Builder bcg = Bjl.CardGroup.newBuilder();
        Bjl.CardGroup.Builder pcg = Bjl.CardGroup.newBuilder();

        this.bjlMgr.getTableCardInfo(table, bcg, pcg);

        builder.addCardGroup(bcg);
        builder.addCardGroup(pcg);

        return builder.build();
    }

    /**
     * 庄家结算信息
     *
     * @param table
     * @return
     */
    private Bjl.BalanceInfo bankerBalance(Table table) {
        Seat banker = table.banker;
        Bjl.BalanceInfo.Builder bankerBalance = Bjl.BalanceInfo.newBuilder();
        bankerBalance.setPlayerId(banker.playerId);
        long totalWin = banker.totalWinGold() - banker.tax;
        bankerBalance.setWinGold(totalWin);
        bankerBalance.setTax(banker.tax);
        for (long winGold : banker.winGolds) {
            bankerBalance.addWinGolds(winGold);
        }

        return bankerBalance.build();
    }

    /**
     * 排名玩家结算信息
     *
     * @param table
     * @return
     */
    private List<Bjl.BalanceInfo> rankPlayerBalances(Table table) {
        List<Bjl.BalanceInfo> balances = new ArrayList<>();
        for (Seat rankSeat : table.rankSeats) {
            if (rankSeat != null) {
                Bjl.BalanceInfo.Builder bankerBalance = Bjl.BalanceInfo.newBuilder();
                bankerBalance.setPlayerId(rankSeat.playerId);
                bankerBalance.setWinGold(rankSeat.totalWinGold() - rankSeat.totalBet());
                bankerBalance.setTax(rankSeat.tax);
                for (long winGold : rankSeat.winGolds) {
                    bankerBalance.addWinGolds(winGold);
                }
                balances.add(bankerBalance.build());
            }
        }

        return balances;
    }

    /**
     * 大赢家信息
     */
    private Bjl.SeatInfoRecord winMaxPlayer(Table table) {
        // 玩家排序
        table.seats.sort(new Comparator<Seat>() {
            @Override
            public int compare(Seat o1, Seat o2) {
                return Long.compare(o2.totalWinGold() - o2.totalBet(), o1.totalWinGold() - o1.totalBet());
            }
        }); // 排序 赢家玩家列表
        Seat seat = table.seats.get(0);
        Bjl.SeatInfoRecord.Builder infoRecord = Bjl.SeatInfoRecord.newBuilder();
        infoRecord.setNickName(seat.nickName);
        infoRecord.setGold(seat.totalWinGold() - seat.totalBet());
        infoRecord.setIcon(seat.icon);
        infoRecord.setPlayerId(seat.playerId);
        return infoRecord.build();
    }

    /**
     * 发送玩家金币变化消息
     *
     * @param seat
     */
    public void sendGoldChangeMsg(Seat seat) {
        Bjl.ResGoldChange.Builder msg = Bjl.ResGoldChange.newBuilder();
        msg.setPlayerId(seat.playerId);
        msg.setGold(seat.gold);
        this.serverHandler.writeMsg(seat.playerId, Bjl.BjlMessageId.ResGoldChange_ID_VALUE, msg.build());
    }

    /**
     * 发送玩家续压成功消息
     *
     * @param seat
     */
    public void sendContinueBetMsg(Seat seat, Bjl.BjlCode code) {
        Bjl.ResContinueBet.Builder msg = Bjl.ResContinueBet.newBuilder();
        msg.setCode(code);

        this.serverHandler.writeMsg(seat.playerId, Bjl.BjlMessageId.ResContinueBet_ID_VALUE, msg.build());
    }

    /**
     * 加入房间返回
     */
    public void sendEnterRoomMsg(Long playerId, Bjl.BjlCode code) {
        Bjl.ResEnterRoom.Builder msg = Bjl.ResEnterRoom.newBuilder();
        msg.setCode(code);

        this.serverHandler.writeMsg(playerId, Bjl.BjlMessageId.ResEnterRoom_ID_VALUE, msg.build());
    }

    /**
     * 牌局明细 我的明细
     */
    public void sendSeatDetailed(Long playerId, List<SeatRecord> records) {
        Bjl.ResSeatDetailed.Builder msg = Bjl.ResSeatDetailed.newBuilder();
        for (int i = records.size() - 1; i >= 0; i--) { // 每局 玩家消息组
            SeatRecord seatRecord = records.get(i);
            if (seatRecord == null) {
                continue;
            }
            Bjl.SeatDetailedInfo.Builder detailedInfo = Bjl.SeatDetailedInfo.newBuilder();

            detailedInfo.setRoomType(seatRecord.roomType);

            detailedInfo.setGameOverTime(seatRecord.gameOverTime);
            detailedInfo.setBetStartGold(seatRecord.betStartGold);
            detailedInfo.setGold(seatRecord.gold);
            detailedInfo.setWinLoseGold(seatRecord.winLoseGold);

            for (int area = 0; area < seatRecord.areaBets.length; area++) {
                detailedInfo.addBetGold(seatRecord.areaBets[area]);
                detailedInfo.addWinGold(seatRecord.areaWinGold[area]);
            }

            Bjl.HandCardInfo.Builder hci = Bjl.HandCardInfo.newBuilder();
            Bjl.CardGroup.Builder builder = Bjl.CardGroup.newBuilder();
            for (int bc = 0; bc < seatRecord.bankerCards.length; bc++) {
                builder.addCard(seatRecord.bankerCards[bc]);
            }
            hci.addCardGroup(builder);

            builder = Bjl.CardGroup.newBuilder();
            for (int pc = 0; pc < seatRecord.playerCards.length; pc++) {
                builder.addCard(seatRecord.playerCards[pc]);
            }
            hci.addCardGroup(builder);

            detailedInfo.setCards(hci);
            msg.addSeatDetailed(detailedInfo);
        }
        this.serverHandler.writeMsg(playerId, Bjl.BjlMessageId.ResSeatDetailed_ID_VALUE, msg.build());
    }

//    /**
//     * 牌局明细 盈利榜
//     */
//    public void sendProfitList(BjlSeat seat) {
//        BjlTable table = seat.table;
//        Bjl.ResProfitList.Builder msg = Bjl.ResProfitList.newBuilder();
//        msg.setOverTime((int) table.brnnTableRecord.recordDate);
//        List<SeatPlayer> list = table.brnnTableRecord.seatDayWinPlayer;// 盈利榜历史 最新20条
//        for (SeatPlayer seatPlayer : list) {
//            if (seatPlayer.getDayGoldTotal() > 0) {
//                Bjl.SeatInfoRecord.Builder record = Bjl.SeatInfoRecord.newBuilder();
//                record.setNickName(seatPlayer.getNickName());
//                record.setGold(seatPlayer.getDayGoldTotal());
//                record.setIcon(seatPlayer.getIcon());
//                record.setPlayerId(seatPlayer.getPlayerId());
//                msg.addSeatRecord(record);
//            }
//        }
//        this.serverHandler.writeMsg(seat.playerId, Bjl.BjlMessageId.ResProfitList_ID_VALUE, msg.build());
//    }

    public void sendProfitList(Long playerId, Bjl.ResProfitList.Builder msg) {
        this.serverHandler.writeMsg(playerId, Bjl.BjlMessageId.ResProfitList_ID_VALUE, msg.build());
    }

    /**
     * 牌局明细 上局赢家
     */
    public void sendSeatWin(Seat seat) {
        Table table = seat.table;
        Bjl.ResSeatWin.Builder msg = Bjl.ResSeatWin.newBuilder();
        List<SeatPlayer> list = table.brnnTableRecord.seatWinPlayer;// 盈利榜历史 最新20条
        for (SeatPlayer seatPlayer : list) {
            if (seatPlayer.getGold() > 0) {
                Bjl.SeatInfoRecord.Builder infoRecord = Bjl.SeatInfoRecord.newBuilder();
                infoRecord.setNickName(seatPlayer.getNickName());
                infoRecord.setGold(seatPlayer.getGold());
                infoRecord.setIcon(seatPlayer.getIcon());
                infoRecord.setPlayerId(seatPlayer.getPlayerId());
                msg.addSeatWin(infoRecord);
            }
        }
        this.serverHandler.writeMsg(seat.playerId, Bjl.BjlMessageId.ResSeatWin_ID_VALUE, msg.build());
    }

    public void sendMsg(Long playerId, int msgId, Message msg) {
        this.serverHandler.writeMsg(playerId, msgId, msg);
    }

    /**
     * 向同桌的玩家(包括自己)发送消息
     */
    public void sendTablePlayersMsg(Table table, int msgId, Message msg) {
        List<Seat> seats = table.seats;
        for (int i = 0; i < seats.size(); i++) {
            Seat seat = seats.get(i);
            if (seat.playerId > 0) {
                this.serverHandler.writeMsg(seat.playerId, msgId, msg);
            }
        }
    }

    /**
     * 发送洗牌消息
     *
     * @param table
     * @param cutCard
     * @param time
     */
    public void sendShuffleMsg(Table table, int time, int cutCard) {
        Bjl.ResShuffleInfo.Builder msg = Bjl.ResShuffleInfo.newBuilder();
        msg.setRestCardNum(table.cards.size());
        msg.setShuffleTime(time);
        msg.setCutCard(cutCard);
        this.sendTablePlayersMsg(table, Bjl.BjlMessageId.ResShuffleInfo_ID_VALUE, msg.build());
    }

    public void sendSyncSeat(Table table, long playerId, long gold) {
        Bjl.ResSyncSeat.Builder msg = Bjl.ResSyncSeat.newBuilder();
        msg.setGold(gold);
        msg.setPlayerId(playerId);

        List<Seat> seats = table.rankSeats;
        for (int i = 0; i < seats.size(); i++) {
            Seat seat = seats.get(i);
            if ((seat != null) && (seat.playerId > 0)) {
                this.serverHandler.writeMsg(seat.playerId, Bjl.BjlMessageId.ResSyncSeat_ID_VALUE, msg.build());
            }
        }
    }

    /**
     * 发送金币充值消息(选场大厅使用)
     */
    public void sendGoldPayMsg(Seat seat, long payGold) {
        com.dmg.common.pb.java.Bjl.ResGoldPay.Builder msg = com.dmg.common.pb.java.Bjl.ResGoldPay.newBuilder();
        msg.setPlayerId(seat.playerId);
        msg.setGold(seat.gold);
        msg.setPaygold(payGold);
        this.sendTablePlayersMsg(seat.table, com.dmg.common.pb.java.Bjl.BjlMessageId.ResGoldPay_ID_VALUE, msg.build());
    }

    /**
     * 发送金币充值消息
     */
    public void sendGoldPayMsg(long playerId, long gold, long payGold) {
        com.dmg.common.pb.java.Bjl.ResGoldPay.Builder msg = com.dmg.common.pb.java.Bjl.ResGoldPay.newBuilder();
        msg.setPlayerId(playerId);
        msg.setGold(gold);
        msg.setPaygold(payGold);
        this.serverHandler.writeMsg(playerId, com.dmg.common.pb.java.Bjl.BjlMessageId.ResGoldPay_ID_VALUE, msg.build());
    }
}
