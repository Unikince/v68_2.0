package com.dmg.gameconfigserver.controller.config;

import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.model.dto.config.GameControlConfigDTO;
import com.dmg.gameconfigserver.model.vo.config.GameControlVO;
import com.dmg.gameconfigserver.model.vo.group.SaveValid;
import com.dmg.gameconfigserver.model.vo.group.UpdateValid;
import com.dmg.gameconfigserver.service.config.GameControlConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @Author liubo
 * @Description //TODO
 * @Date 11:24 2019/10/10
 */
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "game/config/gameControl")
public class GameControlController {

    @Autowired
    private GameControlConfigService gameControlConfigService;

    @GetMapping("/getOne/{fileBaseId}")
    public Result getInfoById(@PathVariable("fileBaseId") Integer fileBaseId) {
        if (fileBaseId == null) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), "fileBaseId cannot be empty");
        }
        GameControlConfigDTO gameControlConfigDTO = gameControlConfigService.getGameControlByBaseId(fileBaseId);
        return Result.success(gameControlConfigDTO);
    }

    @PostMapping("/saveOne")
    public Result saveOne(@RequestBody @Validated({SaveValid.class}) GameControlVO vo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        gameControlConfigService.insert(vo);
        return Result.success();
    }

    @PostMapping("/updateOne")
    public Result updateOne(@RequestBody @Validated({UpdateValid.class}) GameControlVO vo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        gameControlConfigService.update(vo);
        return Result.success();
    }

    @GetMapping("/delete/{fileBaseId}")
    public Result delete(@PathVariable("fileBaseId") Integer fileBaseId) {
        if (fileBaseId == null) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), "fileBaseId cannot be empty");
        }
        gameControlConfigService.deleteById(fileBaseId);
        return Result.success();
    }
}
