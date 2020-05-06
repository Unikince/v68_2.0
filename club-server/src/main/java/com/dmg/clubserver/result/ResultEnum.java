package com.dmg.clubserver.result;

/**
 * @Description  业务异常
 * @Author jock
 * @Date 2019/6/18 0018
 * @Version V1.0
 **/
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
    CLUB_REQUEST_BE_REFUSE(2001,"俱乐部申请被拒绝"),
    REPEAT_REQUEST(2002,"重复的申请"),
    JOINED_CLUB_LIMIT(2003,"超出加入俱乐部上限"),
    CLUB_MEMBER_LIMIT(2004,"俱乐部成员上限"),
    REQUEST_BE_DEAL(2005,"申请已被处理无法撤回"),
    NO_CLUB_OPERATE_AUTH(2006,"还没有可以管理的俱乐部"),
    REMEMBER_NO_EXIST(2007,"该成员不在俱乐部"),
    NO_AUTH(2008,"没有权限"),
    KICK_OUT_LIMIT(2009,"达到踢出上限无法加入"),
    MANAGER_LIMIT(2010,"达到管理员人数上限"),
    CLUB_NUMBER_LIMIT(2011,"达到创建俱乐部上限"),
    CLUB_NAME_EXIST(2012,"俱乐部名称已存在"),
    CLUB_TABLE_LIMIT(2013,"俱乐创建牌桌达到上限"),
    TABLE_HAS_BE_DISSOLVE(2014,"牌桌已解散"),
    TABLE_PLAYER_NUM_LIMIT(2015,"牌桌人数上限"),
    CREAROR_CANNT_QUIT(2016,"创建者不能退出"),
    CLUB_TABLE_NOT_EMPTY(2017,"俱乐部牌桌上有玩家"),
    CLUB_ROOM_CARD_LESS(2018,"俱乐部房卡数量不足"),
    CLUB_TABLE_HAS_PLAYER(2019,"牌桌上有玩家"),
    BE_FREEZED(2020,"你已被冻结"),
    ROOM_INFO_TIME_OUT(2021,"房间信息已过时"),
    SUCCESS(200,"成功");

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
