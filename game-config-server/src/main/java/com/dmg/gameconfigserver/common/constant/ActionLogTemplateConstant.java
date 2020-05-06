package com.dmg.gameconfigserver.common.constant;

/**
 * @Author liubo
 * @Description //TODO 系统操作日志模板
 * @Date 16:00 2019/12/26
 */
public class ActionLogTemplateConstant {

    /*
     * 登陆系统用户
     */
    public static String SYS_USER_LOGIN = "登陆成功";
    /**
     * 修改用户信息
     */
    public static String SYS_USER_UPDATE = "将用户：%s的%配置由%修改为%s";
    /**
     * 修改角色权限
     */
    public static String SYS_USER_ROLE_UPDATE = "将角色：%s的%s配置由%修改为%s";
    /**
     * 修改白名单
     */
    public static String SYS_WHITE_UPDATE = "将地址：%s的%s配置由%修改为%s";
    /*
     * 修改玩家登陆密码
     */
    public static String UPDATE_USER_LOGIN_PASSWORD = "为玩家%s重置了登录密码";
    /*
     * 修改玩家提现密码
     */
    public static String UPDATE_USER_CASH__PASSWORD = "为玩家%s重置了提现密码";
    /**
     * 审核提现订单
     */
    public static String REVIEW_WITHDRAW_ORDER = "审核%s了提现订单：%s";
    /**
     * 处理提现问题订单
     */
    public static String DEAL_ABNORMAL_ORDER = "将提现订单：%s的状态修改为%s";
    /**
     * 修改游戏状态
     */
    public static String CHANGE_GAME_STATUS = "将%s游戏的游戏状态由%s修改为%s";


}
