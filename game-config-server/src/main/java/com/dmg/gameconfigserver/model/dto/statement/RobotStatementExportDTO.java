package com.dmg.gameconfigserver.model.dto.statement;

import lombok.Data;

import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 11:02 2020/1/6
 */
@Data
public class RobotStatementExportDTO {
    //查询起期
    private Date startDate;
    //查询止期
    private Date endDate;
}
