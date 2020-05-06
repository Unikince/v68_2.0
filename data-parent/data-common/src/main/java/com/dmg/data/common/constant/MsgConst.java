package com.dmg.data.common.constant;

/**
 * RPC消息常量
 */
public interface MsgConst {
    /** 心跳 */
    String PING = "ping";
    /** 登录 */
    String LOGIN = "login";

    /** 获取玩家信息 */
    String getUser = "getUser";
    /** 下注 */
    String bet = "bet";
    /** 结算 */
    String settle = "settle";
    /** 同步房间 */
    String syncRoom = "syncRoom";

    /** 金币充值提款 */
    String goldPay = "goldPay";

}
