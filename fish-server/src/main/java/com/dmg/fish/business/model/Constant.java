package com.dmg.fish.business.model;

/**
 * 常量
 */
public interface Constant {
    /** 切换场景延时5秒,每次刷场景的延时(客户端有个场景切换动画) */
    int SWITCH_SCENCE_DELAY = 5000;
    /** 发炮cd毫秒 */
    int FIRE_CD = 50;
    /** 鱼基础死亡分子 */
    long FISH_BASE_DIE_NUMERATOR = 9500;
    /** 鱼基础死亡分母 */
    long FISH_FULL_DIE_DENOMINATOR = 10000;
}
