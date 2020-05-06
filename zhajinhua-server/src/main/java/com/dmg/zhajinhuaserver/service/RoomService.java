package com.dmg.zhajinhuaserver.service;
import com.dmg.zhajinhuaserver.model.bean.GameRoom;
import com.dmg.zhajinhuaserver.model.bean.Player;
import com.dmg.zhajinhuaserver.model.bean.Seat;

import java.util.List;
import java.util.Map;

/**
 * @author hexf
 * @Date 2018年3月13日
 * @Desc
 */
public interface RoomService {
    /**
     * 获取房间
     *
     * @param roomId
     * @return
     */
     GameRoom getRoom(int roomId);
     /**
      * @description: 处理牌局结束
      * @param room
      * @param bool
      * @return void
      * @author mice
      * @date 2019/7/11
     */
    void handleRoundEnd(GameRoom room, boolean bool);

    /**
     * 钱不够弃牌
     * @param oper
     * @param room
     * @param seat
     * @return
     */
     int noMoneyDiscard(int oper, GameRoom room, Seat seat, long wager);
    /**
     * 看牌操作不计入轮数计算
     * @param room
     */
     void addTurns(GameRoom room);
    /**
     * 获得能行动玩家数量
     * @param room
     * @return
     */
     int getCanActionCout(GameRoom room);
    /**
     * 玩家看牌操作
     * @param room
     * @param seatId
     * @param seat
     */
    List<Integer> seeOwenrCards(GameRoom room, int seatId, Seat seat);
    /**
     * 获得血拼数据
     * @param player
     * @param room
     * @param map
     */
    void getRushMsg(Player player, GameRoom room, Map<String, Object> map);
    /**
     * 玩家加注，跟注操作
     * @param room
     * @param seat
     * @param seatWager
     */
    void playerBetChips(GameRoom room, Seat seat, long seatWager,int oper);
    /**
     * 玩家进行比牌操作
     * @param room
     * @param seat
     * @param seatWager
     */
    int playerCompareCards(GameRoom room, Seat seat, Seat pkseat,long seatWager,boolean win,int oper);
    /**
     * 剩余一个玩家进入结算
     * @param room
     * @return
     */
    boolean resultByOnlyOne(GameRoom room);
    /**
     * 设置下一个出牌的座位
     *
     * @param room
     */
    void setNextPlaySeat(GameRoom room);

    int getNextSeat(GameRoom room);

    /**
     * 当前轮数超过最大轮数限制
     * @param room
     */
    void turnsOverMax(GameRoom room);
    /**
     * 发送玩家基本动作数据
     *
     * @param room
     * @param bankSeat
     */
    void sendPlayerBaseAction(GameRoom room, int bankSeat);
    /**
     * 判断是否是断线重连
     *
     * @param player
     * @param roomId
     * @return
     */
    boolean checkRejoinRoom(Player player, int roomId);
    /**
     * 断线重连
     *
     * @param player
     */
    void rejionRoom(Player player);
    /**
     * 房间信息
     *
     * @param room
     * @return
     */
    Map<String, Object> roomMsg(GameRoom room,Seat data);

    void solveRoom(int roomId);
    /**
     * 删除房卡房
     * @param room
     */
    void deletePrivateRoom(GameRoom room);
    /**
     * 成功解散房间
     * @param room
     */
    void dissolveRoomSuccess(GameRoom room);

    /**
     * @description: 离线处理
     * @param player
     * @return void
     * @author mice
     * @date 2019/7/13
    */
    void disconnect(Player player);

    /**
     * @description: 设置庄家位置
     * @param room
     * @return void
     * @author mice
     * @date 2019/7/10
     */
    void setBanker(GameRoom room);

    /**
     * @description: 发牌
     * @param room
     * @return void
     * @author mice
     * @date 2019/7/10
     */
    void distributePoker(GameRoom room);

    /**
     * @description: 返回座位信息
     * @param room
     * @param seatId
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @author mice
     * @date 2019/7/10
     */
    Map<String,Object> sendSeatMsg(GameRoom room, int seatId);

    /**
     * @description: 返回玩家信息
     * @param player
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @author mice
     * @date 2019/7/10
     */
    Map<String,Object> sendPlayerMsg(Player player);

    /**
     * @description: 获取玩家座位号
     * @param room
     * @param player
     * @return int
     * @author mice
     * @date 2019/7/10
     */
    int getPlaySeat(GameRoom room, Player player);

    /**
     * @description: 获取玩家座位
     * @param room
     * @param player
     * @return com.dmg.zhajinhuaserver.model.bean.Seat
     * @author mice
     * @date 2019/7/10
     */
    Seat getSeat(GameRoom room, Player player);

    /**
     * 操作下注
     * @param player 玩家信息
     * @param beSeatId 比牌座位
     * @param oper 下注动作PokerOperState
     * @param wager 加注倍数
     *//*
    void handleBet(Player player, int beSeatId, int oper, long wager);*/

    /**
     * @description: 处理房间结束
     * @param room
     * @param noPlay
     * @return void
     * @author mice
     * @date 2019/7/11
    */
    void handRoomEnd(GameRoom room, boolean noPlay);

}
