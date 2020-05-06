package com.dmg.bairenniuniuserver.service.logic.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dmg.bairenniuniuserver.common.model.BasePlayer;
import com.dmg.bairenniuniuserver.common.model.BaseRobot;
import com.dmg.bairenniuniuserver.common.model.Poker;
import com.dmg.bairenniuniuserver.common.result.MessageResult;
import com.dmg.bairenniuniuserver.common.threadpool.FixedThreadPool;
import com.dmg.bairenniuniuserver.manager.RoomManager;
import com.dmg.bairenniuniuserver.manager.TimerManager;
import com.dmg.bairenniuniuserver.model.Room;
import com.dmg.bairenniuniuserver.model.RoomStatus;
import com.dmg.bairenniuniuserver.model.Seat;
import com.dmg.bairenniuniuserver.model.Table;
import com.dmg.bairenniuniuserver.model.constants.D;
import com.dmg.bairenniuniuserver.model.dto.HandPokerDTO;
import com.dmg.bairenniuniuserver.model.dto.PokerInfo;
import com.dmg.bairenniuniuserver.model.dto.SettleResultDTO;
import com.dmg.bairenniuniuserver.model.dto.SettleResultDTO.SettleInfo;
import com.dmg.bairenniuniuserver.model.dto.TableInfo;
import com.dmg.bairenniuniuserver.quarz.StartGameDelayTask;
import com.dmg.bairenniuniuserver.service.PushService;
import com.dmg.bairenniuniuserver.service.cache.PlayerService;
import com.dmg.bairenniuniuserver.service.cache.RobotCacheService;
import com.dmg.bairenniuniuserver.service.logic.PokerService;
import com.dmg.bairenniuniuserver.service.logic.SettleService;
import com.dmg.bairenniuniuserver.service.task.SaveGameRecordTask;
import com.dmg.bairenniuniuserver.tcp.server.MessageIdConfig;
import com.dmg.common.core.config.RedisRegionConfig;
import com.dmg.data.common.dto.SettleSendDto;
import com.dmg.server.common.model.dto.GameRecordDTO;
import com.dmg.server.common.model.dto.UserResultDTO;
import com.dmg.server.common.util.RoundIdUtils;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/30 16:28
 * @Version V1.0
 **/
@Service
@Slf4j
public class SettleServiceImpl implements SettleService {
    @Autowired
    private PushService pushService;
    @Autowired
    private PokerService pokerService;
    @Autowired
    private PlayerService playerCacheService;
    @Autowired
    private RobotCacheService robotCacheService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Value("${game-id}")
    private String gameId;

    @Override
    public void settle(int roomId) {
        Room room = RoomManager.intance().getRoom(roomId);
        log.info("==>房间:{},进入结算,下注人数为:{}", roomId, room.getBetCount().intValue());
        if (room.getRoomStatus() != RoomStatus.DEAL) {
            log.error("==> 发牌失败,房间{} 状态有误,当前状态为{}", roomId, room.getRoomStatus());
            return;
        }
        long start = System.currentTimeMillis();
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

        if (control && (room.isHasPlayerBet() || !room.isSystemBanker())) {
            this.dealAlgorithm(room, win);
        } else {
            this.dealNormal(room);
        }
        log.info("场次:{}, 控制率为:{},随机数为:{},当局是否控制:{},控制输赢为:{}", room.getLevel(), controlExecuteRate, random, control, win);
        this.calculateWin(room);
        this.settleResult(room);
        log.info("================> 房间:{},结算耗时:{}", room.getRoomId(), System.currentTimeMillis() - start);
        try {
            room.setRoomStatus(RoomStatus.SETTLE);
            room.setCountdownTime(System.currentTimeMillis() + D.SETTLE_TIME);
            TimerManager.instance().submitDelayWork(StartGameDelayTask.class, D.SETTLE_TIME, roomId);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        log.info("系统当前流水: {} ,场次: {} , 盈利: {} ,抽水: {}", RoomManager.intance().getSystemTurnover(), room.getLevel(), RoomManager.intance().getRoomWinGold(room.getLevel()), RoomManager.intance().getPumpMap().get(room.getLevel()));
        this.redisTemplate.opsForValue().set(RedisRegionConfig.FILE_PUMP_KEY + ":" + this.gameId + "_" + room.getLevel(), RoomManager.intance().getPumpMap().get(room.getLevel()).intValue() + "");
        // this.redisTemplate.opsForList().rightPush("bairenniuniu:" + this.gameId + "_"
        // + room.getLevel(),
        // RoomManager.intance().getRoomWinGold(room.getLevel()).intValue() + "");
        // this.redisTemplate.opsForList().rightPush("bairenniuniu_control:" +
        // this.gameId + "_" + room.getLevel(), control + "");
    }

    /**
     * @param room
     * @return void
     * @description: 正常发牌
     * @author mice
     * @date 2019/7/31
     */
    void dealNormal(Room room) {
        log.info("==>房间:{},开始发牌", room.getRoomId());
        // 洗牌
        Collections.shuffle(room.getPokerList());
        LinkedList<Poker> tablePokerList = room.getPokerList();
        int pokerIndex = 0;
        for (Table table : room.getTableMap().values()) {
            List<Poker> pokers = new ArrayList<>();
            for (int j = 1; j < 6; j++) {
                pokers.add(tablePokerList.get(pokerIndex++));
            }
            table.setPokerList(pokers);
            int cardType = this.pokerService.getPokerType(pokers);
            table.setCardType(cardType);
        }
        boolean outMaxPayout = this.normalVirtualComputing(room);
        if (outMaxPayout) {
            log.warn("==> 房间:{},超过最大赔付限额,重新发牌", room.getRoomId());
            this.dealNormal(room);
        }
    }

    /**
     * @param room
     * @param win
     * @return void
     * @description: 算法发牌
     * @author mice
     * @date 2019/7/31
     */
    private void dealAlgorithm(Room room, boolean win) {
        log.info("==>房间:{},开始发牌", room.getRoomId());
        LinkedHashMap<Integer, HandPokerDTO> handPokerDTOMap = new LinkedHashMap<>();
        LinkedList<Poker> tablePokerList = room.getPokerList();
        Collections.shuffle(tablePokerList);
        int pokerIndex = 0;
        for (int i = 1; i < 6; i++) {
            List<Poker> pokers = new ArrayList<>();
            for (int j = 1; j < 6; j++) {
                pokers.add(tablePokerList.get(pokerIndex++));
            }
            int cardType = this.pokerService.getPokerType(pokers);
            HandPokerDTO handPokerDTO = new HandPokerDTO(cardType, pokers);
            handPokerDTOMap.put(i, handPokerDTO);
        }
        // 能赢(输)的牌型 key:全排列key value:PokerInfo
        Map<Integer, PokerInfo> winGoldMap = new HashMap<>();
        for (Integer key : RoomManager.intance().getFullyArrangedMap().keySet()) {
            List<Integer> arrangedList = RoomManager.intance().getFullyArrangedMap().get(key);
            int index = 0;
            for (Table table : room.getTableMap().values()) {
                table.setPokerList(handPokerDTOMap.get(arrangedList.get(index)).getPokerList());
                table.setCardType(handPokerDTOMap.get(arrangedList.get(index++)).getCardType());
            }
            this.algVirtualComputing(key, room, winGoldMap, win);
        }
        List<Integer> arrangedList;
        log.warn("==>winGoldMap.size()==" + winGoldMap.size());
        if (winGoldMap.size() == 0) {
            int randomNum = RandomUtil.randomInt(1, RoomManager.intance().getFullyArrangedMap().size());
            arrangedList = RoomManager.intance().getFullyArrangedMap().get(randomNum);
        } else {
            // 是否闪避通杀或者通赔
            boolean allWinOrLose = RoomManager.intance().isAllWinOrLose(room.getLevel(), win);
            if (allWinOrLose) {
                log.warn("==> 闪避通杀或者通赔");
                winGoldMap = this.removeAllWinOrLose(winGoldMap);
                log.warn(winGoldMap.toString());
            }
            Object[] winGoldKeys = winGoldMap.keySet().toArray();
            int winKey = (int) winGoldKeys[RandomUtil.randomInt(0, winGoldKeys.length)];
            if (!win) {
                // 如果 系统输钱 超过底线 则判定当局系统必赢 重新计算
                if (winGoldMap.get(winKey).getGold().intValue() + RoomManager.intance().getMaxPayout(room.getLevel()) <= 0 && RoomManager.intance().getRoomWinGold(room.getLevel()).intValue() < RoomManager.intance().getMaxPayoutReferenceValue(room.getLevel())) {
                    log.warn("==> 房间{} :系统输钱 超过底线 判定当局系统必赢 重新计算", room.getRoomId());
                    this.dealAlgorithm(room, true);
                    return;
                }
            }
            arrangedList = RoomManager.intance().getFullyArrangedMap().get(winKey);
        }
        log.info("计算出的牌型组合为:{}", JSON.toJSONString(arrangedList));
        // 最后放入计算好的牌型
        int index = 0;
        for (Table table : room.getTableMap().values()) {
            table.setPokerList(handPokerDTOMap.get(arrangedList.get(index)).getPokerList());
            table.setCardType(handPokerDTOMap.get(arrangedList.get(index++)).getCardType());
        }
        int winCount = room.getWinCountMap().get(win) == null ? 0 : room.getWinCountMap().get(win);
        room.getWinCountMap().put(win, winCount + 1);
        log.info("==> 房间:{},当局输赢判定为:{},总输赢场次为:{}", room.getRoomId(), win, room.getWinCountMap());
    }

    /**
     * @param key
     * @param room
     * @param winGoldMap
     * @param win
     * @return void
     * @description: 虚拟计算系统输赢钱的组合
     * @author mice
     * @date 2019/8/5
     */
    private void algVirtualComputing(int key, Room room, Map<Integer, PokerInfo> winGoldMap, boolean win) {
        Table bankerTable = room.getTableMap().get(D.ONE);
        int bankerCardType = bankerTable.getCardType();
        BigDecimal sysWinGold = new BigDecimal(0);
        // 统计通杀通赔 key: true:赢 false:输
        Map<Boolean, Integer> allWinOrLoseMap = new HashMap<>();
        allWinOrLoseMap.put(true, 0);
        allWinOrLoseMap.put(false, 0);
        if (room.isSystemBanker()) {
            for (Table table : room.getTableMap().values()) {
                if (table.getTableIndex().equals(D.ONE)) {
                    continue;
                }
                BigDecimal bankerMultiple = RoomManager.intance().getMultiple(room.getLevel(), bankerCardType);
                BigDecimal tableMultiple = RoomManager.intance().getMultiple(room.getLevel(), table.getCardType());
                // 庄家赢
                boolean bankerWin = this.judgeWinner(bankerTable, table);
                allWinOrLoseMap.put(bankerWin, allWinOrLoseMap.get(bankerWin) + 1);
                for (Seat seat : room.getSeatMap().values()) {
                    if (!(seat.getPlayer() instanceof BaseRobot) && seat.getBetChipMap().containsKey(table.getTableIndex())) {
                        BigDecimal betChip = seat.getBetChipMap().get(table.getTableIndex());
                        if (bankerWin) {
                            sysWinGold = sysWinGold.add(betChip.multiply(bankerMultiple));
                        } else {
                            sysWinGold = sysWinGold.subtract(betChip.multiply(tableMultiple));
                        }
                    }
                }
            }
        } else {
            for (Table table : room.getTableMap().values()) {
                if (table.getTableIndex().equals(D.ONE)) {
                    continue;
                }
                BigDecimal bankerMultiple = RoomManager.intance().getMultiple(room.getLevel(), bankerCardType);
                BigDecimal tableMultiple = RoomManager.intance().getMultiple(room.getLevel(), table.getCardType());
                // 庄家赢
                boolean bankerWin = this.judgeWinner(bankerTable, table);
                allWinOrLoseMap.put(bankerWin, allWinOrLoseMap.get(bankerWin) + 1);
                for (Seat seat : room.getSeatMap().values()) {
                    if (seat.getPlayer() instanceof BaseRobot && seat.getBetChipMap().containsKey(table.getTableIndex())) {
                        BigDecimal betChip = seat.getBetChipMap().get(table.getTableIndex());
                        if (bankerWin) {
                            sysWinGold = sysWinGold.subtract(betChip.multiply(bankerMultiple));
                        } else {
                            sysWinGold = sysWinGold.add(betChip.multiply(tableMultiple));
                        }
                    }
                }
            }
        }
        if (win) {
            if (sysWinGold.compareTo(BigDecimal.ZERO) > 0) {
                if (allWinOrLoseMap.get(win) == 4) {
                    winGoldMap.put(key, new PokerInfo(sysWinGold, true));
                } else {
                    winGoldMap.put(key, new PokerInfo(sysWinGold, false));
                }
            }
        } else {
            if (sysWinGold.compareTo(BigDecimal.ZERO) < 0) {
                if (allWinOrLoseMap.get(win) == 4) {
                    winGoldMap.put(key, new PokerInfo(sysWinGold, true));
                } else {
                    winGoldMap.put(key, new PokerInfo(sysWinGold, false));
                }
            }
        }
    }

    /**
     * @param room
     * @return boolean true:超过最大赔付限制 false:正常
     * @description: 正常发牌计算
     * @author mice
     * @date 2019/9/29
     */
    private boolean normalVirtualComputing(Room room) {
        Table bankerTable = room.getTableMap().get(D.ONE);
        int bankerCardType = bankerTable.getCardType();
        BigDecimal sysWinGold = new BigDecimal(0);
        if (room.isSystemBanker()) {
            for (Table table : room.getTableMap().values()) {
                if (table.getTableIndex().equals(D.ONE)) {
                    continue;
                }
                BigDecimal bankerMultiple = RoomManager.intance().getMultiple(room.getLevel(), bankerCardType);
                BigDecimal tableMultiple = RoomManager.intance().getMultiple(room.getLevel(), table.getCardType());
                // 庄家赢
                boolean bankerWin = this.judgeWinner(bankerTable, table);
                for (Seat seat : room.getSeatMap().values()) {
                    if (!(seat.getPlayer() instanceof BaseRobot) && seat.getBetChipMap().containsKey(table.getTableIndex())) {
                        BigDecimal betChip = seat.getBetChipMap().get(table.getTableIndex());
                        if (bankerWin) {
                            sysWinGold = sysWinGold.add(betChip.multiply(bankerMultiple));
                        } else {
                            sysWinGold = sysWinGold.subtract(betChip.multiply(tableMultiple));
                        }
                    }
                }
            }
        } else {
            for (Table table : room.getTableMap().values()) {
                if (table.getTableIndex().equals(D.ONE)) {
                    continue;
                }
                BigDecimal bankerMultiple = RoomManager.intance().getMultiple(room.getLevel(), bankerCardType);
                BigDecimal tableMultiple = RoomManager.intance().getMultiple(room.getLevel(), table.getCardType());
                // 庄家赢
                boolean bankerWin = this.judgeWinner(bankerTable, table);
                for (Seat seat : room.getSeatMap().values()) {
                    if (seat.getPlayer() instanceof BaseRobot && seat.getBetChipMap().containsKey(table.getTableIndex())) {
                        BigDecimal betChip = seat.getBetChipMap().get(table.getTableIndex());
                        if (bankerWin) {
                            sysWinGold = sysWinGold.subtract(betChip.multiply(bankerMultiple));
                        } else {
                            sysWinGold = sysWinGold.add(betChip.multiply(tableMultiple));
                        }
                    }
                }
            }
        }
        if (RoomManager.intance().getMaxPayout(room.getLevel()) + sysWinGold.intValue() <= 0 && RoomManager.intance().getRoomWinGold(room.getLevel()).intValue() < RoomManager.intance().getMaxPayoutReferenceValue(room.getLevel())) {
            return true;
        }
        return false;
    }

    /**
     * @param winGoldMap
     * @return void
     * @description: 移除通杀 通赔情况 若全是通杀则不作处理
     * @author mice
     * @date 2019/9/28
     */
    private Map<Integer, PokerInfo> removeAllWinOrLose(Map<Integer, PokerInfo> winGoldMap) {
        Map<Integer, PokerInfo> finalMap = new HashMap<>();
        for (Integer key : winGoldMap.keySet()) {
            if (!winGoldMap.get(key).isAllWinOrLose()) {
                finalMap.put(key, winGoldMap.get(key));
            }
        }
        if (finalMap.size() > 0) {
            return finalMap;
        }
        return winGoldMap;
    }

    /**
     * @param room
     * @description: 计算输赢分
     * @author mice
     * @date 2019/7/31
     */
    private void calculateWin(Room room) {
        log.info("==>房间:{},开始计算输赢分", room.getRoomId());
        Table bankerTable = room.getTableMap().get(D.ONE);
        int bankerCardType = bankerTable.getCardType();
        BigDecimal bankerMultiple = RoomManager.intance().getMultiple(room.getLevel(), bankerCardType);
        Seat bankerSeat = room.getBanker();
        LinkedList<Object> winTableList = new LinkedList<>();
        LinkedList<Object> allTablePokerList = new LinkedList<>();

        Map<String, TableInfo> tableInfoMap = new HashMap<>();
        for (Table table : room.getTableMap().values()) {
            allTablePokerList.addAll(table.getPokerList().stream().map(Poker::getValue).collect(Collectors.toList()));

            TableInfo tableInfo = new TableInfo(table.getPokerList(), table.getCardType() + "", bankerMultiple.intValue());
            tableInfoMap.put(table.getTableIndex(), tableInfo);
            if (table.getTableIndex().equals(D.ONE)) {
                continue;
            }

            BigDecimal tableMultiple = RoomManager.intance().getMultiple(room.getLevel(), table.getCardType());
            // 庄家赢
            if (this.judgeWinner(bankerTable, table)) {
                String tableIndex = table.getTableIndex();
                for (Seat seat : room.getSeatMap().values()) {
                    if (seat.getBetChipMap().containsKey(tableIndex)) {
                        BigDecimal betChip = seat.getBetChipMap().get(tableIndex);
                        BigDecimal loseGold = betChip.multiply(bankerMultiple).negate();
                        seat.setTotalWinGold(seat.getTotalWinGold().add(loseGold));
                        seat.getWinGoldMap().put(table.getTableIndex(), loseGold);
                    }
                }
                BigDecimal bankerWinGold = table.getBetChipTotal().multiply(bankerMultiple);

                bankerSeat.setCurWinGold(bankerSeat.getCurWinGold().add(bankerWinGold));
                bankerSeat.setTotalWinGold(bankerSeat.getTotalWinGold().add(bankerWinGold));
                bankerSeat.getWinGoldMap().put(table.getTableIndex(), bankerWinGold);

                if (room.isSystemBanker()) {
                    RoomManager.intance().addRoomWinGold(room.getLevel(), table.getPlayerBetChipTotal().multiply(bankerMultiple));
                } else {
                    RoomManager.intance().addRoomWinGold(room.getLevel(), table.getRobotBetChipTotal().multiply(bankerMultiple).negate());
                }
                // 添加牛牛报表记录
                if (!room.getReportFormMap().containsKey(table.getTableIndex())) {
                    room.getReportFormMap().put(table.getTableIndex(), new LinkedList<>());
                }
                room.getReportFormMap().get(table.getTableIndex()).addFirst(false);

                if (room.getReportFormMap().get(table.getTableIndex()).size() > 10) {
                    room.getReportFormMap().get(table.getTableIndex()).removeLast();
                }
            } else {
                String seatIndex = table.getTableIndex();
                winTableList.add(seatIndex);
                for (Seat seat : room.getSeatMap().values()) {
                    if (seat.getBetChipMap().containsKey(seatIndex)) {
                        BigDecimal betChip = seat.getBetChipMap().get(seatIndex);
                        BigDecimal winGold = betChip.multiply(tableMultiple);
                        seat.setTotalWinGold(seat.getTotalWinGold().add(winGold));
                        seat.getWinGoldMap().put(table.getTableIndex(), winGold);
                    }
                }

                BigDecimal bankerLostGold = table.getBetChipTotal().multiply(tableMultiple).negate();

                bankerSeat.setCurWinGold(bankerSeat.getCurWinGold().add(bankerLostGold));
                bankerSeat.setTotalWinGold(bankerSeat.getTotalWinGold().add(bankerLostGold));
                bankerSeat.getWinGoldMap().put(table.getTableIndex(), bankerLostGold);

                if (room.isSystemBanker()) {
                    RoomManager.intance().addRoomWinGold(room.getLevel(), table.getPlayerBetChipTotal().multiply(tableMultiple).negate());
                } else {
                    RoomManager.intance().addRoomWinGold(room.getLevel(), table.getRobotBetChipTotal().multiply(tableMultiple));
                }

                // 添加牛牛报表记录
                if (!room.getReportFormMap().containsKey(table.getTableIndex())) {
                    room.getReportFormMap().put(table.getTableIndex(), new LinkedList<>());
                }
                room.getReportFormMap().get(table.getTableIndex()).addFirst(true);

                if (room.getReportFormMap().get(table.getTableIndex()).size() > 10) {
                    room.getReportFormMap().get(table.getTableIndex()).removeLast();
                }
            }
        }
        if (bankerSeat.getCurWinGold().compareTo(BigDecimal.ZERO) > 0) {
            winTableList.addFirst(String.valueOf(1));
        }
        List<Long> userIdList = new ArrayList<>();
        List<GameRecordDTO> gameRecordDTOS = new ArrayList<>();
        String roundCode = RoundIdUtils.getGameRecordId(String.valueOf(this.gameId), String.valueOf(room.getRoomId()));
        synchronized (room) {
            for (Seat seat : room.getSeatMap().values()) {
                if (!seat.isReady()) {
                    continue;
                }

                BigDecimal betChipTotal = seat.getBetChipMap().values().stream().reduce(BigDecimal::add).get();
                BigDecimal changeGold = BigDecimal.ZERO;

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
                    userResultDTO.setBetGold(seat.getBetChipMap().get(key));
                    userResultDTO.setWinLosGold(seat.getWinGoldMap().get(key));
                    userResultDTOS.add(userResultDTO);

                }
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

                // 加回下注额(因为下注时已经扣除玩家金币)
                changeGold = changeGold.add(betChipTotal);
                seat.getPlayer().setGold(seat.getPlayer().getGold().add(changeGold));
                if (seat.getPlayer() instanceof BaseRobot) {
                    this.robotCacheService.update((BaseRobot) seat.getPlayer());
                } else {
                    this.playerCacheService.updatePlayer(seat.getPlayer());
                    SettleSendDto incGoldSendDto = SettleSendDto.builder()
                            .userId(seat.getPlayer().getUserId())
                            .changeGold(changeGold)
                            .build();
                    this.playerCacheService.settle(incGoldSendDto);
                }

                // 添加战绩
                this.recordData(seat.getPlayer() instanceof BaseRobot, room, seat.getPlayer(), seat, betChipTotal, pump, userResultDTOS, roundCode, userIdList, gameRecordDTOS);
            }
        }
        BigDecimal bankerPump = BigDecimal.ZERO;
        if (bankerSeat.getCurWinGold().compareTo(BigDecimal.ZERO) > 0) {
            bankerPump = bankerSeat.getCurWinGold().multiply((RoomManager.intance().getPumpRate(room.getLevel())).divide(new BigDecimal(100)));
            bankerSeat.setCurWinGold(bankerSeat.getCurWinGold().subtract(bankerPump));
            if (!room.isSystemBanker()) {
                RoomManager.intance().addPump(room.getLevel(), bankerPump);
            }
        }
        if (!room.isSystemBanker() && bankerSeat.getWinGoldMap().size() != 0) {
            bankerSeat.getPlayer().setGold(bankerSeat.getPlayer().getGold().add(bankerSeat.getCurWinGold()));
            SettleSendDto settleSendDto = SettleSendDto.builder()
                    .userId(bankerSeat.getPlayer().getUserId())
                    .changeGold(bankerSeat.getCurWinGold())
                    .build();
            this.playerCacheService.settle(settleSendDto);

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
     * @param bankerTable
     * @param table
     * @return boolean
     * @description: 比牌
     * @author mice
     * @date 2019/7/31
     */
    private boolean judgeWinner(Table bankerTable, Table table) {
        if (bankerTable.getCardType() > table.getCardType()) {
            return true;
        } else if (bankerTable.getCardType() < table.getCardType()) {
            return false;
        }
        List<Poker> bankerPokerList = new ArrayList<>();
        bankerPokerList.addAll(bankerTable.getPokerList());
        Collections.sort(bankerPokerList);
        List<Poker> tablePokerList = new ArrayList<>();
        tablePokerList.addAll(table.getPokerList());
        Collections.sort(tablePokerList);
        // 牌型一致时 比较牌值 牌值大的赢 若牌值相同 则比较花色
        return this.recursiveComparePokerVaule(bankerPokerList, tablePokerList, 0);
    }

    /**
     * @param bankerPokerList
     * @param tablePokerList
     * @param index
     * @return boolean
     * @description: 递归比较牌值
     * @author mice
     * @date 2019/8/2
     */
    private boolean recursiveComparePokerVaule(List<Poker> bankerPokerList, List<Poker> tablePokerList, int index) {
        if (bankerPokerList.get(index).getValue() > tablePokerList.get(index).getValue()) {
            return true;
        } else if (bankerPokerList.get(index).getValue() < tablePokerList.get(index).getValue()) {
            return false;
        }
        // 牌值相同 比较最大牌值的花色
        if (bankerPokerList.get(0).getType() > tablePokerList.get(0).getType()) {
            return true;
        } else {
            return false;
        }
        /* index++;
         * if (index < 5) {
         * this.recursiveComparePokerVaule(bankerPokerList, tablePokerList, index);
         * } else {
         * }
         * return false; */
    }

    /**
     * @param room
     * @return void
     * @description: 结算结果
     * @author mice
     * @date 2019/7/31
     */
    private void settleResult(Room room) {
        log.info("==>房间:{},结算结果", room.getRoomId());
        SettleResultDTO settleResultDTO = new SettleResultDTO();
        // 庄家结算信息
        Seat bankerSeat = room.getBanker();
        SettleResultDTO.SettleInfo bankerSettleInfo = new SettleResultDTO.SettleInfo();
        bankerSettleInfo.setCurWinGold(bankerSeat.getCurWinGold());
        bankerSettleInfo.setWinGoldMap(bankerSeat.getWinGoldMap());
        bankerSettleInfo.setUserId(bankerSeat.getPlayer().getUserId());
        bankerSettleInfo.setNickname(bankerSeat.getPlayer().getNickname());
        bankerSettleInfo.setHeadIcon(bankerSeat.getPlayer().getHeadIcon());

        settleResultDTO.setBankerSettleInfo(bankerSettleInfo);
        settleResultDTO.setTableMap(room.getTableMap());
        settleResultDTO.getWinGoldRank().add(bankerSettleInfo);

        // 统计外场人员赢钱数
        BigDecimal outfieldWinGold = new BigDecimal(0);
        synchronized (room) {
            for (Seat seat : room.getSeatMap().values()) {
                if (seat.getSeatIndex() > 8 && seat.isReady()) {
                    outfieldWinGold = outfieldWinGold.add(seat.getCurWinGold());
                    settleResultDTO.getWinGoldRank().add(this.settleInfoWarpper(seat));
                }
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

        room.setSettleResultDTO(settleResultDTO);
        // 向游戏中的玩家推送数据
        synchronized (room) {
            for (Seat seat : room.getSeatMap().values()) {
                if (seat.getPlayer() instanceof BaseRobot) {
                    continue;
                }
                if (seat.isLeave()) {
                    continue;
                }

                if (!seat.isReady()) {
                    SettleResultDTO.SettleInfo selfSettleInfo = new SettleResultDTO.SettleInfo();
                    settleResultDTO.setSelfSettleInfo(selfSettleInfo);
                    MessageResult messageResult = new MessageResult(MessageIdConfig.SETTLE_RESULT_NTC, settleResultDTO);
                    this.pushService.push(seat.getPlayer().getUserId(), messageResult);
                    continue;
                }
                SettleResultDTO.SettleInfo selfSettleInfo = this.settleInfoWarpper(seat);
                settleResultDTO.setSelfSettleInfo(selfSettleInfo);
                MessageResult messageResult = new MessageResult(MessageIdConfig.SETTLE_RESULT_NTC, settleResultDTO);
                this.pushService.push(seat.getPlayer().getUserId(), messageResult);
            }
        }
        // 向庄家推送数据
        if (!room.isSystemBanker()) {
            SettleResultDTO.SettleInfo selfSettleInfo = this.settleInfoWarpper(bankerSeat);
            settleResultDTO.setSelfSettleInfo(selfSettleInfo);
            MessageResult messageResult = new MessageResult(MessageIdConfig.SETTLE_RESULT_NTC, settleResultDTO);
            this.pushService.push(bankerSeat.getPlayer().getUserId(), messageResult);
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
     * 记录战绩
     *
     * @param room
     */
    @SuppressWarnings("unchecked")
    private void recordData(boolean isRobot, Room room, BasePlayer player, Seat seat, BigDecimal betChipTotal,
            BigDecimal pump, List<UserResultDTO<Poker>> userResultDTOS, String roundCode, List<Long> userIdList, List<GameRecordDTO> gameRecordDTOS) {
        GameRecordDTO gameRecordDTO = GameRecordDTO.<UserResultDTO<Poker>>builder()
                .gameId(Integer.valueOf(this.gameId))
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