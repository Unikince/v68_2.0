package com.dmg.doudizhuserver.business.ai;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dmg.doudizhuserver.business.model.Card;
import com.dmg.doudizhuserver.business.model.CardsType;
import com.dmg.doudizhuserver.business.model.Room;
import com.dmg.doudizhuserver.business.model.Seat;
import com.dmg.doudizhuserver.business.util.CardsTypeFinderType;
import com.dmg.doudizhuserver.business.util.CardsTypeFinderType.GroupCard;
import com.dmg.doudizhuserver.business.util.CardsTypeGetter;
import com.dmg.doudizhuserver.business.util.Combine;

public class RobotAI {
    private static Logger logger = LoggerFactory.getLogger(RobotAI.class);

    public static List<Card> getPlayCard(Seat seat) {
        boolean myFirst = false;// 是否我的先手
        boolean nextAlly = false;// 下家是否我的盟友
        boolean otherAlly = false;// 下下家是否我的盟友
        Collection<Card> myHandCards = null;// 我的手牌
        Collection<Card> nextHandCards = null;// 下家的手牌
        Collection<Card> otherHandCards = null;// 下下家手牌
        Collection<Card> playCards = null;// 当前出的牌(我的先手的话当前出的牌为空内容)
        boolean prePlay = false;// 是否是我的直接上家出牌(当前我不是先手)

        Room room = seat.room;
        Seat preSeat = room.preSeat(seat);
        Seat nextSeat = room.nextSeat(seat);

        myHandCards = seat.handCards.getSortCards();
        nextHandCards = nextSeat.handCards.getSortCards();
        otherHandCards = preSeat.handCards.getSortCards();

        if (seat == room.prePlaySeat) {
            myFirst = true;
            playCards = new ArrayList<>();
        } else {
            playCards = room.prePlayCards.sortedCards;
        }
        if (room.landlord != seat) {
            if (room.landlord == preSeat) {
                nextAlly = true;
            } else {
                otherAlly = true;
            }
        }
        if (room.prePlaySeat == preSeat) {
            prePlay = true;
        }
        List<Card> list = RobotAI.robotThinking(myFirst, nextAlly, otherAlly, myHandCards, nextHandCards, otherHandCards, playCards, prePlay);
        if (list.isEmpty() && myFirst) {
            System.out.println("================================>>>>>>>>>>>>>>>>>>>>>>>>>>>>>出错了,兼容打牌");
            List<Card> jianrong = new ArrayList<>();
            jianrong.addAll(myHandCards);
            if (!jianrong.isEmpty()) {
                Collections.sort(jianrong, new Comparator<Card>() {

                    @Override
                    public int compare(Card o1, Card o2) {
                        return o1.num - o2.num;
                    }
                });
                list.add(jianrong.get(0));
            } else {
                System.out.println("================================>>>>>>>>>>>>>>>>>>>>>>>>>>>>>出错了,兼容打牌出错");
            }
        }
        return list;
    }

    /**
     *
     * @param myFirst 是否我的先手
     * @param nextAlly 下家是否我的盟友
     * @param otherAlly 下下家是否我的盟友
     * @param myHandCards 我的手牌
     * @param nextHandCards 下家的手牌
     * @param otherHandCards 下下家手牌
     * @param playCards 当前出的牌(我的先手的话当前出的牌为空内容)
     * @param prePlay 是否是我的直接上家出牌(当前我不是先手)
     * @return
     */
    public static List<Card> robotThinking(boolean myFirst, boolean nextAlly, boolean otherAlly, Collection<Card> myHandCards, Collection<Card> nextHandCards, Collection<Card> otherHandCards, Collection<Card> playCards, boolean prePlay) {
        logger.info("当前我的手牌:{},===>prePlay:{}", myHandCards, prePlay);
        List<Card> list = new ArrayList<>();
        List<Card> myHands = new ArrayList<>();
        List<Card> nextHands = new ArrayList<>();
        List<Card> otherHands = new ArrayList<>();
        List<Card> plays = new ArrayList<>();
        myHands.addAll(myHandCards);
        nextHands.addAll(nextHandCards);
        otherHands.addAll(otherHandCards);
        plays.addAll(playCards);
        List<List<Card>> zhaList = checkZha(myHands, nextAlly, nextHands);
        if (!zhaList.isEmpty()) {
            Collections.sort(zhaList, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return findMaxNum(o1) - findMaxNum(o2);
                }
            });
            if (myFirst) {
                list.addAll(zhaList.get(0));
            } else {
                CardsType type = analysis(plays);
                if (type == CardsType.WANG_ZHA) {
                    return list;
                }
                if (type == CardsType.ZHA_DAN) {
                    for (List<Card> list1 : zhaList) {
                        if (list1.get(0).num > plays.get(0).num) {
                            list.addAll(list1);
                            return list;
                        }
                    }
                } else {
                    list.addAll(zhaList.get(0));
                }
            }
            return list;
        }
        zhaList = checkZha1(myHands, nextAlly, otherAlly, nextHands);
        if (!zhaList.isEmpty()) {
            list.addAll(zhaList.get(0));
            return list;
        }
        if (myFirst) {
            boolean baodan = false;
            if (nextHandCards.size() == 1) {
                baodan = true;
            }
            boolean baoshuang = false;
            if (nextHandCards.size() == 2) {
                baoshuang = true;
            }
            boolean xiaowang = false;
            boolean dawang = false;
            Card xw = null;
            Card dw = null;
            if (baodan && !nextAlly) {
                Entity entity = optimumSolution(myHands);
                if (entity != null) {
                    List<List<Card>> solution = entity.solution;
                    if (!solution.isEmpty()) {
                        List<Card> tmp = solution.get(0);
                        list.addAll(tmp);
                        myHandCards.removeAll(list);
                        logger.info("====================我的先手 出牌:{},剩余:{}", list, myHandCards);
                        return list;
                    }
                }
                // 判断是否有小王或者大王
                for (Card card : myHands) {
                    if (card.num == Card.XW_NUM) {
                        xiaowang = true;
                        xw = card;
                    }
                    if (card.num == Card.DW_NUM) {
                        dawang = true;
                        dw = card;
                    }
                }
                if (xiaowang && !dawang) {
                    list.add(xw);
                    return list;
                }
                if (dawang && !xiaowang) {
                    list.add(dw);
                    return list;
                }
                // 出最大的
                List<List<Card>> out = CardsTypeFinderType.findTypeCards(myHands, CardsType.DAN);
                List<Card> zero = choose1(out);
                list.addAll(zero);
                myHandCards.removeAll(list);
                logger.info("====================我的先手 出单张最大的牌:{},剩余:{}", list, myHandCards);
                return list;
            }
            if (baodan && nextAlly) {
                // 下家报单并且是盟友
                Collections.sort(myHands, new Comparator<Card>() {

                    @Override
                    public int compare(Card o1, Card o2) {
                        return o1.num - o2.num;
                    }
                });
                if (!myHands.isEmpty()) {
                    if (myHands.get(0).num < nextHands.get(0).num) {
                        // 直接打最小的(有点作弊哦)
                        list.add(myHands.get(0));
                        return list;
                    }
                }
            }
            Entity entity = optimumSolution(myHands);
            if (entity != null) {
                // 判断自己的单张能否被收回,如果可以收回先打单张
                if (canTakeOver(myHands, nextHands, otherHands, nextAlly, otherAlly)) {
                    // 打单张能够被自己或者盟友收回的情况打单张(有点作弊)
                    List<GroupCard> groups = CardsTypeFinderType.groupByCardNum(entity.left);
                    List<GroupCard> removes = new ArrayList<>();
                    for (GroupCard group : groups) {
                        if (group.getCount() > 1 || group.getCardNum() > 10) {
                            removes.add(group);
                        }
                    }
                    groups.removeAll(removes);
                    if (!groups.isEmpty()) {
                        // 小于10点的能被收回优先打单张
                        Collections.sort(groups, new Comparator<GroupCard>() {
                            @Override
                            public int compare(GroupCard o1, GroupCard o2) {
                                return o1.getCardNum() - o2.getCardNum();
                            }
                        });
                        list.add(groups.get(0).getList().get(0));
                        return list;
                    }
                }
                List<List<Card>> solution = entity.solution;
                if (!solution.isEmpty()) {
                    boolean check = false;

                    int sanDaiCountAndDaShi = 0;
                    int danCount = 0;
                    Collections.sort(entity.left, new Comparator<Card>() {

                        @Override
                        public int compare(Card o1, Card o2) {
                            return o1.num - o2.num;
                        }
                    });
                    List<GroupCard> groups = CardsTypeFinderType.groupByCardNum(entity.left);
                    for (GroupCard group : groups) {
                        if (group.getCount() == 1 && group.getCardNum() <= 12) {
                            danCount += 1;
                        }
                    }
                    for (List<Card> st : solution) {
                        CardsType type = analysis(st);
                        if (type == CardsType.SHUN || type == CardsType.LIAN_DUI || type == CardsType.FEI_JI || type == CardsType.FEI_JI_DAI_DAN || type == CardsType.FEI_JI_DAI_DUI || type == CardsType.SI_DAI_DAN || type == CardsType.SAN_DAI_DUI) {
                            check = true;
                            break;
                        }
                        int n = findMaxNum(st);
                        if ((type == CardsType.SAN_DAI_DAN || type == CardsType.SAN_DAI_DUI || type == CardsType.SAN_ZHANG) && n <= 12) {
                            sanDaiCountAndDaShi += 1;
                        }
                    }
                    if (!check) {
                        danCount = danCount - sanDaiCountAndDaShi;
                        if (danCount > 0) {
                            // 打单
                            Collections.sort(groups, new Comparator<GroupCard>() {

                                @Override
                                public int compare(GroupCard o1, GroupCard o2) {
                                    int n = o1.getCount() - o2.getCount();
                                    if (n == 0) {
                                        n = o1.getCardNum() - o2.getCardNum();
                                    }
                                    return n;
                                }
                            });
                            list.add(groups.get(0).getList().get(0));
                            return list;
                        }
                    }
                    List<Card> tmp = solution.get(0);
                    if (baoshuang) {
                        // 下家报双,如果自己只剩2张牌且是对子则打对子，否则打单张
                        CardsType type = analysis(tmp);
                        if (type == CardsType.DUI && myHands.size() == 2 && myHands.get(0).num == myHands.get(1).num) {
                            list.addAll(tmp);
                            return list;
                        } else {
                            if (type == CardsType.DUI) {
                                list.add(tmp.get(0));
                                return list;
                            }
                        }
                    }
                    int num = findMaxNum(tmp);
                    boolean display = true;
                    if (num == Card.ER_NUM) {
                        int erCount = 0;
                        for (Card card : tmp) {
                            if (card.num == Card.ER_NUM) {
                                erCount++;
                            }
                        }
                        List<List<Card>> out1 = CardsTypeFinderType.findTypeCards(myHands, CardsType.DAN);
                        if (erCount == 3 && out1.size() > 2) {
                            display = false;
                        }
                        if (erCount == 2 && out1.size() > 1) {
                            display = false;
                        }
                        if (display) {
                            List<List<Card>> out2 = CardsTypeFinderType.findTypeCards(myHands, CardsType.DUI);
                            if (erCount == 3 && out2.size() > 2) {
                                display = false;
                            }
                            if (erCount == 2 && out2.size() > 1) {
                                display = false;
                            }
                        }
                    }
                    // 如果当前出的是2的三带,则判断是否还有单张或者对子未出,如果有,则不出
                    if (display) {
                        list.addAll(tmp);
                        return list;
                    } else {
                        solution.remove(0);
                        if (!solution.isEmpty()) {
                            tmp = solution.get(0);
                            list.addAll(tmp);
                            return list;
                        }

                    }
                }
            }
            // 出最小的
            List<List<Card>> out = CardsTypeFinderType.findTypeCards(myHands, CardsType.DAN);
            if (!out.isEmpty()) {
                List<Card> zero = choose(out);
                list.addAll(zero);
                return list;
            }
            // 判断是否有小王或者大王
            for (Card card : myHands) {
                if (card.num == Card.XW_NUM) {
                    xiaowang = true;
                    xw = card;
                }
                if (card.num == Card.DW_NUM) {
                    dawang = true;
                    dw = card;
                }
            }
            if (xiaowang && !dawang) {
                list.add(xw);
                return list;
            }
            if (dawang && !xiaowang) {
                list.add(dw);
                return list;
            }
            myHandCards.removeAll(list);
            logger.info("====================我的先手 出最小单张牌:{},剩余:{}", list, myHandCards);
            return list;
        } else {
            // 跟牌
            CardsType cardsType = analysis(plays);
            int yaPai = checkYa(nextAlly, otherAlly, myHands, nextHands, otherHands, plays, prePlay, cardsType);
            List<Card> out = yapai(nextAlly, otherAlly, myHands, nextHands, otherHands, plays, prePlay, yaPai);
            list.addAll(out);
            return list;
        }
    }

    private static List<List<Card>> checkZha1(List<Card> myHands, boolean nextAlly, boolean otherAlly, List<Card> nextHands) {
        List<List<Card>> result = new ArrayList<>();
        boolean baodan = false;
        if (nextHands.size() == 1) {
            baodan = true;
        }
        if (baodan && nextAlly) {
            // 下家是盟友且报单
            List<List<Card>> out0 = CardsTypeFinderType.findTypeCards(myHands, CardsType.WANG_ZHA);
            List<List<Card>> out1 = CardsTypeFinderType.findTypeCards(myHands, CardsType.ZHA_DAN);
            List<Card> tmp = new ArrayList<>();
            tmp.addAll(myHands);
            if (!out0.isEmpty()) {
                for (List<Card> list : out0) {
                    tmp.removeAll(list);
                }
            }
            if (!out1.isEmpty()) {
                for (List<Card> list : out1) {
                    tmp.removeAll(list);
                }
            }
            if (!tmp.isEmpty()) {
                Collections.sort(tmp, new Comparator<Card>() {
                    @Override
                    public int compare(Card o1, Card o2) {
                        return o1.num - o2.num;
                    }
                });
                if (tmp.get(0).num < nextHands.get(0).num) {
                    result.addAll(out0);
                    result.addAll(out1);
                }
            }

        }
        if (baodan && !nextAlly && otherAlly) {
            // 下家非盟友且报单,如果炸了 自己无单张能够一次性出完 则炸
            List<List<Card>> out0 = CardsTypeFinderType.findTypeCards(myHands, CardsType.WANG_ZHA);
            List<List<Card>> out1 = CardsTypeFinderType.findTypeCards(myHands, CardsType.ZHA_DAN);
            if (!out0.isEmpty()) {
                List<Card> tmp = new ArrayList<>();
                tmp.addAll(myHands);
                tmp.removeAll(out0.get(0));
                Entity entity = RobotAI.optimumSolution(tmp);
                if (entity != null) {
                    List<Card> cards = entity.left;
                    List<GroupCard> groups = CardsTypeFinderType.groupByCardNum(cards);
                    int c = 0;
                    for (GroupCard group : groups) {
                        if (group.getCount() == 1 && group.getCardNum() < Card.ER_NUM) {
                            c += 1;
                        }
                    }
                    if (c >= 2) {
                        // 单张出不完,不能炸
                        return result;
                    } else {
                        result.add(out0.get(0));
                        return result;
                    }
                } else {
                    List<List<Card>> out = CardsTypeFinderType.findTypeCards(myHands, CardsType.DAN);
                    if (!out.isEmpty()) {
                        int c = 0;
                        for (List<Card> list : out) {
                            for (Card card : list) {
                                if (card.num < Card.ER_NUM) {
                                    c += 1;
                                }
                            }
                        }
                        if (c >= 2) {
                            // 单张出不完,不能炸
                            return result;
                        } else {
                            result.add(out0.get(0));
                            return result;
                        }
                    }
                }
            } else {
                if (!out1.isEmpty()) {
                    // 开始尝试依次移除
                    for (List<Card> list : out1) {
                        List<Card> tmp = new ArrayList<>();
                        tmp.addAll(myHands);
                        tmp.removeAll(list);
                        Entity entity = RobotAI.optimumSolution(tmp);
                        if (entity != null) {
                            List<Card> cards = entity.left;
                            List<GroupCard> groups = CardsTypeFinderType.groupByCardNum(cards);
                            int c = 0;
                            for (GroupCard group : groups) {
                                if (group.getCount() == 1 && group.getCardNum() < Card.ER_NUM) {
                                    c += 1;
                                }
                            }
                            if (c >= 2) {
                                // 单张出不完,不能炸
                                return result;
                            } else {
                                result.add(list);
                                return result;
                            }
                        } else {
                            List<List<Card>> out = CardsTypeFinderType.findTypeCards(myHands, CardsType.DAN);
                            if (!out.isEmpty()) {
                                int c = 0;
                                for (List<Card> ll : out) {
                                    for (Card card : ll) {
                                        if (card.num < Card.ER_NUM) {
                                            c += 1;
                                        }
                                    }
                                }
                                if (c >= 2) {
                                    // 单张出不完,不能炸
                                    return result;
                                } else {
                                    result.add(list);
                                    return result;
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    private static boolean canTakeOver(List<Card> myHands, List<Card> nextHands, List<Card> otherHands, boolean nextAlly, boolean otherAlly) {
        Card myMaxDan = maxDan(myHands);
        Card max = myMaxDan;
        Card nextMaxDan = maxDan(nextHands);
        Card otherMaxDan = maxDan(otherHands);
        if (nextAlly) {
            // 下家是我盟友
            if (max == null) {
                max = nextMaxDan;
            }
            if (nextMaxDan != null && nextMaxDan.num > max.num) {
                max = nextMaxDan;
            }

            // 与上家进行比较
            if (max != null) {
                if (otherMaxDan != null && max.num >= otherMaxDan.num) {
                    return true;
                }
            }
        } else if (otherAlly) {
            // 上家是我盟友
            if (max == null) {
                max = otherMaxDan;
            }
            if (otherMaxDan != null && otherMaxDan.num > max.num) {
                max = otherMaxDan;
            }
            // 与下家进行比较
            if (max != null) {
                if (nextMaxDan != null && max.num >= nextMaxDan.num) {
                    return true;
                }
            }
        } else {
            // 我是地主 单独郑营
            Card max1 = nextMaxDan;
            if (max1 == null) {
                max1 = otherMaxDan;
            }
            if (otherMaxDan != null && otherMaxDan.num > max1.num) {
                max1 = otherMaxDan;
            }
            // 与我进行比较
            if (max != null) {
                if (max1 != null && max.num >= max1.num) {
                    return true;
                }
            }
        }
        return false;
    }

    private static Card maxDan(List<Card> nextHands) {
        Entity entity = optimumSolution(nextHands);
        if (entity != null) {
            List<Card> list = entity.left;
            // 排除双王 排除炸弹
            boolean xw = false;
            boolean dw = false;
            Card card1 = null;
            Card card2 = null;
            for (Card card : list) {
                if (card.num == Card.XW_NUM) {
                    xw = true;
                    card1 = card;
                }
                if (card.num == Card.DW_NUM) {
                    dw = true;
                    card2 = card;
                }
            }
            if (xw && dw) {
                list.remove(card1);
                list.remove(card2);
            }
            // 排除个数不为1的不是单张的

            List<GroupCard> groups = CardsTypeFinderType.groupByCardNum(list);
            List<GroupCard> removes = new ArrayList<>();
            for (GroupCard group : groups) {
                if (group.getCount() > 1) {
                    removes.add(group);
                }
            }
            groups.removeAll(removes);
            List<Card> result1 = new ArrayList<>();
            for (GroupCard group : groups) {
                result1.addAll(group.getList());
            }
            Collections.sort(result1, new Comparator<Card>() {

                @Override
                public int compare(Card o1, Card o2) {
                    return o2.num - o1.num;
                }
            });
            if (!result1.isEmpty()) {
                return result1.get(0);
            }
        } else {
            List<List<Card>> next = CardsTypeFinderType.findTypeCards(nextHands, CardsType.DAN);
            boolean xw = false;
            boolean dw = false;
            List<List<Card>> removes = new ArrayList<>();
            for (List<Card> cards : next) {
                for (Card card : cards) {
                    if (card.num == Card.XW_NUM) {
                        xw = true;
                        removes.add(cards);
                        continue;
                    }
                    if (card.num == Card.DW_NUM) {
                        dw = true;
                        removes.add(cards);
                        continue;
                    }
                }
            }
            if (xw && dw) {
                next.removeAll(removes);
            }
            // 排除双王
            Collections.sort(next, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.get(0).num - o1.get(0).num;
                }
            });
            if (!next.isEmpty()) {
                return next.get(0).get(0);
            }
        }
        return null;
    }

    private static List<Card> choose1(List<List<Card>> out) {
        List<Card> zero = new ArrayList<>();
        if (!out.isEmpty()) {
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    int tmp = o2.size() - o1.size();
                    if (tmp == 0) {
                        // 对比最小的先出
                        List<GroupCard> groups1 = CardsTypeFinderType.groupByCardNum(o1);
                        List<GroupCard> groups2 = CardsTypeFinderType.groupByCardNum(o2);
                        Collections.sort(groups1, new Comparator<GroupCard>() {

                            @Override
                            public int compare(GroupCard o1, GroupCard o2) {
                                int tmp1 = o2.getCount() - o1.getCount();
                                if (tmp1 == 0) {
                                    tmp1 = o2.getCardNum() - o2.getCardNum();
                                }
                                return tmp1;
                            }
                        });
                        Collections.sort(groups2, new Comparator<GroupCard>() {

                            @Override
                            public int compare(GroupCard o1, GroupCard o2) {
                                int tmp1 = o2.getCount() - o1.getCount();
                                if (tmp1 == 0) {
                                    tmp1 = o2.getCardNum() - o2.getCardNum();
                                }
                                return tmp1;
                            }
                        });
                        tmp = groups2.get(0).getCardNum() - groups1.get(0).getCardNum();
                    }
                    return tmp;
                }
            });
            zero.addAll(out.get(0));
        }
        return zero;
    }

    private static List<Card> choose(List<List<Card>> out) {
        List<Card> zero = new ArrayList<>();
        if (!out.isEmpty()) {
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    int tmp = o2.size() - o1.size();
                    if (tmp == 0) {
                        // 对比最小的先出
                        List<GroupCard> groups1 = CardsTypeFinderType.groupByCardNum(o1);
                        List<GroupCard> groups2 = CardsTypeFinderType.groupByCardNum(o2);
                        Collections.sort(groups1, new Comparator<GroupCard>() {

                            @Override
                            public int compare(GroupCard o1, GroupCard o2) {
                                int tmp1 = o2.getCount() - o1.getCount();
                                if (tmp1 == 0) {
                                    tmp1 = o2.getCardNum() - o2.getCardNum();
                                }
                                return tmp1;
                            }
                        });
                        Collections.sort(groups2, new Comparator<GroupCard>() {

                            @Override
                            public int compare(GroupCard o1, GroupCard o2) {
                                int tmp1 = o2.getCount() - o1.getCount();
                                if (tmp1 == 0) {
                                    tmp1 = o2.getCardNum() - o2.getCardNum();
                                }
                                return tmp1;
                            }
                        });
                        tmp = groups1.get(0).getCardNum() - groups2.get(0).getCardNum();
                    }
                    return tmp;
                }
            });
            zero.addAll(out.get(0));
        }
        return zero;
    }

    private static List<Card> yapai(boolean nextAlly, boolean otherAlly, List<Card> myHands, List<Card> nextHands, List<Card> otherHands, List<Card> plays, boolean prePlay, int yaPai) {
        logger.info("====================YA:{}", yaPai);
        List<Card> result = new ArrayList<>();
        if (yaPai == 0) {
            return result;
        }
        CardsType cardEnum = analysis(plays);
        List<List<Card>> out = CardsTypeFinderType.findTypeCards(myHands, CardsType.DAN);
        List<Card> target = new ArrayList<>();
        int oldDan = out.size();
        Entity entity = optimumSolution(myHands);
        if (entity != null) {
            oldDan = entity.left.size() + entity.solution.size();
        }
        switch (cardEnum) {
            case DAN: {
                Card card0 = plays.get(0);
                if (out.isEmpty()) {
                    // 要压牌
                    if (yaPai == 2) {
                        result = chaW(myHands, yaPai, card0, oldDan);
                        if (result.isEmpty()) {
                            result = chaDan(myHands, yaPai, card0, oldDan);
                        }
                    }
                    if (yaPai == 1) {
                        result = chaW(myHands, yaPai, card0, oldDan);
                    }
                } else {
                    if (nextHands.size() == 1 && !nextAlly) {
                        // 下家报单不是盟友
                        if (nextHands.get(0).num >= Card.ER_NUM) {
                            result = chaW(myHands, yaPai, card0, oldDan);
                        }
                        if (!result.isEmpty()) {
                            return result;
                        }

                        if (yaPai == 2) {
                            // 倒叙拆
                            Collections.sort(out, new Comparator<List<Card>>() {

                                @Override
                                public int compare(List<Card> o1, List<Card> o2) {
                                    return o2.get(0).num - o1.get(0).num;
                                }
                            });
                        }
                    }
                    // 接单张
                    for (List<Card> list : out) {
                        Card card1 = list.get(0);
                        if (card1.num > card0.num) {
                            if (nextHands.size() == 1 && !nextAlly) {
                                // 下家报单不是盟友
                                target.addAll(list);
                                break;
                            }
                            List<Card> tmps = new ArrayList<>();
                            tmps.addAll(myHands);
                            tmps.removeAll(list);
                            entity = optimumSolution(tmps);
                            if (entity != null) {
                                if (entity.left.size() + entity.solution.size() > oldDan) {
                                    // 拆开打结果新牌型数量比原来还多了,即不能拆
                                    // 如果包含在四带二类型则可以拆
                                    List<List<Card>> solution = entity.solution;
                                    boolean find = false;
                                    for (List<Card> k : solution) {
                                        CardsType type = analysis(k);
                                        if (type == CardsType.SI_DAI_DAN && k.contains(card1)) {
                                            target.addAll(list);
                                            find = true;
                                            break;
                                        }
                                    }
                                    if (find) {
                                        break;
                                    }
                                } else {
                                    target.addAll(list);
                                    break;
                                }
                            } else {
                                target.addAll(list);
                                break;
                            }
                        }
                    }
                    if (target.isEmpty()) {
                        if (yaPai == 2) {
                            result = chaW(myHands, yaPai, card0, oldDan);
                            if (result.isEmpty()) {
                                result = chaDan(myHands, yaPai, card0, oldDan);
                            }
                        }
                        if (yaPai == 1) {
                            result = chaW(myHands, yaPai, card0, oldDan);
                        }
                    } else {
                        result.addAll(target);
                    }
                }
                if (result.isEmpty()) {
                    // 不要的情况,检测是否手里是否只剩王 和单牌,如果是则拆王
                    List<List<Card>> wangzha = CardsTypeFinderType.findTypeCards(myHands, CardsType.WANG_ZHA);
                    if (!wangzha.isEmpty()) {
                        myHands.removeAll(wangzha.get(0));
                    }
                    entity = optimumSolution(myHands);
                    if (entity == null) {
                        out = CardsTypeFinderType.findTypeCards(myHands, CardsType.DAN);
                        if (out.size() >= 2) {
                            result = chaW(myHands, yaPai, card0, oldDan);
                        }
                    } else {
                        List<List<Card>> solution = entity.solution;
                        boolean allSidaier = true;
                        for (List<Card> k : solution) {
                            CardsType type = analysis(k);
                            if (type != CardsType.SI_DAI_DAN) {
                                allSidaier = false;
                                break;
                            }
                        }
                        if (allSidaier) {
                            if (entity.left.size() >= 1) {
                                result = chaW(myHands, yaPai, card0, oldDan);
                            }
                        }
                    }
                }

                if (result.isEmpty() && yaPai != 2) {
                    if (prePlay) {
                        if (!nextAlly && !otherAlly) {
                            yaPai = 2;
                            result = yapai(nextAlly, otherAlly, myHands, nextHands, otherHands, plays, prePlay, yaPai);
                        }
                    } else {
                        if (!nextAlly) {
                            yaPai = 2;
                            result = yapai(nextAlly, otherAlly, myHands, nextHands, otherHands, plays, prePlay, yaPai);
                        }
                    }
                }
            }
                return result;
            case DUI: {
                Card card0 = plays.get(0);
                out = CardsTypeFinderType.findTypeCards(myHands, CardsType.DUI);
                if (out.isEmpty()) {
                    if (yaPai == 2) {
                        result = chaDz(myHands, yaPai, card0, oldDan);
                    }
                } else {
                    if (yaPai == 2) {
                        // 倒叙拆
                        Collections.sort(out, new Comparator<List<Card>>() {

                            @Override
                            public int compare(List<Card> o1, List<Card> o2) {
                                return o2.get(0).num - o1.get(0).num;
                            }
                        });
                    }
                    // 接对子
                    for (List<Card> list : out) {
                        Card card1 = list.get(0);
                        if (card1.num > card0.num) {
                            List<Card> tmps = new ArrayList<>();
                            tmps.addAll(myHands);
                            tmps.removeAll(list);
                            entity = optimumSolution(tmps);
                            if (entity != null) {
                                if (entity.left.size() + entity.solution.size() > oldDan) {
                                    // 拆开打结果新牌型数量比原来还多了,即不能拆
                                } else {
                                    target.addAll(list);
                                    break;
                                }
                            } else {
                                target.addAll(list);
                                break;
                            }
                        }
                    }
                    if (target.isEmpty()) {
                        if (yaPai == 2) {
                            result = chaDz(myHands, yaPai, card0, oldDan);
                        }
                    } else {
                        result.addAll(target);
                    }
                }

                if (result.isEmpty() && yaPai != 2) {
                    if (prePlay) {
                        if (!nextAlly && !otherAlly) {
                            yaPai = 2;
                            result = yapai(nextAlly, otherAlly, myHands, nextHands, otherHands, plays, prePlay, yaPai);
                        }
                    } else {
                        if (!nextAlly) {
                            yaPai = 2;
                            result = yapai(nextAlly, otherAlly, myHands, nextHands, otherHands, plays, prePlay, yaPai);
                        }
                    }
                }
            }
                return result;

            case SAN_ZHANG: {
                int num0 = findMaxNum(plays);
                out = CardsTypeFinderType.findTypeCards(myHands, CardsType.SAN_ZHANG);
                if (out.isEmpty()) {
                } else {
                    // 接三张
                    if (yaPai == 2) {
                        // 倒叙拆
                        Collections.sort(out, new Comparator<List<Card>>() {

                            @Override
                            public int compare(List<Card> o1, List<Card> o2) {
                                return o2.get(0).num - o1.get(0).num;
                            }
                        });
                    }
                    for (List<Card> list : out) {
                        int num = findMaxNum(list);
                        if (num > num0) {
                            List<Card> tmps = new ArrayList<>();
                            tmps.addAll(myHands);
                            tmps.removeAll(list);
                            entity = optimumSolution(tmps);
                            if (entity != null) {
                                if (entity.left.size() + entity.solution.size() > oldDan) {
                                    // 拆开打结果新牌型数量比原来还多了,即不能拆
                                } else {
                                    target.addAll(list);
                                    break;
                                }
                            }
                        }
                    }
                    if (!target.isEmpty()) {
                        result.addAll(target);
                    }
                }
            }
                return result;
            case SAN_DAI_DAN: {
                int num0 = findMaxNum(plays);
                out = CardsTypeFinderType.findTypeCards(myHands, CardsType.SAN_DAI_DAN);
                if (out.isEmpty()) {
                } else {
                    // 接三带一
                    for (List<Card> list : out) {
                        int num = findMaxNum(list);
                        if (num > num0) {
                            List<Card> tmps = new ArrayList<>();
                            tmps.addAll(myHands);
                            tmps.removeAll(list);
                            entity = optimumSolution(tmps);
                            if (entity != null) {
                                if (entity.left.size() + entity.solution.size() > oldDan) {
                                    // 拆开打结果新牌型数量比原来还多了,即不能拆
                                } else {
                                    target.addAll(list);
                                    break;
                                }
                            }
                        }
                    }
                    if (!target.isEmpty()) {
                        result.addAll(target);
                    }
                }
            }
                return result;
            case SAN_DAI_DUI: {
                int num0 = findMaxNum(plays);
                out = CardsTypeFinderType.findTypeCards(myHands, CardsType.SAN_DAI_DUI);
                if (out.isEmpty()) {
                } else {
                    // 接三带二
                    for (List<Card> list : out) {
                        int num = findMaxNum(list);
                        if (num > num0) {
                            List<Card> tmps = new ArrayList<>();
                            tmps.addAll(myHands);
                            tmps.removeAll(list);
                            entity = optimumSolution(tmps);
                            if (entity != null) {
                                if (entity.left.size() + entity.solution.size() > oldDan) {
                                    // 拆开打结果新牌型数量比原来还多了,即不能拆
                                } else {
                                    target.addAll(list);
                                    break;
                                }
                            }
                        }
                    }
                    if (!target.isEmpty()) {
                        result.addAll(target);
                    }
                }
            }
                return result;
            case SI_DAI_DAN: {
                int num0 = findMaxNum(plays);
                out = CardsTypeFinderType.findTypeCards(myHands, CardsType.SI_DAI_DAN);
                if (out.isEmpty()) {
                } else {
                    // 接四带单
                    for (List<Card> list : out) {
                        int num = findMaxNum(list);
                        if (num > num0) {
                            List<Card> tmps = new ArrayList<>();
                            tmps.addAll(myHands);
                            tmps.removeAll(list);
                            entity = optimumSolution(tmps);
                            if (entity != null) {
                                if (entity.left.size() + entity.solution.size() > oldDan) {
                                    // 拆开打结果新牌型数量比原来还多了,即不能拆
                                } else {
                                    target.addAll(list);
                                    break;
                                }
                            }
                        }
                    }
                    if (!target.isEmpty()) {
                        result.addAll(target);
                    }
                }
            }
                return result;
            case SI_DAI_DUI: {
                int num0 = findMaxNum(plays);
                out = CardsTypeFinderType.findTypeCards(myHands, CardsType.SI_DAI_DUI);
                if (out.isEmpty()) {
                } else {
                    // 接四带对
                    for (List<Card> list : out) {
                        int num = findMaxNum(list);
                        if (num > num0) {
                            List<Card> tmps = new ArrayList<>();
                            tmps.addAll(myHands);
                            tmps.removeAll(list);
                            entity = optimumSolution(tmps);
                            if (entity != null) {
                                if (entity.left.size() + entity.solution.size() > oldDan) {
                                    // 拆开打结果新牌型数量比原来还多了,即不能拆
                                } else {
                                    target.addAll(list);
                                    break;
                                }
                            }
                        }
                    }
                    if (!target.isEmpty()) {
                        result.addAll(target);
                    }
                }

            }
                return result;
            case SHUN: {
                Collections.sort(plays, new Comparator<Card>() {

                    @Override
                    public int compare(Card o1, Card o2) {
                        return o1.num - o2.num;
                    }
                });
                Card card0 = plays.get(0);
                out = CardsTypeFinderType.findTypeCards(myHands, CardsType.SHUN);
                if (out.isEmpty()) {
                } else {
                    // 顺子
                    int size = plays.size();
                    for (List<Card> list : out) {
                        if (list.size() == size) {
                            Collections.sort(list, new Comparator<Card>() {

                                @Override
                                public int compare(Card o1, Card o2) {
                                    return o1.num - o2.num;
                                }
                            });
                            if (list.get(0).num > card0.num) {
                                List<Card> tmps = new ArrayList<>();
                                tmps.addAll(myHands);
                                tmps.removeAll(list);
                                entity = optimumSolution(tmps);
                                if (entity != null) {
                                    if (entity.left.size() + entity.solution.size() > oldDan) {
                                        // 拆开打结果新牌型数量比原来还多了,即不能拆
                                    } else {
                                        target.addAll(list);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (!target.isEmpty()) {
                        result.addAll(target);
                    }
                }
            }
                return result;
            case LIAN_DUI: {
                Collections.sort(plays, new Comparator<Card>() {

                    @Override
                    public int compare(Card o1, Card o2) {
                        return o1.num - o2.num;
                    }
                });
                Card card0 = plays.get(0);
                out = CardsTypeFinderType.findTypeCards(myHands, CardsType.LIAN_DUI);
                if (out.isEmpty()) {
                } else {
                    // 顺子
                    int size = plays.size();
                    for (List<Card> list : out) {
                        if (list.size() == size) {
                            Collections.sort(list, new Comparator<Card>() {

                                @Override
                                public int compare(Card o1, Card o2) {
                                    return o1.num - o2.num;
                                }
                            });
                            if (list.get(0).num > card0.num) {
                                List<Card> tmps = new ArrayList<>();
                                tmps.addAll(myHands);
                                tmps.removeAll(list);
                                entity = optimumSolution(tmps);
                                if (entity != null) {
                                    if (entity.left.size() + entity.solution.size() > oldDan) {
                                        // 拆开打结果新牌型数量比原来还多了,即不能拆
                                    } else {
                                        target.addAll(list);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (!target.isEmpty()) {
                        result.addAll(target);
                    }
                }
            }
                return result;
            case FEI_JI: {
                out = CardsTypeFinderType.findTypeCards(myHands, CardsType.FEI_JI);
                if (out.isEmpty()) {
                } else {
                    // 顺子
                    int size = plays.size();
                    List<List<Card>> out0 = CardsTypeFinderType.findTypeCards(plays, CardsType.FEI_JI);
                    int num0 = findMaxNum(out0);
                    for (List<Card> list : out) {
                        if (list.size() == size) {
                            int num = findMaxNum(out);
                            if (num > num0) {
                                List<Card> tmps = new ArrayList<>();
                                tmps.addAll(myHands);
                                tmps.removeAll(list);
                                entity = optimumSolution(tmps);
                                if (entity != null) {
                                    if (entity.left.size() + entity.solution.size() > oldDan) {
                                        // 拆开打结果新牌型数量比原来还多了,即不能拆
                                    } else {
                                        target.addAll(list);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (!target.isEmpty()) {
                        result.addAll(target);
                    }
                }
            }
                return result;
            case FEI_JI_DAI_DAN: {
                out = CardsTypeFinderType.findTypeCards(myHands, CardsType.FEI_JI_DAI_DAN);
                if (out.isEmpty()) {
                } else {
                    // 顺子
                    int size = plays.size();
                    List<List<Card>> out0 = CardsTypeFinderType.findTypeCards(plays, CardsType.FEI_JI_DAI_DAN);
                    int num0 = findMaxNum(out0);
                    for (List<Card> list : out) {
                        if (list.size() == size) {
                            int num = findMaxNum(out);
                            if (num > num0) {
                                List<Card> tmps = new ArrayList<>();
                                tmps.addAll(myHands);
                                tmps.removeAll(list);
                                entity = optimumSolution(tmps);
                                if (entity != null) {
                                    if (entity.left.size() + entity.solution.size() > oldDan) {
                                        // 拆开打结果新牌型数量比原来还多了,即不能拆
                                    } else {
                                        target.addAll(list);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (!target.isEmpty()) {
                        result.addAll(target);
                    }
                }
            }
                return result;
            case FEI_JI_DAI_DUI: {
                out = CardsTypeFinderType.findTypeCards(myHands, CardsType.FEI_JI_DAI_DUI);
                if (out.isEmpty()) {
                } else {
                    // 顺子
                    int size = plays.size();
                    List<List<Card>> out0 = CardsTypeFinderType.findTypeCards(plays, CardsType.FEI_JI_DAI_DUI);
                    int num0 = findMaxNum(out0);
                    for (List<Card> list : out) {
                        if (list.size() == size) {
                            int num = findMaxNum(out);
                            if (num > num0) {
                                List<Card> tmps = new ArrayList<>();
                                tmps.addAll(myHands);
                                tmps.removeAll(list);
                                entity = optimumSolution(tmps);
                                if (entity != null) {
                                    if (entity.left.size() + entity.solution.size() > oldDan) {
                                        // 拆开打结果新牌型数量比原来还多了,即不能拆
                                    } else {
                                        target.addAll(list);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (!target.isEmpty()) {
                        result.addAll(target);
                    }
                }
            }
                return result;
            case ZHA_DAN: {
                int max = 0;
                int min = Integer.MAX_VALUE;
                Card card0 = plays.get(0);
                out = CardsTypeFinderType.findTypeCards(myHands, CardsType.FEI_JI_DAI_DUI);
                if (out.isEmpty()) {
                    // 处理王炸
                    out = CardsTypeFinderType.findTypeCards(myHands, CardsType.WANG_ZHA);
                    if (!out.isEmpty()) {
                        target.addAll(out.get(0));
                    }
                    boolean bone = canZha(myHands, nextHands, otherHands, target, prePlay, nextAlly, otherAlly);
                    if (bone && !target.isEmpty()) {
                        result.addAll(target);
                    }
                } else {
                    for (List<Card> list : out) {
                        Card card1 = list.get(0);
                        if (card1.num > card0.num) {
                            List<Card> tmps = new ArrayList<>();
                            tmps.addAll(myHands);
                            tmps.removeAll(list);
                            if (tmps.isEmpty()) {
                                target.addAll(list);
                                break;
                            } else {
                                entity = optimumSolution(tmps);
                                if (entity != null) {
                                    if (entity.left.size() + entity.solution.size() <= oldDan) {
                                        List<List<Card>> tt = new ArrayList<>();
                                        tt.add(list);
                                        int num = findMaxNum(tt);
                                        if (yaPai == 2) {
                                            if (num > max) {
                                                max = num;
                                                target.clear();
                                                target.addAll(list);
                                            }
                                        } else if (yaPai == 1) {
                                            if (num < min) {
                                                min = num;
                                                target.clear();
                                                target.addAll(list);
                                            }
                                        }
                                    }
                                } else {
                                    target.addAll(list);
                                    break;
                                }
                            }
                        }
                    }

                    boolean bone = canZha(myHands, nextHands, otherHands, target, prePlay, nextAlly, otherAlly);
                    if (bone && !target.isEmpty()) {
                        result.addAll(target);
                    }
                }
            }
                return result;
            default:
                break;
        }
        return result;

    }

    private static List<Card> chaW(List<Card> myHands, int yaPai, Card card0, int oldDan) {
        List<Card> result = new ArrayList<>();
        List<List<Card>> out = CardsTypeFinderType.findTypeCards(myHands, CardsType.WANG_ZHA);
        if (!out.isEmpty() && out.get(0).get(0).num > card0.num) {
            result.add(out.get(0).get(0));
            return result;
        }

        boolean xiaowang = false;
        boolean dawang = false;
        Card xw = null;
        Card dw = null;
        if (result.isEmpty()) {
            // 判断是否有小王或者大王
            for (Card card : myHands) {
                if (card.num == Card.XW_NUM) {
                    xiaowang = true;
                    xw = card;
                }
                if (card.num == Card.DW_NUM) {
                    dawang = true;
                    dw = card;
                }
            }
            if (dawang && !xiaowang) {
                result.add(dw);
                return result;
            }
            if (xiaowang && !dawang) {
                if (xw.num > card0.num) {
                    result.add(xw);
                }
                return result;
            }
        }
        return result;
    }

    private static boolean canZha(List<Card> myHands, List<Card> nextHands, List<Card> otherHands, List<Card> target, boolean prePlay, boolean nextAlly, boolean otherAlly) {
        boolean bone = false;
        boolean preWarn = false;// 上家报警
        boolean preBaoDan = false;// 上家报单
        boolean nextWarn = false;// 下家报警
        boolean nextBaoDan = false;// 下家报单
        if (nextHands.size() == 2) {
            nextWarn = true;
        }
        if (nextHands.size() == 1) {
            nextBaoDan = true;
        }
        if (otherHands.size() == 2) {
            preWarn = true;
        }
        if (otherHands.size() == 1) {
            preBaoDan = true;
        }
        Entity entity = null;
        if (!target.isEmpty()) {
            if (prePlay) {
                if (otherAlly) {
                    // 上家打牌且上家是盟友
                    if (nextBaoDan) {
                        // 下家报单,看自己是否全手无单或者盟友全手无单
                        List<Card> tmps = new ArrayList<>();
                        tmps.addAll(myHands);
                        tmps.removeAll(target);
                        entity = optimumSolution(tmps);
                        if (entity != null && entity.solution.size() + entity.left.size() == 1) {
                            // 可以炸
                            bone = true;
                        }
                        if (entity == null) {
                            List<List<Card>> my_out = CardsTypeFinderType.findTypeCards(tmps, CardsType.DAN);
                            if (my_out.isEmpty() || my_out.size() == 1) {
                                // 可以炸
                                bone = true;
                            }
                        }
                        // 看盟友是否无单牌且自己不全是单张
                        List<List<Card>> pre_out = CardsTypeFinderType.findTypeCards(otherHands, CardsType.DAN);
                        if ((pre_out.isEmpty() || pre_out.size() == 1) && entity != null && entity.solution.size() != 0) {
                            // 可以炸
                            bone = true;
                        }
                    }
                    if (nextWarn) {
                        // 下家报双,看自己能否一次性出完
                        List<Card> tmps = new ArrayList<>();
                        tmps.addAll(myHands);
                        tmps.removeAll(target);
                        entity = optimumSolution(tmps);
                        if (entity != null && entity.solution.size() + entity.left.size() == 1) {
                            // 可以炸
                            bone = true;
                        }
                        if (entity == null) {
                            List<List<Card>> my_out = CardsTypeFinderType.findTypeCards(tmps, CardsType.DAN);
                            if (my_out.isEmpty() || my_out.size() == 1) {
                                // 可以炸
                                bone = true;
                            }
                        }
                    }
                } else if (nextAlly) {
                    // 上家打牌且下家是盟友
                    if (nextBaoDan) {
                        // 可以炸
                        bone = true;
                    }
                    if (nextWarn) {
                        List<Card> tmps = new ArrayList<>();
                        tmps.addAll(myHands);
                        tmps.removeAll(target);
                        entity = optimumSolution(tmps);
                        if (entity != null && entity.solution.size() + entity.left.size() == 1) {
                            // 可以炸
                            bone = true;
                        }
                        if (entity == null) {
                            List<List<Card>> my_out = CardsTypeFinderType.findTypeCards(tmps, CardsType.DAN);
                            if (my_out.isEmpty() || my_out.size() == 1) {
                                // 可以炸
                                bone = true;
                            }
                        }
                    }
                    if (preBaoDan) {
                        // 上家报单
                        List<Card> tmps = new ArrayList<>();
                        tmps.addAll(myHands);
                        tmps.removeAll(target);
                        entity = optimumSolution(tmps);
                        if (entity != null && entity.solution.size() + entity.left.size() == 1) {
                            // 可以炸
                            bone = true;
                        }
                        if (entity == null) {
                            List<List<Card>> my_out = CardsTypeFinderType.findTypeCards(tmps, CardsType.DAN);
                            if (my_out.isEmpty() || my_out.size() == 1) {
                                // 可以炸
                                bone = true;
                            }
                        }
                        // 看盟友是否无单牌且自己不全是单张
                        List<List<Card>> pre_out = CardsTypeFinderType.findTypeCards(nextHands, CardsType.DAN);
                        if ((pre_out.isEmpty() || pre_out.size() == 1) && entity != null && entity.solution.size() != 0) {
                            // 可以炸
                            bone = true;
                        }
                    }
                    if (preWarn) {
                        List<Card> tmps = new ArrayList<>();
                        tmps.addAll(myHands);
                        tmps.removeAll(target);
                        entity = optimumSolution(tmps);
                        if (entity != null && entity.solution.size() + entity.left.size() == 1) {
                            // 可以炸
                            bone = true;
                        }
                        if (entity == null) {
                            List<List<Card>> my_out = CardsTypeFinderType.findTypeCards(tmps, CardsType.DAN);
                            if (my_out.isEmpty() || my_out.size() == 1) {
                                // 可以炸
                                bone = true;
                            }
                        }
                    }
                } else {
                    List<Card> tmps = new ArrayList<>();
                    tmps.addAll(myHands);
                    tmps.removeAll(target);
                    entity = optimumSolution(tmps);
                    if (entity != null && entity.solution.size() + entity.left.size() == 1) {
                        // 可以炸
                        bone = true;
                    }
                    if (entity == null) {
                        List<List<Card>> my_out = CardsTypeFinderType.findTypeCards(tmps, CardsType.DAN);
                        if (my_out.isEmpty() || my_out.size() == 1) {
                            // 可以炸
                            bone = true;
                        }
                    }
                }
            } else {
                if (otherAlly) {
                    // 下家打牌且上家是盟友
                    if (nextBaoDan) {
                        List<Card> tmps = new ArrayList<>();
                        tmps.addAll(myHands);
                        tmps.removeAll(target);
                        entity = optimumSolution(tmps);
                        if (entity != null && entity.solution.size() + entity.left.size() == 1) {
                            // 可以炸
                            bone = true;
                        }
                        if (entity == null) {
                            List<List<Card>> my_out = CardsTypeFinderType.findTypeCards(tmps, CardsType.DAN);
                            if (my_out.isEmpty() || my_out.size() == 1) {
                                // 可以炸
                                bone = true;
                            }
                        }
                        // 看盟友是否无单牌且自己不全是单张
                        List<List<Card>> pre_out = CardsTypeFinderType.findTypeCards(otherHands, CardsType.DAN);
                        if ((pre_out.isEmpty() || pre_out.size() == 1) && entity != null && entity.solution.size() != 0) {
                            // 可以炸
                            bone = true;
                        }
                    }
                    if (nextWarn) {
                        List<Card> tmps = new ArrayList<>();
                        tmps.addAll(myHands);
                        tmps.removeAll(target);
                        entity = optimumSolution(tmps);
                        if (entity != null && entity.solution.size() + entity.left.size() == 1) {
                            // 可以炸
                            bone = true;
                        }
                        if (entity == null) {
                            List<List<Card>> my_out = CardsTypeFinderType.findTypeCards(tmps, CardsType.DAN);
                            if (my_out.isEmpty() || my_out.size() == 1) {
                                // 可以炸
                                bone = true;
                            }
                        }
                    }
                } else if (nextAlly) {
                    // 下家打牌且下家是盟友
                    List<Card> tmps = new ArrayList<>();
                    tmps.addAll(myHands);
                    tmps.removeAll(target);
                    entity = optimumSolution(tmps);
                    if (entity != null && entity.solution.size() + entity.left.size() == 1) {
                        // 可以炸
                        bone = true;
                    }
                    if (entity == null) {
                        List<List<Card>> my_out = CardsTypeFinderType.findTypeCards(tmps, CardsType.DAN);
                        if (my_out.isEmpty() || my_out.size() == 1) {
                            // 可以炸
                            bone = true;
                        }
                    }
                    // 看盟友是否无单牌且自己不全是单张
                    List<List<Card>> pre_out = CardsTypeFinderType.findTypeCards(nextHands, CardsType.DAN);
                    if ((pre_out.isEmpty() || pre_out.size() == 1) && entity != null && entity.solution.size() != 0) {
                        // 可以炸
                        bone = true;
                    }
                } else {
                    List<Card> tmps = new ArrayList<>();
                    tmps.addAll(myHands);
                    tmps.removeAll(target);
                    entity = optimumSolution(tmps);
                    if (entity != null && entity.solution.size() + entity.left.size() == 1) {
                        // 可以炸
                        bone = true;
                    }
                    if (entity == null) {
                        List<List<Card>> my_out = CardsTypeFinderType.findTypeCards(tmps, CardsType.DAN);
                        if (my_out.isEmpty() || my_out.size() == 1) {
                            // 可以炸
                            bone = true;
                        }
                    }
                }
            }
        }
        return bone;
    }

    private static int checkYa(boolean nextAlly, boolean otherAlly, List<Card> myHands, List<Card> nextHands, List<Card> otherHands, List<Card> plays, boolean prePlay, CardsType cardEnum) {
        boolean preWarn = false;// 上家报警
        boolean preBaoDan = false;// 上家报单
        boolean nextWarn = false;// 下家报警
        boolean nextBaoDan = false;// 下家报单
        if (nextHands.size() == 2) {
            nextWarn = true;
        }
        if (nextHands.size() == 1) {
            nextBaoDan = true;
        }
        if (otherHands.size() == 2) {
            preWarn = true;
        }
        if (otherHands.size() == 1) {
            preBaoDan = true;
        }
        if (prePlay) {
            if (otherAlly) {
                // 上家是我盟友,则下家非盟友
                if (nextBaoDan) {
                    if (cardEnum != CardsType.DAN) {
                        // 下家报单且上家盟友打的不是单张
                        List<List<Card>> out = CardsTypeFinderType.findTypeCards(myHands, CardsType.DAN);
                        if (out.isEmpty()) {
                            return 1;// 必压且打最小的牌,因为我手里没有单牌
                        } else {
                            // 自己不压牌,因为可能下家吃不起上家的牌
                        }
                    } else {
                        // 如果自己手里有双王,且上家打的单张是2则不压
                        List<List<Card>> out = CardsTypeFinderType.findTypeCards(myHands, CardsType.WANG_ZHA);
                        if (!out.isEmpty() && plays.get(0).num == Card.ER_NUM) {
                            return 0;
                        }
                        // 如果上家打的单张小于2 则自己出最大的进行压制 否则则检测下家能否大过上家的牌 如果不能则自己不进行压制
                        if (plays.get(0).num >= Card.A_NUM) {
                            if (nextHands.get(0).num > plays.get(0).num) {
                                // 自己需要进行压制
                                return 2;
                            } else {
                                return 0;
                            }
                        }
                        return 2;// 自己必压且打最大的牌
                    }
                }
                if (nextWarn) {
                    if (cardEnum == CardsType.DUI) {
                        // 下家报双,且上家打的是对子
                        return 2;// 自己必压且打最大的
                    } else {
                        if (cardEnum == CardsType.DAN && plays.get(0).num >= 14) {
                            // 如果刚好自己能出完,则压,否则不压
                            if (myHands.size() == 1 && myHands.get(0).num > plays.get(0).num) {
                                return 1;// 必压且打最小的牌,因为我手里只剩一张了
                            }
                            // 自己不压牌,因为可能下家吃不起上家的牌
                        }
                    }
                }

                if (preBaoDan || preWarn) {
                    // 如果刚好自己能出完,则压,否则不压
                    if (cardEnum == CardsType.DAN) {
                        if (myHands.size() == 2 && myHands.get(0).num == myHands.get(1).num && myHands.get(0).num > plays.get(0).num) {
                            return 2;
                        }
                        if (myHands.size() == 3 && myHands.get(0).num == myHands.get(1).num && myHands.get(0).num == myHands.get(2).num && myHands.get(0).num > plays.get(0).num) {
                            return 2;
                        }
                    }
                    if (cardEnum == CardsType.DUI) {
                        if (myHands.size() == 3 && myHands.get(0).num == myHands.get(1).num && myHands.get(0).num == myHands.get(2).num && myHands.get(0).num > plays.get(0).num) {
                            return 2;
                        }
                    }
                    List<List<Card>> out = CardsTypeFinderType.findTypeCards(myHands, cardEnum);
                    if (!out.isEmpty() && out.size() == 1) {
                        List<Card> tmps = new ArrayList<>();
                        tmps.addAll(myHands);
                        tmps.removeAll(out.get(0));
                        if (tmps.size() == 0) {
                            return 2;// 必压且出最小的牌,因为我拿到牌之后可以直接出完
                        }
                    }
                    // 自己不压牌,因为可能下家吃不起上家的牌
                }

                if (cardEnum == CardsType.DAN) {
                    if (myHands.size() == 1 && myHands.get(0).num > plays.get(0).num) {
                        return 1;// 必压且出最小的牌,因为我能够出完
                    }
                    if (cardEnum == CardsType.DAN && plays.get(0).num < 14) {
                        return 1;// 必压且出最小的牌
                    }
                    // 自己不压牌,因为可能下家吃不起上家的牌
                }

                if (cardEnum == CardsType.DUI) {
                    if (myHands.size() == 2 && myHands.get(0).num == myHands.get(1).num && myHands.get(0).num > plays.get(0).num) {
                        return 1;// 必压且出最小的牌,因为我能够出完
                    }
                    if (cardEnum == CardsType.DUI && plays.get(0).num < 14) {
                        return 1;// 必压且出最小的牌
                    }
                    // 自己不压牌,因为可能下家吃不起上家的牌
                }

                if (cardEnum == CardsType.SAN_ZHANG) {
                    if (myHands.size() == 3 && myHands.get(0).num == myHands.get(1).num && myHands.get(0).num == myHands.get(2).num && myHands.get(0).num > plays.get(0).num) {
                        return 1;// 必压且出最小的牌,因为我能够出完
                    }
                    if (cardEnum == CardsType.SAN_ZHANG && plays.get(0).num < 11) {
                        return 1;// 必压且出最小的牌
                    }
                    // 自己不压牌,因为可能下家吃不起上家的牌
                }

                if (cardEnum == CardsType.SAN_DAI_DAN || cardEnum == CardsType.SAN_DAI_DUI || cardEnum == CardsType.SHUN || cardEnum == CardsType.FEI_JI || cardEnum == CardsType.FEI_JI_DAI_DAN || cardEnum == CardsType.FEI_JI_DAI_DUI) {
                    // 如果刚好自己能出完,则压,否则不压
                    List<List<Card>> out = CardsTypeFinderType.findTypeCards(myHands, cardEnum);
                    if (!out.isEmpty() && out.size() == 1) {
                        List<Card> tmps = new ArrayList<>();
                        tmps.addAll(myHands);
                        tmps.removeAll(out.get(0));
                        if (tmps.size() == 0) {
                            return 1;// 必压且出最小的牌,因为我拿到牌之后可以直接出完
                        }
                    }
                    // 自己不压牌,因为可能下家吃不起上家的牌
                }
            } else if (nextAlly) {
                // 下家是我盟友,则上家非盟友,且上家打的牌
                if (preBaoDan) {
                    // 上家报单
                    if (cardEnum != CardsType.DAN) {
                        return 2;// 必压且打最小的牌
                    } else {
                        return 2;// 自己必压且打最大的牌
                    }
                }
                if (preWarn) {
                    if (cardEnum == CardsType.DUI) {
                        // 上家报双,且上家打的是对子
                        return 2;// 自己必压且打最大的
                    } else {
                        return 1;// 必压且打最小的牌
                    }
                }

                if (nextBaoDan) {
                    if (cardEnum == CardsType.DAN) {
                        // 下家报单且打的是单张
                        if (nextHands.get(0).num < plays.get(0).num) {
                            return 2;// 自己必压且打最大的
                        }
                        // 否则不压,让下家接直接出完
                    }
                    if (cardEnum != CardsType.DAN) {
                        return 1;// 必压且打最小的牌
                    }
                }

                if (nextWarn) {
                    if (cardEnum == CardsType.DUI) {
                        // 下家报单且打的是单张
                        if (nextHands.get(0).num != nextHands.get(1).num || nextHands.get(0).num < plays.get(0).num) {
                            return 2;// 自己必压且打最大的
                        }
                        // 否则不压,让下家接直接出完
                    }
                    if (cardEnum != CardsType.DUI) {
                        return 1;// 必压且打最小的牌
                    }
                }

                if (cardEnum == CardsType.DAN && myHands.size() == 2 && myHands.get(0).num == myHands.get(1).num) {
                    return 2;
                }

                if (cardEnum == CardsType.DAN || cardEnum == CardsType.DUI || cardEnum == CardsType.SAN_DAI_DAN || cardEnum == CardsType.SAN_DAI_DUI || cardEnum == CardsType.SHUN || cardEnum == CardsType.FEI_JI || cardEnum == CardsType.FEI_JI_DAI_DAN || cardEnum == CardsType.FEI_JI_DAI_DUI) {
                    return 1;// 必压且出最小的牌,因为我需要拿回出牌权
                }
            } else {
                // 我是地主
                if (nextBaoDan || preBaoDan) {
                    if (cardEnum == CardsType.DAN) {
                        return 2;// 自己必压且打最大的
                    } else {
                        return 1;// 必压且打最小的牌
                    }
                }
                if (nextWarn || preWarn) {
                    if (cardEnum == CardsType.DUI) {
                        return 2;// 自己必压且打最大的
                    } else {
                        return 1;// 必压且打最小的牌
                    }
                }
                if (myHands.size() == 2 && myHands.get(0).num == myHands.get(1).num) {
                    return 2;
                }
                return 1;// 必压且打最大的牌,因为我需要拿回出牌权
            }
        } else {
            // 下家打的牌
            if (otherAlly) {
                // 上家是我盟友,则下家非盟友
                if (nextBaoDan) {
                    if (cardEnum == CardsType.DAN) {
                        return 2;// 自己必压且打最大的牌,拿回牌权
                    } else {
                        return 1;// 自己必压且打最小的牌
                    }
                }
                if (nextWarn) {
                    if (cardEnum == CardsType.DUI) {
                        return 2;// 自己必压且打最大的,拿回牌权
                    } else {
                        return 1;// 自己必压且打最小的牌
                    }
                }

                if (cardEnum == CardsType.DAN || cardEnum == CardsType.DUI || cardEnum == CardsType.SAN_DAI_DAN || cardEnum == CardsType.SAN_DAI_DUI || cardEnum == CardsType.SHUN || cardEnum == CardsType.FEI_JI || cardEnum == CardsType.FEI_JI_DAI_DAN || cardEnum == CardsType.FEI_JI_DAI_DUI) {
                    return 1;// 自己必压且打最小的牌
                }
            } else if (nextAlly) {
                // 下家是我盟友,则上家非盟友,且下家打的牌
                if (cardEnum == CardsType.DAN || cardEnum == CardsType.DUI) {
                    // 如果下家可以接回去,自己可以接
                    List<List<Card>> next_out = CardsTypeFinderType.findTypeCards(nextHands, cardEnum);
                    List<List<Card>> pre_out = CardsTypeFinderType.findTypeCards(nextHands, cardEnum);
                    int max2 = findMaxNum(next_out);
                    int max3 = findMaxNum(pre_out);
                    if ((preBaoDan || preWarn) && max2 < max3) {
                        return 2;// 上家报单或报双且下家打的牌收不回去则强制接牌
                    }
                    if (max2 > max3) {
                        return 1;// 下家打的牌能够收回去
                    }
                }

            } else {
                // 我是地主
                if (nextBaoDan || preBaoDan) {
                    if (cardEnum == CardsType.DAN) {
                        return 2;// 自己必压且打最大的
                    } else {
                        return 1;// 必压且打最小的牌
                    }
                }
                if (nextWarn || preWarn) {
                    if (cardEnum == CardsType.DUI) {
                        return 2;// 自己必压且打最大的
                    } else {
                        return 1;// 必压且打最小的牌
                    }
                }
                return 1;// 必压且打最大的牌,因为我需要拿回出牌权
            }
        }
        return 0;
    }

    private static int findMaxNum(Collection<Card> list) {
        int max = 0;
        List<GroupCard> groups = CardsTypeFinderType.groupByCardNum(list);
        Collections.sort(groups, new Comparator<GroupCard>() {

            @Override
            public int compare(GroupCard o1, GroupCard o2) {
                int num = o2.getCount() - o1.getCount();
                if (num == 0) {
                    num = o2.getCardNum() - o1.getCardNum();
                }
                return num;
            }
        });
        if (groups.get(0).getCardNum() >= max) {
            max = groups.get(0).getCardNum();
        }
        return max;
    }

    private static List<List<Card>> checkZha(List<Card> myHands, boolean nextAlly, List<Card> nextHands) {
        List<List<Card>> out0 = CardsTypeFinderType.findTypeCards(myHands, CardsType.WANG_ZHA);
        List<List<Card>> out1 = CardsTypeFinderType.findTypeCards(myHands, CardsType.ZHA_DAN);
        List<Card> tmp = new ArrayList<>();
        tmp.addAll(myHands);
        List<List<Card>> result = new ArrayList<>();
        boolean hasWz = false;
        if (!out0.isEmpty()) {
            hasWz = true;
            for (List<Card> list : out0) {
                tmp.removeAll(list);
            }
        }
        int zhaCount = 0;
        if (hasWz) {
            zhaCount = 1;
            result.addAll(out0);
        }

        // JJJJ QQQQ JJJJQQQQ

        int num = Integer.MAX_VALUE;
        List<List<Card>> input = new ArrayList<>();
        List<List<List<Card>>> mm = new ArrayList<>();
        input.addAll(out1);
        Combine.combine(input, mm);
        if (!mm.isEmpty()) {
            for (List<List<Card>> m : mm) {
                int zha = zhaCount;
                List<Card> k = new ArrayList<>();
                k.addAll(tmp);
                for (List<Card> c : m) {
                    zha += 1;
                    k.removeAll(c);
                }
                Entity entity = optimumSolution(k);
                if (entity != null) {
                    num = entity.left.size() + entity.solution.size();
                    List<List<Card>> s = entity.solution;
                    for (List<Card> list : s) {
                        int max = findMaxNum(list);
                        if (max >= 14) {
                            num = num - 1;
                        }
                    }
                } else {
                    List<List<Card>> out2 = CardsTypeFinderType.findTypeCards(k, CardsType.DAN);
                    num = out2.size();
                }
                if (num <= zha && zha > 0) {
                    result.addAll(m);
                    return result;
                }
            }
        } else {
            Entity entity = optimumSolution(tmp);
            if (entity != null) {
                num = entity.left.size() + entity.solution.size();
                List<List<Card>> s = entity.solution;
                for (List<Card> list : s) {
                    int max = findMaxNum(list);
                    if (max >= 14) {
                        num = num - 1;
                    }
                }
            } else {
                List<List<Card>> out2 = CardsTypeFinderType.findTypeCards(tmp, CardsType.DAN);
                num = out2.size();
            }
            if (num <= zhaCount && zhaCount > 0) {
                return result;
            }

        }
        boolean baodan = false;
        if (nextHands.size() == 1) {
            baodan = true;
        }
        if (baodan && nextAlly) {
            // 下家是盟友且报单
            return result;
        }
        result.clear();
        return result;
    }

    private static List<Card> chaDz(List<Card> myHands, int yaPai, Card card0, int oldDan) {
        List<Card> target = new ArrayList<>();
        List<Card> result = new ArrayList<>();
        int max = 0;
        int min = Integer.MAX_VALUE;
        // 拆三张
        List<List<Card>> out = CardsTypeFinderType.findTypeCards(myHands, CardsType.SAN_ZHANG);
        if (yaPai == 2) {
            // 倒叙拆
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.get(0).num - o1.get(0).num;
                }
            });
        }
        if (!out.isEmpty()) {
            for (List<Card> list : out) {
                Card card1 = list.get(0);
                if (card1.num > card0.num) {
                    List<Card> tmps = new ArrayList<>();
                    tmps.addAll(myHands);
                    tmps.removeAll(list);
                    if (tmps.isEmpty() || yaPai == 2) {
                        target.addAll(list);
                        break;
                    } else {
                        Entity entity = optimumSolution(tmps);
                        if (entity != null) {
                            if (entity.left.size() + entity.solution.size() <= oldDan) {
                                List<List<Card>> tt = new ArrayList<>();
                                tt.add(list);
                                int num = findMaxNum(tt);
                                if (yaPai == 2) {
                                    if (num > max) {
                                        max = num;
                                        target.clear();
                                        target.addAll(list);
                                    }
                                } else if (yaPai == 1) {
                                    if (num < min) {
                                        min = num;
                                        target.clear();
                                        target.addAll(list);
                                    }
                                }
                            }
                        } else {
                            target.addAll(list);
                            break;
                        }
                    }
                }
            }

            List<Card> target1 = new ArrayList<>();
            if (!target.isEmpty() && target.size() >= 2) {
                for (int i = 0; i < 2; i++) {
                    target1.add(target.get(i));
                }
            }
            if (!target1.isEmpty()) {
                result.addAll(target1);
                return result;
            }
        }
        return result;
    }

    private static List<Card> chaDan(List<Card> myHands, int yaPai, Card card0, int oldDan) {
        List<Card> result = new ArrayList<>();
        if (yaPai == 2) {
            result = chaW(myHands, yaPai, card0, oldDan);
        }
        List<List<Card>> out = CardsTypeFinderType.findTypeCards(myHands, CardsType.DUI);
        if (yaPai == 2) {
            // 倒叙拆
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.get(0).num - o1.get(0).num;
                }
            });
        }
        int max = 0;
        int min = Integer.MAX_VALUE;
        List<Card> target = new ArrayList<>();
        if (!out.isEmpty()) {
            for (List<Card> list : out) {
                Card card1 = list.get(0);
                if (card1.num > card0.num) {
                    List<Card> tmps = new ArrayList<>();
                    tmps.addAll(myHands);
                    tmps.removeAll(list);
                    if (tmps.isEmpty() || yaPai == 2) {
                        target.add(card1);
                        break;
                    } else {
                        Entity entity = optimumSolution(tmps);
                        if (entity != null) {
                            if (entity.left.size() + entity.solution.size() <= oldDan) {
                                List<List<Card>> tt = new ArrayList<>();
                                tt.add(list);
                                int num = findMaxNum(tt);
                                if (yaPai == 2) {
                                    if (num > max) {
                                        max = num;
                                        target.clear();
                                        target.add(card1);
                                    }
                                } else if (yaPai == 1) {
                                    if (num < min) {
                                        min = num;
                                        target.clear();
                                        target.add(card1);
                                    }
                                }
                            }
                        } else {
                            target.add(card1);
                            break;
                        }
                    }
                }
            }

            if (!target.isEmpty()) {
                result.addAll(target);
                return result;
            }

        } else {
            // 没有对子 拆三张
            out = CardsTypeFinderType.findTypeCards(myHands, CardsType.SAN_ZHANG);
            if (yaPai == 2) {
                // 倒叙拆
                Collections.sort(out, new Comparator<List<Card>>() {

                    @Override
                    public int compare(List<Card> o1, List<Card> o2) {
                        return o2.get(0).num - o1.get(0).num;
                    }
                });
            }
            if (!out.isEmpty()) {
                for (List<Card> list : out) {
                    Card card1 = list.get(0);
                    if (card1.num > card0.num) {
                        List<Card> tmps = new ArrayList<>();
                        tmps.addAll(myHands);
                        tmps.removeAll(list);
                        if (tmps.isEmpty() || yaPai == 2) {
                            target.add(card1);
                            break;
                        } else {
                            Entity entity = optimumSolution(tmps);
                            if (entity != null) {
                                if (entity.left.size() + entity.solution.size() <= oldDan) {
                                    List<List<Card>> tt = new ArrayList<>();
                                    tt.add(list);
                                    int num = findMaxNum(tt);
                                    if (yaPai == 2) {
                                        if (num > max) {
                                            max = num;
                                            target.clear();
                                            target.add(card1);
                                        }
                                    } else if (yaPai == 1) {
                                        if (num < min) {
                                            min = num;
                                            target.clear();
                                            target.add(card1);
                                        }
                                    }
                                }
                            } else {
                                target.add(card1);
                                break;
                            }
                        }
                    }
                }
                if (!result.isEmpty()) {
                    result.addAll(target);
                    return result;
                }
            }
        }
        // 必须压,开始拆王
        if (yaPai == 1) {
            result = chaW(myHands, yaPai, card0, oldDan);
        }
        return result;
    }

    private static int findMaxNum(List<List<Card>> out) {
        int max = 0;
        for (List<Card> list : out) {
            List<GroupCard> groups = CardsTypeFinderType.groupByCardNum(list);
            Collections.sort(groups, new Comparator<GroupCard>() {

                @Override
                public int compare(GroupCard o1, GroupCard o2) {
                    int num = o2.getCount() - o1.getCount();
                    if (num == 0) {
                        num = o2.getCardNum() - o1.getCardNum();
                    }
                    return num;
                }
            });
            if (groups.get(0).getCardNum() >= max) {
                max = groups.get(0).getCardNum();
            }
        }
        return max;
    }

    public static CardsType analysis(Collection<Card> playCards) {
        CardsType cardType = null;
        List<Card> cards = new ArrayList<>();
        cards.addAll(playCards);
        if (playCards.isEmpty()) {
            // 不要
            cardType = CardsType.PASS;
        }
        if (CardsTypeGetter.isDan(cards)) { // 单牌
            cardType = CardsType.DAN;
        } else if (CardsTypeGetter.isWangZha(cards)) { // 火箭：即双王（双花牌），什么牌型都可打，是最大的牌
            cardType = CardsType.WANG_ZHA;
        } else if (CardsTypeGetter.isDui(cards)) { // 对子：两个
            cardType = CardsType.DUI;
        } else if (CardsTypeGetter.isSanZhang(cards)) { // 三张牌：三张牌点相同的牌。
            cardType = CardsType.SAN_ZHANG;
        } else if (CardsTypeGetter.isZhaDan(cards)) { // 四张牌点相同的牌（如四个8）。除火箭和比自己大的炸弹外，什么牌型都可打
            cardType = CardsType.ZHA_DAN;
        } else if (CardsTypeGetter.isSanDaiDan(cards)) { // 三带单：三张牌 ＋ 一张单牌。例如： 888+9
            cardType = CardsType.SAN_DAI_DAN;
        } else if (CardsTypeGetter.isSanDaiDui(cards)) { // 三带对：三张牌 ＋ 一对牌。例如： 888+99
            cardType = CardsType.SAN_DAI_DUI;
        } else if (CardsTypeGetter.isShun(cards)) { // 单顺：五张或更多连续的单牌。例如：3+4+5+6+7+8。不包括2和大、小王。
            cardType = CardsType.SHUN;
        } else if (CardsTypeGetter.isShuangShun(cards)) { // 双顺：三个或更多连续的对牌。例如：33+44+55。不包括2和大、小王。
            cardType = CardsType.LIAN_DUI;
        } else if (CardsTypeGetter.isFeiJi(cards)) { // 三顺：二个或更多连续的三张牌。例如：333444、444555666777。不包括2和大、小王。
            cardType = CardsType.FEI_JI;
        } else if (CardsTypeGetter.isFeiJiDaiDan(cards)) { // 飞机带单翅膀：三顺＋同数量的单套牌。例如：333444+69 ，333444+66也是合法的
            cardType = CardsType.FEI_JI_DAI_DAN;
        } else if (CardsTypeGetter.isFeiJiDaiDui(cards)) { // 飞机带双翅膀：三顺＋同数量的对套牌。例如：333444555+667799
            cardType = CardsType.FEI_JI_DAI_DUI;
        } else if (CardsTypeGetter.isSiDaiDan(cards)) { // 四张牌＋任意两套张数相同的单牌。例如：5555＋3＋8
            cardType = CardsType.SI_DAI_DAN;
        } else if (CardsTypeGetter.isSiDaiDui(cards)) { // 四张牌＋任意两套张数相同的对牌。例如：4444＋55＋77
            cardType = CardsType.SI_DAI_DUI;
        }
        return cardType;
    }

    public static Entity solution0(List<Card> cards) {
        // 处理顺子
        List<List<Card>> out1 = CardsTypeFinderType.findTypeCards(cards, CardsType.SHUN);
        Collections.sort(out1, new Comparator<List<Card>>() {

            @Override
            public int compare(List<Card> o1, List<Card> o2) {
                return o2.size() - o1.size();
            }
        });
        int size = out1.size();
        List<Entity> result1 = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            List<List<Card>> result = new ArrayList<>();
            List<Card> tmp = new ArrayList<>();
            tmp.addAll(cards);
            List<Card> out2 = out1.get(i);
            result.add(out2);
            tmp.removeAll(out2);
            List<List<Card>> out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SHUN);
            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SHUN);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DUI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DAN);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DAN);
            }

            // 处理飞机
            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI);
            }

            // 处理连对
            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.LIAN_DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.LIAN_DUI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DAN);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DAN);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DUI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DUI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DAN);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DAN);
            }
            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_ZHANG);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_ZHANG);
            }
            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.DUI);
            }
            Entity entity = new Entity(tmp, result);
            result1.add(entity);
        }
        // 比较
        Collections.sort(result1, new Comparator<Entity>() {

            @Override
            public int compare(Entity o1, Entity o2) {
                int num = o1.getLeft().size() - o2.getLeft().size();
                if (num == 0) {
                    List<List<Card>> out = CardsTypeFinderType.findTypeCards(o1.getLeft(), CardsType.DAN);
                    List<List<Card>> out1 = CardsTypeFinderType.findTypeCards(o2.getLeft(), CardsType.DAN);
                    num = out.size() - out1.size();
                }
                return num;
            }

        });
        if (!result1.isEmpty()) {
            return result1.get(0);
        } else {
            return null;
        }
    }

    public static Entity solution1(List<Card> cards) {
        // 处理连对
        List<List<Card>> out1 = CardsTypeFinderType.findTypeCards(cards, CardsType.LIAN_DUI);
        Collections.sort(out1, new Comparator<List<Card>>() {

            @Override
            public int compare(List<Card> o1, List<Card> o2) {
                return o2.size() - o1.size();
            }
        });
        int size = out1.size();
        List<Entity> result1 = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            List<List<Card>> result = new ArrayList<>();
            List<Card> tmp = new ArrayList<>();
            tmp.addAll(cards);
            List<Card> out2 = out1.get(i);
            result.add(out2);
            tmp.removeAll(out2);
            List<List<Card>> out = CardsTypeFinderType.findTypeCards(tmp, CardsType.LIAN_DUI);
            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.LIAN_DUI);
            }
            // 处理顺子
            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SHUN);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SHUN);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DUI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DAN);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DAN);
            }

            // 处理飞机
            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DAN);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DAN);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DUI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DUI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DAN);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DAN);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_ZHANG);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_ZHANG);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.DUI);
            }
            Entity entity = new Entity(tmp, result);
            result1.add(entity);
        }
        // 比较
        Collections.sort(result1, new Comparator<Entity>() {

            @Override
            public int compare(Entity o1, Entity o2) {
                int num = o1.getLeft().size() - o2.getLeft().size();
                if (num == 0) {
                    List<List<Card>> out = CardsTypeFinderType.findTypeCards(o1.getLeft(), CardsType.DAN);
                    List<List<Card>> out1 = CardsTypeFinderType.findTypeCards(o2.getLeft(), CardsType.DAN);
                    num = out.size() - out1.size();
                }
                return num;
            }

        });
        if (!result1.isEmpty()) {
            return result1.get(0);
        } else {
            return null;
        }
    }

    public static class Entity {
        private List<Card> left = new ArrayList<>();
        private List<List<Card>> solution = new ArrayList<>();

        public Entity() {
            super();
        }

        public Entity(List<Card> left, List<List<Card>> solution) {
            super();
            if (left != null) {
                this.left.addAll(left);
            }
            if (solution != null) {
                this.solution.addAll(solution);
            }
        }

        public List<Card> getLeft() {
            return this.left;
        }

        public void setLeft(List<Card> left) {
            this.left = left;
        }

        public List<List<Card>> getSolution() {
            return this.solution;
        }

        public void setSolution(List<List<Card>> solution) {
            this.solution = solution;
        }

        @Override
        public String toString() {
            return "Entity [left=" + this.left + ", solution=" + this.solution + "]";
        }
    }

    public static Entity optimumSolution(List<Card> list) {
        // 双王排除
        List<List<Card>> out = CardsTypeFinderType.findTypeCards(list, CardsType.WANG_ZHA);
        List<Card> shuangwang = new ArrayList<>();
        if (!out.isEmpty()) {
            // 排除双王
            shuangwang.addAll(out.get(0));
            list.removeAll(shuangwang);
        }
        Entity entity0 = solution0(list);
        Entity entity1 = solution1(list);
        Entity entity2 = solution2(list);
        Entity entity3 = solution3(list);
        Entity entity4 = solution4(list);
        Entity entity5 = solution5(list);
        Entity entity6 = solution6(list);
        Entity entity7 = solution7(list);
        Entity entity8 = solution8(list);
        Entity entity9 = solution9(list);
        List<Entity> list1 = new ArrayList<>();
        if (entity0 != null) {
            list1.add(entity0);
        }
        if (entity1 != null) {
            list1.add(entity1);
        }
        if (entity2 != null) {
            list1.add(entity2);
        }
        if (entity3 != null) {
            list1.add(entity3);
        }
        if (entity4 != null) {
            list1.add(entity4);
        }
        if (entity5 != null) {
            list1.add(entity5);
        }
        if (entity6 != null) {
            list1.add(entity6);
        }
        if (entity7 != null) {
            list1.add(entity7);
        }
        if (entity8 != null) {
            list1.add(entity8);
        }
        if (entity9 != null) {
            list1.add(entity9);
        }
        if (!list1.isEmpty()) {
            Collections.sort(list1, new Comparator<Entity>() {

                @Override
                public int compare(Entity o1, Entity o2) {
                    int num = o1.left.size() - o2.left.size();
                    if (num == 0) {
                        return o1.solution.size() - o2.solution.size();
                    }
                    return num;
                }
            });
            Entity result = list1.get(0);
            List<List<Card>> solution = result.solution;
            Collections.sort(solution, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    CardsType type1 = analysis(o1);
                    CardsType type2 = analysis(o2);
                    int num2 = type2.type;
                    int num1 = type1.type;
                    if (num2 == 13) {
                        num2 = -200;
                    }
                    if (num2 == 14) {
                        num2 = -199;
                    }
                    if (num1 == 13) {
                        num1 = -200;
                    }
                    if (num1 == 14) {
                        num1 = -199;
                    }
                    if (type2 == CardsType.SAN_DAI_DAN || type2 == CardsType.SAN_ZHANG || type2 == CardsType.SAN_DAI_DUI) {
                        int n = findMaxNum(o2);
                        if (n > 10) {
                            num2 = -n;
                        }
                    }
                    if (type1 == CardsType.SAN_DAI_DAN || type1 == CardsType.SAN_ZHANG || type1 == CardsType.SAN_DAI_DUI) {
                        int n = findMaxNum(o1);
                        if (n > 10) {
                            num1 = -n;
                        }
                    }

                    if (type2 == CardsType.DUI) {
                        int n = findMaxNum(o2);
                        if (n > 10) {
                            num2 = -n + 1;
                        }
                    }
                    if (type1 == CardsType.DUI) {
                        int n = findMaxNum(o1);
                        if (n > 10) {
                            num1 = -n + 1;
                        }
                    }
                    int num = num2 - num1;
                    if (num == 0) {
                        num = o2.size() - o1.size();
                    }
                    return num;
                }
            });
            result.left.addAll(shuangwang);
            return result;
        } else {
            return null;
        }
    }

    private static Entity solution9(List<Card> cards) {

        // 处理顺子
        List<List<Card>> out1 = CardsTypeFinderType.findTypeCards(cards, CardsType.SI_DAI_DAN);
        Collections.sort(out1, new Comparator<List<Card>>() {

            @Override
            public int compare(List<Card> o1, List<Card> o2) {
                return o2.size() - o1.size();
            }
        });
        int size = out1.size();
        List<Entity> result1 = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            List<List<Card>> result = new ArrayList<>();
            List<Card> tmp = new ArrayList<>();
            tmp.addAll(cards);
            List<Card> out2 = out1.get(i);
            result.add(out2);
            tmp.removeAll(out2);
            List<List<Card>> out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DAN);
            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DAN);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DUI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DAN);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DAN);
            }

            // 处理飞机
            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SHUN);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SHUN);
            }

            // 处理连对
            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.LIAN_DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.LIAN_DUI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.DUI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DUI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DUI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DAN);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DAN);
            }
            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_ZHANG);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_ZHANG);
            }
            Entity entity = new Entity(tmp, result);
            result1.add(entity);
        }
        // 比较
        Collections.sort(result1, new Comparator<Entity>() {

            @Override
            public int compare(Entity o1, Entity o2) {
                int num = o1.getLeft().size() - o2.getLeft().size();
                if (num == 0) {
                    List<List<Card>> out = CardsTypeFinderType.findTypeCards(o1.getLeft(), CardsType.DAN);
                    List<List<Card>> out1 = CardsTypeFinderType.findTypeCards(o2.getLeft(), CardsType.DAN);
                    num = out.size() - out1.size();
                }
                return num;
            }

        });
        if (!result1.isEmpty()) {
            return result1.get(0);
        } else {
            return null;
        }

    }

    private static Entity solution8(List<Card> cards) {
        // 处理顺子
        List<List<Card>> out1 = CardsTypeFinderType.findTypeCards(cards, CardsType.DUI);
        Collections.sort(out1, new Comparator<List<Card>>() {

            @Override
            public int compare(List<Card> o1, List<Card> o2) {
                return o2.size() - o1.size();
            }
        });
        int size = out1.size();
        List<Entity> result1 = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            List<List<Card>> result = new ArrayList<>();
            List<Card> tmp = new ArrayList<>();
            tmp.addAll(cards);
            List<Card> out2 = out1.get(i);
            result.add(out2);
            tmp.removeAll(out2);
            List<List<Card>> out = CardsTypeFinderType.findTypeCards(tmp, CardsType.DUI);
            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.DUI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DUI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DAN);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DAN);
            }

            // 处理飞机
            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SHUN);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SHUN);
            }

            // 处理连对
            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.LIAN_DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.LIAN_DUI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DAN);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DAN);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DUI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DUI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DAN);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DAN);
            }
            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_ZHANG);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_ZHANG);
            }
            Entity entity = new Entity(tmp, result);
            result1.add(entity);
        }
        // 比较
        Collections.sort(result1, new Comparator<Entity>() {

            @Override
            public int compare(Entity o1, Entity o2) {
                int num = o1.getLeft().size() - o2.getLeft().size();
                if (num == 0) {
                    List<List<Card>> out = CardsTypeFinderType.findTypeCards(o1.getLeft(), CardsType.DAN);
                    List<List<Card>> out1 = CardsTypeFinderType.findTypeCards(o2.getLeft(), CardsType.DAN);
                    num = out.size() - out1.size();
                }
                return num;
            }

        });
        if (!result1.isEmpty()) {
            return result1.get(0);
        } else {
            return null;
        }
    }

    private static Entity solution7(List<Card> cards) {

        // 处理连对
        List<List<Card>> out1 = CardsTypeFinderType.findTypeCards(cards, CardsType.SAN_ZHANG);
        Collections.sort(out1, new Comparator<List<Card>>() {

            @Override
            public int compare(List<Card> o1, List<Card> o2) {
                return o2.size() - o1.size();
            }
        });
        int size = out1.size();
        List<Entity> result1 = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            List<List<Card>> result = new ArrayList<>();
            List<Card> tmp = new ArrayList<>();
            tmp.addAll(cards);
            List<Card> out2 = out1.get(i);
            result.add(out2);
            tmp.removeAll(out2);
            List<List<Card>> out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_ZHANG);
            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_ZHANG);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DUI);
            }

            // 处理飞机
            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DAN);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DAN);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI);
            }

            // 处理顺子
            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SHUN);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SHUN);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.LIAN_DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.LIAN_DUI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DAN);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DAN);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DUI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DAN);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DAN);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DUI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.DUI);
            }
            Entity entity = new Entity(tmp, result);
            result1.add(entity);
        }
        // 比较
        Collections.sort(result1, new Comparator<Entity>() {

            @Override
            public int compare(Entity o1, Entity o2) {
                int num = o1.getLeft().size() - o2.getLeft().size();
                if (num == 0) {
                    List<List<Card>> out = CardsTypeFinderType.findTypeCards(o1.getLeft(), CardsType.DAN);
                    List<List<Card>> out1 = CardsTypeFinderType.findTypeCards(o2.getLeft(), CardsType.DAN);
                    num = out.size() - out1.size();
                }
                return num;
            }

        });
        if (!result1.isEmpty()) {
            return result1.get(0);
        } else {
            return null;
        }

    }

    private static Entity solution6(List<Card> cards) {

        // 处理连对
        List<List<Card>> out1 = CardsTypeFinderType.findTypeCards(cards, CardsType.SAN_DAI_DAN);
        Collections.sort(out1, new Comparator<List<Card>>() {

            @Override
            public int compare(List<Card> o1, List<Card> o2) {
                return o2.size() - o1.size();
            }
        });
        int size = out1.size();
        List<Entity> result1 = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            List<List<Card>> result = new ArrayList<>();
            List<Card> tmp = new ArrayList<>();
            tmp.addAll(cards);
            List<Card> out2 = out1.get(i);
            result.add(out2);
            tmp.removeAll(out2);
            List<List<Card>> out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DAN);
            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DAN);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DUI);
            }

            // 处理飞机
            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DAN);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DAN);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI);
            }

            // 处理顺子
            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SHUN);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SHUN);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.LIAN_DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.LIAN_DUI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DAN);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DAN);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DUI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DUI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_ZHANG);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_ZHANG);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.DUI);
            }
            Entity entity = new Entity(tmp, result);
            result1.add(entity);
        }
        // 比较
        Collections.sort(result1, new Comparator<Entity>() {

            @Override
            public int compare(Entity o1, Entity o2) {
                int num = o1.getLeft().size() - o2.getLeft().size();
                if (num == 0) {
                    List<List<Card>> out = CardsTypeFinderType.findTypeCards(o1.getLeft(), CardsType.DAN);
                    List<List<Card>> out1 = CardsTypeFinderType.findTypeCards(o2.getLeft(), CardsType.DAN);
                    num = out.size() - out1.size();
                }
                return num;
            }

        });
        if (!result1.isEmpty()) {
            return result1.get(0);
        } else {
            return null;
        }

    }

    private static Entity solution5(List<Card> cards) {

        // 处理连对
        List<List<Card>> out1 = CardsTypeFinderType.findTypeCards(cards, CardsType.SAN_DAI_DUI);
        Collections.sort(out1, new Comparator<List<Card>>() {

            @Override
            public int compare(List<Card> o1, List<Card> o2) {
                return o2.size() - o1.size();
            }
        });
        int size = out1.size();
        List<Entity> result1 = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            List<List<Card>> result = new ArrayList<>();
            List<Card> tmp = new ArrayList<>();
            tmp.addAll(cards);
            List<Card> out2 = out1.get(i);
            result.add(out2);
            tmp.removeAll(out2);
            List<List<Card>> out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DUI);
            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DUI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DUI);
            }

            // 处理飞机
            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DAN);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DAN);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI);
            }

            // 处理顺子
            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SHUN);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SHUN);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.LIAN_DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.LIAN_DUI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DAN);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DAN);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DUI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DAN);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DAN);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_ZHANG);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_ZHANG);
            }
            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.DUI);
            }
            Entity entity = new Entity(tmp, result);
            result1.add(entity);
        }
        // 比较
        Collections.sort(result1, new Comparator<Entity>() {

            @Override
            public int compare(Entity o1, Entity o2) {
                int num = o1.getLeft().size() - o2.getLeft().size();
                if (num == 0) {
                    List<List<Card>> out = CardsTypeFinderType.findTypeCards(o1.getLeft(), CardsType.DAN);
                    List<List<Card>> out1 = CardsTypeFinderType.findTypeCards(o2.getLeft(), CardsType.DAN);
                    num = out.size() - out1.size();
                }
                return num;
            }

        });
        if (!result1.isEmpty()) {
            return result1.get(0);
        } else {
            return null;
        }

    }

    private static Entity solution4(List<Card> cards) {

        // 处理连对
        List<List<Card>> out1 = CardsTypeFinderType.findTypeCards(cards, CardsType.FEI_JI);
        Collections.sort(out1, new Comparator<List<Card>>() {

            @Override
            public int compare(List<Card> o1, List<Card> o2) {
                return o2.size() - o1.size();
            }
        });
        int size = out1.size();
        List<Entity> result1 = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            List<List<Card>> result = new ArrayList<>();
            List<Card> tmp = new ArrayList<>();
            tmp.addAll(cards);
            List<Card> out2 = out1.get(i);
            result.add(out2);
            tmp.removeAll(out2);
            List<List<Card>> out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI);
            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DUI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DAN);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DAN);
            }
            // 处理顺子
            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SHUN);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SHUN);
            }
            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.LIAN_DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.LIAN_DUI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DAN);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DAN);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DUI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DUI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DAN);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DAN);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_ZHANG);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_ZHANG);
            }
            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.DUI);
            }
            Entity entity = new Entity(tmp, result);
            result1.add(entity);
        }
        // 比较
        Collections.sort(result1, new Comparator<Entity>() {

            @Override
            public int compare(Entity o1, Entity o2) {
                int num = o1.getLeft().size() - o2.getLeft().size();
                if (num == 0) {
                    List<List<Card>> out = CardsTypeFinderType.findTypeCards(o1.getLeft(), CardsType.DAN);
                    List<List<Card>> out1 = CardsTypeFinderType.findTypeCards(o2.getLeft(), CardsType.DAN);
                    num = out.size() - out1.size();
                }
                return num;
            }

        });
        if (!result1.isEmpty()) {
            return result1.get(0);
        } else {
            return null;
        }

    }

    private static Entity solution3(List<Card> cards) {

        // 处理连对
        List<List<Card>> out1 = CardsTypeFinderType.findTypeCards(cards, CardsType.FEI_JI_DAI_DAN);
        Collections.sort(out1, new Comparator<List<Card>>() {

            @Override
            public int compare(List<Card> o1, List<Card> o2) {
                return o2.size() - o1.size();
            }
        });
        int size = out1.size();
        List<Entity> result1 = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            List<List<Card>> result = new ArrayList<>();
            List<Card> tmp = new ArrayList<>();
            tmp.addAll(cards);
            List<Card> out2 = out1.get(i);
            result.add(out2);
            tmp.removeAll(out2);
            List<List<Card>> out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DAN);
            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DAN);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DUI);
            }

            // 处理飞机
            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI);
            }

            // 处理顺子
            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SHUN);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SHUN);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.LIAN_DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.LIAN_DUI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DAN);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DAN);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DUI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DUI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DAN);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DAN);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_ZHANG);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_ZHANG);
            }
            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.DUI);
            }
            Entity entity = new Entity(tmp, result);
            result1.add(entity);
        }
        // 比较
        Collections.sort(result1, new Comparator<Entity>() {

            @Override
            public int compare(Entity o1, Entity o2) {
                int num = o1.getLeft().size() - o2.getLeft().size();
                if (num == 0) {
                    List<List<Card>> out = CardsTypeFinderType.findTypeCards(o1.getLeft(), CardsType.DAN);
                    List<List<Card>> out1 = CardsTypeFinderType.findTypeCards(o2.getLeft(), CardsType.DAN);
                    num = out.size() - out1.size();
                }
                return num;
            }

        });
        if (!result1.isEmpty()) {
            return result1.get(0);
        } else {
            return null;
        }

    }

    private static Entity solution2(List<Card> cards) {
        // 处理连对
        List<List<Card>> out1 = CardsTypeFinderType.findTypeCards(cards, CardsType.FEI_JI_DAI_DUI);
        Collections.sort(out1, new Comparator<List<Card>>() {

            @Override
            public int compare(List<Card> o1, List<Card> o2) {
                return o2.size() - o1.size();
            }
        });
        int size = out1.size();
        List<Entity> result1 = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            List<List<Card>> result = new ArrayList<>();
            List<Card> tmp = new ArrayList<>();
            tmp.addAll(cards);
            List<Card> out2 = out1.get(i);
            result.add(out2);
            tmp.removeAll(out2);
            List<List<Card>> out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DUI);
            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DUI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DAN);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI_DAI_DAN);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.FEI_JI);
            }

            // 处理顺子
            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SHUN);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SHUN);
            }

            // 处理飞机
            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.LIAN_DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.LIAN_DUI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DAN);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DAN);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SI_DAI_DUI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DUI);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DAN);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_DAI_DAN);
            }

            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_ZHANG);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.SAN_ZHANG);
            }
            out = CardsTypeFinderType.findTypeCards(tmp, CardsType.DUI);
            Collections.sort(out, new Comparator<List<Card>>() {

                @Override
                public int compare(List<Card> o1, List<Card> o2) {
                    return o2.size() - o1.size();
                }
            });

            while (!out.isEmpty()) {
                List<Card> list = out.get(0);
                result.add(list);
                tmp.removeAll(list);
                out = CardsTypeFinderType.findTypeCards(tmp, CardsType.DUI);
            }
            Entity entity = new Entity(tmp, result);
            result1.add(entity);
        }
        // 比较
        Collections.sort(result1, new Comparator<Entity>() {

            @Override
            public int compare(Entity o1, Entity o2) {
                int num = o1.getLeft().size() - o2.getLeft().size();
                if (num == 0) {
                    List<List<Card>> out = CardsTypeFinderType.findTypeCards(o1.getLeft(), CardsType.DAN);
                    List<List<Card>> out1 = CardsTypeFinderType.findTypeCards(o2.getLeft(), CardsType.DAN);
                    num = out.size() - out1.size();
                }
                return num;
            }

        });
        if (!result1.isEmpty()) {
            return result1.get(0);
        } else {
            return null;
        }

    }

    public static void main(String[] args) {
        List<Card> cards = new ArrayList<>();
//		cards.add(Card.DW);
//		cards.add(Card.XW);
//		cards.add(Card.HONG_TAO_ER);
//		cards.add(Card.FANG_KUAI_ER);
//		cards.add(Card.HEI_TAO_ER);
//		cards.add(Card.MEI_HUA_ER);
//		cards.add(Card.HONG_TAO_A);

//		cards.add(Card.HONG_TAO_Q);
//		cards.add(Card.FANG_KUAI_Q);
//		cards.add(Card.HEI_TAO_Q);
//		cards.add(Card.MEI_HUA_Q);
//		cards.add(Card.FANG_KUAI_J);
//		cards.add(Card.HEI_TAO_J);
//		cards.add(Card.MEI_HUA_J);
        cards.add(Card.HONG_TAO_J);
//		cards.add(Card.HONG_TAO_SHI);
//		cards.add(Card.HONG_TAO_JIU);
//		cards.add(Card.HONG_TAO_BA);
//		cards.add(Card.HONG_TAO_QI);
        cards.add(Card.HEI_TAO_J);
        cards.add(Card.HEI_TAO_SHI);
        cards.add(Card.HEI_TAO_JIU);
        cards.add(Card.HEI_TAO_BA);
        cards.add(Card.HEI_TAO_QI);
//		cards.add(Card.HEI_TAO_WU);
//		cards.add(Card.MEI_HUA_WU);
        List<Card> cards1 = new ArrayList<>();
        cards1.add(Card.HONG_TAO_JIU);
        cards1.add(Card.HONG_TAO_BA);
        cards1.add(Card.HONG_TAO_QI);

        Entity entity = optimumSolution(cards);
        System.out.println(entity);

    }

    public static int randInt(int i) {
        Random r = new Random();
        return r.nextInt(i);
    }
}
