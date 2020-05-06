package com.dmg.gameconfigserver.controller.home;

import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.annotation.NoAuthMapping;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.controller.BaseController;
import com.dmg.gameconfigserver.model.dto.home.GamePumpRecordDTO;
import com.dmg.gameconfigserver.model.vo.home.ConditionDetailVO;
import com.dmg.gameconfigserver.service.config.GameFileConfigService;
import com.dmg.gameconfigserver.service.config.GameInfoService;
import com.dmg.gameconfigserver.service.home.GamePumpRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 16:48 2020/3/16
 */
@Slf4j
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "home/pump")
public class GamePumpRecordController extends BaseController {

    @Autowired
    private GamePumpRecordService gamePumpRecordService;

    @Autowired
    private GameInfoService gameInfoService;

    @Autowired
    private GameFileConfigService gameFileConfigService;

    @NoAuthMapping
    @GetMapping("/getCondition")
    public Result getCondition() {
        List<ConditionDetailVO> conditionDetailVOList = new ArrayList<>();
        gameInfoService.getGameInfoList(null).forEach(gameInfoDTO -> {
            ConditionDetailVO conditionDetailVO = new ConditionDetailVO();
            conditionDetailVO.setCode(gameInfoDTO.getGameId());
            conditionDetailVO.setValue(gameInfoDTO.getGameName());
            gameFileConfigService.getFileDetailByGameId(gameInfoDTO.getGameId()).forEach(fileBaseConfigBean -> {
                conditionDetailVO.getFiles().add(ConditionDetailVO.Detail.builder().code(fileBaseConfigBean.getFileId()).value(fileBaseConfigBean.getFileName()).build());
            });
            conditionDetailVOList.add(conditionDetailVO);
        });
        return Result.success(conditionDetailVOList);
    }

    @NoAuthMapping
    @PostMapping("/getList")
    public Result getAllList(@RequestBody @Validated GamePumpRecordDTO gamePumpRecordDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        return Result.success(gamePumpRecordService.getGamePumpRecordList(gamePumpRecordDTO));
    }

    @NoAuthMapping
    @GetMapping("/game/getAll")
    public Result getDetailList() {
        return Result.success(gamePumpRecordService.getGamePumpDetail());
    }

    @NoAuthMapping
    @GetMapping("/file/getAll/{gameId}")
    public Result getFileDetailList(@PathVariable("gameId") Integer gameId) {
        return Result.success(gamePumpRecordService.getGameFilePumpDetail(gameId));
    }
}
