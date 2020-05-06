package com.dmg.gameconfigserver.service.statement.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.gameconfigserver.dao.record.GameRecordDao;
import com.dmg.gameconfigserver.model.bean.record.GameRecordBean;
import com.dmg.gameconfigserver.model.dto.statement.RobotStatementDTO;
import com.dmg.gameconfigserver.model.dto.statement.RobotStatementExportDTO;
import com.dmg.gameconfigserver.model.vo.statement.robot.RobotDataStatementVO;
import com.dmg.gameconfigserver.model.bo.RobotStatementBO;
import com.dmg.gameconfigserver.model.vo.statement.robot.RobotStatementVO;
import com.dmg.gameconfigserver.service.statement.RobotStatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


/**
 * @Author liubo
 * @Description //TODO
 * @Date 11:03 2020/1/6
 */
@Service
public class RobotStatementServiceImpl implements RobotStatementService {

    @Autowired
    private GameRecordDao gameRecordDao;

    @Override
    public RobotStatementVO getRobotStatement(RobotStatementDTO robotStatementDTO) {
        Page<GameRecordBean> pageParam = new Page<>(robotStatementDTO.getCurrent(), robotStatementDTO.getSize()); // 当前页码，每页条数
        pageParam.setSearchCount(false);
        pageParam.setTotal(gameRecordDao.getCountRobotStatement(robotStatementDTO.getStartDate(), robotStatementDTO.getEndDate()));
        IPage<RobotDataStatementVO> robotDataStatementVOIPage = gameRecordDao.getRobotStatement(pageParam, robotStatementDTO.getStartDate(), robotStatementDTO.getEndDate());
        RobotStatementVO robotStatementVO = new RobotStatementVO();
        robotStatementVO.setDatas(robotDataStatementVOIPage);
        RobotDataStatementVO robotDataStatementVO = gameRecordDao.getRobotSumStatement(robotStatementDTO.getStartDate(), robotStatementDTO.getEndDate());
        robotStatementVO.setSumBet(robotDataStatementVO.getSumBet());
        robotStatementVO.setSumWin(robotDataStatementVO.getSumWin());
        robotStatementVO.setSumPay(robotDataStatementVO.getSumPay());
        robotStatementVO.setReturnRate(robotStatementVO.getReturnRate().divide(BigDecimal.valueOf(robotDataStatementVOIPage.getTotal()), 6));
        return robotStatementVO;
    }

    @Override
    public List<RobotStatementBO> exportRobotStatement(RobotStatementExportDTO robotStatementExportDTO) {
        return gameRecordDao.exportRobotStatement(robotStatementExportDTO.getStartDate(), robotStatementExportDTO.getEndDate());
    }
}
