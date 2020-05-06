package com.dmg.gameconfigserver.dao.record;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.gameconfigserver.model.bean.record.GameRecordBean;
import com.dmg.gameconfigserver.model.vo.statement.robot.RobotDataStatementVO;
import com.dmg.gameconfigserver.model.bo.RobotStatementBO;

import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 11:57 2019/11/20
 */
@DS("v68")
public interface GameRecordDao extends BaseMapper<GameRecordBean> {

    IPage<RobotDataStatementVO> getRobotStatement(Page page, @Param("startDate") Date startDate,
                                                  @Param("endDate") Date endDate);

    Long getCountRobotStatement(@Param("startDate") Date startDate,
                                @Param("endDate") Date endDate);

    RobotDataStatementVO getRobotSumStatement(@Param("startDate") Date startDate,
                                              @Param("endDate") Date endDate);

    List<RobotStatementBO> exportRobotStatement(@Param("startDate") Date startDate,
                                                @Param("endDate") Date endDate);

    BigDecimal getTodayServiceCharge();

    BigDecimal getTodayProfit();
}
