package com.dmg.gameconfigserver.model.vo.statement.everyday;

import com.dmg.gameconfigserver.model.vo.statement.everyday.common.EveryDayStatementCommonRes;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 每日报表_每日数据_返回
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EveryDayStatementDayDataRes extends EveryDayStatementCommonRes {
    /** 日期字符串 */
    private String dayStr;
    /** 新增人数 */
    private int newPlayerNum;
    /** 活跃人数 */
    private int activePlayerNum;
}