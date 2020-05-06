package com.dmg.zhajinhuaserver.model.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zhuqd
 * @Date 2017年8月30日
 * @Desc 房间基类，游戏中所以房间继承该类。并将房间放入RoomManager中进行管理
 */
@Data
public class Room implements Serializable {
    private static final long serialVersionUID = 1L;
    protected int roomId; // 房间id
    protected int time; // 房间总时长(毫秒)
    protected int round; // 游戏当前局数
    protected int totalRound; // 总局数
    protected long playTime; // 开始游戏时间(毫秒)
    protected long createTime; // 房间创建时间(毫秒)
    protected int playerNumber; // 房间中座位上的人数
    protected int totalPlayer; // 房间总座位数
    protected long creator; // 房间创建者id
    protected long overTime; // 房间结束时间
    protected int baseScore; //房间底分
    protected BigDecimal lowerLimit; // 当前房间最低携带金额
    protected BigDecimal upperLimit; // 当前房间最高携带金额
    protected int totalBet; // 上限,当前房间最高上限下注金额
    protected int roomType;// 房间类型(自由场,比赛场)
    protected int gameRoomTypeId;// 游戏类型ID(自由场,比赛场)
    protected int rule;//房间规则
    protected int extraRule;//房间额外规则
    protected String gameName; // 游戏名字
    protected int grade;// 当前房间等级(初,中,高...)
    protected String gradeName;//房间名称
    protected transient int serverId;
    protected int costType;//扣房卡类型
    protected boolean noWashCard;//斗地主不洗牌
    protected int gameTypeId; // 游戏ID(炸金花,牛牛)
    protected Integer pumpRate;//抽水百分比
}
