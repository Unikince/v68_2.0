package com.dmg.redblackwarserver.state.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.quartz.SchedulerException;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.alibaba.fastjson.JSON;
import com.dmg.common.core.config.RedisRegionConfig;
import com.dmg.common.core.util.SpringUtil;
import com.dmg.common.starter.rocketmq.core.producer.MQProducer;
import com.dmg.redblackwarserver.common.model.BasePlayer;
import com.dmg.redblackwarserver.common.model.BaseRobot;
import com.dmg.redblackwarserver.common.model.BaseRoom;
import com.dmg.redblackwarserver.common.model.Poker;
import com.dmg.redblackwarserver.common.result.MessageResult;
import com.dmg.redblackwarserver.manager.RoomManager;
import com.dmg.redblackwarserver.manager.TimerManager;
import com.dmg.redblackwarserver.model.Room;
import com.dmg.redblackwarserver.model.RoomStatus;
import com.dmg.redblackwarserver.model.Seat;
import com.dmg.redblackwarserver.model.Table;
import com.dmg.redblackwarserver.model.constants.Combination;
import com.dmg.redblackwarserver.model.constants.D;
import com.dmg.redblackwarserver.model.dto.HandPokerDTO;
import com.dmg.redblackwarserver.model.dto.SettleResultDTO;
import com.dmg.redblackwarserver.model.dto.SettleResultDTO.SettleInfo;
import com.dmg.redblackwarserver.quarz.RoomActionDelayTask;
import com.dmg.redblackwarserver.service.cache.PlayerService;
import com.dmg.redblackwarserver.state.RoomState;
import com.dmg.redblackwarserver.tcp.server.MessageIdConfig;
import com.dmg.server.common.model.dto.GameRecordDTO;
import com.dmg.server.common.util.RoundIdUtils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 结算阶段
 *
 * @author Administrator
 */
@Slf4j
public class SettleState implements RoomState {
    private BaseRoom room;

    public SettleState(BaseRoom room) {
        this.room = room;
    }

    @Override
    public State getState() {
        return State.SETTLE;
    }

    @Override
    public void action() {
        Room room = (Room) this.room;
        if (room.getRoomStatus() != RoomStatus.BET) {
            log.error("==> 结算失败,房间{} 状态有误,当前状态为{}", room.getRoomId(), room.getRoomStatus());
            return;
        }
        room.setRoomStatus(RoomStatus.SETTLE);
        Double controlExecuteRate = RoomManager.intance().getControlExecuteRate(room.getLevel());
        // 是否控制
        boolean control = false;
        // false输 true赢
        boolean win = false;
        int random = RandomUtil.randomInt(100);
        // controlExecuteRate>0控制赢 <0控制输
        if (controlExecuteRate >= 0) {
            win = true;
            if (random <= controlExecuteRate) {
                control = true;
            }
        } else {
            if (random <= Math.abs(controlExecuteRate)) {
                control = true;
            }
        }

        if (control) {
            this.dealAlgorithm(win);
        } else {
            this.dealNormal(room);
        }
        StringRedisTemplate redisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
        redisTemplate.opsForList().rightPush("redblackwar_control:" + RoomManager.intance().getGameId() + "_" + room.getLevel(), control + "");
        log.info("场次:{}, 控制率为:{},随机数为:{},当局是否控制:{},控制输赢为:{}", room.getLevel(), controlExecuteRate, random, control, win);

        // 计算输赢
        this.calculateWin();

        // 结算
        this.settleResult();
        try {
            room.setCountdownTime(System.currentTimeMillis() + D.SETTLE_TIME);
            TimerManager.instance().submitDelayWork(RoomActionDelayTask.class, D.SETTLE_TIME, room.getRoomId());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        room.changeState();
        log.info("场次: {} , 盈利: {} ,抽水: {}", room.getLevel(), RoomManager.intance().getRoomWinGold(room.getLevel()), RoomManager.intance().getPumpMap().get(room.getLevel()));
        redisTemplate.opsForValue().set(RedisRegionConfig.FILE_WIN_GOLD_KEY + ":" + RoomManager.intance().getGameId() + "_" + room.getLevel(), RoomManager.intance().getRoomWinGold(room.getLevel()).intValue() + "");
        redisTemplate.opsForValue().set(RedisRegionConfig.FILE_PUMP_KEY + ":" + RoomManager.intance().getGameId() + "_" + room.getLevel(), RoomManager.intance().getPumpMap().get(room.getLevel()).intValue() + "");
        redisTemplate.opsForList().rightPush("redblackwar:" + RoomManager.intance().getGameId() + "_" + room.getLevel(), RoomManager.intance().getRoomWinGold(room.getLevel()).intValue() + "");
        redisTemplate.opsForList().rightPush("redblackwar_control:" + RoomManager.intance().getGameId() + "_" + room.getLevel(), control + "");

    }

    /**
     * @description: 计算输赢分
     * @author mice
     * @date 2019/7/31
     */
    private void calculateWin() {
        Room room = (Room) this.room;
        log.info("==>房间:{},开始计算输赢分", room.getRoomId());
        for (Seat seat : room.getSeatMap().values()) {
            if (seat.getPlayer() instanceof BasePlayer) {
                if (seat.getUserBetChipTotal().isEmpty()) {
                    seat.setNoBetCount(seat.getNoBetCount() + 1);
                } else {
                    seat.setNoBetCount(0);
                }
            }
        }
        Table table1 = room.getTableMap().get(D.ONE);
        Table table2 = room.getTableMap().get(D.TWO);
        Table table3 = room.getTableMap().get(D.THREE);

        BigDecimal multiple;
        int cardType;
        int cardValue;
        // 结算红黑桌位
        if (this.judgeWinner(table1, table2)) {
            cardType = table1.getCardType();
            cardValue = table1.getPokerList().get(1).getValue();
            multiple = RoomManager.intance().getMultiple(room.getLevel(), table1.getCardType());
            this.settle(table1, table2);
        } else {
            cardType = table2.getCardType();
            cardValue = table2.getPokerList().get(1).getValue();
            multiple = RoomManager.intance().getMultiple(room.getLevel(), table2.getCardType());
            this.settle(table2, table1);
        }
        // 结算幸运一击桌位
        if (cardType > Combination.PAIR.getValue() || (cardType == Combination.PAIR.getValue() && (cardValue >= 9 || cardValue == 1))) {
            room.getWinTableMap().put(table3.getTableIndex(), 1);
            this.settleLuckyTable(table3, multiple, true);
        } else {
            room.getWinTableMap().put(table3.getTableIndex(), 0);
            this.settleLuckyTable(table3, multiple, false);
        }
        for (Seat seat : room.getSeatMap().values()) {
            if (seat.isReady()) {
                seat.getPlayer().setGold(seat.getPlayer().getGold().add(seat.getCurWinGold()));
            }
        }
    }

    /**
     * 结算幸运一击桌位
     *
     * @param table
     * @param multiple
     * @param win
     */
    private void settleLuckyTable(Table table, BigDecimal multiple, boolean win) {
        Room room = (Room) this.room;
        BigDecimal pump = room.getPump();
        for (Seat seat : room.getSeatMap().values()) {
            if (seat.getUserBetChipTotal().containsKey(table.getTableIndex())) {
                BigDecimal betChip = seat.getUserBetChipTotal().get(table.getTableIndex());
                BigDecimal winGold;
                if (win) {
                    BigDecimal allWiGold = betChip.multiply(multiple);
                    BigDecimal pumpGold = allWiGold.multiply(pump).setScale(2, BigDecimal.ROUND_HALF_UP);
                    seat.getUserPumpGoldTotal().put(table.getTableIndex(), pumpGold);
                    winGold = allWiGold.subtract(pumpGold);
                    seat.getWinGoldMap().put(table.getTableIndex(), winGold);
                    if (!(seat.getPlayer() instanceof BaseRobot)) {
                        RoomManager.intance().addRoomWinGold(room.getLevel(), winGold.negate());
                        RoomManager.intance().addPump(room.getLevel(), pumpGold);
                    }
                } else {
                    winGold = betChip.negate();
                    seat.getWinGoldMap().put(table.getTableIndex(), winGold);
                    seat.getUserPumpGoldTotal().put(table.getTableIndex(), BigDecimal.ZERO);
                    if (!(seat.getPlayer() instanceof BaseRobot)) {
                        RoomManager.intance().addRoomWinGold(room.getLevel(), betChip);
                    }
                }
                seat.setCurWinGold(seat.getCurWinGold().add(winGold));
                seat.setTotalWinGold(seat.getTotalWinGold().add(winGold));
                seat.getPlayer().setGold(seat.getPlayer().getGold().add(betChip));

            }
        }
    }

    /**
     * 结算输赢
     *
     * @param table1
     * @param table2
     */
    private void settle(Table table1, Table table2) {
        Room room = (Room) this.room;
        BigDecimal pump = room.getPump();
        room.getWinTableMap().put(table1.getTableIndex(), 1);
        room.getWinTableMap().put(table2.getTableIndex(), 0);
        this.record(table1);

        TreeMap<String, Seat> seatMap = new TreeMap<>(room.getSeatMap());
        for (Seat seat : seatMap.values()) {
            this.settleSeat(pump, table1.getTableIndex(), seat, true);
            this.settleSeat(pump, table2.getTableIndex(), seat, false);
        }
    }

    /**
     * 结算座位输赢
     *
     * @param pump
     * @param index
     * @param seat
     * @param win
     */
    private void settleSeat(BigDecimal pump, String index, Seat seat, boolean win) {
        if (seat.getUserBetChipTotal().containsKey(index)) {
            BigDecimal allWiGold = seat.getUserBetChipTotal().get(index);
            BigDecimal winGold;
            if (win) {
                BigDecimal pumpGold = allWiGold.multiply(pump).setScale(2, BigDecimal.ROUND_HALF_UP);
                winGold = allWiGold.subtract(pumpGold);
                seat.getUserPumpGoldTotal().put(index, pumpGold);
                seat.getWinGoldMap().put(index, winGold);
                if (!(seat.getPlayer() instanceof BaseRobot)) {
                    RoomManager.intance().addRoomWinGold(this.room.getLevel(), winGold.negate());
                    RoomManager.intance().addPump(this.room.getLevel(), pumpGold);
                }
            } else {
                winGold = allWiGold.negate();
                seat.getWinGoldMap().put(index, winGold);
                seat.getUserPumpGoldTotal().put(index, BigDecimal.ZERO);
                if (!(seat.getPlayer() instanceof BaseRobot)) {
                    RoomManager.intance().addRoomWinGold(this.room.getLevel(), allWiGold);
                }
            }

            seat.setCurWinGold(seat.getCurWinGold().add(winGold));
            seat.setTotalWinGold(seat.getTotalWinGold().add(winGold));
            seat.getPlayer().setGold(seat.getPlayer().getGold().add(allWiGold));
        }
    }

    /**
     * 添加走势图记录
     *
     * @param table
     */
    private void record(Table table) {
        Room room = (Room) this.room;
        Map<String, Integer> map = new HashMap<>();
        map.put("type", table.getCardType());
        map.put("tableIndex", Integer.valueOf(table.getTableIndex()));
        int cardType = table.getCardType();
        int cardValue = table.getPokerList().get(1).getValue();
        if (cardType > Combination.PAIR.getValue() || (cardType == Combination.PAIR.getValue() && (cardValue >= 9 || cardValue == 1))) {
            map.put("luck", 1);
        } else {
            map.put("luck", 0);
        }
        room.getReportFormList().addFirst(map);
        if (room.getReportFormList().size() > 100) {
            room.getReportFormList().removeLast();
        }
    }

    /**
     * @param bankerTable 庄家牌
     * @param table
     * @return boolean
     * @description: 判断PK的玩家谁输谁赢
     * @author mice
     * @date 2019/7/31
     */
    private boolean judgeWinner(Table bankerTable, Table table) {
        List<Poker> bankerPokerList = new ArrayList<>();
        bankerPokerList.addAll(bankerTable.getPokerList());
        Collections.sort(bankerPokerList);
        List<Poker> tablePokerList = new ArrayList<>();
        tablePokerList.addAll(table.getPokerList());
        Collections.sort(tablePokerList);
        if (bankerTable.getCardType() != 0 && table.getCardType() != 6 || bankerTable.getCardType() != 6 && table.getCardType() != 0) {
            // 庄家赢
            if (bankerTable.getCardType() > table.getCardType()) {
                return true;
                // 牌型相同时进行比较
            } else if (bankerTable.getCardType() == table.getCardType()) {
                return this.operOneWinTheSame(bankerPokerList, tablePokerList, bankerTable.getCardType());
            } else {
                return false;
            }
        } else if (bankerTable.getCardType() == 0 && table.getCardType() == 6) {
            return true;
        } else if (bankerTable.getCardType() == 6 && table.getCardType() == 0) {
            return false;
        }
        return false;

    }

    /**
     * @param bankerPokerList
     * @param tablePokerList
     * @param cardsType
     * @return boolean
     * @description: 牌型相同比较
     * @author mice
     * @date 2019/8/2
     */
    private boolean operOneWinTheSame(List<Poker> bankerPokerList, List<Poker> tablePokerList, int cardsType) {
        int[] pokers = new int[3];
        int[] bePokers = new int[3];
        for (int i = 0; i < bankerPokerList.size(); i++) {
            int value = bankerPokerList.get(i).getValue() == 1 ? 14 : bankerPokerList.get(i).getValue();
            pokers[i] = value;
        }
        for (int i = 0; i < tablePokerList.size(); i++) {
            int value = tablePokerList.get(i).getValue() == 1 ? 14 : tablePokerList.get(i).getValue();
            bePokers[i] = value;
        }
        switch (Combination.forValue(cardsType)) {
            case PAIR:
                if (pokers[1] > bePokers[1]) {
                    return true;
                } else if (pokers[1] < bePokers[1]) {
                    return false;
                } else {
                    boolean single = this.singleBiger(pokers);
                    boolean beSingle = this.singleBiger(bePokers);
                    if (single) {
                        if (beSingle) {
                            if (pokers[0] > bePokers[0]) {
                                return true;
                            } else if (pokers[0] < bePokers[0]) {
                                return false;
                            } else {
                                return this.pokerTypeBiger(bankerPokerList, single);
                            }
                        } else {
                            return true;
                        }
                    } else {
                        if (beSingle) {
                            return false;
                        } else {
                            if (pokers[0] > bePokers[0]) {
                                return true;
                            } else if (pokers[0] < bePokers[0]) {
                                return false;
                            } else {
                                return this.pokerTypeBiger(bankerPokerList, single);
                            }
                        }
                    }
                }
            case LEOPARD:
                return this.judgeBiger(pokers, bePokers);
            case PROGRESSION:
            case STRAIGHTFLUSH:
                // 顺子 A 2 3 为最小顺子 A点数需转回为1
                if (pokers[0] == 14 && pokers[1] == 2) {
                    pokers[0] = 1;
                }
                if (bePokers[0] == 14 && bePokers[1] == 2) {
                    bePokers[0] = 1;
                }
            case FLUSH:
            case HIGHCARD:
                if (Arrays.equals(pokers, bePokers)) {
                    if (bankerPokerList.get(0).getType() > tablePokerList.get(0).getType()) {
                        return true;
                    }
                    return false;
                } else {
                    return this.judgeBiger(pokers, bePokers);
                }
            default:
                return false;
        }

    }

    /**
     * 花色为4黑桃最大
     *
     * @param pokerList
     * @param single
     * @return
     */
    private boolean pokerTypeBiger(List<Poker> pokerList, boolean single) {
        if (single) {
            if (pokerList.get(1).getType() == 4 || pokerList.get(2).getType() == 4) {
                return true;
            }
        } else {
            if (pokerList.get(0).getType() == 4 || pokerList.get(1).getType() == 4) {
                return true;
            }
        }
        return false;
    }

    /**
     * 对子中单牌点数比对子点数大
     *
     * @param pokers
     * @return
     */
    private boolean singleBiger(int[] pokers) {
        if (pokers[0] != pokers[1]) {
            return true;
        }
        return false;
    }

    /**
     * 依次比较扑克点数大小
     *
     * @param pokers
     * @param bePokers
     * @return
     */
    private boolean judgeBiger(int[] pokers, int[] bePokers) {
        for (int i = 0; i < pokers.length; i++) {
            if (pokers[i] > bePokers[i]) {
                return true;
            } else if (pokers[i] < bePokers[i]) {
                return false;
            }
        }
        return false;
    }

    /**
     * @return void
     * @description: 结算结果
     * @author mice
     * @date 2019/7/31
     */
    private void settleResult() {
        Room room = (Room) this.room;
        log.info("==>房间:{},结算结果", room.getRoomId());
        SettleResultDTO settleResultDTO = new SettleResultDTO();
        Seat bankerSeat = room.getBanker();
        SettleResultDTO.SettleInfo bankerSettleInfo = new SettleResultDTO.SettleInfo();
        bankerSettleInfo.setCurWinGold(bankerSeat.getCurWinGold());
        bankerSettleInfo.setWinGoldMap(bankerSeat.getWinGoldMap());
        bankerSettleInfo.setUserId(bankerSeat.getPlayer().getUserId());
        bankerSettleInfo.setNickname(bankerSeat.getPlayer().getNickname());
        bankerSettleInfo.setHeadIcon(bankerSeat.getPlayer().getHeadIcon());

        settleResultDTO.setBankerSettleInfo(bankerSettleInfo);
        settleResultDTO.setWinTableMap(room.getWinTableMap());
        settleResultDTO.setTableMap(room.getTableMap());
        settleResultDTO.setMultipleConfigMap(room.getMultipleConfigMap());
        settleResultDTO.getWinGoldRank().add(bankerSettleInfo);

        SettleResultDTO.Winner winner = new SettleResultDTO.Winner();
        for (Seat seat : room.getSeatMap().values()) {
            if (seat.isReady() && seat.getCurWinGold().doubleValue() > 0) {
                if (winner.getWinGold().compareTo(seat.getCurWinGold()) < 0) {
                    winner.setWinner(seat.getPlayer());
                    winner.setWinGold(seat.getCurWinGold());
                }
            }
        }
        settleResultDTO.setWinner(winner);
        // 统计外场人员赢钱数
        BigDecimal outfieldWinGold = new BigDecimal(0);
        for (Seat seat : room.getSeatMap().values()) {
            if (seat.getSeatIndex() > 6 && seat.isReady()) {
                outfieldWinGold = outfieldWinGold.add(seat.getCurWinGold());
                settleResultDTO.getWinGoldRank().add(this.settleInfoWarpper(seat));
            }
        }
        settleResultDTO.setOutfieldWinGold(outfieldWinGold);
        // 包装内场人员数据
        for (Seat seat : room.getInfieldSeatMap().values()) {
            SettleResultDTO.SettleInfo infieldSettleInfo = this.settleInfoWarpper(seat);
            settleResultDTO.getInfieldSettleInfoMap().put(seat.getSeatIndex() + "", infieldSettleInfo);
            settleResultDTO.getWinGoldRank().add(infieldSettleInfo);
        }

        settleResultDTO.getWinGoldRank().sort(new Comparator<SettleResultDTO.SettleInfo>() {
            @Override
            public int compare(SettleResultDTO.SettleInfo o1, SettleResultDTO.SettleInfo o2) {
                return o2.getCurWinGold().compareTo(o1.getCurWinGold());
            }
        });
        if (settleResultDTO.getWinGoldRank().size() > 10) {
            List<SettleInfo> winGoldRank = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                winGoldRank.add(settleResultDTO.getWinGoldRank().get(i));
            }
            settleResultDTO.getWinGoldRank().clear();
            settleResultDTO.getWinGoldRank().addAll(winGoldRank);
        }

        // 向游戏中的玩家推送数据
        for (Seat seat : room.getSeatMap().values()) {
            if (!seat.isReady()) {
                SettleResultDTO.SettleInfo selfSettleInfo = new SettleResultDTO.SettleInfo();
                settleResultDTO.setSelfSettleInfo(selfSettleInfo);
                MessageResult messageResult = new MessageResult(MessageIdConfig.SETTLE_RESULT_NTC, settleResultDTO);
                if (seat.getPlayer() instanceof BasePlayer) {
                    (seat.getPlayer()).push(messageResult);
                }
                continue;
            }
            SettleResultDTO.SettleInfo selfSettleInfo = this.settleInfoWarpper(seat);
            settleResultDTO.setSelfSettleInfo(selfSettleInfo);
            MessageResult messageResult = new MessageResult(MessageIdConfig.SETTLE_RESULT_NTC, settleResultDTO);
            if (seat.getPlayer() instanceof BasePlayer) {
                (seat.getPlayer()).push(messageResult);
            }
        }
        PlayerService playerCacheService = SpringUtil.getBean(PlayerService.class);
        try {
            List<Seat> seats = new ArrayList<>();
            for (Seat seat : room.getSeatMap().values()) {
                if (!seat.isReady()) {
                    continue;
                }
                Seat tseat = new Seat();
                BeanUtil.copyProperties(seat, tseat);
                seats.add(tseat);
            }
            String roundCode = RoundIdUtils.getGameRecordId(String.valueOf(RoomManager.intance().getGameId()), String.valueOf(room.getRoomId()));
            for (Seat seat : seats) {
                BigDecimal changeGold = seat.getCurWinGold();
                BigDecimal betChipTotal = BigDecimal.ZERO;
                for (BigDecimal t : seat.getUserBetChipTotal().values()) {
                    betChipTotal = betChipTotal.add(t);
                }

                if (!(seat.getPlayer() instanceof BaseRobot)) {
                    playerCacheService.settle(seat.getPlayer().getUserId(), changeGold.add(betChipTotal));
                }

                BigDecimal pumpTotal = BigDecimal.ZERO;
                for (BigDecimal t : seat.getUserPumpGoldTotal().values()) {
                    pumpTotal = pumpTotal.add(t);
                }
                // 记录战绩

                Map<String, Object> gameResult = new HashMap<>();
                Table table1 = room.getTableMap().get(D.ONE);
                Table table2 = room.getTableMap().get(D.TWO);
                Table table3 = room.getTableMap().get(D.THREE);

                gameResult.put("红方牌组", table1.getPokerList());
                gameResult.put("红方牌型", table1.getCardType());
                gameResult.put("黑方牌组", table2.getPokerList());
                gameResult.put("黑方牌型", table2.getCardType());

                if (this.judgeWinner(table1, table2)) {
                    gameResult.put("赢家", "红赢");
                    BigDecimal multiple = RoomManager.intance().getMultiple(room.getLevel(), table1.getCardType());
                    gameResult.put("幸运倍数", multiple.intValue());
                } else {
                    gameResult.put("赢家", "红赢");
                    BigDecimal multiple = RoomManager.intance().getMultiple(room.getLevel(), table2.getCardType());
                    gameResult.put("幸运倍数", multiple.intValue());
                }

                gameResult.put("红方下注", seat.getUserBetChipTotal().get(table1.getTableIndex()));
                gameResult.put("黑方下注", seat.getUserBetChipTotal().get(table2.getTableIndex()));
                gameResult.put("幸运下注", seat.getUserBetChipTotal().get(table3.getTableIndex()));

                gameResult.put("红方输赢", seat.getWinGoldMap().get(table1.getTableIndex()));
                gameResult.put("黑方输赢", seat.getWinGoldMap().get(table2.getTableIndex()));
                gameResult.put("幸运输赢", seat.getWinGoldMap().get(table3.getTableIndex()));

                this.recordData(seat.getPlayer() instanceof BaseRobot, room, seat.getPlayer(), seat, betChipTotal, pumpTotal, gameResult, roundCode);
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * 保存战绩
     */
    private void recordData(boolean isRobot, Room room, BasePlayer player, Seat seat, BigDecimal betChipTotal, BigDecimal pump, Map<String, Object> gameResult, String roundCode) {
        MQProducer defautProducer = SpringUtil.getBean(MQProducer.class);
        List<Object> gameResultList = new ArrayList<>();
        gameResultList.add(gameResult);
        try {
            defautProducer.sendAsync(JSON.toJSONString(GameRecordDTO.<Object>builder()
                    .gameId(Integer.valueOf(Integer.parseInt(RoomManager.intance().getGameId())))// 游戏id
                    .gameDate(new Date())// 游戏牌局时间
                    .userId((long) player.getUserId())// 用户id
                    .userName(player.getNickname())// 用户昵称
                    .fileId(room.getLevel())// 场次id
                    .fileName(RoomManager.intance().getName(room.getLevel()))// 场次名称
                    .afterGameGold(player.getGold())// 游戏后金币
                    .beforeGameGold(player.getGold().subtract(seat.getCurWinGold()))// 游戏前金币
                    .betsGold(betChipTotal)// 下注金币
                    .winLosGold(seat.getCurWinGold())// 输赢金币
                    .serviceCharge(pump)// 服务费
                    .isRobot(isRobot)// 是否是机器人
                    .roundCode(roundCode)
                    .gameResult(gameResultList).build()));
        } catch (Exception e) {
            log.error("战绩信息发送异常:{}", e);
        }
    }

    private SettleResultDTO.SettleInfo settleInfoWarpper(Seat seat) {
        SettleResultDTO.SettleInfo settleInfo = new SettleResultDTO.SettleInfo();
        settleInfo.setGold(seat.getPlayer().getGold());
        settleInfo.setWinGoldMap(seat.getWinGoldMap());
        settleInfo.setCurWinGold(seat.getCurWinGold());
        settleInfo.setSeatIndex(seat.getSeatIndex());
        settleInfo.setUserId(seat.getPlayer().getUserId());
        settleInfo.setNickname(seat.getPlayer().getNickname());
        settleInfo.setHeadIcon(seat.getPlayer().getHeadIcon());
        return settleInfo;
    }

    /**
     * @param room
     * @return void
     * @description: 正常发牌
     * @author mice
     * @date 2019/7/31
     */
    void dealNormal(Room room) {
        log.info("==>房间:{},开始正常发牌", room.getRoomId());
        // 洗牌
        Collections.shuffle(room.getPokerList());
        LinkedList<Poker> tablePokerList = room.getPokerList();
        int pokerIndex = 0;
        for (Table table : room.getTableMap().values()) {
            if (table.getTableIndex().equals(D.THREE)) {
                continue;
            }
            List<Poker> pokers = new ArrayList<>();
            for (int j = 1; j < 4; j++) {
                pokers.add(tablePokerList.get(pokerIndex++));
            }
            table.setPokerList(pokers);
            table.getPokerType();
        }
        boolean outMaxPayout = this.normalVirtualComputing(room);
        if (outMaxPayout) {
            log.warn("==> 房间:{},超过最大赔付限额,重新发牌", room.getRoomId());
            this.dealNormal(room);
        }
    }

    /**
     * @return void
     * @description: 算法发牌
     * @author mice
     * @date 2019/7/31
     */
    private void dealAlgorithm(boolean win) {
        Room room = (Room) this.room;
        log.info("==>房间:{},开始算法发牌", room.getRoomId());
        long start = System.currentTimeMillis();
        LinkedHashMap<Integer, HandPokerDTO> handPokerDTOMap = new LinkedHashMap<>();
        LinkedList<Poker> tablePokerList = room.getPokerList();
        RoomManager manager = RoomManager.intance();
        Collections.shuffle(tablePokerList);
        int pokerIndex = 0;
        List<Poker> pokers = null;
        int cardType = 0;
        for (int i = 1; i < 11; i++) {
            pokers = new ArrayList<>();
            for (int j = 1; j < 4; j++) {
                pokers.add(tablePokerList.get(pokerIndex++));
            }
            HandPokerDTO handPokerDTO = new HandPokerDTO(cardType, pokers);
            handPokerDTOMap.put(i, handPokerDTO);
        }

        int winCount = room.getWinMap().get(win) == null ? 0 : room.getWinMap().get(win);
        room.getWinMap().put(win, winCount + 1);
        log.info("==> 房间:{},当局输赢判定为:{},总输赢场次为:{}", room.getRoomId(), win, room.getWinMap());
        // 能赢的牌型 key:全排列key value:赢钱数额
        Map<Integer, BigDecimal> winGoldMap = new HashMap<>();
        Map<Integer, List<Integer>> arrangedMap = manager.getFullyArrangedMap();
        for (Integer key : arrangedMap.keySet()) {
            List<Integer> arrangedList = arrangedMap.get(key);
            int index = 0;
            for (Table table : room.getTableMap().values()) {
                if (table.getTableIndex().equals(D.THREE)) {
                    break;
                }
                table.setPokerList(handPokerDTOMap.get(arrangedList.get(index++)).getPokerList());
                table.getPokerType();
            }
            this.virtualComputing(key, winGoldMap, win);
        }
        List<Integer> arrangedList;
        if (winGoldMap.size() == 0) {
            arrangedList = manager.getFullyArrangedMap().get(RandomUtil.randomInt(1, manager.getFullyArrangedMap().size()));
        } else {
            // 赢多赢少可以在这里控制 根据winGoldMap的金额排序 赢钱最多的牌型组合
            // 当前为随机赢钱
//            Object[] winGoldKeys = winGoldMap.keySet().toArray();
//            arrangedList = RoomManager.intance().getFullyArrangedMap().get(winGoldKeys[RandomUtil.randomInt(0, winGoldKeys.length)]);
            Object[] winGoldKeys = winGoldMap.keySet().toArray();
            int index = RandomUtil.randomInt(0, winGoldKeys.length);
            arrangedList = manager.getFullyArrangedMap().get(winGoldKeys[index]);
            manager.setSystemGold(manager.getSystemGold().add(winGoldMap.get(winGoldKeys[index])));
            if (manager.getSystemGold().intValue() < manager.getWinLimit()) {
                manager.setNextRoundWin(true);
            } else {
                manager.setNextRoundWin(false);
            }
            manager.setWinLimit();
        }
        log.info("计算出的牌型组合为:{}", JSON.toJSONString(arrangedList));
        // 最后放入计算好的牌型
        int index = 0;
        for (Table table : room.getTableMap().values()) {
            if (table.getTableIndex().equals(D.THREE)) {
                break;
            }
            table.setPokerList(handPokerDTOMap.get(arrangedList.get(index++)).getPokerList());
            table.getPokerType();
        }
        log.info("房间{},发牌耗时:{}", room.getRoomId(), System.currentTimeMillis() - start);
    }

    /**
     * @param key
     * @param winGoldMap
     * @param win
     * @return void
     * @description: 虚拟计算系统赢钱的组合
     * @author mice
     * @date 2019/8/5
     */
    private void virtualComputing(int key, Map<Integer, BigDecimal> winGoldMap, boolean win) {
        Room room = (Room) this.room;
        Table table1 = room.getTableMap().get(D.ONE);
        Table table2 = room.getTableMap().get(D.TWO);
        Table table3 = room.getTableMap().get(D.THREE);
        BigDecimal sysWinGold = new BigDecimal(0);
        BigDecimal multiple;
        if (this.judgeWinner(table1, table2)) {
            multiple = RoomManager.intance().getMultiple(room.getLevel(), table1.getCardType());
            int cardValue = table1.getPokerList().get(1).getValue();
            sysWinGold = sysWinGold.add(table2.getPlayerBetChipTotal()).subtract(table1.getPlayerBetChipTotal());
            if (table1.getCardType() > Combination.PAIR.getValue() || (table1.getCardType() == Combination.PAIR.getValue() && (cardValue >= 9 || cardValue == 1))) {
                sysWinGold = sysWinGold.subtract(table3.getPlayerBetChipTotal().multiply(multiple));
            } else {
                sysWinGold = sysWinGold.add(table3.getPlayerBetChipTotal());
            }
        } else {
            multiple = RoomManager.intance().getMultiple(room.getLevel(), table2.getCardType());
            int cardValue = table2.getPokerList().get(1).getValue();
            sysWinGold = sysWinGold.add(table1.getPlayerBetChipTotal()).subtract(table2.getPlayerBetChipTotal());
            if (table2.getCardType() > Combination.PAIR.getValue() || (table2.getCardType() == Combination.PAIR.getValue() && (cardValue >= 9 || cardValue == 1))) {
                sysWinGold = sysWinGold.subtract(table3.getPlayerBetChipTotal().multiply(multiple));
            } else {
                sysWinGold = sysWinGold.add(table3.getPlayerBetChipTotal());
            }
        }
        if ((win && sysWinGold.compareTo(BigDecimal.ZERO) > 0) || (!win && sysWinGold.compareTo(BigDecimal.ZERO) < 0)) {
            winGoldMap.put(key, sysWinGold);
        }
    }

    /**
     * @param room
     * @return void
     * @description: 正常发牌计算
     * @author mice
     * @date 2019/8/5
     */
    private boolean normalVirtualComputing(Room room) {
        Table table1 = room.getTableMap().get(D.ONE);
        Table table2 = room.getTableMap().get(D.TWO);
        Table table3 = room.getTableMap().get(D.THREE);
        BigDecimal sysWinGold = new BigDecimal(0);
        BigDecimal multiple;
        if (this.judgeWinner(table1, table2)) {
            multiple = RoomManager.intance().getMultiple(room.getLevel(), table1.getCardType());
            int cardValue = table1.getPokerList().get(1).getValue();
            sysWinGold = sysWinGold.add(table2.getPlayerBetChipTotal()).subtract(table1.getPlayerBetChipTotal());
            if (table1.getCardType() > Combination.PAIR.getValue() || (table1.getCardType() == Combination.PAIR.getValue() && (cardValue >= 9 || cardValue == 1))) {
                sysWinGold = sysWinGold.subtract(table3.getPlayerBetChipTotal().multiply(multiple));
            } else {
                sysWinGold = sysWinGold.add(table3.getPlayerBetChipTotal());
            }
        } else {
            multiple = RoomManager.intance().getMultiple(room.getLevel(), table2.getCardType());
            int cardValue = table2.getPokerList().get(1).getValue();
            sysWinGold = sysWinGold.add(table1.getPlayerBetChipTotal()).subtract(table2.getPlayerBetChipTotal());
            if (table2.getCardType() > Combination.PAIR.getValue() || (table2.getCardType() == Combination.PAIR.getValue() && (cardValue >= 9 || cardValue == 1))) {
                sysWinGold = sysWinGold.subtract(table3.getPlayerBetChipTotal().multiply(multiple));
            } else {
                sysWinGold = sysWinGold.add(table3.getPlayerBetChipTotal());
            }
        }

        if (RoomManager.intance().getMaxPayout(room.getLevel()) + sysWinGold.intValue() <= 0 && RoomManager.intance().getRoomWinGold(room.getLevel()).intValue() < RoomManager.intance().getMaxPayoutReferenceValue(room.getLevel())) {
            return true;
        }
        return false;
    }

    /**
     * 获取特殊牌型
     *
     * @return
     */
    public List<Poker> getSpecialPoker() {

        List<Poker> pokers = new ArrayList<>();
        int random = RandomUtil.randomInt(10);
        int type = RandomUtil.randomInt(1, 5);
        Set<Integer> set = new HashSet<>();
        int value = 0;
        switch (random) {
            case 5:
            case 6:
            case 7:
                value = RandomUtil.randomInt(1, 14);
                if (value > 11) {
                    value = 1;
                }
                set.add(value);
                set.add(value + 1);
                set.add(value + 2);
                break;
            case 8:
            case 9:
                value = RandomUtil.randomInt(1, 14);
                break;
            default:
                while (set.size() < 3) {
                    set.add(RandomUtil.randomInt(1, 14));
                }
                break;
        }

        if (set.size() < 3) {// 豹子
            pokers.add(new Poker(value, 1));
            pokers.add(new Poker(value, 2));
            pokers.add(new Poker(value, RandomUtil.randomInt(3, 5)));

        } else {// 金花、顺金
            for (int val : set) {
                Poker poker = new Poker(val, type);
                pokers.add(poker);
            }
        }
        return pokers;
    }
}
