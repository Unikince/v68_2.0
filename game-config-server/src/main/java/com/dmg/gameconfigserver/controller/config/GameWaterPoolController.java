package com.dmg.gameconfigserver.controller.config;

import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.annotation.NoAuthMapping;
import com.dmg.gameconfigserver.annotation.NoNeedLoginMapping;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.model.bean.config.GameWaterPoolConfigBean;
import com.dmg.gameconfigserver.model.dto.config.GameWaterPoolByWaterDTO;
import com.dmg.gameconfigserver.model.vo.config.GameWaterPoolConfigVO;
import com.dmg.gameconfigserver.model.vo.group.SaveValid;
import com.dmg.gameconfigserver.model.vo.group.UpdateValid;
import com.dmg.gameconfigserver.service.config.GameWaterPoolConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO 游戏水池配置控制器
 * @Date 17:21 2019/9/27
 */
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "game/config/gameWaterPool")
public class GameWaterPoolController {

    @Autowired
    private GameWaterPoolConfigService gameWaterPoolConfigService;

    @GetMapping("/getAll")
    public Result getAllList() {
        List<GameWaterPoolConfigBean> gameWaterPoolConfigBeanList = gameWaterPoolConfigService.getAllList();
        return Result.success(gameWaterPoolConfigBeanList);
    }

    @GetMapping("/getList")
    public Result getAllList(@RequestParam("gameId") Integer gameId, @RequestParam("fileId") Integer fileId) {
        if (gameId == null) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), "gameId cannot be empty");
        }
        if (fileId == null) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), "fileId cannot be empty");
        }
        List<GameWaterPoolConfigBean> gameWaterPoolConfigBeanList = gameWaterPoolConfigService.getGameWaterPoolByGame(gameId, fileId);
        return Result.success(gameWaterPoolConfigBeanList);
    }

    @GetMapping("/getOne/{id}")
    public Result getInfoById(@PathVariable("id") Integer id) {
        if (id == null) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), "id cannot be empty");
        }
        GameWaterPoolConfigBean gameWaterPoolConfigBean = gameWaterPoolConfigService.getGameWaterPoolById(id);
        return Result.success(gameWaterPoolConfigBean);
    }

    @NoNeedLoginMapping
    @PostMapping("/getInfoByWater")
    public Result getInfoByWater(@RequestBody @Validated GameWaterPoolByWaterDTO gameWaterPoolByWaterDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        GameWaterPoolConfigBean gameWaterPoolConfigBean = gameWaterPoolConfigService.getInfoByWater(gameWaterPoolByWaterDTO);
        return Result.success(gameWaterPoolConfigBean);
    }

    @NoAuthMapping
    @GetMapping("/getInfoByGame")
    public Result getInfoByGame(@RequestParam("gameId") Integer gameId, @RequestParam("fileId") Integer fileId) {
        if (gameId == null) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), "gameId cannot be empty");
        }
        if (fileId == null) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), "fileId cannot be empty");
        }
        List<GameWaterPoolConfigBean> gameWaterPoolConfigBeanList = gameWaterPoolConfigService.getGameWaterPoolByGame(gameId, fileId);
        return Result.success(gameWaterPoolConfigBeanList);
    }

    @NoNeedLoginMapping
    @GetMapping("/getInfoByGame/{gameId}")
    public Result getInfoByGame(@PathVariable("gameId") Integer gameId) {
        if (gameId == null) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), "gameId cannot be empty");
        }
        List<GameWaterPoolConfigBean> gameWaterPoolConfigBeanList = gameWaterPoolConfigService.getGameWaterPoolByGame(gameId);
        return Result.success(gameWaterPoolConfigBeanList);
    }

    @PostMapping("/saveOne")
    public Result saveOne(@RequestBody @Validated({SaveValid.class}) GameWaterPoolConfigVO vo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        GameWaterPoolConfigBean gameWaterPoolConfigBean = new GameWaterPoolConfigBean();
        BeanUtils.copyProperties(vo, gameWaterPoolConfigBean);
        gameWaterPoolConfigService.insert(gameWaterPoolConfigBean);
        return Result.success();
    }

    @PostMapping("/updateOne")
    public Result updateOne(@RequestBody @Validated({UpdateValid.class}) GameWaterPoolConfigVO vo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        gameWaterPoolConfigService.update(vo);
        return Result.success();
    }

    @GetMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Integer id) {
        if (id == null) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), "id cannot be empty");
        }
        gameWaterPoolConfigService.deleteById(id);
        return Result.success();
    }
}
