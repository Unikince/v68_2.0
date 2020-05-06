package com.dmg.redblackwarserver.common.model;

import com.dmg.common.core.util.SpringUtil;
import com.dmg.redblackwarserver.common.result.MessageResult;
import com.dmg.redblackwarserver.service.PushService;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description: 玩家基本信息
 * @return
 * @author mice
 * @date 2019/7/29
*/
@Data
public class BasePlayer implements Serializable {
    private static final long serialVersionUID = 5171745246066095660L;
    /**
     * 玩家id
    */
    private int userId;
    /**
     * 玩家code
     */
    private String userCode;
    /**
     * 是否在线
     */
    private boolean online;
    /**
     * 玩家昵称
     */
    private String nickname;
    /**
     * 金币
     */
    private BigDecimal gold = new BigDecimal(0);
    /**
     * 性别 1:男 2:女
     */
    private int sex;
    /**
     * 头像
     */
    private String headIcon;
    /**
     * 房间id
     */
    private int roomId;
    /**
     * 座位号
     */
    private int seatIndex;
    /**
     * 掉线时间
     */
    private long disconnectTime;
    /**
     * 离开房间原因
     */
    private int leaveReason = -1;
    /**
     * 当前房间玩牌局数
     */
    private int gameRound;
    /**
     * 所在城市
     */
    private String city;

    public void push(String m,Integer res,Object msg){
        PushService service = SpringUtil.getBean(PushService.class);
        service.push(getUserId(), m, res, msg);
    }

    public void push(String m, Integer res) {
        PushService service = SpringUtil.getBean(PushService.class);
        service.push(getUserId(), m, res);
    }

    public void push(String m) {
        PushService service = SpringUtil.getBean(PushService.class);
        service.push(getUserId(), m);
    }

    public void push(MessageResult messageResult) {
        PushService service = SpringUtil.getBean(PushService.class);
        service.push(getUserId(), messageResult);
    }

}
