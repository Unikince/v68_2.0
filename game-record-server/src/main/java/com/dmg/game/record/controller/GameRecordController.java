package com.dmg.game.record.controller;

import com.dmg.common.core.web.Result;
import com.dmg.game.record.model.dto.GameRecordQueryDTO;
import com.dmg.game.record.service.GameRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:43 2019/11/29
 */
@Slf4j
@RestController
@RequestMapping("game-record-api/gameRecord")
public class GameRecordController {

    @Autowired
    private GameRecordService gameRecordService;

    @PostMapping("/getInfoList")
    public Result getInfoList(@RequestBody @Validated GameRecordQueryDTO vo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error("1000", bindingResult.getFieldError().getDefaultMessage());
        }
        log.info("{}", vo.toString());
        return Result.success(gameRecordService.getGameRecordInfo(vo));
    }

}
