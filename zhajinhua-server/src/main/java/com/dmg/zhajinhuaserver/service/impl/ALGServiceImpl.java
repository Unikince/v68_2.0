package com.dmg.zhajinhuaserver.service.impl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.dmg.zhajinhuaserver.config.D;
import com.dmg.zhajinhuaserver.config.Config;
import com.dmg.zhajinhuaserver.model.bean.*;
import com.dmg.zhajinhuaserver.service.ALGService;
import org.springframework.stereotype.Service;


/**
 * @author hexf
 * @Date 2018年3月13日
 * @Desc
 */
@Service
public class ALGServiceImpl implements ALGService {

    @Override
    public LinkedList<Poker> createDeck() {
        LinkedList<Poker> list = new LinkedList<>();
        // 添加普通牌
        for (int value = 1; value <= 13; value++) {
            for (int type = 1; type <= 4; type++) {
                Poker poker = new Poker(value, type);
                list.add(poker);
            }
        }
        // 洗牌
        Collections.shuffle(list);
        return list;
    }

    /**
     * 比牌segment1是否大于segment2。
     *
     * @param segment1
     * @param segment2
     * @return true segment1 > segment2, false segment1<= segment2
     */
    @Override
    public boolean isBigThan(Segment segment1, Segment segment2) {
        return false;
    }

    @Override
    public LinkedList<Poker> createTestDeck() {
        LinkedList<Poker> list = new LinkedList<>();
        return list;
    }

    @Override
    public List<Segment> evalTipSegment(List<Poker> hand, Segment lastPlayCard, boolean hasJoker) {
        return null;
    }

    /**
     * 将出的牌从小到大排序
     *
     * @return
     */
    public List<Poker> upperArray(List<Poker> pokers) {
        int n = pokers.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1; j++) {
                if (pokers.get(j).getValue() > pokers.get(j + 1).getValue()) {
                    int value = pokers.get(j).getValue();
                    int type = pokers.get(j).getType();
                    pokers.get(j).setValue(pokers.get(j + 1).getValue());
                    pokers.get(j).setType(pokers.get(j + 1).getType());
                    pokers.get(j + 1).setValue(value);
                    pokers.get(j + 1).setType(type);
                }
            }
        }
        return pokers;
    }

    /**
     * 判断PK的玩家谁输谁赢
     *
     * @param cardList
     * @param becardList
     * @return
     */
    @Override
    public boolean operOneWin(GameRoom room, List<Poker> cardList, List<Poker> becardList) {
        int cardsType = evalPokerType(cardList);
        int beCardsType = evalPokerType(becardList);
        if (cardsType > beCardsType) {
            if (room.getGameRoomTypeId() == D.PRIVATE_FIELD) {
                return !rule235(room, becardList, cardList, beCardsType, cardsType);
            }
            return true;
        } else if (cardsType == beCardsType) {
            //牌型相同
            return operOneWinTheSame(room, cardList, becardList, cardsType);
        } else {
            if (room.getGameRoomTypeId() == D.PRIVATE_FIELD) {
                return rule235(room, cardList, becardList, cardsType, beCardsType);
            }
        }
        return false;
    }

    private boolean rule235(GameRoom room, List<Poker> cardList, List<Poker> becardList, int cardsType, int beCardsType) {
        switch (room.getCustomRule().getRule235()) {
            case 0:
                if (beCardsType == Config.Combination.LEOPARD.getValue()
                        && cardsType == Config.Combination.HIGHCARD.getValue()) {
                    upperArray(cardList);
                    if (cardList.get(0).getValue() == 2 && cardList.get(1).getValue() == 3
                            && cardList.get(2).getValue() == 5) {
                        return true;
                    }
                }
                break;
            case 1:
                if (beCardsType == Config.Combination.LEOPARD.getValue()
                        && cardsType == Config.Combination.HIGHCARD.getValue()) {
                    upperArray(cardList);
                    boolean bool = becardList.get(0).getValue() == 1 && becardList.get(1).getValue() == 1 && becardList.get(2).getValue() == 1;
                    boolean beBool = cardList.get(0).getValue() == 2 && cardList.get(1).getValue() == 3 && cardList.get(2).getValue() == 5;
                    if (bool && beBool) {
                        return true;
                    }
                }
                break;
            case 2:
                break;
            default:
                break;
        }
        return false;

    }

    /**
     * 获得同牌型玩家大小比较
     *
     * @param cardList
     * @param becardList
     * @return
     */
    public boolean operOneWinTheSame(GameRoom room, List<Poker> cardList, List<Poker> becardList, int cardsType) {
        upperArray(cardList);
        upperArray(becardList);
        int[] pokers = new int[3];
        int[] bePokers = new int[3];
        for (int i = 0; i < cardList.size(); i++) {
            pokers[i] = cardList.get(i).getValue();
        }
        for (int i = 0; i < becardList.size(); i++) {
            bePokers[i] = becardList.get(i).getValue();
        }
        //同花或散牌处理
        if (cardsType == Config.Combination.FLUSH.getValue()
                || cardsType == Config.Combination.HIGHCARD.getValue()) {
            // 同为同花或散牌
            if (pokers[0] == 1 || bePokers[0] == 1) {
                if (pokers[0] == bePokers[0]) {
                    if (pokers[2] > bePokers[2]) {
                        return true;
                    } else if (pokers[2] == bePokers[2]) {
                        if (pokers[1] > bePokers[1]) {
                            return true;
                        }
                    }
                } else {
                    if (pokers[0] == 1) {
                        return true;
                    }
                }
                return false;
            } else {
                if (pokers[2] > bePokers[2]) {
                    return true;
                } else if (pokers[2] == bePokers[2]) {
                    if (pokers[1] > bePokers[1]) {
                        return true;
                    } else if (pokers[1] == bePokers[1]) {
                        if (pokers[0] > bePokers[0]) {
                            return true;
                        }
                    }
                }
                return false;
            }
        }
        //对子处理
        if (cardsType == Config.Combination.PAIR.getValue()) {
            if (pokers[1] == 1 || bePokers[1] == 1) {
                if (pokers[1] == bePokers[1]) {
                    if (pokers[2] > bePokers[2]) {
                        return true;
                    }
                    return false;
                } else {
                    if (pokers[1] == 1) {
                        return true;
                    }
                    return false;
                }
            } else {
                if (pokers[1] > bePokers[1]) {
                    return true;
                }
            }
        }

        if (cardsType == Config.Combination.LEOPARD.getValue()) {// 豹子处理
            if (pokers[0] == 1) {
                return true;
            } else if (pokers[0] - bePokers[0] > 0) {
                if (bePokers[0] != 1) {
                    return true;
                }
            }
            return false;
        }

        if (cardsType == Config.Combination.PROGRESSION.getValue()
                || cardsType == Config.Combination.STRAIGHTFLUSH.getValue()) {
            //顺子或者同花顺
            if (pokers[0] == 1 || bePokers[0] == 1) {
                if (pokers[0] == bePokers[0]) {
                    if (pokers[2] > bePokers[2]) {
                        return true;
                    }
                } else {
                    if (pokers[0] == 1) {
                        if (room.getGameRoomTypeId() == D.PRIVATE_FIELD) {
                            if (!room.getCustomRule().isRuleA23()) {
                                return true;
                            }
                        }
                        return true;
                    } else {
                        if (room.getGameRoomTypeId() == D.PRIVATE_FIELD) {
                            if (!room.getCustomRule().isRuleA23()) {
                                return false;
                            }
                        }
                        return false;
                    }
                }
                return false;
            } else {
                if (pokers[2] > bePokers[2]) {
                    return true;
                }
                return false;
            }

        }
        return false;
    }

    @Override
    public int evalPokerType(List<Poker> cardList) {
        if (cardList.size() != 3) {
            return Config.Combination.NONE.getValue();
        }
        upperArray(cardList);
        int[] pokers = new int[3];
        for (int i = 0; i < cardList.size(); i++) {
            pokers[i] = cardList.get(i).getValue();
        }
        if (cardList.get(0).getType() == cardList.get(1).getType()
                && cardList.get(0).getType() == cardList.get(2).getType()) {
            if (pokers[1] - pokers[0] == 1 && pokers[2] - pokers[1] == 1) {
                return Config.Combination.STRAIGHTFLUSH.getValue();
            } else {
                if (pokers[0] == 1 && pokers[1] == 12 && pokers[2] == 13) {
                    return Config.Combination.STRAIGHTFLUSH.getValue();
                }
                return Config.Combination.FLUSH.getValue();
            }
        } else {
            if (pokers[0] == pokers[1] && pokers[0] == pokers[2]) {
                return Config.Combination.LEOPARD.getValue();
            } else {
                if (pokers[2] - pokers[1] == 1 && pokers[1] - pokers[0] == 1) {
                    return Config.Combination.PROGRESSION.getValue();
                } else if (pokers[0] == pokers[1] || pokers[2] == pokers[1]) {
                    return Config.Combination.PAIR.getValue();
                } else {
                    if (pokers[0] == 1 && pokers[1] == 12 && pokers[2] == 13) {
                        return Config.Combination.PROGRESSION.getValue();
                    }
                    return Config.Combination.HIGHCARD.getValue();
                }
            }
        }
    }

    /**
     * 能否进行看牌操作
     *
     * @param seat
     * @return
     */
    public boolean canSeeCard(Seat seat, GameRoom room) {
        seat.getOperNotify().seatId = seat.getSeatId();
        // 必闷圈数内不允许看牌
        if (room.getOperTurns() <= 1)
            return false;
        // 已经看过了不能看牌
        if (seat.isHaveSeenCard())
            return false;
        return true;
    }

    /**
     * 能否弃牌
     *
     * @param seat
     * @return
     */
    private boolean canDisCard(Seat seat) {
//		logger.warn("======================================检查能否弃牌=======================================");
        // seat.baseOperNotify.beseatIdList.add(seat.seatID);
        seat.getOperNotify().seatId = seat.getSeatId();
        if (seat.isPass()) {
            return false;
        }
        return true;
    }

    /**
     * 能否比牌,下注,跟注
     *
     * @param seat
     * @return
     */
    private boolean canBet(Seat seat, GameRoom room) {
//		logger.warn("======================================检查能否跟注======================================= canOper:"+deskData.canOper+"===actionState:"+seat.actionState+"===operCount:"+deskData.operCount+"===canAddChips:"+deskData.ruleConfig.canAddChips);
        if (room.getGameRoomTypeId() == D.PRIVATE_FIELD) {
            return true;
        }
        seat.getOperNotify().seatId = seat.getSeatId();
        double seatmny = seat.getChipsRemain();
        seat.getOperNotify().seatId = seat.getSeatId();
        boolean isLogic = false;
        if (seat.isHaveSeenCard()) {
            if (room.isHaveSeenCards() && seatmny >= room.getLastBetChips()) {
                isLogic = true;
            }
            if (!room.isHaveSeenCards() && seatmny >= room.getLastBetChips() * 2) {
                isLogic = true;
            }
        } else {
            if (room.isHaveSeenCards() && seatmny >= room.getLastBetChips() / 2) {
                isLogic = true;
            }
            if (!room.isHaveSeenCards() && seatmny >= room.getLastBetChips()) {
                isLogic = true;
            }
        }
        return isLogic;
    }

    public boolean canCompare(Seat seat, GameRoom room) {
        seat.getOperNotify().seatId = seat.getSeatId();
        if (room.getOperTurns() < 2) {
            // 轮次小于2轮无法比牌
            return false;
        }
        boolean isLogic = false;
        // 剩余筹码
        double seatmny = seat.getChipsRemain();
        seat.getOperNotify().seatId = seat.getSeatId();
        for (Seat beseat : room.getSeatMap().values()) {
            if (!beseat.isPlayed() || !beseat.isReady() || beseat.getSeatId() == seat.getSeatId()) {
                continue;
            }
            if (beseat.isPass()) {
                continue;
            }
            if (room.getGameRoomTypeId() == D.PRIVATE_FIELD) {
                if (!beseat.isPlayed()) {
                    continue;
                }
            }
            // 在游戏中的椅子才能加入比牌
            seat.getOperNotify().beSeatIdList.add(beseat.getSeatId());
            return true;
        }
        if (room.isHaveSeenCards()) {
            if (seat.isHaveSeenCard() && seatmny >= room.getLastBetChips()) {
                isLogic = true;
            }
            if (!seat.isHaveSeenCard() && seatmny >= room.getLastBetChips() / 2) {
                isLogic = true;
            }
        } else {
            if (seat.isHaveSeenCard() && seatmny >= room.getLastBetChips() * 2) {
                isLogic = true;
            }
            if (!seat.isHaveSeenCard() && seatmny >= room.getLastBetChips()) {
                isLogic = true;
            }
        }
        return isLogic;
    }

    /**
     * 能否加注
     *
     * @param seat
     * @return
     */
    private boolean canAddChips(Seat seat, GameRoom room) {
        if (room.getAddChipsBet() >= 5 || room.isHaveRush()) {
            return false;
        }
        if (room.getGameRoomTypeId() == D.PRIVATE_FIELD) {
            return true;
        }
        double seatmny = seat.getChipsRemain();
        seat.getOperNotify().seatId = seat.getSeatId();
        int bet = 0;
        if (seat.isHaveSeenCard()) {
            bet = 2;
        } else {
            bet = 1;
        }
        long gold = room.getBaseScore() * room.getBetMul()[room.getAddChipsBet()] * bet;
        if (seatmny < gold) {
            return false;
        }
        return true;
    }

    /**
     * 可以进行的操作
     *
     * @param seat
     */
    @Override
    public void haveBaseOper(Seat seat, GameRoom room) {
        if (seat.getOperNotify() == null) {
            seat.setOperNotify(new OperNotify());
        } else {
            seat.getOperNotify().clear();
        }
        //看牌
        if (canSeeCard(seat, room)) {
            seat.getOperNotify().canSeeCard = true;
        }
        //比牌
        if (canCompare(seat, room)) {
            seat.getOperNotify().canCompare = true;
            for (Seat beseat : room.getSeatMap().values()) {
                if (room.getGameRoomTypeId() == D.PRIVATE_FIELD) {
                    if (!beseat.isPlayed()) {
                        continue;
                    }
                }
                if (!beseat.isPlayed() || !beseat.isReady() || beseat.getSeatId() == seat.getSeatId()) {
                    continue;
                }
                if (beseat.isPass()) {
                    continue;
                }
                //在游戏中的椅子才能加入比牌
                seat.getOperNotify().beSeatIdList.add(beseat.getSeatId());
            }
        }
        //跟注
        if (canBet(seat, room)) {
            seat.getOperNotify().canFollowChips = true;
        }
        //弃牌
        if (canDisCard(seat)) {
            seat.getOperNotify().canDisCard = true;
        }
        //加注
        if (canAddChips(seat, room)) {
            seat.getOperNotify().canAddChips = true;
        }
    }


}









