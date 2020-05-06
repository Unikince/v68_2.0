package com.dmg.gameconfigserver.service.statement;

import com.dmg.gameconfigserver.model.dto.statement.RobotStatementDTO;
import com.dmg.gameconfigserver.model.dto.statement.RobotStatementExportDTO;
import com.dmg.gameconfigserver.model.bo.RobotStatementBO;
import com.dmg.gameconfigserver.model.vo.statement.robot.RobotStatementVO;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:47 2020/1/6
 */
public interface RobotStatementService {
    /**
     * @Author liubo
     * @Description //TODO 查询机器人报表数据
     * @Date 11:02 2020/1/6
     **/
    RobotStatementVO getRobotStatement(RobotStatementDTO robotStatementDTO);

    /**
     * @Author liubo
     * @Description //TODO 机器人报表数据导出
     * @Date 15:50 2020/1/6
     **/
    List<RobotStatementBO> exportRobotStatement(RobotStatementExportDTO robotStatementExportDTO);
}
