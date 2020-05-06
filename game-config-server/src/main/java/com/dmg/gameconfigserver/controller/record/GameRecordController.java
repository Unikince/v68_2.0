package com.dmg.gameconfigserver.controller.record;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.controller.BaseController;
import com.dmg.gameconfigserver.model.bean.record.GameRecordBean;
import com.dmg.gameconfigserver.model.dto.record.GameRecordDTO;
import com.dmg.gameconfigserver.service.record.GameRecordService;
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
 * @Date 15:17 2019/11/20
 */
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "gameRecord/chess/")
public class GameRecordController extends BaseController {

    @Autowired
    private GameRecordService gameRecordService;

    @PostMapping("/getList")
    public Result getAllList(@RequestBody @Validated GameRecordDTO gameRecordDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        IPage<GameRecordBean> gameRecord = gameRecordService.getGameRecord(gameRecordDTO);
        return Result.success(gameRecord);
    }


}
