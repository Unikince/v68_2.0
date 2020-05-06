package com.dmg.niuniuserver.result;

/**
 * @Author: ChenHao
 * @Date: 2018/5/22 17:21
 */
public enum ResultEnum {

    UNKONW_ERROR(-1,"未知错误"),
    LESS_PARAMS(1001,"传入参数不全"),
    PARAM_ERROR(1002,"参数错误"),
    CODE_EXIST(1003,"编码已存在"),
    SYSTEM_EXCEPTION(1004,"系统异常"),
    SQL_EXCEPTION(1005,"数据库异常"),
    ACCOUNT_NOT_EXIST(1006,"账号不存在"),
    ACCOUNT_ERROR(1007,"账号或密码错误"),
    VALIDATE_CODE_ERROR(1008,"验证码错误"),
    VALIDATE_CODE_TIME_OUT(1009,"验证码已过期"),

    BUSINESS_EXCEPTION(2000,"业务异常"),
    GET_USERINFO_FAIL(2001,"获取用户信息失败"),
    ROOM_HAS_NO_EXIST(2002,"房间不存在"),
    NOT_ON_SEAT(2003,"不在座位上"),
    PLAYER_HAS_NO_MONEY(2004,"金币不足"),
    PLAYER_HAS_NOT_INROOM(2005,"玩家不在房间里"),
    PLAYER_HAS_NOT_SEAT(2006,"玩家不在椅子上"),
    ROOM_NO_SEAT(2007,"房间已满"),
    ROOM_HAS_STARTED(2008,"房间已经开始游戏"),
    REPEAT_BET(2009,"重复下注"),
    PLAYER_HAS_PLAYING(2010,"玩家正在游戏中"),
    ROOM_HAS_EMPTY(2011,"当前没有空余房间"),
    NO_PERMISSIONS(2012,"玩家无权限进行此操作"),
    PEOPLE_NO_REGIST(2013,"该玩家已经正式注册帐号，无法使用游客进行登录"),

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
