package com.dmg.bjlserver.business.model.room;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import com.dmg.bjlserver.business.model.Constant;
import com.dmg.bjlserver.business.model.game.Card;
import com.dmg.bjlserver.business.model.game.GameStage;
import com.dmg.bjlserver.business.model.game.TableRecord;
import com.dmg.common.pb.java.Bjl;
import com.google.common.collect.EvictingQueue;

/**
 * 场次
 */
public class Table {
    /** 场次id */
    public final int id;
    /** 场次名称 */
    public final String name;
    /** 倍数规则 */
    public final List<Integer> multiples;
    /** 系统位置 */
    public final Seat sysSeat;
    /** 当前庄家 */
    public Seat banker;
    /** 当前阶段 */
    public GameStage stage = GameStage.REST;
    /** 场次里面的位置(order:0) */
    public final List<Seat> seats;
    /** 场次区域下注信息 */
    public long[] areaBets = new long[Bjl.BetZoneType.ZONE_MAX.getNumber()];
    /** 场次区域真实玩家下注信息 */
    public long[] areaSeatBets = new long[Bjl.BetZoneType.ZONE_MAX.getNumber()];
    /** 庄家牌 */
    public List<Card> bankerCards = new ArrayList<>();
    /** 闲家牌 */
    public List<Card> playerCards = new ArrayList<>();
    public Bjl.BetZoneType winner;
    /** 合计下注 */
    public long totalBet;
    /** 一副牌 */
    public EvictingQueue<Card> cards;
    /** 输赢路线,固定大小HISTORY_SIZE */
    public final EvictingQueue<Bjl.RouteInfo> routes = EvictingQueue.create(Constant.HISTORY_SIZE);
    /** 玩家排名,休息的时候刷新 */
    public List<Seat> rankSeats;
    /** 无座玩家,休息的时候刷新 */
    public final List<Seat> otherSeats = new ArrayList<>();
    /** 游戏阶段(下注、游戏(含结算))定时Future */
    public ScheduledFuture<?> stageFuture = null;
    /** 通知场次下注金币Future */
    public ScheduledFuture<?> noticeGoldFuture = null;
    /** 第几局 */
    public long round = System.currentTimeMillis();
    /** 开始时间 */
    public Date startTime;
    /** 牌局明细 */
    public TableRecord brnnTableRecord;
    /** 是否有人下注 */
    public boolean isBet = false;
    /** 无座下注筹码 */
    public Map<Integer, List<Long>> noSeatCard = new ConcurrentHashMap<>();
    /** 无座下注人员ID */
    public Map<Integer, List<Long>> noSeatPlayerId = new ConcurrentHashMap<>();
    /** 天地合下注筹码 最新30条 用于用户加入房间恢复数据用 */
    public Map<Integer, List<Long>> betGoldCard = new ConcurrentHashMap<>();
    /** 没个区域的每个玩家的下注信息 */
    public Map<Integer, Map<Long, List<Long>>> playerBets = new ConcurrentHashMap<>();

    /** 赌神第一次下注位置 */
    public int godOfGamblersFirstBetTable = -1;

    // 最新的盈利榜数据
    public Bjl.ResProfitList.Builder lastProfitList = null;
    // 盈利榜数据有变更
    public boolean profitListChanged = true;

    public Table(int id, String name, List<Integer> multiples, int playerNum) {
        this.id = id;
        this.name = name;
        this.multiples = multiples;

        this.brnnTableRecord = new TableRecord();
        this.sysSeat = new Seat(-1, this, 0, "系统坐庄");
        this.sysSeat.robot = true;
        this.setBanker(this.sysSeat);
        this.seats = this.createSeats(playerNum);
        this.rankSeats = this.createRankSeats(Constant.RANK_SEAT_COUNT);
        this.shuffle();
        this.initParameter();
    }

    /**
     * 初始化房间参数
     */
    private void initParameter() {
        for (int i = 0; i < Bjl.BetZoneType.ZONE_MAX.getNumber(); i++) {
            this.noSeatCard.put(i, new ArrayList<>());
            this.betGoldCard.put(i, new ArrayList<>());
            this.noSeatPlayerId.put(i, new ArrayList<>());
            this.playerBets.put(i, new ConcurrentHashMap<>());
        }
    }

    /**
     * 创建位置
     *
     * @param num
     * @return
     */
    private List<Seat> createSeats(int num) {
        List<Seat> seats = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            seats.add(new Seat(i, this));
        }

        return seats;
    }

    /**
     * 初始化桌上座位
     */
    private List<Seat> createRankSeats(int num) {
        List<Seat> rankSeats = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            rankSeats.add(i, null);
        }
        return rankSeats;
    }

    /**
     * 玩家下座位
     */
    public void clearRankSeat(Seat seat) {
        for (int i = 0; i < this.rankSeats.size(); i++) {
            Seat seat1 = this.rankSeats.get(i);
            if (seat1 == seat) {
                this.rankSeats.set(i, null);
            }
        }
    }

    /**
     * reset场次
     */
    public void reset() {
        this.totalBet = 0;
        this.areaBets = new long[Bjl.BetZoneType.ZONE_MAX.getNumber()];
        this.areaSeatBets = new long[Bjl.BetZoneType.ZONE_MAX.getNumber()];
        this.bankerCards = new ArrayList<>();
        this.playerCards = new ArrayList<>();
        this.godOfGamblersFirstBetTable = -1;
        // this.carTypeScores = new int[5];
        this.otherSeats.clear();
        if (this.stageFuture != null) {
            this.stageFuture.cancel(false);
        }
        if (this.noticeGoldFuture != null) {
            this.noticeGoldFuture.cancel(false);
        }
        this.startTime = null;
        for (int i = 0; i < Bjl.BetZoneType.ZONE_MAX.getNumber(); i++) {
            this.noSeatCard.get(i).clear();
            this.betGoldCard.get(i).clear();
            this.noSeatPlayerId.get(i).clear();
            this.playerBets.get(i).clear();
        }
    }

    /**
     * 玩家数量
     *
     * @return
     */
    public int playerNum() {
        int playerNum = 0;
        for (Seat s : this.seats) {
            if (s.playerId > 0 && s.robot == false) {
                playerNum++;
            }
        }
        return playerNum;
    }

    /**
     * 机器人总人数
     */
    public int robotNumber() {
        int number = 0;
        for (Seat seat : this.seats) {
            if ((seat.playerId > 0) && seat.robot) {
                number++;
            }
        }
        return number;
    }

    /**
     * 查找空位置,优先查找有人的场次
     *
     * @return null, 当前没有位置
     */
    public Seat findEmptySeat() {
        for (Seat seat : this.seats) {
            if (seat.playerId == 0) {
                return seat;
            }
        }

        return null;
    }

    /**
     * 是否有机器人
     */
    public boolean isTableRobot() {
        for (Seat seat : this.seats) {
            if ((seat.playerId > 0) && seat.robot) {
                return true;
            }
        }
        return false;
    }

    /**
     * 洗牌,返回切牌
     */
    public int shuffle() {
        Card[] c = Card.values();
        this.cards = EvictingQueue.create(Constant.PACK_SIZE * c.length);
        List<Card> listCards = new ArrayList<>();

        for (int i = 0; i < Constant.PACK_SIZE; i++) {
            listCards.addAll(Arrays.asList(c));
        }
        Collections.shuffle(listCards);
        // 切牌
        final int index = (int) (Math.random() * listCards.size());
        final Card cutCard = listCards.remove(index);

        this.cards.addAll(listCards);
        // 清空线路图
        this.routes.clear();

        return cutCard.id;
    }

    /**
     * 需要洗牌？
     *
     * @return
     */
    public boolean needShuffle() {
        return this.cards.size() <= Constant.SHUFFLE_LIMIT;
    }

    public void setBanker(Seat banker) {
        this.banker = banker;
        banker.notBetRound = 0;
    }

    public int[] bankerCards() {
        return this.dumpCards(this.bankerCards);
    }

    public int[] playerCards() {
        return this.dumpCards(this.playerCards);
    }

    private int[] dumpCards(List<Card> card) {
        int[] c = new int[card.size()];
        for (int i = 0; i < card.size(); i++) {
            c[i] = card.get(i).id;
        }
        return c;
    }
}
