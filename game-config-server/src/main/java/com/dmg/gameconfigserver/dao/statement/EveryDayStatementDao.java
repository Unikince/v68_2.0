package com.dmg.gameconfigserver.dao.statement;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.gameconfigserver.model.bean.statement.StatementEveryDay;
import com.dmg.gameconfigserver.model.vo.statement.everyday.EveryDayStatementCollectReq;
import com.dmg.gameconfigserver.model.vo.statement.everyday.EveryDayStatementCollectRes;
import com.dmg.gameconfigserver.model.vo.statement.everyday.EveryDayStatementDayDataReq;
import com.dmg.gameconfigserver.model.vo.statement.everyday.EveryDayStatementDayDataRes;

/** 每日报表 */
@DS("v68")
public interface EveryDayStatementDao extends BaseMapper<StatementEveryDay> {
    /**更新新用户数量*/
    void updateDataOfNewPlayerNum(@Param("beginDate") Date beginDate,@Param("endDate") Date endDate,@Param("dayStr") java.sql.Date dayStr);
    
    /**更新活跃用户数量*/
    void updateDataOfActivePlayerNum(@Param("dayStr") java.sql.Date dayStr);
    
    /**更新其他*/
    void updateDataOfOther(@Param("dayStr") java.sql.Date dayStr);

    /** 汇总报表 */
    EveryDayStatementCollectRes collect(@Param("reqVo") EveryDayStatementCollectReq reqVo);

    /** 每日数据 */
    IPage<EveryDayStatementDayDataRes> dayData(Page<EveryDayStatementDayDataRes> page,@Param("reqVo") EveryDayStatementDayDataReq reqVo);
}
