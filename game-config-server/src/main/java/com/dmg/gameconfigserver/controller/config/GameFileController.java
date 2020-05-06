package com.dmg.gameconfigserver.controller.config;

import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.annotation.NoNeedLoginMapping;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.model.bean.config.FileBaseConfigBean;
import com.dmg.gameconfigserver.model.bean.config.GameFileBean;
import com.dmg.gameconfigserver.model.dto.config.GameFileDTO;
import com.dmg.gameconfigserver.model.vo.config.GameFileConfigVO;
import com.dmg.gameconfigserver.model.vo.group.SaveValid;
import com.dmg.gameconfigserver.model.vo.group.UpdateValid;
import com.dmg.gameconfigserver.service.config.GameFileConfigService;
import com.dmg.server.common.util.DmgBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 11:24 2019/10/10
 */
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "game/config/gameFile")
public class GameFileController {

    @Autowired
    private GameFileConfigService gameFileConfigService;

    @NoNeedLoginMapping
    @GetMapping("/getGameFileByGameId")
    public Result getGameFileByGameId(@RequestParam("gameId") Integer gameId) {
        if (gameId == null) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), "gameId cannot be empty");
        }
        List<GameFileDTO> gameFileDTOList = gameFileConfigService.getGameFileByGameId(gameId);
        return Result.success(gameFileDTOList);
    }

    @GetMapping("/getOne/{fileBaseId}")
    public Result getInfoById(@PathVariable("fileBaseId") Integer fileBaseId) {
        if (fileBaseId == null) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), "fileBaseId cannot be empty");
        }
        GameFileDTO gameFileDTO = gameFileConfigService.getGameFileByBaseId(fileBaseId);
        return Result.success(gameFileDTO);
    }

    @PostMapping("/saveOne")
    public Result saveOne(@RequestBody @Validated({SaveValid.class}) GameFileConfigVO vo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        FileBaseConfigBean fileBaseConfigBean = new FileBaseConfigBean();
        GameFileBean gameFileBean = new GameFileBean();
        DmgBeanUtils.copyProperties(vo, fileBaseConfigBean);
        DmgBeanUtils.copyProperties(vo, gameFileBean);
        gameFileConfigService.insert(fileBaseConfigBean, gameFileBean);
        return Result.success();
    }

    @PostMapping("/updateOne")
    public Result updateOne(@RequestBody @Validated({UpdateValid.class}) GameFileConfigVO vo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        FileBaseConfigBean fileBaseConfigBean = new FileBaseConfigBean();
        GameFileBean gameFileBean = new GameFileBean();
        DmgBeanUtils.copyProperties(vo, fileBaseConfigBean);
        DmgBeanUtils.copyProperties(vo, gameFileBean);
        gameFileConfigService.update(fileBaseConfigBean, gameFileBean);
        return Result.success();
    }

    @GetMapping("/delete/{fileBaseId}")
    public Result delete(@PathVariable("fileBaseId") Integer fileBaseId) {
        if (fileBaseId == null) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), "fileBaseId cannot be empty");
        }
        gameFileConfigService.deleteById(fileBaseId);
        return Result.success();
    }
}
