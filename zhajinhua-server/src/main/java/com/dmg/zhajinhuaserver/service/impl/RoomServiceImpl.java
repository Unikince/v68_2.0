package com.dmg.zhajinhuaserver.service.impl;

import static com.dmg.zhajinhuaserver.config.MessageConfig.GAME_RESULT_MSG;
import static com.dmg.zhajinhuaserver.config.MessageConfig.GAME_SEND_CARDS;
import static com.dmg.zhajinhuaserver.config.MessageConfig.JION_ROOM;
import static com.dmg.zhajinhuaserver.config.MessageConfig.PLAYER_ISONLINE;
import static com.dmg.zhajinhuaserver.config.MessageConfig.PLAYER_PLAY;
import static com.dmg.zhajinhuaserver.config.MessageConfig.PLAYER_READY_NTC;
import static com.dmg.zhajinhuaserver.config.MessageConfig.SEE_CARD_ACTION;
import static com.dmg.zhajinhuaserver.config.MessageConfig.SUCCESS_DISOLUT_ROOM_NTC;
import static com.dmg.zhajinhuaserver.config.MessageConfig.TURN_ACTION;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.dmg.common.core.web.BaseResultEnum;
import com.dmg.common.core.web.Result;
import com.dmg.server.common.enums.GameOddsEnum;
import com.dmg.server.common.enums.UserControlStateEnum;
import com.dmg.server.common.model.dto.GameWaterPoolDTO;
import com.dmg.server.common.model.dto.UserControlInfoDTO;
import com.dmg.server.common.util.GameOnlineChangeUtils;
import com.dmg.server.common.util.RoundIdUtils;
import com.dmg.zhajinhuaserver.config.MessageConfig;
import com.dmg.zhajinhuaserver.service.config.UserControlService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dmg.common.core.config.RedisRegionConfig;
import com.dmg.common.core.util.RedisUtil;
import com.dmg.common.starter.rocketmq.core.producer.MQProducer;
import com.dmg.data.common.dto.BetRecvDto;
import com.dmg.server.common.model.dto.GameRecordDTO;
import com.dmg.server.common.model.dto.UserResultDTO;
import com.dmg.zhajinhuaserver.SpringContextUtil;
import com.dmg.zhajinhuaserver.common.constant.Constant;
import com.dmg.zhajinhuaserver.config.Config;
import com.dmg.zhajinhuaserver.config.Config.PokerOperState;
import com.dmg.zhajinhuaserver.config.Config.SeatState;
import com.dmg.zhajinhuaserver.config.D;
import com.dmg.zhajinhuaserver.manager.RoomManager;
import com.dmg.zhajinhuaserver.manager.TimerManager;
import com.dmg.zhajinhuaserver.manager.work.delay.AutoCallDelayWork;
import com.dmg.zhajinhuaserver.manager.work.delay.AutoCompareCardsDelayWork;
import com.dmg.zhajinhuaserver.manager.work.delay.AutoReadyDelayWork;
import com.dmg.zhajinhuaserver.manager.work.delay.BalanceResultDelayWork;
import com.dmg.zhajinhuaserver.manager.work.delay.ReadyTimeEndDelayWork;
import com.dmg.zhajinhuaserver.manager.work.delay.ReadyTimeEndGameStartDelayWork;
import com.dmg.zhajinhuaserver.model.bean.Balance;
import com.dmg.zhajinhuaserver.model.bean.GameRoom;
import com.dmg.zhajinhuaserver.model.bean.Player;
import com.dmg.zhajinhuaserver.model.bean.Poker;
import com.dmg.zhajinhuaserver.model.bean.Record;
import com.dmg.zhajinhuaserver.model.bean.Robot;
import com.dmg.zhajinhuaserver.model.bean.RoomRecord;
import com.dmg.zhajinhuaserver.model.bean.Seat;
import com.dmg.zhajinhuaserver.model.bean.ZhaJinHuaD;
import com.dmg.zhajinhuaserver.model.constants.GameConfig;
import com.dmg.zhajinhuaserver.model.dto.BrandDTO;
import com.dmg.zhajinhuaserver.model.dto.OutPlayerDTO;
import com.dmg.zhajinhuaserver.model.dto.RecordDataDTO;
import com.dmg.zhajinhuaserver.result.MessageResult;
import com.dmg.zhajinhuaserver.result.ResultEnum;
import com.dmg.zhajinhuaserver.service.ALGService;
import com.dmg.zhajinhuaserver.service.PushService;
import com.dmg.zhajinhuaserver.service.QuitRoomService;
import com.dmg.zhajinhuaserver.service.RoomService;
import com.dmg.zhajinhuaserver.service.cache.GameWaterPoolCacheService;
import com.dmg.zhajinhuaserver.service.cache.PlayerService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RoomServiceImpl implements RoomService {

    @Autowired
    private PushService pushService;

    @Autowired
    private PlayerService playerCacheService;

    @Autowired
    private QuitRoomService quitRoomService;

    @Autowired
    private ALGService algService;

    @Autowired
    private GameWaterPoolCacheService gameWaterPoolCacheService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private MQProducer defautProducer;

    @Autowired
    private UserControlService userControlService;

    @Override
    public Map<String, Object> roomMsg(GameRoom room, Seat data) {
        Map<String, Object> tableInfo = new ConcurrentHashMap<>();
        Map<String, Object> tableBase = new ConcurrentHashMap<>();
        Map<String, Object> seatInfo = new ConcurrentHashMap<>();
        Map<String, Object> playerInfo = new ConcurrentHashMap<>();
        if (room.getRoomStatus() == Config.RoomStatus.GAME) {
            if (room.getOperTurns() == 0) {
                room.setOperTurns(1);
            }
        }
        tableBase.put(D.GAME_RULE, room.getRule());
        tableBase.put(D.GAME_EXTRA_RULE, room.getExtraRule());
        tableBase.put(D.TOTAL_ROUND, room.getTotalRound());
        tableBase.put(D.TABLE_CUR_JUSHU, room.getRound());
        tableBase.put(D.TABLE_CUR_TURN_NUM, room.getOperTurns());
        tableBase.put(D.TABLE_MAX_TURN_NUM, room.getCountTurns());
        tableBase.put(D.TABLE_MAX_BETCHIP, room.getBetMul()[room.getBetMul().length - 1] * room.getBaseScore());
        // tableBase.put(D.TABLE_FENG_DING, room.getTotalBet());
        tableBase.put(D.TABLE_INDEX, room.getRoomId());
        tableBase.put(D.TABLE_STATE, room.getRoomStatus());
        tableBase.put(D.TABLE_ROOM_TYPE, room.getGameRoomTypeId());
        tableBase.put(D.TABLE_GAME_TYPE, room.getGameTypeId());
        tableBase.put(D.TABLE_CREATE_RID, room.getCreator());
        tableBase.put(D.TABLE_CREATE_ROLENAME, "");
        tableBase.put(D.TABLE_CREATE_TIME, room.getCreateTime());
        tableBase.put(D.TABLE_LEVEL, room.getGrade());
        tableBase.put(D.TABLE_MIN_CARRY, room.getLowerLimit());
        tableBase.put(D.TABLE_LEAVE_CARRY, room.getUpperLimit());
        tableBase.put(D.TABLE_BASE_SCORE, room.getBaseScore());
        tableBase.put(D.TABLE_TIP, 0);
        tableBase.put(D.TABLE_ALL_BETS, room.getTotalChips());
        tableBase.put(D.TABLE_MAX_PLAYER_NUM, room.getTotalPlayer());
        tableBase.put(D.TABLE_CUR_PLAYER_NUM, room.getSeatMap().size());
        tableBase.put(D.TABLE_ACTION_SEAT_INDEX, room.getPlaySeat());
        tableBase.put(ZhaJinHuaD.BET_CHIPS_LIST, room.getBetList());
        tableBase.put(ZhaJinHuaD.BET_MUL_LIST, room.getBetMul());
        for (int i = 1; i <= room.getTotalPlayer(); i++) {
            Seat seat = room.getSeatMap().get(i);
            if (seat != null) {
                Map<String, Object> seatMsg = new HashMap<>();
                Map<String, Object> playerMsg = new HashMap<>();
                Player p = seat.getPlayer();
                if (seat.isHaveSeenCard()) {
                    if (data != null && seat.getSeatId() == data.getSeatId()) {
                        seatMsg.put(D.SEAT_HAND_CARDS, seat.getHand());
                        seatMsg.put(D.HAND_CARDS_TYPE, seat.getHandCardsType());
                    } else {
                        seatMsg.put(D.SEAT_HAND_CARDS, seat.getBackHand());
                    }
                } else {
                    seatMsg.put(D.SEAT_HAND_CARDS, seat.getBackHand());
                }
                if (room.getGameRoomTypeId() == D.PRIVATE_FIELD) {
                    seatMsg.put("isfirstman", false);
                    if (room.getOperTurns() == 1) {
                        if (room.getCustomRule().getMenRule() == 1) {
                            if (seat.getSeatId() == room.getBankerSeat() && room.getBetList().size() == this.getPlayedPlayers(room)) {
                                seatMsg.put("isfirstman", true);
                            }
                        }
                    }
                }
                seatMsg.put(D.SEAT_IS_AUTOCALL, seat.isHasFollowEnd());
                seatMsg.put(D.SEAT_PLAYER_ONLINE, p.isActive());
                seatMsg.put(D.PLAYER_ROLENAME, seat.getPlayer().getNickname());
                seatMsg.put(D.PLAYER_RID, p.getRoleId());
                seatMsg.put("userCode", p.getUserCode());
                seatMsg.put(D.SEAT_GOLD, seat.getChipsRemain());
                seatMsg.put(D.SEAT_CARDSNUM, seat.getHand().size());
                playerMsg.put(D.PLAYER_RID, p.getRoleId());
                playerMsg.put("userCode", p.getUserCode());
                playerMsg.put(D.PLAYER_ROLENAME, p.getNickname());
                playerMsg.put(D.PLAYER_LOGO, p.getHeadImgUrl() == null ? "" : p.getHeadImgUrl());
                playerMsg.put(D.PLAYER_SEX, p.getSex());
                playerMsg.put(D.PLAYER_FANGKA, p.getMasonry());
                playerMsg.put(D.PLAYER_INTRODUCE, "");
                seatMsg.put(D.SEAT_STATE, seat.getState());
                seatMsg.put(D.SEAT_INDEX, i);
                seatMsg.put(D.SEAT_IS_TUOGUAN, false);
                seatMsg.put(D.SEAT_SEECARDS, seat.isHaveSeenCard());
                seatMsg.put(D.SEAT_AUTO_CALL, false);
                seatMsg.put(D.SEAT_IS_READY, seat.isReady());
                seatMsg.put(D.SEAT_READY_TO_TIME, seat.getActionEndTime());
                seatMsg.put(D.SEAT_ALL_BETS, seat.getBetChips());
                seatMsg.put("isplayed", seat.isPlayed());
                seatInfo.put(i + "", seatMsg);
                playerInfo.put(i + "", playerMsg);
            }
        }
        tableInfo.put(D.TABLE_BASEINFO, tableBase);
        tableInfo.put(D.SEAT_INFO, seatInfo);
        tableInfo.put(D.PLAYER_SEAT_PLAYERINFO, playerInfo);
        return tableInfo;
    }

    public int getPlayedPlayers(GameRoom room) {
        int count = 0;
        for (Seat seat : room.getSeatMap().values()) {
            if (seat.isPlayed()) {
                count++;
            }
        }
        return count;
    }

    /**
     * 返回玩家信息
     *
     * @param player
     * @return
     */
    @Override
    public Map<String, Object> sendPlayerMsg(Player player) {
        Map<String, Object> playerMsg = new ConcurrentHashMap<>();
        playerMsg.put(D.PLAYER_RID, player.getRoleId());
        playerMsg.put(D.PLAYER_ROLENAME, player.getNickname());
        playerMsg.put(D.PLAYER_LOGO, player.getHeadImgUrl() == null ? "" : player.getHeadImgUrl());
        playerMsg.put(D.PLAYER_SEX, player.getSex());
        playerMsg.put(D.PLAYER_FANGKA, player.getMasonry());
        playerMsg.put(D.PLAYER_INTRODUCE, "");
        return playerMsg;
    }

    /**
     * 返回座位信息
     *
     * @param room
     * @return
     */
    @Override
    public Map<String, Object> sendSeatMsg(GameRoom room, int seatId) {
        Map<String, Object> seatMsg = new ConcurrentHashMap<>();
        Seat seat = room.getSeatMap().get(seatId);
        Player p = seat.getPlayer();
        seatMsg.put(D.SEAT_STATE, seat.getState());
        if (seat.isHaveSeenCard()) {
            seatMsg.put(D.SEAT_HAND_CARDS, seat.getHand());
        } else {
            seatMsg.put(D.SEAT_HAND_CARDS, seat.getBackHand());
        }
        seatMsg.put(D.SEAT_IS_AUTOCALL, seat.isHasFollowEnd());
        seatMsg.put(D.SEAT_PLAYER_ONLINE, p.isActive());
        seatMsg.put(D.PLAYER_RID, p.getRoleId());
        if (room.getGameRoomTypeId() == D.PRIVATE_FIELD) {
            seatMsg.put(D.SEAT_GOLD, seat.getChipsRemain());
        } else {
            seatMsg.put(D.SEAT_GOLD, seat.getChipsRemain() != 0 ? seat.getChipsRemain() : p.getGold());
        }
        seatMsg.put(D.SEAT_CARDSNUM, seat.getHand().size());
        seatMsg.put(D.SEAT_INDEX, seatId);
        seatMsg.put(D.SEAT_IS_TUOGUAN, false);
        seatMsg.put(D.SEAT_STATE, 0);
        seatMsg.put(D.SEAT_SEECARDS, seat.isHaveSeenCard());
        seatMsg.put(D.SEAT_AUTO_CALL, false);
        seatMsg.put(D.SEAT_IS_READY, seat.isReady());
        seatMsg.put(D.SEAT_READY_TO_TIME, seat.getActionEndTime());
        seatMsg.put(D.SEAT_ALL_BETS, seat.getBetChips());
        seatMsg.put("isplayed", seat.isPlayed());
        if (seat.isHaveSeenCard()) {
            seatMsg.put(D.HAND_CARDS_TYPE, seat.getHandCardsType());
        }
        return seatMsg;
    }

    /**
     * 判断是否是断线重连
     *
     * @param player
     * @param roomId
     * @return
     */
    @Override
    public boolean checkRejoinRoom(Player player, int roomId) {
        GameRoom room = RoomManager.instance().getRoom(roomId);
        if (room == null) {
            if (roomId > 0) {
                return false;
            }
        }
        if (room == null || room.getSeatMap() == null) {
            return false;
        }
        for (Entry<Integer, Seat> entry : room.getSeatMap().entrySet()) {
            Seat data = entry.getValue();
            if (data.getPlayer().getRoleId() == player.getRoleId()) {
                return true;
            }
        }
        for (Player player0 : room.getWatchList()) {
            if (player0.getRoleId() == player.getRoleId()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 断线重连
     *
     * @param player
     */
    @Override
    public void rejionRoom(Player player) {
        GameRoom room = RoomManager.instance().getRoom(player.getRoomId());
        if (room == null) {
            player.setRoomId(0);
            this.playerCacheService.update(player);
            this.pushService.push(player.getRoleId(), JION_ROOM, ResultEnum.PLAYER_HAS_NOT_INROOM.getCode());
            return;
        }
        room.setDeadTime(0);
        player.setActive(true);
        int seat = this.getPlaySeat(room, player);
        long leftTime = 15 * 60 * 1000 - (System.currentTimeMillis() - room.getCreateTime());
        if (leftTime < 0) {
            leftTime = 0;
        }
        if (seat == 0) {
            // 不在座位上
            log.info("玩家:{}不在座位", player.getRoleId());
            this.pushService.push(player.getRoleId(), JION_ROOM);
        } else {
            Seat data = room.getSeatMap().get(seat);
            data.setOffline(false);
            long dissovleLeftTime = System.currentTimeMillis() - room.getDissolveTime();
            if (dissovleLeftTime >= 30000) {
                dissovleLeftTime = 0;
            } else {
                dissovleLeftTime = 30000 - dissovleLeftTime;
            }
            Map<String, Object> tableInfo = this.roomMsg(room, data);
            Map<String, Object> actionInfo = new HashMap<>();
            Map<String, Object> map = new ConcurrentHashMap<>();
            Seat actionSeat = room.getSeatMap().get(room.getPlaySeat());
            if (room.getGameRoomTypeId() == D.PRIVATE_FIELD && data.isPlayed()) {
                if (room.getDissolveRid() > 0) {
                    actionInfo.put("solve_rid", room.getDissolveRid());
                    actionInfo.put("solve_to_time", room.getDissolveTime());
                    Map<Long, Object> solveMap = new HashMap<>();
                    for (Seat ss : room.getSeatMap().values()) {
                        if (ss.isPlayed()) {
                            solveMap.put(ss.getPlayer().getRoleId(), false);
                        }
                    }
                    for (Entry<Integer, Boolean> entry : room.getDissolveMap().entrySet()) {
                        Player play = room.getSeatMap().get(entry.getKey()).getPlayer();
                        solveMap.put(play.getRoleId(), entry.getValue());
                    }
                    actionInfo.put("solve_map", solveMap);
                }
            }
            if (actionSeat == null || room.getRoomStatus() != Config.RoomStatus.GAME) {
                tableInfo.put(D.SEAT_ACTION_INFO, actionInfo);
            } else {
                List<Integer> list = new ArrayList<>();
                if (actionSeat.getOperNotify().canAddChips) {
                    for (int j = 0; j < room.getAddChipsBet(); j++) {
                        list.add(0);
                    }
                    for (int i = room.getAddChipsBet() + 1; i <= 5; i++) {
                        if (actionSeat.isHaveSeenCard()) {
                            list.add(room.getBaseScore() * room.getBetMul()[i - 1] * 2);
                        } else {
                            list.add(room.getBaseScore() * room.getBetMul()[i - 1]);
                        }
                    }
                }
                actionInfo.put(D.PLAYER_RID, actionSeat.getPlayer().getRoleId());
                actionInfo.put(D.TABLE_ACTION_SEAT_INDEX, actionSeat.getSeatId());
                actionInfo.put(D.SEAT_ACTION_TO_TIME, actionSeat.getActionEndTime());
                actionInfo.put(D.PLAYER_BASE_ACTION_OPER, actionSeat.getActionOper());
                actionInfo.put(D.SEAT_FOLLOW_CHIPS, room.getLastBetChips());
                actionInfo.put(D.PLAYER_CANCOMPARE_CARD_PLAYERLIST, actionSeat.getOperNotify().beSeatIdList);
                actionInfo.put(ZhaJinHuaD.IS_RUSH, room.isHaveRush());
                actionInfo.put(ZhaJinHuaD.RUSH_ID, room.getBloodId());
                actionInfo.put(ZhaJinHuaD.ADD_CHIPS, list);
                tableInfo.put(D.SEAT_ACTION_INFO, actionInfo);
            }

            map.put(D.TABLE_INFO, tableInfo);
            MessageResult messageResult = new MessageResult(JION_ROOM, map);
            this.pushService.push(player.getRoleId(), messageResult);
            Map<String, Object> notifyMap = new HashMap<>();
            notifyMap.put(D.SEAT_PLAYER_ONLINE, player.isActive());
            notifyMap.put(D.PLAYER_RID, player.getRoleId());
            notifyMap.put(D.TABLE_ACTION_SEAT_INDEX, seat);
            messageResult = new MessageResult(PLAYER_ISONLINE, notifyMap);
            this.pushService.broadcastWithOutPlayer(messageResult, player, room);
            room.getSeatMap().get(seat).setOffline(false);
            GameOnlineChangeUtils.incOnlineNum(Constant.GAME_ID, room.getGrade());
        }
    }

    /**
     * 正常发牌
     *
     * @param room
     */
    private void dealPoker(GameRoom room) {
        // 初始扑克
        LinkedList<Poker> deck = this.algService.createDeck();
        // 开始发牌
        List<BrandDTO> brandDTOList = new ArrayList<>();
        List<Long> playerList = new ArrayList<>();
        List<Long> robotList = new ArrayList<>();
        for (Seat seat : room.getSeatMap().values()) {
            if (!seat.isReady()) {
                continue;
            }
            if (!(seat.getPlayer() instanceof Robot)) {
                playerList.add(seat.getPlayer().getRoleId());
            } else {
                robotList.add(seat.getPlayer().getRoleId());
            }
            seat.setNoReady(0);
            // 每人3张牌
            List<Poker> handCards = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                Poker poker = deck.pop();
                handCards.add(poker);
            }
            BrandDTO brandDTO = new BrandDTO();
            brandDTO.setHand(handCards);
            brandDTO.setCombination(this.algService.evalPokerType(handCards));
            brandDTOList.add(brandDTO);
        }
        // 排序
        Collections.sort(brandDTOList);// .sort((x, y) -> Integer.compare(x.getCombination().getValue(),
        // y.getCombination().getValue()));
        log.info("排序完成：{}", brandDTOList.toString());
        // 查询水池配置
        Object object = this.redisUtil.get(RedisRegionConfig.FILE_WIN_GOLD_KEY + ":" + Constant.GAME_ID + "_" + room.getGrade());
        BigDecimal sysGold = new BigDecimal(object == null ? "0" : object.toString());
        GameWaterPoolDTO gameWaterPoolDTO = this.gameWaterPoolCacheService.getGameWaterPool(sysGold, com.dmg.zhajinhuaserver.common.constant.Constant.GAME_ID, room.getGrade());
        log.info("房间：{}，game-config查询水池配置：{}", room.getRoomId(), gameWaterPoolDTO.toString());
        Integer probability = gameWaterPoolDTO.getProbabilityBasics() + gameWaterPoolDTO.getProbabilityIncrease() * playerList.size();
        //查询玩家点控自控
        if (playerList.size() > 0) {
            log.info("房间：{}，调用game-config查询用户控制模型req data:{}", room.getRoomId(), playerList.toString());
            try {
                Map<Long, UserControlInfoDTO> result = userControlService.getUserControlModel(playerList);
                log.info("房间：{}，调用game-config查询用户控制模型resp data:{}", room.getRoomId(), result.toString());
                if (result != null && result.size() > 0) {
                    room.setUserControlInfoDTOMap(result);
                    result.values().forEach(userControlInfoDTO -> {
                        Boolean isWin = GameOddsEnum.getIsWinByIcon(userControlInfoDTO.getModel());
                        if (gameWaterPoolDTO.getIsSystem() ? isWin : !isWin) {
                            robotList.add(userControlInfoDTO.getUserId());
                            playerList.remove(userControlInfoDTO.getUserId());
                        }
                    });
                    probability = gameWaterPoolDTO.getProbabilityBasics()
                            + gameWaterPoolDTO.getProbabilityIncrease() * (gameWaterPoolDTO.getIsSystem() ? robotList.size() : playerList.size());
                }
            } catch (Exception e) {
                log.info("房间：{}，调用game-config查询用户控制模型出现异常:{}", room.getRoomId(), e);
            }
        }
        log.info("房间：{}，控制概率：{}", room.getRoomId(), probability);
        // 概率击中
        Long playerMax = null;
        if (RandomUtil.randomInt(10000) + 1 <= probability) {
            if (gameWaterPoolDTO.getIsSystem() && robotList != null && robotList.size() > 0) {
                playerMax = robotList.get(RandomUtil.randomInt(0, robotList.size()));
            } else {
                if (playerList != null && playerList.size() > 0) {
                    playerMax = playerList.get(RandomUtil.randomInt(0, playerList.size()));
                } else {
                    playerMax = robotList.get(RandomUtil.randomInt(0, robotList.size()));
                }
            }
            log.info("房间：{}，概率命中player:{}", room.getRoomId(), playerMax);
        }
        // 拿最大牌
        Boolean isMax = false;
        for (Seat seat : room.getSeatMap().values()) {
            if (!seat.isReady()) {
                continue;
            }
            Integer i;
            if (!isMax && playerMax != null && seat.getPlayer().getRoleId() == playerMax.longValue()) {
                i = brandDTOList.size() - 1;
                isMax = true;
            } else {
                if (isMax || playerMax == null) {
                    i = RandomUtil.randomInt(0, brandDTOList.size());
                } else {
                    if (brandDTOList.size() == 1) {
                        i = 0;
                    } else {
                        i = RandomUtil.randomInt(0, brandDTOList.size() - 1);
                    }
                }
            }
            BrandDTO brandDTO = brandDTOList.get(i);
            brandDTOList.remove(brandDTO);
            log.info("用户:{},手牌:{},手牌牌型:{}", seat.getPlayer().getRoleId(), brandDTO.getHand().toString(), brandDTO.getCombination());
            seat.addResultInfo(brandDTO.getCombination());
            seat.setHandCardsType(brandDTO.getCombination());
            Collections.sort(brandDTO.getHand());
            seat.setHand(brandDTO.getHand());
            seat.setHandSize(brandDTO.getHand().size());
            // 推送发牌消息
            this.sendCardsMSG(seat, room);
            if (seat.getPlayer() instanceof Robot) {
                seat.getPlayer().setPlayRounds(seat.getPlayer().getPlayRounds() + 1);
                seat.setTrust(true);
            }
        }
        this.nextDeal(room);
    }

    /**
     * 发牌之后的后续步骤
     *
     * @param room
     */
    private void nextDeal(GameRoom room) {
        // 推送旁观者
        if (room.getGameRoomTypeId() == D.PRIVATE_FIELD) {
            for (Seat seat : room.getSeatMap().values()) {
                if (!seat.isPlayed()) {
                    Player player = seat.getPlayer();
                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put(D.PLAYER_RID, player.getRoleId());
                    MessageResult messageResult = new MessageResult(GAME_SEND_CARDS, resultMap);
                    this.pushService.push(player.getRoleId(), messageResult);
                }
            }
        }
        if (room.getGameRoomTypeId() == D.FREE_FIELD) {
            // 设置庄家
            this.setBanker(room);
        }
        // 发送玩家行动、基本操作信息
        this.sendPlayerBaseAction(room, room.getPlaySeat());
    }

    /**
     * 发牌
     *
     * @param room
     */
    @Override
    public void distributePoker(GameRoom room) {
        this.dealPoker(room);
    }

    /**
     * 设置庄家位置
     *
     * @param room
     */
    @Override
    public void setBanker(GameRoom room) {
        if (room.getGameRoomTypeId() == D.PRIVATE_FIELD) {
            switch (room.getCustomRule().getFirstRule()) {
                case 0:
                    int banker = room.getBankerSeat();
                    this.getNextBanker(room, banker);
                    break;
                case 1:
                    room.setPlaySeat(room.getBankerSeat());
                    break;
                case 2:
                    int lastBanker = room.getLastBanker();
                    this.getNextBanker(room, lastBanker);
                    break;
            }
            for (Seat seat : room.getSeatMap().values()) {
                seat.setWinOrder(false);
            }
        } else {
            int bankSeat = 0;
            for (Entry<Integer, Seat> entry : room.getSeatMap().entrySet()) {
                Seat data = entry.getValue();
                if (data.isWinOrder()) {
                    bankSeat = entry.getKey();
                }
                data.setWinOrder(false);
            }
            Seat seat = room.getSeatMap().get(bankSeat);
            if (bankSeat == 0 || seat == null || !seat.isReady()) {
                for (Seat s : room.getSeatMap().values()) {
                    if (s.isReady()) {
                        room.setPlaySeat(s.getSeatId());
                        break;
                    }
                }
            } else {
                room.setPlaySeat(bankSeat);
            }
        }
    }

    /**
     * 获得首轮行动椅子
     *
     * @param room
     * @param banker
     */
    private void getNextBanker(GameRoom room, int banker) {
        for (int i = 1; i < room.getTotalPlayer(); i++) {
            int playSeat = (banker + i) % room.getTotalPlayer();
            if (playSeat == 0) {
                playSeat = room.getTotalPlayer();
            }
            Seat seat = room.getSeatMap().get(playSeat);
            if (seat == null || !seat.isReady()) {
                continue;
            }
            room.setPlaySeat(playSeat);
            room.setLastBanker(playSeat);
            room.setBankerSeat(playSeat);
            break;
        }
    }

    /**
     * 发牌消息推送
     *
     * @param seat
     */
    private void sendCardsMSG(Seat seat, GameRoom room) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Seat tempSeat : room.getSeatMap().values()) {
            if (tempSeat.isReady()) {
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put(D.PLAYER_RID, tempSeat.getPlayer().getRoleId());
                resultMap.put(D.TABLE_ACTION_SEAT_INDEX, tempSeat.getSeatId());
                resultMap.put(D.SEAT_HAND_CARDS, tempSeat.getBackHand());
                resultList.add(resultMap);
            }

        }
        MessageResult messageResult = new MessageResult(GAME_SEND_CARDS, resultList);
        this.pushService.push(seat.getPlayer().getRoleId(), messageResult);
    }

    /**
     * 发送玩家基本动作数据
     *
     * @param room
     * @param bankSeat
     */
    @Override
    @SuppressWarnings("unchecked")
    public void sendPlayerBaseAction(GameRoom room, int bankSeat) {
        try {
            Seat actionSeat = room.getSeatMap().get(bankSeat);
            if (room.getBloodId() == actionSeat.getPlayer().getRoleId()) {
                room.setHaveSeenCards(room.isBloodUpSeenCards());
                room.setLastBetChips(room.getBloodUpGold());
                room.setBloodId(0);
                room.setHaveRush(false);
            }
            // 行动玩家行动数据信息
            this.algService.haveBaseOper(actionSeat, room);
            if (actionSeat.getOperNotify().havaOper()) {
                if (room.getGameRoomTypeId() == D.PRIVATE_FIELD) {
                    switch (room.getCustomRule().getMenRule()) {
                        case 0:
                            if (room.getOperTurns() <= 1) {
                                actionSeat.getOperNotify().canSeeCard = false;
                                actionSeat.getOperNotify().canDisCard = false;
                            }
                            break;
                        case 1:
                            if (room.getOperTurns() <= 1) {
                                if (actionSeat.getSeatId() == room.getBankerSeat()) {
                                    actionSeat.getOperNotify().canSeeCard = false;
                                    actionSeat.getOperNotify().canDisCard = false;
                                }
                            }
                            break;
                    }
                }
                Map<String, Object> map = actionSeat.getOperNotify().toMap(actionSeat, true);
                map.put(ZhaJinHuaD.IS_RUSH, room.isHaveRush());
                if (room.getOperTurns() == room.getCountTurns()) {
                    map.put(D.PLAYER_BASE_ACTION_OPER, new ArrayList<>());
                }
                Player player = actionSeat.getPlayer();
                this.getRushMsg(player, room, map);
                // 服务器做自动跟注
                if (actionSeat.getPlayer() instanceof Robot) {
                    try {
                        int time = (int) (Math.random() * 5) + 1;
                        TimerManager.instance().submitDelayWork(AutoCallDelayWork.class, time * 1000, room.getRoomId(), actionSeat.getSeatId());
                    } catch (SchedulerException e) {
                        e.printStackTrace();
                        log.error("submit AutoCallDelayWork error");
                    }
                } else {
                    try {
                        TimerManager.instance().submitDelayWork(AutoCallDelayWork.class, (int) ZhaJinHuaD.PLAYER_CARD_TIME, room.getRoomId(), actionSeat.getSeatId());
                    } catch (SchedulerException e) {
                        e.printStackTrace();
                        log.error("submit AutoCallDelayWork error");
                    }
                }
                if (actionSeat.getOperNotify().canAddChips) {
                    List<Integer> list = new ArrayList<>();
                    for (int j = 0; j < room.getAddChipsBet(); j++) {
                        list.add(0);
                    }
                    for (int i = room.getAddChipsBet() + 1; i <= room.getBetMul().length; i++) {
                        if (actionSeat.isHaveSeenCard()) {
                            list.add(room.getBaseScore() * room.getBetMul()[i - 1] * 2);
                        } else {
                            list.add(room.getBaseScore() * room.getBetMul()[i - 1]);
                        }
                    }
                    map.put(ZhaJinHuaD.ADD_CHIPS, list);
                }
                if (actionSeat.getOperNotify().canFollowChips || actionSeat.getOperNotify().canCompare) {
                    long seatwager = room.wagerBetChips(bankSeat);
                    map.put(D.SEAT_FOLLOW_CHIPS, seatwager);
                } else {
                    map.put(D.SEAT_FOLLOW_CHIPS, 0);
                }
                MessageResult messageResult = new MessageResult(TURN_ACTION, map);
                this.pushService.broadcast(messageResult, room);
            }
        } catch (Exception e) {
            log.error("{}", e);
        }
    }

    /**
     * 获取玩家座位号
     *
     * @param room
     * @param player
     * @return
     */
    @Override
    public int getPlaySeat(GameRoom room, Player player) {
        for (Entry<Integer, Seat> entry : room.getSeatMap().entrySet()) {
            if (entry.getValue().getPlayer().getRoleId() == player.getRoleId()) {
                return entry.getKey();
            }
        }
        return 0;
    }

    /**
     * 获取玩家座位号
     *
     * @param room
     * @param player
     * @return
     */
    @Override
    public Seat getSeat(GameRoom room, Player player) {
        for (Entry<Integer, Seat> entry : room.getSeatMap().entrySet()) {
            if (entry.getValue().getPlayer().getRoleId() == player.getRoleId()) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * 获取房间
     *
     * @param roomId
     * @return
     */
    @Override
    public GameRoom getRoom(int roomId) {
        return RoomManager.instance().getRoom(roomId);
    }

    /**
     * 处理牌局结束
     *
     * @param room
     */
    @Override
    public void handleRoundEnd(GameRoom room, boolean bool) {
        try {
            int winSeatId = 0;
            GameRoom gameRoom = RoomManager.instance().getRoom(room.getRoomId());
            BigDecimal reward;
            for (Seat seat : room.getSeatMap().values()) {
                if (room.getGameRoomTypeId() == D.PRIVATE_FIELD) {
                    if (!seat.isPlayed()) {
                        continue;
                    }
                }
                if (seat.getPlayer() == null || !seat.isReady()) {
                    continue;
                }
                if (seat.isWinOrder()) {
                    winSeatId = seat.getSeatId();
                    log.info("winseatid:" + winSeatId);
                }
            }
            // 计算喜钱
            reward = new BigDecimal("0"); // getReward(room, winSeatId, reward);

            // @author LINANJUN 一局游戏结束更新机器人行为概率
            // RobotAutoActionLogic.oneRoundEndChangeRobotActionLv(room, winSeatId);

            // 发送玩家结算数据
            List<OutPlayerDTO> outList = new ArrayList<>();
            RecordDataDTO recordDataDTO = this.sendResultMsg(room, winSeatId, reward, outList);
            // 记录战绩
            this.recordData(room, recordDataDTO);

            // 重置数据，准备开始下一把
            if (bool) {
                this.resetRoomAndBegin(gameRoom, outList);
            } else {
                // 清除数据
                this.clearRoomData(room);
            }
        } catch (Exception e) {
            log.error("结算出现异常:{}", e);
        }
    }

    /**
     * 清除房间数据
     *
     * @param room
     */
    private void clearRoomData(GameRoom room) {
        for (Seat seat : room.getSeatMap().values()) {
            Player player = seat.getPlayer();
            player.setRoomId(0);
            this.playerCacheService.update(player);
            seat.clear(true);
        }
        for (Player player : room.getWatchList()) {
            player.setRoomId(0);
            this.playerCacheService.update(player);
        }
        room.clearAll();
    }

    /**
     * 记录战绩
     *
     * @param room
     * @param recordDataDTO
     */
    private void recordData(GameRoom room, RecordDataDTO recordDataDTO) {
        // 发送结算数据l
        String roundCode = RoundIdUtils.getGameRecordId(String.valueOf(Constant.GAME_ID), String.valueOf(room.getRoomId()));
        recordDataDTO.getBalanceList().forEach(balance -> {
            try {
                UserControlInfoDTO userControlInfoDTO = room.getUserControlInfoDTOMap().get(balance.getRoleId());
                this.defautProducer.send(JSON.toJSON(GameRecordDTO.<UserResultDTO<Poker>>builder()
                        .gameId(Constant.GAME_ID)
                        .gameDate(new Date())
                        .userId(balance.getRoleId())
                        .userName(balance.getRolename())
                        .fileId(room.getGrade())
                        .fileName(room.getGradeName())
                        .baseScore(BigDecimal.valueOf(room.getBaseScore()))
                        .afterGameGold(balance.getTotalscore())
                        .beforeGameGold(balance.getTotalscore().subtract(balance.getWinscore()))
                        .betsGold(balance.getBetChips())
                        .winLosGold(balance.getWinscore())
                        .serviceCharge(balance.getPumpMoney())
                        .isRobot(balance.getIsRobot())
                        .roundCode(roundCode)
                        .controlState((userControlInfoDTO != null && userControlInfoDTO.getControlState().intValue() == UserControlStateEnum.CONTROL_STATE_POINT.getCode().intValue()))
                        .gameResult(recordDataDTO.getUserResult()).build()).toString());
            } catch (Exception e) {
                log.error("战绩信息发送异常:{}", e);
            }
        });

    }

    /**
     * 计算结算分数并发送数据
     *
     * @param room
     * @param winSeatId
     * @param reward
     */
    private RecordDataDTO sendResultMsg(GameRoom room, int winSeatId, BigDecimal reward, List<OutPlayerDTO> outList) {
        try {
            Map<String, Balance> balanceMap = new HashMap<>();
            int count = 0;
            List<Object> pokerList = new ArrayList<>();
            for (Seat data : room.getSeatMap().values()) {
                if (room.getGameRoomTypeId() == D.PRIVATE_FIELD) {
                    if (data.isPlayed()) {
                        count++;
                    }
                } else {
                    if (data.isReady()) {
                        count++;
                    }
                }
                if (!CollUtil.isEmpty(data.getHand())) {
                    List<Poker> ps = data.getHand();
                    for (Poker p : ps) {
                        pokerList.add(p.fullValue());

                    }
                }
            }
            List<Object> dataList = new ArrayList<>();
            dataList.add(winSeatId);
            List<UserResultDTO<Poker>> userResultDTOList = new ArrayList<>();
            List<Balance> balanceList = new ArrayList<>();
            for (Entry<Integer, Seat> entry : room.getSeatMap().entrySet()) {
                Seat data = entry.getValue();
                if (room.getGameRoomTypeId() == D.PRIVATE_FIELD && !data.isPlayed()) {
                    continue;
                }
                BigDecimal pumpMoney = BigDecimal.ZERO;
                Balance balance = new Balance(data.getPlayer().getRoleId(), room.getRound(), data.getPlayer().getNickname());
                if (winSeatId == 0) {
                    data.setChipsRemain(data.getChipsRemain() + data.getBetChips());
                } else if (winSeatId == entry.getKey()) {
                    // 赢家抽水
                    BigDecimal winscore = BigDecimal.valueOf(room.getTotalChips() - data.getBetChips()).add(reward);
                    pumpMoney = winscore.multiply(BigDecimal.valueOf(room.getPumpRate())).divide(BigDecimal.valueOf(100));
                    log.info("房间：{}，结算数据player:{},赢钱：{}，抽水金额：{}", room.getRoomId(), data.getPlayer().getRoleId(), winscore, pumpMoney);
                    this.redisUtil.incr(RedisRegionConfig.FILE_PUMP_KEY + ":" + Constant.GAME_ID + "_" + room.getGrade(), pumpMoney.doubleValue());
                    balance.setWinscore(winscore.subtract(pumpMoney));
                    balance.setIswin(true);
                    balance.setTotalscore(BigDecimal.valueOf(data.getChipsRemain() + data.getBetChips()).add(balance.getWinscore()));
                    data.setChipsRemain(balance.getTotalscore().doubleValue());
                    balance.setTakebackscore(room.getTotalChips());
                    BigDecimal sendGold = balance.getWinscore().add(BigDecimal.valueOf(data.getBetChips()));
                    if (!(data.getPlayer() instanceof Robot)) {
                        this.playerCacheService.increaseGold(sendGold, data.getPlayer().getRoleId());
                    }
                    data.getPlayer().setGold(data.getPlayer().getGold() + balance.getWinscore().doubleValue() + data.getBetChips());
                    this.playerCacheService.update(data.getPlayer());
                } else {
                    balance.setWinscore(BigDecimal.valueOf(-data.getBetChips()).subtract(reward.divide(BigDecimal.valueOf(count - 1))));
                    balance.setIswin(false);
                    balance.setTotalscore(BigDecimal.valueOf(data.getChipsRemain()).subtract(reward.divide(BigDecimal.valueOf(count - 1))));
                    data.setChipsRemain(balance.getTotalscore().doubleValue());
                    balance.setTakebackscore(0);
                }
                userResultDTOList.add(UserResultDTO.<Poker>builder()
                        .pokers(data.getHand())
                        .pokerType(String.valueOf(data.getHandCardsType()))
                        .userId(Integer.parseInt(String.valueOf(data.getPlayer().getRoleId())))
                        .isRobot((data.getPlayer() instanceof Robot))
                        .winLosGold(balance.getWinscore()).build());
                balance.setRolename(data.getPlayer().getNickname());
                balance.setCard_type(data.getHandCardsType());
                List<Poker> list = new ArrayList<>();
                for (Poker p : data.getHand()) {
                    list.add(p);
                }
                balance.setHand_cards(list);
                balance.setSeat_index(data.getSeatId());
                data.getBalanceList().add(balance);
                balanceMap.put(String.valueOf(entry.getKey()), balance);
                balance.setPumpMoney(pumpMoney);
                balance.setBetChips(BigDecimal.valueOf(data.getBetChips()));
                if (data.getPlayer() instanceof Robot) {
                    if (balance.getWinscore().compareTo(BigDecimal.ZERO) > 0) {
                        this.redisUtil.incr(RedisRegionConfig.FILE_WIN_GOLD_KEY + ":" + Constant.GAME_ID + "_" + room.getGrade(), balance.getWinscore().doubleValue());
                    } else {
                        this.redisUtil.decr(RedisRegionConfig.FILE_WIN_GOLD_KEY + ":" + Constant.GAME_ID + "_" + room.getGrade(), -balance.getWinscore().doubleValue());
                    }
                } else {
                    balance.setIsRobot(false);
                }
                balanceList.add(balance);
                if (room.getGameRoomTypeId() == D.FREE_FIELD && balance.getWinscore().compareTo(BigDecimal.ZERO) > 0) {
                    data.getPlayer().setWinLostGold(data.getPlayer().getWinLostGold().add(BigDecimal.valueOf(data.getBetChips())).add(balance.getWinscore()));
                } else {
                    data.getPlayer().setWinLostGold(data.getPlayer().getWinLostGold().add(balance.getWinscore()));
                }

                int lostGold = GameConfig.robotPercents.get(Config.RobotPercent.OUT_LOST_MONEY);
                int winGold = GameConfig.robotPercents.get(Config.RobotPercent.OUT_WIN_MONEY);
                int playRounds = GameConfig.robotPercents.get(Config.RobotPercent.OUT_PLAY_ROUNDS);
                if (data.getPlayer() instanceof Robot
                        && (data.getPlayer().getWinLostGold().doubleValue() <= -room.getBaseScore() * lostGold
                        || data.getPlayer().getWinLostGold().doubleValue() >= room.getBaseScore() * winGold)) {

                    OutPlayerDTO outPlayerDTO = new OutPlayerDTO();
                    outPlayerDTO.setSeatId(data.getSeatId());
                    outPlayerDTO.setLeaveReason(Config.LeaveReason.LEAVE_NORMAL);
                    outList.add(outPlayerDTO);
                }
                if (data.getPlayer().getPlayRounds() >= playRounds) {
                    Boolean isSeatId = false;
                    for (OutPlayerDTO outPlayerDTO : outList) {
                        if (outPlayerDTO.getSeatId() == data.getSeatId()) {
                            isSeatId = true;
                            break;
                        }
                    }
                    if (!isSeatId) {
                        OutPlayerDTO outPlayerDTO = new OutPlayerDTO();
                        outPlayerDTO.setSeatId(data.getSeatId());
                        outPlayerDTO.setLeaveReason(Config.LeaveReason.LEAVE_NORMAL);
                        outList.add(outPlayerDTO);
                    }
                }
            }
            MessageResult messageResult = new MessageResult(GAME_RESULT_MSG, balanceMap);
            this.pushService.broadcast(messageResult, room);
            return RecordDataDTO.builder().balanceList(balanceList).userResult(userResultDTOList).build();
        } catch (Exception e) {
            log.error("结算时出现异常:{}", e);
        }
        return null;
    }

    /**
     * 计算喜钱
     *
     * @param room
     * @param winSeatId
     * @param reward
     * @return
     */
    public int getReward(GameRoom room, int winSeatId, int reward) {
        Seat winSeat = room.getSeatMap().get(winSeatId);
        if (winSeat == null) {
            return 0;
        }
        log.info("seat:" + winSeat + "----room seatmap:" + room.getSeatMap());
        if (winSeat.isWinOrder() && winSeat.getHandCardsType() == Config.Combination.LEOPARD.getValue()) {
            room.setHaveThree(true);
        }
        if (winSeat.isWinOrder() && winSeat.getHandCardsType() == Config.Combination.STRAIGHTFLUSH.getValue()) {
            room.setHaveShunJin(true);
        }
        if (room.isHaveThree()) {
            for (Seat seat : room.getSeatMap().values()) {
                if (room.getGameRoomTypeId() == D.PRIVATE_FIELD) {
                    if (!seat.isPlayed()) {
                        continue;
                    }
                    if (seat.isReady() && seat.getSeatId() != winSeatId) {
                        reward += room.getBaseScore() * room.getCustomRule().getBaoXiBet();
                    }
                } else {
                    if (seat.isReady() && seat.getSeatId() != winSeatId) {
                        reward += seat.getChipsRemain() < room.getBaseScore() * ZhaJinHuaD.ROOM_BAOREWARD_MULTIPLE ? seat.getChipsRemain() : room.getBaseScore() * ZhaJinHuaD.ROOM_BAOREWARD_MULTIPLE;
                    }
                }
            }
        }
        if (room.isHaveShunJin()) {
            for (Seat seat : room.getSeatMap().values()) {
                if (room.getGameRoomTypeId() == D.PRIVATE_FIELD) {
                    if (!seat.isPlayed()) {
                        continue;
                    }
                    if (seat.isReady() && seat.getSeatId() != winSeatId) {
                        reward += room.getBaseScore() * room.getCustomRule().getShunXiBet();
                    }
                } else {
                    if (seat.isReady() && seat.getSeatId() != winSeatId) {
                        reward += seat.getChipsRemain() < room.getBaseScore() * ZhaJinHuaD.ROOM_SHUNREWARD_MULTIPLE ? seat.getChipsRemain() : room.getBaseScore() * ZhaJinHuaD.ROOM_SHUNREWARD_MULTIPLE;
                    }
                }
            }
        }
        return reward;
    }

    /**
     * 重置房间数据，开始下一把游戏
     *
     * @param room
     */
    private void resetRoomAndBegin(GameRoom room, List<OutPlayerDTO> outList) {
        Object object = this.redisUtil.get(RedisRegionConfig.FILE_WIN_GOLD_KEY + ":" + Constant.GAME_ID + "_" + room.getGrade());
        BigDecimal sysGold = new BigDecimal(object == null ? "0" : object.toString());
        Object pumpObj = this.redisUtil.get(RedisRegionConfig.FILE_PUMP_KEY + ":" + Constant.GAME_ID + "_" + room.getGrade());
        BigDecimal pump = new BigDecimal(pumpObj == null ? "0" : pumpObj.toString());
        log.info("房间：{}，房间等级：{}，当前系统金币为：{},抽水金币为：{}", room.getRoomId(), room.getGrade(), sysGold, pump);
        //暂时存储金币记录
        redisUtil.set("file_win_gold_record" + ":" + Constant.GAME_ID + "_" + room.getGrade() + "_" + new Date().getTime(), sysGold.toString());
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            log.error("{}", e);
        }
        room.clear();
        // 停服通知
        if (RoomManager.instance().isShutdownServer()) {
            room.setRoomStatus(Config.RoomStatus.STOP);
            for (Entry<Integer, Seat> entry : room.getSeatMap().entrySet()) {
                Seat seat = entry.getValue();
                if (!(seat.getPlayer() instanceof Robot)) {
                    MessageResult messageResult = new MessageResult(MessageConfig.SHUTDOWN_SERVER_NTC);
                    this.pushService.push(seat.getPlayer().getRoleId(), messageResult);
                    continue;
                }
            }
            return;
        }
        long time = System.currentTimeMillis() + 10000;
        Map<String, Object> map = new HashMap<>();
        map.put(D.READY_TO_TIME, time);
        MessageResult messageResult = new MessageResult(PLAYER_READY_NTC, map);
        for (Entry<Integer, Seat> entry : room.getSeatMap().entrySet()) {
            Seat data = entry.getValue();
            if (data.getChipsRemain() < room.getLowerLimit().doubleValue()) {
                if (data.getPlayer() instanceof Robot) {
                    Boolean isSeatId = false;
                    for (OutPlayerDTO outPlayerDTO : outList) {
                        if (outPlayerDTO.getSeatId() == data.getSeatId()) {
                            isSeatId = true;
                            break;
                        }
                    }
                    if (!isSeatId) {
                        OutPlayerDTO outPlayerDTO = new OutPlayerDTO();
                        outPlayerDTO.setSeatId(data.getSeatId());
                        outPlayerDTO.setLeaveReason(Config.LeaveReason.LEAVE_NORMAL);
                        outList.add(outPlayerDTO);
                    }
                } else {
                    // outList.add(data.getSeatId());
                    OutPlayerDTO outPlayerDTO = new OutPlayerDTO();
                    outPlayerDTO.setSeatId(data.getSeatId());
                    outPlayerDTO.setLeaveReason(Config.LeaveReason.LEAVE_NOMONEY);
                    outList.add(outPlayerDTO);
                }
            }
            if (data.getPlayer().isActive()) {
                data.getPlayer().setDisconnectTime(0);
            }
            if (!(data.getPlayer() instanceof Robot)) {
                Player player = playerCacheService.getPlayer(data.getPlayer().getRoleId());
                if (!player.isActive()) {
                    // 离线
                    data.getPlayer().setLeaveReason(Config.LeaveReason.LEAVE_OFFLINE);
                    OutPlayerDTO outPlayerDTO = new OutPlayerDTO();
                    outPlayerDTO.setSeatId(data.getSeatId());
                    outPlayerDTO.setLeaveReason(Config.LeaveReason.LEAVE_NORMAL);
                    outList.add(outPlayerDTO);
                }
            }
            data.clear(false);
            data.setState(SeatState.STATE_WAIT_READY);
            map.put(D.ROLE_ID, data.getPlayer().getRoleId());
            data.setActionEndTime(time);
            this.pushService.push(data.getPlayer().getRoleId(), messageResult);
        }

        if (CollUtil.isNotEmpty(outList)) {
            for (Seat seat : room.getSeatMap().values()) {
                outList.forEach(outPlayer -> {
                    if (outPlayer.getSeatId() == seat.getSeatId()) {
                        quitRoomService.quitRoom(seat.getPlayer(), false, outPlayer.getLeaveReason());
                    }
                });
            }
        }

        // 自动准备(机器人自动准备/玩家不自动准备)
        for (Seat seat : room.getSeatMap().values()) {
            if (seat.getPlayer() instanceof Robot) {
                try {
                    TimerManager.instance().submitDelayWork(AutoReadyDelayWork.class, RandomUtil.randomInt(4000), room.getRoomId(), seat);
                } catch (SchedulerException e) {
                    log.error("submit AutoReadyDelayWork error:{}", e);
                }
            }
        }

        try {
            TimerManager.instance().submitDelayWork(ReadyTimeEndDelayWork.class, room.getReadyTime(), room.getRoomId());
        } catch (SchedulerException e) {
            log.error("submit ReadyTimeEndDelayWork error:{}", e);
        }
        try {
            TimerManager.instance().submitDelayWork(ReadyTimeEndGameStartDelayWork.class, 10000, room.getRoomId());
        } catch (SchedulerException e) {
            log.error("submit ReadyTimeEndGameStartDelayWork error:{}", e);
        }
    }

    /**
     * 处理房间结束
     *
     * @param room --解散房间不需要广播结算消息
     */
    @Override
    public void handRoomEnd(GameRoom room, boolean noPlay) {
        try {
            List<Record> list = new ArrayList<>();
            List<RoomRecord> roomRecordList = new ArrayList<>();
            String date = cn.hutool.core.date.DateUtil.now();
            int roundTime = (int) (System.currentTimeMillis() - room.getCreateTime());
            long unionId = System.currentTimeMillis() * 1000000 + room.getRoomId();
            for (Entry<Integer, Seat> entry : room.getSeatMap().entrySet()) {
                Seat data = entry.getValue();
                room.getWinScoreMap().put(data.getPlayer().getRoleId(), data);
            }
            for (Entry<Long, Seat> entry : room.getWinScoreMap().entrySet()) {
                Seat data = entry.getValue();
                Record record = new Record();
                record.setGame("zjh");
                record.setUniqueSign(unionId);
                record.setRoleId(data.getPlayer().getRoleId());
                record.setRoomId(room.getRoomId());
                record.setHeadImgUrl(data.getPlayer().getHeadImgUrl());
                record.setNickname(data.getPlayer().getNickname());
                record.setPlatform(data.getPlayer().getPlatform());
                record.setScore(data.getScore());
                record.setRoundTime(roundTime);
                record.setDate(date);
                list.add(record);
                //
                RoomRecord roomRecord = new RoomRecord();
                roomRecord.setGameObject("zjh");
                roomRecord.setServerId(14);
                roomRecord.setRoleId(data.getPlayer().getRoleId());
                roomRecord.setHeadImgUrl(data.getPlayer().getHeadImgUrl());
                roomRecord.setRoomId(room.getRoomId());
                roomRecord.setAllTime((int) ((System.currentTimeMillis() - room.getPlayTime()) / 1000));
                if (data.getPlayer().getRoleId() == room.getCreator()) {
                    roomRecord.setStatus(0);
                } else {
                    roomRecord.setStatus(1);
                }
                roomRecord.setPlatform(data.getPlayer().getPlatform());
                roomRecord.setUniqueSign("" + unionId);
                roomRecord.setEndTime(System.currentTimeMillis());
                roomRecord.setCreateTime(room.getPlayTime());
                roomRecord.setAddress("diqu.vequn.cn");
                roomRecord.setServerId(13);
                roomRecord.setRound(room.getRound());
                roomRecordList.add(roomRecord);
            }

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("list", list);
            resultMap.put("round", room.getRound());
            MessageResult messageResult = new MessageResult("132014", resultMap);
            this.pushService.broadcast(messageResult, room);
            // 保存游戏记录
            if (!list.isEmpty()) {
                // TODO 保存游戏记录到数据库
            }
            if (!roomRecordList.isEmpty()) {
                // TODO 保存游戏记录到数据库
            }
            // 清除数据
            for (Entry<Integer, Seat> entry : room.getSeatMap().entrySet()) {
                Player player = entry.getValue().getPlayer();
                player.setRoomId(0);
                this.playerCacheService.update(player);
            }
            for (Player player : room.getWatchList()) {
                player.setRoomId(0);
                this.playerCacheService.update(player);
            }
        } catch (Exception e) {
            log.error("{}", e);
        }

    }

    /**
     * 设置下一个出牌的座位
     *
     * @param room
     */
    @Override
    public void setNextPlaySeat(GameRoom room) {
        int playSeat = room.getPlaySeat();
        for (int i = 1; i < room.getTotalPlayer(); i++) {
            int id = (playSeat + i) % room.getTotalPlayer();
            if (id == 0) {
                id = room.getTotalPlayer();
            }
            Seat seat = room.getSeatMap().get(id);
            if (seat == null || !seat.isReady()) {
                continue;
            }
            if (room.getGameRoomTypeId() == D.PRIVATE_FIELD) {
                if (!seat.isPlayed()) {
                    continue;
                }
            }
            if (seat.isPass() && seat.getPlayer().getRoleId() == room.getBloodId()) {
                room.setHaveRush(false);
            }
            if (!seat.isPass()) {
                room.setPlaySeat(id);
                break;
            }
        }
    }

    /**
     * 获取下一个可行动的座位
     *
     * @param room
     */
    @Override
    public int getNextSeat(GameRoom room) {
        int playSeat = room.getPlaySeat();
        for (int i = 1; i < room.getTotalPlayer(); i++) {
            int id = (playSeat + i) % room.getTotalPlayer();
            if (id == 0) {
                id = room.getTotalPlayer();
            }
            Seat seat = room.getSeatMap().get(id);
            if (seat == null || !seat.isReady()) {
                continue;
            }
            if (room.getGameRoomTypeId() == D.PRIVATE_FIELD) {
                if (!seat.isPlayed()) {
                    continue;
                }
            }
            if (seat.isPass() && seat.getPlayer().getRoleId() == room.getBloodId()) {
                room.setHaveRush(false);
            }
            if (!seat.isPass()) {
                return id;
            }
        }
        return 0;
    }

    /**
     * 删除房卡房
     *
     * @param room
     */
    @Override
    public void deletePrivateRoom(GameRoom room) {
        RoomManager.instance().removeRoom(room.getRoomId());
    }

    /**
     * 成功解散房间
     *
     * @param room
     */
    @Override
    public void dissolveRoomSuccess(GameRoom room) {
        room.setDissolveTime(0);
        room.setDissolveRid(0);
        this.handleRoundEnd(room, false);
    }

    @Override
    public void disconnect(Player player) {
        GameRoom room = this.getRoom(player.getRoomId());
        if (room == null) {
            return;
        }
        int seat = this.getPlaySeat(room, player);
        if (seat == 0) {
            room.getWatchList().remove(player);
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put(D.SEAT_PLAYER_ONLINE, player.isActive());
            map.put(D.PLAYER_RID, player.getRoleId());
            map.put(D.TABLE_ACTION_SEAT_INDEX, seat);
            MessageResult messageResult = new MessageResult(PLAYER_ISONLINE, map);
            this.pushService.broadcastWithOutPlayer(messageResult, player, room);
            room.getSeatMap().get(seat).setOffline(true);
        }
    }

    /**
     * 获得能行动玩家数量
     *
     * @param room
     * @return
     */
    @Override
    public int getCanActionCout(GameRoom room) {
        int canActionseat = 0;
        for (Seat ss : room.getSeatMap().values()) {
            if (room.getGameRoomTypeId() == D.PRIVATE_FIELD) {
                if (!ss.isPlayed()) {
                    continue;
                }
            }
            if (ss.isReady() && !ss.isPass()) {
                canActionseat++;
            }
        }
        return canActionseat;
    }

    /**
     * 钱不够弃牌
     *
     * @param oper
     * @param room
     * @param seat
     * @return
     */
    @Override
    public int noMoneyDiscard(int oper, GameRoom room, Seat seat, long wager) {
        if (seat.isHaveSeenCard() && oper != PokerOperState.SEECARDS) {
            if ((room.isHaveSeenCards() && seat.getChipsRemain() < room.getLastBetChips()) || (!room.isHaveSeenCards() && seat.getChipsRemain() < room.getLastBetChips() * 2)) {
                oper = PokerOperState.DISCARD;
            }
        } else if (!seat.isHaveSeenCard() && oper != PokerOperState.SEECARDS) {
            if ((room.isHaveSeenCards() && seat.getChipsRemain() < room.getLastBetChips() / 2) || (!room.isHaveSeenCards() && seat.getChipsRemain() < room.getLastBetChips())) {
                oper = PokerOperState.DISCARD;
            } else if (oper == PokerOperState.ADDCHIPS) {
                int addindex = room.getAddChipsBet();
                if (wager > room.getBetMul().length && room.getAddChipsBet() < room.getBetMul().length) {
                    addindex = room.getAddChipsBet() + 1;
                } else {
                    addindex = (int) wager;
                }
                if (addindex > room.getBetMul().length) {
                    addindex = room.getBetMul().length;
                }
                long curBetChips = 0;
                if (seat.isHaveSeenCard()) {
                    curBetChips = room.getBaseScore() * room.getBetMul()[addindex - 1] * 2;
                } else {
                    curBetChips = room.getBaseScore() * room.getBetMul()[addindex - 1];
                }
                if (curBetChips > seat.getChipsRemain()) {
                    oper = PokerOperState.DISCARD;
                }
            }
        }
        return oper;
    }

    /**
     * 获得血拼数据
     *
     * @param player
     * @param room
     * @param map
     */
    @Override
    public void getRushMsg(Player player, GameRoom room, Map<String, Object> map) {
        Seat seat = this.getSeat(room, player);
        if (room.getOperTurns() >= 3 && room.getBloodId() == 0) {
            if (seat.isHaveSeenCard()) {
                if (room.getGameRoomTypeId() == D.FREE_FIELD && seat.getChipsRemain() < room.getLastBetChips() * room.getOperTurns() * 2) {
                    return;
                }
                map.put(ZhaJinHuaD.RUSH_CHIPS, room.getLastBetChips() * room.getOperTurns() * 2);
                seat.getActionOper().add(PokerOperState.RUSH);
            } else {
                if (room.getGameRoomTypeId() == D.FREE_FIELD && seat.getChipsRemain() < room.getLastBetChips() * room.getOperTurns()) {
                    return;
                }
                map.put(ZhaJinHuaD.RUSH_CHIPS, room.getLastBetChips() * room.getOperTurns());
                seat.getActionOper().add(PokerOperState.RUSH);
            }
        }
    }

    /**
     * 当前轮数超过最大轮数限制
     *
     * @param room
     */
    @Override
    public void turnsOverMax(GameRoom room) {
        log.info("房间:{},当前轮数:{},最大轮数:{}", room.getRoomId(), room.getOperTurns(), room.getCountTurns());
        if (room.getOperTurns() == room.getCountTurns()) {
            Seat seat = room.getSeatMap().get(room.getPlaySeat());
            int beSeatid = 0;
            for (int i = 1; i < room.getTotalPlayer(); i++) {
                int seatId = (seat.getSeatId() + i) % room.getTotalPlayer();
                if (seatId == 0) {
                    seatId = room.getTotalPlayer();
                }
                Seat beSeat = room.getSeatMap().get(seatId);
                if (room.getGameRoomTypeId() == D.PRIVATE_FIELD) {
                    if (beSeat == null || !beSeat.isPlayed()) {
                        continue;
                    }
                }
                if (beSeat != null && beSeat.isReady() && !beSeat.isPass() && beSeat.getSeatId() != seat.getSeatId()) {
                    beSeatid = beSeat.getSeatId();
                    break;
                }
            }
            long wager = room.wagerChips(room.getPlaySeat());
            try {
                log.info("房间:{},当前轮数:{},最大轮数:{},【自动比牌】", room.getRoomId(), room.getOperTurns(), room.getCountTurns());
                TimerManager.instance().submitDelayWork(AutoCompareCardsDelayWork.class, 3000, room.getRoomId(), seat.getSeatId(), beSeatid, wager);
            } catch (SchedulerException e) {
                log.error("submit AutoCompareCardsDelayWork error:{}", e);
            }
        }
    }

    /**
     * 剩余一个玩家进入结算
     *
     * @param room
     * @return
     */
    @Override
    public boolean resultByOnlyOne(GameRoom room) {
        int count = 0;
        Seat lastOne = null;
        for (Seat c : room.getSeatMap().values()) {
            if (room.getGameRoomTypeId() == D.PRIVATE_FIELD) {
                if (!c.isPlayed()) {
                    continue;
                }
            }
            if (c.isReady() && !c.isPass()) {
                count++;
                lastOne = c;
            }
        }
        if (count == 1) {
            room.setHaveRush(false);
            ;
            lastOne.setWinOrder(true);
            room.setBankerSeat(lastOne.getSeatId());
            try {
                TimerManager.instance().submitDelayWork(BalanceResultDelayWork.class, 2000, room.getRoomId());
            } catch (SchedulerException e) {
                log.error("submit BalanceResultDelayWork error:{}", e);
            }
            return true;
        }
        return false;
    }

    /**
     * 看牌操作不计入轮数计算
     *
     * @param room
     */
    @Override
    public void addTurns(GameRoom room) {
        boolean oneTurnEnd = true;
        for (Seat c : room.getSeatMap().values()) {
            if (!c.isReady() || c.isPass()) {
                continue;
            }
            if (room.getGameRoomTypeId() == D.PRIVATE_FIELD) {
                if (!c.isPlayed()) {
                    continue;
                }
            }
            if (!c.isOperedInOneTurn()) {
                oneTurnEnd = false;
                break;
            }
        }
        if (oneTurnEnd) {
            for (Seat c : room.getSeatMap().values()) {
                if (!c.isReady() || c.isPass()) {
                    continue;
                }
                if (room.getGameRoomTypeId() == D.PRIVATE_FIELD) {
                    if (!c.isPlayed()) {
                        continue;
                    }
                }
                c.setOperedInOneTurn(false);
            }
            room.setOperTurns(room.getOperTurns() >= room.getCountTurns() ? room.getCountTurns() : room.getOperTurns() + 1);
            // 调用机器人行为概率变化
            // RobotAutoActionLogic.oneTurnEndChangeRobotActionLv(room);
            // 第一轮结束发送可以看牌操作
            if (room.getOperTurns() == 2) {
                Map<String, Object> map = new HashMap<>();
                map.put(D.PLAYER_BASE_ACTION_OPER, Config.PokerOperState.SEECARDS);
                MessageResult messageResult = new MessageResult(SEE_CARD_ACTION, map);
                room.getSeatMap().values().forEach(seat -> {
                    if (seat.isReady() && !seat.isPass()) {
                        this.pushService.push(seat.getPlayer().getRoleId(), messageResult);
                    }
                });
            }
            if (room.getOperTurns() == room.getCountTurns()) {
                for (Seat c : room.getSeatMap().values()) {
                    if (!c.isReady() || c.isPass() || !c.isHasFollowEnd()) {
                        continue;
                    }
                    if (room.getGameRoomTypeId() == D.PRIVATE_FIELD) {
                        if (!c.isPlayed()) {
                            continue;
                        }
                    }
                    c.setHasFollowEnd(false);
                    Map<String, Object> map = new HashMap<>();
                    map.put(ZhaJinHuaD.ACTION_TYPE, PokerOperState.AUTOCALL);
                    map.put(D.SEAT_IS_AUTOCALL, c.isHasFollowEnd());
                    MessageResult messageResult = new MessageResult(PLAYER_PLAY, map);
                    this.pushService.push(c.getPlayer().getRoleId(), messageResult);
                }
            }
        }
    }

    /**
     * 玩家进行比牌操作
     *
     * @param room
     * @param seat
     * @param seatWager
     */
    @Override
    public int playerCompareCards(GameRoom room, Seat seat, Seat pkseat, long seatWager, boolean win, int oper) {
        if (!room.isHaveSeenCards()) {
            if (seat.isHaveSeenCard()) {
                room.setHaveSeenCards(true);
            }
        } else {
            if (!seat.isHaveSeenCard()) {
                room.setHaveSeenCards(false);
            }
        }
        if (room.getGameRoomTypeId() == D.FREE_FIELD && !(seat.getPlayer() instanceof Robot)) {
            BetRecvDto betRecvDto = this.playerCacheService.decreaseGold(new BigDecimal(seatWager).negate(), seat.getPlayer().getRoleId());
            if (betRecvDto.getCode() != 0) {
                this.pushService.push(seat.getPlayer().getRoleId(), PLAYER_PLAY, ResultEnum.PLAYER_HAS_NO_MONEY.getCode());
                return 2;
            }
            seat.getPlayer().setGold(seat.getPlayer().getGold() - seatWager);
            this.playerCacheService.update(seat.getPlayer());
        }
        seat.setChipsRemain(seat.getChipsRemain() - seatWager);
        room.setTotalChips(room.getTotalChips() + seatWager);
        seat.setBetChips(seat.getBetChips() + seatWager);
        room.getBetList().add((int) seatWager);
        room.setLastBetChips(seatWager);
        List<Poker> cardList = this.getSeatCardList(seat);
        List<Poker> becardList = this.getSeatCardList(pkseat);
        // ---------比较两家的牌-----PK---
        win = this.algService.operOneWin(room, cardList, becardList);
        log.info("***playerName：" + seat.getPlayer().getNickname() + "," + seat.getPlayer().getRoleId() + ",seatId: " + seat.getSeatId()
                + ",cardsType :" + seat.getHandCardsType() + ",cards:" + seat.getHand() + "+++iswin：" + win);
        log.info("___playerName：" + pkseat.getPlayer().getNickname() + "," + pkseat.getPlayer().getRoleId() + ",seatid:" + pkseat.getSeatId()
                + ",cardsType :" + pkseat.getHandCardsType() + ",cards:" + pkseat.getHand());
        if (win) {
            pkseat.setPass(true);
            pkseat.setLostPk(true);
            pkseat.setState(SeatState.STATE_COMPARE_FAIL);
        } else {
            seat.setPass(true);
            seat.setLostPk(true);
            seat.setState(SeatState.STATE_COMPARE_FAIL);
        }
        return win ? 1 : 0;
    }

    /**
     * 返回玩家扑克列表
     *
     * @param seat
     * @return
     */
    private List<Poker> getSeatCardList(Seat seat) {
        List<Poker> cardList = new ArrayList<>();
        for (Poker poker : seat.getHand()) {
            cardList.add(poker);
        }
        return cardList;
    }

    /**
     * 玩家加注，跟注操作
     *
     * @param room
     * @param seat
     * @param seatWager
     */
    @Override
    public void playerBetChips(GameRoom room, Seat seat, long seatWager, int oper) {
        if (!room.isHaveSeenCards()) {
            if (seat.isHaveSeenCard()) {
                room.setHaveSeenCards(true);
            }
        } else {
            if (!seat.isHaveSeenCard()) {
                room.setHaveSeenCards(false);
            }
        }
        if (!(seat.getPlayer() instanceof Robot)) {
            BetRecvDto betRecvDto = this.playerCacheService.decreaseGold(new BigDecimal(seatWager).negate(), seat.getPlayer().getRoleId());
            if (betRecvDto.getCode() != 0) {
                this.pushService.push(seat.getPlayer().getRoleId(), PLAYER_PLAY, ResultEnum.PLAYER_HAS_NO_MONEY.getCode());
                return;
            }
            seat.getPlayer().setGold(seat.getPlayer().getGold() - seatWager);
            this.playerCacheService.update(seat.getPlayer());
        }
        seat.setChipsRemain(seat.getChipsRemain() - seatWager);
        if (seat.getPlayer() instanceof Robot) {
            seat.getPlayer().setGold(seat.getChipsRemain());
        }
        room.setLastBetChips(seatWager);
        room.setTotalChips(room.getTotalChips() + seatWager);
        seat.setBetChips(seat.getBetChips() + seatWager);
        room.getBetList().add((int) seatWager);
    }

    /**
     * 玩家看牌操作
     *
     * @param room
     * @param seatId
     * @param seat
     */
    @Override
    public List<Integer> seeOwenrCards(GameRoom room, int seatId, Seat seat) {
        seat.setHaveSeenCard(true);
        List<Integer> list = new ArrayList<>();
        this.algService.haveBaseOper(seat, room);
        if (seat.getOperNotify().havaOper()) {
            if (room.getGameRoomTypeId() == D.PRIVATE_FIELD) {
                switch (room.getCustomRule().getMenRule()) {
                    case 0:
                        if (room.getOperTurns() <= 1) {
                            seat.getOperNotify().canSeeCard = false;
                            seat.getOperNotify().canDisCard = false;
                        }
                        break;
                    case 1:
                        if (room.getOperTurns() <= 1) {
                            if (seat.getSeatId() == room.getBankerSeat()) {
                                seat.getOperNotify().canSeeCard = false;
                                seat.getOperNotify().canDisCard = false;
                            }
                        }
                        break;
                }
            }
        }
        seat.getOperNotify().toMap(seat, false);
        if (seat.getSeatId() == room.getPlaySeat() && seat.getOperNotify().canAddChips) {
            for (int j = 0; j < room.getAddChipsBet(); j++) {
                list.add(0);
            }
            for (int i = room.getAddChipsBet() + 1; i <= 5; i++) {
                int gold = room.getBaseScore() * room.getBetMul()[i - 1] * 2;
                list.add(gold);
            }
        }
        return list;
    }

    @Override
    public void solveRoom(int roomId) {
        GameRoom room = RoomManager.instance().getRoom(roomId);
        if (room.getGameRoomTypeId() == D.FREE_FIELD) {
            // 机器人个数
            int robotCount = 0;
            for (Seat seat : room.getSeatMap().values()) {
                if (seat.getPlayer() == null) {
                    continue;
                } else if (seat.getPlayer() instanceof Robot) {
                    robotCount++;
                }
                seat.getPlayer().setLeaveReason(Config.LeaveReason.LEAVE_SOLVE);
                this.quitRoomService.quitRoom(seat.getPlayer(), true, Config.LeaveReason.LEAVE_SOLVE);
            }
            if (robotCount >= 1) {
            }
            room.clearAll();
        } else {
            if (room.getRound() < 1) {
                Map<String, Object> ret = new HashMap<>();
                ret.put("dissolve", true);
                MessageResult messageResult = new MessageResult(SUCCESS_DISOLUT_ROOM_NTC, ret);
                this.pushService.broadcast(messageResult, room);
                // 清除数据
                for (Seat data : room.getSeatMap().values()) {
                    data.getPlayer().setRoomId(0);
                    this.playerCacheService.update(data.getPlayer());
                    data.clear(true);
                }
                room.clearAll();
                this.deletePrivateRoom(room);
            } else {
                this.dissolveRoomSuccess(room);
            }
        }
    }

}
