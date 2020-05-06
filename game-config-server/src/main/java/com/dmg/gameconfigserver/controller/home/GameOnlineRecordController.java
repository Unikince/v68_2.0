package com.dmg.gameconfigserver.controller.home;

import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.annotation.NoAuthMapping;
import com.dmg.gameconfigserver.annotation.NoNeedLoginMapping;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.controller.BaseController;
import com.dmg.gameconfigserver.model.bean.config.FileBaseConfigBean;
import com.dmg.gameconfigserver.model.dto.home.GameOnlineRecordDTO;
import com.dmg.gameconfigserver.model.vo.home.ConditionDetailVO;
import com.dmg.gameconfigserver.service.config.GameFileConfigService;
import com.dmg.gameconfigserver.service.config.GameInfoService;
import com.dmg.gameconfigserver.service.home.GameOnlineRecordService;
import com.dmg.server.common.enums.FilePlaceEnum;
import com.dmg.server.common.enums.PlaceEnum;
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
@RequestMapping(Constant.CONTEXT_PATH + "home/online")
public class GameOnlineRecordController extends BaseController {

    @Autowired
    private GameOnlineRecordService gameOnlineRecordService;

    @Autowired
    private GameInfoService gameInfoService;

    @Autowired
    private GameFileConfigService gameFileConfigService;

    @NoAuthMapping
    @NoNeedLoginMapping
    @GetMapping("/getCondition")
    public Result getCondition() {
        List<ConditionDetailVO> conditionDetailVOList = new ArrayList<>();
        ConditionDetailVO lobby = new ConditionDetailVO();
        lobby.setCode(PlaceEnum.CODE_LOBBY.getCode());
        lobby.setValue(PlaceEnum.CODE_LOBBY.getMsg());
        conditionDetailVOList.add(lobby);
        gameInfoService.getGameInfoList(null).forEach(gameInfoDTO -> {
            ConditionDetailVO conditionDetailVO = new ConditionDetailVO();
            conditionDetailVO.setCode(gameInfoDTO.getGameId());
            conditionDetailVO.setValue(gameInfoDTO.getGameName());
            List<FileBaseConfigBean> fileBaseConfigBeanList = gameFileConfigService.getFileDetailByGameId(gameInfoDTO.getGameId());
            if (fileBaseConfigBeanList != null) {
                fileBaseConfigBeanList.forEach(fileBaseConfigBean -> {
                    conditionDetailVO.getFiles().add(ConditionDetailVO.Detail.builder().code(fileBaseConfigBean.getFileId()).value(fileBaseConfigBean.getFileName()).build());
                });
                conditionDetailVOList.add(conditionDetailVO);
            }
        });

        return Result.success(conditionDetailVOList);
    }

    @NoAuthMapping
    @PostMapping("/getList")
    public Result getAllList(@RequestBody @Validated GameOnlineRecordDTO gameOnlineRecordDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        return Result.success(gameOnlineRecordService.getGameOnlineRecordList(gameOnlineRecordDTO));
    }

    @NoAuthMapping
    @GetMapping("/game/getAll")
    public Result getDetailList() {
        return Result.success(gameOnlineRecordService.getGameOnlineDetail());
    }

    @NoAuthMapping
    @GetMapping("/file/getAll/{gameId}")
    public Result getFileDetailList(@PathVariable("gameId") Integer gameId) {
        return Result.success(gameOnlineRecordService.getGameFileOnlineDetail(gameId));
    }
}
