package com.dmg.gameconfigserver.controller;

import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.annotation.NoAuthMapping;
import com.dmg.gameconfigserver.annotation.NoNeedLoginMapping;
import com.dmg.gameconfigserver.common.enums.GameStatusEnum;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.common.constant.ActionLogTemplateConstant;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.model.bean.GameInfoBean;
import com.dmg.gameconfigserver.model.dto.CommonRespDTO;
import com.dmg.gameconfigserver.model.dto.config.GameConfigDetailDTO;
import com.dmg.gameconfigserver.model.dto.GameInfoDTO;
import com.dmg.gameconfigserver.model.dto.sys.SysActionLogDTO;
import com.dmg.gameconfigserver.model.vo.GameFileVO;
import com.dmg.gameconfigserver.model.vo.GameInfoVO;
import com.dmg.gameconfigserver.service.config.GameInfoService;
import com.dmg.gameconfigserver.service.sys.SysActionLogService;
import com.dmg.server.common.enums.GameType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author mice
 * @Date 2019/9/27 14:43
 * @Version V1.0
 **/
@Slf4j
@RequestMapping(Constant.CONTEXT_PATH + "gameInfo")
@RestController
public class GameInfoController extends BaseController {

    @Autowired
    private GameInfoService gameInfoService;

    @Autowired
    private SysActionLogService sysActionLogService;


    @NoAuthMapping
    @GetMapping("list")
    public Result getGameInfoList(Integer gameType) {
        List<GameInfoDTO> gameInfoDTOS = gameInfoService.getGameInfoList(gameType);
        return Result.success(gameInfoDTOS);
    }

    @NoAuthMapping
    @GetMapping("detailList/{gameId}")
    public Result getGameConfigDetailList(@PathVariable("gameId") int gameId) {
        List<GameConfigDetailDTO> gameConfigDetailDTOS = gameInfoService.getGameConfigDetailList(gameId);
        return Result.success(gameConfigDetailDTOS);
    }

    @GetMapping("/status/getAll")
    public Result getGameStatus() {
        List<GameInfoVO> gameInfoDTOS = gameInfoService.getGameStatus();
        return Result.success(gameInfoDTOS);
    }

    @NoNeedLoginMapping
    @GetMapping("gameOpen")
    public Result getGameOpen() {
        List<GameInfoVO> gameInfoDTOS = gameInfoService.getGameOpen();
        return Result.success(gameInfoDTOS);
    }

    @NoNeedLoginMapping
    @GetMapping("detail/{gameId}")
    public Result getGameDetail(@PathVariable("gameId") int gameId) {
        GameInfoVO gameInfoVO = gameInfoService.getGameInfo(gameId);
        return Result.success(gameInfoVO);
    }

    @NoNeedLoginMapping
    @GetMapping("gameFile")
    public Result getGameDetail() {
        List<GameFileVO> gameFileVOList = gameInfoService.getGameFile();
        return Result.success(gameFileVOList);
    }

    @PostMapping("/status/updateOne")
    public Result updateOne(@RequestBody @Validated GameInfoVO gameInfoVO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }

        CommonRespDTO<GameInfoBean> respDTO = gameInfoService.changeGameStatus(Long.valueOf(gameInfoVO.getId()), gameInfoVO.getGameStatus());
        try {
            sysActionLogService.pushActionLog(SysActionLogDTO.builder()
                    .actionDesc(String.format(ActionLogTemplateConstant.CHANGE_GAME_STATUS, GameType.getValueByKey(respDTO.getData().getGameId()),
                            GameStatusEnum.getMsg(respDTO.getData().getGameStatus()), GameStatusEnum.getMsg(gameInfoVO.getGameStatus())))
                    .createUser(this.getUserId())
                    .loginIp(getIpAddr()).build());
        } catch (Exception e) {
            log.error("{}", e);
        }
        return Result.success();
    }

}