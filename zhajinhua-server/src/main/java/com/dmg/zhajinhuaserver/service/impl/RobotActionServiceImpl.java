package com.dmg.zhajinhuaserver.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.dmg.zhajinhuaserver.config.Config;
import com.dmg.zhajinhuaserver.model.bean.GameRoom;
import com.dmg.zhajinhuaserver.model.bean.Seat;
import com.dmg.zhajinhuaserver.model.dto.RobotActionDTO;
import com.dmg.zhajinhuaserver.model.dto.RobotProbabilityDTO;
import com.dmg.zhajinhuaserver.model.dto.RobotSeeDTO;
import com.dmg.zhajinhuaserver.service.RobotActionService;
import com.dmg.zhajinhuaserver.service.cache.RobotActionCacheService;
import com.dmg.zhajinhuaserver.service.cache.RobotProbabilityCacheService;
import com.dmg.zhajinhuaserver.service.cache.RobotSeeCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 15:09 2019/9/30
 */
@Slf4j
@Service
public class RobotActionServiceImpl implements RobotActionService {

    @Autowired
    private RobotSeeCacheService robotSeeCacheService;

    @Autowired
    private RobotProbabilityCacheService robotProbabilityCacheService;

    @Autowired
    private RobotActionCacheService robotActionCacheService;


    @Override
    public int getRobotAction(GameRoom room, Seat seat) {
        //是否看牌
        Double maxProbability;
        Double minProbability = new Double(1);
        int random;
        RobotProbabilityDTO robotProbabilityDTO = null;
        Collections.sort(seat.getHand());
        log.info("房间：{},player:{}{},是否看牌：{},当前轮数：{}", room.getRoomId(), seat.getPlayer().getRoleId(), seat.getPlayer().getNickname(), seat.isHaveSeenCard(), room.getOperTurns());
        if (room.getOperTurns() >= room.getCountTurns()) {
            // 最大轮自动比牌
            return Config.PokerOperState.COMPARECARD;
        }
        if (seat.isReady() && !seat.isHaveSeenCard()) {
            //闷牌操作
            RobotActionDTO robotActionDTO = robotActionCacheService.getRobotActionByIsSee(false);
            if (robotActionDTO.getFollowUpType() == 0) {
                maxProbability = new Double(robotActionDTO.getProbabilityFollowUp());
            } else {
                if (robotProbabilityDTO == null) {
                    robotProbabilityDTO = robotProbabilityCacheService.getRobotProbability();
                }
                //执行跟注的概率=当前轮数X概率*20+看牌加注人数X概率+看牌跟注人数X概率+比牌赢人数X概率
                maxProbability = new Double(room.getOperTurns() * robotProbabilityDTO.getProbabilityRound() * 20
                        + room.getSeeAnnotation() * robotProbabilityDTO.getProbabilitySeeAnnotation()
                        + room.getSeeFollowUp() * robotProbabilityDTO.getProbabilitySeeFollowUp()
                        + room.getComparisonWin() * robotProbabilityDTO.getProbabilityComparisonWin());
            }
            log.info("房间：{},player:{},跟注概率：{}", room.getRoomId(), seat.getPlayer().getRoleId(), maxProbability);
            random = RandomUtil.randomInt(10000) + 1;
            if (random < (maxProbability + minProbability) && random >= minProbability) {
                //概率命中跟注
                return Config.PokerOperState.FOLLOWCHIPS;
            }
            //若已经达到注额上限，则随机到加注操作时，改为执行跟注操作
            if (room.getAddChipsBet() >= room.getBetMul()[room.getBetMul().length - 1]) {
                return Config.PokerOperState.FOLLOWCHIPS;
            }
            //执行加注
            return Config.PokerOperState.ADDCHIPS;
        } else if (seat.isReady()) {
            //看牌后操作
            RobotActionDTO robotActionDTO = robotActionCacheService.getRobotActionByCard(seat.getHandCardsType(), seat.getHand().get(0).getValue());
            if (robotProbabilityDTO == null) {
                robotProbabilityDTO = robotProbabilityCacheService.getRobotProbability();
            }
            random = RandomUtil.randomInt(10000) + 1;
            if (robotActionDTO.getFollowUpType() == 0) {
                maxProbability = new Double(robotActionDTO.getProbabilityFollowUp());
            } else {
                //对子跟注
                if (Config.Combination.PAIR.getValue() == seat.getHandCardsType()) {
                    //跟注：1-比牌概率
                    //比牌概率=(当前轮数X概率+看牌加注人数X概率+看牌跟注人数X概率+比牌赢人数X概率)/2
                    maxProbability = 10000 - new Double(room.getOperTurns() * robotProbabilityDTO.getProbabilityRound()
                            + room.getSeeAnnotation() * robotProbabilityDTO.getProbabilitySeeAnnotation()
                            + room.getSeeFollowUp() * robotProbabilityDTO.getProbabilitySeeFollowUp()
                            + room.getComparisonWin() * robotProbabilityDTO.getProbabilityComparisonWin()) * 0.5;
                } else {
                    //跟注概率=（当前轮数X概率+看牌加注人数X概率+看牌跟注人数X概率+比牌赢人数X概率）X0.6
                    maxProbability = new Double(room.getOperTurns() * robotProbabilityDTO.getProbabilityRound()
                            + room.getSeeAnnotation() * robotProbabilityDTO.getProbabilitySeeAnnotation()
                            + room.getSeeFollowUp() * robotProbabilityDTO.getProbabilitySeeFollowUp()
                            + room.getComparisonWin() * robotProbabilityDTO.getProbabilityComparisonWin()) * 0.6;
                }
            }
            if (random < (maxProbability + minProbability) && random >= minProbability) {
                //命中跟注
                return Config.PokerOperState.FOLLOWCHIPS;
            }
            minProbability += maxProbability;
            if (robotActionDTO.getComparisonType() == 0) {
                maxProbability = new Double(robotActionDTO.getProbabilityComparison());
            } else {
                //对子比牌
                if (Config.Combination.PAIR.getValue() == seat.getHandCardsType()) {
                    //比牌概率=(当前轮数X概率+看牌加注人数X概率+看牌跟注人数X概率+比牌赢人数X概率)/2
                    maxProbability = new Double(room.getOperTurns() * robotProbabilityDTO.getProbabilityRound()
                            + room.getSeeAnnotation() * robotProbabilityDTO.getProbabilitySeeAnnotation()
                            + room.getSeeFollowUp() * robotProbabilityDTO.getProbabilitySeeFollowUp()
                            + room.getComparisonWin() * robotProbabilityDTO.getProbabilityComparisonWin()) * 0.5;
                } else {
                    //顺子
                    //比牌概率=（当前轮数X概率+看牌加注人数X概率+看牌跟注人数X概率+比牌赢人数X概率）X0.4
                    maxProbability = new Double(room.getOperTurns() * robotProbabilityDTO.getProbabilityRound()
                            + room.getSeeAnnotation() * robotProbabilityDTO.getProbabilitySeeAnnotation()
                            + room.getSeeFollowUp() * robotProbabilityDTO.getProbabilitySeeFollowUp()
                            + room.getComparisonWin() * robotProbabilityDTO.getProbabilityComparisonWin()) * 0.4;
                }
            }
            if (random < (maxProbability + minProbability) && random >= minProbability) {
                //命中比牌
                return Config.PokerOperState.COMPARECARD;
            }
            minProbability += maxProbability;
            if (robotActionDTO.getAnnotationType() == 0) {
                maxProbability = new Double(robotActionDTO.getProbabilityAnnotation());
            } else {
                //加注概率=（1-当前轮数X概率+看牌加注人数X概率+看牌跟注人数X概率+比牌赢人数X概率）X1
                maxProbability = 10000 - new Double(room.getOperTurns() * robotProbabilityDTO.getProbabilityRound()
                        + room.getSeeAnnotation() * robotProbabilityDTO.getProbabilitySeeAnnotation()
                        + room.getSeeFollowUp() * robotProbabilityDTO.getProbabilitySeeFollowUp()
                        + room.getComparisonWin() * robotProbabilityDTO.getProbabilityComparisonWin());
            }
            if (random < (maxProbability + minProbability) && random >= minProbability) {
                //若已经达到注额上限，则随机到加注操作时，改为执行跟注操作
                if (room.getAddChipsBet() >= room.getBetMul()[room.getBetMul().length - 1]) {
                    return Config.PokerOperState.FOLLOWCHIPS;
                }
                //命中加注
                return Config.PokerOperState.ADDCHIPS;
            }
            minProbability += maxProbability;
            if (robotActionDTO.getDiscardType() == 0) {
                maxProbability = new Double(robotActionDTO.getProbabilityDiscard());
            } else {
                //弃牌概率=（1-当前轮数X概率+看牌加注人数X概率+看牌跟注人数X概率+比牌赢人数X概率）X0
                maxProbability = (10000 - new Double(room.getOperTurns() * robotProbabilityDTO.getProbabilityRound()
                        + room.getSeeAnnotation() * robotProbabilityDTO.getProbabilitySeeAnnotation()
                        + room.getSeeFollowUp() * robotProbabilityDTO.getProbabilitySeeFollowUp()
                        + room.getComparisonWin() * robotProbabilityDTO.getProbabilityComparisonWin())) * 0;
            }
            if (random < (maxProbability + minProbability) && random >= minProbability) {
                //命中弃牌 单牌10以上 在只剩2家的情况时，机器人必定会比牌 (A = 1)
                if (seat.getHandCardsType() == Config.Combination.HIGHCARD.getValue() && (seat.getHand().get(0).getValue() > 10 || seat.getHand().get(2).getValue() == 1)) {
                    //判断是否只剩两个人
                    Integer count = 0;
                    for (Map.Entry<Integer, Seat> m : room.getSeatMap().entrySet()) {
                        Seat i = m.getValue();
                        if (i.isReady() && !i.isPass() && !i.isLostPk()) {
                            count += 1;
                        }
                    }
                    if (count == 2) {
                        //命中比牌
                        return Config.PokerOperState.COMPARECARD;
                    }
                }
                return Config.PokerOperState.DISCARD;
            }
        }
        return Config.PokerOperState.DISCARD;
    }

    @Override
    public Boolean getRobotActionIsSee(GameRoom room, Seat seat) {
        //是否看牌
        Double maxProbability;
        Double minProbability = new Double(1);
        int random;
        RobotProbabilityDTO robotProbabilityDTO = null;
        Collections.sort(seat.getHand());
        log.info("房间：{},player:{}{},当前轮数：{}", room.getRoomId(), seat.getPlayer().getRoleId(), seat.getPlayer().getNickname(), room.getOperTurns());
        if (room.getOperTurns() >= room.getCountTurns()) {
            // 最大轮自动比牌
            return true;
        }
        if (seat.isReady() && !seat.isHaveSeenCard()) {
            //判断当前轮数计算是否看牌
            RobotSeeDTO robotSeeDTO = robotSeeCacheService.getRobotSeeByRound(room.getOperTurns());
            if (robotSeeDTO.getSeeType() == 0) {
                maxProbability = new Double(robotSeeDTO.getProbabilitySee());
            } else {
                robotProbabilityDTO = robotProbabilityCacheService.getRobotProbability();
                //看牌的概率=当前轮数X概率*4+看牌加注人数X概率+看牌跟注人数X概率+比牌赢人数X概率
                maxProbability = new Double(room.getOperTurns() * robotProbabilityDTO.getProbabilityRound() * 4
                        + room.getSeeAnnotation() * robotProbabilityDTO.getProbabilitySeeAnnotation()
                        + room.getSeeFollowUp() * robotProbabilityDTO.getProbabilitySeeFollowUp()
                        + room.getComparisonWin() * robotProbabilityDTO.getProbabilityComparisonWin());
            }
            log.info("房间：{},player:{},看牌概率：{}", room.getRoomId(), seat.getPlayer().getRoleId(), maxProbability);
            random = RandomUtil.randomInt(10000) + 1;
            if (random < (maxProbability + minProbability) && random >= minProbability) {
                //概率命中看牌
                return true;
            }
        }
        return false;
    }
}
