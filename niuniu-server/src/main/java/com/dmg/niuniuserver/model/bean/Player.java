package com.dmg.niuniuserver.model.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;
    protected Long id;
    protected Long userId;
    protected long userCode;
    protected boolean online;
    protected String nickname;
    protected int sex;
    protected String headImg;
    protected int roomId; // 当前加入的房间
    protected int platform; // 渠道
    protected transient long loginTime;
    protected long disconnectTime;
    protected int leaveReason = -1; // 离开房间原因
    protected int bloodNum; // 血拼次数
    protected double gold; // 游戏币
    protected long changeRoomTime;// 快速换房时间
    protected Map<Integer, Object> recordMap = new HashMap<>();
    protected Double winLostGold = new Double("0");// 输赢金币
    protected int playRounds;// 当前房间玩牌局数
    // 用户登录类型(0=h5-pc,1=ANDROID,2=IOS,3=h5-mobile)
    private int loginType;
    // 加入房间时间戳
    private String joinRoomTimeStamp;

    public void setId(long id) {
        this.id = id;
        this.userId = id;
    }

    public void setUserId(long userId) {
        this.id = userId;
        this.userId = userId;
    }

}
