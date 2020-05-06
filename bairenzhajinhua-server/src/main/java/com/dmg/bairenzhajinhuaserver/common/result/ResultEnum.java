package com.dmg.bairenzhajinhuaserver.common.result;


/**
 * @Author: ChenHao
 * @Date: 2018/5/22 17:21
 */
public enum ResultEnum {
    PARAM_ERROR(1000,"参数错误"),
    SYSTEM_EXCEPTION(1001,"系统异常"),
    SYSTEM_INIT_ERROR(1002,"系统启动异常"),
    GET_USERINFO_FAIL(1003,"获取用户信息失败"),
    ACCOUNT_NOT_EXIST(1006,"账号不存在"),
    CANNOT_START_GAME(2000,"不能开始游戏"),
    ROOM_NO_EXIST(2001,"房间不存在"),
    SEAT_BE_USE(2002,"座位被抢占"),
    ROOM_NO_SEAT(2003,"房间已满"),
    ROOM_HAS_STARTED(2004,"房间已经开始游戏"),
    PLAYER_HAS_NO_SEAT(2005,"玩家不在座位上"),
    PLAYER_HAS_NO_MONEY(2006,"金币不足"),
    PLAYER_HAS_NOT_INROOM(2007,"玩家不在房间里"),
    PLAYER_ASK_WRONG(2008,"游戏开始无法退出"),
    PLAYER_ASK_TOO_FAST(2009,"请求过于频繁"),
    PLAYER_HAS_PLAYING(2010,"玩家已经开始游戏"),
    ROOM_IS_GAME(2011,"房间在游戏中"),
    ROOM_HAS_FULL(2012,"房间已满"),
    PLAYER_HAS_ACTION_ERROR(2013,"当前动作无效"),
    PLAYER_HAS_NO_CARDS(2014,"玩家无手牌"),
    ROOM_HAS_EMPTY(2015,"暂无可用房间"),
    GOLD_NEED_10000(2016,"上庄金额不足"),
    NO_BET_RECORD(2017,"没有上局押注记录"),
    HAS_APPLY_SHANGZHUANG(2018,"你已在上庄列表中"),
    OVER_ROOM_BET_TOTAL(2019,"超过当前房间可下注总额"),
    BANKER_PLAYER_QUIT_WRONG(2020,"庄家不能退出游戏"), 
    MONEY_ENOUGH_CAN_BET(2021,"金币以上才可以玩游戏!"), 
    OVER_BET_LIMIT(2022,"余额或预扣金不足，请补充余额"),
    WITH_SHANGZHUANG(2023,"你当前为庄家，请下庄之后再试"),
    AREA_UP_LIMIT(2024,"已达到区域下注上限"),
    AREA_LOW_LIMIT(2025,"未达到区域下注下限"),
    SUCCESS(1,"成功");
    private Integer code;
    private String msg;
    ResultEnum(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }
    public Integer getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
}
