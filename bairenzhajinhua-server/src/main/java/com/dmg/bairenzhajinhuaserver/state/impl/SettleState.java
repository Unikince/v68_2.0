package com.dmg.bairenzhajinhuaserver.state.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.quartz.SchedulerException;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.dmg.bairenzhajinhuaserver.common.model.BasePlayer;
import com.dmg.bairenzhajinhuaserver.common.model.BaseRobot;
import com.dmg.bairenzhajinhuaserver.common.model.BaseRoom;
import com.dmg.bairenzhajinhuaserver.common.model.Poker;
import com.dmg.bairenzhajinhuaserver.common.result.MessageResult;
import com.dmg.bairenzhajinhuaserver.common.threadpool.FixedThreadPool;
import com.dmg.bairenzhajinhuaserver.common.utils.SpringContextUtil;
import com.dmg.bairenzhajinhuaserver.manager.RoomManager;
import com.dmg.bairenzhajinhuaserver.manager.TimerManager;
import com.dmg.bairenzhajinhuaserver.model.Room;
import com.dmg.bairenzhajinhuaserver.model.RoomStatus;
import com.dmg.bairenzhajinhuaserver.model.Seat;
import com.dmg.bairenzhajinhuaserver.model.Table;
import com.dmg.bairenzhajinhuaserver.model.constants.Combination;
import com.dmg.bairenzhajinhuaserver.model.constants.D;
import com.dmg.bairenzhajinhuaserver.model.dto.SettleResultDTO;
import com.dmg.bairenzhajinhuaserver.model.dto.SettleResultDTO.SettleInfo;
import com.dmg.bairenzhajinhuaserver.model.dto.TableInfo;
import com.dmg.bairenzhajinhuaserver.quarz.RoomActionDelayTask;
import com.dmg.bairenzhajinhuaserver.service.cache.PlayerService;
import com.dmg.bairenzhajinhuaserver.service.task.SaveGameRecordTask;
import com.dmg.bairenzhajinhuaserver.state.RoomState;
import com.dmg.bairenzhajinhuaserver.tcp.server.MessageIdConfig;
import com.dmg.common.core.config.RedisRegionConfig;
import com.dmg.common.core.util.SpringUtil;
import com.dmg.data.common.dto.SettleSendDto;
import com.dmg.server.common.model.dto.GameRecordDTO;
import com.dmg.server.common.model.dto.UserResultDTO;
import com.dmg.server.common.util.RoundIdUtils;

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
        if (room.getRoomStatus() != RoomStatus.DEAL) {
            log.error("==> 发牌失败,房间{} 状态有误,当前状态为{}", room.getRoomId(), room.getRoomStatus());
            return;
        }
        room.setRoomStatus(RoomStatus.SETTLE);
        this.calculateWin();
        this.settleResult();
        try {
            room.setCountdownTime(System.currentTimeMillis() + D.SETTLE_TIME);
            room.setNextcountdownTime(room.getCountdownTime());
            TimerManager.instance().submitDelayWork(RoomActionDelayTask.class, D.SETTLE_TIME, room.getRoomId());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        room.changeState();
        log.info("系统当前流水: {} ,场次: {} , 盈利: {} ,抽水: {}", RoomManager.intance().getSystemTurnover(), room.getLevel(), RoomManager.intance().getRoomWinGold(room.getLevel()), RoomManager.intance().getPumpMap().get(room.getLevel()));
        StringRedisTemplate redisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
        // redisTemplate.opsForValue().set(RedisRegionConfig.FILE_WIN_GOLD_KEY + ":" +
        // RoomManager.intance().getGameId() + "_" + room.getLevel(),
        // RoomManager.intance().getRoomWinGold(room.getLevel()).intValue() + "");
        redisTemplate.opsForValue().set(RedisRegionConfig.FILE_PUMP_KEY + ":" + RoomManager.intance().getGameId() + "_" + room.getLevel(), RoomManager.intance().getPumpMap().get(room.getLevel()).intValue() + "");
        // redisTemplate.opsForList().rightPush("bairenzhajinhua:" +
        // RoomManager.intance().getGameId() + "_" + room.getLevel(),
        // RoomManager.intance().getRoomWinGold(room.getLevel()).intValue() + "");
    }

    /**
     * @description: 计算输赢分
     * @author mice
     * @date 2019/7/31
     */
    private void calculateWin() {
        Room room = (Room) this.room;
        log.info("==>房间:{},开始计算输赢分", room.getRoomId());
        Table bankerTable = room.getTableMap().get(D.ONE);
        Seat bankerSeat = room.getBanker();
        BigDecimal bankerWinGold = new BigDecimal(0);
        PlayerService playerCacheService = SpringContextUtil.getBean(PlayerService.class);
        for (Seat seat : room.getSeatMap().values()) {
            if (!(seat.getPlayer() instanceof BaseRobot)) {
                if (seat.getUserBetChipTotal().isEmpty()) {
                    seat.setNoBetCount(seat.getNoBetCount() + 1);
                } else {
                    seat.setNoBetCount(0);
                }
            }
        }
        LinkedList<Object> winTableList = new LinkedList<>();
        LinkedList<Object> allTablePokerList = new LinkedList<>();

        Map<String, TableInfo> tableInfoMap = new HashMap<>();

        for (Table table : room.getTableMap().values()) {
            allTablePokerList.addAll(table.getPokerList().stream().map(Poker::getValue).collect(Collectors.toList()));
            TableInfo tableInfo = new TableInfo(table.getPokerList(), table.getCardType() + "",
                    RoomManager.intance().getMultiple(room.getLevel(), table.getCardType()).intValue());
            tableInfoMap.put(table.getTableIndex(), tableInfo);
            if (table.getTableIndex().equals(D.ONE)) {
                continue;
            }
            BigDecimal multiple;
            // 庄家赢
            if (this.judgeWinner(bankerTable, table)) {
                multiple = RoomManager.intance().getMultiple(room.getLevel(), bankerTable.getCardType());
                String seatIndex = table.getTableIndex();
                room.getWinTableMap().put(seatIndex, 0);
                this.record(table, 0);
                for (Seat seat : room.getSeatMap().values()) {
                    if (seat.getUserBetChipTotal().containsKey(seatIndex)) {
                        // 输金额=下注额*牌型倍数+下注额(因为下注时已经扣除玩家金币)
                        BigDecimal betChip = seat.getUserBetChipTotal().get(seatIndex);
                        BigDecimal loseGold = betChip.multiply(multiple).negate();
                        seat.setCurWinGold(seat.getCurWinGold().add(loseGold));
                        seat.setTotalWinGold(seat.getTotalWinGold().add(loseGold));
                        seat.getWinGoldMap().put(seatIndex, loseGold);
                    }
                }
                BigDecimal win = table.getBetChipTotal().multiply(multiple);
                bankerWinGold = bankerWinGold.add(win);
                bankerSeat.getWinGoldMap().put(seatIndex, win);

                if (room.isSystemBanker()) {
                    RoomManager.intance().addRoomWinGold(room.getLevel(), table.getPlayerBetChipTotal().multiply(multiple));
                } else {
                    RoomManager.intance().addRoomWinGold(room.getLevel(), table.getRobotBetChipTotal().multiply(multiple).negate());
                }

            } else {
                String seatIndex = table.getTableIndex();
                winTableList.add(seatIndex);
                multiple = RoomManager.intance().getMultiple(room.getLevel(), table.getCardType());
                room.getWinTableMap().put(seatIndex, 1);
                this.record(table, 1);
                for (Seat seat : room.getSeatMap().values()) {
                    if (seat.getUserBetChipTotal().containsKey(seatIndex)) {
                        BigDecimal betChip = seat.getUserBetChipTotal().get(seatIndex);
                        BigDecimal winGold = betChip.multiply(multiple);
                        seat.getWinGoldMap().put(seatIndex, winGold);
                        seat.setCurWinGold(seat.getCurWinGold().add(winGold));
                        seat.setTotalWinGold(seat.getTotalWinGold().add(winGold));
                    }
                }
                BigDecimal lose = table.getBetChipTotal().multiply(multiple).negate();
                bankerWinGold = bankerWinGold.add(lose);
                bankerSeat.getWinGoldMap().put(seatIndex, lose);

                if (room.isSystemBanker()) {
                    RoomManager.intance().addRoomWinGold(room.getLevel(), table.getPlayerBetChipTotal().multiply(multiple).negate());
                } else {
                    RoomManager.intance().addRoomWinGold(room.getLevel(), table.getRobotBetChipTotal().multiply(multiple));
                }
            }
        }
        List<Long> userIdList = new ArrayList<>();
        List<GameRecordDTO> gameRecordDTOS = new ArrayList<>();
        String roundCode = RoundIdUtils.getGameRecordId(String.valueOf(RoomManager.intance().getGameId()), String.valueOf(room.getRoomId()));
        for (Seat seat : room.getSeatMap().values()) {
            if (seat.isReady()) {
                if (seat.getPlayer().getUserId() == bankerSeat.getPlayer().getUserId()) {
                }
                BigDecimal betChipTotal = seat.getUserBetChipTotal().values().stream().reduce(BigDecimal::add).get();
                BigDecimal changeGold = seat.getCurWinGold();
                BigDecimal pump = BigDecimal.ZERO;
                if (changeGold.compareTo(BigDecimal.ZERO) > 0) {
                    pump = changeGold.multiply(RoomManager.intance().getPumpRate(room.getLevel()).divide(new BigDecimal(100)));
                    changeGold = changeGold.subtract(pump);
                    if (!(seat.getPlayer() instanceof BaseRobot) || !room.isSystemBanker()) {
                        RoomManager.intance().addPump(room.getLevel(), pump);
                    }
                }
                changeGold = changeGold.setScale(2, BigDecimal.ROUND_HALF_UP);
                seat.setCurWinGold(changeGold);
                // 加回下注额
                changeGold = changeGold.add(betChipTotal);
                seat.getPlayer().setGold(seat.getPlayer().getGold().add(changeGold));
                if (!(seat.getPlayer() instanceof BaseRobot)) {
                    playerCacheService.updatePlayer(seat.getPlayer());
                    SettleSendDto settleSendDto = SettleSendDto.builder()
                            .userId(seat.getPlayer().getUserId())
                            .changeGold(changeGold)
                            .build();
                    playerCacheService.settle(settleSendDto);
                }

                List<UserResultDTO<Poker>> userResultDTOS = new LinkedList<>();
                for (String key : tableInfoMap.keySet()) {

                    if (seat.getWinGoldMap().get(key) != null) {
                        changeGold = changeGold.add(seat.getWinGoldMap().get(key));
                        if (seat.getWinGoldMap().get(key).compareTo(BigDecimal.ZERO) < 0) {
                            seat.getWinGoldMap().put(key, BigDecimal.ZERO);
                        }
                    }

                    TableInfo tableInfo = tableInfoMap.get(key);

                    UserResultDTO userResultDTO = new UserResultDTO();
                    userResultDTO.setPokers(tableInfo.getPokers());
                    userResultDTO.setPokerType(tableInfo.getPokerType());
                    userResultDTO.setMul(tableInfo.getMul());
                    userResultDTO.setIsSys(room.isSystemBanker());
                    userResultDTO.setBetGold(seat.getUserBetChipTotal().get(key));
                    userResultDTO.setWinLosGold(seat.getWinGoldMap().get(key));
                    userResultDTOS.add(userResultDTO);
                }
                // 记录战绩
                this.recordData(seat.getPlayer() instanceof BaseRobot, room, seat.getPlayer(), seat, betChipTotal, pump, userResultDTOS, roundCode, userIdList, gameRecordDTOS);

            }
        }
        bankerSeat.setCurWinGold(bankerWinGold);
        BigDecimal bankerPump = BigDecimal.ZERO;
        if (bankerWinGold.compareTo(BigDecimal.ZERO) > 0) {
            bankerPump = bankerWinGold.multiply(RoomManager.intance().getPumpRate(room.getLevel()).divide(new BigDecimal(100)));
            bankerSeat.setCurWinGold(bankerWinGold.subtract(bankerPump));
            if (!room.isSystemBanker()) {
                RoomManager.intance().addPump(room.getLevel(), bankerPump);
            }
        }
        bankerSeat.getPlayer().setGold(bankerSeat.getPlayer().getGold().add(bankerSeat.getCurWinGold()));
        bankerSeat.setTotalWinGold(bankerSeat.getTotalWinGold().add(bankerSeat.getCurWinGold()));
        if (!room.isSystemBanker() && bankerSeat.getWinGoldMap().size() != 0) {
            SettleSendDto settleSendDto = SettleSendDto.builder()
                    .userId(bankerSeat.getPlayer().getUserId())
                    .changeGold(bankerSeat.getCurWinGold())
                    .build();
            playerCacheService.settle(settleSendDto);

            List<UserResultDTO<Poker>> userResultDTOS = new LinkedList<>();
            for (String key : tableInfoMap.keySet()) {
                TableInfo tableInfo = tableInfoMap.get(key);
                UserResultDTO userResultDTO = new UserResultDTO();
                userResultDTO.setPokers(tableInfo.getPokers());
                userResultDTO.setPokerType(tableInfo.getPokerType());
                userResultDTO.setMul(tableInfo.getMul());
                userResultDTO.setIsSys(room.isSystemBanker());
                userResultDTO.setBetGold(BigDecimal.ZERO);
                userResultDTO.setWinLosGold(bankerSeat.getWinGoldMap().get(key));
                userResultDTOS.add(userResultDTO);
            }
            this.recordData(false, room, bankerSeat.getPlayer(), bankerSeat, BigDecimal.ZERO,
                    bankerPump, userResultDTOS, roundCode, userIdList, gameRecordDTOS);
        }
        FixedThreadPool.submit(new SaveGameRecordTask(gameRecordDTOS, userIdList));
    }

    /**
     * 添加走势图记录
     *
     * @param table
     * @param win
     */
    private void record(Table table, int win) {
        Room room = (Room) this.room;
        if (!room.getReportFormMap().containsKey(table.getTableIndex())) {
            room.getReportFormMap().put(table.getTableIndex(), new LinkedList<>());
        }
        Map<String, Integer> map = new HashMap<>();
        map.put("type", table.getCardType());
        map.put("win", win);
        room.getReportFormMap().get(table.getTableIndex()).addFirst(map);
        if (room.getReportFormMap().get(table.getTableIndex()).size() > 10) {
            room.getReportFormMap().get(table.getTableIndex()).removeLast();
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
        log.info("==>房间:{},结算结果", this.room.getRoomId());
        Room room = (Room) this.room;
        SettleResultDTO settleResultDTO = new SettleResultDTO();
        // 庄家结算信息
        Seat bankerSeat = room.getBanker();
        SettleResultDTO.SettleInfo bankerSettleInfo = new SettleResultDTO.SettleInfo();
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
        settleResultDTO.setWinTableMap(room.getWinTableMap());
        bankerSettleInfo.setCurWinGold(bankerSeat.getCurWinGold());
        bankerSettleInfo.setWinGoldMap(bankerSeat.getWinGoldMap());
        bankerSettleInfo.setUserId(bankerSeat.getPlayer().getUserId());
        bankerSettleInfo.setNickname(bankerSeat.getPlayer().getNickname());
        bankerSettleInfo.setHeadIcon(bankerSeat.getPlayer().getHeadIcon());

        settleResultDTO.setBankerSettleInfo(bankerSettleInfo);
        settleResultDTO.setTableMap(room.getTableMap());
        settleResultDTO.setMultipleConfigMap(room.getMultipleConfigMap());
        settleResultDTO.getWinGoldRank().add(bankerSettleInfo);
//        settleResultDTO.setOrder(getOrder());
        // 统计外场人员赢钱数
        BigDecimal outfieldWinGold = new BigDecimal(0);
        for (Seat seat : room.getSeatMap().values()) {
            if (seat.getSeatIndex() > 8 && seat.isReady()) {
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
            if (seat.isLeave()) {
                continue;
            }
            if (!seat.isReady()) {
                SettleResultDTO.SettleInfo selfSettleInfo = new SettleResultDTO.SettleInfo();
                settleResultDTO.setSelfSettleInfo(selfSettleInfo);
                MessageResult messageResult = new MessageResult(MessageIdConfig.SETTLE_RESULT_NTC, settleResultDTO);
                if (!(seat.getPlayer() instanceof BaseRobot)) {
                    seat.getPlayer().push(messageResult);
                }
                continue;
            }
            SettleResultDTO.SettleInfo selfSettleInfo = this.settleInfoWarpper(seat);
            settleResultDTO.setSelfSettleInfo(selfSettleInfo);
            MessageResult messageResult = new MessageResult(MessageIdConfig.SETTLE_RESULT_NTC, settleResultDTO);
            if (!(seat.getPlayer() instanceof BaseRobot)) {
                seat.getPlayer().push(messageResult);
            }
        }
        // 向庄家推送数据
        if (!room.isSystemBanker()) {
            SettleResultDTO.SettleInfo selfSettleInfo = this.settleInfoWarpper(bankerSeat);
            settleResultDTO.setSelfSettleInfo(selfSettleInfo);
            MessageResult messageResult = new MessageResult(MessageIdConfig.SETTLE_RESULT_NTC, settleResultDTO);
            bankerSeat.getPlayer().push(messageResult);
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
     * 获取开牌顺序(从小到大)
     *
     * @return
     */
    private List<Integer> getOrder() {
        Room room = (Room) this.room;
        Map<String, Table> map = room.getTableMap();
        Map<Integer, Integer> winCountMap = new HashMap<>();
        for (int i = 2; i < 5; i++) {
            for (int j = i + 1; j < 6; j++) {
                if (this.judgeWinner(map.get(i + ""), map.get(j + ""))) {
                    int value = winCountMap.get(i) == null ? 0 : winCountMap.get(i);
                    winCountMap.put(i, value + 1);
                    int value1 = winCountMap.get(j) == null ? 0 : winCountMap.get(j);
                    winCountMap.put(j, value1 - 1);
                } else {
                    int value = winCountMap.get(i) == null ? 0 : winCountMap.get(i);
                    winCountMap.put(i, value - 1);
                    int value1 = winCountMap.get(j) == null ? 0 : winCountMap.get(j);
                    winCountMap.put(j, value1 + 1);
                }

            }
        }
        List<Integer> list = new ArrayList<>();
        for (int value : winCountMap.keySet()) {
            list.add(winCountMap.get(value));
        }
        Collections.sort(list);
        List<Integer> order = new ArrayList<>();
        for (int value : list) {
            for (Entry<Integer, Integer> entry : winCountMap.entrySet()) {
                if (entry.getValue() == value) {
                    order.add(entry.getKey());
                    break;
                }
            }
        }
        return order;
    }

    /**
     * 记录战绩
     *
     * @param room
     */
    @SuppressWarnings("unchecked")
    private void recordData(boolean isRobot, Room room, BasePlayer player, Seat seat, BigDecimal betChipTotal,
            BigDecimal pump, List<UserResultDTO<Poker>> userResultDTOS, String roundCode, List<Long> userIdList, List<GameRecordDTO> gameRecordDTOS) {
        GameRecordDTO gameRecordDTO = GameRecordDTO.<UserResultDTO<Poker>>builder()
                .gameId(Integer.valueOf(RoomManager.intance().getGameId()))
                .gameDate(new Date())
                .userId((long) player.getUserId())
                .userName(player.getNickname())
                .fileId(room.getLevel())
                .fileName(room.getFileName())
                .afterGameGold(player.getGold())
                .beforeGameGold(player.getGold().subtract(seat.getCurWinGold()))
                .betsGold(betChipTotal)
                .winLosGold(seat.getCurWinGold())
                .serviceCharge(pump)
                .isRobot(isRobot)
                .roundCode(roundCode)
                .gameResult(userResultDTOS).build();
        if (!isRobot) {
            userIdList.add((long) player.getUserId());
        }
        gameRecordDTOS.add(gameRecordDTO);
    }
}
