package com.dmg.gameconfigserver.controller.statement;

import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.common.util.ExcelUtil;
import com.dmg.gameconfigserver.controller.BaseController;
import com.dmg.gameconfigserver.model.dto.statement.RobotStatementDTO;
import com.dmg.gameconfigserver.model.dto.statement.RobotStatementExportDTO;
import com.dmg.gameconfigserver.model.bo.RobotStatementBO;
import com.dmg.gameconfigserver.service.statement.RobotStatementService;
import com.dmg.server.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 机器人统计报表
 */
@Slf4j
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "statement/robot")
public class RobotStatementController extends BaseController {

    @Autowired
    private RobotStatementService robotStatementService;

    private static final String fileName = "机器人统计报表";

    @PostMapping("/getList")
    @SuppressWarnings("rawtypes")
    public Result getAllList(@RequestBody RobotStatementDTO robotStatementDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        return Result.success(robotStatementService.getRobotStatement(robotStatementDTO));
    }

    @PostMapping("/export")
    public void export(@RequestBody RobotStatementExportDTO robotStatementExportDTO) {
        if (robotStatementExportDTO == null) {
            robotStatementExportDTO = new RobotStatementExportDTO();
        }
        List<RobotStatementBO> robotStatementBOList = robotStatementService.exportRobotStatement(robotStatementExportDTO);
        try {
            ExcelUtil.writeExcel(getResponse(), robotStatementBOList, fileName.concat(DateUtils.getDate("yyyyMMddHHmmss")), fileName, RobotStatementBO.class);
        } catch (Exception e) {
            log.error("导出机器人统计报表出现异常:{}", e);
        }

    }
}
