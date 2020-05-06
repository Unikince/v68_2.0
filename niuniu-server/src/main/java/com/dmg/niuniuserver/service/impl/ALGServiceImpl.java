package com.dmg.niuniuserver.service.impl;


import com.dmg.niuniuserver.model.bean.GameRoom;
import com.dmg.niuniuserver.model.bean.Poker;
import com.dmg.niuniuserver.model.bean.Seat;
import com.dmg.niuniuserver.model.constants.Combination;
import com.dmg.niuniuserver.model.constants.GameConfig;
import com.dmg.niuniuserver.service.ALGService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author hexf
 * @Date 2018年3月13日
 * @Desc
 */
@Service
public class ALGServiceImpl implements ALGService {

    @Override
    public LinkedList<Poker> createDeck(GameRoom room) {
        LinkedList<Poker> list = new LinkedList<>();
        // 添加普通牌
        for (int value = 1; value <= 13; value++) {
            for (int type = 1; type <= 4; type++) {
                Poker poker = new Poker(value, type);
                list.add(poker);
            }
        }
        // 判断是否添加大小王, 两张牌
        if (room.isPrivateRoom() && room.getCustomRule().isWangPai()) {
            list.add(new Poker(GameConfig.KING, 0));
            list.add(new Poker(GameConfig.WANG, 0));
        }
        // 洗牌
        Collections.shuffle(list);
        return list;
    }

    @Override
    public LinkedList<Poker> createTestDeck() {
        LinkedList<Poker> list = new LinkedList<>();
        return list;
    }


    /**
     * 牌从小到大排序
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
     * 计算牌型, 先计算特殊牌型,在计算牛牛, 最后计算普通牌型
     *
     * @return int
     * @author CharlesLee
     * @date 2018/4/12 0012 16:46
     */
    @Override
    public Combination evalPokerType(List<Poker> cardList, Seat seat, GameRoom room) {
        return getOrdinaryCattle(cardList, haveKing(cardList), seat);
    }

    public int getPokerType(List<Poker> pokerList) {
        Collections.sort(pokerList);

        // 首先获取特殊牌型
        Combination combination = getSpecialType(pokerList, 0);
        if (combination != null) {
            return combination.getValue();
        }
        // 如果没有特殊牌型,将大于10的牌转换为10进行计算
        List<Poker> copyPokerList = new ArrayList<>();
        for (Poker poker : pokerList) {
            Poker copyPoker = new Poker();
            copyPoker.setType(poker.getType());
            copyPoker.setValue(poker.getValue());
            copyPokerList.add(copyPoker);
        }
        return getOrdinaryCattle(copyPokerList);
    }


    /**
     * 获取普通牛牛牌型
     *
     * @return com.dmg.niuniu.def.Config.Combination
     * @author CharlesLee
     * @date 2018/4/12 0012 18:22
     */
    private Combination getOrdinaryCattle(List<Poker> pokerList, int wangCount, Seat seat) {
        Collections.sort(pokerList);
        if (wangCount > 0) {
            return Combination.HIGH_CARD;
        }
        // 首先获取特殊牌型
        Combination combination = getSpecialType(pokerList, wangCount);
        if (combination != null) {
            return combination;
        }
        // 如果没有特殊牌型,将大于10的牌转换为10进行计算
        List<Poker> copyPokerList = new ArrayList<>();
        for (Poker poker : pokerList) {
            Poker copyPoker = new Poker();
            copyPoker.setType(poker.getType());
            copyPoker.setValue(poker.getValue());
//                if (copyPoker.getValue() > 10) {
//                    copyPoker.setValue(10);
//                }
            copyPokerList.add(copyPoker);
        }
        int type = getOrdinaryCattleByNoWang(copyPokerList);
        return getCombinationType(type, wangCount);
    }

    private Combination getSpecialType(List<Poker> list, int wangCount) {
        if (wangCount > 0) {

        } else {
            if (getWuXiaoCattle(list, wangCount) != Combination.UNDEFINE) {
                return Combination.WU_XIAO_CATTLE;
            }
            if (getBombCattle(list, wangCount) != Combination.UNDEFINE) {
                return Combination.BOMB_CATTLE;
            }
            /*if (getHuLuCattle(list, wangCount) != Combination.UNDEFINE) {
                return Combination.HU_LU_CATTLE;
            }*/
            if (getWuHuaCattle(list, wangCount) != Combination.UNDEFINE) {
                return Combination.WU_HUA_CATTLE;
            }
            if (getTongHuaCattle(list, wangCount) != Combination.UNDEFINE) {
                return Combination.TONG_HUA_CATTLE;
            }
            if (getShuenZiCattle(list, wangCount) != Combination.UNDEFINE) {
                return Combination.SUEN_ZI_CATTLE;
            }
        }
        return null;
    }

    private Combination getCombinationType(int type, int wangCount) {
        if (wangCount > 0) {
            switch (type) {
                case 1:
                    return Combination.FALSE_CATTLE_ONE;
                case 2:
                    return Combination.FALSE_CATTLE_TWO;
                case 3:
                    return Combination.FALSE_CATTLE_THREE;
                case 4:
                    return Combination.FALSE_CATTLE_FOUR;
                case 5:
                    return Combination.FALSE_CATTLE_FIVE;
                case 6:
                    return Combination.FALSE_CATTLE_SIX;
                case 7:
                    return Combination.FALSE_CATTLE_SEVEN;
                case 8:
                    return Combination.FALSE_CATTLE_EIGHT;
                case 9:
                    return Combination.FALSE_CATTLE_NINE;
                case 0:
                    return Combination.FALSE_CATTLE_CATTLE;
                case -1:
                    return Combination.HIGH_CARD;
                default:
            }
        } else {
            switch (type) {
                case 1:
                    return Combination.CATTLE_ONE;
                case 2:
                    return Combination.CATTLE_TWO;
                case 3:
                    return Combination.CATTLE_THREE;
                case 4:
                    return Combination.CATTLE_FOUR;
                case 5:
                    return Combination.CATTLE_FIVE;
                case 6:
                    return Combination.CATTLE_SIX;
                case 7:
                    return Combination.CATTLE_SEVEN;
                case 8:
                    return Combination.CATTLE_EIGHT;
                case 9:
                    return Combination.CATTLE_NINE;
                case 0:
                    return Combination.CATTLE_CATTLE;
                case -1:
                    return Combination.HIGH_CARD;
                default:
            }
        }
        return Combination.HIGH_CARD;
    }

    /**
     * 计算普通牛牛牌型
     *
     * @return int
     * @author CharlesLee
     * @date 2018/4/13 0013 09:56
     */
    private int getOrdinaryCattleByNoWang(List<Poker> pokerList) {
        List<Poker> copyPokers = new ArrayList<>();
        copyPokers.addAll(pokerList);
        int pokeListSize = pokerList.size();
        for (int i = 0; i < pokeListSize - 2; i++) {
            for (int j = i + 1; j < pokeListSize - 1; j++) {
                for (int k = j + 1; k < pokeListSize; k++) {
                    Poker pokerI = pokerList.get(i);
                    Poker pokerJ = pokerList.get(j);
                    Poker pokerK = pokerList.get(k);
                    int valueI = pokerI.getValue() > 10 ? 10 : pokerI.getValue();
                    int valueJ = pokerJ.getValue() > 10 ? 10 : pokerJ.getValue();
                    int valueK = pokerK.getValue() > 10 ? 10 : pokerK.getValue();
                    // 该牌型有牛
                    if ((valueI + valueJ + valueK) % 10 == 0) {
                        copyPokers.remove(pokerI);
                        copyPokers.remove(pokerK);
                        copyPokers.remove(pokerJ);
                        int count = 0;
                        for (Poker poker : copyPokers) {
                            count += poker.getValue() > 10 ? 10 : poker.getValue();
                        }
                        return count % 10;
                    }
                }
            }
        }
        return -1;
    }

    /**
     * 设置WholeTenCards
     *
     * @return int
     * @author CharlesLee
     * @date 2018/4/13 0013 09:56
     */
    @Override
    public void setOrdinaryCattleByNoWang(List<Poker> pokerList, Seat seat) {
        Collections.sort(pokerList);
        int wangCount = haveKing(pokerList);
        if (wangCount > 0) {
            return;
        }
        // 首先获取特殊牌型
        Combination combination = getSpecialType(pokerList, wangCount);
        if (combination != null) {
            return;
        }
        List<Poker> copyPokers = new ArrayList<>();
        copyPokers.addAll(pokerList);
        int pokeListSize = pokerList.size();
        for (int i = 0; i < pokeListSize - 2; i++) {
            for (int j = i + 1; j < pokeListSize - 1; j++) {
                for (int k = j + 1; k < pokeListSize; k++) {
                    Poker pokerI = pokerList.get(i);
                    Poker pokerJ = pokerList.get(j);
                    Poker pokerK = pokerList.get(k);
                    int valueI = pokerI.getValue() > 10 ? 10 : pokerI.getValue();
                    int valueJ = pokerJ.getValue() > 10 ? 10 : pokerJ.getValue();
                    int valueK = pokerK.getValue() > 10 ? 10 : pokerK.getValue();
                    // 该牌型有牛
                    if ((valueI + valueJ + valueK) % 10 == 0) {
                        seat.getWholeTenCards().add(pokerI);
                        seat.getWholeTenCards().add(pokerJ);
                        seat.getWholeTenCards().add(pokerK);
                        copyPokers.remove(pokerI);
                        copyPokers.remove(pokerK);
                        copyPokers.remove(pokerJ);
                        for (Poker poker : copyPokers) {
                            seat.getScatteredCards().add(poker);
                        }
                        return;
                    }
                }
            }
        }
    }

    private int getOrdinaryCattle(List<Poker> pokerList) {
        List<Poker> copyPokers = new ArrayList<>();
        copyPokers.addAll(pokerList);
        int pokeListSize = pokerList.size();
        for (int i = 0; i < pokeListSize - 2; i++) {
            for (int j = i + 1; j < pokeListSize - 1; j++) {
                for (int k = j + 1; k < pokeListSize; k++) {
                    Poker pokerI = pokerList.get(i);
                    Poker pokerJ = pokerList.get(j);
                    Poker pokerK = pokerList.get(k);
                    // 如果没有特殊牌型,将大于10的牌转换为10进行计算
                    int valueI = pokerI.getValue() > 10 ? 10 : pokerI.getValue();
                    int valueJ = pokerJ.getValue() > 10 ? 10 : pokerJ.getValue();
                    int valueK = pokerK.getValue() > 10 ? 10 : pokerK.getValue();
                    // 该牌型有牛
                    if ((valueI + valueJ + valueK) % 10 == 0) {
                        copyPokers.remove(pokerI);
                        copyPokers.remove(pokerK);
                        copyPokers.remove(pokerJ);
                        int count = 0;
                        for (Poker poker : copyPokers) {
                            count += poker.getValue() > 10 ? 10 : poker.getValue();
                        }
                        count = count % 10;
                        if (count == 0) {
                            return Combination.CATTLE_CATTLE.getValue();
                        }
                        return count;
                    }
                }
            }
        }
        return Combination.HIGH_CARD.getValue();
    }


    /**
     * 获取顺子牛
     *
     * @return com.dmg.niuniu.def.Config.Combination
     * @author CharlesLee
     * @date 2018/4/12 0012 18:19
     */
    private Combination getShuenZiCattle(List<Poker> pokerList, int wangCount) {
        if (wangCount > 0) {

        } else {
            for (int i = 0; i < pokerList.size() - 1; i++) {
                if (pokerList.get(i).getValue() + 1 != pokerList.get(i + 1).getValue()) {
                    return Combination.UNDEFINE;
                }
            }
            return Combination.SUEN_ZI_CATTLE;
        }
        return Combination.UNDEFINE;
    }

    /**
     * 获取同花牛
     *
     * @return com.dmg.niuniu.def.Config.Combination
     * @author CharlesLee
     * @date 2018/4/12 0012 18:15
     */
    private Combination getTongHuaCattle(List<Poker> pokerList, int wangCount) {
        int count = 0;
        if (wangCount > 0) {

        } else {
            for (int i = 1; i < pokerList.size(); i++) {
                if (pokerList.get(0).getType() == pokerList.get(i).getType()) {
                    count++;
                } else {
                    break;
                }
            }
            if (count == 4) {
                return Combination.TONG_HUA_CATTLE;
            }
        }
        return Combination.UNDEFINE;
    }

    /**
     * 获取五花牛
     *
     * @return com.dmg.niuniu.def.Config.Combination
     * @author CharlesLee
     * @date 2018/4/12 0012 18:09
     */
    private Combination getWuHuaCattle(List<Poker> pokerList, int wangCount) {
        int count = 0;
        if (wangCount > 0) {

        } else {
            for (Poker poker : pokerList) {
                if (poker.getValue() > 10) {
                    count++;
                }
            }
            if (count == 5) {
                return Combination.WU_HUA_CATTLE;
            }
        }
        return Combination.UNDEFINE;
    }

    /**
     * 获取葫芦牛
     *
     * @return com.dmg.niuniu.def.Config.Combination
     * @author CharlesLee
     * @date 2018/4/12 0012 17:54
     */
    private Combination getHuLuCattle(List<Poker> pokerList, int wangCount) {
        if (wangCount > 0) {

        } else {

        }
        return Combination.UNDEFINE;
    }

    /**
     * 获取炸弹牛
     *
     * @return
     * @author CharlesLee
     * @date 2018/4/12 0012 17:29
     */
    private Combination getBombCattle(List<Poker> pokerList, int wangCount) {
        //获取相同牌值数量的牌
        if (wangCount > 0) {

        } else {
            Map<Integer, List<Poker>> listMap = pokerList.stream().collect(Collectors.groupingBy(Poker::getValue));
            for (List<Poker> pokers : listMap.values()) {
                if (pokers.size() == 4) {
                    return Combination.BOMB_CATTLE;
                }
            }
        }
        return Combination.UNDEFINE;
    }

    /**
     * 获取五小牛
     *
     * @return com.dmg.niuniu.def.Config.Combination
     * @author CharlesLee
     * @date 2018/4/12 0012 17:03
     */
    private Combination getWuXiaoCattle(List<Poker> pokerList, int wangCount) {
        int count = 0; // 小于5的牌的数量
        int number = 0; // 所有牌加起来的牌值
        if (wangCount > 0) {
            pokerList = copyNoWang(pokerList);
        }
        for (Poker poker : pokerList) {
            if (poker.getValue() < 5) {
                count++;
                number += poker.getValue();
            } else {
                // 直接退出循环, 节约时间
                break;
            }
        }
        if (wangCount > 0) {
            if ((count + wangCount) == 5 && (number - wangCount) <= 10) {
                return Combination.FALSE_WU_XIAO_CATTLE;
            }
        } else {
            if (count == 5 && number <= 10) {
                return Combination.WU_XIAO_CATTLE;
            }
        }
        // 查找不到五小牛的牌型, 返回空
        return Combination.UNDEFINE;
    }


    /**
     * 判断该扑克列表中王牌的数量
     *
     * @return boolean
     * @author CharlesLee
     * @date 2018/4/12 0012 17:04
     */
    private int haveKing(List<Poker> pokerList) {
        int count = 0;
        for (Poker poker : pokerList) {
            if (poker.getValue() >= GameConfig.WANG) {
                count++;
            }
        }
        return count;
    }

    /**
     * 复制玩家手牌, 并去掉手牌中的王牌
     *
     * @return java.util.List<com.dmg.niuniu.entity.Poker>
     * @author CharlesLee
     * @date 2018/4/12 0012 17:11
     */
    private List<Poker> copyNoWang(List<Poker> list) {
        List<Poker> copyList = new ArrayList<>();
        for (Poker poker : list) {
            if (poker.getValue() < GameConfig.WANG) {
                copyList.add(poker);
            }
        }
        return copyList;
    }
}









