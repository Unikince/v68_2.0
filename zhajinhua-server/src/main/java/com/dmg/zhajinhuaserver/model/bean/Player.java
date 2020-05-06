package com.dmg.zhajinhuaserver.model.bean;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Data
public class Player implements Serializable {
    protected long roleId;
    protected String userCode;
    protected boolean isActive;
    protected transient long loginTime;
    protected transient String openId;
    protected String nickname;
    protected int sex;
    protected String headImgUrl;
    protected int roomId; // 当前加入的房间
    protected int platform; // 渠道
    protected transient long lastActiveTime; //
    protected String ip;
    protected String provice; // 省份
    protected String city; // 市
    protected List<Integer> roomList = new ArrayList<>(); // 玩家代开的房间列表
    protected long disconnectTime;
    protected int leaveReason = -1; // 离开房间原因
    protected int bloodNum;  //血拼次数
    protected double gold; // 游戏币
    protected int masonry; // 钻石;
    protected long changeRoomTime;//快速换房时间
    protected Map<Integer, Object> recordMap = new HashMap<>();
    protected boolean firstGoodCards;//起手好牌
    protected BigDecimal winLostGold = new BigDecimal("0");//输赢金币
    protected int playRounds;//当前房间玩牌局数
    protected String cheatHandPoker = ""; // 作弊手牌
    //进入房间时间戳
    private String joinRoomTimeStamp;


}
