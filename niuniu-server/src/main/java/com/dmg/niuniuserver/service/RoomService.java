package com.dmg.niuniuserver.service;

import com.alibaba.fastjson.JSONObject;
import com.dmg.niuniuserver.model.bean.GameRoom;
import com.dmg.niuniuserver.model.bean.Player;
import com.dmg.niuniuserver.model.bean.Seat;
import com.dmg.niuniuserver.model.dto.RoomInfoDTO;
import com.dmg.niuniuserver.model.dto.SeatMsgDTO;

import java.util.List;

/**
 * @author hexf
 * @Date 2018年3月13日
 * @Desc
 */
public interface RoomService {

    /**
     * @description: 获取房间信息
     * @param room
     * @param seats
     * @return RoomInfoDTO
     * @author mice
     * @date 2019/7/4
    */
    RoomInfoDTO getRoomInfo(GameRoom room, Seat seats);

    /**
     * @description: 获取房间当前状态
     * @param room
     * @param seat
     * @return java.lang.Integer
     * @author mice
     * @date 2019/7/4
    */
    Integer getActionType(GameRoom room, Seat seat);

    /**
     * 获取玩家座位号
     *
     * @param room
     * @param player
     * @return
     */
    int getPlayerSeatId(GameRoom room, Player player);

    /**
     * @description: 解散房间
     * @param roomId
     * @return void
     * @author mice
     * @date 2019/7/3
    */
    void solveRoom(int roomId);

    /**
     * @description: 发牌消息推送
     * @param seat
     * @param count
     * @return void
     * @author mice
     * @date 2019/7/2
    */
    void pushDealMSG(List<Seat> seats, Seat seat, int count);

    void resultAll(GameRoom room);

    /**
     * @description: 获取座位信息
     * @param room
     * @param seatId
     * @return SeatMsgDTO
     * @author mice
     * @date 2019/7/4
    */
    SeatMsgDTO getSeatInfo(GameRoom room, int seatId);

    /**
     * @description: 进入下一步操作,该操作为发牌
     * @param room
     * @return void
     * @author mice
     * @date 2019/7/4
    */
    void enterDeal(GameRoom room);

    /**
     * 玩家退出房间
     * @param player 玩家信息
     * @param bool 退出房间来源是否为解散
     * @return
     */
    boolean quitRoom(Player player, boolean bool);

    /**
     * 处理玩家投票同意/拒绝解散房间
     *
     * @param player
     * @param agree
     */
    void answerDissolveRoom(Player player, boolean agree);

    /**
     * 聊天
     *
     * @param player
     * @param message
     */
    void chat(Player player, JSONObject message);


    void definitionBanker(GameRoom room, Seat seat);

    /**
     * 结算
     *
     * @return void
     * @author CharlesLee
     * @date 2018/4/17 0017 14:35
     */
    void settlement(int roomId);

    /**
     * @description: 根据roomId获取房间
     * @param roomId
     * @return GameRoom
     * @author mice
     * @date 2019/7/4
    */
    GameRoom getRoom(int roomId);

    /**
     * @description: 处理牌局结束
     * @param room
     * @return void
     * @author mice
     * @date 2019/7/4
     */
    void handleRoundEnd(GameRoom room);

    void startGameSeatStatus(GameRoom room);

   /**
    * @description: 检查是否进入发牌阶段
    * @param room
    * @return void
    * @author mice
    * @date 2019/7/4
   */
    void canEnterDeal(GameRoom room);


}
