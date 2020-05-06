package com.dmg.gameconfigserver.common.constant;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:07 2019/10/10
 */
public class UserControlConstant {
	 /**
     * 系统控制员
     */
    public static final String ADMIN = "admin";
    /**
     * 点控状态
     */
    public static final Integer CONTROL_STATE_POINT = 1;
    /**
     * 自控状态
     */
    public static final Integer CONTROL_STATE_AUTO = 2;
    
    
    /**
     * 正常
     */
    public static final Integer CONTROL_NORMAL = 10;
    /**
     * 控制赢
     */
    // 赢
    public static final Integer CONTROL_WIN = 20; 
    // 点控赢
    public static final Integer CONTROL_WIN1 = 21; 
    // 自控赢
    public static final Integer CONTROL_WIN2= 22; 
    /**
     * 控制输
     */
    // 输
    public static final Integer CONTROL_LOSE = 30;
    // 点控输
    public static final Integer CONTROL_LOSE1 = 31;
    // 自控输
    public static final Integer CONTROL_LOSE2 = 32;
}
