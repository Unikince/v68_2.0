package com.dmg.gameconfigserver.controller.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.controller.BaseController;
import com.dmg.gameconfigserver.model.dto.user.AccountChangeLogDTO;
import com.dmg.gameconfigserver.model.vo.user.AccountChangeLogVO;
import com.dmg.gameconfigserver.service.config.GameInfoService;
import com.dmg.gameconfigserver.service.user.AccountChangeLogService;
import com.dmg.server.common.enums.AccountChangeTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 15:17 2019/11/20
 */
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "member/user/account")
public class AccountChangeLogController extends BaseController {

    @Autowired
    private AccountChangeLogService accountChangeLogService;

    @Autowired
    private GameInfoService gameInfoService;

    @PostMapping("/getList")
    public Result getAllList(@RequestBody @Validated AccountChangeLogDTO accountChangeLogDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        IPage<AccountChangeLogVO> accountChangeLogList = accountChangeLogService.getAccountChangeLog(accountChangeLogDTO);
        return Result.success(accountChangeLogList);
    }

    @GetMapping("/getTypeAll")
    public Result getTypeAll() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AccountChangeTypeEnum type : AccountChangeTypeEnum.values()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", type.getCode());
            map.put("name", type.getMsg());
            list.add(map);
        }
        gameInfoService.getGameOpen().forEach(gameInfoVO -> {
            Map<String, Object> mapGame = new HashMap<>();
            mapGame.put("id", gameInfoVO.getGameId());
            mapGame.put("name", gameInfoVO.getGameName());
            list.add(mapGame);
        });
        return Result.success(list);
    }
}
